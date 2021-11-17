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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include "PuzzlePegs.h"
#include <algorithm>
#include <iostream>
#include <sstream>

template<typename T>
bool PuzzlePegs::check_between_inclusive(const T &lower, const T &upper, const T &value)
{
	if ((value >= lower) && (value <= upper))
	{
		return true;
	}
	else
	{
		return false;
	}

}

template<typename T>
int PuzzlePegs::count(const std::vector<T> &vector, const T &value)
{
	int count = 0;
	for (const T& item : vector)
	{
		if (item == value)
		{
			count++;
		}
	}
	return count;
}

void PuzzlePegs::help()
{
	std::cout << "Usage: ./PuzzlePegs [hole] [ending peg]" << std::endl;
	std::cout << "hole: the location of the starting hole in the board, e.g. 13" << std::endl;
	std::cout << "ending peg: the location of the last peg, e.g. 13" << std::endl;
}

void PuzzlePegs::print_board(const std::vector<char>& board)
{
	// This uses printf instead of iostream for easier reading
	std::printf("    %c\n", board[1]);
	std::printf("   %c %c\n", board[2], board[3]);
	std::printf("  %c %c %c\n", board[4], board[5], board[6]);
	std::printf(" %c %c %c %c\n", board[7], board[8], board[9], board[10]);
	std::printf("%c %c %c %c %c\n", board[11], board[12], board[13], board[14], board[15]);
}

PuzzlePegs::PuzzlePegs(int start_pos, int end_pos)
{
	// Check that start_pos and end_pos meet the requirements
	if (!check_between_inclusive(1, 15, start_pos))
	{
		throw std::invalid_argument("Starting hole location must be an integer from 1 to 15, inclusive");
	}

	// end_pos can be -1 if the location is not important
	if (!check_between_inclusive(1, 15, end_pos) && (end_pos != -1))
	{
		throw std::invalid_argument("Ending peg location must be an integer from 1 to 15 (inclusive) or -1 if location does not matter");
	}

	// With the starting hole and ending peg specified, try to solve for a board with these constraints
	this->start_pos = start_pos;
	this->end_pos = end_pos;
}

PuzzlePegs::PuzzlePegs(int start_pos)
{
	// With only the starting hole specified, do not care where the final peg is located
	PuzzlePegs(start_pos, -1);
}

PuzzlePegs::PuzzlePegs()
{
	// With no positional arguments, set up the classic board, not caring where the final peg is located
	PuzzlePegs(13, -1);
}

void PuzzlePegs::solve()
{
	// Build the board. Reserve 16 spaces.
	std::vector<char> board(16);
	board[0] = ' '; // Null "space", this space is not used
	for (int i = 1; i < 16; ++i)
	{
		if (start_pos == i)
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
	if (solve_internal(board, end_pos))
	{
		std::cout << "Initial board" << std::endl;
		print_board(original);

		// Print the moves and board to the output. The moves are in reverse order due to the
		// recursion. The board states are not
		// NOTE: i must be >= 0 instead of > -1 as size_t is unsigned
		for (std::size_t i = jumps.size() - 1, j = 0; i >= 0; --i, ++j)
		{
			std::cout << jumps[i] << std::endl;
			print_board(boards[j]);
		}
	}
	else
	{
		std::cout << "No solution could be found for this combination" << std::endl;
	}
}

bool PuzzlePegs::solve_internal(std::vector<char>& board, const int& end_pos)
{
	// For every move in the table of possible moves...
	for (const auto& move : MOVES)
	{
		// See if we can match a PPH pattern. If we can, try following htis route by calling
		// ourselves again with this modified board
		if ((board[move[0]] == PEG) && (board[move[1]] == PEG) && (board[move[2]] == HOLE))
		{
			// Apply the move
			board[move[0]] = HOLE;
			board[move[1]] = HOLE;
			board[move[2]] = PEG;

			// Record the board in history of boards
			auto clone = board;
			boards.emplace_back(clone);

			// Call ourselves recurisvely. If we return true then the conclusion was good. If it was
			// false, we hit a dead end and we shouldn't print the move
			if (solve_internal(board, end_pos))
			{
				// Record the jump
				std::stringstream ss;
				ss << "Moved " << move[0] << " to " << move[2] << ", jumping over " << move[1];
				jumps.emplace_back(ss.str());
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

	// If no pattern is matched, see if there is only one peg left and see if it is in the right spot
	// Situation 1: Count of PEG is 1 and the ending position was not specified
	auto peg_count = std::count(board.begin(), board.end(), PEG);
	if ((peg_count == 1) && (end_pos == -1))
	{
		return true;
	}
	// Situation 2: Count of PEG is 1 and the value of the ending position was PEG
	else if ((peg_count == 1) && (board[end_pos] == PEG))
	{
		return true;
	}
	// Situation 3: Count of PEG was not 1 or the value at the ending position was not PEG
	else
	{
		return false;
	}
}
