import java.util.Scanner;

// Task POJO (Plain Old Java Object)
class Task {
    private int taskId;
    private String taskTitle;
    private String taskText;
    private String assignedTo;
    
    // Constructor
    public Task(int taskId, String taskTitle, String taskText, String assignedTo) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskText = taskText;
        this.assignedTo = assignedTo;
    }
    
    // Getters
    public int getTaskId() {
        return taskId;
    }
    
    public String getTaskTitle() { //There are no complex methods or classes and everything is plain
        return taskTitle;
    }
    
    public String getTaskText() {
        return taskText;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    // Setters
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

// DAO (Data Access Object) class for Task operations. Using CRUD
class TaskDAO {
    private Task[] tasks;
    private int taskCount;
    private int nextId;
    
    // Constructor
    public TaskDAO(int maxSize) {
        tasks = new Task[maxSize]; 
        taskCount = 0;
        nextId = 1;
    }
    
    // Create - Add a new task
    public boolean addTask(String taskTitle, String taskText, String assignedTo) {
        if (taskCount >= tasks.length) {
            return false;
        }
        
        Task newTask = new Task(nextId, taskTitle, taskText, assignedTo); //initialize object
        tasks[taskCount] = newTask;
        taskCount++;
        nextId++;
        return true;
    }
    
    // Read - Get all tasks
    public Task[] getAllTasks() {
        Task[] activeTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            activeTasks[i] = tasks[i];
        }
        return activeTasks;
    }
    
    // Read - Get task by ID
    public Task getTaskById(int taskId) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                return tasks[i];
            }
        }
        return null;
    }
    
    // Update - Update a task
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
    
    // Delete - Delete a task
    public boolean deleteTask(int taskId) {
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskId() == taskId) {
                // Shift all tasks after the deleted task
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
    
    // Search - Search tasks by keyword
    public Task[] searchTasks(String keyword) {
        Task[] results = new Task[taskCount];
        int resultCount = 0;
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].getTaskTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                tasks[i].getTaskText().toLowerCase().contains(keyword.toLowerCase()) || //convert all to lowercase
                tasks[i].getAssignedTo().toLowerCase().contains(keyword.toLowerCase())) {
                results[resultCount] = tasks[i];
                resultCount++;
            }
        }
        
        // Create array with only the results
        Task[] finalResults = new Task[resultCount];
        for (int i = 0; i < resultCount; i++) {
            finalResults[i] = results[i];
        }
        return finalResults;
    }
    
    // Get task count
    public int getTaskCount() {
        return taskCount;
    }
    
    // Sort tasks by title
    public Task[] getTasksSorted(boolean ascending) {
        Task[] sortedTasks = new Task[taskCount];
        for (int i = 0; i < taskCount; i++) {
            sortedTasks[i] = tasks[i];
        }
        
        // Bubble sort
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
    
    // Check for duplicate tasks
    public void checkDuplicates() {
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

// Main class with menu only
public class TodoManagerSprint3 {
    static Scanner scanner = new Scanner(System.in);
    static TaskDAO taskDAO = new TaskDAO(100);
    
    public static void main(String[] args) {
        String userName = "John Doe";
        System.out.println("========================================");
        System.out.println("  Welcome to Todo Manager - Sprint 3");
        System.out.println("  User: " + userName);
        System.out.println("========================================\n");
        
        int choice = -1;
        
        // Menu loop - exits only when user enters 0
        while (choice != 0) {
            displayMenu();
            choice = getIntInput("Enter your choice: ");
            System.out.println();
            
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
            } else if (choice == 0) {
                System.out.println("Thank you for using Todo Manager. Goodbye!");
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
            
            if (choice != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("           TODO MANAGER MENU");
        System.out.println("========================================");
        System.out.println("1. Add Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Search Task"); //shows all options to choose
        System.out.println("6. View Tasks Sorted (A-Z / Z-A)");
        System.out.println("7. Check for Duplicate Tasks");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }
    
    static void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter task description: "); //added new title, description, and assign
        String text = scanner.nextLine();
        
        System.out.print("Assign task to: ");
        String assignedTo = scanner.nextLine();
        
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
    
    static void viewAllTasks() {
        Task[] allTasks = taskDAO.getAllTasks();
        
        if (allTasks.length == 0) {
            System.out.println("No tasks available. Add some tasks first!");
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
    
    static void updateTask() {
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
        
        System.out.print("Assign task to: ");
        String assignedTo = scanner.nextLine();
        
        if (title.trim().isEmpty() || text.trim().isEmpty()) {
            System.out.println("Title and description cannot be empty!"); //error check
            return;
        }
        
        if (taskDAO.updateTask(taskId, title, text, assignedTo)) {
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Failed to update task!");
        }
    }
    
    static void deleteTask() {
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to delete!");
            return;
        }
        
        viewAllTasks();
        System.out.print("\nEnter Task ID to delete: "); //refactored from sprint2 but uses DAO class
        int taskId = getIntInput("");
        
        Task task = taskDAO.getTaskById(taskId); //DAO read
        if (task == null) {
            System.out.println("Task not found!");
            return;
        }
        
        if (taskDAO.deleteTask(taskId)) { //DAO delete
            System.out.println("Task deleted successfully!");
            System.out.println("Total tasks: " + taskDAO.getTaskCount()); 
        } else {
            System.out.println("Failed to delete task!");
        }
    }
    
    static void searchTask() {
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to search!"); //refactored from sprint 2
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
    
    static void viewTasksSorted() {
        if (taskDAO.getTaskCount() == 0) {
            System.out.println("No tasks available to sort!"); //refactored from sprint 2
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
    
    static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Invalid input! Please enter a number: "); //int error checker
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }
}