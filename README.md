# GameVault - Online Game Store

GameVault is a web-based game store application built using Java EE technologies. This project implements core e-commerce functionality for browsing, purchasing, and managing a simulated game catalog.

## Development Environment

- **Java Version**: JDK 1.8
- **Web Server**: GlassFish 4.1
- **IDE**: NetBeans 8
- **Servlet API**: javax.servlet (Java EE 7)
- **Database**: MySQL with JDBC connector

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

## Database Schema
The application uses the following main database tables:
- Users
- Games (with imagePath for game covers)
- Reviews
- Orders
- OrderItems
- Transactions

## License
This project is an educational demonstration for university coursework.

## Contributors
- Initial development: GameVault Team