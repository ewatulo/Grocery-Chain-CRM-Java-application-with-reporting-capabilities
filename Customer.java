package bonus_system;
	public class Customer {
		int customerId;
		String name;
		int age;
		String gender;
		int postalCode;
		String email;
		boolean membership;
		
		
		public Customer(int customerId, String name, int age, String gender, int postalCode, String email, boolean membership) {
			this.customerId = customerId;
			this.name = name;
			this.age = age;
			this.gender = gender;
			this.postalCode = postalCode;
			this.email = email;
			this.membership = membership;
			customerMenu.customerCounter++;
		}
		
		public static Customer GetCustomer(int customer_id){
			//the method provides Customer object based on the passed customer id
			customerMenu.readCustomers();
			int target_id = customer_id;
			Customer target_customer=null;
			for (int i=0; i<customerMenu.customerArray.size(); i++){
				if (customerMenu.customerArray.get(i).customerId==target_id){
					target_customer = customerMenu.customerArray.get(i);
				}
			}
			return target_customer;
		}
	}
	
		
