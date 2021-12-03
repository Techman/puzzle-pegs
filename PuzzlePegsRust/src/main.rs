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
use clap::{Arg, App};
use regex::Regex;

fn main() {
    // Create regular expressions for valid input
    // start_pos: can be anything from 1 to 15, inclusive
    // end_pos: can be anything from 1 to 15, inclusive OR -1 if ending position does not matter
    let start_pos_regex = Regex::new(r"(?m)^([1-9]|1[0-5])$").unwrap();
    let end_pos_regex = Regex::new(r"(?m)^(-1|[1-9]|1[0-5])$").unwrap();

    // Build the command line arguments
    let matches = App::new("Puzzle Pegs")
        .author("Michael Hazell <michaelhazell@hotmail.com>")
        .about("A program that solves the 15-hole peg game (triangular board)")
        .arg(Arg::new("start_pos")
            .short('s')
            .long("start_pos")
            .value_name("start_pos")
            .about("The location of the starting hole in the board, e.g. 13")
            .takes_value(true)
            .required(true)
            .multiple_occurrences(false)
            .validator_regex(&start_pos_regex,
                "Starting hole must range from 1 to 15 (inclusive)"))
        .arg(Arg::new("end_pos")
            .short('e')
            .long("end_pos")
            .value_name("end_pos")
            .about("The location of the last peg, e.g. 13")
            .takes_value(true)
            .required(true)
            .multiple_occurrences(false)
            .validator_regex(&end_pos_regex,
                "Ending peg must range from 1 to 15 (inclusive) or -1 for any location"))
        .get_matches();

    // Extract start_pos and end_pos from args
    // App::values_of returns a Result<T, Err>, and then parsing returns Result<F, F::Err>, so
    // both have to be unwrap()'d
    let start_pos: i32 = matches.value_of("start_pos").unwrap().parse().unwrap();
    let end_pos: i32 = matches.value_of("end_pos").unwrap().parse().unwrap();

    // Now, for construction of the puzzle!
    let mut puzzle = PuzzlePegs::new(start_pos, end_pos);

    // And solving it.
    // Solving changes the puzzle's internal state, so it has to be mutable
    puzzle.solve();
}
