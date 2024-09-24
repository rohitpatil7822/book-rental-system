# Book Rental System

This is a RESTful API service using Spring Boot to manage an online book rental system with MySQL persistence.

## Features

- User Registration and Login
- Book Management (CRUD operations)
- Rental Management (Rent and Return books)
- Authentication and Authorization (Basic Auth)
- Role-based access control (USER and ADMIN roles)

## Prerequisites

- Java 11 or higher
- Maven
- MySQL

## Setup

1. Clone the repository: https://github.com/rohitpatil7822/book-rental-system

2. Navigate to the project directory

3. Create a MySQL database named `bookrental`.

4. Update the `application.properties` file with your MySQL username and password.

5. Build the project

6. Run the application


The application will start running at `http://localhost:8080`.

## API Endpoints

- POST /api/auth/register - Register a new user
- GET /api/books - Get all books (requires authentication)
- POST /api/books - Create a new book (requires ADMIN role)
- PUT /api/books/{id} - Update a book (requires ADMIN role)
- DELETE /api/books/{id} - Delete a book (requires ADMIN role)
- POST /api/rentals/rent/{bookId} - Rent a book
- POST /api/rentals/return/{bookId} - Return a book

## Running Tests

To run the unit tests, execute the following command: mvn test



## Postman Collection

You can find a Postman collection for testing the API endpoints [https://api.postman.com/collections/21583443-957985ec-4a0e-45fa-a7da-76e14d231f16?access_key=PMAT-01J8HRXTH7Q75K9XHXN7PM191W](link-to-your-postman-collection).

## License

This project is licensed under the MIT License.
