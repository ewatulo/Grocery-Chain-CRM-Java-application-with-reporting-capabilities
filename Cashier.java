package bonus_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Cashier {
		
	static Scanner uInput = new Scanner(System.in);
	static int cashierCounter = 0; 
	static ArrayList <Cashier> cashierArray = new ArrayList<Cashier>(); 
	int cashierID;	//set cashier attributes
	int storeID;	

	public static void main (String[] args){
		
		Product.readProducts();
		printMenu();
		int choice = 0; // initializes the user's choice in the menu
		while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 can try again
			System.out.println("Please enter your choice:");
	        choice = uInput.nextInt(); // asks for user input
	        switch (choice){
	        	case 1: Transaction.createNewTransaction();
	        			break;
	        	case 2: System.exit(0);
	        			break;
	        	default: printInvalidInput();
	        }
		}
	}

	public Cashier (int cashier_ID){
			this.cashierID = cashier_ID;
			this.storeID = Store.getStoreID(this.cashierID);
			cashierCounter++;
	}

	public static void saveChangesToFile() {
	// saves changes made to the cashierArray and prints all objects of the cashierArray into file
		try {	
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("cashierDatabase.txt", false)));	
			for(int i = 0; i < cashierCounter; i++) { //stores each object in customer array
				pr.print(cashierArray.get(i).cashierID);
				pr.print(",");	
				pr.print(cashierArray.get(i).storeID);
				pr.println("");
			} 
			pr.close();	
		}
		
		catch(IOException ex) {
			System.out.println("File not found");
		}
	}
	
	public static void readCashiers() {
	    // Loads all the cashiers from the txt file and creates objects based on each line of the file
		try {
			int counter = 0; // this variable is used to index each row in the txt file (first row is index zero)
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("cashierDatabase.txt"));
			
			while((line = br.readLine())!= null) {	
				String [] cashiers = line.split(",");
				int cashierId = Integer.parseInt(cashiers[0]);	
				cashierArray.add(counter, new Cashier(cashierId));
				counter ++;
			}	
			 br.close();
		}
			
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	

	public static void createNewCashier () {
		readCashiers();
		System.out.println("Please enter cashier ID:");
		int cashierID = uInput.nextInt(); 
		int cashierId = cashierCounter + 1;
		cashierArray.add(cashierId - 1, new Cashier(cashierID)); 
		saveChangesToFile (); 
	}
	public static void printInvalidInput () {
		System.out.println("___________________________________________________");
	    System.out.println("|                Invalid input!                   |");
	    System.out.println("___________________________________________________");
	}
	public static void printMenu() {
		System.out.println("_________________________________________________________________");
	    System.out.println("|      Welcome to the Technical Administrator interface     	|");
	    System.out.println("|                                                 		|");
	    System.out.println("|  Press 1 to create new transaction				|");
	    System.out.println("|  Press 2 to shut down the system				|");
	    System.out.println("__________________________________________________________________");
	}
}
		
		
	

