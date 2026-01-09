public class TodoManagerSprint1 {
    public static void main(String[] args) {
        // User Story 1: Print user name on screen
        String userName = "Aeron Flores";
        System.out.println("Welcome to Todo Manager!");
        System.out.println("User: " + userName);
        System.out.println("================================");
        
        // User Story 2: List at least 5 tasks for the day
        String task1 = "Complete Java assignment";
        String task2 = "Attend team meeting";
        String task3 = "Review code changes"; //turn them into  variables for an array
        String task4 = "Complete Java assignment"; //this will be caught as repeated task later
        String task5 = "Update project documentation";
        
        System.out.println("\nYour Tasks for Today:");
        System.out.println("1. " + task1);
        System.out.println("2. " + task2);
        System.out.println("3. " + task3);
        System.out.println("4. " + task4);
        System.out.println("5. " + task5);
        
        // User Story 3: Display tasks in increasing order (alphabetically)
        System.out.println("\n================================");
        System.out.println("Tasks in Increasing Order (A-Z):");
        System.out.println("================================");
        
        String temp;
        String[] tasks = {task1, task2, task3, task4, task5}; //Using array
        
        // Sort in ascending order
        if (tasks[0].compareTo(tasks[1]) > 0) { //using if else to compare the 5 tasks and sort
            temp = tasks[0];
            tasks[0] = tasks[1];
            tasks[1] = temp;
        }
        if (tasks[1].compareTo(tasks[2]) > 0) {
            temp = tasks[1];
            tasks[1] = tasks[2];
            tasks[2] = temp;
        }
        if (tasks[2].compareTo(tasks[3]) > 0) {
            temp = tasks[2];
            tasks[2] = tasks[3];
            tasks[3] = temp;
        }
        if (tasks[3].compareTo(tasks[4]) > 0) {
            temp = tasks[3];
            tasks[3] = tasks[4];
            tasks[4] = temp;
        }
        // Second pass
        if (tasks[0].compareTo(tasks[1]) > 0) {
            temp = tasks[0];
            tasks[0] = tasks[1];
            tasks[1] = temp;
        }
        if (tasks[1].compareTo(tasks[2]) > 0) {
            temp = tasks[1];
            tasks[1] = tasks[2];
            tasks[2] = temp;
        }
        if (tasks[2].compareTo(tasks[3]) > 0) {
            temp = tasks[2];
            tasks[2] = tasks[3];
            tasks[3] = temp;
        }
        // Third pass
        if (tasks[0].compareTo(tasks[1]) > 0) {
            temp = tasks[0];
            tasks[0] = tasks[1];
            tasks[1] = temp;
        }
        if (tasks[1].compareTo(tasks[2]) > 0) {
            temp = tasks[1];
            tasks[1] = tasks[2];
            tasks[2] = temp;
        }
        // Fourth pass
        if (tasks[0].compareTo(tasks[1]) > 0) {
            temp = tasks[0];
            tasks[0] = tasks[1];
            tasks[1] = temp;
        }
        
        // Print sorted tasks (ascending)
        System.out.println("1. " + tasks[0]); //sorted
        System.out.println("2. " + tasks[1]);
        System.out.println("3. " + tasks[2]);
        System.out.println("4. " + tasks[3]);
        System.out.println("5. " + tasks[4]);
        
        // Display tasks in decreasing order (Z-A)
        System.out.println("\n================================");
        System.out.println("Tasks in Decreasing Order (Z-A):");
        System.out.println("================================");
        System.out.println("1. " + tasks[4]);
        System.out.println("2. " + tasks[3]); //reversed the order
        System.out.println("3. " + tasks[2]);
        System.out.println("4. " + tasks[1]);
        System.out.println("5. " + tasks[0]);
        
        // User Story 4: Check for repeated tasks
        System.out.println("\n================================");
        System.out.println("Checking for Repeated Tasks:");
        System.out.println("================================");
        
        boolean foundDuplicate = false; //Use boolean to check 
        
        if (task1.equals(task2)) {
            System.out.println("Duplicate found: Task 1 and Task 2 are the same - \"" + task1 + "\"");
            foundDuplicate = true;
        }
        if (task1.equals(task3)) {
            System.out.println("Duplicate found: Task 1 and Task 3 are the same - \"" + task1 + "\"");
            foundDuplicate = true;
        }
        if (task1.equals(task4)) {
            System.out.println("Duplicate found: Task 1 and Task 4 are the same - \"" + task1 + "\"");
            foundDuplicate = true;
        }
        if (task1.equals(task5)) {
            System.out.println("Duplicate found: Task 1 and Task 5 are the same - \"" + task1 + "\"");
            foundDuplicate = true;
        }
        if (task2.equals(task3)) {
            System.out.println("Duplicate found: Task 2 and Task 3 are the same - \"" + task2 + "\"");
            foundDuplicate = true;
        }
        if (task2.equals(task4)) {
            System.out.println("Duplicate found: Task 2 and Task 4 are the same - \"" + task2 + "\"");
            foundDuplicate = true;
        }
        if (task2.equals(task5)) {
            System.out.println("Duplicate found: Task 2 and Task 5 are the same - \"" + task2 + "\"");
            foundDuplicate = true;
        }
        if (task3.equals(task4)) {
            System.out.println("Duplicate found: Task 3 and Task 4 are the same - \"" + task3 + "\"");
            foundDuplicate = true;
        }
        if (task3.equals(task5)) {
            System.out.println("Duplicate found: Task 3 and Task 5 are the same - \"" + task3 + "\"");
            foundDuplicate = true;
        }
        if (task4.equals(task5)) {
            System.out.println("Duplicate found: Task 4 and Task 5 are the same - \"" + task4 + "\"");
            foundDuplicate = true;
        }
        
        if (!foundDuplicate) {
            System.out.println("No duplicate tasks found.");
        } else {
            System.out.println("\nNote: You have duplicate tasks in your list!");
        }
        
        System.out.println("\n================================");
        System.out.println("Todo Manager Sprint 1 Complete!");
        System.out.println("================================");
    }
}