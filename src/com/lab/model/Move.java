package com.lab.model;

public class Move {
	private Position from;
	private Position to;
	private int cost;
	
	public Move(Position from, Position to, int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}
	
	public Move() {
		this.from = new Position();
		this.to = new Position();
		this.cost = 0;
	}
	
	public Move(Move other){
		this.from = new Position(other.getFrom());
		this.to = new Position(other.getTo());
		this.cost = other.getCost();
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
		return "##Move [from=" + to + ", to=" + from + ", cost=" + cost + "]##";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cost;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Move other = (Move) obj;
		if (cost != other.cost)
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
	
	
}
