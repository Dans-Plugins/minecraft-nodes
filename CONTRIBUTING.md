# Contributing

## Thank You

Thank you for your interest in contributing to Minecraft Nodes! This guide will help you get started.

## Links

- [Website](https://dansplugins.com)
- [Discord](https://discord.gg/xXtuAQ2)

## Requirements

- A GitHub account
- Git installed on your local machine
- Java JDK 21 (the Paper 1.21 dev bundle will not resolve on older JDKs)
- A Java IDE or text editor
- A basic understanding of Kotlin

## Getting Started

1. [Sign up for GitHub](https://github.com/signup) if you don't have an account.
2. Fork the repository by clicking **Fork** at the top right of the repo page.
3. Clone your fork: `git clone https://github.com/<your-username>/minecraft-nodes.git`
4. Open the `nodes/` project in your IDE.
5. Build the plugin:
   ```
   cd nodes
   ./gradlew build
   ```
   If you encounter errors, please open an issue.

## Identifying What to Work On

### Issues

Work items are tracked as [GitHub issues](https://github.com/Dans-Plugins/minecraft-nodes/issues).

### Milestones

Issues are grouped into [milestones](https://github.com/Dans-Plugins/minecraft-nodes/milestones) representing upcoming releases.

## Making Changes

1. Make sure an issue exists for the work. If not, create one.
2. Switch to `master`: `git checkout master`
3. Create a branch: `git checkout -b <branch-name>`
4. Make your changes.
5. Test your changes (see [Testing](#testing)).
6. Commit: `git commit -m "Description of changes"`
7. Push: `git push origin <branch-name>`
8. Open a pull request against `master`, linking the related issue with `#<number>`.
9. Address review feedback.

## Testing

Every change is verified in two steps. Automated coverage is still small, so the second step matters for most changes.

### 1. Build

The build compiles the Kotlin sources, runs the unit tests, and runs `ktlintCheck`.

Unit tests live in `nodes/src/test/kotlin/`, in the same package as the code they test, and run on JUnit 5 without a server. They cover config loading, the territory income split, the hidden ore rule, chunk coordinate parsing, name validation, port save data, the enum-keyed array map, and the string helpers used by town serialization. Add a test for code that can run without a live server, such as config parsing and pure logic. To run only the tests, use `./gradlew test` from `nodes/`.

Linux:
```
cd nodes
./gradlew clean build
```

Windows:
```
cd nodes
.\gradlew.bat clean build
```

### 2. Manual testing on a server

Commands, listeners, and war logic have no unit tests, so anything that changes how the plugin acts in game must be checked by hand. Take the JAR from `nodes/build/libs/` and drop it into a local Paper/Spigot server together with the [Kotlin runtime plugin](https://github.com/d-z4/minecraft-kotlin), then exercise the commands and scenarios your change affects.

Describe what was tested manually in your pull request, so reviewers know which behaviour has been checked and which has not.

## Questions

Ask in the [Discord server](https://discord.gg/xXtuAQ2).
