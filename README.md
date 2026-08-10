# Register App

A small Java Maven multi-module web application that builds a server module and a web layer packaged as a WAR.

## Build and Test Status

The current workspace has been validated with Maven test execution:

- `mvn test` completed successfully in the main project reactor.
- Reactor summary reported `BUILD SUCCESS` for the server and webapp modules.
- The server test suite executed with `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0` in the source-layout reactor.

## Environment Requirements

The project is designed around the following runtime expectations:

- JDK 17 runtime is installed for the Java IDE/runtime surface and can satisfy Java SE 17 execution environment discovery.
- Maven runs on the host JDK discovered by the command line, which is currently Java 24.0.1 in this environment (`mvn -version` evidence).
- The build compiler configuration is set to Java 21 (`maven.compiler.source` and `maven.compiler.target` are `21` in the root POM).
- The web container image referenced in the Docker build uses Tomcat 9 with JDK 17 (`FROM tomcat:9.0-jdk17`).
- Maven 3.9.x is the version used by the current workspace command-line runtime.

## Runtime & Tooling Notes

- Use Maven from the project root to run the build lifecycle and tests.
- The IDE may report Java build path warnings if VS Code’s Java configuration has not refreshed after JDK installation.
- The project uses Maven multi-module build semantics from the root POM.

## Troubleshooting

If you see a Java build-environment message such as `Build path specifies execution environment JavaSE-17`:

1. Confirm the Java 17 JDK is installed and visible to the system.
2. Refresh the Java tooling from the VS Code command palette using `Java: Clean Java Language Server Workspace`.
3. Reload the workspace if the Java extension reports stale classpath or JRE discovery details.
4. Re-run `mvn test` from the repository root to confirm Maven remains the source of truth for compilation and test execution.

When a JDK is installed manually, the Java extension may need a workspace settings refresh so it can discover the new runtime.

