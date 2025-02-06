import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean loop = true;
        while(loop){
            System.out.println("1.Add customer 2.Login 10.DisplayCustomers 12.Exit");
            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    AccountManagement.addCustomer();
                    break;
                case 2:
                    AccountManagement.customerLogin();
                    break;
                case 10:
                    AccountManagement.displayCustomers();
                    break;
                case 12:
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}