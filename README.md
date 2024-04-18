# Introduction
The inventory management app is a software solution aimed at improving the way of working inside a warehouse, increasing speed and reliability. The inventory management app includes four pages, there will be four windows: a login page to authenticate users, a main page where all the main controls are located, a barcode scanner page, which will include a scanner in combination with basic information about the scanned article and a data page. This application could be used in a business with a warehouse and other departments to streamline the processes.
## Login page:
On this page, the user will be able to authenticate themselves and get certain permissions within the app. The lowest permission will only be able to login to the app and search inside the order and article databases. This will make sure that the whole company is involved with the ordering process.

* staffs use name or staff ID to login
* fetch encrypted password from the database, decrypt it and compare with password that user input

## Registration page

* the system will automatically allocate an ID for new staff
* check if the user name is unique before registration
* encrypt password before it is sent to database

## Main page:
The main page will be the control center of the system, including a button to open the barcode scanner window, a button to open the data page, and a button to log out of the app. The main screen also includes a small portion of information on the latest changes to the database or highlighted orders.
## Data page:
On this page, you can select which database you want to look into for example: the articles or orders. You will be able to filter the data based on time and how big the order is.
## Barcode scanner page:
The barcode scanner will be located on this page. When you scan an article, you can see the basic information about the scanned item. Lastly, there will be a label to give some feedback about the scanning process: found, not found, or scan again.

## Staff management page

* Only the Boss (3rd permission) can access to this page
* Allocate permission for worker
* New staff have 0 permisison initially



# Essential features:

- Barcode scanner
- Database implementation to keep track     of the staff, the available articles, and the ongoing orders
- Login page
- Main page: barcode scanner button,     general controls for the app
- Scanner page
- Data page: look into the database to     get extra information.

# Required:

- Different permissions for the users     (use case for the login): some of the staff can only look into the     database.
- Allocating scanned articles to     different orders, changing the state from “in delivery” to “delivered”
- Keeping track of how many articles are     in the warehouse per order.

# Optional features:

- Making a receipt of the order when it     is complete
- Suggestion for “almost complete     orders” to add a certain article to an order (based on time)
- Keep track of location inside the     warehouse with the use of barcodes.9
- Implement functionality to record and keep track of delivery times
- Adding “highlighted orders” and displaying them on the main screen