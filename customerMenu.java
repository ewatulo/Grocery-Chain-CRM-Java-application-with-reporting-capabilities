package bonus_system;
import java.io.*;
import java.util.*;

public class customerMenu {
	static Scanner userInput = new Scanner(System.in);
	static int customerCounter = 0; // keeps system up to date with number of customers 
	static ArrayList <Customer> customerArray = new ArrayList <Customer>(); // references for the array of objects is created here (only the references and not the actual objects)
	static ArrayList <MemberCustomer> memberArray = new ArrayList <MemberCustomer>(); // TODO initialization of an array of customers who are also members
	//******************************
	public static void main (String[] args) {
		
		readCustomers(); // this method retrieves customers from database and creates an object array for each customer
		readMembers(); //this method extracts every member from the database and stores each member as 

		
		printWelcomeMessage(); // prints welcome screen
		int choice = 0; // initializes the user's choice in the menu
        while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 can try again
        	System.out.println("Please enter your choice:");
        	choice = userInput.nextInt(); // asks for user input
        	switch (choice){
        		case 1: createNewCustomer(); // method to make new account
        				break;
        		case 2: returningCustomerAuthentication(); // method to change existing account to member
        				break;
        		default: printInvalidInput();}
        }
	}   
	
	public static void returningCustomerAuthentication () { 
		//This method asks a user to key in his email address and then checks if this is in the database. 
		//If this email exists, it asks the user to confirm it is him by entering his postal code. 
		//If the postal code matches with what is in the Database, the user is sent to a menu for returning customers
 		System.out.println("___________________________________________________");
        System.out.println("Please enter your email address:");
        String emailTemp = userInput.next(); // temporarily stores the user's email
        String retrievedName = getName(emailTemp); // fetches this user's name and stores as a local variable
        if (retrievedName.equals("")) { // checks if the method managed to retrieve a name 
            System.out.println("We're sorry but this email is not in the database.");
            returningCustomerAuthentication(); // sends the user back to the beginning of this method
        } 
        int retrievedPostalCode = getPostalCode(emailTemp); // fetches user's postal code and stores as local variable
     	System.out.println("___________________________________________________");
        System.out.println("Welcome back " + retrievedName + ",");
        System.out.println("Please enter the first 4 digits of your postal code so we know it's you:");
        boolean correctPostalCode = false;
        while (correctPostalCode == false) { // this loop keeps asking for a postal code until the correct postal code is provided
        	int postalCodeTemp = userInput.nextInt(); // asks user for postal code
        	if (retrievedPostalCode == postalCodeTemp) { // checks whether postal code input is same as the one retrieved from database
            		System.out.println("This corresponds with our database.");
            		System.out.println("What would you like to do " + retrievedName + "?");
            		correctPostalCode = true;
            		int correspondingCustomerId = getCustomerId(emailTemp);
            		returningCustomerMenu (correspondingCustomerId);
        	}
        	else {
        		System.out.println("Incorrect postal code, try again:");
        	}
        }
    }
	
	public static void createNewCustomer () {
		String gender = "male";
		boolean membership = false;
		String temp = "x"; //temporary variable holding data about user's input for membership
		System.out.println("Please enter your name:");
		String name = userInput.next(); 
		System.out.println("Please enter your age:");
		int age = userInput.nextInt();
		System.out.println("Please enter your gender: (1 for female, 0 for male)");
		int temp1 = userInput.nextInt();
		if (temp1 == 1) gender = "female"; // sorts out gender. male was already defined earlier so do not need 'else' statement
		System.out.println("Please enter the first 4 digits of your postal code:");
		int postalCode = userInput.nextInt();
		System.out.println("Please enter your email:");
		String email = userInput.next();
		System.out.println("Would you like to receive customer offers by us? ('y' for yes and 'n' for no)");
		temp = userInput.next();
		if (temp.equals("y")) membership = true;
		int customerId = customerCounter + 1;
		customerArray.add(customerId - 1,new Customer(customerId, name, age, gender, postalCode, email, membership)); // this line creates a new object of the customer
		saveChangesToFile (); // stores the newly created object into the database
		printNewCustomer (); //message confirming a successful creation of a customer account
	}
	
	
	
	public static void returningCustomerMenu (int id) {
		printReturningCustomerMenu (); // menu designed for returning customers
		int choice = 0; // initializes the user's choice in the menu
        while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 or 3 can try again
        	System.out.println("Please enter your choice:");
        	choice = userInput.nextInt(); // asks for user input
        	switch (choice) {
        		case 1: viewAccountDetails(id); // method to make new account
        				break;
        		case 2: updateDetails(id); // method to change existing account to member
        				break;
        		case 3: changeMembership(id);
        				break;
        		case 4: seeMyTransactions(id);
        				break;
        		default: printInvalidInput();
        	}
        }
	}
	
	 public static void updateDetails (int id) {
	 		printUpdateDetailsMenu ();
	 		int choice = 0;
	 		while (true) { // this loop ensures that even a person that doesn't enter an invalid number
 	        	System.out.println("Please enter your choice:");
 	        	choice = userInput.nextInt(); // asks for user input
 	        	switch (choice) {
 	        		case 1: changeName(id); 
 	        				break;
 	        		case 2: changeAge(id);
 	        				break;
 	        		case 3: changeGender(id);
 	        				break;
 	        		case 4: changePostalCode(id);
 	        				break;
 	        		case 5: changeEmail(id);
 	        				break;
 	        		case 0: returningCustomerMenu(id); // returns the user to the previous menu
 	        				break;
 	        		default: printInvalidInput();
 	        	}
	 		}
	 }
	 
	    public static void viewAccountDetails (int customerId) {
	 
			System.out.println("___________________________________________________");
	        System.out.println("|  Name : " + customerArray.get(customerId-1).name); // we have to subtract 1 because userIDs start from 1 while arrays start from index 0
	        System.out.println("|  Age: " + customerArray.get(customerId-1).age);
	        System.out.println("|  Gender: " + customerArray.get(customerId-1).gender);
	        System.out.println("|  PostalCode: " + customerArray.get(customerId-1).postalCode);
	        System.out.println("|  Email: " + customerArray.get(customerId-1).email);
	        System.out.println("|  Membership status: " + customerArray.get(customerId-1).membership);
	        System.out.println("___________________________________________________");
	        System.out.println("|  Press 0 to return to previous menu");
	        System.out.println("|  Press 1 to edit this information");
	        System.out.println("___________________________________________________");
	        //*****************************
	        int choice = -1;
			while (choice != 1 && choice != 0) { //loop ensures user enters a valid input 
				System.out.println("   Please enter your choice: ");
				choice = userInput.nextInt();
				if (choice == 1) updateDetails(customerId); //sends user to the update details page
				else if (choice == 0) returningCustomerMenu(customerId); // returns customer to previous menu
				else printInvalidInput();
			}
		}
	
	public static void changeName (int id) {
		System.out.println("Please enter your new name: ");
		String newName = userInput.next(); //This input doesn't work if there are spaces in the name
		
		customerArray.get(id-1).name = newName; // changes the name within the object array
		saveChangesToFile(); // saves the changes made to customer array in the customer database file
		printChangesSuccessful();
		returningCustomerMenu (id);// returns user to menu
	}
	
	public static void changeAge (int id) {
		System.out.println("Please enter your new age: ");
		int newAge = userInput.nextInt();
		
		customerArray.get(id-1).age = newAge; // changes the age within the object array
		
		saveChangesToFile (); // saves the changes made to customer array in the customer database file
		printChangesSuccessful ();
		returningCustomerMenu (id);// returns user to menu
	}
	
	public static void changeGender (int id) {
		int newGenderInput = -1;
		String newGender = "";
		while (newGenderInput != 1 && newGenderInput != 0) { //loop ensures user enters a valid input 
			System.out.println("Please enter your new gender: (0 = male, 1 = female)");
			newGenderInput = userInput.nextInt();
			if (newGenderInput == 1) newGender = "female";
			else if (newGenderInput == 0) newGender = "male";
			else printInvalidInput();
		}
		customerArray.get(id-1).gender = newGender; // changes the gender within the object array
		
		saveChangesToFile ();// saves the changes made to customer array in the customer database file
		printChangesSuccessful ();
		returningCustomerMenu (id); // returns user to menu
	}
	
	public static void changePostalCode (int id) {
		System.out.println("Please enter the first 4 digits of your postal code: ");
		int newPostalCode = userInput.nextInt();
		customerArray.get(id-1).postalCode = newPostalCode; // changes the postal code within the object array
		
		saveChangesToFile ();// saves the changes made to customer array in the customer database file
		printChangesSuccessful ();
		returningCustomerMenu (id);// returns user to menu
	}
	
	public static void changeEmail (int id) {
		System.out.println("Please enter your new email address: ");
		String newEmail = userInput.next();
		customerArray.get(id-1).email = newEmail; // changes the email within the object array
		
		saveChangesToFile ();// saves the changes made to customer array in the customer database file
		printChangesSuccessful ();
		returningCustomerMenu (id);// returns user to menu
	}
	
	public static void changeMembership (int id) {
        System.out.println("________________________________________________________");
		System.out.println(" Press 'y' to confirm subscription to customized offers");
		System.out.println(" Press 'n' to return to previous menu");
        System.out.println("________________________________________________________");
        String choice = "";
		while (choice.equals("")) {// loop ensures a user input is valid
			System.out.println("Enter your choice: ");
			choice = userInput.next();
			if (choice.equals("y")) {
				customerArray.get(id-1).membership = true; // changes the name within the object in the array
				
				saveChangesToFile ();// saves the changes made to customer array in the customer database file
				printChangesSuccessful ();
				returningCustomerMenu (id);
			}
			else if (choice.equals("n")){
				customerArray.get(id-1).membership = false; // changes the name within the object in the array
				saveChangesToFile ();
				printChangesSuccessful ();
				returningCustomerMenu (id); // returns the user back to the previous page which is the updating details page
			}
			else {
				printInvalidInput();
				choice = ""; // resets the choice back to default so the loop continues
			}
		}
	}
	
	public static void seeMyTransactions (int customerId) {
		Transaction.readTransactions();
		System.out.println("Here is a summary of your past transactions:");
		System.out.println("ID	Date			Total Sum");
			for (Transaction i:Transaction.transactionArray){
					if (i.customerID.customerId== customerId){
						System.out.println("-----------------------------------");
						System.out.println(i.transactionID + " | "+ i.transactionDate+ " | "+ i.total + " |");
						}
				}
		System.out.println("Press 0 to return to previous menu");
			int choice = -1;
				while (choice != 1 && choice != 0) { //loop ensures user enters a valid input 
					System.out.println("   Please enter your choice: ");
						choice = userInput.nextInt();
					if (choice == 0) returningCustomerMenu(customerId); // returns customer to previous menu
							else printInvalidInput();
					}
	}
	
	//the following method searches the customer array for an email address and returns the person's name
	public static String getName(String email) {
		String correspondingName = "";
		int lastCustomerId = customerCounter;
		for (int i = 0; i<lastCustomerId; i++) {
			if (customerArray.get(i).email.equals(email)) {
				correspondingName = customerArray.get(i).name;
			}
		}
		return correspondingName;
	}
	
	public static int getPostalCode(String email) { // this method searches customer array for an email address and returns the customer's postal code
		int correspondingPostalCode = 0;
		int lastCustomerId = customerCounter;
		for (int i = 0; i<lastCustomerId; i++) {
			if (customerArray.get(i).email.equals(email)) {
				correspondingPostalCode = customerArray.get(i).postalCode;
			}
		}
		return correspondingPostalCode;
	}
	// the following method fetches the ID of the customer on the last row of the .txt file
	public static int getLastCustomerId() {
		String lastCustomerId = "0";
		try {
			String line;
			BufferedReader br = new	BufferedReader(new FileReader("customerDatabase.txt")); 
				while ((line = br.readLine())!= null) {
					String[] wordsInFile = line.split(",");
					lastCustomerId = wordsInFile[0];
				}
			br.close();
		}
		catch (IOException ex) {
			System.out.println("Error");
		}
		int returningId = Integer.parseInt(lastCustomerId);
		return returningId;
	}
	public static int getCustomerId(String email) { // this method searches the customer array for an email and returns his customerId
		int correspondingId = 0;
		int lastCustomerId = customerCounter;
		for (int i = 0; i < lastCustomerId; i++) {
			if (customerArray.get(i).email.equals(email)) {
				correspondingId = customerArray.get(i).customerId;
			}
		}
		return correspondingId;
	}
	
	public static void saveChangesToFile() {
	    // saves changes made to the customerArray and prints all items in customerArray to file
		try {	
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("customerDatabase.txt", false)));	
			for(int i = 0; i < customerCounter; i++) { //stores each object in customer array
			
				pr.print(customerArray.get(i).customerId);
				pr.print(",");	
				pr.print(customerArray.get(i).name);
				pr.print(",");
				pr.print(customerArray.get(i).age);
				pr.print(",");
				pr.print(customerArray.get(i).gender);
				pr.print(",");
				pr.print(customerArray.get(i).postalCode);
				pr.print(",");
				pr.print(customerArray.get(i).email);
				pr.print(",");
				pr.print(customerArray.get(i).membership);
				pr.println("");	
			} pr.close();	
		}
			
		catch(IOException ex) {
			System.out.println("File not found");
		}
	}
	public static void readMembers() { // this method is currently not used
	    // Loads all the members from the file and creates objects from each line of code	
		try {
			int counter = 0; // this variable is used to index each row in the file (first row is index zero)
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("customerDatabase.txt"));
			
			while((line = br.readLine())!= null) {
				String [] customers = line.split(",");
				if (customers[6].equals("true")) { // this condition looks for customers who have 'true' as their membership status.
					int customerId = Integer.parseInt(customers[0]);
					String name = customers[1];
					int age = Integer.parseInt(customers[2]);
					String gender = customers[3];
					int postalCode = Integer.parseInt(customers[4]);
					String email = customers[5];
					boolean member = Boolean.parseBoolean(customers[6]);	
					
					memberArray.add(counter, new MemberCustomer(customerId,name,age,gender,postalCode,email,member));
					counter ++;
				}
			}	
			 br.close();
		}
			
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	}
	
	public static void readCustomers() {
	    // Loads all the customers from the txt file and creates objects from each line of code	
		try {
			int counter = 0; // this variable is used to index each row in the txt file (first row is index zero)
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("customerDatabase.txt"));
			
			while((line = br.readLine())!= null) {	
				String [] customers = line.split(",");
				int customerId = Integer.parseInt(customers[0]);
				String name = customers[1];
				int age = Integer.parseInt(customers[2]);
				String gender = customers[3];
				int postalCode = Integer.parseInt(customers[4]);
				String email = customers[5];
				boolean member = Boolean.parseBoolean(customers[6]);
				
				customerArray.add(counter, new Customer(customerId,name,age,gender,postalCode,email,member));
				counter ++;
			}	
			 br.close();
		}
			
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	
	public static void printUpdateDetailsMenu () {
		System.out.println("___________________________________________________");
        System.out.println("|  Which field would you like to update?          |");
        System.out.println("|  Press 1 for name                               |");
        System.out.println("|  Press 2 for age                                |");
        System.out.println("|  Press 3 for gender                             |");
        System.out.println("|  Press 4 for postal code                        |");
        System.out.println("|  Press 5 for email                              |");
        System.out.println("|                                                 |");
        System.out.println("|  Press 0 to return to previous menu             |");
        System.out.println("__________________________________________________");
 	}
        
	public static void printWelcomeMessage () {
		System.out.println("___________________________________________________");
        System.out.println("|      Welcome to the customer interface          |");
        System.out.println("|                                                 |");
        System.out.println("|  Press 1 to create a new customer account       |");
        System.out.println("|  Press 2 if you are a returning customer        |");
        System.out.println("___________________________________________________");
	}
		
	public static void printReturningCustomerMenu () {
		System.out.println("___________________________________________________");
        System.out.println("|                                                 |");
        System.out.println("|  Press 1 to view your account details           |");
        System.out.println("|  Press 2 to update your account details         |");
        System.out.println("|  Press 3 to receive customized offers           |");
        System.out.println("|  Press 4 to view your purchase history          |");
        System.out.println("___________________________________________________");
	}
		
	public static void printChangesSuccessful () {
		System.out.println("___________________________________________________");
        System.out.println("|      Changes have been saved successfully!      |");
        System.out.println("___________________________________________________");
	}
	
	public static void printNewCustomer () {
		System.out.println("___________________________________________________");
        System.out.println("|          You have successfully created          |");
        System.out.println("|               a new account                     |");
        System.out.println("___________________________________________________");
	}
		
	public static void printInvalidInput () {
		System.out.println("___________________________________________________");
        System.out.println("|                Invalid input!                   |");
        System.out.println("___________________________________________________");
	}
}

