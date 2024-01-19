# Ztacker

**Ztacker** is a Tetris bot that utilizes [ST stacking](https://www.youtube.com/watch?v=-LJt-sXe_XA) to maximise its score in the Tetris *Ultra* game mode.

## About Ztacker

Originally developed for the *Ultra* mode on [Tetris Friends](https://tetrisfriends.com/), Ztacker's goal was to achieve the highest score in a 2-minute span. Following the shutdown of Tetris Friends in 2019, Ztacker was adapted for [Nullpomino](https://github.com/nullpomino/nullpomino), an open-source Tetris implementation.

![](st.gif)

While it's primary purpose was to play *Ultra*, due to the precise and fast nature of computer inputs, it did exceedingly well at the *Sprint* game mode. In this game mode the player is tasked with clearing 40 lines as fast as possible. Due to the different nature of the game mode a different strategy than ST stacking was used. For lack of a better term I have referred to it as "Playing Forever", as in theory this technique will allow an optimal player to keep playing indefinately. It happened to be faster than ST stacking because it didn't require many "Soft Dropping" of pieces.

![](pf.gif)

Ztacker held the fastest time on Tetris Friends for the *Sprint* game mode.

![](tf.gif)

> This gif was recorded at 30fps (since it was recording a replay) the others are 15fps since they were recorded in real-time and recording at higher frame rates impacted the performance of the bot. This is why it looks faster despite being the same bot.

## How It Works

### Reading The Grid
Given there was no API to communicate with Tetris Friends, I resorted to taking snapshots of the portion of the screen containing the Tetris grid.
Ztacker would then pin point the middle of each cell in the grid and based on the colour of each cell it would be able to determine what the state of the
whole grid was. For efficiency in the search step this grid was represented as a handful of longs whose bits were used to demarkate whether a cell was occupied or not.

### The Decision Making Process
Ztacker would receive the state of the matrix along with the active piece and the 5 upcoming piece in the queue. With this it would perform a depth first search carrying out 
each possible permutation of legal moves (as denoted by the ST stacking strategy) with the active piece and then making the next piece in the queue the active piece. It would repeat this recursively until no further moves could be carried out, at which point it would evaluate the value of the grid. It would choose the move that yielded the highest expected value leaf node.

The value of a grid configuration was based on various factors, including how flat the mid section of the board was, how balanced the L and S pieces were, how close to each other the three divisions of the grid were, the expected probability of move options based on the next items in the queue, etc. Losing a game was assigned a value of negative infinity.

### Communicating Back
Since we are constrained by not being able to communicate with the Tetris software directly, we must attempt to do so as a human would. We cannot just say move this piece here as a single API or function call, rather we communicated our moves through key strokes. Soft dropping moves which involve dropping a piece at a fast, but not immediate rate, require the bot to continually monitor the grid as it is making a move to know at what height and orientation the piece is currently at, and adjusting it's key presses accordingly. While the search procedure gives us back the best move, we must then convert it into a series of key presses and releases. Once the bot has checked, by comparing the grid position with it's intended move, that a move has been completed succesfully it will initiate another search.


## Why Ztacker?

The bot will choose to horizontally mirror the way it would normally use pieces if it finds that more advantageous. In this regard the role of the S piece is met by the Z piece and so ST stacking, becomes ZT stacking. Hence ZT stacker -> Ztacker (or ZTK for short). I originally wrote this when I was 19 and I thought it sounded cool.

![](zt.gif)

