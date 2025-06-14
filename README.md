# step-move-mod

## What is this?
This is a mod for Minecraft.
It expands the system and enables fast step movement by double-tapping the movement keys.

## How to install
1. Download the mod from the [releases page](https://github.com/yuu-sky20/step-move-mod/releases/latest)
2. Extract the contents of the zip file to the `mods` folder in your Step installation directory.
3. Launch Step and the mod should be loaded automatically.

## Dependence Mods
- [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config)
- [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu)

## How to use
Use the arrow keys to move around the map. The mod will automatically update the position of the player on the map as you move.

## How to Develop

1. Clone the repository
2. Install the dependencies using `JDK` and `Gradle`
3. Run the `./gradlew build" command to build the mod
5. If you want to run the mod in development mode, you can use the `./gradlew runClient` command to start the game with the mod loaded.
4. The mod will be built in the `build/libs` directory