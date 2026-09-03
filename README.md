# StutterFix (Forge Port)

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://www.minecraft.net/)
[![Forge](https://img.shields.io/badge/Forge-47.3.0-orange.svg)](https://files.minecraftforge.net/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-See%20LICENSE-lightgrey.svg)](./LICENSE)

StutterFix is a low-level optimization mod that reduces micro-stutters and improves framerate stability in Minecraft. This repository contains the unofficial Forge port, adapted from the original Fabric version developed by [wisecase2](https://github.com/wisecase2/StutterFix).

The mod works directly with Minecraft's internal thread pools and the operating system, dynamically adjusting the number of worker threads and their priorities to minimize CPU contention on multi-core systems.

---

## Features

- Advanced worker thread management: configure the number of worker threads used by Minecraft's main executor.
- Thread priority tuning: control the priority of the Render Thread and Server Thread to favor performance on clients or integrated servers.
- `Thread.yield()` removal: experimental option to remove `Thread.yield()` calls from the main render loop (automatically disabled if VulkanMod is detected, to prevent incompatibilities).
- Integrated configuration menu, accessible directly from the Minecraft Options screen.
- Persistent configuration: all settings are saved to `config/stutterfix-config.json`.
- Mixin-based integration: clean injection via `StutterFixMixinPlugin`, with conflict detection for specific mods at load time.

---

## System Requirements

| Component | Required Version |
|-----------|------------------|
| Minecraft | `1.20.1` |
| Forge | `47.3.0` or higher |
| Java | `17` or higher |
| Architecture | 64-bit (x86_64 / ARM64) |

Note: this mod is designed for systems with multi-core CPUs (4+ logical threads). On dual-core systems, the benefits may be limited.

---

## Installation

1. Make sure you have Minecraft 1.20.1 and Forge 47.3.0 installed.
2. Download the latest `stutterfix-1.20.1-x.x.x-forge.jar` from the [Releases](../../releases) section.
3. Place the `.jar` file into your mods folder:
   - Windows: `%appdata%\.minecraft\mods`
   - Linux: `~/.minecraft/mods`
   - macOS: `~/Library/Application Support/minecraft/mods`
4. Launch Minecraft using the Forge profile.

---

## Usage and Configuration

Once in-game, the mod requires no manual intervention. To adjust the parameters:

1. Open the Options menu (`Esc` → `Options...`).
2. Look for the "StutterFix Options" button (injected via Mixin into the main options screen).
3. Adjust the following parameters according to your hardware:

| Parameter | Description | Range |
|-----------|-------------|-------|
| Worker threads count | Number of threads for the main executor. | `1` - `CPU Threads` |
| Priority cutoff point | Threshold for priority assignment. | `0` - `CPU Threads` |
| Render thread priority | Priority of the rendering thread. | `1` - `10` |
| Server thread priority | Priority of the integrated server thread. | `1` - `10` |
| Remove Yield() | Removes `Thread.yield()` from the render loop. | `Remove` / `Keep` |

Recommendation: the default values are optimal for most systems. Only change them if you're experiencing specific stutters or have an advanced CPU profiling setup.

---

## Compatibility with Other Mods

StutterFix is designed to be highly compatible. A few specific notes:

- VulkanMod: if present, the "Remove Yield()" option is automatically disabled at the Mixin level (`RemoveYieldMixin`) to avoid rendering conflicts.
- OptiFine / Rubidium / Embeddium: compatible. StutterFix operates at the system thread level, not the rendering pipeline.
- Optimization mods (Sodium, Lithium, FerriteCore): fully compatible. Using them together is recommended for comprehensive optimization.

If you run into any incompatibilities, report them in the [Issues](../../issues) section.

---

## Building from Source

This project uses Gradle 8.8 and the ForgeGradle build system.

### Development Requirements
- JDK 17 ([Eclipse Adoptium](https://adoptium.net/) recommended)
- Git

### Build Commands

```bash
# Clone the repository
git clone https://github.com/manel740/StutterFix-Reforged-Unofficial-Forge-Port
cd StutterFix

# Clean and build
./gradlew clean build

# Run the development client
./gradlew runClient

# Run the development server
./gradlew runServer
```
