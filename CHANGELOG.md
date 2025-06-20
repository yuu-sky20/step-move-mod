# CHANGELOG

## Unreleased

### [v1.0.0] - 2025-06-13
- #### Added
  - Initial setup of the project structure.
  - MVP release with basic functionality.

### [v1.1.2a] - 2025-06-14
- #### Added
  - Made it compatible with modmenu so that settings can be customized.
- #### Dependencies
  - Added `modmenu` as a dependency.
  - Added `clotch-config-api` as a dependency.
- #### Not yet implemented
  - Saving, loading, and initializing settings are not yet implemented.

### [v1.1.2b] - 2025-06-14
- #### Added
  - Implemented saving, loading, and initializing settings.
- #### Dependencies
  - Added `gson` as a dependency.

### [v1.2.0] - 2025-06-14
- #### Added
  - Implemented processing to assign individual settings to each WASD key.
    - Speed Control
    - Toggle Step Move
    - Add Mode Menu Setting Panel

### [v1.3.0] - 2025-06-19
- #### Added
  - ##### PlayerTeleportUseItemBookHandler
    - Add a feature that allows you to move 50 blocks forward when you right-click on a book.

### [v1.3.1] - 2025-06-19
- #### Changed
  - ##### PlayerTeleportUseItemBookHandler
    - Change the number of moves from 50 to 100 blocks.
- #### Added
  - ##### PlayerTeleportTracker
    - Wait indefinitely in midair after jumping.
    - It will be canceled if you move or sneak.

### [v1.3.2] - 2025-06-19
- #### Changed
  - ##### PlayerTeleportUseItemBookHandler
    - Removed the trigger for floating cancellation caused by sneaking and landing on the ground.
- #### Added
  - ##### PlayerTeleportUseItemBookHandler
    - Add floating particle effects.
- #### Fixed
  - ##### PlayerTeleportUseItemBookHandler
    - Fixed a bug where teleportation would not stop properly in midair.

### [v1.3.2-2] - 2025-06-19
- #### Refactored
  - ##### `PlayerTeleportTracker` -> `HoverTracker`
    - Changed confusing class names.
    - Separate the `HoverState class` from `HoverTracker`.
  - ##### `PlayerTeleportUseItemBookHandler` -> `BookTeleportHandler`
    - Changed to a shorter class name.

