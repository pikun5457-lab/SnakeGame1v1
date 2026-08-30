# Snake

A JavaFX Snake game with a retro arcade start screen where you pick **1 Player**
or **2 Player** mode before you play.

## Features
- Start menu (matches the "Game Guy" arcade UI style) with mode selection
- **1 Player** — classic Snake, WASD to move
- **2 Player** — versus mode on a wider board, Player 1 uses WASD, Player 2 uses Arrowkeys(UP Arrow,Down Arrow,Left Arrow,Right Arrow)
- Game-over screen shows the score(s) and lets you restart, return to the menu, or quit

## Controls
| Player | Keys                       |
|---|----------------------------|
| Player 1 | `W` `A` `S` `D`            |
| Player 2 (2P mode only) | `UP` `Down` `Left` `Right` |

## Run it
Requires JDK 21+. Maven will pull in JavaFX automatically.

```bash
./mvnw clean javafx:run
```

or from your IDE, run `Launcher.main`.

## Project structure
```
src/main/java/com/example/snakefinal/
├── Launcher.java        # app entry point, opens the start menu
├── SnakeMenu.java        # start screen: choose 1P or 2P
├── SnakeWindow.java       # builds the gameplay screen + game-over dialog
└── SnakeAlgorithm.java    # game state, movement, and collision logic
src/main/resources/
├── snake.css              # arcade theme (menu + in-game HUD)
└── fonts/PressStart2P-Regular.ttf
```
