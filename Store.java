package bonus_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {
	static Scanner uInput = new Scanner(System.in);
	static int storeCounter = 0;  
	static ArrayList <Store> storeArray = new ArrayList <Store>(); 
	
	int storeID;
	int postalcode;	
	String storename;
	int cashierID;
	
	public static void main(String[] args){
	}
	
	Store (int storeID, int postalcode, String storename, int cashierID ){  //set store constructors
		this.storeID = storeID;
		this.postalcode = postalcode;
		this.storename = storename;
		this.cashierID = cashierID;
		storeCounter++;
		}

	public static void saveChangesToFile() {
	// saves changes made to the storeArray and prints all objects of the storeArray into file
	try {	
		PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("storeDatabase.txt", false)));	
		for(int i = 0; i < storeCounter; i++) { //stores each object in store array
			pr.print(storeArray.get(i).storeID);
			pr.print(",");	
			pr.print(storeArray.get(i).postalcode);
			pr.print(",");
			pr.print(storeArray.get(i).storename);
			pr.print(",");
			pr.print(storeArray.get(i).cashierID);
			pr.println("");
		} pr.close();	
	}
		
	catch(IOException ex) {
		System.out.println("File not found");
	}
	}


public static void readStores() {
    // Loads all the customers from the txt file and creates objects based on each line of the file	
	try {
		int counter = 0; // this variable is used to index each row in the txt file (first row is index zero)
		String line;
			
		BufferedReader br = new BufferedReader(new FileReader("storeDatabase.txt"));
		
		while((line = br.readLine())!= null) {	
			String [] shops = line.split(",");
			int storeId = Integer.parseInt(shops[0]);
			int zipcode = Integer.parseInt(shops[1]);
			String storename = shops[2];
			int cashierID = Integer.parseInt(shops[3]);		
			storeArray.add(counter, new Store(storeId,zipcode,storename,cashierID));
			counter ++;
		}	
		 br.close();
	}
		
	catch (IOException e) {
		System.out.println("File does not exist");
	}
} 	

	public static int getLastStoreId() {
		String lastStoreId = "0";
		try {
			String line;
			BufferedReader br = new	BufferedReader(new FileReader("storeDatabase.txt")); 
				while ((line = br.readLine())!= null) {
					String[] wordsInFile = line.split(",");
					lastStoreId = wordsInFile[0];
				}
			br.close();
		}
		catch (IOException ex) {
			System.out.println("Error");
		}
		int returningId = Integer.parseInt(lastStoreId);
		return returningId;
	}

	public static void createNewStore () {
		readStores();
		System.out.println("Please enter store name:");
		String storename = uInput.next(); 
		System.out.println("Please enter zipcode:");
		int zipcode = uInput.nextInt();
		System.out.println("Please enter cashier ID");
		int cashierID = uInput.nextInt();
		int storeId = storeCounter + 1;
		storeArray.add(storeId - 1, new Store(storeId, zipcode, storename, cashierID)); 
		saveChangesToFile (); 
	}
	
	public static int getStoreID(int cashier_id){
		//the method provides store ID in which the given cashier works
		readStores();
		storeCounter=getLastStoreId();
		int storeNumber=0;
		for (int i=0; i<storeArray.size(); i++){
			if (storeArray.get(i).cashierID==cashier_id){
				 storeNumber = storeArray.get(i).storeID;
			}
		}
		return storeNumber;
	}
}
