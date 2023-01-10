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



### Transactions


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
