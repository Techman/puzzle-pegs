# Puzzle Pegs (Java)

This is the (new-ish) Java version of the Puzzle Pegs program. Puzzle Pegs was originally written in Java as a single `.java` file, but has now been rewritten to be a proper Maven project. This was mostly for experience.

# Build

Maven is the tool used to build the project

To build:

1. In the project root, `mvn install` to install dependencies
2. `mvn assembly:single` to build a packaged `.jar` file, with dependencies included
3. You can run this file by typing `java -jar target/puzzlepegs-1.0-jar-with-dependencies.jar`

## Why?

The original Java version can now be found in the Git history. The rewritten Java version now uses a command line argument parsing library.
