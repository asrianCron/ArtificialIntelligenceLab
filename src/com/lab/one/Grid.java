package com.lab.one;

import java.util.Arrays;

public class Grid {
	private int[][] numbers;
	private int size;
	private int blank = -1;
	private Position blankPos;
	public Grid(){
		this.numbers = new int[0][0];
	}
	public Grid(int[][] arg){
		this.numbers = arg;
		this.size = arg.length;
	}
	
	public Grid(Grid arg){
		this.size = arg.size();
		this.blank = arg.getBlank();
		this.blankPos = arg.getBlankPosition();
		this.numbers = new int[arg.size()][arg.size()];
		for(int i=0;i<arg.size();i++){
			System.arraycopy(arg.getGrid()[i], 0, this.numbers[i], 0, arg.size());
		}
	}
	
	public int size(){
		return size;
	}
	public int[][] getGrid(){
		return numbers;
	}
	
	public int getBlank(){
		return blank;
	}
	
	public Position getBlankPosition(){
		if(this.blankPos == null){
			for(int i=0;i<numbers.length;i++){
				for(int j=0;j<numbers[i].length;j++){
					if(numbers[i][j] == blank){
						this.blankPos = new Position(i, j);
					}
				}
			}
			return this.blankPos;
		}
		return blankPos;
	}
	
	public int[] getGridAsRow(){
		int[] row = new int[size * size];
		for(int i=0;i<size;i++){
			System.arraycopy(numbers[i], 0, row, (i * size), size);
		}
		return row;
	}
	
	@Override
	public String toString() {
		StringBuffer bfr = new StringBuffer();
		for(int i=0;i<numbers.length;i++){
			for(int j=0;j<numbers[i].length;j++){
				bfr.append(numbers[i][j] + " ");
			}
			bfr.append("\n");
		}
		return bfr.toString();
	}
	@Override
	public int hashCode() {
		int result = 0;
		int count = 1;
		for(int i=0;i<this.numbers.length;i++){
			for(int j=0;j<this.numbers[i].length;j++){
				result += this.numbers[i][j] * count;
				count = count * 10;
			}
		}
        
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
		Grid other = (Grid) obj;
		if (!Arrays.deepEquals(numbers, other.numbers))
			return false;
		return true;
	}
	
	
	
}
