
package bonus_system;

import java.util.*;
import java.io.*;


public class Product {
	
	static Scanner uInput = new Scanner(System.in);
	static int productCounter = 0; 
	static ArrayList <Product> productArray = new ArrayList <Product>(); 
	int productID; 
	String productname;
	double price;
	String category;

	public static void main (String[] args){
	}

	public Product (int productID, String productname, double price, String category){  //set product constructors
			this.productID = productID;
			this.productname = productname;
			this.price = price;
			this.category = category;
			productCounter++;
	}


	public static void saveChangesToFile() {
	    // saves changes made to the productArray and prints all items in productArray to file
		try {	
			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter ("productDatabase.txt", false)));	
			for(int i = 0; i < productCounter; i++) { //stores each object in customer array
				pr.print(productArray.get(i).productID);
				pr.print(",");	
				pr.print(productArray.get(i).productname);
				pr.print(",");
				pr.print(productArray.get(i).price);
				pr.print(",");
				pr.print(productArray.get(i).category);
				pr.println("");
			} pr.close();	
		}
		
		catch(IOException ex) {
			System.out.println("File not found");
		}
	}
	
	
	public static void readProducts() {
	    // Loads all the products from the txt file and creates objects based on each line of the file	
		try {
			int counter = 0; // this variable is used to index each row in the txt file (first row is index zero)
			String line;
				
			BufferedReader br = new BufferedReader(new FileReader("productDatabase.txt"));
			
			while((line = br.readLine())!= null) {	
				String [] items = line.split(",");
				int itemId = Integer.parseInt(items[0]);
				String itemName = items[1];
				double price = Double.parseDouble(items[2]);
				String category = items[3];		
				productArray.add(counter, new Product(itemId,itemName,price,category));
				counter ++;
			}	
			 br.close();
		}
			
		catch (IOException e) {
			System.out.println("File does not exist");
		}
	} 	
	
	
	public static int getLastProductId() {
		String lastProductId = "0";
		try {
			String line;
			BufferedReader br = new	BufferedReader(new FileReader("productDatabase.txt")); 
				while ((line = br.readLine())!= null) {
					String[] wordsInFile = line.split(",");
					lastProductId = wordsInFile[0];
				}
			br.close();
		}
		catch (IOException ex) {
			System.out.println("Error");
		}
		int returningId = Integer.parseInt(lastProductId);
		return returningId;
	}

	public static void createNewProduct () {
		readProducts();
		System.out.println("Please enter product name:");
		String productname = uInput.next(); 
		System.out.println("Please enter price:");
		double price = uInput.nextDouble();
		System.out.println("Please enter category");
		String category = uInput.next();
		int productId = productCounter + 1;
		productArray.add(productId - 1, new Product(productId, productname, price, category));
		saveChangesToFile (); 
		}

//*********************************************************************		
		public static double GetTotalSum(ArrayList<Product> products, ArrayList<Integer> quantities){
			//the method computed total sum of the given products based on their prices & their quantities
			double total_sum=0;
			//declaring ArrayList to fetch the prices of the passed products
			ArrayList <Double> price_list = new ArrayList<Double>();
			for (int i=0; i<products.size(); i++){
				for (int k=0; k<productArray.size(); k++){
					if (productArray.get(k).productID==products.get(i).productID){
						price_list.add(productArray.get(k).price);
						}
					}
			}	for(int j=0 ; j<products.size() ; j++) {
							total_sum += price_list.get(j)*quantities.get(j);
				}
			total_sum = Math.round(total_sum*100.0)/100.0;
			return total_sum;
		}
		
//***********************************************	
		public static String[] GetCategories(){ 
			//the method withdraw list of unique categories
			
			productCounter=getLastProductId();
			ArrayList <String> products_categories = new ArrayList<String>();
			ArrayList <String> unique_categories = new ArrayList <String>();
			for (int i=0; i<productArray.size(); i++){
				products_categories.add(productArray.get(i).category);}
			
			//loop to get list of unique categories - gets rid of the duplicates
			for(String entity: products_categories) {
				if(!unique_categories.contains(entity)) {
					unique_categories.add(entity);
				}
			}
			String[] classes = new String[unique_categories.size()];
			classes = unique_categories.toArray(classes);
			return	classes;
		}	

//*****************************************************************	
		public static Product GetProduct(int product_id){
			//method provides Product object based on the passed product ID
			int target_id = product_id;
			Product target_item=null;
			for (int i=0; i<productArray.size(); i++){
				if (productArray.get(i).productID==target_id){
					target_item = productArray.get(i);
				}
			}
			return target_item;
		}
//**********************************************************************
		public static Product GetProduct(String product_name){
			//the methods provides Product object based on the given product name
			String target_name = product_name;
			Product target_item=null;
			for (int i=0; i<productArray.size(); i++){
				if (productArray.get(i).productname.equals(target_name)){
					target_item = productArray.get(i);
				}
			}
			return target_item;
		}
//***********************************************************************
		public void setPrice(double cost){
			//setter for price
			this.price = cost;
		}
}
