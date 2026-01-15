# Todo Manager

A command-line task management application developed through an iterative sprint approach, demonstrating progressive Java programming concepts from basic control structures to object-oriented design patterns with exception handling.

**Author:** Aeron Flores

---

## Project Overview

This project showcases the evolution of a todo management system across five sprints, each building upon the previous version with enhanced features and improved code architecture. The application progresses from simple task listing to a fully-featured system with user authentication, role-based access control, task completion tracking, and custom exception handling.

---

## Sprint Evolution

### Sprint 1: Foundation
**File:** `TodoManagerSprint1.java`

**Features:**
- Display user name and welcome message
- List 5 hardcoded tasks
- Sort tasks alphabetically (ascending and descending)
- Detect duplicate tasks

**Key Concepts:**
- Basic variables and strings
- Manual bubble sort implementation using if-else statements
- Array manipulation
- Boolean logic for duplicate detection

---

### Sprint 2: Dynamic Task Management
**File:** `TodoManagerSprint2.java`

**Features:**
- Interactive menu-driven interface
- Add, view, update, and delete tasks
- Search tasks by keyword
- Sort tasks dynamically (A-Z / Z-A)
- Check for duplicate tasks
- Support for up to 100 tasks

**Key Concepts:**
- Scanner for user input
- While loops for menu navigation
- Static methods for code organization
- Array resizing and shifting
- Input validation and error handling

---

### Sprint 3: Object-Oriented Refactoring
**File:** `TodoManagerSprint3.java`

**Features:**
- Enhanced task structure with ID, title, description, and assignee
- CRUD operations (Create, Read, Update, Delete)
- Search across multiple task fields
- Improved duplicate detection by task title

**Key Concepts:**
- POJO (Plain Old Java Object) design pattern
- DAO (Data Access Object) pattern
- Encapsulation with getters and setters
- Separation of concerns (Task class vs TaskDAO class)
- Object-oriented design principles

---

### Sprint 4: User Authentication & Authorization
**File:** `TodoManagerSprint4.java`

**Features:**
- User registration and login system
- Two user roles: Client and Visitor
- **Client privileges:** Full CRUD access to all tasks
- **Visitor privileges:** View only assigned tasks
- Role-based menu systems
- User management and viewing

**Key Concepts:**
- Class inheritance (User → Client/Visitor)
- Interface implementation (UserDAO, TaskDAO)
- Polymorphism and abstract interfaces
- Authentication flow
- Role-based access control (RBAC)

---

### Sprint 5: Task Completion & Exception Handling
**File:** `TodoManagerSprint5.java`

**Features:**
- Task completion dates with validation
- Mark tasks as completed (Visitor feature)
- Filter tasks by completion status (completed/incomplete)
- Sort tasks by completion date (earliest/latest first)
- Custom exception handling for all operations
- Improved logout flow (can re-login as different user)

**Key Concepts:**
- LocalDate for date handling
- DateTimeFormatter for date formatting (DD-MM-YYYY)
- Custom exceptions (ClientException, VisitorException, TaskException, UserException)
- Try-catch blocks for error handling
- Date validation (prevent past dates)
- Boolean flags for task completion status

---

## Usage Guide

### Sprint 5 Example Workflow

1. **Registration**
   - Choose option 1 to register
   - Enter username and password
   - Select user type (Client or Visitor)

2. **Login**
   - Choose option 2 to login
   - Enter credentials

3. **Client Actions**
   - Add tasks with completion dates (format: DD-MM-YYYY)
   - Assign tasks to registered users
   - View, update, or delete any task
   - Search and sort tasks by date
   - Check for duplicates
   - View all registered users

4. **Visitor Actions**
   - View tasks assigned to them
   - Sort their tasks by completion date
   - Mark tasks as completed
   - View completed vs incomplete tasks separately

5. **Logout & Re-login**
   - Logout returns to authentication menu
   - Can login as different user without restarting app

---

## Architecture

### Design Patterns Used

**Sprint 1-2:**
- Procedural programming with static methods

**Sprint 3:**
- POJO pattern for Task entity
- DAO pattern for data access layer

**Sprint 4:**
- Interface-based design
- Inheritance hierarchy (User → Client/Visitor)
- Implementation classes (UserDAOImpl, TaskDAOImpl)
- Separation of authentication and business logic

**Sprint 5:**
- Custom exception hierarchy
- Date validation layer
- Enhanced error handling with try-catch blocks

### Class Structure (Sprint 5)

```
Custom Exceptions:
├── ClientException
├── VisitorException
├── TaskException
└── UserException

User (Parent Class)
├── Client (extends User)
└── Visitor (extends User)

Task (Entity/POJO)
├── taskId (auto-generated)
├── taskTitle
├── taskText
├── assignedTo
├── completionDate (LocalDate)
└── isCompleted (boolean)

Interfaces:
├── UserDAO
└── TaskDAO

Implementations:
├── UserDAOImpl (implements UserDAO)
└── TaskDAOImpl (implements TaskDAO)

TodoManagerSprint5 (Main class - Menu & UI only)
```

---

## Date Format

**Important:** All dates must use the format **DD-MM-YYYY**

Examples:
- `15-01-2026` (January 15, 2026)
- `01-12-2026` (December 1, 2026)
- `25-03-2026` (March 25, 2026)

**Rules:**
- Day and month must be 2 digits
- Year must be 4 digits
- Dates cannot be in the past
- Use hyphens (-) as separators

---

## Learning Objectives Achieved

- **Control Structures:** if-else, while loops, for loops
- **Data Structures:** Arrays, array manipulation
- **OOP Principles:** Encapsulation, inheritance, polymorphism, abstraction
- **Design Patterns:** POJO, DAO, Interface-based design
- **User Input:** Scanner class and input validation
- **Error Handling:** Custom exceptions, try-catch blocks, input validation
- **Date Handling:** LocalDate, DateTimeFormatter, date parsing
- **Code Organization:** Separation of concerns, modular design
- **Authentication:** User management and role-based access
- **Exception Hierarchy:** Custom exception classes for different error types

---

## Refactoring Journey

Each sprint demonstrates progressive refactoring:

1. **Sprint 1 → 2:** From hardcoded values to dynamic user input
2. **Sprint 2 → 3:** From procedural to object-oriented design
3. **Sprint 3 → 4:** From single-user to multi-user with authentication
4. **Sprint 4 → 5:** Added date validation, task completion tracking, and comprehensive exception handling

Code comments throughout the sprints highlight the evolution and reasoning behind design decisions.

---

## Exception Handling (Sprint 5)

### Custom Exceptions

**ClientException**
- Thrown when clients perform invalid operations
- Examples: No users available, user doesn't exist

**VisitorException**
- Thrown when visitors try unauthorized actions
- Examples: Marking someone else's task, task already completed

**TaskException**
- Thrown for task-related errors
- Examples: Empty fields, invalid dates, task not found

**UserException**
- Thrown for authentication errors
- Examples: Empty credentials, duplicate username, invalid login

All exceptions provide clear error messages to guide users in correcting their input.

---

## Author

**Aeron Flores**

Feel free to explore each sprint to see the progression of Java programming concepts and best practices!
