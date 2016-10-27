package bonus_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;


public class Transaction {

	static Scanner uInput = new Scanner(System.in);
	static int transactionCounter = 0; 
	static ArrayList <Transaction> transactionArray = new ArrayList <Transaction>(); 
	int transactionID; 
	Customer customerID;
	int cashierID;
	Date transactionDate;
	ArrayList<Product> productID = new ArrayList <Product> (); //Attribute in the form of ArrayList(dynamically sizable) to keep all products
	ArrayList<Integer> productQuantity = new ArrayList<Integer> ();//as in the previous attribute
	double total;

	public static void main (String[] args){	

	}
	
	public Transaction (int transactionID, int clientID, int cashierID, Date transactionDate, double total_sum){
		this.transactionID = transactionID;
		this.cashierID= cashierID;
		this.customerID = Customer.GetCustomer(clientID);
		this.transactionDate = transactionDate;
		this.total = total_sum;
		transactionCounter++;
	}
	
	
	public Transaction (int transactionID, int clientID, int cashierID, Date transactionDate, ArrayList<Product> productID, ArrayList<Integer> productQuantity){
	///This constructor serves for CreateNewTransaction()
		this.transactionID = transactionID;
		this.cashierID= cashierID;
		this.customerID = Customer.GetCustomer(clientID);
		this.transactionDate = transactionDate;
		this.productID = productID;
		this.productQuantity= productQuantity;
		this.total = Product.GetTotalSum(productID, productQuantity);
		transactionCounter++;
	}


	public static void saveChangesToFile() {
    // saves changes made to the transactionArray and prints all objects of transactionArray into file
		try {	
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("transactionDatabase.txt", false)));	
			for(int i = 0; i < transactionCounter; i++) { //stores each object in customer array
				pr.print(transactionArray.get(i).transactionID);
				pr.print(",");	
				pr.print(transactionArray.get(i).customerID.customerId);
				pr.print(",");
				pr.print(transactionArray.get(i).cashierID);
				pr.print(",");
				pr.print(transactionArray.get(i).transactionDate);
				pr.print(",");
				pr.print(transactionArray.get(i).total);
				pr.println("");
			} pr.close();	
		}
		
		catch(IOException ex) {
		System.out.println("File not found");
		}
	}
	
	public static void saveAttributesToFile() {
    // saves changes made to the transactions_attributeArray and prints all items of that Array into file
		try {	
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("transactionAttributesDatabase.txt", false)));	
			for(int i = 0; i < transactionCounter; i++) { //stores each object in customer array
				for (int j = 0; j<transactionArray.get(i).productID.size(); j++){
					pr.print(transactionArray.get(i).transactionID);
					pr.print(",");
					pr.print(transactionArray.get(i).productID.get(j).productID);
					pr.print(",");	
					pr.print(transactionArray.get(i).productQuantity.get(j));
					pr.println("");
				}
			}pr.close();	
		}
		
		catch(IOException ex) {
		System.out.println("File not found");
		}
	}
	
	public static void readTransactions() {
	    // Loads all the customers from the txt file and creates objects based on each line of the file	
		try {
			int counter = 0; // this variable is used to index each row in the txt file (first row is index zero)
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("transactionDatabase.txt"));
			DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			while((line = br.readLine())!= null) {	
				String [] events = line.split(",");
				int transactionId = Integer.parseInt(events[0]);
				int customer_ID = Integer.parseInt(events[1]);
				int cashierID = Integer.parseInt(events[2]);
				Date transactionDate = (Date)formatter.parse(events[3]);
				double totalSum = Double.parseDouble(events[4]);
				transactionArray.add(counter, new Transaction(transactionId, customer_ID, cashierID, transactionDate, totalSum));
				counter ++;
			}	
			 br.close();
		}
		catch (ParseException e) {
	        e.printStackTrace();
	    }
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	

	public static void readTransactionAttributes() {
	    // Loads all the customers from the txt file and creates attributes-ArrayLists based on each line of the file	
		try {
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("transactionAttributesDatabase.txt"));
			while((line = br.readLine())!= null) {	
				String [] events = line.split(",");
				int transactionId = Integer.parseInt(events[0]);
				int productID = Integer.parseInt(events[1]);
				Integer productQuantity = Integer.parseInt(events[2]);
				for (int i=0; i<transactionArray.size(); i++){
					if (transactionArray.get(i).transactionID==transactionId){
						transactionArray.get(i).productID.add(Product.GetProduct(productID));
						transactionArray.get(i).productQuantity.add(productQuantity);
					}
				}
			}	
			 br.close();
		}
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	

	
	public static int getLastTransactionId() {
		String lastTransactionId = "0";
		try {
			String line;
			BufferedReader br = new	BufferedReader(new FileReader("transactionDatabase.txt")); 
				while ((line = br.readLine())!= null) {
					String[] wordsInFile = line.split(",");
					lastTransactionId = wordsInFile[0];
				}
			br.close();
		}
		catch (IOException ex) {
			System.out.println("Error");
		}
		int returningId = Integer.parseInt(lastTransactionId);
		return returningId;
	}

	public static void createNewTransaction() {
		try {
			readTransactions();
			readTransactionAttributes();
			Product.readProducts();
			System.out.println("Please enter Customer ID:");
			int client_ID = uInput.nextInt(); 
			System.out.println("Please enter cashierID:");
			int cashierID = uInput.nextInt();
			System.out.println("Please enter date of the transaction");
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date)formatter.parse(uInput.next());
			int transactionId = transactionCounter + 1;
			System.out.println("Please enter product IDs:");
			System.out.println("Please, type '0' to signal the end of the list");
			ArrayList <Product> productsID = new ArrayList <Product> ();
			final int SENTINEL = 0;
			while (true){
				int k = uInput.nextInt();
				if (k==SENTINEL) break;
				productsID.add(Product.GetProduct(k));
			}
			System.out.println("Please enter products' Quantities:");
			ArrayList <Integer> productsQuantity = new ArrayList <Integer> ();
			for (int i=0; i < productsID.size(); i++){
				int k = uInput.nextInt();
				productsQuantity.add(k);
			}
			
			transactionArray.add(transactionId - 1, new Transaction(transactionId, client_ID, cashierID, date, productsID, productsQuantity));
			saveChangesToFile ();
			saveAttributesToFile();
		}
		catch (ParseException e) {
	        e.printStackTrace();
	    }
	}
	
}		
