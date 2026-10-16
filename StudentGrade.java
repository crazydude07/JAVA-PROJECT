import java.util.*;
import java.io.*;

// CO4: Interface
interface ReportOperations {
    void displayReport();
}

// CO4: Custom exception
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

// CO4: Parent class
class Student {
    protected String name;
    protected int id;
    protected ArrayList<Integer> marks;
    protected double attendance;

    public Student(String name, int id, ArrayList<Integer> marks, double attendance) {
        this.name = name;
        this.id = id;
        this.marks = marks;
        this.attendance = attendance;
    }

    public int getTotal() {
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total;
    }

    public double getAverage() {
        return (double) getTotal() / marks.size();
    }

    public String getGrade() {
        double average = getAverage();
        if (average >= 90)
            return "A";
        else if (average >= 75)
            return "B";
        else if (average >= 60)
            return "C";
        else
            return "D";
    }

    public String getEligibility() {
        if (attendance >= 75)
            return "Eligible";
        else
            return "Not Eligible";
    }

    public String getName() {
        return name;
    }

    public double getAttendance() {
        return attendance;
    }
}

// CO4: Inheritance; CO4: Interface implementation
class GradeStudent extends Student implements ReportOperations {
    public GradeStudent(String name, int id, ArrayList<Integer> marks, double attendance) {
        super(name, id, marks, attendance);
    }

    @Override
    public void displayReport() {
        // CO5: String operations
        String heading = "STUDENT REPORT".concat(" - ").concat(name.toUpperCase());
        System.out.println("\n--- " + heading + " ---");
        System.out.println("Student ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Total Marks: " + getTotal());
        System.out.printf("Average: %.2f%n", getAverage());
        System.out.println("Grade: " + getGrade());
        System.out.println("Attendance: " + attendance + "%");
        System.out.println("Exam Eligibility: " + getEligibility());
        // CO5: substring() example, safely used when ID text is long enough
        String idText = String.valueOf(id);
        if (idText.length() >= 2)
            System.out.println("ID Prefix: " + idText.substring(0, 2));
    }

    public String toFileRecord() {
        return id + "|" + name.replace("|", "/") + "|" + marks.toString().replace("[", "").replace("]", "")
                + "|" + attendance + "|" + getTotal() + "|" + String.format(Locale.US, "%.2f", getAverage())
                + "|" + getGrade() + "|" + getEligibility();
    }
}

public class StudentGrade {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<GradeStudent> students = new ArrayList<>(); // CO6: Collection framework
        int count = 0;

        // CO1-CO3: input, variables, loop and validation
        while (count <= 0) {
            System.out.print("Enter number of students: ");
            if (sc.hasNextInt()) {
                count = sc.nextInt();
                if (count <= 0)
                    System.out.println("Enter a positive number.");
            } else {
                System.out.println("Enter a whole number.");
                sc.next();
            }
        }

        for (int s = 1; s <= count; s++) {
            sc.nextLine(); // consume newline
            System.out.println("\nStudent " + s);
            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            System.out.print("Enter student ID (integer): ");
            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid integer ID: ");
                sc.next();
            }
            int id = sc.nextInt();

            int n = 0;
            while (n <= 0) {
                System.out.print("Enter number of subjects: ");
                if (sc.hasNextInt()) {
                    n = sc.nextInt();
                    if (n <= 0)
                        System.out.println("Number of subjects must be positive.");
                } else {
                    System.out.println("Enter a whole number.");
                    sc.next();
                }
            }

            ArrayList<Integer> marks = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                int mark = -1;
                while (mark < 0 || mark > 100) {
                    System.out.print("Enter marks for subject " + i + " (0-100): ");
                    if (sc.hasNextInt()) {
                        mark = sc.nextInt();
                        try {
                            if (mark < 0 || mark > 100)
                                throw new InvalidMarksException("Marks must be between 0 and 100.");
                            marks.add(mark);
                        } catch (InvalidMarksException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Enter a whole number.");
                        sc.next();
                    }
                }
            }

            double attendance = -1;
            while (attendance < 0 || attendance > 100) {
                System.out.print("Enter attendance percentage (0-100): ");
                if (sc.hasNextDouble()) {
                    attendance = sc.nextDouble();
                    if (attendance < 0 || attendance > 100) {
                        System.out.println("Attendance must be between 0 and 100.");
                    }
                } else {
                    System.out.println("Enter a numeric percentage.");
                    sc.next();
                }
            }

            GradeStudent student = new GradeStudent(name, id, marks, attendance);
            students.add(student); // CO6: add object to ArrayList
            student.displayReport();
        }

        // CO5: FileWriter saves records; CO6: collection processing
        try (FileWriter writer = new FileWriter("students.txt", true)) {
            for (GradeStudent student : students) {
                writer.write(student.toFileRecord() + System.lineSeparator());
            }
            System.out.println("\nStudent records saved to students.txt.");
        } catch (IOException e) {
            System.out.println("Could not save records: " + e.getMessage());
        }

        // CO5: FileReader reads saved records
        System.out.println("\n--- Saved Records ---");
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Could not read records: " + e.getMessage());
        }

        // CO5: compareTo() for alphabetical sorting; CO6: streams, filter, sorted, count
        System.out.println("\n--- Students Sorted by Name ---");
        students.stream()
                .sorted((a, b) -> a.getName().toUpperCase().compareTo(b.getName().toUpperCase()))
                .forEach(student -> System.out.println(student.getName() + " - " + student.getGrade()));

        System.out.println("\n--- Students Eligible for Exams ---");
        students.stream()
                .filter(student -> student.getAttendance() >= 75)
                .forEach(student -> System.out.println(student.getName()));

        long eligibleCount = students.stream()
                .filter(student -> student.getAttendance() >= 75)
                .count();
        System.out.println("Eligible student count: " + eligibleCount);

        sc.close();
    }
}
