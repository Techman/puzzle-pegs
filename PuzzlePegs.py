"""
Puzzle Pegs: Solves 15-peg triangular board game
Copyright (C) 2020 Michael Hazell <michaelhazell@hotmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

"""

import argparse
import copy

parser = argparse.ArgumentParser(
    description='Solve triangular puzzle peg game with 15 pegs')
parser.add_argument('-b', '--begin', type=int, default=13,
                    help='The location of the hole when the game begins, e.g. 13')
parser.add_argument('-e', '--end', type=int, default=13,
                    help='The location of the last peg, e.g. 13')
args = parser.parse_args()


# Constants for representing a peg and a hole
PEG = 'P'
HOLE = 'H'

# Table of all possible moves on this board
MOVES = [
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
boards = []

# History of jumps
jumps = []


def main():
    # Pull from arguments and see if they are within spec
    begin = args.begin
    end = args.end
    if not betweenInclusive(1, 15, begin):
        print('Valid range for location has to be 1 to 15, inclusive')
        exit(1)

    if not betweenInclusive(1, 15, end):
        print('Valid range for location has to be 1 to 15, inclusive')
        exit(1)

    # Build array
    board = []
    board.append(' ')  # This space (board[0]) is not used

    for i in range(1, 16, 1):
        if begin == i:
            board.insert(i, HOLE)
        else:
            board.insert(i, PEG)

    # Store the initial board to show before the moves are printed
    original = copy.deepcopy(board)

    # Now, solve the puzzle! If there is a solution, print it out. If there
    # isn't, mention that
    if solve(board, end):
        # Print the initial board before the moves
        print('Initial board')
        print(printBoard(original))

        # Print the moves and board state to the output. The moves are in
        # reverse order due to recursion. The board states are not.
        j = 0
        for i in range(len(jumps) - 1, -1, -1):
            print(jumps[i])
            print(printBoard(boards[j]))
            j += 1
    else:
        print('No solution could be found for this combination')


def betweenInclusive(lower: int, upper: int, num: int):
    """Check whether a number is in range (inclusive)"""
    if num >= lower and num <= upper:
        return True
    else:
        return False


def count(array: list, value: str):
    """Count occurances of a value within a range (inclusive)"""
    count = 0
    for char in array:
        if char == value:
            count += 1
    return count


def printBoard(board: list):
    """Print the puzzle board"""
    string = ''
    string += '    ' + board[1] + '\n'
    string += '   ' + board[2] + ' ' + board[3] + '\n'
    string += '  ' + board[4] + ' ' + board[5] + ' ' + board[6] + '\n'
    string += ' ' + board[7] + ' ' + board[8] + \
        ' ' + board[9] + ' ' + board[10] + '\n'
    string += board[11] + ' ' + board[12] + ' ' + \
        board[13] + ' ' + board[14] + ' ' + board[15]
    return string


def solve(board: list, end: int):
    """Solve the puzzle board"""
    # Check the valid moves against the board
    for move in MOVES:
        # See if we can math a PPH pattern. If we can, try following this route
        # by calling ourselves again with this modified board
        if board[move[0]] == PEG and board[move[1]] == PEG and board[move[2]] == HOLE:
            # Apply the move
            board[move[0]] = HOLE
            board[move[1]] = HOLE
            board[move[2]] = PEG

            # Record the board as-is by making a copy of it
            clone = copy.deepcopy(board)
            boards.append(clone)

            # Call ourselves recursively, if we return True then the conclusion
            # was good. If it was false, we hit a dead end and we shouldn't
            # print the move
            if solve(board, end):
                jumps.append(
                    'Moved ' + str(move[0]) + ' to ' + str(move[2]) + ', jumping over ' + str(move[1]))
                return True

            # If we end up here, undo the move and try the next one
            if clone in boards:
                boards.remove(clone)

            board[move[0]] = PEG
            board[move[1]] = PEG
            board[move[2]] = HOLE

    # If no pattern is matched, see if there is only one puzzle peg
    # left and see if it is in the right spot.
    # Count of 'P' is 1 and the ending position was not specified
    pegCount = count(board, PEG)
    if pegCount == 1 and end == -1:
        return True
    # Count of 'P' is 1 and the value at the ending position is 'P'
    elif pegCount == 1 and board[end] == PEG:
        return True
    # Count of 'P' was not 1 or the value at the ending position was not 'P'
    else:
        return False

# Run main()
if __name__ == "__main__":
    main()
