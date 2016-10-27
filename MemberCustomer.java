package bonus_system;

import java.util.Scanner;

public class MemberCustomer extends Customer {
	static Scanner userInput = new Scanner(System.in);
	static int memberCustomerCounter;
	
	public static void main (String[] args){
		}

	
	public MemberCustomer (int customerId, String name, int age, String gender, int postalCode, String email, boolean membership) {
		super(customerId,name,age,gender,postalCode,email,membership);
		customerMenu.customerCounter--;//since in super() we have customerCounter++ we need to deduct
		//1 every time new Member is created while reading the txt file
		memberCustomerCounter++;
	}
	
}
