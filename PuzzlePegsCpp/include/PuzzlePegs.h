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

#include <stdexcept>
#include <vector>

#pragma once

class PuzzlePegs
{
private:
	// Universal representation of a peg
	const char PEG = 'P';

	// Universal representation of a hole
	const char HOLE = 'H';

	// Universal table of moves
	// This is only valid for 15-hole boards of triangular shape
	const std::vector<std::vector<int>> MOVES = {
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

	// History of boards representing the jumps
	std::vector<std::vector<char>> boards;

	// Ending peg location
	int end_pos;

	// History of jumps
	std::vector<std::string> jumps;

	// Starting hole location
	int start_pos;

	/**
	 * @brief Check if a value is between two bounds, inclusive
	 * @param lower The lower bound
	 * @param upper The upper bound
	 * @param value The value to be checked against
	 * @return true if within bounds, false otherwise
	 */
	template<typename T>
	bool check_between_inclusive(const T& lower, const T& upper, const T& value);

	/**
	 * @brief Count occurances of a value within a range, inclusive
	 * @param vector Vector of values
	 * @param value The value to check against
	 */
	template<typename T>
	int count(const std::vector<T>& vector, const T& value);

	/**
	 * @brief Print the game board in ASCII form
	 * @param board The game board
	 */
	void print_board(const std::vector<char>& board);

	/**
	 * @brief Internal recursive function for solving, making use of backtracking
	 * @param board The game board
	 * @param end_pos The ending peg location
	 */
	bool solve_internal(std::vector<char>& board, const int& end_pos);

public:
	/**
	 * @brief Print help information
	 */
	static void help();

	/** Constructor
	 * @brief Create a puzzle with a starting hole and ending peg location specified
	 * @param start_pos Starting position of hole
	 * @param end_pos Ending position of final peg
	 * @exception std::invalid_argument If start_pos or end_pos are not between 1 to 15 inclusive, or -1 in the case of end_pos
	 * (end_pos can be -1 when the final peg location does not matter)
	 */
	PuzzlePegs(int start_pos, int end_pos);

	/** Constructor
	 * @brief Create a puzzle with a starting hole specified, not caring where the final peg is located
	 * @param start_pos Starting position of the hole
	 * @exception std::invalid_argument If start_pos is not between 1 to 15, inclusive
	 */
	PuzzlePegs(int start_pos);

	/** Constructor
	 * @brief Create a "default" puzzle, with starting hole in position 13 and not caring where the final peg is located
	 * This is the classic setup of the board
	 */
	PuzzlePegs();

	/**
	 * @brief Solve the puzzle
	 */
	void solve();
};
