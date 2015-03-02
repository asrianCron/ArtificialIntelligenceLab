package com.lab.one;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
 * Rezolvare a problemei The 15 Puzzle;
 * Rezolvarea implica generarea unui arbore care contine toate miscarile posibile, pana se ajunge la o stare finala valida, in cazul unei tablele 3x3:
 * 1 2 3
 * 4 5 6
 * 7 8 
 * Pentru spatiul alb folosesc numarul -1
 * Intai se verifica daca puzzle-ul are o rezolvare valida
 * Al doilea pas este de a calcula arborele de miscari, incerc sa evit miscari duplicate
 * 
 * 
 */
public class Main {

	public static void main(String[] args) {
		String filePath = "res/puzzle.txt";
		if (args.length >= 1) {
			filePath = args[0];
		}
		Path puzzlePath = Paths.get(filePath);
		int[][] nrs = null;
		if (Files.exists(puzzlePath)) {
			nrs = getGrid(puzzlePath);
		} else {
			System.out.println("Puzzle input file doesn't exist, exiting");
			return;
		}
		Grid initialPuzzle = new Grid(nrs);
		Grid solution = new Grid(getGrid(Paths.get("res/solution.txt")));
		if (isSolvable(initialPuzzle)) {
			System.out.println("Puzzle solvable, searching for a solution...");
			System.out.println(getPossibleMoves(initialPuzzle));
			System.out.println(initialPuzzle);
			while(!initialPuzzle.equals(solution)){
				
			}
		} else {
			System.out.println("Puzzle unsolvable, exiting...");
			return;
		}
	}
	
	public static Grid doMove(Grid originalGrid, Move move){
		return null;
		
		
	}
	
	public static List<Move> getPossibleMoves(Grid state) {
		List<Move> moves = new ArrayList<Move>();
		Position blank = state.getBlankPosition();
		System.out.println(blank);

		// looking up
		if (!(blank.getI() <= 0)) {
			System.out.println("UP");
			moves.add(new Move(new Position(blank.getI() - 1, blank.getJ()), blank, 1));
		}

		// looking right
		if (!(blank.getJ() >= state.size() - 1)) {
			System.out.println("RIGHT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() + 1), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking down
		if (!(blank.getI() >= state.size() - 1)) {
			System.out.println("DOWN");
			moves.add(new Move(new Position(blank.getI() - 1, blank.getJ()), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking left
		if (!(blank.getJ() <= 0)) {
			System.out.println("LEFT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() - 1), new Position(blank.getI(), blank.getJ()), 1));
		}
		// woooah, double rainbow
		return moves;
	}

	public static boolean isSolvable(Grid puzzle) {
		int size = puzzle.size();
		if (size % 2 == 1) {
			return getInversions(puzzle) % 2 == 0;
		} else {
			if ((size - puzzle.getBlankPosition().getI()) % 2 == 0) {
				return getInversions(puzzle) % 2 == 1;
			}
			if ((size - puzzle.getBlankPosition().getI()) % 2 == 1) {
				return getInversions(puzzle) % 2 == 0;
			}
		}

		return false;
	}

	public static int getInversions(Grid puzzle) {
		int[] puzzleRow = puzzle.getGridAsRow();
		int inversions = 0;
		int wht = puzzle.getBlank();
		for (int i = 0; i < puzzleRow.length; i++) {
			if (puzzleRow[i] != wht) {
				for (int j = i; j < puzzleRow.length; j++) {
					if (puzzleRow[j] != wht) {
						if (puzzleRow[i] > puzzleRow[j]) {
							inversions++;
						}
					}
				}
			}
		}
		return inversions;
	}

	private static int[][] getGrid(Path puzzlePath) {
		List<String> puzzleStrings = null;
		try {
			puzzleStrings = Files.readAllLines(puzzlePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int puzzleSideLength = puzzleStrings.get(0).split(" ").length;

		int[][] numbers = new int[puzzleSideLength][puzzleSideLength];
		for (int i = 0; i < puzzleStrings.size(); i++) {
			String[] vect = puzzleStrings.get(i).split(" ");
			List<String> vectr = new ArrayList<String>();
			for (String str : vect) {
				if (!str.trim().isEmpty()) {
					vectr.add(str.trim());
				}
			}
			for (int j = 0; j < vectr.size(); j++) {
				numbers[i][j] = Integer.parseInt(vectr.get(j));
			}
		}
		return numbers;
	}
}
