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
		System.out.println("[S]tudent  [A]dmin  [Q]uit");
		input = scanner.next();

		switch (input.toUpperCase()) {
		case "S":	// student
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
				System.out.println("Invalid student ID.");
				id = -1;
			}
			break;
			
		case "A":	// admin
			System.out.println("Welcome, admin.");
			id = -1;
			loggedIn = true;
			break;
			
		case "Q":	// quit
			System.exit(0);
			break;
			
		default:
			System.out.println("Invalid input.");
			break;
		}
	}

	private void studentMenu() {
		System.out.println("What would you like to do, student?");
		System.out.println("[L]ogout");
		input = scanner.next();
		switch (input.toUpperCase()) {
		case "L":	// logout
			loggedIn = false;
			id = -1;
			System.out.println("Successfully logged out.");
			break;
		
		default:
			System.out.println("Invalid input.");
			break;
		}
	}

	private void adminMenu() {
		System.out.println("What would you like to do, admin?");
		System.out.println("[L]ogout");
		input = scanner.next();
		switch (input.toUpperCase()) {
		case "L":	// logout
			loggedIn = false;
			id = -1;
			System.out.println("Successfully logged out.");
			break;
		
		default:
			System.out.println("Invalid input.");
			break;
		}
	}
}
