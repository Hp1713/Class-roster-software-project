import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Scanner;

public class ClassRoster {
	static boolean isRosterLocked = false;
	static String fileToOpen;
	static Scanner scan = new Scanner(System.in);

	static class Student {
		private int iD, year;
		private String name, major;

		public Student(String name, int iD, String major, int year) {
			this.iD = iD;
			this.name = name;
			this.year = year;
			this.major = major;
		}

		public int getID() {
			return iD;
		}

		public int getYear() {
			return year;
		}

		public String getName() {
			return name;
		}

		public String getMajor() {
			return major;
		}

		public String toString() {
			return "Student: " + name + " ID: " + iD + " Major: " + major + " Year: " + year;

		}

		public static Comparator<Student> StudentID = new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				int iD1 = s1.getID();
				int iD2 = s2.getID();

				return iD1 - iD2;
			}
		};

	}

	public static void main(String[] args) {
		login();
		getProps();
		
		// Variable Initializations
		boolean isRunning = true;
		final int studentSize = 500;

		// Initialize ArrayList
		ArrayList<Student> students = new ArrayList<Student>(studentSize);

		// TESTSd
		if ((fileToOpen = createFile()) != null) {
			students = readFile(fileToOpen);
		}
		// BEGIN
		System.out.println("Welcome to the Class Roster Software");


		while (isRunning) {
			System.out.println("Directions: Type Given Commands");
			System.out.println("\"Add\": Add a new student");
			System.out.println("\"Remove\": Remove an existing student");
			System.out.println("\"Sort\": Sort by either Name or ID");
			System.out.println("\"List\": List students in roster");
			System.out.println("\"Lock\": Lock class roster");
			System.out.println("\"Unlock\": unlock class roster");
			System.out.println("\"Help\": Relist directions");
			System.out.println("\"Quit\": Quit application");
			// Scan user input for which type of function to go to next
			String userInApp = scan.nextLine();
			if (userInApp.equalsIgnoreCase("Add")) {
				if (isRosterLocked)
					System.out.println("Roster Locked action denied");
				else {
					addStudent(students);
				}
			}
			if (userInApp.equalsIgnoreCase("Remove")) {
				if (isRosterLocked)
					System.out.println("Roster Locked action denied");
				else {
					removeStudent(students);
				}
			}
			if (userInApp.equalsIgnoreCase("Sort")) {
				sortStudent(students);
			}
			if (userInApp.equalsIgnoreCase("List")) {
				listStudents(students);
			}
			if (userInApp.equalsIgnoreCase("Help")) {
				System.out.println("Directions: Type Given Commands");
				System.out.println("\"Add\": Add a new student");
				System.out.println("\"Remove\": Remove an existing student");
				System.out.println("\"Sort\": Sort by either Name or ID");
				System.out.println("\"List\": List students in roster");
				System.out.println("\"Lock\": Lock/Unlock class roster");
				System.out.println("\"Help\": Relist directions");
				System.out.println("\"Quit\": Quit application");
			}
			if (userInApp.equalsIgnoreCase("Lock")) {
				lockRoster();
			}
			if (userInApp.equalsIgnoreCase("Unlock")) {
				unlockRoser();
			}
			if (userInApp.equalsIgnoreCase("Quit")) {
				isRunning = false;
				quit(students, fileToOpen);
			}
		}
	}

	/***********************************************************/
	public static ArrayList<Student> readFile(String filename) {
		int iD, year;
		String StudentName, StudentLastName, Major, name;

		ArrayList<Student> student = new ArrayList<Student>(500);

		try {
			Scanner sc = new Scanner(new File(filename));

			while (sc.hasNext()) {
				StudentName = sc.next();
				StudentLastName = sc.next();
				name = StudentName + " " + StudentLastName;
				iD = sc.nextInt();
				Major = sc.next();
				year = sc.nextInt();

				student.add(new Student(name, iD, Major, year));

			}

			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return student;
	}

	public static String createFile() {
		String filename = "Students.txt";
		try {
			File myObj = new File(filename);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
				return filename;

			} else {
				return filename;

			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return null;

	}

	public static void quit(ArrayList<Student> student, String filename) {

		try {
			FileWriter myWriter = new FileWriter(filename);

			for (int i = 0; i < student.size(); i++) {
				Student data = student.get(i);
				myWriter.write(data.name + "\t\t" + data.iD + "\t\t" + data.major + "\t\t" + data.year + "\n");
			}
			myWriter.close();
			System.out.println("Successfully saved roster");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static void sortStudent(ArrayList<Student> student) {

		Scanner Sort = new Scanner(System.in);
		System.out.println("Would you like to sort by Name or ID?");
		System.out.println("Enter Name or ID");
		String name_or_id = Sort.nextLine();

		if (name_or_id.equalsIgnoreCase("Name")) {
			System.out.println("Sorted by Name:");
			student.sort((o1, o2) -> o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()));

			for (int i = 0; i < student.size(); i++) {
				Student data = student.get(i);
				System.out.println(data.name + "\t\t" + data.iD + "\t\t" + data.major + "\t\t" + data.year);
			}
		} else if (name_or_id.equalsIgnoreCase("ID")) {
			System.out.println("Sorted by ID:");
			Collections.sort(student, Student.StudentID);
			for (int i = 0; i < student.size(); i++) {
				Student data = student.get(i);
				System.out.println(data.name + "\t\t" + data.iD + "\t\t" + data.major + "\t\t" + data.year);
			}
		}

	}

	public static void addStudent(ArrayList<Student> students) {
		Scanner nameIDScan = new Scanner(System.in);
		Scanner majorYearScan = new Scanner(System.in);
		System.out.println("Type Name: (Firstname Lastname)");
		String name = nameIDScan.nextLine();
		System.out.println("Type ID: (XXXX)");
		int iD = nameIDScan.nextInt();
		System.out.println("Type Major: (Ex: Computer Science)");
		String major = majorYearScan.nextLine();
		System.out.println("Type Year: (XXXX)");
		int year = majorYearScan.nextInt();
		students.add(new Student(name, iD, major, year));
		System.out.println(students.toString());// Prints to show students after being removed (but this can be deleted)
	}

	public static void removeStudent(ArrayList<Student> students) {
		Scanner nameScan = new Scanner(System.in);
		Scanner iDScan = new Scanner(System.in);
		System.out.println("Type Name of student to be removed: (Firstname Lastname)");
		String name = nameScan.nextLine();
		System.out.println("Type ID of student to be removed: (XXXX)");
		int iD = iDScan.nextInt();
		for (int i = 0; i < students.size(); i++) {
			Student tempStudent = students.get(i);
			if (tempStudent.name.equalsIgnoreCase(name) & tempStudent.iD == iD) {
				students.remove(i);
			}
		}
		System.out.println(students.toString());
	}

	public static void listStudents(ArrayList<Student> students) {
		for (int i = 0; i < students.size(); i++) {
			Student data = students.get(i);
			// System.out.println(data.name + "\t\t" + data.iD + "\t\t" + data.major +
			// "\t\t" + data.year);
			System.out.println(data.toString());
		}
		System.out.println();
	}

	public static void login() {
		System.out.println("Enter Username: ");
		String userName = scan.nextLine();
		System.out.print("Enter password: ");
		String password = scan.nextLine();

		if ("admin".equals(userName) && "admin".equals(password)) {
			System.out.println(" User successfully logged-in.. ");

		} else {
			System.out.println(" Invalid Login Credentials ");
			System.exit(0);

		}

	}

	private static void unlockRoser() {
		try (OutputStream output = new FileOutputStream("config.properties")) {

			Properties prop = new Properties();

			// set the properties value
			prop.setProperty("lockRoster", "False");

			// save properties to project root folder
			prop.store(output, null);

			// System.out.println(prop);
			isRosterLocked = Boolean.parseBoolean(prop.getProperty("lockRoster"));

		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	private static void lockRoster() {
		try (OutputStream output = new FileOutputStream("config.properties")) {

			Properties prop = new Properties();

			// set the properties value
			prop.setProperty("lockRoster", "True");

			// save properties to project root folder
			prop.store(output, null);

			isRosterLocked = Boolean.parseBoolean(prop.getProperty("lockRoster"));

		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public static void getProps() {
		try (InputStream input = new FileInputStream("config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);
			isRosterLocked = Boolean.parseBoolean(prop.getProperty("lockRoster"));

		} catch (IOException ex) {
			// prop file dosent exist so we make one
			try (OutputStream output = new FileOutputStream("config.properties")) {

				Properties prop = new Properties();

				// set the properties value
				prop.setProperty("lockRoster", "False");

				// save properties to project root folder
				prop.store(output, null);

				// System.out.println(prop);
				isRosterLocked = Boolean.parseBoolean(prop.getProperty("lockRoster"));

			} catch (IOException io) {
				io.printStackTrace();
			}

		}

	}

}