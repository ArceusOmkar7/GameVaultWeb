# GameVault Web Application

A Java web application for managing and selling games online. This document provides information about the application features, specifically focusing on the admin dashboard and JSON data loading functionality.

## Application Overview

GameVault is a web-based game store that allows users to:
- Browse and search for games
- View detailed game information
- Add games to cart
- Complete purchases
- Write reviews
- Manage their account and wallet

Administrators can:
- Manage games (add, edit, delete)
- View user information
- Process orders
- Import game data from JSON
- Upload game images
- Check database status and generate dummy data

## Development Environment

- **Java Version**: JDK 1.8
- **Web Server**: GlassFish 4.1
- **IDE**: NetBeans 8
- **Servlet API**: javax.servlet (Java EE 7)
- **Database**: MySQL with JDBC connector

## Admin Dashboard

The admin dashboard provides a central interface for administrators to manage the GameVault store.

### Features

- **Statistics Overview**: View key metrics like total games, users, orders, and revenue
- **Data Visualization**: View charts for sales, popular games, user growth, etc.
- **Game Management**: Add, edit, and delete games in the store
- **User Management**: View and manage user accounts
- **Order Management**: Process and track customer orders
- **JSON Data Loader**: Import game data from a JSON file
- **Database Status**: Check the status of database tables and connections
- **Dummy Data Generation**: Populate the database with sample data for testing

### Accessing the Admin Dashboard

1. Log in with an admin account
2. Navigate to `/admin/dashboard`

## JSON Game Data Loader

### Overview

The JSON Game Data Loader allows administrators to populate the database with game information from a structured JSON file. This is particularly useful for:

- Initial database setup
- Bulk updating game information
- Restoring game data from backups

### How It Works

1. The application reads the `games.json` file from the `WEB-INF` directory
2. It parses the JSON data into Game objects using a custom JSON parser
3. Each game is saved to the database with all its attributes (title, description, developer, price, platform, genre, rating, etc.)
4. The system keeps track of the load status to prevent duplicate loading

### JSON Game Structure

Each game in the JSON file includes the following information:
- Title
- Description
- Developer
- Platform (array of supported platforms)
- Price
- Release Date
- Image Path
- Rating
- Genre (array of genres)

### Using the JSON Loader

1. Log in with an admin account
2. Navigate to `/admin/dashboard`
3. Click on "Load JSON Data" in the sidebar
4. Review the information and click "Force Reload Data" if you want to reload data

### Technical Details

- No external libraries are used for JSON parsing
- The application uses a custom `JSONUtil` class with Java's built-in features for parsing
- Game data is stored in a MySQL database

## Database Status Feature

### Overview

The Database Status feature allows administrators to verify the integrity of the database structure, ensuring that all required tables and connections are properly set up. This is useful for:

- Diagnosing database connectivity issues
- Verifying that all necessary tables exist
- Checking database schema and structure
- Ensuring the application can properly interact with the database

### How It Works

1. The application uses the `DatabaseCheckServlet` to establish a connection to the database
2. It verifies that all required tables exist (Games, Users, Orders, etc.)
3. The servlet generates a detailed report showing the status of each table
4. Administrators can view this report through the admin dashboard

### Using the Database Status Feature

1. Log in with an admin account
2. Navigate to `/admin/dashboard`
3. Click on "Check Database Status" in the sidebar
4. Review the detailed report showing table statuses and database connections

## Dummy Data Generation

### Overview

The Dummy Data Generator allows administrators to populate the database with sample data for testing and demonstration purposes. This feature:

- Creates sample user accounts with different roles
- Adds games from the `games.json` file to the database
- Generates sample orders and transactions
- Creates dummy reviews for games

### How It Works

1. The system uses a configuration file (`dummy_data_config.txt`) to track data generation status
2. When triggered, the `DummyDataServlet` reads game data from `games.json`
3. It creates sample users, orders, and reviews based on parameters in the config file
4. The system marks the data as loaded to prevent duplicate generation

### Configuration Parameters

The `dummy_data_config.txt` file contains the following settings:
- `dummy_data.loaded`: Boolean flag indicating if data has been loaded
- `games.count`: Number of games to generate
- `orders.count`: Number of sample orders to create
- `users.count`: Number of user accounts to generate
- `last_load_date`: Timestamp of the last data load

### Using the Dummy Data Generator

1. Log in with an admin account
2. Navigate to `/admin/dashboard`
3. Click on "Generate Dummy Data" in the sidebar
4. Review the status and click "Generate Data" to populate the database

## Implemented Features

### User Management
- **Simplified Login System**: Quick login as either Admin or User with a single click
- **User Profiles**: Users can view and edit their profile information
- **Admin Dashboard**: Administrative interface to manage the store

### Game Catalog
- **Game Browsing**: View all available games with filtering and sorting options
- **Game Details**: Detailed view of individual games with descriptions, pricing, and platform information
- **Game Image Management**: Admin users can upload and manage game images
- **Featured Games**: Highlighting selected games on the home page

### Shopping Experience
- **Shopping Cart**: Add games to cart, view cart contents, and remove items
- **Checkout Process**: Complete purchases and convert cart to orders
- **Order History**: View past purchases and transaction details

### Review System
- **Game Reviews**: Users can leave reviews for games they've purchased
- **Rating System**: Provide ratings alongside textual reviews

### Technical Features
- **JDBC Database Integration**: Direct database connectivity for data persistence
- **JSP/Servlet Architecture**: Classic Java EE web application structure
- **Responsive UI**: Mobile-friendly interface using Tailwind CSS
- **Image Handling**: Upload, storage, and retrieval of game cover images

## Project Structure

```
GameVaultWeb/
│
├── src/                           # Source code directory
│   └── java/
│       └── gamevaultbase/         # Base package
│           ├── entities/          # Data models
│           │   ├── Cart.java
│           │   ├── Game.java
│           │   ├── Order.java
│           │   ├── Review.java
│           │   ├── Transaction.java
│           │   └── User.java
│           ├── exceptions/        # Custom exceptions
│           ├── helpers/           # Utility classes
│           ├── interfaces/        # Interfaces for abstraction
│           ├── listeners/         # Application context listeners
│           ├── management/        # Business logic
│           ├── servlets/          # Servlet controllers
│           └── storage/           # Data access layer
│
├── web/                           # Web resources
│   ├── css/                       # Stylesheets
│   ├── game_images/               # Uploaded game cover images
│   ├── index.jsp                  # Entry point
│   └── WEB-INF/
│       ├── jsp/                   # JSP view templates
│       │   ├── adminDashboard.jsp
│       │   ├── cart.jsp
│       │   ├── gameDetail.jsp
│       │   ├── home.jsp
│       │   ├── login.jsp
│       │   └── ...
│       └── web.xml                # Servlet and application config
│
├── lib/                           # External libraries
│   └── mysql-connector-j-9.2.0.jar # MySQL JDBC driver
│
└── build/                         # Compiled application
    └── web/                       # Deployed web content
```

## Setup Instructions

1. **Database Setup**
   - Create a MySQL database named 'gamevaultdb'
   - Database credentials are configured in web.xml:
     - Driver: com.mysql.cj.jdbc.Driver
     - URL: jdbc:mysql://localhost:3306/gamevaultdb
     - Username: root
     - Password: 1234

2. **Deployment**
   - Deploy the WAR file to a Java EE compatible server (e.g., GlassFish, Tomcat)
   - Or run directly from NetBeans IDE with integrated server

3. **Initial Access**
   - Access the application at: http://localhost:8080/GameVaultWeb/
   - Login using the simplified login system:
     - Click "Login as Admin" for administrative access
     - Click "Login as User" for regular user access

## Core Technologies

- **Java EE (Servlets, JSP)**: Server-side processing
- **JDBC**: Database connectivity
- **HTML5/CSS**: Frontend structure and styling
- **Tailwind CSS**: Responsive design framework
- **MySQL**: Relational database

## Database Schema

### Games Table
```sql
CREATE TABLE IF NOT EXISTS Games (
    gameId INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    developer VARCHAR(255),
    platform VARCHAR(255),
    price FLOAT NOT NULL,
    releaseDate DATE,
    imagePath VARCHAR(255),
    genre VARCHAR(100),
    rating FLOAT DEFAULT 0.0
)
```

The application uses the following main database tables:
- Users
- Games (with imagePath for game covers)
- Reviews
- Orders
- OrderItems
- Transactions

## Files and Components

### Key Files
- `GameJsonLoaderServlet.java`: Handles the JSON data loading process
- `JSONUtil.java`: Utility class for parsing JSON without external dependencies
- `Game.java`: Entity class representing a game
- `GameStorage.java`: Handles database operations for games
- `adminDashboard.jsp`: The main admin dashboard interface
- `games.json`: Source file containing game data (located in WEB-INF folder)

## How to Customize

### Adding New Game Properties
1. Update the `Game.java` entity with new fields
2. Modify the `GameStorage.java` to include the new fields in database operations
3. Update the `DBUtil.java` class to include the new fields in the table creation SQL
4. Update the `JSONUtil.java` to parse the new fields from the JSON data
5. Ensure the `games.json` file includes the new properties

### Changing the JSON Format
If you need to change the format of the JSON file, update the parsing logic in `JSONUtil.java` accordingly.

## Troubleshooting

### Common Issues

1. **NullPointerException in GameJsonLoaderServlet**
   - Make sure the user is properly authenticated as an admin
   - Ensure the Session attributes are correctly set

2. **JSON File Not Found**
   - Verify that the `games.json` file exists in the `WEB-INF` directory
   - Check file permissions

3. **Database Connection Issues**
   - Verify database connection parameters in `web.xml`
   - Ensure MySQL server is running
   - Check user permissions

4. **Image Paths Not Working**
   - Ensure the image paths in the JSON file are accessible
   - Verify that the web server has permission to access the image locations

5. **Database Status Button Not Working**
   - Check that the `DatabaseCheckServlet` is properly mapped to `/admin/check-database`
   - Verify that the server has permission to query database metadata
   - Ensure the database user has privileges to view table information

6. **Dummy Data Not Being Generated**
   - Check the `dummy_data_config.txt` file to ensure `dummy_data.loaded` is set to `false`
   - Verify that the `games.json` file contains valid game data
   - Check that the `DummyDataServlet` is properly mapped to `/admin/generate-dummy-data`
   - Ensure proper database write permissions for the application

## Note on Game Delivery
This is a simulated game store created for educational purposes. No actual digital game delivery functionality is implemented. The application focuses on demonstrating e-commerce concepts, catalog management, and the user purchase journey without delivering actual digital products.

## Future Enhancements

To enhance GameVault for this university project, consider implementing these key features:

### User Experience
- **Advanced Search**: Implement more complex search with filtering by multiple parameters
- **Game Recommendations**: Personalized game suggestions based on purchase history
- **Wishlist Feature**: Allow users to save games for later consideration
- **Game Comparison**: Compare features and prices of multiple games side-by-side
- **User Notifications**: In-app alerts for price drops, new releases, etc.

### Game Management
- **Simulated Purchase Confirmation**: Receipt generation without actual delivery
- **Pre-orders**: Support for upcoming game releases (simulated)
- **Game Bundles**: Package multiple games together with special pricing
- **Game Categories**: More extensive categorization by genre, tags, etc.
- **Platform Filtering**: Filter games by supported gaming platforms

### Business Features
- **Mock Payment Processing**: Simulated payment flow for educational purposes
- **Discounts & Promotions**: Sales periods, coupon codes, and promotional pricing
- **Receipt Generation**: Order confirmations for purchases

### Admin Analytics & Reporting
- **Sales Dashboard**: Visual representation of sales data over time
- **Inventory Reports**: Track most/least popular games
- **User Activity Metrics**: Analysis of user behavior and purchasing patterns
- **Revenue Reports**: Financial summaries with filtering by date ranges
- **Platform Distribution**: Breakdown of sales by gaming platform
- **Review Analytics**: Track average ratings and review sentiment
- **Custom Report Generation**: Export data in CSV/PDF formats for analysis

### Technical Improvements
- **Session Management**: Improved user session handling
- **Transaction Processing**: Ensure reliable order processing
- **Error Logging**: Better error handling and logging for troubleshooting

1. Add validation for game data during import
2. Create an interface for editing the JSON file directly
3. Allow selective updates of specific games
4. Add support for managing game categories and tags
5. Implement data export functionality

## License
This project is an educational demonstration for university coursework.

## Contributors
- Initial development: GameVault Team