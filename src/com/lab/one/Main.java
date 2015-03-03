package com.lab.one;

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
			System.out.println();
//			Node test1 = new Node(initialPuzzle, null, new Move(), 2);
//			Node test4 = new Node(new Grid(initialPuzzle), null, null, 232);
//			Node test2 = new Node(initialPuzzle, new Node(), null, 0);
//			Node test3 = new Node(solution, new Node(), new Move(), 3);
//			System.out.println(test1.hashCode());
//			System.out.println(test4.hashCode());
			
//			System.out.println(getPossibleMoves(initialPuzzle));
//			System.out.println(initialPuzzle);
			Node initialNode = new Node(initialPuzzle, null, null, 0);
//			System.out.println(initialNode);
			Set<Node> expanded = new HashSet<Node>();
			expanded.addAll(expand(initialNode));
//			List<Node> expandedNodes = expand(initialNode);
//			List<Node> auxNodes = new ArrayList<Node>();
			

			Set<Node> auxNodes = new HashSet<Node>();
			
			for(int i=0;i<100000;i++){
			Node solutionNode = null;
//				while((solutionNode = getSolution(expanded, solution)) == null){
					for(Node node : expanded){
						auxNodes.addAll(expand(node));
					}
					expanded.addAll(auxNodes);
					auxNodes.clear();
//				}
			}
			
			System.out.println(expanded.size());

			
			
//			for(Node node : expandedNodes){
//				System.out.println(node);
//				System.out.println("Parent " + node.getParent());
//				System.out.println();
//			}
			
		} else {
			System.out.println("Puzzle unsolvable, exiting...");
			return;
		}
	}
	
	public static Node getSolution(Set<Node> currentStates, Grid solution){
//		System.out.println("Searching for solution in set: " + currentStates);
		for(Node node : currentStates){
			if(node.getCurrentState().equals(solution)){
				return node;
			}
		}
		return null;
	}
	
	public static Node getSolution(List<Node> currentStates, Grid solution){
		for(Node node : currentStates){
			if(node.getCurrentState().equals(solution)){
				return node;
			}
		}
		return null;
	}
	
	public static List<Node> expandWithoutPastMove(Node current){
		if(current != null){
			List<Node> expandedNodes = new ArrayList<Node>();
			List<Move> possibleMoves = getPossibleMoves(current.getCurrentState());
			
			for(Move move : possibleMoves){
				if(!move.equals(current.getMove())){
					Node node = new Node();
					node.setCurrentState(doMove(current.getCurrentState(), move));
					node.setParent(current);
					node.setCost(current.getCost() + 1);
					node.setMove(move);
					expandedNodes.add(node);
				}
			}
			return expandedNodes;
		}
		
		return null;
	}
	
	public static List<Node> expand(Node current){
		if(current != null){
			List<Node> expandedNodes = new ArrayList<Node>();
			List<Move> possibleMoves = getPossibleMoves(current.getCurrentState());
			for(Move move : possibleMoves){
				Node node = new Node();
				node.setCurrentState(doMove(current.getCurrentState(), move));
				node.setParent(current);
				node.setCost(current.getCost() + 1);
				node.setMove(move);
				expandedNodes.add(node);
			}
			return expandedNodes;
		}
		
		return null;
	}
	
	public static Grid doMove(Grid current, Move move){
//		Node parent = current.getParent();
		if(current != null){
			Grid state = new Grid(current);
			int[][] stateGrid = state.getGrid();
			Position from = move.getFrom();
			Position to = move.getTo();
			
			int fromVal = stateGrid[from.getI()][from.getJ()];
			int toVal = stateGrid[to.getI()][to.getJ()];
			stateGrid[from.getI()][from.getJ()] = toVal;
			stateGrid[to.getI()][to.getJ()] = fromVal;
			
			return state;
		}
		return null;
	}
	
	
	/*
	 * Returns a list containing all possible moves that can be done on the current grid
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
