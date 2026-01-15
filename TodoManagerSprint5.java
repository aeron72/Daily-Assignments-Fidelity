import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// CUSTOM EXCEPTIONS 

/**
 * Custom exception for client-specific errors
 * Thrown when clients perform invalid operations
 */
class ClientException extends Exception {
    public ClientException(String message) {
        super(message);
    }
}

/**
 * Custom exception for visitor-specific errors
 * Thrown when visitors try to perform unauthorized actions
 */
class VisitorException extends Exception {
    public VisitorException(String message) {
        super(message);
    }
}

/**
 * Custom exception for task-related errors
 * Thrown for invalid task operations (empty fields, invalid dates, etc.)
 */
class TaskException extends Exception {
    public TaskException(String message) {
        super(message);
    }
}

/**
 * Custom exception for user-related errors
 * Thrown for authentication and registration errors
 */
class UserException extends Exception {
    public UserException(String message) {
        super(message);
    }
}

// USER CLASSES (Inheritance)

/**
 * Parent User class
 * Contains common properties for all users
 */
class User {
    private String username;
    private String password;
    private String userType; // "client" or "visitor"
    
     //Constructor for User
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    // Getter methods to access private fields
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getUserType() {
        return userType;
    }
}

/**
 * Client class - extends User
 * Clients have full access to all features
 */
class Client extends User {
    public Client(String username, String password) {
        super(username, password, "client"); // Calls parent constructor
    }
}

/**
 * Visitor class - extends User
 * Visitors can only view and complete their assigned tasks
 */
class Visitor extends User {
    public Visitor(String username, String password) {
        super(username, password, "visitor"); // Calls parent constructor
    }
}


// TASK CLASS (POJO)

/**
 * Task POJO (Plain Old Java Object)
 * Represents a task with all its properties
 * Enhanced in Sprint 5 with completion date and status
 */
class Task {
    private int taskId;                  // Auto-generated unique ID
    private String taskTitle;            // Task title
    private String taskText;             // Task description
    private String assignedTo;           // Username of assigned user
    private LocalDate completionDate;    // Sprint 5: Deadline for task
    private boolean isCompleted;         // Sprint 5: Completion status
    
    /**
     * Task constructor
     * Creates a new task with INCOMPLETE status by default
     */
    public Task(int taskId, String taskTitle, String taskText, String assignedTo, LocalDate completionDate) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskText = taskText;
        this.assignedTo = assignedTo;
        this.completionDate = completionDate;
        this.isCompleted = false; // New tasks are incomplete by default
    }
    
    // Getter methods
    public int getTaskId() {
        return taskId;
    }
    
    public String getTaskTitle() {
        return taskTitle;
    }
    
    public String getTaskText() {
        return taskText;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public LocalDate getCompletionDate() {
        return completionDate;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    // Setter methods for updating task
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    
    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }
    
    /**
     * Sprint 5: Mark task as completed or incomplete
     */
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }
    
    /**
     * Override toString() to display task details nicely
     * Includes Sprint 5 additions: completion date and status
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "Task ID: " + taskId + "\n" +
               "Title: " + taskTitle + "\n" +
               "Description: " + taskText + "\n" +
               "Assigned To: " + assignedTo + "\n" +
               "Completion Date: " + completionDate.format(formatter) + "\n" +
               "Status: " + (isCompleted ? "COMPLETED" : "INCOMPLETE");
    }
}


// DAO INTERFACES 

/**
 * UserDAO Interface
 * Defines contract for user-related operations
 * Sprint 5: Methods now throw custom exceptions
 */
interface UserDAO {
    boolean registerUser(String username, String password, String userType) throws UserException;
    User login(String username, String password) throws UserException;
    boolean userExists(String username);
    User[] getAllUsers();
    int getUserCount();
}

/**
 * TaskDAO Interface
 * Defines contract for task-related CRUD operations
 * Sprint 5: Added completion date parameter and new methods for task completion
 */
interface TaskDAO {
    boolean addTask(String taskTitle, String taskText, String assignedTo, LocalDate completionDate) throws TaskException;
    Task[] getAllTasks();
    Task getTaskById(int taskId) throws TaskException;
    boolean updateTask(int taskId, String taskTitle, String taskText, String assignedTo, LocalDate completionDate) throws TaskException;
    boolean deleteTask(int taskId) throws TaskException;
    Task[] searchTasks(String keyword);
    Task[] getTasksByAssignee(String username);
    Task[] getCompletedTasks(String username);      // Sprint 5: Get only completed tasks
    Task[] getIncompleteTasks(String username);     // Sprint 5: Get only incomplete tasks
    boolean markTaskAsCompleted(int taskId, String username) throws VisitorException, TaskException; // Sprint 5
    int getTaskCount();
    Task[] getTasksSortedByDate(boolean ascending); // Sprint 5: Sort by completion date
    void checkDuplicates();
}

// DAO IMPLEMENTATIONS 

/**
 * UserDAOImpl - Implementation of UserDAO interface
 * Handles all user-related database operations using arrays
 * Sprint 5: Enhanced with exception handling
 */
class UserDAOImpl implements UserDAO {
    private User[] users;      // Array to store users
    private int userCount;     // Current number of users
    
    /**
     * Constructor - initializes user storage
     */
    public UserDAOImpl(int maxSize) {
        users = new User[maxSize];
        userCount = 0;
    }
    
    /**
     * Register a new user
     * Sprint 5: Throws UserException for validation errors
     */
    public boolean registerUser(String username, String password, String userType) throws UserException {
        // Check if array is full
        if (userCount >= users.length) {
            throw new UserException("User database is full.");
        }
        
        // Validate username is not empty
        if (username == null || username.trim().isEmpty()) {
            throw new UserException("Username cannot be empty.");
        }
        
        // Validate password is not empty
        if (password == null || password.trim().isEmpty()) {
            throw new UserException("Password cannot be empty.");
        }
        
        // Check for duplicate username
        if (userExists(username)) {
            throw new UserException("Username already exists.");
        }
        
        // Create appropriate user type
        User newUser;
        if (userType.equalsIgnoreCase("client")) {
            newUser = new Client(username, password);
        } else if (userType.equalsIgnoreCase("visitor")) {
            newUser = new Visitor(username, password);
        } else {
            throw new UserException("Invalid user type.");
        }
        
        // Add user to array
        users[userCount] = newUser;
        userCount++;
        return true;
    }
    
    /**
     * Authenticate user login
     * Sprint 5: Throws UserException for invalid credentials
     */
    public User login(String username, String password) throws UserException {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            throw new UserException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new UserException("Password cannot be empty.");
        }
        
        // Search for matching user
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username) && 
                users[i].getPassword().equals(password)) {
                return users[i]; // Login successful
            }
        }
        
        // No match found
        throw new UserException("Invalid username or password.");
    }
    
    /**
     * Check if username already exists
     * Used to prevent duplicate usernames
     */
    public boolean userExists(String username) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get all registered users
     * Returns a copy of the active users array
     */
    public User[] getAllUsers() {
        User[] activeUsers = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            activeUsers[i] = users[i];
        }
        return activeUsers;
    }
    
    /**
     * Get total number of registered users
     */
    public int getUserCount() {
        return userCount;
    }
}

/**
 * TaskDAOImpl - Implementation of TaskDAO interface
 * Handles all task-related CRUD operations using arrays
 * Sprint 5: Enhanced with date validation and completion tracking
 */
class TaskDAOImpl implements TaskDAO {
    private Task[] tasks;      // Array to store tasks
    private int taskCount;     // Current number of tasks
    private int nextId;        // Auto-incrementing ID for new tasks
    
    /**
     * Constructor - initializes task storage
     */
    public TaskDAOImpl(int maxSize) {
        tasks = new Task[maxSize];
        taskCount = 0;
        nextId = 1; // Start IDs from 1
    }
    
    /**
     * Add a new task (CREATE operation)
     * Sprint 5: Includes completion date and validates it's not in the past
     */
    public boolean addTask(String taskTitle, String taskText, String assignedTo, LocalDate completionDate) throws TaskException {
        // Check if array is full
        if (taskCount >= tasks.length) {
            throw new TaskException("Task list is full.");
        }
        
        // Validate title
        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            throw new TaskException("Task title cannot be empty.");
        }
        
        // Validate description
        if (taskText == null || taskText.trim().isEmpty()) {
            throw new TaskException("Task description cannot be empty.");
        }
        
        // Validate completion date exists
        if (completionDate == null) {
            throw new TaskException("Completion date cannot be null.");
        }
        
        // Sprint 5: Ensure date is not in the past
        if (completionDate.isBefore(LocalDate.now())) {
            throw new TaskException("Completion date cannot be in the past.");
        }
        
        // Create and add new task
        Task newTask = new Task(nextId, taskTitle, taskText, assignedTo, completionDate);
        tasks[taskCount] = newTask;
        taskCount++;
        nextId++; // Increment ID for next task
        return true;
    }
    
    /**
     * Get all tasks (READ operation)
     * Returns a copy of all active tasks
     */
    public Task[] getAllTasks() {
        Task[] activeTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            activeTasks[i] = tasks[i];
        }
        return activeTasks;
    }
    
    /**
     * Find task by ID (READ operation)
     * Sprint 5: Throws TaskException if not found
     */
    public Task getTaskById(int taskId) throws TaskException {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                return tasks[i];
            }
        }
        throw new TaskException("Task with ID " + taskId + " not found.");
    }
    
    /**
     * Update existing task (UPDATE operation)
     * Sprint 5: Includes completion date parameter
     */
    public boolean updateTask(int taskId, String taskTitle, String taskText, String assignedTo, LocalDate completionDate) throws TaskException {
        // Validate inputs
        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            throw new TaskException("Task title cannot be empty.");
        }
        if (taskText == null || taskText.trim().isEmpty()) {
            throw new TaskException("Task description cannot be empty.");
        }
        if (completionDate == null) {
            throw new TaskException("Completion date cannot be null.");
        }
        
        // Find and update task
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                tasks[i].setTaskTitle(taskTitle);
                tasks[i].setTaskText(taskText);
                tasks[i].setAssignedTo(assignedTo);
                tasks[i].setCompletionDate(completionDate);
                return true;
            }
        }
        throw new TaskException("Task with ID " + taskId + " not found.");
    }
    
    /**
     * Delete task (DELETE operation)
     * Shifts remaining tasks to fill the gap
     */
    public boolean deleteTask(int taskId) throws TaskException {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                // Shift all tasks after deleted task one position left
                for (int j = i; j < taskCount - 1; j++) {
                    tasks[j] = tasks[j + 1];
                }
                tasks[taskCount - 1] = null; // Clear last position
                taskCount--;
                return true;
            }
        }
        throw new TaskException("Task with ID " + taskId + " not found.");
    }
    
    /**
     * Search tasks by keyword (SEARCH operation)
     * Searches in title, description, and assignedTo fields
     */
    public Task[] searchTasks(String keyword) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        // Search through all tasks
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                tasks[i].getTaskText().toLowerCase().contains(keyword.toLowerCase()) ||
                tasks[i].getAssignedTo().toLowerCase().contains(keyword.toLowerCase())) {
                results[resultCount] = tasks[i];
                resultCount++;
            }
        }
        
        // Create array with exact size of results
        Task[] finalResults = new Task[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }
    
    /**
     * Get tasks assigned to a specific user
     * Used by visitors to see their tasks
     */
    public Task[] getTasksByAssignee(String username) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getAssignedTo().equals(username)) {
                results[resultCount] = tasks[i];
                resultCount++;
            }
        }
        
        Task[] finalResults = new Task[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }
    
    /**
     * Sprint 5: Get only completed tasks for a user
     */
    public Task[] getCompletedTasks(String username) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getAssignedTo().equals(username) && tasks[i].isCompleted()) {
                results[resultCount] = tasks[i];
                resultCount++;
            }
        }
        
        Task[] finalResults = new Task[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }
    
    /**
     * Sprint 5: Get only incomplete tasks for a user
     */
    public Task[] getIncompleteTasks(String username) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getAssignedTo().equals(username) && !tasks[i].isCompleted()) {
                results[resultCount] = tasks[i];
                resultCount++;
            }
        }
        
        Task[] finalResults = new Task[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }
    
    /**
     * Sprint 5: Mark task as completed
     * Only the assigned user can mark their own tasks as completed
     */
    public boolean markTaskAsCompleted(int taskId, String username) throws VisitorException, TaskException {
        Task task = null;
        
        // Find the task
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                task = tasks[i];
                break;
            }
        }
        
        // Validate task exists
        if (task == null) {
            throw new TaskException("Task with ID " + taskId + " not found.");
        }
        
        // Ensure user can only mark their own tasks
        if (!task.getAssignedTo().equals(username)) {
            throw new VisitorException("You can only mark tasks assigned to you as completed.");
        }
        
        // Check if already completed
        if (task.isCompleted()) {
            throw new VisitorException("Task is already marked as completed.");
        }
        
        // Mark as completed
        task.setCompleted(true);
        return true;
    }
    
    /**
     * Get total number of tasks
     */
    public int getTaskCount() {
        return taskCount;
    }
    
    /**
     * Sprint 5: Sort tasks by completion date
     */
    public Task[] getTasksSortedByDate(boolean ascending) {
        // Create a copy to avoid modifying original array
        Task[] sortedTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            sortedTasks[i] = tasks[i];
        }
        
        for (int i = 0; i < taskCount - 1; i++) {
            for (int j = 0; j < taskCount - i - 1; j++) {
                boolean shouldSwap;
                if (ascending) {
                    // Ascending: swap if current is after next
                    shouldSwap = sortedTasks[j].getCompletionDate().isAfter(sortedTasks[j + 1].getCompletionDate());
                } else {
                    // Descending: swap if current is before next
                    shouldSwap = sortedTasks[j].getCompletionDate().isBefore(sortedTasks[j + 1].getCompletionDate());
                }
                
                if (shouldSwap) {
                    Task temp = sortedTasks[j];
                    sortedTasks[j] = sortedTasks[j + 1];
                    sortedTasks[j + 1] = temp;
                }
            }
        }
        return sortedTasks;
    }
    
    /**
     * Check for duplicate task titles
     * Uses nested loops to compare all pairs
     */
    public void checkDuplicates() {
        boolean foundDuplicate = false;
        System.out.println("\n========================================");
        System.out.println("      DUPLICATE TASKS CHECK");
        System.out.println("========================================");
        
        // Compare each task with all tasks after it
        for (int i = 0; i < taskCount; i++) {
            for (int j = i + 1; j < taskCount; j++) {
                if (tasks[i].getTaskTitle().equalsIgnoreCase(tasks[j].getTaskTitle())) {
                    System.out.println("Duplicate: Task " + tasks[i].getTaskId() + " and " + tasks[j].getTaskId());
                    foundDuplicate = true;
                }
            }
        }
        
        if (!foundDuplicate) {
            System.out.println("No duplicate tasks found!");
        }
        System.out.println("========================================");
    }
}

// MAIN CLASS - Menu and User Interaction Only

/**
 * TodoManagerSprint5 - Main application class
 * Contains only menu and user interaction logic
 * All business logic is delegated to DAO classes
 */
public class TodoManagerSprint5 {
    static Scanner scanner = new Scanner(System.in);
    static UserDAO userDAO = new UserDAOImpl(100);  // User database
    static TaskDAO taskDAO = new TaskDAOImpl(100);  // Task database
    static User currentUser = null;                  // Currently logged in user
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    /**
     * Main method - Entry point of application
     */
  //I fixed this code from sprint 4 because it wouldnt let the user go back to register or login in loop
    public static void main(String[] args) { 
        System.out.println("========================================");
        System.out.println("  Welcome to Todo Manager - Sprint 5");
        System.out.println("========================================\n");
        
        boolean running = true;
        
        // OUTER LOOP - allows logout and re-login
        while (running) {
            // Authentication loop - continue until user logs in or exits
            while (currentUser == null) {
                showAuthMenu();
                int authChoice = getIntInput("Enter your choice: ");
                System.out.println();
                
                if (authChoice == 1) {
                    register();
                } else if (authChoice == 2) {
                    login();
                } else if (authChoice == 0) {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                } else {
                    System.out.println("Invalid choice!\n");
                }
            }
            
            // Main application loop - show menu based on user type
            int choice = -1;
            while (choice != 0 && currentUser != null) {  
                if (currentUser.getUserType().equals("client")) {
                    displayClientMenu();
                } else {
                    displayVisitorMenu();
                }
                
                choice = getIntInput("Enter your choice: ");
                System.out.println();
                
                handleMenuChoice(choice);
                
                // Pause before showing menu again (only if still logged in)
                if (choice != 0 && currentUser != null) {  
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        }  
        
        scanner.close();
    }
    
    /**
     * Display authentication menu (Register/Login/Exit)
     */
    static void showAuthMenu() {
        System.out.println("========================================");
        System.out.println("       AUTHENTICATION MENU");
        System.out.println("========================================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }
    
    /**
     * Handle user registration
     * Sprint 5: Uses try-catch to handle UserException
     */
    static void register() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            System.out.println("Select user type:");
            System.out.println("1. Client");
            System.out.println("2. Visitor");
            int typeChoice = getIntInput("Enter choice: ");
            
            String userType;
            if (typeChoice == 1) {
                userType = "client";
            } else if (typeChoice == 2) {
                userType = "visitor";
            } else {
                System.out.println("Invalid choice!\n");
                return;
            }
            
            userDAO.registerUser(username, password, userType);
            System.out.println("Registration successful!\n");
        } catch (UserException e) {
            // Sprint 5: Catch and display custom exception
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Handle user login
     * Sprint 5: Uses try-catch to handle UserException
     */
    static void login() {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            currentUser = userDAO.login(username, password);
            System.out.println("Welcome, " + currentUser.getUsername() + "!\n");
        } catch (UserException e) {
            // Sprint 5: Catch and display custom exception
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }
    
    /**
     * Display menu for CLIENT users
     * Clients have full access to all features
     */
    static void displayClientMenu() {
        System.out.println("\n========================================");
        System.out.println("      CLIENT MENU - " + currentUser.getUsername());
        System.out.println("========================================");
        System.out.println("1. Add Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Search Task");
        System.out.println("6. View Tasks Sorted by Date");
        System.out.println("7. Check Duplicates");
        System.out.println("8. View All Users");
        System.out.println("0. Logout");
        System.out.println("========================================");
    }
    
    /**
     * Display menu for VISITOR users
     * Visitors have limited access - only their assigned tasks
     */
    static void displayVisitorMenu() {
        System.out.println("\n========================================");
        System.out.println("     VISITOR MENU - " + currentUser.getUsername());
        System.out.println("========================================");
        System.out.println("1. View My Tasks");
        System.out.println("2. View Tasks Sorted by Date");
        System.out.println("3. Mark Task Completed");
        System.out.println("4. View Completed Tasks");
        System.out.println("5. View Incomplete Tasks");
        System.out.println("0. Logout");
        System.out.println("========================================");
    }
    
    /**
     * Route menu choice to appropriate handler based on user type
     */
    static void handleMenuChoice(int choice) {
        if (currentUser.getUserType().equals("client")) {
            handleClientChoice(choice);
        } else {
            handleVisitorChoice(choice);
        }
    }
    
    /**
     * Handle CLIENT menu choices
     * Sprint 5: Uses try-catch for exception handling
     */
    static void handleClientChoice(int choice) {
        try {
            if (choice == 1) {
                addTask();
            } else if (choice == 2) {
                viewAllTasks();
            } else if (choice == 3) {
                updateTask();
            } else if (choice == 4) {
                deleteTask();
            } else if (choice == 5) {
                searchTask();
            } else if (choice == 6) {
                viewTasksSortedByDate();
            } else if (choice == 7) {
                taskDAO.checkDuplicates();
            } else if (choice == 8) {
                viewAllUsers();
            } else if (choice == 0) {
                // Logout - return to authentication menu
                System.out.println("Logging out...");
                currentUser = null;
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Handle VISITOR menu choices
     * Sprint 5: Uses try-catch for exception handling
     */
    static void handleVisitorChoice(int choice) {
        try {
            if (choice == 1) {
                viewMyTasks();
            } else if (choice == 2) {
                viewMyTasksSortedByDate();
            } else if (choice == 3) {
                markTaskCompleted();
            } else if (choice == 4) {
                viewCompletedTasks();
            } else if (choice == 5) {
                viewIncompleteTasks();
            } else if (choice == 0) {
                // Logout - return to authentication menu
                System.out.println("Logging out...");
                currentUser = null;
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * CLIENT FEATURE: Add a new task
     * Sprint 5: Includes completion date input and validation
     */
    static void addTask() throws ClientException, TaskException {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter task description: ");
        String text = scanner.nextLine();
        
        // Show list of available users to assign task to
        User[] users = userDAO.getAllUsers();
        if (users.length == 0) {
            throw new ClientException("No users available!");
        }
        
        System.out.println("\nAvailable users:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
        
        System.out.print("Assign to username: ");
        String assignedTo = scanner.nextLine();
        
        if (!userDAO.userExists(assignedTo)) {
            throw new ClientException("User does not exist!");
        }
        
        System.out.print("Completion date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        LocalDate completionDate = parseDate(dateStr);
        
        taskDAO.addTask(title, text, assignedTo, completionDate);
        System.out.println("Task added successfully!");
    }
    
    /**
     * VIEW: Display all tasks (Client only)
     */
    static void viewAllTasks() {
        Task[] allTasks = taskDAO.getAllTasks();
        
        if (allTasks.length == 0) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.println("========================================");
        System.out.println("           ALL TASKS");
        System.out.println("========================================");
        for (int i = 0; i < allTasks.length; i++) {
            System.out.println(allTasks[i]);
            if (i < allTasks.length - 1) {
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================");
    }
    
    /**
     * VISITOR FEATURE: View only tasks assigned to current visitor
     */
    static void viewMyTasks() {
        Task[] myTasks = taskDAO.getTasksByAssignee(currentUser.getUsername());
        
        if (myTasks.length == 0) {
            System.out.println("No tasks assigned to you.");
            return;
        }
        
        System.out.println("========================================");
        System.out.println("        MY ASSIGNED TASKS");
        System.out.println("========================================");
        for (int i = 0; i < myTasks.length; i++) {
            System.out.println(myTasks[i]);
            if (i < myTasks.length - 1) {
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================");
    }
    
    /**
     * CLIENT FEATURE: Update an existing task
     * Sprint 5: Includes completion date update
     */
    static void updateTask() throws ClientException, TaskException {
        if (taskDAO.getTaskCount() == 0) {
            throw new ClientException("No tasks to update!");
        }
        
        viewAllTasks();
        System.out.print("\nEnter Task ID: ");
        int taskId = getIntInput("");
        
        Task task = taskDAO.getTaskById(taskId);
        System.out.println("\nCurrent task:");
        System.out.println(task);
        
        System.out.print("\nNew title: ");
        String title = scanner.nextLine();
        
        System.out.print("New description: ");
        String text = scanner.nextLine();
        
        User[] users = userDAO.getAllUsers();
        System.out.println("\nAvailable users:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
        
        System.out.print("Assign to: ");
        String assignedTo = scanner.nextLine();
        
        if (!userDAO.userExists(assignedTo)) {
            throw new ClientException("User does not exist!");
        }
        
        System.out.print("Completion date (DD-MM-YYYY): ");
        String dateStr = scanner.nextLine();
        LocalDate completionDate = parseDate(dateStr);
        
        taskDAO.updateTask(taskId, title, text, assignedTo, completionDate);
        System.out.println("Task updated!");
    }
    
    /**
     * CLIENT FEATURE: Delete a task
     */
    static void deleteTask() throws ClientException, TaskException {
        if (taskDAO.getTaskCount() == 0) {
            throw new ClientException("No tasks to delete!");
        }
        
        viewAllTasks();
        System.out.print("\nEnter Task ID to delete: ");
        int taskId = getIntInput("");
        
        taskDAO.deleteTask(taskId);
        System.out.println("Task deleted successfully!");
    }
    
    /**
     * CLIENT FEATURE: Search tasks by keyword
     */
    static void searchTask() {
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        
        Task[] results = taskDAO.searchTasks(keyword);
        
        System.out.println("\n========================================");
        System.out.println("         SEARCH RESULTS");
        System.out.println("========================================");
        
        if (results.length == 0) {
            System.out.println("No tasks found.");
        } else {
            for (int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
                if (i < results.length - 1) {
                    System.out.println("----------------------------------------");
                }
            }
        }
        System.out.println("========================================");
    }
    
    /**
     * CLIENT FEATURE: View all tasks sorted by completion date
     * Sprint 5 requirement
     */
    static void viewTasksSortedByDate() {
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available.");
            return;
        }
        
        System.out.println("1. Earliest First");
        System.out.println("2. Latest First");
        int sortChoice = getIntInput("Choice: ");
        
        Task[] sortedTasks;
        if (sortChoice == 1) {
            sortedTasks = taskDAO.getTasksSortedByDate(true);
            System.out.println("\n=== TASKS (EARLIEST FIRST) ===");
        } else if (sortChoice == 2) {
            sortedTasks = taskDAO.getTasksSortedByDate(false);
            System.out.println("\n=== TASKS (LATEST FIRST) ===");
        } else {
            System.out.println("Invalid choice!");
            return;
        }
        
        for (int i = 0; i < sortedTasks.length; i++) {
            System.out.println(sortedTasks[i]);
            if (i < sortedTasks.length - 1) {
                System.out.println("----------------------------------------");
            }
        }
    }
    
    /**
     * VISITOR FEATURE: View my tasks sorted by completion date
     * Sprint 5 requirement
     */
    static void viewMyTasksSortedByDate() {
        Task[] myTasks = taskDAO.getTasksByAssignee(currentUser.getUsername());
        
        if (myTasks.length == 0) {
            System.out.println("No tasks assigned.");
            return;
        }
        
        System.out.println("1. Earliest First");
        System.out.println("2. Latest First");
        int sortChoice = getIntInput("Choice: ");
        
        //refactored from past sprint
        for (int i = 0; i < myTasks.length - 1; i++) {
            for (int j = 0; j < myTasks.length - i - 1; j++) {
                boolean shouldSwap;
                if (sortChoice == 1) {
                    shouldSwap = myTasks[j].getCompletionDate().isAfter(myTasks[j + 1].getCompletionDate());
                } else {
                    shouldSwap = myTasks[j].getCompletionDate().isBefore(myTasks[j + 1].getCompletionDate());
                }
                
                if (shouldSwap) {
                    Task temp = myTasks[j];
                    myTasks[j] = myTasks[j + 1];
                    myTasks[j + 1] = temp;
                }
            }
        }
        
        System.out.println("\n=== MY TASKS SORTED ===");
        for (int i = 0; i < myTasks.length; i++) {
            System.out.println(myTasks[i]);
            if (i < myTasks.length - 1) {
                System.out.println("----------------------------------------");
            }
        }
    }
    
    /**
     * VISITOR FEATURE: Mark a task as completed
     * Sprint 5 requirement
     */
    static void markTaskCompleted() throws VisitorException, TaskException {
        Task[] myTasks = taskDAO.getTasksByAssignee(currentUser.getUsername());
        
        if (myTasks.length == 0) {
            throw new VisitorException("No tasks assigned.");
        }
        
        System.out.println("=== YOUR INCOMPLETE TASKS ===");
        boolean hasIncomplete = false;
        for (int i = 0; i < myTasks.length; i++) {
            if (!myTasks[i].isCompleted()) {
                System.out.println(myTasks[i]);
                System.out.println("----------");
                hasIncomplete = true;
            }
        }
        
        if (!hasIncomplete) {
            throw new VisitorException("All tasks completed!");
        }
        
        System.out.print("\nEnter Task ID to mark as completed: ");
        int taskId = getIntInput("");
        
        taskDAO.markTaskAsCompleted(taskId, currentUser.getUsername());
        System.out.println("Task marked as completed!");
    }
    
    /**
     * VISITOR FEATURE: View only completed tasks
     * Sprint 5 requirement
     */
    static void viewCompletedTasks() {
        Task[] completed = taskDAO.getCompletedTasks(currentUser.getUsername());
        
        if (completed.length == 0) {
            System.out.println("No completed tasks.");
            return;
        }
        
        System.out.println("=== COMPLETED TASKS ===");
        for (int i = 0; i < completed.length; i++) {
            System.out.println(completed[i]);
            if (i < completed.length - 1) {
                System.out.println("----------");
            }
        }
    }
    
    /**
     * VISITOR FEATURE: View only incomplete tasks
     * Sprint 5 requirement
     */
    static void viewIncompleteTasks() {
        Task[] incomplete = taskDAO.getIncompleteTasks(currentUser.getUsername());
        
        if (incomplete.length == 0) {
            System.out.println("No incomplete tasks.");
            return;
        }
        
        System.out.println("=== INCOMPLETE TASKS ===");
        for (int i = 0; i < incomplete.length; i++) {
            System.out.println(incomplete[i]);
            if (i < incomplete.length - 1) {
                System.out.println("----------");
            }
        }
    }
    
    /**
     * CLIENT FEATURE: View all registered users
     */
    static void viewAllUsers() {
        User[] users = userDAO.getAllUsers();
        
        if (users.length == 0) {
            System.out.println("No users registered.");
            return;
        }
        
        System.out.println("=== ALL USERS ===");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
    }
    
    /**
     * Helper: Parse date string to LocalDate
     * Sprint 5: Throws TaskException for invalid format
     */
    static LocalDate parseDate(String dateStr) throws TaskException {
        try {
            return LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new TaskException("Invalid date format. Use DD-MM-YYYY.");
        }
    }
    
    /**
     * Helper: Get integer input with validation
     * Prevents crashes from non-numeric input
     */
    static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid! Enter a number: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }
}