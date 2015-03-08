package com.lab.one;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Rezolvare a problemei The 15 Puzzle;
 * Rezolvarea implica generarea unui arbore care contine toate miscarile posibile, pana se ajunge la o stare finala valida, in cazul unei tablele 3x3:
 * 1 2 3
 * 4 5 6
 * 7 8 
 * Pentru spatiul alb folosesc numarul -1
 * Intai se verifica daca puzzle-ul are o rezolvare valida
 * Al doilea pas este de a calcula arborele de miscari, incercand sa evit miscari duplicate
 * 
 * 
 */
public class Main {

	public static void main(String[] args) {
		String puzzlePath = "res/puzzle.txt";
		String solutionPath = "res/solution.txt";
		String solutionOutputPath = "res/solution_.txt";
		int times = 0;
		if (args.length == 2) {
			puzzlePath = args[0];
			solutionPath = args[1];
		}
		Grid initialPuzzle = new Grid(getPuzzleGrid(puzzlePath));
		Grid solution = new Grid(getPuzzleGrid(solutionPath));
		
//		initialPuzzle = mixItUp(solution, times);
		
		if (isSolvable(initialPuzzle)) {
			try {
				if(Files.exists(Paths.get(solutionOutputPath))){
					Files.delete(Paths.get(solutionOutputPath));
				}
				Files.createFile(Paths.get(solutionOutputPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedWriter writer = null;
			try {
				writer = Files.newBufferedWriter(Paths.get(solutionOutputPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Puzzle solvable, searching for a solution...(" + times + " random moves to start with)\n");
			write(writer, "Puzzle solvable, searching for a solution...(" + times + " random moves to start with)\n");
			Node initialNode = new Node(initialPuzzle, null, null, 0);
			Set<Node> closed = new HashSet<Node>();
			Deque<Node> fringe = new ArrayDeque<Node>();
			fringe.add(initialNode);
			
			Node solutionNode = graphSearch(solution, closed, fringe);
			
			System.out.println("Solution found: \n");
			write(writer, "Solution found: \n");
			
			Deque<Node> solutionDeq = new ArrayDeque<Node>();
			
			Node sol = solutionNode;
			
			while(sol != null){
				solutionDeq.add(sol);
				sol = sol.getParent();
			}
			
//			while(!solutionDeq.isEmpty()){
//				Node last = solutionDeq.removeLast();
//				System.out.println(last);
//				System.out.println(last.getMove());
//			}
			
			while(!solutionDeq.isEmpty()){
				Node last = solutionDeq.removeLast();

				write(writer, last.toString());
				write(writer, (last.getMove() == null) ? "null" : last.getMove().toString());

				System.out.println(last);
				System.out.println(last.getMove());
			}
			
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			System.out.println("FRINGE:");
//			System.out.println(fringe);
//			System.out.println("CLOSED:");
//			System.out.println(closed);			
		} else {
			System.out.println("Puzzle unsolvable, exiting...");
			return;
		}
	}
	
	
	/**
	 * convenience function, write line to opened file
	 * @param writer
	 * @param str
	 */
	public static void write(BufferedWriter writer, String str){
		try {
			writer.write(str + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Apply a random number of available moves to a grid state
	 * @param originalGrid
	 * @param times
	 * @return
	 */
	public static Grid mixItUp(Grid originalGrid, int times){
		Grid result = new Grid(originalGrid);
		for(int i=0;i<times;i++){
			List<Move> possibleMoves = getPossibleMoves(result);
			result = doMove(result, possibleMoves.get(getRandInt(0, possibleMoves.size() - 1)));
		}
		return result;
	}
	
	
	/**
	 * Returns a multidimensional array containing the puzzle state, from file
	 * @param path
	 * @return
	 */
	public static int[][] getPuzzleGrid(String path){
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
	 * Current algorith for finding solution, uses Graph-Search strategy for expanding states
	 * @param solution
	 * @param closed
	 * @param fringe
	 * @return
	 */
	public static Node graphSearch(Grid solution, Set<Node> closed, Deque<Node> fringe){
		while(!fringe.isEmpty()){
			Node currentNode = fringe.removeFirst();
			if(currentNode.getCurrentState().equals(solution)){
				return currentNode;
			}
			if(!closed.contains(currentNode)){
//				System.out.println("EXPANDING:");
//				System.out.println(currentNode);
//				System.out.println("EXPANDED:");
//				System.out.println(expand(currentNode));
				closed.add(currentNode);
				fringe.addAll(expand(currentNode));
			}
		}
		return null;
	}
	
	/**
	 * If any of the states from the given set is a solution, returns that solution
	 * @param currentStates
	 * @param solution
	 * @return
	 */
	public static Node getSolution(Set<Node> currentStates, Grid solution){
//		System.out.println("Searching for solution in set: " + currentStates);
		for(Node node : currentStates){
			if(node.getCurrentState().equals(solution)){
				return node;
			}
		}
		return null;
	}
	/**
	 * If any of the states from the given list is a solution, returns that solution
	 * @param currentStates
	 * @param solution
	 * @return
	 */
	public static Node getSolution(List<Node> currentStates, Grid solution){
		for(Node node : currentStates){
			if(node.getCurrentState().equals(solution)){
				return node;
			}
		}
		return null;
	}
	
	/**
	 * Expands the current node, using available moves. 
	 * @param current
	 * @return list containing all the expanded nodes
	 */
	public static List<Node> expand(Node current){
		if(current != null){
			List<Node> expandedNodes = new ArrayList<Node>();
			List<Move> possibleMoves = getPossibleMoves(current.getCurrentState());
			for(Move move : possibleMoves){
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
	 * @param current
	 * @param move
	 * @return
	 */
	public static Grid doMove(Grid current, Move move){
//		Node parent = current.getParent();
		if(current != null){
			Grid newGrid = new Grid(current);
			int[][] stateGrid = newGrid.getGrid();
			Position from = move.getFrom();
			Position to = move.getTo();
			
			int fromVal = stateGrid[from.getI()][from.getJ()];
			int toVal = stateGrid[to.getI()][to.getJ()];
			stateGrid[from.getI()][from.getJ()] = toVal;
			stateGrid[to.getI()][to.getJ()] = fromVal;
			
			newGrid.setGrid(stateGrid); //??
			return newGrid;
		}
		return null;
	}
	
	
	/**
	 * Returns a list containing all possible moves that can be done on the current grid
	 * @param state
	 * @return
	 */
	public static List<Move> getPossibleMoves(Grid state) {
		List<Move> moves = new ArrayList<Move>();
		Position blank = state.getBlankPosition();
//		System.out.println(blank);

		// looking up
		if (!(blank.getI() <= 0)) {
//			System.out.println("UP");
			moves.add(new Move(new Position(blank.getI() - 1, blank.getJ()), blank, 1));
		}

		// looking right
		if (!(blank.getJ() >= state.size() - 1)) {
//			System.out.println("RIGHT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() + 1), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking down
		if (!(blank.getI() >= state.size() - 1)) {
//			System.out.println("DOWN");
			moves.add(new Move(new Position(blank.getI() + 1, blank.getJ()), new Position(blank.getI(), blank.getJ()), 1));
		}

		// looking left
		if (!(blank.getJ() <= 0)) {
//			System.out.println("LEFT");
			moves.add(new Move(new Position(blank.getI(), blank.getJ() - 1), new Position(blank.getI(), blank.getJ()), 1));
		}
		return moves;
	}

	
	/**
	 * Checks if the current grid state is solvable, by looking at the number of inversion for this state
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
	
	/**
	 * Returns a random integer, including the given parameters
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandInt(int min, int max) {
		return (int) Math.round(Math.random() * (max - min) + min);
	}
	
}
