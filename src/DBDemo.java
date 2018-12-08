import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * This class demonstrates how to connect to MySQL and run some basic commands.
 * 
 * In order to use this, you have to download the Connector/J driver and add
 * its .jar file to your build path.  You can find it here:
 * 
 * http://dev.mysql.com/downloads/connector/j/
 * 
 * You will see the following exception if it's not in your class path:
 * 
 * java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/
 * 
 * To add it to your class path:
 * 1. Right click on your project
 * 2. Go to Build Path -> Add External Archives...
 * 3. Select the file mysql-connector-java-5.1.24-bin.jar
 *    NOTE: If you have a different version of the .jar file, the name may be
 *    a little different.
 *    
 * The user name and password are both "root", which should be correct if you followed
 * the advice in the MySQL tutorial. If you want to use different credentials, you can
 * change them below. 
 * 
 * You will get the following exception if the credentials are wrong:
 * 
 * java.sql.SQLException: Access denied for user 'userName'@'localhost' (using password: YES)
 * 
 * You will instead get the following exception if MySQL isn't installed, isn't
 * running, or if your serverName or portNumber are wrong:
 * 
 * java.net.ConnectException: Connection refused
 */
public class DBDemo {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final static String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final static String password = "myroot";

	/** The name of the computer running MySQL */
	private final static String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final static int portNumber = 3308;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final static String dbName = "academic_records";
	
	/** The name of the table we are testing with */
	private final String tableName = "JDBC_TEST";
	
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);

		conn = DriverManager.getConnection("jdbc:mysql://"
				+ serverName + ":" + portNumber + "/" + dbName,
				connectionProps);

		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
	    Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(command); // This will throw a SQLException if it fails
	        return true;
	    } finally {

	    	// This will run whether we throw an exception or not
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	/**
	 * Connect to MySQL and do some stuff.
	 */
	public void run() {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
			System.out.println("SUCCESS: Connected to database.\n");
			// User Request #1: View all course offerings.
			Functions.viewCourses(conn);
            // User Request #2: Check grades, specific student.
			Functions.checkGrades(conn, 100017);
            // User Request #3: Search for courses by professor
			Functions.viewCoursesByProf(conn, 400);
            // User Request #4: Search for courses containing a keyword
			Functions.searchByKeyword(conn, "course", "courseName", "Introduction");
            // User Request #5: Enroll student in a section
			Functions.enroll(conn, 100004, 10004);
            // User Request #6: Search for courses containing keyword1 or keyword2
			Functions.unionSearch(conn, "course", "courseName", "courseName", "Biology", "Chemistry");
            // User Request #7: Drop student from a section
			Functions.drop(conn, 100004, 10004);
            // User Request #8: View a student's schedule
			Functions.viewSchedule(conn, 100004);
            // User Request #9: View all professors and their sections (also shows null values)
			Functions.outerJoinProfSearch(conn);
            // User Request #10: Add new course
			Functions.newCourse(conn, 9999, 1000, "199A", "New Class");
            // User Request #11: View the total capacity of combined sections of a course (aggregate function)
			Functions.classSize(conn, 30);
            // User Request #12: Add new professor
			Functions.newProfessor(conn, "Bobby", "Smith", 1000, "Professor", 2018);
            // User Request #13: Delete professor
			Functions.deleteProfessor(conn, 506);
            // User Request #14: Update professor
			Functions.updateProfessor(conn, 500, "Lydia", "Graham", 1000, "Professor", 2000);
            // User Request #15: Add new student
			Functions.newStudent(conn, "Ray", "Ban", 1000, 18, 1);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Connect to the DB and do some stuff
	 */
	public static void main(String[] args) {
		Connection conn = null;
		try {
			conn = getConnection();
			Console console = new Console(conn);
			console.run();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
