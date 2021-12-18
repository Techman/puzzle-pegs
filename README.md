# Puzzle Pegs

Puzzle Pegs is a program that solves the 15-hole triangular peg solitaire game, also known as the peg game from Cracker Barrel. It is an exercise in the algorithm technique known as [backtracking](https://en.wikipedia.org/wiki/Backtracking).

This program has been implemented in various languages:
- Java (original implementation language; this repository)
- [C++](https://github.com/Techman/puzzle-pegs-cpp)
- [Python](https://github.com/Techman/puzzle-pegs-py)
- [Rust](https://github.com/Techman/puzzle-pegs-rs)

## How It Works

The biggest challenge for me when I first picked up this exercise, aside from the backtracking itself, was how to represent the game board. There are a few ways the board can be represented:

* One way is a word search. If you think of it that way, half of a grid can represent the triangular board. Finding valid moves is as simple as trying to search the grid for a `PPH` pattern, with `P` representing pegs and `H` representing holes.
* Another way is a graph. This is actually a horrible idea, I think, but I thought of this initially when I was trying to come up with a plan. It should technically be possible.

**The method I eventually settled on can be thought of as heap-like.** Take the triangular board, and number it from 1 to 15.
```
      1
     2  3
   4  5  6
  7  8  9 10
11 12 13 14 15
```
You can represent it in an array as follows:

| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 |
|---|---|---|---|---|---|---|---|---|---|----|----|----|----|----|----|
|   | P | P | P | P | P | P | P | P | P | P  | P  | P  | H  | P  | P  |

This array represents a board with a peg in every hole except 13, which is an empty hole. In ASCII form, it looks like
```
    P
   P P
  P P P
 P P P P
P P H P P
```
Moves are made using a *table of moves.* Valid moves are created by finding the (legal) jumps where you can transform a `PPH` into a `HHP` pattern. For example, referring back to the number-based board, `[1, 2, 4]` represents a valid move as a peg at position `1` can jump over a peg at position `2` to arrive in the hole located at `4`.

Naturally, due to this design, applying this technique to bigger boards or boards of different shapes will increase the list of all possible moves by a large amount, which is tedious to determine beforehand. If this process can be automated (perhaps by finding a pattern), this becomes less of an issue. However, this was a manual process for me. Additionally, the way that the game board itself is represented will have to be changed slightly, but this translates to expanding the size of the array. The numbering of the peg/hole locations corresponds with the table of possible moves. *In this case, the word search approach may be more feasible, as it is easy to define define the board shape in the grid by simply setting locations outside of the board to a character that will not be matched.*

## How The Algorithm Works

Now that I have described how the program works, here is a loose description of how the algorithm works:

- For every move in the list of possible moves,
  - Check and see if the positions currently house a `PPH` pattern.
    - If not, iterate to the next possible move
    - If so, make the move, and call the function again on a "new" version of the game board, with this move made.
      - If this recursion returns `true`, then a solution was found
        - To return true, there must be only one peg left on the board in the position specified in the command line arguments, or anywhere if the ending position was not specified.
      - If this recursion returns `false`, then a solution was not found
        - Undo the move, remove the board from the list of game boards that represent the moves made, and iterate to the next move in the list of possible moves.

# Puzzle Pegs (Java)

This is the (new-ish) Java version of the Puzzle Pegs program. Puzzle Pegs was originally written in Java as a single `.java` file, but has now been rewritten to be a proper Maven project. This was mostly for experience.

# Build

Maven is the tool used to build the project

To build:

1. In the project root, `mvn install` to install dependencies
2. `mvn assembly:single` to build a packaged `.jar` file, with dependencies included
3. You can run this file by typing `java -jar target/puzzlepegs-1.0-jar-with-dependencies.jar`

## Why?

The original Java version can now be found in the Git history. The rewritten Java version now uses a command line argument parsing library.
