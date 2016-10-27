package bonus_system;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

	public static Scanner userInput = new Scanner(System.in);
	
	//variables declared to keep the results of some report methods
	static double[] salesPerProduct;
	static double[] salesPerCategory;
	static double[] salesPerStore;
	
	public static void main(String[] args) {
	
		//** Step 1 - reading data
		Product.readProducts();
		StoreManager.readLogins();
		customerMenu.readCustomers();
		customerMenu.readMembers();
		Store.readStores();
		Cashier.readCashiers();
		Transaction.readTransactions();
		Transaction.readTransactionAttributes();
		
		//Step 2 - authentication
		authenticateUser();
		
		//Step 3 - show report-options list
		printMenuOptions();
		
		int choice = 0; // initializes the user's choice in the menu
        while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 can try again
        	System.out.println("Please enter your choice:");
        	choice = userInput.nextInt(); // asks for user input
        	switch (choice){
        	case 1: GetTotalSalesPerStore();
        			break;
        	case 2: GetTotalSalesPerCategory();
        			break;
        	case 3: GetTotalSalesPerProduct();
        			break;
        	case 4: GetFrequentCustomersForDiscount();
        			break;
        	case 5: GetRareCustomersForDiscount();
        			break;
        	case 6: CreateOfferMail();
        			break;
        	default: printInvalidInput();
        	}
        }

	}	
///************************************************
	public static void CreateOfferMail(){
		
		Date[] promotion_period = getPromotionDate();
		userInput.nextLine();
		//here the discount can be given for a different product than the one taken into the report later on
		System.out.println("Please, enter the name of the product being subject to the discount");
		String target_product = userInput.nextLine();
		Product discount_item = Product.GetProduct(target_product);
		System.out.println("Please, enter the percent of the discount (input format: integer, e.g. 20)");
		double discount = userInput.nextDouble();
		
		//declaring ArrayList for customers to be targeted by the campaign
		ArrayList <Customer> targeted_clients = new ArrayList<Customer>();
		int choice=0;
		while (choice !=1 && choice != 2 && choice != 3){
			System.out.println("Please, choose the type of customers for the offer.");
			System.out.println("Enter '1' for the Frequent Customers,"); 
			System.out.println("'2' for the Rare Customers, and '3' for sending");
			System.out.println("the offer to all customers");
			choice=userInput.nextInt();
			if (choice==1)targeted_clients = GetFrequentCustomersForDiscount();
			else if (choice==2 )targeted_clients = GetRareCustomersForDiscount();
			else if (choice==3) targeted_clients = customerMenu.customerArray;
			else printInvalidInput();}

		//declaring ArrayList for customized email messages
		ArrayList <String> promotionalMails = new ArrayList <String>();
		for (int i=0; i<targeted_clients.size(); i++){
			String offer_mail = "Dear " + targeted_clients.get(i).name +",\n" + "We want to thank you for choosing Albert Heijn for all your shopping needs. We know you have many shopping choices, so we are pleased that you continue to shop at Albert Heijn. We are committed to providing you with the best products at the lowest prices. As a token of our appreciation for your loyalty, we grant you a " + discount + "% discount for " + discount_item.productname + " from " + promotion_period[0] + " till " + promotion_period[1] + ". We look forward to serving you in our shop." + "\n" + "Albert Heijn Team";
			promotionalMails.add(i, offer_mail);
			System.out.println(offer_mail);
			System.out.println("");
		}
		//passing emails and clients to the artificial method
		SendEmails(promotionalMails, targeted_clients);
		printMenuOptions();
	}
//**************************************************
	public static void SendEmails(ArrayList <String> offer_mails, ArrayList <Customer> customers){
		//this method pretends sending the emails
		//ArrayList to fetch clients' email addresses
		ArrayList <String> email_addresses = new ArrayList <String>();
		for (Customer person:customers){
			email_addresses.add(person.email);
		}
		//ArrayList <String> promotional_messages = new ArrayList<String>();
		//promotional_messages = offer_mails;
		
		System.out.println("The promotional emails were sent successfully");
	}
	
///*************************************************
	public static ArrayList <Customer> GetRareCustomersForDiscount(){
		//this method gives us customers who rarely bought certain product
		Date[] date_range = getDate();
		//ArrayList to fetch all the transactions in the given date range
		ArrayList <Transaction> transactionSet = GetTransactionSet(date_range);
		//Declaring ArrayList to collect all the customers of the transactions above - collecting customers who were buying anything in the given time range
		ArrayList <Customer> customersOfTransactions = new ArrayList <Customer>();
		//loop to withdraw those clients and attach them to the ArrayList above
		for (int i=0; i < transactionSet.size(); i++){
			customersOfTransactions.add(transactionSet.get(i).customerID);		
		}
		//ArrayList to get unique customers from the customers fetched above
		ArrayList <Customer> unique_customers = new ArrayList <Customer>();
		for(Customer client: customersOfTransactions) {
			if(!unique_customers.contains(client)) {
				unique_customers.add(client);
			}
		}
		userInput.nextLine();
		System.out.println("Please, enter the product name for the analysis");
		String target_product = userInput.nextLine();
		//Fetching the product object based on the given String name of the product
		Product discount_item = Product.GetProduct(target_product);
			
		//Declaring ArrayList fetching buying frequency of a given product for each unique customer
		ArrayList<Integer> buying_frequency = new ArrayList <Integer>();
		//loop to compute the frequency for each customer
		for (int i=0; i < unique_customers.size(); i++){
			int total_quant = 0;//for every new customer new total is declared
			//for each customer all his transactions are fetched in the ArrayList transactions_of_theCLeint
			ArrayList <Transaction> transactions_of_theClient = new ArrayList <Transaction>();
			for (int k=0; k < transactionSet.size(); k++){
				if (transactionSet.get(k).customerID==unique_customers.get(i)){
					transactions_of_theClient.add(transactionSet.get(k));
				}
			}
			//ArrayLists to fetch all products and their quantities of all transactions in the given period of each customer
			//For each customer new arrayLists are declared (regarding products & quantities)
			ArrayList <Product> productsSet = GetCollectionOfProducts(transactions_of_theClient);
			ArrayList <Integer> quantities_array = GetCollectionOfQuantities(transactions_of_theClient);
			for (int l=0; l < productsSet.size(); l++){
				if (productsSet.get(l).productID==discount_item.productID){
					//if the searched product is in the product ArrayList
					//we add its quantity to the total sum 
					total_quant += quantities_array.get(l);
				}
				//if the given customer hasn't bought the searched product at all
				//he receives 0 at his index of the buying_frequency ArrayList
				else {buying_frequency.add(i,0) ;}
					
			}
				buying_frequency.add(i,total_quant);
		}
		
		System.out.println("Please, enter the buying frequency threshold you are interested in.");
		int threshold = userInput.nextInt();
		//ArrayList to fetch the client who bought the given product below certain threshold
		ArrayList <Customer> targeted_customers = new ArrayList <Customer>();
		for (int m=0; m < unique_customers.size(); m++){
			if (buying_frequency.get(m) < threshold){
				targeted_customers.add(unique_customers.get(m));
			}
		}
		System.out.println("The following customers were selected:");
		for (int n=0; n<targeted_customers.size(); n++){
			System.out.println(targeted_customers.get(n).name);
		}

		return targeted_customers;
	}	
	
///*************************************************
	public static ArrayList<Customer> GetFrequentCustomersForDiscount(){
		//the method is the same as GetRareCustomers, it only takes the customers
		//who bought certain product Beyond certain threshold
		Date[] date_range = getDate();
		ArrayList <Transaction> transactionSet = GetTransactionSet(date_range);
		ArrayList <Customer> customersOfTransactions = new ArrayList <Customer>();
		for (int i=0; i < transactionSet.size(); i++){
			customersOfTransactions.add(transactionSet.get(i).customerID);
			
		}
		ArrayList <Customer> unique_customers = new ArrayList <Customer>();
		for(Customer client: customersOfTransactions) {
			  if(!unique_customers.contains(client)) {
			    unique_customers.add(client);
			  }
		}
		userInput.nextLine();
		System.out.println("Please, enter the product name for the analysis");
		String target_product = userInput.nextLine();
		Product discount_item = Product.GetProduct(target_product);
		
	
		ArrayList<Integer> buying_frequency = new ArrayList <Integer>();
		for (int i=0; i < unique_customers.size(); i++){
			int total_quant = 0;
			ArrayList <Transaction> transactions_of_theClient = new ArrayList <Transaction>();
			for (int k=0; k < transactionSet.size(); k++){
				if (transactionSet.get(k).customerID==unique_customers.get(i)){
					transactions_of_theClient.add(transactionSet.get(k));
				}
			}			
			ArrayList <Product> productsSet = GetCollectionOfProducts(transactions_of_theClient);
			ArrayList <Integer> quantities_array = GetCollectionOfQuantities(transactions_of_theClient);
			for (int l=0; l < productsSet.size(); l++){
				if (productsSet.get(l).productID==discount_item.productID){
					total_quant += quantities_array.get(l);
				}
				else {buying_frequency.add(i,0) ;}
				
			}
			buying_frequency.add(i,total_quant);
		}
		
		System.out.println("Please, enter the buying frequency threshold you are interested in.");
		int threshold = userInput.nextInt();
		ArrayList <Customer> targeted_customers = new ArrayList <Customer>();
		for (int m=0; m < unique_customers.size(); m++){
			if (buying_frequency.get(m) > threshold){
				targeted_customers.add(unique_customers.get(m));
			}
		}
		System.out.println("The following customers were selected:");
		for (int n=0; n<targeted_customers.size(); n++){
			System.out.println(targeted_customers.get(n).name);
		}

		return targeted_customers;
	}
	
//*************************************************************
	public static void GetTotalSalesPerProduct(){
		Date[] date_range = getDate();
		//ArrayList to fetch the transactions occurred in the given period
		ArrayList <Transaction> transactionSet = GetTransactionSet(date_range);
		//fetching all products and their quantities of those transactions
		ArrayList <Product> productsSet = GetCollectionOfProducts(transactionSet);
		ArrayList <Integer> quantities_array = GetCollectionOfQuantities(transactionSet);

		//declaring list to fetch total sales of each product
		double[] totals = new double [Product.productArray.size()];
		//for each product in the product catalog
		for (int l=0; l < Product.productArray.size(); l++){
			
			//looping over the products of the transactions
			for (int m=0; m < productsSet.size(); m++){
				//if product from Catalog is found in the transaction products' set
				if(productsSet.get(m).productname.equals(Product.productArray.get(l).productname)){
					
					//for given product l from Catalog computing total Sum  
					totals[l] += productsSet.get(m).price*quantities_array.get(m);
				}
			}
		}
		
		for (int i=0; i<totals.length; i++){
			System.out.println("**********************************");
			System.out.println("The total sales of the product : "+ Product.productArray.get(i).productname + " is " + (Math.round(totals[i]*100.0)/100.0)+" €") ;}
		salesPerProduct = totals;
		printMenuOptions();
	}	
//***********************************************************
	public static void GetTotalSalesPerCategory(){
		Date[] date_range = getDate();
		//fetching transactions of the given period
		ArrayList <Transaction> transactionSet = GetTransactionSet(date_range);
		//fetching products&their quantities of those transactions
		ArrayList <Product> productsSet = GetCollectionOfProducts(transactionSet);
		ArrayList <Integer> quantities_array = GetCollectionOfQuantities(transactionSet);
		
		//String List for categories' names
		String[] categories = Product.GetCategories();
		//List for total sums of the categories
		double[] totals = new double [categories.length];
		//loop for computing total sum for each category
		for (int l=0; l < categories.length; l++){
			ArrayList <Product> category_items = new ArrayList <Product>();
			ArrayList <Integer> items_quantities = new ArrayList <Integer>();
			//looping over all products to get those belonging to a given category
			for (int m=0; m < productsSet.size(); m++){
				if(productsSet.get(m).category.equals(categories[l])){
					category_items.add(productsSet.get(m));
					items_quantities.add(quantities_array.get(m));
					totals[l] += productsSet.get(m).price*quantities_array.get(m);
				}
			}
		}
		
		for (int i=0; i<totals.length; i++){
			System.out.println("**********************************");
			System.out.println("The total sales of the category : "+ categories[i] + " is " + (Math.round(totals[i]*100.0)/100.0)+" €") ;}
		salesPerCategory=totals;
		printMenuOptions();
	}	

//***************************************************************************
	public static void GetTotalSalesPerStore(){
		Date[] date_range = getDate();
		ArrayList <Transaction> transactionSet = GetTransactionSet(date_range);
		//list to fetch total sales of each store
		double[] totals = new double [Store.storeCounter];
		for (int a=0; a<Store.storeCounter; a++){
			for (int i=0; i<transactionSet.size(); i++){
				//recognizing the location of the transaction by Cashier ID 
				if (transactionSet.get(i).cashierID==Store.storeArray.get(a).cashierID){
							totals[a]+= transactionSet.get(i).total;	
				}
			}
		}
		for (int i=0; i<totals.length; i++){
			System.out.println("**********************************");
		System.out.println("The total sales for the store no: "+ (i+1) + ", name: " + Store.storeArray.get(i).storename + " is " + (Math.round(totals[i]*100.0)/100.0)+" €") ;}
		salesPerStore = totals;
		printMenuOptions();
	}
//********************************************************************
	public static ArrayList <Transaction> GetTransactionSet(Date[] dates){
		//method to collect transaction set over the given period of time
		Date[] date_range = dates;
		ArrayList <Transaction> transactions_lot = new ArrayList <Transaction>();
		for (int i=0; i<Transaction.transactionArray.size(); i++){
			if (Transaction.transactionArray.get(i).transactionDate.after(date_range[0]) && Transaction.transactionArray.get(i).transactionDate.before(date_range[1])){
						transactions_lot.add(Transaction.transactionArray.get(i));	
			}
		}
		return transactions_lot; 
	}
//***************************************************************************
	public static ArrayList <Product> GetCollectionOfProducts(ArrayList <Transaction> transactionsCollection){
		//method to fetch in ArrayList all product of the transaction Set
		ArrayList <Transaction> transactionSet = transactionsCollection;
		ArrayList <Product> productsSet = new ArrayList<Product>();
		//ArrayList <Integer> quantities_array = new ArrayList <Integer>();
		for (int i=0; i<transactionSet.size(); i++){
			for (int k=0; k<transactionSet.get(i).productID.size(); k++){
				productsSet.add(transactionSet.get(i).productID.get(k));
			}
		}
		return productsSet;
	}
//***************************************************************************
		public static ArrayList <Integer> GetCollectionOfQuantities(ArrayList <Transaction> transactionsCollection){
			//method to fetch in ArrayList all products' quantities of the transaction Set
			ArrayList <Transaction> transactionSet = transactionsCollection;
			ArrayList <Integer> quantitiesSet = new ArrayList<Integer>();
			//ArrayList <Integer> quantities_array = new ArrayList <Integer>();
			for (int i=0; i<transactionSet.size(); i++){
				for (int k=0; k<transactionSet.get(i).productID.size(); k++){
					quantitiesSet.add(transactionSet.get(i).productQuantity.get(k));
				}
			}
			return quantitiesSet;
		}

	
	public static void authenticateUser(){
		/* username: Manager
		 * password: Java2016
		 */

	boolean approved = false;
	
		do 
		{
			try{
				String username = getUser();
				String pass = getPassword();
				StoreManager employee = StoreManager.GetEmployee(username);
				
				if (employee.password.equals(pass))
				{
					System.out.println("Successful authentication");
					approved = true;
					
				}
				else 
				{
					System.out.println("Incorrect Password. You cannot proceed further.");
				}
			}
			catch (InputMismatchException e) {
				System.out.println("Invalid input type");
			}	
		}while (!approved);
		
	}
	
	public static String getUser(){
		String username;
		do {
			
			System.out.println("Please, enter your username");
			username = userInput.nextLine();
			
			if(!username.matches("[a-zA-Z_0-9]{3,10}"))
			{
				System.out.println("Invalid input. Your username does not match the required format.");
			}
		}while (!username.matches("[a-zA-Z_0-9]{3,10}"));
		
		return username;
	}
	
	public static String getPassword(){
		
		String codeword;
		
		do {
			System.out.println("Please, enter your password");
			codeword = userInput.nextLine();
			
			if (!codeword.matches("[a-zA-Z_0-9]{3,10}")){
				
				System.out.println("The input does not match the required format");
			}	
		}while (!codeword.matches("[a-zA-Z_0-9]{3,10}"));
		
		return codeword;
	}

///**********************************************************************
	public static Date[] getPromotionDate(){
		Date[] dateRange = new Date[2];
		String[] inputDates = new String[2];
		System.out.println("Please, set the start date for the promotion. The required format: dd/MM/yyyy");
		inputDates[0] = userInput.next();
		userInput.nextLine();
		System.out.println("Please, set the end date for the promotion. The required format: dd/MM/yyyy");
		inputDates[1] = userInput.next();
		//converting the input to the type Date
		dateRange[0]= convertToDateDecrease(inputDates[0]);
		dateRange[1]= convertToDateIncrease(inputDates[1]);
		
		return dateRange;
	}
//***************************************************************************
	public static Date[] getDate(){
		Date[] dateRange = new Date[2];
		String[] inputDates = new String[2];
		System.out.println("Please, set the start date for the report. The required format: dd/MM/yyyy");
		inputDates[0] = userInput.next();
		userInput.nextLine();
		System.out.println("Please, set the end date for the report. The required format: dd/MM/yyyy");
		inputDates[1] = userInput.next();
	
		dateRange[0]= convertToDateDecrease(inputDates[0]);
		dateRange[1]= convertToDateIncrease(inputDates[1]);
		
		return dateRange;
	}
	
//************************************************************
	public static Date convertToDateIncrease(String date){
		//the method adds 1 day to the given date to have the report until certain day incl. that day
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = new Date();
	    try {
	    		convertedDate = (Date)formatter.parse(date); 
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(convertedDate);
	    		cal.add(Calendar.DATE, 1); // add 1 day
	    		 
	    		convertedDate = cal.getTime();
	    }
	    catch (ParseException e) {
	        e.printStackTrace();
	    }
		return convertedDate;
	}
//************************************************************
	public static Date convertToDateDecrease(String date){
		//the method deducts 1 day to have the report computed since the given date incl. that day
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date convertedDate = new Date();
	    try {
	    		convertedDate = (Date)formatter.parse(date); 
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(convertedDate);
	    		cal.add(Calendar.DATE, -1); // substract 1 day
	    		 
	    		convertedDate = cal.getTime();
	    }
	    catch (ParseException e) {
	        e.printStackTrace();
	    }
		return convertedDate;
	}
//**************************************************	
	public static void printInvalidInput () {
		System.out.println("___________________________________________________");
        System.out.println("|                Invalid input!                   |");
        System.out.println("___________________________________________________");
	}
	public static void printMenuOptions () {
		System.out.println("__________________________________________________________________");
        System.out.println("|      Welcome to the Store Manager interface     		   |");
        System.out.println("|                                                 		   |");
        System.out.println("|  Press 1 to obtain total sales per store        		   |");
        System.out.println("|  Press 2 for total sales per category           		   |");
        System.out.println("|  Press 3 for total sales per product            		   |");
        System.out.println("|  Press 4 to obtain frequent customers of a given product 	   |");
        System.out.println("|  Press 5 to obtain infrequent clients of a given product 	   |");
        System.out.println("|  Press 6 to create Email Campaign                        	   |");
        System.out.println("___________________________________________________________________");
	}
}
	

