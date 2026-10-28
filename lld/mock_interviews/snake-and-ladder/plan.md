# Snake And Ladder

## Requirements
1. Game can be played among 2 or more players - player get chance in turns
2. Game board is of size 10*10
3. Game with have configurable snake & ladder
4. Game with have a dice with 1-6
5. With 6 player gets another chance, with 3 consecutive 6's the chance moves to next player
6. Player reaching to 100 - WINs

## Entity
1. Player
2. Dice
3. Snake
4. Ladder
5. Game

## Classes

GameStatus
```
GameStatus: IN_PROGRESS, FINISHED
```

Player
```
Player
- name: String
- position: Integer

+ Player(name)
+ getPosition(): Integer
+ setPosition(int value)
```

Dice
```
Dice
- minVal: Integer
- maxVal: Integer

+ Dice(minVal, maxVal)
+ rollDice(): Integer
```

Snake/Ladder - BoardEntity
```
Snake/Ladder - BoardEntity
- start: Integer
- end: Integer

+ Snake/Ladder(start, end)
```

Board
```
Board
- size
- map<Integer, Integer> : Snake/Ladder

+ Board(size, list of Snake/Ladder)
```

Game
```
Game
- players: Queue<Player>
- board: Board
- dice: Dice
- gameState
- winner: Player

+ Game(players, board, dice)
+ play()
+ playTurn()
```
