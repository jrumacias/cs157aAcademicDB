import java.sql.*;
import java.util.Properties;

public class UniDB {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/university";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "password";
    static final String userName = "root";
    static final String password = "password";


    /**
     * The name of the computer running MySQL
     */
    private final static String serverName = "localhost";

    /**
     * The port of the MySQL server (default is 3306)
     */
    private final static int portNumber = 3306;

    /**
     * The name of the database we are using
     */
    private final static String dbName = "university";


    /**
     * Get a new database connection
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        //Register JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
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
     * Connect to the DB and do some stuff
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Connection conn = null;
        try {
            //Open a connection
			conn = getConnection();
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Console console = new Console(conn);
            console.run();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("ERROR: Could not connect to the database");
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
