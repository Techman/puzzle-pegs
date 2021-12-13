// Puzzle Pegs: A program that solves the 15-hole peg game (triangular board)
// Copyright (C) 2021 Michael Hazell <michaelhazell@hotmail.com>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

/// Represents a peg game puzzle
pub struct PuzzlePegs {
    // Universal representation of a peg
    peg: char,

    // Universal representation of a hole
    hole: char,

    // Universal table of moves
    // This is only valid for 15-hole boards of triangular shape
    moves: [[i32; 3]; 36],

    // History of boards representing the jumps
    boards: Vec<Vec<char>>,

    // Ending peg location
    end_pos: i32,

    // History of jumps
    jumps: Vec<String>,

    // Starting hole location
    start_pos: i32,
}

impl PuzzlePegs {
    /// Create a puzzle with a starting hole and ending peg location specified
    /// start_pos: Starting position of hole
    /// end_pos: Ending position of final peg
    pub fn new(start_pos: i32, end_pos: i32) -> PuzzlePegs {
        PuzzlePegs {
            peg: 'P',
            hole: 'H',
            moves: [
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
                [15, 14, 13],
            ],
            boards: Vec::new(),
            end_pos,
            jumps: Vec::new(),
            start_pos,
        }
    }

    /// Count occurances of a character within range, inclusive
    /// vector: Vector of characters
    /// value: a chracter
    fn count(vector: &[char], value: &char) -> usize {
        let mut count: usize = 0;
        for &i in vector {
            if i == *value {
                count += 1;
            }
        }
        count
    }

    /// Print the game board in ASCII form
    /// board: The game board
    fn print_board(board: &[char]) {
        println!("    {}", board[1]);
        println!("   {} {}", board[2], board[3]);
        println!("  {} {} {}", board[4], board[5], board[6]);
        println!(" {} {} {} {}", board[7], board[8], board[9], board[10]);
        println!(
            "{} {} {} {} {}",
            board[11], board[12], board[13], board[14], board[15]
        );
    }

    /// Internal recursive function for solving, making use of backtracking
    /// board: The game board
    /// end_pos: The ending peg location
    fn solve_internal(&mut self, board: &mut Vec<char>) -> bool {
        // For every move in the table of possible moves...
        for a_move in self.moves {
            // See if we can match a PPH pattern. If we can, try following this route by calling
            // ourselves again with this modified board
            if (board[a_move[0] as usize] == self.peg)
                && (board[a_move[1] as usize] == self.peg)
                && (board[a_move[2] as usize] == self.hole)
            {
                // Apply the move
                board[a_move[0] as usize] = self.hole;
                board[a_move[1] as usize] = self.hole;
                board[a_move[2] as usize] = self.peg;

                // Record the board in history of boards
                let clone = board.clone();
                self.boards.push(clone);

                // Call ourselves recursively. If we return true then the conclusion was good.
                // If it was false, we hit a dead end and we shouldn't print the move
                if self.solve_internal(board) {
                    self.jumps.push(format!(
                        "Moved {} to {}, jumping over {}",
                        a_move[0], a_move[2], a_move[1]
                    ));
                    return true;
                }

                // If we end up here, undo the move and try the next one
                self.boards.remove(self.boards.len() - 1);
                board[a_move[0] as usize] = self.peg;
                board[a_move[1] as usize] = self.peg;
                board[a_move[2] as usize] = self.hole;
            }
        }

        // If no pattern is matched, see if there is only one peg left and see if it is in the
        // right spot
        // Situation 1: Count of PEG is 1 and the ending position was not specified
        let peg_count = PuzzlePegs::count(board, &self.peg);
        if (peg_count == 1) && (self.end_pos == -1) {
            true
        }
        // Situation 2: Count of PEG is 1 and the value of the ending position was PEG
        else if (peg_count == 1) && (board[self.end_pos as usize] == self.peg) {
            true
        }
        // Situation 3: Count of PEG was not 1 or the value at the ending position was not PEG
        else {
            false
        }
    }

    /// Solve the puzzle
    pub fn solve(&mut self) {
        // Build the board. Reserve 16 spaces.
        let mut board: Vec<char> = Vec::with_capacity(16);
        board.push(' '); // board[0] - "empty" spot so that peg holes line up with array indice
        for i in 1..16 {
            if self.start_pos == i {
                board.push(self.hole);
            } else {
                board.push(self.peg)
            }
        }

        // Store the initial board to show before the moves are printed
        let original = board.clone();

        // Now, solve the puzzle!
        if self.solve_internal(&mut board) {
            println!("Initial board");
            PuzzlePegs::print_board(&original);

            // Print the moves and board to the output. The moves (jumps) are in reverse order
            // due to the recursion. The board states are not.
            self.jumps.reverse();
            for i in 0..self.boards.len() {
                println!("{}", self.jumps[i]);
                PuzzlePegs::print_board(&self.boards[i]);
            }
        } else {
            println!("No solution could be found for this combination");
        }
    }
}
