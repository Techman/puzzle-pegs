cmake_minimum_required(VERSION 3.10)

# Project name
project(PuzzlePegs)

# Include directories
include_directories(include)
# target_include_directories(PuzzlePegs PUBLIC ${CMAKE_SOURCE_DIR}/include)
add_subdirectory(src bin)
