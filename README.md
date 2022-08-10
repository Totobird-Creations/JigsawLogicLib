<image src="https://raw.githubusercontent.com/Totobird-Creations/JigsawLogicLib/1.19/src/main/resources/assets/jigsawlogiclib/icon.png" alt="Icon" height=100px>

# Jigsaw Logic Lib
A library that allows modders to add custom game logic when a structure loads.

[Modrinth](https://modrinth.com/mod/jigsawlogiclib)
[Github](https://github.com/Totobird-Creations/JigsawLogicLib)

__Installation__:
Add the following to your `build.gradle`.
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    modImplementation 'com.github.Totobird-Creations:JigsawLogicLib:${jigsawlogiclib_version}'
    // Optional:
    include 'com.github.Totobird-Creations:JigsawLogicLib:${jigsawlogiclib_version}'
}
```
Add the following to your `gradle.properties`.
```properties
jigsawlogiclib_version = v1.0.1-mc1.19
```
Ad the following to your `fabric.mod.json`.
```json
"depends": {
    "jigsawlogiclib": "1.x.x"
}
```

__Usage__:
In your initialiser's `onInitialize` method, add the following:
```java
LogicCommandManager.register(
    new Identifier("yourModId", "commandName"),
    (metadata, world, blockPos, structureOrigin) -> {
        // Arguments:
        // - String   metadata        : Some extra info given by the logic block.
        // - World    world           : The world where the logic block was run.
        // - BlockPos blockPos        : The position of the logic block when it was run.
        // - BlockPos structureOrigin : The position where the structure started generating.
        // Safety notes:
        // - Make sure that if you set any blocks, they are in the same chunk.
    }
);
```
1. Set up the structure blocks and your structure, then place a logic block inside.
2. In game, run the following command: `/give @s jigsawlogiclib:logic`
3. Place it and set the command value to what you what you set above `yourModId:commandName`.
4. Optionally, add some metadata. This will be passed to your function you specified above.
5. Press "Done" to save, or press "Run" to test it.
6. Save your structure and set up all of the worldgen data files.
