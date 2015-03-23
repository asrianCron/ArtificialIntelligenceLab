package com.lab.main;

public class Main {

	public static void main(String[] args) {
		int labToRun = 2;
		
		switch(labToRun){
		case 1:
			com.lab.one.Main.fakeMain(args);
			break;
		case 2:
			com.lab.two.Main.fakeMain(args);
			break;
		default:
			com.lab.one.Main.fakeMain(args);
			break;
		}
		
	}

}
