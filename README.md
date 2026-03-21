# store-engine

Parent repository for the store-engine multi-module project.

> Small, focused README with build/run/development instructions. Generated from the project's POM metadata.

---

## Quick facts

- GroupId: `az.kon.academy`
- Root artifactId: `store-engine` (packaging: `pom`)
- Parent/module artifact: `store-engine-parent` (packaging: `jar`)
- Version: `1.0.0`
- Java: `25` (configured in parent POM)
- Spring Boot: `4.0.4`

Contact: Rovshan Aghayev <rovsenaqazadeh@gmail.com>

---

## Table of contents

- [Requirements](#requirements)
- [Build](#build)
- [Run](#run)
- [Modules](#modules)
- [Development / Contributing](#development--contributing)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

---

## Requirements

- Java 25 (JDK 25)
- Maven 3.15.0 (Maven command-line)
- Network access for external Maven repositories (the POM references Confluent repo and Spring BOMs)

Verify your environment:

```bash
mvn -v
java -version
```

---

## Build

From the repository root you can build everything with a single command (uses Maven):

```bash
# parallel build using 1 core per thread (adjust -T value if you want)
mvn -T 1C clean install
```

Build a single module and its dependencies:

```bash
# build only the store-engine-parent module and its required modules
mvn -pl shared/store-engine-parent -am clean package
```

Notes:
- The parent/module `store-engine-parent` has version `1.0.0` and is packaged as a JAR.
- The project uses the `spring-boot-maven-plugin` which may repackage application modules into runnable jars.

---

## Run

If a module produces an executable jar (Spring Boot application), you can run it like this (example for the parent module):

```bash
# example path — replace with the module you built if different
java -jar shared/store-engine-parent/target/store-engine-parent-1.0.0.jar
```

If the module is not an application (library), include it as a dependency in the consumer module or run the module-specific example class from your IDE.

---

## Modules

This repository contains the following high-level folders/modules (summaries inferred from the POMs):

- `shared/store-engine-parent` — Parent POM and likely the main Spring Boot application module. Coordinates: `az.kon.academy:store-engine-parent:1.0.0`.
- `library/` — (describe purpose here if this is a utilities or domain library; add details as you know them).
- `shared/` — contains shared modules and the parent POM.

If you add or rename modules, update the root `pom.xml` modules section accordingly.

---

## Development / Contributing

If you'd like to contribute:

1. Fork the repository and create a feature branch.
2. Run the project build and tests locally:

```bash
mvn -T 1C clean install
```

3. Open a pull request with a clear description and related issue (if any).

Coding style and formatter: follow existing project conventions. If the project uses a formatter (Checkstyle/Spotless), run it before committing.

Commit message guideline: short summary in first line, blank line, more details.

---

## Testing

Run unit tests with Maven (default behavior):

```bash
mvn test
```

To skip tests during a build:

```bash
mvn -DskipTests=true package
```

The parent POM sets `maven-surefire-plugin` configuration; if tests fail, inspect `${project.build.directory}/surefire-reports` for details.

---

## Troubleshooting

- Common Java compatibility issues: ensure JAVA_HOME points to a JDK 25 installation.
- If Maven fails to resolve Spring BOMs or other dependencies, check your network and repository definitions. The parent POM references the Confluent Maven repository for specific artifacts.
- Rebuild after cleaning when encountering binary/classpath problems:

```bash
mvn clean -T 1C install
```

- If you see issues related to `spring-boot-maven-plugin` repackaging, try running the module's main class from your IDE to verify behavior.
