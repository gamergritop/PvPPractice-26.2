# PvP Practice — Minecraft Java 26.2 (Fabric)

This is the first source prototype for the requested single-player PvP practice mod.

## Intended behavior

- `/pvpsummon` registers at permission level 0, intended to work in Survival without enabling vanilla cheats or switching to Creative.
- Summons one geared vanilla Zombie opponent and gives the player a Netherite PvP loadout.
- `/pvpbot remove` removes the active bot.
- `/pvpbot gear` reapplies the player's loadout.
- `/pvpbot help` lists the commands.

## Prototype limitations

This is not a finished human-like pro PvP AI. It uses vanilla Zombie navigation/melee AI, so it chases and attacks but does not yet perform shield timing, sprint resetting, crystal placement, or adaptive learning. The player gets crystals and obsidian for manual crystal practice; the bot does not execute crystal PvP. The bot's shield is equipped visually but vanilla Zombie AI does not actively block with it.

The source has not been compiled or tested in a Minecraft client yet. Treat it as starter source, not a verified ready-to-install JAR.

## Build

The project uses Java 25 and Gradle 9.5.1. GitHub Actions should build the project and upload a JAR artifact if compilation succeeds.

1. Open the repository's **Actions** tab.
2. Select **Build Fabric mod**.
3. Click **Run workflow**.
4. Open the completed run and download the `PvPPractice-Fabric-26.2` artifact.

The JAR output, if compilation succeeds, is in `build/libs/`.

## Install after a successful build

1. Back up your world.
2. Install Fabric Loader for Minecraft 26.2 and the matching Fabric API.
3. Copy the built JAR into `.minecraft/mods`.
4. Launch the Fabric profile and enter a single-player world in Survival.
5. Test `/pvpsummon`.

Dependencies: Minecraft Java 26.2, Fabric Loader 0.19.3+, Fabric API 0.161.0+26.2, Java 25.
