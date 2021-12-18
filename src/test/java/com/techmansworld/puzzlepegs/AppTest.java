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

import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for Puzzle Pegs
 */
public class AppTest {

    /**
     * Test known-good combinations of start positions and end positions
     */
    @Test
    public void testGoodLocations() {
        int[][] knownGoodSolvablePositions = {
                { 1, 13 },
                { 1, 10 },
                { 13, 13 }
        };
        for (var location : knownGoodSolvablePositions) {
            var puzzle = new PuzzlePegs(location[0], location[1]);
            assertTrue(puzzle.solve());
        }
    }

    /**
     * Test known-bad combinations of start positions and end positions
     */
    @Test
    public void testBadLocations() {
        int[][] knownBadPositions = {
                { 1, 2 },
                { 1, 3 },
                { 3, 11 }
        };
        for (var location : knownBadPositions) {
            var puzzle = new PuzzlePegs(location[0], location[1]);
            assertFalse(puzzle.solve());
        }
    }

    /**
     * Test all locations
     * For fun
     *
     * Results from when test was initially written:
     * [1, 1] = true
     * [1, 2] = false
     * [1, 3] = false
     * [1, 4] = false
     * [1, 5] = false
     * [1, 6] = false
     * [1, 7] = true
     * [1, 8] = false
     * [1, 9] = false
     * [1, 10] = true
     * [1, 11] = false
     * [1, 12] = false
     * [1, 13] = true
     * [1, 14] = false
     * [1, 15] = false
     * [2, 1] = false
     * [2, 2] = true
     * [2, 3] = false
     * [2, 4] = false
     * [2, 5] = false
     * [2, 6] = true
     * [2, 7] = false
     * [2, 8] = false
     * [2, 9] = false
     * [2, 10] = false
     * [2, 11] = true
     * [2, 12] = false
     * [2, 13] = false
     * [2, 14] = true
     * [2, 15] = false
     * [3, 1] = false
     * [3, 2] = false
     * [3, 3] = true
     * [3, 4] = true
     * [3, 5] = false
     * [3, 6] = false
     * [3, 7] = false
     * [3, 8] = false
     * [3, 9] = false
     * [3, 10] = false
     * [3, 11] = false
     * [3, 12] = true
     * [3, 13] = false
     * [3, 14] = false
     * [3, 15] = true
     * [4, 1] = false
     * [4, 2] = false
     * [4, 3] = true
     * [4, 4] = true
     * [4, 5] = false
     * [4, 6] = false
     * [4, 7] = false
     * [4, 8] = false
     * [4, 9] = true
     * [4, 10] = false
     * [4, 11] = false
     * [4, 12] = true
     * [4, 13] = false
     * [4, 14] = false
     * [4, 15] = true
     * [5, 1] = false
     * [5, 2] = false
     * [5, 3] = false
     * [5, 4] = false
     * [5, 5] = false
     * [5, 6] = false
     * [5, 7] = false
     * [5, 8] = false
     * [5, 9] = false
     * [5, 10] = false
     * [5, 11] = false
     * [5, 12] = false
     * [5, 13] = true
     * [5, 14] = false
     * [5, 15] = false
     * [6, 1] = false
     * [6, 2] = true
     * [6, 3] = false
     * [6, 4] = false
     * [6, 5] = false
     * [6, 6] = true
     * [6, 7] = false
     * [6, 8] = true
     * [6, 9] = false
     * [6, 10] = false
     * [6, 11] = true
     * [6, 12] = false
     * [6, 13] = false
     * [6, 14] = true
     * [6, 15] = false
     * [7, 1] = true
     * [7, 2] = false
     * [7, 3] = false
     * [7, 4] = false
     * [7, 5] = false
     * [7, 6] = false
     * [7, 7] = true
     * [7, 8] = false
     * [7, 9] = false
     * [7, 10] = true
     * [7, 11] = false
     * [7, 12] = false
     * [7, 13] = true
     * [7, 14] = false
     * [7, 15] = false
     * [8, 1] = false
     * [8, 2] = false
     * [8, 3] = false
     * [8, 4] = false
     * [8, 5] = false
     * [8, 6] = true
     * [8, 7] = false
     * [8, 8] = false
     * [8, 9] = false
     * [8, 10] = false
     * [8, 11] = false
     * [8, 12] = false
     * [8, 13] = false
     * [8, 14] = false
     * [8, 15] = false
     * [9, 1] = false
     * [9, 2] = false
     * [9, 3] = false
     * [9, 4] = true
     * [9, 5] = false
     * [9, 6] = false
     * [9, 7] = false
     * [9, 8] = false
     * [9, 9] = false
     * [9, 10] = false
     * [9, 11] = false
     * [9, 12] = false
     * [9, 13] = false
     * [9, 14] = false
     * [9, 15] = false
     * [10, 1] = true
     * [10, 2] = false
     * [10, 3] = false
     * [10, 4] = false
     * [10, 5] = false
     * [10, 6] = false
     * [10, 7] = true
     * [10, 8] = false
     * [10, 9] = false
     * [10, 10] = true
     * [10, 11] = false
     * [10, 12] = false
     * [10, 13] = true
     * [10, 14] = false
     * [10, 15] = false
     * [11, 1] = false
     * [11, 2] = true
     * [11, 3] = false
     * [11, 4] = false
     * [11, 5] = false
     * [11, 6] = true
     * [11, 7] = false
     * [11, 8] = false
     * [11, 9] = false
     * [11, 10] = false
     * [11, 11] = true
     * [11, 12] = false
     * [11, 13] = false
     * [11, 14] = true
     * [11, 15] = false
     * [12, 1] = false
     * [12, 2] = false
     * [12, 3] = true
     * [12, 4] = true
     * [12, 5] = false
     * [12, 6] = false
     * [12, 7] = false
     * [12, 8] = false
     * [12, 9] = false
     * [12, 10] = false
     * [12, 11] = false
     * [12, 12] = true
     * [12, 13] = false
     * [12, 14] = false
     * [12, 15] = true
     * [13, 1] = true
     * [13, 2] = false
     * [13, 3] = false
     * [13, 4] = false
     * [13, 5] = true
     * [13, 6] = false
     * [13, 7] = true
     * [13, 8] = false
     * [13, 9] = false
     * [13, 10] = true
     * [13, 11] = false
     * [13, 12] = false
     * [13, 13] = true
     * [13, 14] = false
     * [13, 15] = false
     * [14, 1] = false
     * [14, 2] = true
     * [14, 3] = false
     * [14, 4] = false
     * [14, 5] = false
     * [14, 6] = true
     * [14, 7] = false
     * [14, 8] = false
     * [14, 9] = false
     * [14, 10] = false
     * [14, 11] = true
     * [14, 12] = false
     * [14, 13] = false
     * [14, 14] = true
     * [14, 15] = false
     * [15, 1] = false
     * [15, 2] = false
     * [15, 3] = true
     * [15, 4] = true
     * [15, 5] = false
     * [15, 6] = false
     * [15, 7] = false
     * [15, 8] = false
     * [15, 9] = false
     * [15, 10] = false
     * [15, 11] = false
     * [15, 12] = true
     * [15, 13] = false
     * [15, 14] = false
     * [15, 15] = true
     */
    // @Test
    public void testAllLocations() {
        List<String> results = new ArrayList<>();
        for (int i = 1; i < 16; ++i) {
            for (int j = 1; j < 16; ++j) {
                var puzzle = new PuzzlePegs(i, j);
                results.add("[" + i + ", " + j + "] = " + puzzle.solve());
            }
        }
        for (var result : results) {
            System.out.println(result);
        }
    }
}
