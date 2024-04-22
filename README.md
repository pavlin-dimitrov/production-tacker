# Production Tracker

## Introduction
Production Tracker is an application designed to manage and track production orders, providing tools for monitoring progress, editing orders, and managing items effectively. This tool is especially useful for manufacturing or assembly environments where staying up-to-date with production statuses is crucial.

## Features
- **Track Production Progress**: Monitor the status of production orders.
- **Edit Orders**: Update details about production orders as needed.
- **Manage Items**: Edit and delete items associated with orders.
- **User Authentication**: Manage user access and roles.
- **Admin Controls**: Special administrative functionalities for managing all aspects of production.

## Technology Stack
- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Spring Boot (Java)
- **Database**: MySQL (Hosted on Railway)
- **Deployment**: Deployed on Railway platform

## Local Development Setup
To set up Production Tracker locally, follow these steps:

1. **Clone the Repository**
   ```bash
   git clone https://github.com/pavlin-dimitrov/production-tracker.git
   cd production-tracker
   ```

2. **Configure Environment Variables**
   Create a `.env` file or set environment variables for your database configuration:
   ```plaintext
   DATABASE_URL=jdbc:mysql://localhost:3306/yourDatabase
   DATABASE_USERNAME=yourUsername
   DATABASE_PASSWORD=yourPassword
   ```

3. **Build and Run the Application**
   If using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   Access the application at `http://localhost:8080`.

## Deployment
This application is deployed on Railway. To deploy your own instance:

1. **Set up on Railway**
   - Create a new project on Railway.
   - Add a MySQL plugin and configure the database using the Railway dashboard.

2. **Deploy the Application**
   - Link your GitHub repository to the Railway project.
   - Set environment variables on Railway to match your database configuration.

3. **Access the Application**
   - Once deployed, visit the provided URL by Railway to access your application.
## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details.

## Contact
For any questions or suggestions, please reach out to [Pavlin Dimitrov](mailto:pavlin.k.dimitrov@gmail.com).
