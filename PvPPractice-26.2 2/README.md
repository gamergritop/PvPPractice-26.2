# PvP Practice — Minecraft Java 26.2 (Fabric)

This is the **first source prototype** for the requested single-player PvP practice mod.

## Intended behavior

- `/pvpsummon` works as a mod-registered command with permission level 0, so the player does not need to enable vanilla cheats or switch to Creative.
- Summons one persistent, geared vanilla Zombie opponent and gives the player a Netherite PvP loadout.
- `/pvpbot remove` removes the active bot.
- `/pvpbot gear` reapplies the player's loadout.
- `/pvpbot help` lists the commands.
- The bot has increased health, movement speed, attack damage, and follow range.

## Important prototype limitations

This is **not yet a fully human-like pro PvP AI**. It uses the vanilla Zombie navigation/melee AI, so it will chase and attack but does not yet perform real shield timing, sprint resetting, crystal placement, or adaptive learning. The player's inventory includes crystals and obsidian for manual crystal practice; the current bot does not execute crystal PvP. Also, the bot's shield is equipped visually but vanilla Zombie AI does not actively block with it.

The code targets the 26.2 official-name toolchain and has **not been compiled or tested in a Minecraft client in this environment**. Treat it as a starter source project, not a verified ready-to-install JAR. Minecraft 26.2 requires the newer toolchain; Fabric's 26.2 guidance recommends Loom 1.17, Gradle 9.5.1, and Loader 0.19.3.

## Build

Use Java 25 and Gradle 9.5.1. Open this folder as a Gradle project in IntelliJ IDEA, let Gradle sync, then run:

```bash
gradle build
```

The output JAR, if the project compiles cleanly, will be in `build/libs/`.

## Install

1. Back up the world first.
2. Install Fabric Loader for Minecraft 26.2 and the matching Fabric API.
3. Copy the built `pvppractice-0.1.0.jar` into `.minecraft/mods`.
4. Launch Minecraft 26.2 with the Fabric profile.
5. Enter a single-player world in Survival and type `/pvpsummon` in chat.

## Dependencies

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3 or newer compatible with 26.2
- Fabric API 0.161.0+26.2
- Java 25
