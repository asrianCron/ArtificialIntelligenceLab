package com.lab.one;
/*
 * 	a.	Stare: reprezinta starea tablei de puzzle
	b.	Nod-parinte: reprezinta o legatura catre nodul corespunzator starii din care s-a generat starea curenta
	c.	Actiune: actiunea care a fost aplicata starii parinte pentru a se ajunge in starea curenta
	d	Path-cost: costul (numarul de mutari efectuate) pentru a se ajunge din starea initiala in starea curenta

 */
public class Node {
	private Grid currentState;
	private Node parent;
	private Move move;
	private int cost;
	
	public Node(){
		this.currentState = new Grid();
		this.parent = null;
		this.move = null;
		this.cost = 0;
	}
	
	public Node(Grid state, Node parent, Move move, int cost){
		this.currentState = state;
		this.parent = parent;
		this.move = move;
		this.cost = cost;
	}

	public Grid getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Grid currentState) {
		this.currentState = currentState;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Node [currentState=" + currentState + ", parent=" + parent + ", move=" + move + ", cost=" + cost + "]";
	}
	
	
	
}
