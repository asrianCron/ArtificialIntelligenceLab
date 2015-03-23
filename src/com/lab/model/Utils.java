package com.lab.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	public static Node aStarSearch(Set<Node> closed, List<Node> fringe){
		while(!fringe.isEmpty()){
			if(first(fringe) == null){
				return null;
			}
			Node currentNode = new Node(first(fringe));
			fringe.remove(first(fringe));

			if(currentNode.getCurrentState().equals(Node.solutionState)){
				return currentNode;
			}
			if(!closed.contains(currentNode)){
				closed.add(currentNode);
				fringe.addAll(expand(currentNode));
			}
		}
		return null;
	}
	
	public static Node first(List<Node> fringe){
		Node first = fringe.get(0);
		for(int i=0;i<fringe.size();i++){
			if(Utils.getManhattan(first.getCurrentState(),Node.solutionState) + first.getCost() < Utils.getManhattan(fringe.get(i).getCurrentState(), Node.solutionState) + fringe.get(i).getCost()){
				first = fringe.get(i);
			}
		}
		return first;
	}
	
	public static Node aStarSearch(Set<Node> closed, SortedSet<Node> fringe){
		while(!fringe.isEmpty()){
			Node currentNode = new Node(fringe.first());
			fringe.remove(fringe.first());
			if(currentNode.getCurrentState().equals(Node.solutionState)){
				return currentNode;
			}
			if(!closed.contains(currentNode)){
				closed.add(currentNode);
				fringe.addAll(expand(currentNode));
			}
		}
		return null;
	}
	
	/**
	 * Current algorithm for finding solution, uses a sorted set for current search strategy
	 * 
	 * @param solution
	 * @param closed
	 * @param fringe
	 * @return
	 */
	public static Node graphSearch(Grid solution, Set<Node> closed, SortedSet<Node> fringe) {
		while (!fringe.isEmpty()) {
			Node currentNode = fringe.first();
			fringe.remove(fringe.first());
			if (currentNode.getCurrentState().equals(solution)) {
				return currentNode;
			}
			if (!closed.contains(currentNode)) {
				closed.add(currentNode);
				fringe.addAll(expand(currentNode));
//				System.out.println("FRINGE EXPANDED: ");
//				System.out.println(fringe);
			}
		}
		return null;
	}
	/**
	 * Current algorithm for finding solution, uses Graph-Search strategy for
	 * expanding states
	 * 
	 * @param solution
	 * @param closed
	 * @param fringe
	 * @return
	 */
	public static Node graphSearch(Grid solution, Set<Node> closed, Deque<Node> fringe) {
		while (!fringe.isEmpty()) {
			Node currentNode = fringe.removeFirst();
			if (currentNode.getCurrentState().equals(solution)) {
				return currentNode;
			}
			if (!closed.contains(currentNode)) {
				closed.add(currentNode);
				fringe.addAll(expand(currentNode));
			}
		}
		return null;
	}

	public static int getManhattan(Grid puzzle, Grid solution) {
		int result = 0;
		for (int i = 0; i < solution.size(); i++) {
			for (int j = 0; j < solution.size(); j++) {
				Position puzzleElementPos = puzzle.getPosition(solution.getGrid()[i][j]);
//				System.out.println(puzzleElementPos);
				result += heuristicFunction(i, j, puzzleElementPos.getI(), puzzleElementPos.getJ());
			}
		}
		return result;
	}
	
	public static int heuristicFunction(int i1, int j1, int i2, int j2){
//		System.out.println(Math.abs(i1 - i2) + Math.abs(j1 - j2));
		return Math.abs(i1 - i2) + Math.abs(j1 - j2);
	}

	/**
	 * Apply a random number of available moves to a grid state
	 * 
	 * @param originalGrid
	 * @param times
	 * @return
	 */
	public static Grid mixItUp(Grid originalGrid, int times) {
		Grid result = new Grid(originalGrid);
		for (int i = 0; i < times; i++) {
			List<Move> possibleMoves = getPossibleMoves(result);
			result = doMove(result, possibleMoves.get(getRandInt(0, possibleMoves.size() - 1)));
		}
		return result;
	}

	/**
	 * Returns a multidimensional array containing the puzzle state, from file
	 * 
	 * @param path
	 * @return
	 */
	public static int[][] getPuzzleGrid(String path) {
		Path puzzlePath = Paths.get(path);
		int[][] result = null;
		if (Files.exists(puzzlePath)) {
			result = getGrid(puzzlePath);
			return result;
		} else {
			System.out.println("Puzzle input file doesn't exist, exiting");
			return null;
		}
	}

	/**
	 * If any of the states from the given set is a solution, returns that
	 * solution
	 * 
	 * @param currentStates
	 * @param solution
	 * @return
	 */
	public static Node getSolution(Set<Node> currentStates, Grid solution) {
		// System.out.println("Searching for solution in set: " +
		// currentStates);
		for (Node node : currentStates) {
			if (node.getCurrentState().equals(solution)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * If any of the states from the given list is a solution, returns that
	 * solution
	 * 
	 * @param currentStates
	 * @param solution
	 * @return
	 */
	public static Node getSolution(List<Node> currentStates, Grid solution) {
		for (Node node : currentStates) {
			if (node.getCurrentState().equals(solution)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Expands the current node, using available moves.
	 * 
	 * @param current
	 * @return list containing all the expanded nodes
	 */
	public static List<Node> expand(Node current) {
		if (current != null) {
			List<Node> expandedNodes = new ArrayList<Node>();
			List<Move> possibleMoves = getPossibleMoves(current.getCurrentState());
			for (Move move : possibleMoves) {
				Node node = new Node();
				node.setCurrentState(doMove(current.getCurrentState(), move));
				node.setParent(current);
				node.setCost(current.getCost() + 1);
				node.setMove(new Move(move));
				expandedNodes.add(node);
			}
			return expandedNodes;
		}

		return null;
	}

	/**
	 * Applies valid move to a grid state
	 * 
	 * @param current
	 * @param move
	 * @return
	 */
	public static Grid doMove(Grid current, Move move) {
		// Node parent = current.getParent();
		if (current != null) {
			Grid newGrid = new Grid(current);
			int[][] stateGrid = newGrid.getGrid();
			Position from = move.getFrom();
			Position to = move.getTo();

			int fromVal = stateGrid[from.getI()][from.getJ()];
			int toVal = stateGrid[to.getI()][to.getJ()];
			stateGrid[from.getI()][from.getJ()] = toVal;
			stateGrid[to.getI()][to.getJ()] = fromVal;

			newGrid.setGrid(stateGrid); // ??
			return newGrid;
		}
		return null;
	}

	/**
	 * Returns a list containing all possible moves that can be done on the
	 * current grid
	 * 
	 * @param state
	 * @return
	 */
	public static List<Move> getPossibleMoves(Grid state) {
		List<Move> moves = new ArrayList<Move>();
		Position blank = state.getBlankPosition();
		// System.out.println(blank);

		// looking up
		if (!(blank.getI() <= 0)) {
			// System.out.println("UP");
			moves.add(new Move(new Position(blank.getI() - 1, blank.getJ()), blank, 1));
		}

		// looking right
		if (!(blank.getJ() >= state.size() - 1)) {
			// System.out.println("RIGHT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() + 1), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking down
		if (!(blank.getI() >= state.size() - 1)) {
			// System.out.println("DOWN");
			moves.add(new Move(new Position(blank.getI() + 1, blank.getJ()), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking left
		if (!(blank.getJ() <= 0)) {
			// System.out.println("LEFT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() - 1), new Position(blank.getI(), blank.getJ()), 1));
		}
		return moves;
	}

	/**
	 * Checks if the current grid state is solvable, by looking at the number of
	 * inversion for this state
	 * 
	 * @param puzzle
	 * @return
	 */
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

	/**
	 * 
	 * @param puzzle
	 * @return number of inversion in the current grid state
	 */
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

		Pattern patt = Pattern.compile("-?[0-9]+"); // the "-" symbol once or
													// not at all, and any digit
													// following that
		List<String> nrs = new ArrayList<String>();
		for (String str : puzzleStrings) {
			Matcher match = patt.matcher(str);
			while (match.find()) {
				nrs.add(match.group());
			}
		}

		int puzzleSideLength = (int) Math.sqrt(nrs.size());
		int index = 0;
		int[][] numbers = new int[puzzleSideLength][puzzleSideLength];
		for (int i = 0; i < puzzleSideLength; i++) {
			for (int j = 0; j < puzzleSideLength; j++) {
				numbers[i][j] = Integer.parseInt(nrs.get(index));
				index += 1;
			}
		}
		return numbers;
	}

	/**
	 * Returns a random integer, including the given parameters
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandInt(int min, int max) {
		return (int) Math.round(Math.random() * (max - min) + min);
	}
}
