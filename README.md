Project Structure

For the basic game view, following https://www.simplifiedcoding.net/android-game-development-tutorial-1/

We will need the following classes:
1. Paddle: 
Fields: coordinate, size, speed
Methods: draw(), 
2. Brick:
Fields: coordinate, size, speed = 0
Methods: draw(),
3. Ball:
Fields: coordinate, size, speed
Methods: draw(), collide(),
4. Map:
which will contain paddle, multiple bricks and a ball

Since 1,2 and 3 have many similar properties, we can create a MapObject class and let them all inherit from it
