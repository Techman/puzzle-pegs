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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.techmansworld.puzzlepegs;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.helper.HelpScreenException;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 * Main application
 */
public class App {
    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("PuzzlePegs").build()
                .defaultHelp(true)
                .description("A program that solves the 15-hole peg game (triangular board)");
        parser.addArgument("-s", "--start_pos")
                .type(Integer.class)
                .setDefault(13)
                .required(true)
                .choices(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
                .help("The location of the starting hole, e.g. 13");
        parser.addArgument("-e", "--end_pos")
                .type(Integer.class)
                .setDefault(13)
                .required(true)
                .choices(-1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
                .help("The location of the last peg, e.g. 13");

        try {
            var parsed = parser.parseArgs(args);
            var startPos = parsed.getInt("start_pos");
            var endPos = parsed.getInt("end_pos");

            // Build the puzzle!
            var puzzle = new PuzzlePegs(startPos, endPos);

            // Solve the puzzle!
            puzzle.solve();
        } catch (HelpScreenException e) {
            // This is triggered when someone types --help, so this is technically not an
            // error
            return;
        } catch (ArgumentParserException e) {
            System.err.println("An error occurred parsing command line arguments:");
            System.err.println(e.getLocalizedMessage());
            System.out.println("Run this program with --help to read about available arguments");
        } catch (IllegalArgumentException e) {
            System.err.println("An error constructing the puzzle has occurred:");
            System.err.println(e.getLocalizedMessage());
        }
    }
}
