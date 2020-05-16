# Paddle Game
## Course: Paradigms of Programming

The overall design of the game is as follows:
#### Paddle: 
Fields: coordinate, size, speed
Methods: draw(), 
#### Brick:
Fields: coordinate, size, speed = 0
Methods: draw(),
#### Ball:
Fields: coordinate, size, speed
Methods: draw(), collide(),
#### Map:
which will contain a Paddle, multiple Bricks and a Ball
Paddle, Brick and Ball all inherit from a single class - **MapObject**.

For the basic game view, refer following https://www.simplifiedcoding.net/android-game-development-tutorial-1/
