## Description
The following is the high-level description for BestDeal website:
- The intent is to build servlet-based web application that will allow customers to place orders online from BestDeal website
- The store has a StoreManager, Customers, and Salesmen
- The retailers sells different types of products
- The StoreManager can Add/Delete/Update products
- Salesman can create Customer accounts and can Add/Delete/Update customers’ orders
- Every product might have accessories that could be bought separately
- Retailer offers warranty that can be purchased by the customer for every console
- The customer is able to create an account online
- The customer is able to place an order online, check the status of the order, or cancel the order.
- The customer can pay using a credit card
- Some of the products may have retailer special-discounts
- Some of the products may have manufacturer rebates
- Customer shall be able to shop online to buy one or multiple items in the same session from the BestDeal online retailer.
- In the same session, the customer must be able to add or remove items from the shopping cart
- When the customer chooses to check out:
    1. The customer will enter personal information (Name, Address, Credit Card, etc.)
    2. The customer will be provided with a confirmation number and a delivery date (2 weeks after the order date) that the customer can use to cancel an order at a later timer, though it must be 5 business days before the delivery date.

## Installation, Compliation and Execution
- Unzip the compressed file.
- Put unzipped folder "BestDeal" under Tomcat's webapp path (%TOMCAT_HOME\webapps).
- All source codes are under the folder \BestDeal\WEB-INF\classes. In case you need to recompile, open command prompt window, navigate to %TOMCAT_HOME\webapps\BestDeals\WEB-INF\classes and issue javac command. Launch Tomcat server, the servlet application now can be accessed via http://localhost/BestDeal/

## Notes
- For testing purpose, 3 users will be created upon launching. These users are:
    1. username = "aa", password = "aa", type = "Customer";
    2. username = "as", password = "as", type = "Salesman";
    3. username = "ad", password = "ad", type = "Store Manager".
	You can also create your own user accounts as usual.
- In case needs to move the "BestDeal" folder into different location or change its name, you can simply modified the path information by editing Properties.java. There are 6 global attributes defined in that file:
    1. TOMCAT_HOME: defines the Tomcat installation location;
    2. WEBAPP_PATH: defines location of webapps folder;
    3. PROJECT_PATH: defines location of BestDeal folder;
    4. PRODUCT_CATALOG_PATH: defines the name of product catalog XML file;
    5. USER_DETAILS_PATH: defines the name of user details file;
    6. PAYMENT_DETAILS_PATH: defines the name of payment details file.
