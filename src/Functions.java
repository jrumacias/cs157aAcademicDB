import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Functions {
	public static boolean studentExists(Connection conn, int id) {
		boolean exists = false;
		try {
			String getStudent = "select * from student where studentid=" + id + ";";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getStudent);
			if (rs.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve student.");
			e.printStackTrace();
		}
		return exists;
	}
	
	public static boolean profExists(Connection conn, int id) {
		boolean exists = false;
		try {
			String getProf = "select * from professor where professorid=" + id + ";";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getProf);
			if (rs.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve professor.");
			e.printStackTrace();
		}
		return exists;
	}
	
	public static void viewCourses(Connection conn) {
		try {
			String viewCourses = 
					"SELECT d.name AS department, d.abbreviation, c.courseNo, c.courseName " + 
					"FROM Course c, Department d " + 
					"WHERE c.deptID=d.deptID " + 
					"ORDER BY d.name, c.courseNo ASC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(viewCourses);
			
            System.out.println("Retrieving all course offerings...");

            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.format("%-20s %-18s %-26s",
                    "Department", "Course Number", "Course Name");
            System.out.println();
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			while(rs.next()) {
				String dept = rs.getString("department");
				String abb = rs.getString("abbreviation");
				String courseNo = rs.getString("courseNo");
				String courseName = rs.getString("courseName");

                System.out.format("%-20s %-4s %-13s %-16s",
                        dept, abb, courseNo, courseName);
                System.out.println();
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			rs.close();
		} catch(SQLException e) {
			System.out.println("ERROR: Could not view courses");
			e.printStackTrace();
		}
	}
	
	public static void checkGrades(Connection conn, int studentID) {
		try {
			String sql = "SELECT abbreviation, courseNo, courseName, grade\r\n" + 
					"FROM Grade\r\n" +
					"JOIN Section ON Grade.sectionID=Section.sectionID\r\n" + 
					"JOIN Course ON Section.courseID=Course.courseID\r\n" + 
					"JOIN Department ON Course.deptID=Department.deptID\r\n" + 
					"WHERE studentID= ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();
            System.out.println("Retrieving grades for Student " + studentID + "...");

            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.format("%-20s %-46s %-16s",
                    "Course Number", "Course Name", "Grade");
            System.out.println();
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while(rs.next()) {
				String abb = rs.getString("abbreviation");
				String courseNo = rs.getString("courseNo");
				String courseName = rs.getString("courseName");
				String grade = rs.getString("grade");

                System.out.format("%-2s %-17s %-46s %-16s",
                        abb, courseNo, courseName, grade);
                System.out.println();
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			rs.close();
		} catch(SQLException e) {
			System.out.println("ERROR: Could not check grades");
			e.printStackTrace();
		}
	}

	public static void viewCoursesByProf(Connection conn, int profID) {
        try {
            String viewCoursesByProf =
                    "SELECT c.courseName, t.professorID, c.courseNo, s.sectionID, p.firstName, p.lastName," +
                            " d.abbreviation \r\n" +
                            "FROM Section s\r\n" +
                            "JOIN Professor p\r\n" +
                            "JOIN Department d\r\n" +
                            "JOIN Teaches t ON s.sectionID=t.sectionID\r\n" +
                            "JOIN Course c ON s.courseID=c.courseID\r\n" +
                            "WHERE t.professorID= ? AND p.professorID=? AND d.deptID=p.dept;";
            PreparedStatement pstmt = conn.prepareStatement(viewCoursesByProf);
            pstmt.setInt(1, profID);
            pstmt.setInt(2, profID);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Retrieving all course offerings by Professor " + profID + "...");

            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.format("%-8s %-14s %-14s %-14s %-14s %-14s",
                    "ID No.", "Last Name", "First Name", "Section ID", "Course", "Course Title");
            System.out.println();
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            while(rs.next()) {
                String profIDNo = rs.getString("professorID");
                String profFN = rs.getString("firstName");
                String profLN = rs.getString("lastName");
                String secIDNo = rs.getString("sectionID");
                String courseNo = rs.getString("courseNo");
                String courseName = rs.getString("courseName");
                String deptAbb = rs.getString("abbreviation");

                System.out.format("%-8s %-14s %-14s %-14s %-1s %-9s %-1s",
                        profIDNo, profLN, profFN, secIDNo, deptAbb, courseNo, courseName);
                System.out.println();
            }
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
            rs.close();
        } catch(SQLException e) {
            System.out.println("ERROR: Could not view courses");
            e.printStackTrace();
        }
    }
	
	//searches database for any course title containing keyWord
	public static void searchByKeyword(Connection con, String table, String column, String keyWord) {
		
		Statement stmt = null;
		String query = "select * from " + table + " WHERE " + column + " LIKE " + "\'%"+ keyWord + "%\'"; 
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			System.out.println("Retrieving all course offerings containing the keyword \"" + keyWord + "\"...");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while (rs.next()) {
				int courseID = rs.getInt("courseID");
				int deptID = rs.getInt("deptID");
				String courseNo = rs.getString("courseNo");
				String courseName = rs.getString("courseName");

				System.out.println("courseId: " + courseID + ", deptId: " + deptID + ", courseNo: "
						   + courseNo + ", courseName:" + courseName);
			}
			
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("Could not return courses");
			e.printStackTrace();
		}
	}
	
	public static void enroll(Connection con, int studentID, int sectionID) {
		//first check to see if studentID is valid
		Statement stmt = null;
		String getStudents = "select * from student where studentID = " + studentID;
		String getSection = "select * from section where sectionID = " + sectionID;

		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getStudents);
			if (!rs.next()) {
				System.out.println("No student with that ID exists.");
				return;
			}
			
			rs = stmt.executeQuery(getSection);
			if (!rs.next()) {
				System.out.println("No section with that ID exists.");
				return;
			}
			if (rs.getInt("capacity") <= rs.getInt("enrolled")) {
				System.out.println("Section is full.");
				return;
			}

			System.out.println("Enrolling student " + studentID + " in section " + sectionID + "...");

			String add = "insert into enrolledIn values (" + sectionID + "," + studentID + ")";
			stmt.executeUpdate(add);
			System.out.println("Student " + studentID + " successfully enrolled in section " + sectionID);
			System.out.println("Done.\n");
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			System.out.println("Student is already enrolled in this section.");
		} catch (SQLException e) {
			System.out.println("Student does not exist.");
			e.printStackTrace();
		}
	}
	
	//searches for courses that contain either key1 or key2
	public static void unionSearch(Connection con, String table, String firstColumn, String secondColumn, String firstKey, String secondKey) {
		Statement s1 = null;
		final String UNION = "union";
		String q1 = "select * from " + table + " WHERE " + firstColumn + " LIKE " + "\'%"+ firstKey + "%\'";
		String q2 = "select * from " + table + " WHERE " + secondColumn + " LIKE " + "\'%"+ secondKey + "%\'";
		
		try {
			s1 = con.createStatement();
			//System.out.println(q1 + UNION + q2);
			ResultSet rs = s1.executeQuery(q1 + " " + UNION + " "+ q2);
			
			System.out.println("Retrieving all course offerings containing the keyword \"" + firstKey + "\" or \"" + secondKey + "\"...");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while (rs.next()) {
				int courseID = rs.getInt("courseID");
				int deptID = rs.getInt("deptID");
				String courseNo = rs.getString("courseNo");
				String courseName = rs.getString("courseName");
				System.out.println("courseId: " + courseID + ", deptId: " + deptID + ", courseNo: "
								   + courseNo + ", courseName:" + courseName);
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data");
			e.printStackTrace();
		}
	}
	
	public static void drop(Connection conn, int studentID, int sectionID) {
		//first check to see if studentID is valid
		Statement stmt = null;
		String getStudents = "select * from student where studentID = " + studentID;
		String getSection = "select * from section where sectionID = " + sectionID;
		String checkEnrollment = "SELECT * FROM EnrolledIn WHERE studentID=" + studentID + " AND sectionID=" + sectionID + ";";
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getStudents);
			if (!rs.next()) {
				System.out.println("No student with that ID exists.");
				return;
			}
			
			rs = stmt.executeQuery(getSection);
			if (!rs.next()) {
				System.out.println("No section with that ID exists.");
				return;
			}
			
			rs = stmt.executeQuery(checkEnrollment);
			if (!rs.next()) {
				System.out.println("Student is not enrolled in this section.");
				return;
			}
			
			System.out.println("Dropping student " + studentID + " from section " + sectionID + "...");
			
			String drop = "DELETE FROM EnrolledIn WHERE studentID=" + studentID + " AND sectionID=" + sectionID + ";";
			stmt.executeUpdate(drop);
			System.out.println("Student : " + studentID + " successfully dropped from section : " + sectionID);
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Student does not exist");
			e.printStackTrace();
		}
	}
	
	public static void viewSchedule(Connection conn, int studentID) {
		try {
			String sql = "SELECT Section.sectionID, abbreviation, courseNo, courseName, date, building, roomNo\r\n" + 
					"FROM EnrolledIn\r\n" + 
					"JOIN Section ON EnrolledIn.sectionID=Section.sectionID\r\n" + 
					"JOIN Course ON Section.courseID=Course.courseID\r\n" + 
					"JOIN Department ON Course.deptID=Department.deptID\r\n" + 
					"WHERE studentID= ?;\r\n";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();
            System.out.println("Retrieving schedule for Student " + studentID + "...");

            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Section ID \t\t Course Number \t\t Course Name \t\t Date \t\t Room");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while(rs.next()) {
				String section = rs.getString("sectionID");
				String abb = rs.getString("abbreviation");
				String courseNo = rs.getString("courseNo");
				String courseName = rs.getString("courseName");
				String date = rs.getString("date");
				String building = rs.getString("building");
				String roomNo = rs.getString("roomNo");

                System.out.println(section + "\t\t" + abb + " " + courseNo + "\t\t" + courseName + "\t\t" + date + "\t\t" + building + " " + roomNo);
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			rs.close();
		} catch(SQLException e) {
			System.out.println("ERROR: Could not retrieve schedule.");
			e.printStackTrace();
		}
	}
	
	/**
	 * View all professors and the sections they're teaching. Also lists professors that are not 
	 * teaching any sections.
	 * @param conn
	 * @param studentID
	 */
	public static void outerJoinProfSearch(Connection conn) {
		try {
			String sql = "SELECT Professor.professorID, lastName, firstName, Section.sectionID, abbreviation, courseNo FROM Professor\r\n" + 
					"LEFT OUTER JOIN Teaches ON Professor.professorID=Teaches.professorID\r\n" + 
					"LEFT OUTER JOIN Section ON Teaches.sectionID=Section.sectionID\r\n" + 
					"LEFT OUTER JOIN Course ON Section.courseID=Course.courseID\r\n" + 
					"LEFT OUTER JOIN Department ON Course.deptID=Department.deptID;\r\n";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
            System.out.println("Retrieving all professors and their course offerings...");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Professor ID \t\t Last Name \t\t First Name \t\t Section ID \t\t Course");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while(rs.next()) {
				String id = rs.getString("professorID");
				String last = rs.getString("lastName");
				String first = rs.getString("firstName");
				String section = rs.getString("sectionID");
				String abb = rs.getString("abbreviation");
				String courseNo = rs.getString("courseNo");

                System.out.println(id + "\t\t" + last + "\t\t" + first + "\t\t" + section + "\t\t" + abb + " " + courseNo);
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			rs.close();
		} catch(SQLException e) {
			System.out.println("ERROR: Could not retrieve schedule.");
			e.printStackTrace();
		}
	}
	
	public static void newCourse(Connection conn, int courseID, int deptID, String courseNo, String courseName) {
		//first check to see if parameters are valid
		Statement stmt = null;
		String checkCourseID = "select * from course where courseID = " + courseID;
		String checkDeptID = "select * from department where deptID= " + deptID;
		String checkCourseNo = "select * from course where deptID = " + deptID + " and courseNo = '" + courseNo + "';";
		
		System.out.println("Adding new course...");

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkCourseID);
			if (rs.next()) {
				System.out.println("A course with that ID already exists.\n");
				return;
			}
			
			rs = stmt.executeQuery(checkDeptID);
			if (!rs.next()) {
				System.out.println("No department with that ID exists.\n");
				return;
			}
			
			rs = stmt.executeQuery(checkCourseNo);
			if (rs.next()) {
				System.out.println("A course with that course number already exists.\n");
				return;
			}
			
			String addCourse = "insert into course values (" + courseID + ", " + deptID + ", '" + courseNo + "', '" + courseName + "');";
			stmt.executeUpdate(addCourse);
			System.out.println("Successfully added the course \"" + courseName + "\"");
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Failed to add new course.");
			e.printStackTrace();
		}
	}
	
	public static void classSize(Connection conn, int minSeats) {
		try {
			String sql = "SELECT courseID, SUM(capacity) AS totalSeats FROM Section\r\n" + 
					"GROUP BY courseID\r\n" + 
					"HAVING totalSeats > ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, minSeats);
			ResultSet rs = pstmt.executeQuery();
            System.out.println("Retrieving courses with at least " + minSeats + " seats...");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Course ID \t\t Capacity");
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
			
			while(rs.next()) {
				String courseID = rs.getString("courseID");
				String capacity = rs.getString("totalSeats");

                System.out.println(courseID + "\t\t" + capacity);
			}
            System.out.println("-----------------------------------------" +
                    "---------------------------------------------");
            System.out.println("Done.\n");
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void newProfessor(Connection conn, String first, String last, int dept, String title, int yearHired) {
		//first check to see if parameters are valid
		Statement stmt = null;
		String checkDeptID = "select * from department where deptID= " + dept;
		
		System.out.println("Adding new professor...");

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkDeptID);
			rs = stmt.executeQuery(checkDeptID);
			if (!rs.next()) {
				System.out.println("No department with that ID exists.\n");
				return;
			}
			
			String addProf = "insert into professor(firstName, lastName, dept, title, yearHired) "
					+ "values ('" + first + "', '" + last + "', " + dept + ", '" + title + "', " + yearHired + ");";
			stmt.executeUpdate(addProf);
			System.out.println("Successfully added " + title + " " + first + " " + last + "\"");
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Failed to add new professor.");
			e.printStackTrace();
		}
	}
	
	public static void deleteProfessor(Connection conn, int professorID) {
		//first check to see if professorID is valid
		Statement stmt = null;
		String getProf = "select * from professor where professorID = " + professorID;
		
		System.out.println("Removing professor " + professorID + "...");

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(getProf);
			if (!rs.next()) {
				System.out.println("No professor with that ID exists.\n");
				return;
			}
			
			String delete = "DELETE FROM Professor WHERE professorID=" + professorID + ";";
			stmt.executeUpdate(delete);
			System.out.println("Professor " + professorID + " successfully removed.");
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Professor does not exist");
			e.printStackTrace();
		}
	}
	
	public static void updateProfessor(Connection conn, int profID, String first, String last, int dept, String title, int yearHired) {
		//first check to see if parameters are valid
		Statement stmt = null;
		String checkDeptID = "select * from department where deptID= " + dept;
		String checkProfID = "select * from professor where professorID= " + profID;
		
		System.out.println("Updating professor " + profID + "...");

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkDeptID);
			rs = stmt.executeQuery(checkDeptID);
			if (!rs.next()) {
				System.out.println("No department with that ID exists.\n");
				return;
			}
			
			rs = stmt.executeQuery(checkProfID);
			if (!rs.next()) {
				System.out.println("No professor with that ID exists.\n");
				return;
			}
			
			String update = "update professor set "
					+ "firstName='" + first + "', lastName='" + last + "', dept=" + dept + ", title='" + title + "', yearHired=" + yearHired
					+ " where professorID=" + profID + ";";
			stmt.executeUpdate(update);
			System.out.println("Successfully updated professor " + profID);
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Failed to add new professor.");
			e.printStackTrace();
		}
	}
	
	public static void newStudent(Connection conn, String first, String last, int dept, int age, int year) {
		//first check to see if parameters are valid
		Statement stmt = null;
		String checkDeptID = "select * from department where deptID= " + dept;
		
		System.out.println("Adding new student...");

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkDeptID);
			rs = stmt.executeQuery(checkDeptID);
			if (!rs.next()) {
				System.out.println("No department with that ID exists.\n");
				return;
			}
			
			String addProf = "insert into student(firstName, lastName, major, age, year) "
					+ "values ('" + first + "', '" + last + "', " + dept + ", '" + age + "', " + year + ");";
			stmt.executeUpdate(addProf);
			System.out.println("Successfully added " + first + " " + last);
			System.out.println("Done.\n");
		} catch (SQLException e) {
			System.out.println("Failed to add new student.");
			e.printStackTrace();
		}
	}
}
