import java.util.Scanner;

// ============================================
// USER CLASSES (Parent and Child)
// ============================================

// Parent User class
class User {
    private String username;
    private String password;
    private String userType; // "client" or "visitor"
    
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getUserType() { //refactored from sprint 3
        return userType; 
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

// Client class (extends User)
class Client extends User {
    public Client(String username, String password) {
        super(username, password, "client");
    }
}

// Visitor class (extends User)
class Visitor extends User {
    public Visitor(String username, String password) {
        super(username, password, "visitor");
    }
}

// ============================================
// TASK CLASS
// ============================================

class Task {
    private int taskId;
    private String taskTitle;
    private String taskText;
    private String assignedTo;
    
    public Task(int taskId, String taskTitle, String taskText, String assignedTo) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskText = taskText;
        this.assignedTo = assignedTo;
    }
    
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
    
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    
    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    @Override
    public String toString() {
        return "Task ID: " + taskId + "\n" +
               "Title: " + taskTitle + "\n" +
               "Description: " + taskText + "\n" +
               "Assigned To: " + assignedTo;
    }
}

// ============================================
// DAO INTERFACES
// ============================================

interface UserDAO {
    boolean registerUser(String username, String password, String userType);
    User login(String username, String password);
    boolean userExists(String username);
    User[] getAllUsers();
    int getUserCount();
}

interface TaskDAO {
    boolean addTask(String taskTitle, String taskText, String assignedTo);
    Task[] getAllTasks();
    Task getTaskById(int taskId);
    boolean updateTask(int taskId, String taskTitle, String taskText, String assignedTo);
    boolean deleteTask(int taskId);
    Task[] searchTasks(String keyword);
    Task[] getTasksByAssignee(String username);
    int getTaskCount();
    Task[] getTasksSorted(boolean ascending);
    void checkDuplicates();
}

// ============================================
// DAO IMPLEMENTATIONS
// ============================================

class UserDAOImpl implements UserDAO { //children from interface
    private User[] users;
    private int userCount;
    
    public UserDAOImpl(int maxSize) {
        users = new User[maxSize];
        userCount = 0;
    }
    
    @Override
    public boolean registerUser(String username, String password, String userType) {
        if (userCount >= users.length) {
            return false;
        }
        
        // Check if username already exists
        if (userExists(username)) {
            return false;
        }
        
        User newUser;
        if (userType.equalsIgnoreCase("client")) {
            newUser = new Client(username, password);
        } else if (userType.equalsIgnoreCase("visitor")) { //makes new user
            newUser = new Visitor(username, password);
        } else {
            return false;
        }
        
        users[userCount] = newUser;
        userCount++;
        return true;
    }
    
    @Override
    public User login(String username, String password) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username) && 
                users[i].getPassword().equals(password)) { //checks for equal matching
                return users[i];
            }
        }
        return null;
    }
    
    @Override
    public boolean userExists(String username) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public User[] getAllUsers() {
        User[] activeUsers = new User[userCount];
        for (int i = 0; i < userCount; i++) {
            activeUsers[i] = users[i];
        }
        return activeUsers;
    }
    
    @Override
    public int getUserCount() {
        return userCount;
    }
}

class TaskDAOImpl implements TaskDAO {
    private Task[] tasks;
    private int taskCount;
    private int nextId;
    
    public TaskDAOImpl(int maxSize) {
        tasks = new Task[maxSize];
        taskCount = 0;
        nextId = 1;
    }
    
    @Override
    public boolean addTask(String taskTitle, String taskText, String assignedTo) {
        if (taskCount >= tasks.length) {
            return false;
        }
        
        Task newTask = new Task(nextId, taskTitle, taskText, assignedTo);
        tasks[taskCount] = newTask;
        taskCount++;
        nextId++;
        return true;
    }
    
    @Override
    public Task[] getAllTasks() {
        Task[] activeTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            activeTasks[i] = tasks[i];
        }
        return activeTasks;
    }
    
    @Override
    public Task getTaskById(int taskId) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                return tasks[i];
            }
        }
        return null;
    }
    
    @Override
    public boolean updateTask(int taskId, String taskTitle, String taskText, String assignedTo) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                tasks[i].setTaskTitle(taskTitle);
                tasks[i].setTaskText(taskText);
                tasks[i].setAssignedTo(assignedTo);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean deleteTask(int taskId) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                for (int j = i; j < taskCount - 1; j++) {
                    tasks[j] = tasks[j + 1];
                }
                tasks[taskCount - 1] = null;
                taskCount--;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Task[] searchTasks(String keyword) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                tasks[i].getTaskText().toLowerCase().contains(keyword.toLowerCase()) ||
                tasks[i].getAssignedTo().toLowerCase().contains(keyword.toLowerCase())) {
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
    
    @Override
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
    
    @Override
    public int getTaskCount() {
        return taskCount;
    }
    
    @Override
    public Task[] getTasksSorted(boolean ascending) { 
        Task[] sortedTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            sortedTasks[i] = tasks[i];
        }
        
        for (int i = 0; i < taskCount - 1; i++) {
            for (int j = 0; j < taskCount - i - 1; j++) {
                boolean shouldSwap;
                if (ascending) {
                    shouldSwap = sortedTasks[j].getTaskTitle().compareToIgnoreCase(sortedTasks[j + 1].getTaskTitle()) > 0;
                } else {
                    shouldSwap = sortedTasks[j].getTaskTitle().compareToIgnoreCase(sortedTasks[j + 1].getTaskTitle()) < 0;
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
    
    @Override
    public void checkDuplicates() { //refactored
        boolean foundDuplicate = false;
        System.out.println("\n========================================");
        System.out.println("      DUPLICATE TASKS CHECK");
        System.out.println("========================================");
        
        for (int i = 0; i < taskCount; i++) {
            for (int j = i + 1; j < taskCount; j++) {
                if (tasks[i].getTaskTitle().equalsIgnoreCase(tasks[j].getTaskTitle())) {
                    System.out.println("Duplicate found:");
                    System.out.println("  Task ID " + tasks[i].getTaskId() + " and Task ID " + tasks[j].getTaskId());
                    System.out.println("  Title: \"" + tasks[i].getTaskTitle() + "\"");
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

// ============================================
// MAIN CLASS (Menu Only)
// ============================================

public class TodoManagerSprint4 {
    static Scanner scanner = new Scanner(System.in);
    static UserDAO userDAO = new UserDAOImpl(100);
    static TaskDAO taskDAO = new TaskDAOImpl(100);
    static User currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Welcome to Todo Manager - Sprint 4");
        System.out.println("========================================\n");
        
        // Authentication loop
        while (currentUser == null) {
            showAuthMenu();
            int authChoice = getIntInput("Enter your choice: ");
            System.out.println();
            
            if (authChoice == 1) {
                register();
            } else if (authChoice == 2) {
                login();
            } else if (authChoice == 0) {
                System.out.println("Thank you for using Todo Manager. Goodbye!");
                scanner.close();
                return;
            } else {
                System.out.println("Invalid choice! Please try again.\n");
            }
        }
        
        // Main application loop
        int choice = -1;
        while (choice != 0) {
            if (currentUser.getUserType().equals("client")) {
                displayClientMenu();
            } else {
                displayVisitorMenu();
            }
            
            choice = getIntInput("Enter your choice: ");
            System.out.println();
            
            handleMenuChoice(choice);
            
            if (choice != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    static void showAuthMenu() {
        System.out.println("========================================");
        System.out.println("       AUTHENTICATION MENU");
        System.out.println("========================================"); //options for user
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }
    
    static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        if (userDAO.userExists(username)) {
            System.out.println("Username already exists! Please choose another.\n");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.println("Select user type:");
        System.out.println("1. Client (Full access)");
        System.out.println("2. Visitor (View assigned tasks only)");
        int typeChoice = getIntInput("Enter choice: ");
        
        String userType;
        if (typeChoice == 1) {
            userType = "client";
        } else if (typeChoice == 2) { //asigns type
            userType = "visitor";
        } else {
            System.out.println("Invalid choice!\n");
            return;
        }
        
        if (userDAO.registerUser(username, password, userType)) {
            System.out.println("Registration successful! Please login.\n");
        } else {
            System.out.println("Registration failed!\n");
        }
    }
    
    static void login() { //For both client and visitor to enter their account
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        User user = userDAO.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
            System.out.println("User type: " + currentUser.getUserType().toUpperCase() + "\n");
        } else {
            System.out.println("Invalid username or password!\n");
        }
    }
    
    static void displayClientMenu() {
        System.out.println("\n========================================");
        System.out.println("      CLIENT MENU - " + currentUser.getUsername());
        System.out.println("========================================");
        System.out.println("1. Add Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Search Task"); //from sprint 3
        System.out.println("6. View Tasks Sorted (A-Z / Z-A)");
        System.out.println("7. Check for Duplicate Tasks");
        System.out.println("8. View All Users");
        System.out.println("0. Logout");
        System.out.println("========================================");
    }
    
    static void displayVisitorMenu() { //options for visitor
        System.out.println("\n========================================");
        System.out.println("     VISITOR MENU - " + currentUser.getUsername());
        System.out.println("========================================");
        System.out.println("1. View My Assigned Tasks");
        System.out.println("0. Logout");
        System.out.println("========================================");
    }
    
    static void handleMenuChoice(int choice) {
        if (currentUser.getUserType().equals("client")) { //visitor or client
            handleClientChoice(choice);
        } else {
            handleVisitorChoice(choice);
        }
    }
    
    static void handleClientChoice(int choice) { //client options
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
            viewTasksSorted();
        } else if (choice == 7) {
            taskDAO.checkDuplicates();
        } else if (choice == 8) {
            viewAllUsers();
        } else if (choice == 0) {
            System.out.println("Logging out... Goodbye, " + currentUser.getUsername() + "!");
            currentUser = null;
            System.exit(0);
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }
    
    static void handleVisitorChoice(int choice) { //log out or view tasks for visitor
        if (choice == 1) {
            viewMyTasks();
        } else if (choice == 0) {
            System.out.println("Logging out... Goodbye, " + currentUser.getUsername() + "!");
            currentUser = null;
            System.exit(0);
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }
    
    static void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter task description: ");
        String text = scanner.nextLine();
        
        // Show available users to assign
        User[] users = userDAO.getAllUsers();
        if (users.length == 0) {
            System.out.println("No users available to assign tasks!");
            return;
        }
        
        System.out.println("\nAvailable users:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
        
        System.out.print("Enter username to assign task to: ");
        String assignedTo = scanner.nextLine();
        
        if (!userDAO.userExists(assignedTo)) {
            System.out.println("User does not exist! Task not created.");
            return;
        }
        
        if (title.trim().isEmpty() || text.trim().isEmpty()) {
            System.out.println("Title and description cannot be empty!");
            return;
        }
        
        if (taskDAO.addTask(title, text, assignedTo)) {
            System.out.println("Task added successfully! Total tasks: " + taskDAO.getTaskCount());
        } else {
            System.out.println("Task list is full! Cannot add more tasks.");
        }
    }
    
    static void viewAllTasks() { //For client to see
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
        System.out.println("Total tasks: " + allTasks.length);
    }
    
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
        System.out.println("Total assigned tasks: " + myTasks.length);
    }
    
    static void updateTask() { //refactored with taskDAO
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to update!");
            return;
        }
        
        viewAllTasks();
        System.out.print("\nEnter Task ID to update: ");
        int taskId = getIntInput("");
        
        Task task = taskDAO.getTaskById(taskId);
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }
        
        System.out.println("\nCurrent task details:");
        System.out.println(task);
        
        System.out.print("\nEnter new task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter new task description: ");
        String text = scanner.nextLine();
        
        User[] users = userDAO.getAllUsers();
        System.out.println("\nAvailable users:");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
        
        System.out.print("Assign task to: ");
        String assignedTo = scanner.nextLine();
        
        if (!userDAO.userExists(assignedTo)) {
            System.out.println("User does not exist! Task not updated.");
            return;
        }
        
        if (title.trim().isEmpty() || text.trim().isEmpty()) {
            System.out.println("Title and description cannot be empty!");
            return;
        }
        
        if (taskDAO.updateTask(taskId, title, text, assignedTo)) {
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Failed to update task!");
        }
    }
    
    static void deleteTask() { //refactored with taskDAO
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to delete!");
            return;
        }
        
        viewAllTasks();
        System.out.print("\nEnter Task ID to delete: ");
        int taskId = getIntInput("");
        
        Task task = taskDAO.getTaskById(taskId);
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }
        
        if (taskDAO.deleteTask(taskId)) {
            System.out.println("Task deleted successfully!");
            System.out.println("Total tasks: " + taskDAO.getTaskCount());
        } else {
            System.out.println("Failed to delete task!");
        }
    }
    
    static void searchTask() { //refactored with taskDAO
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to search!");
            return;
        }
        
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();
        
        if (keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty!");
            return;
        }
        
        Task[] results = taskDAO.searchTasks(keyword);
        
        System.out.println("\n========================================");
        System.out.println("         SEARCH RESULTS");
        System.out.println("========================================");
        
        if (results.length == 0) {
            System.out.println("No tasks found matching \"" + keyword + "\"");
        } else {
            for (int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
                if (i < results.length - 1) {
                    System.out.println("----------------------------------------");
                }
            }
            System.out.println("========================================");
            System.out.println("Found " + results.length + " task(s)");
        }
    }
    
    static void viewTasksSorted() { //refactored from sprint 3 sort ascend descend
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to sort!");
            return;
        }
        
        System.out.println("1. Sort A-Z (Ascending)");
        System.out.println("2. Sort Z-A (Descending)");
        int sortChoice = getIntInput("Enter your choice: ");
        
        Task[] sortedTasks;
        if (sortChoice == 1) {
            sortedTasks = taskDAO.getTasksSorted(true);
            System.out.println("\n========================================");
            System.out.println("      TASKS SORTED (A-Z)");
            System.out.println("========================================");
        } else if (sortChoice == 2) {
            sortedTasks = taskDAO.getTasksSorted(false);
            System.out.println("\n========================================");
            System.out.println("      TASKS SORTED (Z-A)");
            System.out.println("========================================");
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
        System.out.println("========================================");
    }
    
    static void viewAllUsers() { //refactored 
        User[] users = userDAO.getAllUsers();
        
        if (users.length == 0) {
            System.out.println("No users registered.");
            return;
        }
        
        System.out.println("========================================");
        System.out.println("         ALL REGISTERED USERS");
        System.out.println("========================================");
        for (int i = 0; i < users.length; i++) {
            System.out.println((i + 1) + ". " + users[i].getUsername() + " (" + users[i].getUserType() + ")");
        }
        System.out.println("========================================");
        System.out.println("Total users: " + users.length);
    }
    
    static int getIntInput(String prompt) { //refactored from sprint 3
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid input! Please enter a number: "); //help with getting only int
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }
}