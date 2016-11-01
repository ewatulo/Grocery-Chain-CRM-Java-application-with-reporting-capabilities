# Grocery-Chain-CRM-Java-application-with-reporting-capabilities
crm application with reporting capabilities

The application keeps track of customers' purchase histories in all stores of a grocery chain.
New transactions (receipts) can be generated and added to the database via Cashier Class.
customerMenu Class stands for an interface for customers (Customer Class) who have Bonus cards, the interface enables them to view their 
profile,update their personal data, check their transactions and modify their membership (to a MemberCustomer in order to receive 
special offers).
StoreManager Class constitutes an interface for the following: adding new store/employee/product, product price update, password update,
access to existing product list/client list.
Admin (subclass of StoreManager) used for access management.

All reporting functionalities are in Menu Class, the examples of the reports are: 
- total sales per store for a given product in a given period of time
- total sales per category in a given time interval (set by user)
- total sales per product as above
- selection of customers who have bought certain product's quantity beyond/below a certain threshol over a given period of time
(the selection is conducted in order to obtain customers to whom marketing offers/promo emails are to be sent)

The applciation does not have a GUI.
