import java.util.*;

public class AccountManagement{
    static Scanner sc = new Scanner(System.in);
    static int cusId = 1;
    static int accountNoGenerator = 500500;

    static Map<Integer, BankAccount> bankAccounts = new HashMap<>(); // account No and Account Objects
    static Map<Integer, String> customers = new HashMap<>(); // customer id and passwords
    static Map<Integer, Integer> customerAccounts = new HashMap<>(); //customer id and their account Nos

    static void customerLogin(){
        System.out.println("enter customer id");
        int customerLoginId = sc.nextInt();
        if(!customers.containsKey(customerLoginId)) {
            System.out.println("Customer not found!");
            return;
        }
        System.out.println("enter password");
        String customerLoginPass = sc.next();
        String encryptedLoginPass = encryptPassword(customerLoginPass);
        if(!customers.get(customerLoginId).equals(encryptedLoginPass)){
            System.out.println("Password did not match");
            return;
        }
        int customerAccountNumber = customerAccounts.get(customerLoginId);
        boolean looper = true;
        while (looper) {
        System.out.println("1.ATM withdraw 2.Cash Deposit 3.Amount Transfer 4.Transactions 5.change password 6. exit");
        int choice = sc.nextInt();
            switch (choice){
                case 1: // cash Withdrawal
                    amountWithdraw(customerAccountNumber);
                    break;
                case 2: // cash Deposit
                    cashDeposit(customerAccountNumber);
                    break;
                case 3: // Account to account money transfer
                    System.out.println("enter beneficiary's account number");
                    int beneficiaryAccountNo = sc.nextInt();
                    accountTransfer(customerAccountNumber, beneficiaryAccountNo);
                    break;
                case 4: // Display Transactions of a customer
                    displayTransactions(customerAccountNumber);
                    break;
                case 5: // change the existing password of the customer
                    changePassword(customerAccountNumber, customerLoginId);
                    looper = false;
                    break;
                case 6: //to exit
                    looper = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void displayNCustomers(int n){
        if(customerAccounts.size() < n) {
            System.out.println("No. of customers are less than n");
            return;
        }

    }

    public static void displayTransactions(int customerAccountNumber){
        BankAccount accountTransactions = bankAccounts.get(customerAccountNumber);
        System.out.println(accountTransactions.displayDetails());
        List<BankAccount.Transactions> transactions = accountTransactions.getTransactionsList();
        for(BankAccount.Transactions transaction : transactions){
            System.out.println(transaction);
        }
    }

    public static void changePassword(int customerAccountNumber, int customerLoginId) {
        BankAccount accountForPasswordChange = bankAccounts.get(customerAccountNumber);
        System.out.println("Enter old password");
        String oldPassword = encryptPassword(sc.next());
        if(!accountForPasswordChange.getEncryptedPwd().equals(oldPassword)){
            System.out.println("Password did not match");
            return;
        }
        System.out.println("enter new password");
        String password = sc.next();
        if(oldPassword.equals(encryptPassword(password))){
            System.out.println("Password can't be the same as old password");
            return;
        }
        while(isPasswordWeak(password)) {
            System.out.println("Password is weak. Enter a Strong Password");
            password = sc.next();
        }
        System.out.println("Re-enter new password");
        if(!password.equals(sc.next())) {
            System.out.println("Password did not match");
            return;
        }
        String encryptedPassword = encryptPassword(password);
        accountForPasswordChange.setEncryptedPwd(encryptedPassword);
        bankAccounts.replace(customerAccountNumber, accountForPasswordChange);
        customers.replace(customerLoginId, encryptedPassword);
        System.out.println("Password changed successfully");
    }

    public static void accountTransfer(int accountNoTransfer, int beneficiaryAccountNo){
        if(!bankAccounts.containsKey(beneficiaryAccountNo)){
            System.out.println("beneficiary's account Number not found!");
            return;
        }
        if(accountNoTransfer == beneficiaryAccountNo){
            System.out.println("your account and beneficiary account numbers can't are same");
            return;
        }
        BankAccount bankAccountFROM = bankAccounts.get(accountNoTransfer);
        BankAccount bankAccountTO = bankAccounts.get(beneficiaryAccountNo);
        System.out.println("enter amount to transfer");
        double amountToTransfer = sc.nextDouble();
        double previousBalanceFROM = bankAccountFROM.getBalance();
        double previousBalanceTO = bankAccountTO.getBalance();
        if(previousBalanceFROM-amountToTransfer < 1000){
            System.out.println("Low balance");
            return;
        }
        bankAccountFROM.setBalance(previousBalanceFROM-amountToTransfer);
        bankAccountTO.setBalance(previousBalanceTO+amountToTransfer);
        BankAccount.Transactions transactions = new BankAccount.Transactions("TransferTo" + beneficiaryAccountNo + " ", previousBalanceFROM, bankAccountFROM.getBalance());
        bankAccountFROM.setTransactions(transactions);
        BankAccount.Transactions transaction = new BankAccount.Transactions("Cash Deposit", previousBalanceTO, bankAccountTO.getBalance());
        bankAccountTO.setTransactions(transaction);
        System.out.println("Amount transfer successful");
    }

    public static void amountWithdraw(int accountNo){
        BankAccount bankAccountWithdraw = bankAccounts.get(accountNo);
        System.out.println("Enter amount to withdraw");
        double amountToWithdraw = sc.nextDouble();
        double previousBalance = bankAccountWithdraw.getBalance();
        if((previousBalance-amountToWithdraw) < 1000){
            System.out.println("Balance low");
            return;
        }
        bankAccountWithdraw.setBalance(bankAccountWithdraw.getBalance()-amountToWithdraw);
        double currentBalance = bankAccountWithdraw.getBalance();
        BankAccount.Transactions transactions = new BankAccount.Transactions("ATMWithdrawal", previousBalance, currentBalance);
        bankAccountWithdraw.setTransactions(transactions);
        System.out.println("Amount debited Successfully");
    }

    public static void cashDeposit(int accountNoDeposit){
        BankAccount bankAccountDeposit = bankAccounts.get(accountNoDeposit);
        System.out.println("Enter amount to deposit");
        double amountToDeposit = sc.nextDouble();
        double previousBalance = bankAccountDeposit.getBalance();
        bankAccountDeposit.setBalance(previousBalance + amountToDeposit);
        double currentBalance = bankAccountDeposit.getBalance();
        BankAccount.Transactions transactions = new BankAccount.Transactions("Cash Deposit", previousBalance, currentBalance);
        bankAccountDeposit.setTransactions(transactions);
        System.out.println("Cash Deposited Successfully");
    }

    static void addCustomer(){
        System.out.println("Enter name");
        String name = sc.next();
        System.out.println("Enter password");
        String pass = sc.next();
        if(isPasswordWeak(pass)){
            System.out.println("Password is weak");
            return;
        }
        System.out.println("Re-enter password");
        String rePass = sc.next();
        if(!pass.equals(rePass)) {
            System.out.println("Passwords don't match!");
            return;
        }
        int customerId = cusId++;
        String encryptedPwd  = encryptPassword(pass);
        int accountNo = accountNoGenerator++;
        BankAccount bankAccount = new BankAccount(customerId, accountNo, name, encryptedPwd);
        BankAccount.Transactions transactions = new BankAccount.Transactions("Opening", 0, 10000);
        bankAccount.setTransactions(transactions);
        bankAccounts.put(accountNo, bankAccount);
        customers.put(customerId, encryptedPwd);
        customerAccounts.put(customerId, accountNo);
        System.out.println("Customer Added Successfully");
        System.out.println("Customer Id is " + customerId);
        System.out.println("Account Number is " + accountNo);
    }

    public static String encryptPassword(String password){
        StringBuilder sb = new StringBuilder();
        for(char ch : password.toCharArray()){
            if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch>= '0' && ch<= '9')){
                if(ch == 'Z'){
                    sb.append('A');
                }
                else if(ch == 'z'){
                    sb.append('a');
                }
                else if(ch == '9'){
                    sb.append('0');
                }
                else {
                    sb.append(++ch);
                }
            }
        }
        return sb.toString();
    }

    public static void displayCustomers(){
        for(int i: bankAccounts.keySet())
            System.out.println(bankAccounts.get(i));
    }

    public static boolean isPasswordWeak(String password){
        int[] comp = new int[3];
        for(char ch: password.toCharArray()){
            if(ch >= 'A' && ch <= 'Z') comp[0]++;
            else if(ch >= 'a' && ch <= 'z') comp[1]++;
            else if(ch>= '0' && ch<= '9') comp[2]++;
        }
        for(int i:comp)
            if(i<2)
                return true;
        return false;
    }
}
