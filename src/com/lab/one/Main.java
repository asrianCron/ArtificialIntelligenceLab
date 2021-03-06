package com.lab.one;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lab.model.Grid;
import com.lab.model.Node;
import com.lab.model.Utils;

/*
 * Rezolvarea problemei The 15 Puzzle;
 * Rezolvarea implica generarea unui arbore care contine toate miscarile posibile, pana se ajunge la o stare finala valida, in cazul unei tablele 3x3:
 * 1 2 3
 * 4 5 6
 * 7 8 
 * Pentru spatiul alb folosesc numarul -1
 * Intai se verifica daca puzzle-ul are o rezolvare valida
 * Al doilea pas este de a calcula arborele de miscari, incercand sa evit miscari duplicate
 * 
 */
public class Main {
	
	public static void fakeMain(String[] args) {
		String puzzlePath = "res/puzzle.txt";
		String solutionPath = "res/solution.txt";

		LocalDateTime time = LocalDateTime.now(Clock.systemDefaultZone());
		String tm = time.toString();
		tm = tm.replaceAll(":", "-");
		String solutionOutputPath = "res/solution_" + tm + ".txt";

		int times = 0;
		if (args.length == 2) {
			puzzlePath = args[0];
			solutionPath = args[1];
		}
		Grid initialPuzzle = new Grid(Utils.getPuzzleGrid(puzzlePath));
		Grid solution = new Grid(Utils.getPuzzleGrid(solutionPath));

		initialPuzzle = Utils.mixItUp(initialPuzzle, times);

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

			System.out.println("Puzzle solvable, searching for a solution...(" + times + " random moves to start with)\n");
			write(writer, "Puzzle solvable, searching for a solution...(" + times + " random moves to start with)\n");
			System.out.println(initialPuzzle);
			Node initialNode = new Node(initialPuzzle, null, null, 0);
			Set<Node> closed = new HashSet<Node>();
			Deque<Node> fringe = new ArrayDeque<Node>();
			fringe.add(initialNode);

			long startTime = System.nanoTime();

			Node solutionNode = Utils.graphSearch(solution, closed, fringe); // get
																				// things
																				// done

			long endTime = System.nanoTime() - startTime;
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
