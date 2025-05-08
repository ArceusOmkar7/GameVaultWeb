# GameVault Web Application

## Project Overview
GameVault is a web-based digital game store platform built with Java EE technologies. It allows users to browse, purchase, and manage digital game licenses, while providing administrators with tools to manage the game catalog and monitor sales.

## Modularization and Optimization

### Project Structure Optimization
The project has been fully modularized to improve maintainability, reduce code duplication, and enhance performance:

1. **MVC Architecture**
   - **Model**: Entity classes in `entities/` package
   - **View**: JSP files in `web/WEB-INF/jsp/` with component-based structure
   - **Controller**: Servlet classes in `servlets/` package

2. **Component-Based UI**
   - Reusable UI components in `web/WEB-INF/jsp/components/`
   - Common layouts in `web/WEB-INF/jsp/layouts/`
   - External CSS moved to `web/css/gamevault.css` for browser caching

3. **Utility Classes**
   - `AppUtil.java`: Application-level utility methods
   - `ServletUtil.java`: Servlet-specific utility methods
   - `DBUtil.java`: Database operations utility methods
   - `JSONUtil.java`: JSON processing utility methods

### Key Optimizations

#### 1. Removed Redundant Files
- `Helper.java` - Removed console table-printing methods not needed in a web application
- `DbInitializer.java` - Removed hardcoded game data in favor of JSON-based approach
- Removed duplicated header/footer files in favor of component-based approach
- Eliminated redundant theme.jsp files by externalizing CSS
- Removed duplicated index files in build directory

#### 2. CSS Externalization
- Moved inline CSS from `theme.jsp` to external `gamevault.css` file
- Added more comprehensive styling for all UI components
- Improved performance through browser caching

#### 3. Servlet Modularization
- Updated servlets to use the utility classes for common operations
- Removed HTML generation from servlets in favor of JSP views
- Standardized error handling and user authentication

#### 4. JSP-Based Views
- Created dedicated JSP views for all admin functions
- Implemented component-based architecture for UI elements
- Standardized layouts with header, footer, and sidebar components
- Created specialized admin layout with sidebar navigation

#### 5. Automated JSON Data Loading
- Enhanced `JSONUtil.java` to use the javax.json API for more robust JSON parsing
- Implemented automatic game data loading from JSON when database is empty
- Added proper handling of array-based JSON fields (platforms, genres) by converting to comma-separated strings
- Added helper methods to Game entity for working with platforms and genres as lists

#### 6. Normalized Database Schema
- Implemented separate tables for genres and platforms
- Created many-to-many relationships between games and genres/platforms
- Maintained backward compatibility with string-based approach
- Enhanced database operations to handle the relational data model

## Core Features

### User Features
- Account registration and authentication
- Game browsing with search and filtering
- Shopping cart management
- Purchase checkout process
- Order history and game library access
- User profile management

### Admin Features
- Dashboard with sales statistics
- Game management (add, edit, delete)
- User management
- Database utilities
- Game image upload functionality

## Technical Implementation

### Technologies Used
- Java Servlets & JSP
- MySQL Database
- Tailwind CSS (via CDN)
- JSON for data storage and transfer
- javax.json API for JSON processing

### Application Architecture
- **Storage Layer**: Database access via Storage classes implementing the StorageInterface
- **Entity Layer**: Java beans representing business objects
- **Management Layer**: Business logic encapsulation
- **Servlet Layer**: HTTP request handling
- **JSP Layer**: View rendering with component-based structure

### Database Schema
The application uses a normalized relational database schema:

- **Games**: Core game information (title, description, price, etc.)
- **Users**: User account information
- **Orders/OrderItems**: Purchase history
- **Carts/CartItems**: Shopping cart contents
- **Genres**: Game genre categories
- **Platforms**: Gaming platforms
- **GameGenres**: Many-to-many mapping between games and genres
- **GamePlatforms**: Many-to-many mapping between games and platforms
- **Reviews**: User reviews for games
- **Transactions**: Financial transaction records

### Entity Relationships

#### Games and Genres/Platforms
- Each game can have multiple genres (RPG, Action, Strategy, etc.)
- Each game can be available on multiple platforms (PC, Xbox, PlayStation, etc.)
- The relationships are managed through junction tables (GameGenres, GamePlatforms)
- Legacy string fields maintained for backward compatibility

## Setup and Development

### Prerequisites
- Java Development Kit (JDK) 11+
- Apache Tomcat 9+
- MySQL Database
- IDE with Java EE support (NetBeans, IntelliJ, etc.)

### Database Setup
1. Create a MySQL database named `gamevault`
2. Update database connection settings in `DBUtil.java` if needed
3. Run the application which will automatically create the necessary tables and load game data from JSON

### Development Workflow
1. Use the `/admin/check-database` endpoint to verify database structure
2. Game data will be automatically loaded from JSON on first startup if the database is empty
3. Admin credentials: username: `admin@gamevault.com`, password: `admin123`

## Completed Tasks
- ✅ Full modularization of JSP files into components and layouts
- ✅ Creation of specialized admin layout with sidebar navigation
- ✅ Moving header and footer to components directory
- ✅ CSS externalization for improved performance
- ✅ Removal of redundant files from project structure
- ✅ Standardization of error handling across the application
- ✅ Implementation of component-based UI architecture
- ✅ Created modular servlet structure with base classes:
  - BaseServlet: Common functionality for all servlets
  - PublicBaseServlet: For pages accessible to all users
  - UserBaseServlet: For pages requiring user login
  - AdminBaseServlet: For admin-only pages
- ✅ Refactored initial servlets to use the new modular structure
- ✅ Completed refactoring of all servlets to use new modular structure
- ✅ Removed servlet mapping conflicts in web.xml
- ✅ Implemented automatic JSON data loading on application startup
- ✅ Upgraded JSON parsing to use javax.json API
- ✅ Added robust handling of JSON arrays for platforms and genres
- ✅ Enhanced Game entity with helper methods for working with platform and genre lists
- ✅ Normalized database schema with separate tables for genres and platforms
- ✅ Implemented many-to-many relationships for games-genres and games-platforms
- ✅ Updated GameStorage to handle the new entity relationships
- ✅ Modified JSON loading process to populate the related tables
- ✅ Maintained backward compatibility with string-based approach

## Tasks Remaining
- ⏳ Update all JSP files to use new layout system
- ⏳ Test all routes with new component structure
- ⏳ Ensure proper error handling in all servlets
- ⏳ Optimize database queries for better performance
- ⏳ Improve mobile responsiveness of the UI
- ⏳ Add unit tests for critical business logic
- ⏳ Complete documentation for all endpoints

## Future Enhancements
- RESTful API integration
- Advanced search capabilities
- User reviews and ratings system
- Wishlist functionality
- Payment gateway integration
- Responsive email notifications
- Game recommendation engine