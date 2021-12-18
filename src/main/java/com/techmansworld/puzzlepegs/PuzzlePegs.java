/**
 * Puzzle Pegs: A program that solves the 15-hole peg game (triangular board)
 * Copyright (C) 2021 Michael Hazell <michaelhazell@hotmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.techmansworld.puzzlepegs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a peg game puzzle
 * Specifically: a 15-hole peg board (triangular)
 */
public class PuzzlePegs {
    // Universal representation of a peg
    private static final char peg = 'P';

    // Universal representation of a hole
    private static final char hole = 'H';

    // Table of all possible moves
    private static final int[][] moves = {
            { 1, 2, 4 },
            { 1, 3, 6 },
            { 2, 4, 7 },
            { 2, 5, 9 },
            { 3, 5, 8 },
            { 3, 6, 10 },
            { 4, 2, 1 },
            { 4, 5, 6 },
            { 4, 7, 11 },
            { 4, 8, 13 },
            { 5, 8, 12 },
            { 5, 9, 14 },
            { 6, 3, 1 },
            { 6, 5, 4 },
            { 6, 9, 13 },
            { 6, 10, 15 },
            { 7, 4, 2 },
            { 7, 8, 9 },
            { 8, 5, 3 },
            { 8, 9, 10 },
            { 9, 5, 2 },
            { 9, 8, 7 },
            { 10, 6, 3 },
            { 10, 9, 8 },
            { 11, 7, 4 },
            { 11, 12, 13 },
            { 12, 8, 5 },
            { 12, 13, 14 },
            { 13, 12, 11 },
            { 13, 8, 4 },
            { 13, 9, 6 },
            { 13, 14, 15 },
            { 14, 13, 12 },
            { 14, 9, 5 },
            { 15, 10, 6 },
            { 15, 14, 13 }
    };

    // History of boards representing the jumps
    private List<char[]> boards;

    // History of jumps
    private List<String> jumps;

    // Starting hole location
    int startPos;

    // Ending peg location
    int endPos;

    /**
     * Constructor
     *
     * @param startPos The starting position of the hole
     * @param endPos   The ending position of the peg, or -1 if location is not
     *                 important
     */
    PuzzlePegs(int startPos, int endPos) throws IllegalArgumentException {
        // Assign starting hole and ending peg location
        if (!betweenInclusive(1, 15, startPos)) {
            throw new IllegalArgumentException("Starting hole location must range from [1, 15]");
        } else {
            this.startPos = startPos;
        }

        if ((!betweenInclusive(1, 15, endPos)) && (endPos != -1)) {
            throw new IllegalArgumentException("Ending peg location must range from [1, 15]");
        } else {
            this.endPos = endPos;
        }

        // Init board and jump history
        boards = new ArrayList<>();
        jumps = new ArrayList<>();
    }

    /**
     * Checks if a value is within a range, inclusive
     *
     * @param lower The lower end
     * @param upper The upper end
     * @param value The value to check
     */
    private static boolean betweenInclusive(int lower, int upper, int value) {
        if ((value >= lower) && (value <= upper)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Count occurances of a character within a range (inclusive)
     *
     * @param array The array of values to check
     * @param value The value to check
     * @return The number of occurances that value appears
     */
    private static int count(char[] array, char value) {
        var count = 0;
        for (var c : array) {
            if (c == value) {
                count++;
            }
        }
        return count;
    }

    /**
     * Solve the puzzle
     */
    public boolean solve() {
        // Build the board. Reserve 16 spaces.
        char[] board = new char[16];
        board[0] = ' '; // Null "space", this space is not used
        for (var i = 1; i < 16; ++i) {
            if (startPos == i) {
                board[i] = hole;
            } else {
                board[i] = peg;
            }
        }

        // Store the original board to show before the moves are printed
        var original = board.clone();

        // Now, solve the puzzle!
        if (solveInternal(board)) {
            System.out.println("Initial board");
            printBoard(original);

            // Print the moves and board to the output. The moves (jumps) are in reverse
            // order due to the recursion. The board states are not
            Collections.reverse(jumps);
            for (var i = 0; i < boards.size(); ++i) {
                System.out.println(jumps.get(i));
                printBoard(boards.get(i));
            }
            return true;
        } else {
            System.out.println("No solution could be found for this combination");
            return false;
        }
    }

    /**
     * Internal recursive function for solving, making use of backtracking
     */
    private boolean solveInternal(char[] board) {
        // For every move inthe table of possible moves...
        for (var move : moves) {
            // See if we can match a PPH pattern. If we can, try following this route by
            // calling ourselves again with this modified board
            if ((board[move[0]] == peg) && (board[move[1]] == peg) && (board[move[2]] == hole)) {
                // Apply the move
                board[move[0]] = hole;
                board[move[1]] = hole;
                board[move[2]] = peg;

                // Record the board in history of boards
                var clone = board.clone();
                boards.add(clone);

                // Call ourselves recursively. If we return true then the conclusion was good
                // If it was false, we hit a dead end and we shouldn't print the board
                if (solveInternal(board)) {
                    // Record the jump
                    jumps.add("Moved " + move[0] + " to " + move[2] + ", jumping over " + move[1]);
                    return true;
                }

                // If we end up here, undo the move and try the next one
                boards.remove(boards.size() - 1);
                board[move[0]] = peg;
                board[move[1]] = peg;
                board[move[2]] = hole;
            }
        }

        // If no pattern is matched, see if there is only one peg left and see if it is
        // in the right spot
        // Situation 1: Count of PEG is 1 and the ending position was not specified
        var pegCount = count(board, peg);
        if ((pegCount == 1) && (endPos == -1)) {
            return true;
        }
        // Situation 2: Count of PEG is 1 and the value of the ending position was PEG
        else if ((pegCount == 1) && (board[endPos] == peg)) {
            return true;
        }
        // Situation 3: Count of PEG was not 1 or the value at the ending position was
        // not PEG
        else {
            return false;
        }
    }

    /**
     * Print board to stdout
     *
     * @param board An array representing the game board
     */
    private static void printBoard(char[] board) {
        System.out.println("    " + board[1] + "\n");
        System.out.println("   " + board[2] + " " + board[3] + "\n");
        System.out.println("  " + board[4] + " " + board[5] + " " + board[6] + "\n");
        System.out.println(" " + board[7] + " " + board[8] + " " + board[9] + " " + board[10] + "\n");
        System.out.println(board[11] + " " + board[12] + " " + board[13] + " " + board[14] + " " + board[15]);
    }

    /**
     * Show help information
     */
    public static void help() {
        System.out.println("Usage: PuzzlePegs [start_pos] [end_pos]");
        System.out.println("start_pos: The location of the starting hole, e.g. 13");
        System.out.println("end_pos: The location of the last peg, e.g. 13");
    }

}
