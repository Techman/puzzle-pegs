"""
Puzzle Pegs: Solves 15-peg triangular board game
Copyright (C) 2021 Michael Hazell <michaelhazell@hotmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
"""

from argparse import ArgumentParser
from puzzle_pegs import PuzzlePegs

parser = ArgumentParser(
    description='Solve triangular puzzle peg game with 15 pegs')
parser.add_argument('-b', '--begin', type=int, default=13,
                    help='The location of the hole when the game begins, e.g. 13')
parser.add_argument('-e', '--end', type=int, default=13,
                    help='The location of the last peg, e.g. 13')
args = parser.parse_args()


def main():
    """Main method, used for running the program"""

    puzzle = PuzzlePegs(args.begin, args.end)
    puzzle.solve()


# Run main()
if __name__ == "__main__":
    main()
