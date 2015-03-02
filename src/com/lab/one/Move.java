package com.lab.one;

public class Move {
	private Position from;
	private Position to;
	private int cost;
	
	public Move(Position from, Position to, int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
	
	public Position getFrom() {
		return from;
	}
	public void setFrom(Position from) {
		this.from = from;
	}
	public Position getTo() {
		return to;
	}
	public void setTo(Position to) {
		this.to = to;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "##Move [from=" + from + ", to=" + to + ", cost=" + cost + "]##";
	}
	
	
}
