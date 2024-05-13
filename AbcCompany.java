import java.util.Scanner;
public class AbcCompany {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BranchManager manager = new BranchManager();

        System.out.print("Enter name: ");
        manager.name = scanner.nextLine();

        System.out.print("Enter date of birth: ");
        manager.dob = scanner.nextLine();

        System.out.print("Enter contact: ");
        manager.contact = scanner.nextLine();

        System.out.print("Enter position: ");
        manager.position = scanner.nextLine();

        System.out.print("Enter branch name: ");
        manager.branchName = scanner.nextLine();

        System.out.print("Enter work experience (in years): ");
        manager.workExperience = scanner.nextInt();

        System.out.print("Enter total hours worked: ");
        int totalHoursWorked = scanner.nextInt();

        manager.display();
        manager.calSalary(totalHoursWorked);
        scanner.close();
    }
}


