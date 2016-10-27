package bonus_system;

import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends StoreManager {
	
	static Scanner uInput = new Scanner(System.in); 
	static int adminCounter = 0;  
	static ArrayList <Admin> adminArray = new ArrayList <Admin>(); 

	public static void main(String[] args) {
		
		printMenuOptions();
		
		int choice = 0; // initializes the user's choice in the menu
        while (true) { // this loop ensures that even a person that doesn't enter 1 or 2 can try again
        	System.out.println("Please enter your choice:");
        	choice = uInput.nextInt(); // asks for user input
        	switch (choice){
        	case 1: changeUsername();
        			break;
        	case 2: changePassword();
        			break;
        	default: printInvalidInput();
        	}
        }
	}
//*************************************
	private static void changeUsername(){
		StoreManager.readLogins();
		System.out.println("Please, enter the username to be modified");
		String search_user = uInput.next();
		uInput.nextLine();
		StoreManager person = StoreManager.GetUser(search_user);
			
		System.out.println("Please, enter your username");
		String admin_user = uInput.next();
		uInput.nextLine();
		Admin admin =(Admin) StoreManager.GetUser(admin_user);
		admin.viewUsername(person);
			
		System.out.println("Please, enter the new username to be set");
		String new_username = uInput.next();
		admin.editUsername(person, new_username);
			
		System.out.println("The username has been changed.");
		admin.viewUsername(person);
		adminArray=null;
	}
//******************
	private static void changePassword(){
		StoreManager.readLogins();
		System.out.println("Please, enter the user whose password will be modified");
		String search_user = uInput.next();
		uInput.nextLine();
		StoreManager person = StoreManager.GetUser(search_user);
			
		System.out.println("Please, enter your username");
		String admin_user = uInput.next();
		uInput.nextLine();
		Admin admin =(Admin) StoreManager.GetUser(admin_user);
		admin.viewPassword(person);
			
		System.out.println("Please, enter the new password to be set");
		String new_pass = uInput.next();
		admin.editPassword(person, new_pass);
			
		System.out.println("The username has been changed.");
		admin.viewPassword(person);
		adminArray=null;
	}

	Admin(String username, String password, boolean accessRights){
		super(username, password, accessRights);
		store_managerCounter--;
		adminCounter++;
	}

//****************
	public void editUsername(StoreManager user, String newUsername){
		user.setUsername(newUsername, user.password);
		StoreManager.saveChangesToFile();
		adminArray=null;
	}
	
	public void editPassword(StoreManager employee, String new_password){
		employee.setPassword(new_password, employee.username);
		StoreManager.saveChangesToFile();
		adminArray=null;
	}
	public void viewPassword(StoreManager user){
		String password = user.getPassword(user.username);
		System.out.println("The requested password is: " + password);
	}
	
	public void viewUsername(StoreManager employee){
		String pass = employee.getUsername(employee.password);
		System.out.println("The requested username is: " + pass);
	}
	public static void printInvalidInput () {
		System.out.println("___________________________________________________");
	    System.out.println("|                Invalid input!                   |");
	    System.out.println("___________________________________________________");
	}
	public static void printMenuOptions () {
		System.out.println("_________________________________________________________________");
	    System.out.println("|      Welcome to the Technical Administrator interface     	|");
	    System.out.println("|                                                 		|");
	    System.out.println("|  Press 1 to change username					|");
	    System.out.println("|  Press 2 to change password					|");
	    System.out.println("__________________________________________________________________");
	}
}
