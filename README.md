# SudokuSolverGUI

Author: Arthur Thouzeau  
Date: 3rd November 2014

## Overview

SudokuSolverGUI is a java application with a user interface solving 9*9
Sudoku puzzles using the Recursive Backtracking method. It imports a Sudoku 
from a CSV file, tries to solve it, displays the results and exports them 
back to CSV. The user also has the possibility to generate random Sudokus.

## How to use

First make sure that your input CSV containing the unsolved Sudoku respects the following format:
- values must be comma-separated, without quotes, but can have whitespaces between them
- unassigned values must be 0s
- the grid must be a 9*9 square grid respecting basic Sudoku rules

Then import the project in a Java IDE like Eclipse. You can either run the program from your IDE or export the project into a runnable JAR file and then run the program at the command-line:

> java -jar runnable_name.jar

This program doesn't take any argument.

## Documentation

See the Javadoc.

## Notes

This program uses the OpenCSV library for reading and writing CSV files.

Please the "Notes" document for some explanations on the choices of implementations.
