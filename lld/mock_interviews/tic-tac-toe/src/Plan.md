# Tic Tac Toe LLD

## Requirements
1. Two player game, each player will have either of symbol X or O
2. Default board size 3*3, but can be configurable
3. Pleayers can be 
   4. Human
   5. AI bot
6. Game state
   7. Win: one player get symbol in continuous 3 row, col or digonal
   8. Tie: no one win and board is filled


## Entities

1. Player
2. Board
3. Game

## Class
```
Game State(ENUM): in_progress, won, tie
```

```
Player SymbolENUM): X, O
```

```
Player(Interface)
- Name
- Symbol
+ boolean playMove(row, col)
    
Human Player(Class)
AI Player(Class)
```

```
Board
- row
- col
- grid[][]

+ Board()
+ fillCell(row, col, symbol)

```

```
Game
- board
- player1
- player2
- playerturn
- winner

+ makeMove(player, row, col)
+ isWinningMove(row, col, symbol)
```

