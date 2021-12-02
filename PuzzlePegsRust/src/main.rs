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

// Rust style guide:
// https://doc.rust-lang.org/1.0.0/style/README.html

mod puzzle_pegs;
use puzzle_pegs::PuzzlePegs;

fn main() {
    let mut puzzle = PuzzlePegs::new(1, 2);
    puzzle.solve();
}
