# Build PvP Practice on your Mac

This project targets Minecraft Java 26.2, Fabric, and Java 25.

## Option A: Build on your Mac

1. Install a Java 25 JDK (Temurin 25 or another compatible JDK).
2. Install Gradle 9.5.1.
3. Open Terminal and `cd` into the extracted `PvPPractice-26.2` folder.
4. Run:

   ```sh
   java -version
   gradle --version
   gradle clean build
   ```

5. If the build succeeds, the compiled mod should be in `build/libs/`, usually named `pvppractice-0.1.0.jar`.

## Option B: Build on GitHub Actions

1. Create a new GitHub repository.
2. Upload the contents of this project to the repository (the `.github/workflows/build.yml` file must be included).
3. Open the repository's **Actions** tab and select **Build Fabric mod**.
4. Run the workflow with **Run workflow**.
5. Open the completed run and download the `PvPPractice-Fabric-26.2` artifact. It contains the JAR file(s).

## Important

The workflow builds the code; it does not guarantee the prototype's gameplay is fully tested. If the build reports errors, use the error log to correct version-specific Fabric/Minecraft API calls before installing the JAR. The current opponent is a geared vanilla Zombie prototype, not a finished human-like pro PvP AI.
