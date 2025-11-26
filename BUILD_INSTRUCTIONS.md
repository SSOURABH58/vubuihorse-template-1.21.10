# Build Instructions for VuBui Horse Mod

## Prerequisites
- Java 21 or higher
- Gradle (included via gradlew wrapper)

## Building the Mod

1. Open a terminal in the project directory

2. On macOS/Linux, run:
   ```bash
   ./gradlew build
   ```

   On Windows, run:
   ```cmd
   gradlew.bat build
   ```

3. The built mod jar will be located in:
   ```
   build/libs/vubuihorse-<version>.jar
   ```

## Running in Development

To test the mod in a development environment:

```bash
./gradlew runClient
```

This will launch Minecraft with the mod loaded.

## Installing the Mod

1. Build the mod using the instructions above
2. Copy the jar file from `build/libs/` to your Minecraft `mods` folder
3. Make sure you have NeoForge installed for Minecraft 1.21.x
4. Launch Minecraft

## Configuration

After running Minecraft with the mod once, a config file will be created at:
```
config/vubuihorse-common.toml
```

You can edit this file to customize:
- Fireball cooldown times (min and max in seconds)
- Fireball damage
- Fire spread behavior
- Wild horse spit chance

## Controls

- **R key** (default): Make your tamed, saddled horse spit a fireball
- You can change this keybind in Minecraft's Controls settings under "VuBui Horse"

## Troubleshooting

If you encounter build errors:
1. Make sure you have Java 21 installed: `java -version`
2. Try cleaning the build: `./gradlew clean build`
3. If dependencies fail to download, check your internet connection

## Notes

The IDE may show errors in the code before building - this is normal. The Minecraft and NeoForge dependencies are downloaded during the Gradle build process.
