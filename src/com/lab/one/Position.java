package com.lab.one;

public class Position {
	public int i;
	public int j;
	
	public Position(int i, int j) {
		super();
		this.i = i;
		this.j = j;
	}
	public Position() {
		this.i = 0;
		this.j = 0;
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
		return "Pos [i=" + i + ", j=" + j + "]";
	}
	
}
