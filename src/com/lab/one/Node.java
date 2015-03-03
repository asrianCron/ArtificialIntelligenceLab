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
//		return "Node\n##currentState=\n" + currentState + "\n##parent=\n" + parent + "\n##move=\n" + move + "\n##cost=\n" + cost + "\n]";
		return "Node\n##currentState=\n" + currentState  + move + "\n##cost=" + cost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + cost;
		result = prime * result + ((currentState == null) ? 0 : currentState.hashCode());
//		result = prime * result + ((move == null) ? 0 : move.hashCode());
//		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
//		if (cost != other.cost)
//			return false;
		if (currentState == null) {
			if (other.currentState != null)
				return false;
		} else if (!currentState.equals(other.currentState))
			return false;
//		if (move == null) {
//			if (other.move != null)
//				return false;
//		} else if (!move.equals(other.move))
//			return false;
//		if (parent == null) {
//			if (other.parent != null)
//				return false;
//		} else if (!parent.equals(other.parent))
//			return false;
		return true;
	}
	
	
	
}
