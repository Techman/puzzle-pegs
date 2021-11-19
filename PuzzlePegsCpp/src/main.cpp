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
#include <string>

/** Check whether a value within range (inclusive)
 * @param lower The lower bound
 * @param upper The upper bound
 * @param num The value to check within the range
 * @return true if the value is in range, false otherwise
 */
template<typename T>
bool between_inclusive(const T& lower, const T& upper, const T& value);

int main(int argc, char *argv[])
{
	int starting_hole_location;
	int ending_peg_location;

	try
	{
		switch (argc)
		{
		// ./PuzzlePegs 13 13 -- would solve the game with the starting hole
		// at position 13, and the last peg also at position 13
		case 3:
			starting_hole_location = std::stoi(argv[1]);
			ending_peg_location = std::stoi(argv[2]);
			break;

		// ./PuzzlePegs 13 -- would solveP the game with the starting hole at
		// position 13, but the last peg can be anywhere
		case 2:
			starting_hole_location = std::stoi(argv[1]);
			// We use -1 because it is a clear "nothing specified"
			ending_peg_location = -1;
			break;

		// ./PuzzlePegs -- with no args, default to case 1
		case 1:
			starting_hole_location = 13;
			ending_peg_location = -1;
			break;

		// Catch-all for improper input
		default:
			std::cerr << "Too many arguments" << std::endl;
			PuzzlePegs::help();
			return EXIT_FAILURE;
		}
	}
	catch (std::invalid_argument &e)
	{
		std::cerr << "Invalid input. Please use integers as numeric input" << std::endl;
		PuzzlePegs::help();
		return EXIT_FAILURE;
	}

	// Sanitize the actual numbers put in
	if (!between_inclusive(1, 15, starting_hole_location))
	{
		std::cerr << "Invalid input. Valid pegs/holes range from 1 to 15, inclusive" << std::endl;
		PuzzlePegs::help();
		return 1;
	}
	if (!between_inclusive(1, 15, ending_peg_location) && (ending_peg_location != -1))
	{
		std::cerr << "Invalid input. Valid pegs/holes range from 1 to 15, inclusive" << std::endl;
		PuzzlePegs::help();
		return 1;
	}

	// Create puzzle
	auto puzzle = PuzzlePegs(starting_hole_location, ending_peg_location);

	// Solve!
	puzzle.solve();

	return EXIT_SUCCESS;
}

template<typename T>
bool between_inclusive(const T& lower, const T& upper, const T& value)
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
