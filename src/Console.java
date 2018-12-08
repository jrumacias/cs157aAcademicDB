import java.sql.Connection;
import java.util.Scanner;

public class Console {
	private Connection conn;
	private Scanner scanner;
	private String input;		// command entered by user
	private boolean loggedIn;
	private int id;		// student id if logged in as student, -1 if logged in as admin or not logged in
	
	public Console(Connection conn) {
		this.conn = conn;
		scanner = new Scanner(System.in);
		id = -1;
	}
	
	public void run() {
		while (true) {
			if (!loggedIn) {
				loginMenu();
			}
			else if (loggedIn && id >= 0) {
				studentMenu();
			}
			else {
				adminMenu();
			}
		}
	}
	
	private void loginMenu() {
		System.out.println("Welcome to University DB.");
		System.out.println("[1] Student  [2] Admin  [3] Quit");
		input = scanner.next();

		switch (input) {
		case "1":	// student
			System.out.println("Student ID:");
			while (!scanner.hasNextInt()) {
				System.out.println("Please enter a number.");
				scanner.next();
			}
			id = scanner.nextInt();
			if (Functions.studentExists(conn, id)) {
				loggedIn = true;
				System.out.println("Welcome, student " + id + ".");
			}
			else {
				System.out.println("No student with that ID exists.");
				id = -1;
			}
			break;
			
		case "2":	// admin
			System.out.println("Welcome, admin.");
			id = -1;
			loggedIn = true;
			break;
			
		case "3":	// quit
			System.exit(0);
			break;
			
		default:
			System.out.println("Invalid input.");
			break;
		}
	}

	private void studentMenu() {
		System.out.println("What would you like to do?");
		System.out.println("[1] Search for classes");
		System.out.println("[2] Add/drop classes");
		System.out.println("[3] View schedule");
		System.out.println("[4] View grades");
		System.out.println("[5] Logout");
		input = scanner.next();
		switch (input) {
		case "1":	// search for classes
			searchForClasses();
			break;
		case "2":	// add/drop classes
			addDropClass();
			break;
		case "3":	// view schedule
			Functions.viewSchedule(conn, id);
			break;
		case "4":	// view grades
			Functions.checkGrades(conn, id);
			break;
		case "5":	// logout
			logout();
			break;
		default:
			System.out.println("Invalid input.");
			break;
		}
	}
	
	private void addDropClass() {
		while (true) {
			System.out.println("Add or drop classes");
			System.out.println("[1] Add class");
			System.out.println("[2] Drop class");
			System.out.println("[3] Back");
			input = scanner.next();
			int sectionID;

			switch (input) {
			case "1":	// add
				System.out.println("Section ID:");
				while (!scanner.hasNextInt()) {
					System.out.println("Please enter a number.");
					scanner.next();
				}
				sectionID = scanner.nextInt();
				Functions.enroll(conn, id, sectionID);
				break;
			case "2":	// drop
				System.out.println("Section ID:");
				while (!scanner.hasNextInt()) {
					System.out.println("Please enter a number.");
					scanner.next();
				}
				sectionID = scanner.nextInt();
				Functions.drop(conn, id, sectionID);
				break;
			case "3":	// back
				return;
			default:
				System.out.println("Invalid input.");
				break;
			}
		}
	}
	
	private void searchForClasses() {
		while (true) {
		System.out.println("Search for classes");
		System.out.println("[1] View all courses");
		System.out.println("[2] Search for section by 1 keyword");
		System.out.println("[3] Search for section by 2 keywords");
		System.out.println("[4] Search for section by professor");
		System.out.println("[5] Show all professors and their course offerings");
		System.out.println("[6] Back");
		input = scanner.next();
		
		switch (input) {
		case "1":	// View all courses
			Functions.viewCourses(conn);
			break;
		case "2":	// Search for section by 1 keyword
			System.out.println("Keyword:");
			input = scanner.next();
			Functions.searchByKeyword(conn, "course", "courseName", input);
			break;
		case "3":	// Search for section by 2 keywords
			System.out.println("Keyword 1:");
			String input1 = scanner.next();
			System.out.println("Keyword 2:");
			String input2 = scanner.next();
			Functions.unionSearch(conn, "course", "courseName", "courseName", input1, input2);
			break;
		case "4":	// Search for section by professor
			System.out.println("Professor ID:");
			while (!scanner.hasNextInt()) {
				System.out.println("Please enter a number.");
				scanner.next();
			}
			int profID = scanner.nextInt();
			if (Functions.profExists(conn, profID)) {
				Functions.viewCoursesByProf(conn, profID);
			}
			else {
				System.out.println("No professor with that ID exists.");
			}
			break;
		case "5":	// Show all professors and their course offerings
			Functions.outerJoinProfSearch(conn);
			break;
		case "6":	// Back
			return;
		default:
			System.out.println("Invalid input.");
			break;
		}
		}
	}

	private void adminMenu() {
		System.out.println("What would you like to do?");
		System.out.println("[L]ogout");
		input = scanner.next();
		switch (input.toUpperCase()) {
		case "L":	// logout
			logout();
			break;
		
		default:
			System.out.println("Invalid input.");
			break;
		}
	}
	
	private void logout() {
		loggedIn = false;
		id = -1;
		System.out.println("Successfully logged out.");
	}
}
