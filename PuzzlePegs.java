/**
 * Puzzle Pegs: A program that solves the 15-hole peg game (triangular board)
 * Copyright (C) 2020 Michael Hazell <michaelhazell@hotmail.com>
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.List;

public class PuzzlePegs
{
	// Constants for representing a peg and a hole
	private final static char PEG = 'P';
	private final static char HOLE = 'H';

	// Table of all possible moves
	private static final int[][] moves =
	{
		{1, 2, 4},
		{1, 3, 6},
		{2, 4, 7},
		{2, 5, 9},
		{3, 5, 8},
		{3, 6, 10},
		{4, 2, 1},
		{4, 5, 6},
		{4, 7, 11},
		{4, 8, 13},
		{5, 8, 12},
		{5, 9, 14},
		{6, 3, 1},
		{6, 5, 4},
		{6, 9, 13},
		{6, 10, 15},
		{7, 4, 2},
		{7, 8, 9},
		{8, 5, 3},
		{8, 9, 10},
		{9, 5, 2},
		{9, 8, 7},
		{10, 6, 3},
		{10, 9, 8},
		{11, 7, 4},
		{11, 12, 13},
		{12, 8, 5},
		{12, 13, 14},
		{13, 12, 11},
		{13, 8, 4},
		{13, 9, 6},
		{13, 14, 15},
		{14, 13, 12},
		{14, 9, 5},
		{15, 10, 6},
		{15, 14, 13}
	};

	// History of jumps
	private static List<String> jumps;

	// History of boards representing the jumps
	private static List<char[]> boards;

	public static void main(String[] args)
	{
		int startingHoleLocation;
		int endingPegLocation;
		try
		{
			switch (args.length)
			{
				// java PuzzlePegs 13 13 -- would solve the game with the
				// starting hole at position 13, and the last peg also at
				// position 13
				case 2:
					startingHoleLocation = Integer.parseInt(args[0]);
					endingPegLocation = Integer.parseInt(args[1]);
					break;

				// java PuzzlePegs 13 -- would solve the game with the
				// starting hole at position 13, but the last peg can be
				// anywhere
				case 1:
					startingHoleLocation = Integer.parseInt(args[0]);
					// We use -1 because it is a clear "nothing specified"
					endingPegLocation = -1;
					break;

				// java PuzzlePegs -- with no args, default to case 1
				case 0:
					startingHoleLocation = 13;
					endingPegLocation = -1;
					break;

				// Catch-all for improper input
				default:
					System.err.println("Too many arguments");
					showHelp();
					return;
			}
		}
		catch (NumberFormatException e)
		{
			System.err.println("Invalid number input. Please use whole numbers.");
			return;
		}

		// Sanitize the actual numbers put in
		if (!betweenInclusive(1, 15, startingHoleLocation))
		{
			System.err.println("Invalid input. Valid pegs/holes range from 1 to 15, inclusive.");
			return;
		}
		if (!betweenInclusive(1, 15, endingPegLocation) && endingPegLocation != -1)
		{
			System.err.println("Invalid input. Valid pegs/holes range from 1 to 15, inclusive.");
			return;
		}

		// Build array
		char[] board = new char[16];
		board[0] = ' '; // Null "space", this space is not used
		for (int i = 1; i < 16; ++i)
		{
			if (startingHoleLocation == i)
			{
				board[i] = HOLE;
			}
			else
			{
				board[i] = PEG;
			}
		}

		// Store the initial board to show before the moves are printed
		char[] original = board.clone();

		// Initialize the history of jumps and boards
		jumps = new ArrayList<>();
		boards = new ArrayList<>();

		// Now, solve the puzzle! If there is a solution, print it out. If
		// there isn't, mention that
		if (puzzlePegs(board, endingPegLocation))
		{
			// Print the initial board before the moves
			System.out.println("Initial board");
			System.out.println(printBoard(original));

			// Print the moves and board state to the output. The moves are in
			// reverse order due to recursion. THe board states are not.
			for (int i = jumps.size() - 1, j = 0; i > -1; --i, ++j)
			{
				System.out.println(jumps.get(i));
				System.out.println(printBoard(boards.get(j)));
			}
		}
		else
		{
			System.out.println("No solution could be found for this combination");
		}

	}

	/** Puzzle pegs
	 * @param board The board that represents the puzzle
	 * @param startPos The starting position of the hole
	 * @param endPos The ending position of the final peg
	 */
	private static boolean puzzlePegs(char[] board, int endPos)
	{
		// Check the valid moves against the board
		for (int[] move : moves)
		{
			// See if we can match a PPH pattern. If we can, try following this
			// route by calling ourselves again with this modified board
			if ((board[move[0]] == PEG) && (board[move[1]] == PEG) && (board[move[2]] == HOLE))
			{
				// Apply the move
				board[move[0]] = HOLE;
				board[move[1]] = HOLE;
				board[move[2]] = PEG;

				// Record the board as-is
				char[] clone = board.clone();
				boards.add(clone);

				// Call ourselves recursively, if we return true then the conclusion was good.
				// If it was false, we hit a dead end and we shouldn't print the move.
				if (puzzlePegs(board, endPos))
				{
					// Record the jump, since it was a successful one.
					jumps.add("Moved " + move[0] + " to " + move[2] + ", jumping over " + move[1]);
					return true;
				}

				// If we end up here, undo the move and try the next one
				if (boards.contains(clone))
				{
					boards.remove(clone);
				}
				board[move[0]] = PEG;
				board[move[1]] = PEG;
				board[move[2]] = HOLE;
			}
		}
		// If no pattern is matched, see if there is only one puzzle peg
		// left and see if it is in the right spot.
		// Count of 'P' is 1 and the ending position was not specified
		int pegCount = count(board, PEG);
		if ((pegCount == 1) && (endPos == -1))
		{
			return true;
		}
		// Count of 'P' is 1 and the value at the ending position was 'P'
		else if ((pegCount == 1) && (board[endPos] == PEG))
		{
			return true;
		}
		// Count of 'P' was not 1 or the value at the ending position
		// was not 'P'
		else
		{
			return false;
		}
	}

	/** Show help information
	 */
	private static void showHelp()
	{
		System.out.println("Usage: java PuzzlePegs [hole] [ending peg]");
		System.out.println("hole: the location of the hole when the game begins, e.g. 13");
		System.out.println("ending peg: the location of the last peg, e.g. 13");
	}

	/** Check whether a number is range (inclusive)
	 * @param lower The lower bound
	 * @param upper The upper bound
	 * @param num The number to check within the range
	 * @return true if the number is in range, false otherwise
	 */
	private static boolean betweenInclusive(int lower, int upper, int num)
	{
		if ((num >= lower) && (num <= upper))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/** Count occurences of a value within a range (inclusive)
	 * @param array The array of values to check
	 * @param value The value to check
	 * @return The number of occurences that value appears
	 */
	private static int count(char[] array, char value)
	{
		int count = 0;
		for (char c : array)
		{
			if (c == value)
			{
				count++;
			}
		}
		return count;
	}

	/** Print the peg board
	 * @param board An array representing the board
	 * Board output results in looking like:
	 *      X
	 *     X X
	 *    X X X
	 *   X X X X
	 *  X X X X X
	 */
	private static String printBoard(char[] board)
	{
		StringBuilder boardString = new StringBuilder();
		boardString.append("    " + board[1] + "\n");
		boardString.append("   " + board[2] + " " + board[3] + "\n");
		boardString.append("  " + board[4] + " " + board[5] + " " + board[6] + "\n");
		boardString.append(" " + board[7] + " " + board[8] + " " + board[9] + " " + board[10] + "\n");
		boardString.append(board[11] + " " + board[12] + " " + board[13] + " " + board[14] + " " + board[15]);
		return boardString.toString();
	}
}
