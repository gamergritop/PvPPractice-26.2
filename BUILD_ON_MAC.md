# Build PvP Practice on your Mac

This project targets Minecraft Java 26.2, Fabric, and Java 25.

## Build on GitHub Actions

1. Open the repository's **Actions** tab and select **Build Fabric mod**.
2. Click **Run workflow** if shown.
3. Open the completed run and download the **PvPPractice-Fabric-26.2** artifact. It should contain the JAR.

## Important

The workflow builds the code; it does not guarantee the prototype's gameplay is fully tested. If the build reports errors, use the error log to correct version-specific Fabric/Minecraft API calls before installing the JAR. The current opponent is a geared vanilla Zombie prototype, not a finished human-like pro PvP AI.
