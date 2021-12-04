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

# Preface: a note on private variables/members (they do not exist)
# https://docs.python.org/3/tutorial/classes.html#private-variables

from argparse import ArgumentError
from copy import deepcopy


class PuzzlePegs:
    """ Solves the 15-peg triangular board game"""

    # Universal representation of a peg
    _PEG: str = 'P'

    # Universal representation of a hole
    _HOLE: str = 'H'

    # Universal table of moves
    # This is only valid for 14-hole boards of trangular shape
    _MOVES: list[list[int]] = [
        [1, 2, 4],
        [1, 3, 6],
        [2, 4, 7],
        [2, 5, 9],
        [3, 5, 8],
        [3, 6, 10],
        [4, 2, 1],
        [4, 5, 6],
        [4, 7, 11],
        [4, 8, 13],
        [5, 8, 12],
        [5, 9, 14],
        [6, 3, 1],
        [6, 5, 4],
        [6, 9, 13],
        [6, 10, 15],
        [7, 4, 2],
        [7, 8, 9],
        [8, 5, 3],
        [8, 9, 10],
        [9, 5, 2],
        [9, 8, 7],
        [10, 6, 3],
        [10, 9, 8],
        [11, 7, 4],
        [11, 12, 13],
        [12, 8, 5],
        [12, 13, 14],
        [13, 12, 11],
        [13, 8, 4],
        [13, 9, 6],
        [13, 14, 15],
        [14, 13, 12],
        [14, 9, 5],
        [15, 10, 6],
        [15, 14, 13]
    ]

    # History of boards representing the jumps
    _boards: list[list[str]] = []

    # Ending peg location
    _end_pos: int

    # History of jumps
    _jumps: list[str] = []

    # Starting hole location
    _start_pos: int

    def _check_between_inclusive(self, lower: int, upper: int, value: int) -> bool:
        """Check if a value is between two bounds, inclusive"""
        if (value >= lower) and (value <= upper):
            return True
        else:
            return False

    def _print_board(self, board: list[str]) -> None:
        """Print the game board in ASCII form"""
        string = ''
        string += '    ' + board[1] + '\n'
        string += '   ' + board[2] + ' ' + board[3] + '\n'
        string += '  ' + board[4] + ' ' + board[5] + ' ' + board[6] + '\n'
        string += ' ' + board[7] + ' ' + board[8] + \
            ' ' + board[9] + ' ' + board[10] + '\n'
        string += board[11] + ' ' + board[12] + ' ' + \
            board[13] + ' ' + board[14] + ' ' + board[15]
        print(string)

    def _solve(self, board: list[str], end_pos: int) -> bool:
        """Internal recursive function for solving, making use of backtracking"""
        # For every move in the table of possible moves...
        for move in self._MOVES:
            # See if we can match a PPH pattern. If we can, try following this route by calling
            # ourselves again with this modified board
            if (board[move[0]] == self._PEG) and (board[move[1]] == self._PEG) and (board[move[2]] == self._HOLE):  # pylint: disable=line-too-long
                # Apply the move
                board[move[0]] = self._HOLE
                board[move[1]] = self._HOLE
                board[move[2]] = self._PEG

                # Record the board in history of boards
                clone = deepcopy(board)
                self._boards.append(clone)

                # Call ourselves recursively. If we return true then the conclusion was good. If it
                # was false, we hit a dead end and we should not print the move
                if self._solve(board, end_pos):
                    # Record the jump
                    self._jumps.append(
                        f'Moved {move[0]} to {move[2]}, jumping over {move[1]}')
                    return True

                # If we end up here, undo the move and try the next one
                self._boards.pop()
                board[move[0]] = self._PEG
                board[move[1]] = self._PEG
                board[move[2]] = self._HOLE

        # If no pattern is matched, see if there is only one peg left and see if it is in the
        # right spot
        # Situation 1: count of PEG is 1 and the ending position was not specified
        peg_count: int = board.count(self._PEG)
        if peg_count == 1 and end_pos == -1:
            return True
        # Count of 'P' is 1 and the value at the ending position is 'P'
        elif peg_count == 1 and board[end_pos] == self._PEG:
            return True
        # Count of 'P' was not 1 or the value at the ending position was not 'P'
        else:
            return False

    @staticmethod
    def help():
        """Print help information"""
        print('Usage: PuzzlePegs(start_pos, end_pos)')
        print('start_pos: the location of the starting hole in the board, e.g. 13')
        print('end_pos: the location of the last peg, e.g. 13')

    def __init__(self, start_pos: int, end_pos: int):
        """Create a puzzle with a starting hole and ending peg location specified"""
        if not self._check_between_inclusive(1, 15, start_pos):
            raise ArgumentError(
                None, 'Starting hole location must be an integer from 1 to 15, inclusive')  # pylint: disable=line-too-long

        if (not self._check_between_inclusive(1, 15, end_pos)) and (end_pos != -1):
            raise ArgumentError(
                None, 'Ending peg location must be an integer from 1 to 15 (inclusive) or -1 if location does not matter')  # pylint: disable=line-too-long

        self._start_pos = start_pos
        self._end_pos = end_pos

    def solve(self):
        """Solve the puzzle"""
        # Build the board. Reserve 16 spaces.
        board: list[str] = []
        board.insert(0, ' ')  # Null "space", this space is not used
        for i in range(1, 16, 1):
            if self._start_pos == i:
                board.insert(i, self._HOLE)
            else:
                board.insert(i, self._PEG)

        # Store the initial board to show before the moves are printed
        original = deepcopy(board)

        # Now, solve the puzzle!
        if self._solve(board, self._end_pos):
            print('Initial board')
            self._print_board(original)

            # Print the moves and board to the output. The moves are in reverse order due to the
            # recursion. THe board states are not.
            j: int = 0
            for i in range(len(self._jumps) - 1, -1, -1):
                print(self._jumps[i])
                self._print_board(self._boards[j])
                j += 1
        else:
            print('No solution could be found for this combination')
