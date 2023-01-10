# Personal Finance Manager

The service for control users expenses and income.

## Description

The program was created to organize your own income and expenses. By saving all completed transactions in the database, the user can create reports for a certain period for data analysis.

## Features and capabilities

The project implements the main necessary functions and opportunities for controlling one's own income and expenses.

### Features
* [Create, read, update and delete a category](#categories);
* [Create, read, update and delete a transaction](#transactions).

### Capabilities
* [Expense/revenue report by category](#expenserevenue-report-by-category);
* [Expense/revenue report by days](#expenserevenue-report-by-days);
* [Report on the dynamics of expenses/revenue for the category in the year (by years)](#report-on-the-dynamics-of-expensesrevenue-for-the-category-in-the-year-by-years).

## Usage

### Authentication
* **Login**
  
  To enter the system and gain access to the functionality, the user needs to go to the link: login and enter his login and password.
  
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211407008-a49bda2a-d197-451b-836f-0e842b1f9279.png" width=60%>
  </p>
  
* **Logout**
  
  To log out of the system, the user needs to select: Username > Log out in the upper navigation panel.
  
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211524094-286ac302-af6c-4322-8b53-f00c3e4184ca.png" width=75%>
  </p>

### Categories
* **Create a category**
  
  To save a category, you need to go to the link: categories/new or select: Categories > Add new category in the upper navigation panel. In the window that opens, you need to enter the name and description of the new category.

  The following restrictions have been introduced for the category name:
    - The minimum name length is 2 characters;
    - The maximum name length is 50 characters;
    - The name must be unique.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211526453-8db1f9c0-7a55-494c-8410-b8e517087ebb.png" width=92%>
  </p>

* **Read categories**
  
  To view all categories, you need to go to the link: categories or select: Categories > List of categories in the upper navigation panel. If more than seven categories are stored in the database, a pagination mechanism appears on the page to view the categories in parts. You can select a category in the table and update or delete it.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211553870-e72659df-397f-4593-a692-5b4c8e1b362e.png" width=92%>
  </p>

* **Update a category**
  
  To update a category, you need to go to the link: categories/id/update, where id is the identifier of the desired category or click on the yellow "Edit" button next to the corresponding category. A form with category data appears in the open window, which can be changed if necessary. When changing a name, you should consider the restrictions that apply to it.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211554237-798584a6-8ba8-4789-b0d5-03ebf0376467.png" width=92%>
  </p>
  
  If the update is successful, a corresponding message appears on the page.
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211554429-bdbe68b5-aede-4167-a245-35996e88009a.png" width=92%>
  </p>
  
* **Delete a category**
  
  To delete a category, click on the red "Delete" button next to the corresponding category. Before performing the process, a window will appear on the screen asking you to confirm the deletion of the category.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211554651-05d042b4-d263-48e8-aced-a22c3596d8d1.png" width=92%>
  </p>
  
  In case of successful deletion, a corresponding message appears on the page.
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211554667-53047439-d666-4009-afba-e038b3b997a2.png" width=92%>
  </p>

### Transactions
* **Create a transactions**
  
  To save a transaction, you need to go to the link: transactions/new or select: Transactions > Add new transaction in the upper navigation panel. In the open window, you need to fill in all the fields of the new transaction: category name, type of operation, amount, brief description, date.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211555906-1a0fb72e-fbb0-4d7f-a1e8-589d062e2d76.png" width=92%>
  </p>

* **Read transactions**
  
  To view all transactions, you need to go to the link: transactions or select: Transactions > List of transactions in the upper navigation panel. If more than seven transactions are stored in the database, a pagination mechanism appears on the page to view the transactions in parts. You can select a transaction in the table and update or delete it.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211556078-e1989a7d-3729-42a3-85dd-8bb89e0b4192.png" width=92%>
  </p>

* **Update a transaction**
  
  To update a transaction, you need to go to the link: transactions/id/update, where id is the identifier of the desired transaction or click on the yellow "Edit" button next to the corresponding transaction. A form with transaction data appears in the open window, which can be changed if necessary.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211556365-1f588e74-6217-455a-8b4b-3246cca90347.png" width=92%>
  </p>
  If the update is successful, a corresponding message appears on the page.
  
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211556406-5d5e1d1e-fcc5-40cb-bd98-d52aece10597.png" width=92%>
  </p>
  
* **Delete a transaction**

  To delete a transaction, click on the red "Delete" button next to the corresponding transaction. Before performing the process, a window will appear on the screen with a question to confirm the deletion of the transaction.

  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211556719-b5355c5b-fc3e-4641-b057-1d1038316c25.png" width=92%>
  </p>
  
  In case of successful deletion, a corresponding message appears on the page.
  <p align="center">
  <img src="https://user-images.githubusercontent.com/38464535/211556752-463a6719-a913-4899-ba0f-8030f86100e8.png" width=92%>
  </p>


### Expense/revenue report by category


### Expense/revenue report by days


### Report on the dynamics of expenses/revenue for the category in the year (by years)

## Testing

### Built With

* Spring Framework
* JPA
* MySQL
* JUnit 5
* Bootstrap
* Thymeleaf

## Getting Started

### Installing

1. Clone the repo
   ```sh
   git clone https://github.com/PavloDrabchuk/fin-manager.git
   ```
2. Rename `application.properties.example` to `application.properties`
3. Enter your datasource settings in `application.properties`
   ```properties
   spring.datasource.url=datasource_url
   spring.datasource.username=username
   spring.datasource.password=password

### Executing program

* Run project
* Open in your web browser:
   ```
   http://localhost:8080/
   ```

## Help

If you have a problem, write me: ravluk2000@gmail.com

## Authors

* **Pavlo Drabchuk** - ravluk2000@gmail.com
