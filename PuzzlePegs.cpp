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

#include <algorithm>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

// Constants for representing a peg and a hole
const static char PEG = 'P';
const static char HOLE = 'H';

// Table of all possible moves
static std::vector<std::vector<int>> moves = {
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

// History of jumps.
static std::vector<std::string> jumps;

// History of boards representing the jumps
static std::vector<std::vector<char>> boards;

// Declaration of additional functions
/** Solve
 * @param board The board that represents the puzzle
 * @param startPos The starting position of the hole
 * @param endPos The ending position of the final peg
 */
static bool solve(std::vector<char>& board, const int& endPos);

/** Show help information
 */
static void showHelp();

/** Check whether a number is in range (inclusive)
 * @param lower The lower bound
 * @param upper The upper bound
 * @param num The number to check within the rain
 * @return true if the number is in range, false otherwise
 */
static bool betweenInclusive(const int& lower, const int& upper, const int& number);

/** Count occurrences of a value within a range (inclusive)
 * @param array The array of values to check
 * @param value The value to check
 * @return The number of occurrences that a value appears
 */
template<typename T>
static int count(const std::vector<T>& array, const T& value);

/** Print the peg board
 * @param board An array representing the board
 * Board output results in looking like:
 *      X
 *     X X
 *    X X X
 *   X X X X
 *  X X X X X
 */
static std::string printBoard(const std::vector<char>& board);

// Main
int main(int argc, char* argv[])
{
	int startingHoleLocation;
	int endingPegLocation;

	switch (argc)
	{
		// ./PuzzlePegs 13 13 -- would solve the game with the starting hole
		// at position 13, and the last peg also at position 13
		case 3:
			startingHoleLocation = std::stoi(argv[1]);
			endingPegLocation = std::stoi(argv[2]);
			break;

		// ./PuzzlePegs 13 -- would solveP the game with the starting hole at
		// position 13, but the last peg can be anywhere
		case 2:
			startingHoleLocation = std::stoi(argv[1]);
			// We use -1 because it is a clear "nothing specified"
			endingPegLocation = -1;
			break;

		// ./PuzzlePegs -- with no args, default to case 1
		case 1:
			startingHoleLocation = 13;
			endingPegLocation = -1;
			break;

		// Catch-all for improper input
		default:
			std::cerr << "Too many arguments\n";
			showHelp();
			return 1;
	}

	// Sanitize the actual numbers put in
	if (!betweenInclusive(1, 15, startingHoleLocation))
	{
		std::cerr << "Invalid input. Valid pegs/holes range from 1 to 15, inclusive\n";
		return 1;
	}
	if (!betweenInclusive(1, 15, endingPegLocation) && (endingPegLocation != -1))
	{
		std::cerr << "Invalid input. Valid pegs/holes range from 1 to 15, inclusive\n";
		return 1;
	}

	// Build the board. Reserve 16 spaces.
	std::vector<char> board(16);
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
	auto original = board;

	// Now, solve the puzzle!
	if (solve(board, endingPegLocation))
	{
		std::cout << "Initial board\n";
		std::cout << printBoard(original) << "\n";

		// Print the moves and board to the output. The moves are in reverse
		// order due to the recursion. The board states are not.
		for (int i = jumps.size() - 1, j = 0; i > -1; --i, ++j)
		{
			std::cout << jumps[i] << "\n";
			std::cout << printBoard(boards[j]) << "\n";
		}
	}
	else
	{
		std::cout << "No solution could be found for this combination\n";
	}
}

static bool betweenInclusive(const int& lower, const int& upper, const int& num)
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

template<typename T>
static int count(const std::vector<T>& array, const T& value)
{
	int count = 0;
	for (const T& item : array)
	{
		if (item == value)
		{
			count++;
		}
	}
	return count;
}

static std::string printBoard(const std::vector<char>& board)
{
	std::stringstream ss;
	ss << "    " << board[1] <<  "\n";
	ss << "   " << board[2] << " " << board[3] << "\n";
	ss << "  " << board[4] << " " << board[5] << " " << board[6] << "\n";
	ss << " " << board[7] << " " << board[8] << " " << board[9] << " " << board[10] << "\n";
	ss << board[11] << " " << board[12] << " " << board[13] << " " << board[14] << " " << board[15];
	return ss.str();
}

static void showHelp()
{
	std::cout << "Usage: ./PuzzlePegs [hole] [ending peg]\n";
	std::cout << "hole: the location of the hole where the game begins, e.g. 13\n";
	std::cout << "ending peg: the location of the last peg, e.g. 13\n";
}

static bool solve(std::vector<char>& board, const int& endPos)
{
	for (auto move : moves)
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
			auto clone = board;
			boards.push_back(clone);

			// Call ourselves recursively. If we return true then the
			// conclusion was good. If it was false, we hit a dead end and we
			// shouldn't print the move
			if (solve(board, endPos))
			{
				// Record the jump, since it was a successful one
				std::stringstream ss;
				ss << "Moved " << move[0] << " to " << move[2] << ", jumping over " << move[1];
				jumps.push_back(ss.str());
				return true;
			}

			// If we end up here, undo the move and try the next one
			auto result = std::find(boards.begin(), boards.end(), clone);
			if (result != boards.end())
			{
				boards.erase(result);
			}
			board[move[0]] = PEG;
			board[move[1]] = PEG;
			board[move[2]] = HOLE;
		}
	}
	// If no pattern is matched, see if there is only one puzzle peg
	// left and see if it is in the right spot.
	// Count of PEG is 1 and the ending position was not specified
	auto pegCount = count(board, PEG);
	if ((pegCount == 1) && (endPos == -1))
	{
		return true;
	}
	// Count of PEG is 1 and the value at the ending position was PEG
	else if ((pegCount == 1) && (board[endPos] == PEG))
	{
		return true;
	}
	// Count of PEG was not 1 or the value at the ending position was not PEG
	else
	{
		return false;
	}
}
