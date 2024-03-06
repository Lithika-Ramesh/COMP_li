import java.util.LinkedList;
import java.util.Scanner;


public class Main {
	
	static Scanner input = new Scanner(System.in);
	
	public static int numofprocessors() {
		System.out.println("Insert the number of processors you want. Please choose a number between 0 - 1000");
		int num = input.nextInt();
		if(num < 0 || num > 1000) {
			System.out.println("try again");
			numofprocessors();
		}
		return num;
	}
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to my COMP212 assignment"); 
		System.out.println("Please choose which algorithm you want: A for LCR algorithm and B for HS algorithm"); 
		String in = input.nextLine();
		System.out.println("A: Random || B: Ascending || C: Descending");
		String order = input.nextLine();
		if(order.equals("A") || order.equals("B") || order.equals("C")) {
			if(in.equals("A") || in.equals("a")) {
				int num = numofprocessors();
				LCRAlgorithm algorithm = new LCRAlgorithm(); 
				algorithm.LCRSimulator(num, order); 
			} else if(in.equals("B") || in.equals("b")) { 
				int num = numofprocessors();
				HSSimulator algorithm = new HSSimulator(); 
				algorithm.HRAlgorithm(num, order); 
			}		
		} else {
			System.out.println("Please input an acceptable value (A, B or C");
		}			
		
	}
}
