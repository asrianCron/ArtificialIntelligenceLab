package com.lab.one;

public class Position {
	private int i;
	private int j;
	
	public Position(int i, int j) {
		this.i = i;
		this.j = j;
	}
	public Position() {
		this.i = 0;
		this.j = 0;
	}
	public Position(Position other){
		this.i = other.getI();
		this.j = other.getJ();
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public int getJ() {
		return j;
	}
	public void setJ(int j) {
		this.j = j;
	}
	@Override
	public String toString() {
		return "Pos [i=" + (i + 1) + ", j=" + (j + 1) + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
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
		Position other = (Position) obj;
		if (i != other.i)
			return false;
		if (j != other.j)
			return false;
		return true;
	}
	
}
