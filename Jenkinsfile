pipeline {
    agent { label 'Jenkins-Agent' }
    
    triggers {
        githubPush()
    }

    parameters {
        string(name: 'APP_NAME', defaultValue: 'eddie-register-app', description: 'Application Name')
        string(name: 'RELEASE_VERSION', defaultValue: '1.0.0', description: 'Base release version')
        string(name: 'DOCKER_USER', defaultValue: 'eddiebyte', description: 'Docker Hub Namespace/Username')
        string(name: 'GITHUB_USER', defaultValue: 'EddieByte', description: 'GitHub Username/Org')
        string(name: 'TARGET_BRANCH', defaultValue: 'main', description: 'Branch to target for manifests')
        
        string(name: 'DOCKER_CRED_ID', defaultValue: 'DockerHub', description: 'Jenkins Credentials ID for Docker')
        string(name: 'GIT_CRED_ID', defaultValue: 'github', description: 'Jenkins Credentials ID for Git')
    }

    tools {
        jdk 'Java21'
        maven 'Maven3'
    }

    environment {
        // Explicitly map params to environment variables for shell access
        APP_NAME           = "${params.APP_NAME}"
        RELEASE_VERSION    = "${params.RELEASE_VERSION}"
        DOCKER_USER        = "${params.DOCKER_USER}"
        GITHUB_USER        = "${params.GITHUB_USER}"
        TARGET_BRANCH      = "${params.TARGET_BRANCH}"
        
        IMAGE_NAME         = "${params.DOCKER_USER}/${params.APP_NAME}"
        IMAGE_TAG          = "${params.RELEASE_VERSION}-${BUILD_NUMBER}"
        TRIVY_IMAGE        = "aquasec/trivy:0.69.3"
        MANIFEST_REPO_NAME = "${params.APP_NAME}-manifests"
    }

    stages {
        stage("Cleanup Workspace") {
            steps { cleanWs() }
        }

        stage("Checkout from SCM") {
            steps { checkout scm }
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
                        credentialsId: params.DOCKER_CRED_ID,
                        usernameVariable: 'DH_USER',
                        passwordVariable: 'DH_PASS'
                    )]) {
                        sh '''
                            echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
                            docker push "${IMAGE_NAME}:${IMAGE_TAG}"
                            docker tag "${IMAGE_NAME}:${IMAGE_TAG}" "${IMAGE_NAME}:latest"
                            docker push "${IMAGE_NAME}:latest"
                        '''
                    }
                }
            }
        }

        stage("Update Manifests") {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: params.GIT_CRED_ID,
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_PASS'
                    )]) {
                        sh '''
                            # Install kustomize if not present
                            if ! command -v kustomize &> /dev/null; then
                                echo "kustomize not found globally. Installing local binary..."
                                curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh" | bash
                                export PATH="$PATH:$(pwd)"
                            fi

                            rm -rf "$MANIFEST_REPO_NAME"
                            git clone "https://github.com/${GITHUB_USER}/${MANIFEST_REPO_NAME}.git"
                            cd "${MANIFEST_REPO_NAME}/overlays/dev"

                            # Update manifest image tag
                            kustomize edit set image "${IMAGE_NAME}=${IMAGE_NAME}:${IMAGE_TAG}"

                            git config user.email "jenkins@eddiebyte.com"
                            git config user.name "Jenkins CI"
                            git add kustomization.yaml
                            git commit -m "Update dev image to ${IMAGE_TAG} [skip ci]" || echo "No changes to commit"
                            
                            # Push using environment variable resolution
                            git push "https://${GIT_USER}:${GIT_PASS}@github.com/${GITHUB_USER}/${MANIFEST_REPO_NAME}.git" "${TARGET_BRANCH}"
                        '''
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