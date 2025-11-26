# VuBui Horse Mod - Implementation Summary

## Overview
This mod implements Vu Bui's hilarious idea from the "LATEST GAME DROP DATE REVEAL! | MINECRAFT MONTHLY" video: making horses spit fireballs!

## Features Implemented

### 1. Core Fireball Mechanics
- **HorseFireballHandler.java**: Main event handler that manages horse fireball spitting
  - Tracks cooldowns per horse using UUID-based HashMap
  - Handles defensive behavior when horses are attacked
  - Manages wild horse random spitting at nearby threats
  - Uses Minecraft's LargeFireball entity (ghast fireball)

### 2. Player Controls
- **KeyBindings.java**: Registers the 'R' key (configurable) for player-controlled fireball spitting
- **ClientInputHandler.java**: Handles client-side key press detection
- **NetworkHandler.java**: Client-server communication for fireball commands
  - Custom packet payload system for NeoForge
  - Validates that horse is tamed and saddled before allowing player control

### 3. Configuration System
- **Config.java**: Fully configurable mod settings
  - Fireball cooldown min/max (default: 5-10 seconds)
  - Fireball damage (default: 6.0)
  - Fire spread toggle (default: enabled)
  - Wild horse spit chance (default: 0.01 per tick)

### 4. Breeding System
- **HorseBreedingHandler.java**: Makes fireball trait hereditary
  - Baby horses automatically inherit the ability
  - Logs when new fireball-spitting horses are born

### 5. Behavior Details
- **Passive by default**: Horses don't attack unless provoked
- **Player-specific targeting**: When hit by a player, horses remember that specific player and spit at them
- **Tamed horses are peaceful**: Tamed horses don't auto-spit, only respond to player commands
- **Wild horse aggression**: Untamed horses occasionally spit at nearby hostile mobs (excludes players)
- **Player-controlled aiming**: Tamed horses shoot fireballs in the player's look direction
- **Cooldown HUD**: Visual cooldown display at top-left with fire icon and countdown timer
- **Cooldown system**: Random cooldown between configured min/max values

### 6. Fireball Properties
- Uses vanilla ghast fireball mechanics and animations
- Explosion power set to 0 (entity damage only, NO block destruction)
- Fire spread enabled (configurable)
- Plays ghast shooting sound effect
- Player-aimed fireballs follow look direction (not straight up)

### 7. UI/UX Features
- **Cooldown HUD Overlay**: Displays at top-left corner like potion effects
  - Fire resistance icon
  - Countdown timer in seconds
  - Only visible when riding a tamed, saddled horse with active cooldown
- **Keybind Configuration**: Appears in Controls menu under "VuBui Horse" category
  - Default: R key
  - Fully customizable by player
  - Uses GAMEPLAY category for proper integration

## File Structure

```
src/main/java/github/ssourabh58/vubuihorse/
├── VuBuiHorse.java              # Main mod class
├── Config.java                   # Configuration system
├── HorseFireballHandler.java    # Core fireball mechanics
├── HorseBreedingHandler.java    # Breeding/hereditary system
├── KeyBindings.java              # Keybind registration
├── ClientInputHandler.java       # Client input handling
├── NetworkHandler.java           # Client-server networking
└── CooldownHudOverlay.java       # HUD cooldown display

src/main/resources/assets/vubuihorse/lang/
└── en_us.json                    # English translations
```

## Technical Details

### Event Subscriptions
- `LivingDamageEvent.Pre`: Detects when horses are attacked
- `EntityTickEvent.Pre`: Handles per-tick horse behavior
- `BabyEntitySpawnEvent`: Manages breeding inheritance
- `ClientTickEvent.Post`: Client-side input handling
- `RegisterKeyMappingsEvent`: Keybind registration
- `RegisterPayloadHandlersEvent`: Network packet registration

### Network Protocol
- Custom packet: `vubuihorse:horse_fireball`
- Client → Server communication for player-triggered fireballs
- Validates permissions server-side (tamed + saddled)

### Data Persistence
- Uses UUID-based HashMaps for cooldown tracking
- NBT tags for marking horses with fireball trait
- Automatic cleanup when horses are removed

## Configuration File
Located at: `config/vubuihorse-common.toml`

```toml
[general]
fireballCooldownMin = 5
fireballCooldownMax = 10
fireballDamage = 6.0
enableFireSpread = true
wildHorseSpitChance = 0.01
```

## Keybinds
- **Default**: R key
- **Category**: VuBui Horse
- **Action**: Horse Fireball Spit
- Configurable in Minecraft's Controls menu

## Credits
Inspired by Vu Bui's suggestion in the Minecraft Monthly video about the Mounts of Mayhem update.

*"Okay, can we make horses spit fireballs?" - Yes. Yes we can.*
