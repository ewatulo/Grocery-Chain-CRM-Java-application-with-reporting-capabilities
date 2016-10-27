package bonus_system;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreManager {

	static Scanner uInput = new Scanner(System.in); 
	static int store_managerCounter = 0;  
	static ArrayList <StoreManager> store_managerArray = new ArrayList <StoreManager>(); 
	
	protected String username;
	protected String password;
	protected boolean accessRights;
	
	public static void main(String[] args) {
		
		printMenuOptions();
		
		int choice = 0; // initializes the user's choice in the menu
        while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 can try again
        	System.out.println("Please enter your choice:");
        	choice = uInput.nextInt(); // asks for user input
        	switch (choice){
        	case 1: createNewEmployee();
        			break;
        	case 2: AddNewStore();
        			break;
        	case 3: AddNewProduct();
        			break;
        	case 4: ChangeProductPrice();
					break;
        	case 5: ViewProductList();
					break;
        	case 6: ViewClientList();
					break;
        	case 7: changePassword();
        			break;
        	default: printInvalidInput();
        	}
        }
		
	}
	private static void createNewEmployee () {
		readLogins();
		System.out.println("Please enter username:");
		String userName = uInput.next(); 
		System.out.println("Please enter password:");
		String Password = uInput.next();
		System.out.println("Does the user have manager access control?");
		boolean authorisation = Boolean.parseBoolean(uInput.next());
		store_managerArray.add(store_managerCounter, new StoreManager(userName, Password, authorisation)); 
		saveChangesToFile (); 
		printMenuOptions();
		store_managerArray=null;
	}
		
	private static void AddNewStore(){
		Store.createNewStore();
		System.out.println("Enter the cashier ID number into the Cashier database");
		System.out.println("to save the data of the new cashier employee");
		Cashier.createNewCashier();
		System.out.println("New store creation process completed");
		printMenuOptions();
	}
		
	private static void AddNewProduct(){
		Product.createNewProduct();
		System.out.println("New Product was added to the product list");
		printMenuOptions();
	}
		
	private static void ChangeProductPrice(){
		Product modified_product;
		System.out.println("Please, enter name of the product to be modified");
		String name = uInput.next();
		modified_product = Product.GetProduct(name);
		System.out.println("Enter new price");
		double new_price = uInput.nextDouble();
		modified_product.setPrice(new_price);
		Product.saveChangesToFile();
		System.out.println("The price has been successfully changed");
		System.out.println("-------------------------------");
		System.out.println("ID  Name  Price	Category");
		System.out.println("-------------------------------");
		System.out.println(modified_product.productID + " | " + modified_product.productname + " | " + modified_product.price + " | " + modified_product.category);
		printMenuOptions();
	}
		
	private static void ViewProductList(){
		Product.readProducts();
		System.out.println("-------------------------------");
		System.out.println("ID  Name  Price	Category");
		System.out.println("-------------------------------");
		for (Product item:Product.productArray){
			System.out.println(item.productID + " | " + item.productname + " | " + item.price +"€" + " | " + item.category );
		}
		System.out.println("-------------------------------");
		printMenuOptions();
	}
		
	private static void ViewClientList(){
		customerMenu.readCustomers();
		System.out.println("--------------------------------------------------");
		System.out.println("ID  Name  Gender  Age  Email Address   Zipcode");
		System.out.println("--------------------------------------------------");
		for (Customer client:customerMenu.customerArray){
			System.out.println(client.customerId + " | " + client.name + " | " + client.gender + " | " + client.age + " | " + client.email  + " | " + client.postalCode );
		}
		System.out.println("--------------------------------------------------");
		printMenuOptions();
	}
	private static void changePassword(){
		System.out.println("Please, enter your username");
		String username = uInput.next();
		uInput.nextLine();
		System.out.println("Please, enter your password");
		String code = uInput.next();
		uInput.nextLine();
		StoreManager employee = StoreManager.GetUser(username);
		System.out.println("Please, enter your new password");
		String new_password = uInput.next();
		if (employee.getPassword(username).equals(code)){
			employee.setPassword(new_password, username);
		}
		saveChangesToFile();
		System.out.println("The password has been changed.");
	}
	
	protected static StoreManager GetUser(String userName){
		//the method provides StoreManager object based on the passed username
		//it looks for in both StoreManager objects and Admin objects
		String user_name = userName;
		StoreManager employee = null;
		for (StoreManager user:store_managerArray){
			if (user.username.equals(user_name)){
				employee = user;
			}
			else {
				for (Admin worker:Admin.adminArray){
					if (worker.username.equals(user_name)){
						employee = worker;
					}
				}
			}
		}
		return employee;
	}
//*****************************************
	StoreManager(String login, String codeword, boolean access_control){
		username = login;
		password = codeword;
		accessRights = access_control;
		store_managerCounter++;
	}
	
	public String getUsername(String Pass){
		String user;
		if(Pass.equals(password)){
			user = username;
		}
		else {
			user = "Enter correct password to obtain the username";
		}
		return user;
	}
	
	public void setUsername(String login, String pass){
		if(pass.equals(password)){
			username = login;
		}
		else {
			System.out.println("Incorrect password, setting the user cannot be completed");
		}
	}
	
	public String getPassword(String name){
		String code;
		if (name.equals(username)){
			code = password;
		}
		else {
			code = "Enter correct username to get the password";
		}
		return code;
	}
	
	public void setPassword(String code, String name){
		if (name.equals(username)){
			password = code;
		}
		else {
			System.out.println("Incorrect username, setting password cannot be completed");
		}
	}	
	

	public static StoreManager GetEmployee(String name){
		//the method created for the sake of Menu Class (Reporting Interface)
		//provides StoreManager object based on the passed username
		//it search only in StoreManager objects' Array since the interface is only for Management Staff
		readLogins();
		String user_name = name;
		StoreManager user = null;
		for (StoreManager s:store_managerArray){
			if(s.username.equals(user_name)){
				user = s;
			}
		}
		return user;
	}
	
	public static void readLogins() {
	    // Loads all the customers from the txt file and creates objects from each line of code	
		try {
			
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("loginsDatabase.txt"));
			
			while((line = br.readLine())!= null) {	
				String [] employee = line.split(",");
				String username = employee[0];
				String password = employee[1];
				boolean access_control = Boolean.parseBoolean(employee[2]);
				if (employee[2].equals("false"))Admin.adminArray.add(new Admin(username, password, access_control));
				else store_managerArray.add(new StoreManager(username, password, access_control));
			}	
			 br.close();
		}
			
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	
	
	public static void saveChangesToFile() {
		// saves changes made to the store_amangerArray and prints all items in store_managerArray to file
		try {
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("loginsDatabase.txt", false)));	
			for(int i = 0; i < store_managerCounter; i++) { //stores each object in customer array
				pr.print(store_managerArray.get(i).username);
				pr.print(",");	
				pr.print(store_managerArray.get(i).password);
				pr.print(",");
				pr.print(store_managerArray.get(i).accessRights);
				pr.println("");}
			for(int j = 0; j < Admin.adminCounter; j++) { //stores each object in customer array
				pr.print(Admin.adminArray.get(j).username);
				pr.print(",");	
				pr.print(Admin.adminArray.get(j).password);
				pr.print(",");
				pr.print(Admin.adminArray.get(j).accessRights);
				pr.println("");
			} pr.close();	
		}
			
		catch(IOException ex) {
			System.out.println("File not found");
		}
	}
	
	public static void printInvalidInput () {
		System.out.println("___________________________________________________");
	    System.out.println("|                Invalid input!                   |");
	    System.out.println("___________________________________________________");
	}
	public static void printMenuOptions () {
		System.out.println("_________________________________________________________________");
	    System.out.println("|      Welcome to the Store Manager interface     		|");
	    System.out.println("|                                                 		|");
	    System.out.println("|  Press 1 to create new employee account			|");
	    System.out.println("|  Press 2 to add new store to database				|");
	    System.out.println("|  Press 3 to add new product to database			|");
	    System.out.println("|  Press 4 to change product price				|");
	    System.out.println("|  Press 5 to view Product Catalog				|");
	    System.out.println("|  Press 6 to view Customer List				|");
	    System.out.println("|  Press 7 to change password					|");
	    System.out.println("__________________________________________________________________");
	}
}

