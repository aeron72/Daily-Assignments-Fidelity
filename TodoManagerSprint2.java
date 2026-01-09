import java.util.Scanner;

public class TodoManagerSprint2 {
    // Array to store tasks (max 100 tasks)
    static String[] tasks = new String[100];
    static int taskCount = 0;
    static Scanner scanner = new Scanner(System.in); //for input
    
    public static void main(String[] args) {
        String userName = "Aeron Flores";
        System.out.println("========================================"); //I reused the part 1 code
        System.out.println("  Welcome to Todo Manager - Sprint 2");
        System.out.println("  User: " + userName);
        System.out.println("========================================\n");
        
        int choice = -1; 
        
        // Menu loop - exits only when user enters 0
        while (choice != 0) { //while loop
            displayMenu();
            choice = getIntInput("Enter your choice: "); //getIntInput is a helper method I use so that there are no errors in int input
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
                checkDuplicates();
            } else if (choice == 0) {
                System.out.println("Thank you for using Todo Manager. Goodbye!"); //exit
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
            
            if (choice != 0) {
                System.out.println("\nPress Enter to continue..."); //just in case there is mistype
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
        System.out.println("4. Delete Task"); //all the numbers correspond to the options/choices
        System.out.println("5. Search Task");
        System.out.println("6. View Tasks Sorted (A-Z / Z-A)");
        System.out.println("7. Check for Duplicate Tasks");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }
    
    // Add a new task to the array
    static void addTask() {
        if (taskCount >= tasks.length) {
            System.out.println("Task list is full! Cannot add more tasks."); //max amount 
            return;
        }
        
        System.out.print("Enter task description: ");
        String task = scanner.nextLine();
        
        if (task.trim().isEmpty()) {
            System.out.println("Task cannot be empty!"); //no input error check
            return;
        }
        
        tasks[taskCount] = task; //add to tasks
        taskCount++;
        System.out.println("Task added successfully! Total tasks: " + taskCount); 
    }
    
    // View all tasks
    static void viewAllTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks available. Add some tasks first!"); 
            return;
        }
        
        System.out.println("========================================");
        System.out.println("           ALL TASKS");
        System.out.println("========================================");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]); //print all tasks
        }
        System.out.println("========================================");
        System.out.println("Total tasks: " + taskCount);
    }
    
    // Update an existing task
    static void updateTask() {
        if (taskCount == 0) {
            System.out.println("No tasks available to update!");
            return;
        }
        
        viewAllTasks();//used previous method to show tasks available
        System.out.print("\nEnter task number to update (1-" + taskCount + "): "); //however much tasks there are shows
        int taskNum = getIntInput("");
        
        if (taskNum < 1 || taskNum > taskCount) {
            System.out.println("Invalid task number!");
            return;
        }
        
        System.out.println("Current task: " + tasks[taskNum - 1]);
        System.out.print("Enter new task description: "); 
        String newTask = scanner.nextLine();
        
        if (newTask.trim().isEmpty()) {
            System.out.println("Task cannot be empty!");
            return;
        }
        
        tasks[taskNum - 1] = newTask; //update that task by replacing
        System.out.println("Task updated successfully!");
    }
    
    // Delete a task from the array
    static void deleteTask() {
        if (taskCount == 0) {
            System.out.println("No tasks available to delete!");
            return;
        }
        
        viewAllTasks();
        System.out.print("\nEnter task number to delete (1-" + taskCount + "): ");
        int taskNum = getIntInput("");
        
        if (taskNum < 1 || taskNum > taskCount) {
            System.out.println("Invalid task number!");
            return;
        }
        
        String deletedTask = tasks[taskNum - 1];
        
        // Shift all tasks after the deleted task one position left
        for (int i = taskNum - 1; i < taskCount - 1; i++) { 
            tasks[i] = tasks[i + 1]; 
        }
        
        tasks[taskCount - 1] = null;
        taskCount--;
        
        System.out.println("Task deleted successfully: \"" + deletedTask + "\"");
        System.out.println("Total tasks: " + taskCount);
    }
    
    // Search for a task
    static void searchTask() {
        if (taskCount == 0) {
            System.out.println("No tasks available to search!"); 
            return;
        }
        
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().toLowerCase(); //to avoid syntax mistakes and easier to find
        
        if (keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty!");
            return;
        }
        
        boolean found = false;
        System.out.println("\n========================================");
        System.out.println("         SEARCH RESULTS");
        System.out.println("========================================");
        
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i].toLowerCase().contains(keyword)) {
                System.out.println((i + 1) + ". " + tasks[i]); //print tasks
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No tasks found matching \"" + keyword + "\"");
        }
        System.out.println("========================================");
    }
    
    // View tasks in sorted order
    static void viewTasksSorted() {
        if (taskCount == 0) {
            System.out.println("No tasks available to sort!");
            return;
        }
        
        System.out.println("1. Sort A-Z (Ascending)");
        System.out.println("2. Sort Z-A (Descending)");
        int sortChoice = getIntInput("Enter your choice: "); //descend or ascend
        
        // Create a copy of tasks array to sort
        String[] sortedTasks = new String[taskCount];
        for (int i = 0; i < taskCount; i++) {
            sortedTasks[i] = tasks[i];
        }
        
        // Bubble sort using if-else
        for (int i = 0; i < taskCount - 1; i++) {
            for (int j = 0; j < taskCount - i - 1; j++) { //used logic from sprint 1 but for a bigger array to work I used loop
                if (sortChoice == 1) {
                    // Ascending order
                    if (sortedTasks[j].compareToIgnoreCase(sortedTasks[j + 1]) > 0) { 
                        String temp = sortedTasks[j];
                        sortedTasks[j] = sortedTasks[j + 1];
                        sortedTasks[j + 1] = temp;
                    }
                } else if (sortChoice == 2) {
                    // Descending order
                    if (sortedTasks[j].compareToIgnoreCase(sortedTasks[j + 1]) < 0) {
                        String temp = sortedTasks[j];
                        sortedTasks[j] = sortedTasks[j + 1];
                        sortedTasks[j + 1] = temp;
                    }
                }
            }
        }
        
        System.out.println("\n========================================");
        if (sortChoice == 1) {
            System.out.println("      TASKS SORTED (A-Z)");
        } else if (sortChoice == 2) {
            System.out.println("      TASKS SORTED (Z-A)");
        }
        System.out.println("========================================");
        
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + sortedTasks[i]); //print tasks sorted
        }
        System.out.println("========================================");
    }
    
    // Check for duplicate tasks
    static void checkDuplicates() {
        if (taskCount == 0) {
            System.out.println("No tasks available to check!");
            return;
        }
        
        boolean foundDuplicate = false;
        System.out.println("\n========================================");
        System.out.println("      DUPLICATE TASKS CHECK");
        System.out.println("========================================");
        
        for (int i = 0; i < taskCount; i++) {
            for (int j = i + 1; j < taskCount; j++) {
                if (tasks[i].equalsIgnoreCase(tasks[j])) { //always ignore case so it can match 
                    System.out.println("Duplicate found at positions " + (i + 1) + " and " + (j + 1) + ": \"" + tasks[i] + "\"");
                    foundDuplicate = true;
                }
            }
        }
        
        if (!foundDuplicate) {
            System.out.println("No duplicate tasks found!");
        }
        System.out.println("========================================");
    }
    
    // Helper method to get integer input with error handling
    static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) { //hasNextInt checks if input is an integer not anything else
            scanner.nextLine();
            System.out.print("Invalid input! Please enter a number: "); 
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }
}