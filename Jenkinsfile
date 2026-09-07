pipeline {
    agent { label 'Jenkins-Agent' }

    tools {
        jdk 'Java21'
        maven 'Maven3'
    }

    environment {
        APP_NAME    = "eddie-register-app"
        RELEASE     = "1.0.0"
        IMAGE_NAME  = "eddiebyte/${APP_NAME}"
        IMAGE_TAG   = "${RELEASE}-${BUILD_NUMBER}"
        TRIVY_IMAGE = "aquasec/trivy:0.69.3"
    }

    stages {
        stage("Cleanup Workspace") {
            steps { cleanWs() }
        }

        stage("Checkout from SCM") {
            steps {
                git branch: 'main',
                    credentialsId: 'github',
                    url: 'https://github.com/EddieByte/eddie-register-app'
            }
        }

        stage("Build & Test") {
            steps {
                sh "mvn clean package"
                sh "mvn test"
            }
        }

        stage("SonarQube Analysis") {
            steps {
                script {
                    withSonarQubeEnv('sonarqube-server') {
                        sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar"
                    }
                    timeout(time: 2, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage("Docker Build") {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage("Trivy Scan") {
            steps {
                script {
                    sh """
                        docker run --rm \
                            -e DOCKER_API_VERSION=1.44 \
                            -v /var/run/docker.sock:/var/run/docker.sock \
                            ${TRIVY_IMAGE} image \
                            --severity HIGH,CRITICAL \
                            --exit-code 0 \
                            ${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage("Docker Login & Push") {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'DockerHub',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh """
                            echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                            docker push ${IMAGE_NAME}:${IMAGE_TAG}
                            docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                            docker push ${IMAGE_NAME}:latest
                        """
                    }
                }
            }
        }

        stage("Update Manifests") {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'github',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_PASS'
                    )]) {
                        sh """
                            # Ensure kustomize is available
                            if ! command -v kustomize &> /dev/null; then
                                echo "kustomize not found globally. Installing local binary..."
                                curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh" | bash
                                KUSTOMIZE_CMD="./kustomize"
                            else
                                KUSTOMIZE_CMD="kustomize"
                            fi

                            rm -rf eddie-register-app-manifests
                            git clone https://github.com/EddieByte/eddie-register-app-manifests.git
                            cd eddie-register-app-manifests/overlays/dev
                            
                            # Execute kustomize via system path or local binary
                            ../..\$/KUSTOMIZE_CMD edit set image ${IMAGE_NAME}=${IMAGE_NAME}:${IMAGE_TAG}
                            
                            git config user.email "jenkins@eddiebyte.com"
                            git config user.name "Jenkins CI"
                            git add kustomization.yaml
                            git commit -m "Update dev image to ${IMAGE_TAG} [skip ci]"
                            git push https://\$GIT_USER:\$GIT_PASS@github.com/EddieByte/eddie-register-app-manifests.git main
                        """
                    }
                }
            }
        }

        stage('Cleanup Artifacts') {
            steps {
                script {
                    sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
                    sh "docker rmi ${IMAGE_NAME}:latest || true"
                }
            }
        }
    }
}