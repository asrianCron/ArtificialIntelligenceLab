package com.lab.two;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.lab.model.Grid;
import com.lab.model.Move;
import com.lab.model.Node;
import com.lab.model.Utils;

public class Main {

	public static void fakeMain2(String[] args){

		String puzzlePath = "res/puzzle.txt";
		String solutionPath = "res/solution.txt";
		
		Grid initialPuzzle = new Grid(Utils.getPuzzleGrid(puzzlePath));
		Grid solution = new Grid(Utils.getPuzzleGrid(solutionPath));
		System.out.println(Utils.getManhattan(initialPuzzle, solution));
		
		Node puzzle = new Node(initialPuzzle, null, null, 0);
		Node sol = new Node(solution, null, null, 0);
		Node.solutionState = solution;
		System.out.println(puzzle);
		System.out.println(sol);
		System.out.println(puzzle.compareTo(sol));
		SortedSet<Node> fringe = Collections.synchronizedSortedSet(new TreeSet<Node>());
		fringe.add(puzzle);
		fringe.add(sol);
		System.out.println(fringe.first());
	}
	
	
	public static void fakeMain(String[] args) {

		String puzzlePath = "res/puzzle.txt";
		String solutionPath = "res/solution.txt";

		LocalDateTime time = LocalDateTime.now(Clock.systemDefaultZone());
		String tm = time.toString();
		tm = tm.replaceAll(":", "-");
		String solutionOutputPath = "res/solution_" + tm + ".txt";
		
		ArrayList<String> strststs = new ArrayList<String>();
		int times = 0;
		
		if (args.length == 2) {
			puzzlePath = args[0];
			solutionPath = args[1];
		}
		Grid initialPuzzle = new Grid(Utils.getPuzzleGrid(puzzlePath));
		Grid solution = new Grid(Utils.getPuzzleGrid(solutionPath));
		Node.solutionState = solution;

//		initialPuzzle = Utils.mixItUp(initialPuzzle, times);

		if (Utils.isSolvable(initialPuzzle)) {
			try {
				if (Files.exists(Paths.get(solutionOutputPath))) {
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
				e.printStackTrace();
			}

			System.out.println("Puzzle solvable, searching for a solution using A* ...(" + times + " random moves to start with)\n");
			write(writer, "Puzzle solvable, searching for a solution using A* ...(" + times + " random moves to start with)\n");

			Node initialNode = new Node(initialPuzzle, new Node(), new Move(), 0);

			Set<Node> closed = new HashSet<Node>();
			// Deque<Node> fringe = new ArrayDeque<Node>();
//			SortedSet<Node> fringe = Collections.synchronizedSortedSet(new TreeSet<Node>());
			List<Node> fringe = new ArrayList<Node>();
			fringe.add(initialNode);
			long startTime = System.nanoTime();

			Node solutionNode = Utils.aStarSearch(closed, fringe); // get
																				// things
																				// done
			long endTime = System.nanoTime() - startTime;

			if (solutionNode == null) {
				System.out.println("Solution not found");
				write(writer, "Solution not found");

				System.out.println("SOL NOT FOUND; CLOSED:");
				System.out.println(closed);

				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}

			System.out.println("Solution found in " + endTime / 1000000 + " ms: \n");
			write(writer, "Solution found in " + endTime / 1000000 + " : ms\n");

			Deque<Node> solutionDeq = new ArrayDeque<Node>();

			System.out.println("Cost to solution: " + solutionNode.getCost());
			write(writer, "Cost to solution: " + solutionNode.getCost());
			while (solutionNode != null) {
				solutionDeq.add(solutionNode);
				solutionNode = solutionNode.getParent();
			}

			while (!solutionDeq.isEmpty()) {
				Node last = solutionDeq.removeLast();

				write(writer, last.toString());
				write(writer, (last.getMove() == null) ? "null" : last.getMove().toString());

				System.out.println(last);
				System.out.println(last.getMove());
			}
			System.out.println("Closed size: " + closed.size());
			System.out.println("Fringe size: " + fringe.size());
			write(writer, "Closed size: " + closed.size());
			write(writer, "Fringe size: " + fringe.size());
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(Utils.getManhattan(solution, solution));

			// System.out.println("FRINGE:");
			// System.out.println(fringe);
			// System.out.println("CLOSED:");
			// System.out.println(closed);
		} else {
			System.out.println("Puzzle unsolvable, exiting...");
			return;
		}

		//
		// System.out.println("INITIAL PUZZLE: ");
		// System.out.println(initialPuzzle);
		//
		// System.out.println("SOLUTION");
		// System.out.println(solution);
		//
		// // System.out.println(Utils.getManhattan(initialPuzzle, solution));
		//
		// SortedSet<Node> sortedSet = Collections.synchronizedSortedSet(new
		// TreeSet<Node>());
		// Node smallerNode = new Node(initialPuzzle, null, null, 0);
		//
		// initialPuzzle = Utils.mixItUp(initialPuzzle, 1000);
		//
		// Node initialNode = new Node(initialPuzzle, null, null, 0);
		// sortedSet.add(initialNode);
		// sortedSet.add(smallerNode);
		//
		//
		// System.out.println(sortedSet);

	}

	/**
	 * convenience function, write line to opened file
	 * 
	 * @param writer
	 * @param str
	 */
	public static void write(BufferedWriter writer, String str) {
		try {
			writer.write(str + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
