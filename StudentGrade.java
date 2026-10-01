import java.util.Scanner;

public class StudentGrade {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        int n = 0;
        while (n <= 0) {
            System.out.print("Enter number of subjects: ");
            n = sc.nextInt();

            if (n <= 0)
                System.out.println("Enter a valid number!");
        }

        int total = 0;

        for (int i = 1; i <= n; i++) {
            int mark = -1;

            while (mark < 0 || mark > 100) {
                System.out.print("Enter marks for subject " + i + ": ");
                mark = sc.nextInt();

                if (mark < 0 || mark > 100)
                    System.out.println("Marks must be between 0 and 100.");
            }

            total += mark;
        }

        System.out.print("Enter attendance percentage: ");
        double attendance = sc.nextDouble();

        double average = (double) total / n;
        String grade;

        // else-if ladder for grade
        if (average >= 90)
            grade = "A";
        else if (average >= 75)
            grade = "B";
        else if (average >= 60)
            grade = "C";
        else
            grade = "D";

        System.out.println("\n--- Student Report ---");
        System.out.println("Name: " + name);
        System.out.println("Total Marks: " + total);
        System.out.println("Average: " + average);
        System.out.println("Grade: " + grade);

        // if-else for attendance
        if (attendance >= 75)
            System.out.println("Exam Eligibility: Eligible");
        else
            System.out.println("Exam Eligibility: Not Eligible");

        sc.close();
    }
}  