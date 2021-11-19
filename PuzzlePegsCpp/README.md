# Puzzle Pegs (C++)

This is the C++ version of the Puzzle Pegs program. It has had a few iterations, from originally using top-level functions to now using a class to represent the entire puzzle.

## Build
CMake is now used as the build system for the project. I did this on purpose to learn more about it. (It is definitely more complicated than needed for a small project like this.)

To build:

1. In the project root, `mkdir build` or otherwise create the `build` directory
2. `cd build` (navigate inside)
3. Run `cmake ..` to run CMake, using the files in the project root.
4. Run `make` (at least on UNIX operating systems)

The compiled binary will be located in the `build/bin` directory.
