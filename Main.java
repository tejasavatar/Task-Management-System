import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.util.List;
// Data Structure
import java.util.HashSet;
// Data Structure
import java.util.HashMap;
// Data Structure
import java.util.Set;
// Data Structures
import java.util.Map;
import java.util.PriorityQueue;
// Data Structure
import java.util.ArrayList;
// Data Structure
import java.util.Comparator;
import java.util.Collections;
import java.util.Stack;
//Data Structure

//Import all the necessary imports that may be needed
// In this project, the major data structures we will be using are Stacks, Queues, ArrayLists, HashMaps, HashSets & Interfaces
interface Task {
    // Using this interface, we can get the name using strings and the date using integers
    // We also use lists that use strings
    String getName();
    String getProject();
    Date getDeadline();
    List<String> getPeople();
    // Lists gather people's names that are entered by the user
    List<String> getJobs();
    // Lists gather the job names that are entered by the user
    Stack<String> getJobStack();
    // Using a stack, which may not be useful, utilizes the same jobs and saves them
    // Stacks just used for repetition
}

class SimpleTask implements Task {
    // This class needs to implement the last interface
    // We need to set our private or public variables due to the fact that  it can or cannot be accessed and change after code runs
    private String name; // String used to hold the name of people that are working on the project
    private String project; // String to hold type of project that needs to be saved
    private Date deadline; // The date to hold the variables
    private List<String> people; // List to hold the strings of people that are saved into array
    private List<String> jobs; // List to hold jobs
    private Stack<String> jobStack; // Stack to hold jobs (repetitive)


    public SimpleTask(String name, String project, Date deadline, List<String> people, List<String> jobs) {
        // We make our constructor for our instance variables
        // Now we would have to initialize our variables
        this.name = name; // Assigning name parameter to the name instance variable
        this.project = project; // Assigning project parameter to the project instance variable
        this.deadline = deadline; // Assigned deadline parameter to deadline instance variable
        this.people = people; // Assigned people parameter to deadline instance variable
        this.jobs = jobs; // Assigned jobs parameter to deadline instance variable
        // This.
        jobStack = new Stack<>(); //Adds a new stack for jobs
        for (String job : jobs) {
            jobStack.push(job);
        }
    }

    public String getName() {
        return name;
    } // Returns the name of the project as a String

    public String getProject() {
        return project;
    } // Returns the project as a String

    public Date getDeadline() {
        return deadline;
    } // Returns the deadline date as a date object

    public List<String> getPeople() {
        return people;
    } // Returns a list of people that are added by the user as a String

    public List<String> getJobs() {
        return jobs;
    } // Returns the jobs as a string by the user

    public Stack<String> getJobStack() {
        return jobStack;
    } // Returns the jobStack from what the user has entered,
    // Somewhat repetitive when it comes to repeating the same getJobs that the user enters, just added as a Stack Data Struture for organization
}

class TaskManager {
    private Set<Task> tasks;
    // Task that is used to hold the task name using a set
    private Map<String, List<Task>> projectTasks;
    // Task that is used to hold the string and task name
    private PriorityQueue<Task> deadlineOrder;
    // Task that is used to hold the deadlineOrder date using a PriorityQueue to sort eariest to latest or latest to earliest date

    public TaskManager() {
        tasks = new HashSet<>();
// Creates a new hash sets to store objects of the type of task
        projectTasks = new HashMap<>();
        // Creates a new HashMap to store key-value pairs
        deadlineOrder = new PriorityQueue<>(Comparator.comparing(Task::getDeadline));
        // Creates a new PriorityQueue using a comparator that sorts out the deadline by earliest to latest date and time that is entered by the user
    }

    public void addTask(String name, String project, Date deadline, List<String> people, List<String> jobs) {
        // Using this constructor, creates an object called Task
        Task task = new SimpleTask(name, project, deadline, people, jobs);
        tasks.add(task);
        // Adds the task to the tags HashSet to prevent repetition
        projectTasks.computeIfAbsent(project, k -> new ArrayList<>()).add(task);
        // Checks if it is already available in the HashMap and creates an empty new ArrayList and adds the task of what the user has entered
        deadlineOrder.add(task);
    }

    public List<Task> getTasksByProject(String project) {
        // Keys are the project names and values
        return projectTasks.getOrDefault(project, Collections.emptyList());
        // Returns the empty collection list if the project is empty back to the user

    }

    public List<Task> getTasksByDeadlineOrder() {
        List<Task> sortedTasks = new ArrayList<>();
        // Initializes a new array list that will hold the tasks that are sorted by the deadlines
        while (!deadlineOrder.isEmpty()) {
            // Uses a while loop until the queue is empty
            sortedTasks.add(deadlineOrder.poll());
            // the task that is retrieved by the user is now saved to the sorted tasks which keeps the order of the deadlines
        }
        return sortedTasks;
        // Returns the task back to the user
    }

    public void printTasks(List<Task> tasks) {
        for (Task task : tasks) {
            // Iterates through each task object in Task
            System.out.println("Name: " + task.getName() + ", Project: " + task.getProject() + ", Deadline: " + task.getDeadline());
            System.out.println("People: " + task.getPeople());
            System.out.println("Jobs: " + task.getJobs());
            System.out.println("Speciality: " + task.getJobStack());
            System.out.println("-----------------------");
            // Now for user input, the user inputs the name of the project, the name of the people working on the project
            // Then the jobs of each person which corresponds to the person
            // The speciality is a stack that is used to be repetitive and adds on to getJobs, pretty much the same thing
        }
    }
}


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
// Creates an instance of task manager to assign it to the new variable TaskManager (Name of class)
        Scanner scanner = new Scanner(System.in);
        addProjects(taskManager, scanner);
        System.out.println("\nTasks sorted by the deadline:");
        // Prints out that tasks will be sorted by the deadline order using a comparator
        taskManager.printTasks(taskManager.getTasksByDeadlineOrder());
// Returns a list of projects that are printed along with their deadlines, an add-on to the project
        scanner.close();
    }

    private static void addProjects(TaskManager taskManager, Scanner scanner) {
        // By using a While True statement, the programmer asks if the user wants to enter a project name
        // The Project name can be entered as a blank, if the user wants to leave the program, they would have to click e to exit
        while (true) {
            System.out.print("Enter project name (or 'e' to exit): ");
            String projectName = scanner.nextLine().trim();

            if (projectName.equalsIgnoreCase("e")) {
                break;
            }

            Date deadline = getDateInput(scanner);
            // Using the scanner, the user inputs the exact date, format given in program, then returns the date at the end of the program

            List<String> people = new ArrayList<>();
            // Creates a new list of people that are added where the programmer uses scanner for the user to give input
            // People and Jobs are stored using the new array lists held with String values
            List<String> jobs = new ArrayList<>();
            addPeopleAndJobs(scanner, people, jobs);
            // Then returns the value that the user enters at the end of the program

            taskManager.addTask(projectName, projectName, deadline, people, jobs);
            // Task Manager variables adds the project name, deadline, people and jobs and returns that back to the user
        }
    }


    public static Date getDateInput(Scanner scanner) {
        System.out.print("Enter deadline (yyyy-MM-dd): ");
        String deadlineString = scanner.nextLine().trim();
        return parseDate(deadlineString);
        // Set to public accessible from other classes
        // After user inputs the deadline in specific format, it returns that input back to the user at the end of each entered project
    }

    public static Date parseDate(String dateInt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Since this is a Data Structure project, Parsing Exceptions are not really used unless
        // You need to specify a specific parsing error
        try {
            return dateFormat.parse(dateInt);
            // Returns the integer that is used specifically for the simple date format yyyy-MM-dd
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            // Prints out the date format and asks the user to print it out in that specific format
            return parseDate(getUserInput("Enter deadline (yyyy-MM-dd): "));
            // If there are any parsing errors using the simple data format in that second sentence,
            // it releases an error
            // This is known as a parsing error where the input string that the user enters does not match the format that I have given to the user
            // For example if the user uses 04 representing the year, 12 representing the month and 13 representing the day, it will show an error to the system
            // A Simple Date Format just uses the pattern for a date and time that you want to represent in input for the user to see
        }
    }


    public static void addPeopleAndJobs(Scanner scanner, List<String> people, List<String> jobs) {
        while (true) {
            // Uses a while true statement until the person decides to input their name to the assigned project
            System.out.print("Enter person's name (or 'quit' to finish): ");
            String personName = scanner.nextLine().trim();
            if (personName.equalsIgnoreCase("quit")) {
                break;
            }
            // The program will ask for the persons job and speciality using the jobStack();
            // After the person enters their job, it will add the job, the person's name along with the project to an array list sorted using a HashSet and Map
            System.out.print("Enter person's job: ");
            String job = scanner.nextLine().trim();
            people.add(personName);
            jobs.add(job);
        }
    }

    public static String getUserInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine().trim();
        // Using .trim(); removes any whitespace or trails from the input string
        // The program waits until the user responds by using nextLine();, then returns and prints the message out
    }
}
//* This program was made for a Data Structure project (College Based Project) that uses all forms of data structures that were learned during class.



