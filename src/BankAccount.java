import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {
    private final int cusId;
    private final int accountNo;
    private final String name;
    private double balance;
    private String encryptedPwd;
    private final List<Transactions> transactionsList;

    public BankAccount(int cusId, int accountNo, String name, String password){
        this.cusId = cusId;
        this.accountNo = accountNo;
        this.name = name;
        this.balance = 10_000.0;
        this.encryptedPwd = password;
        this.transactionsList = new ArrayList<>();
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public String displayDetails() {
        return "Customer Name: " + name + "\n" +
                "Account No: " + accountNo + "\n" +
                "Customer Id: " + cusId;
    }

    public void setTransactions(Transactions transaction) {
        this.transactionsList.add(transaction);
    }

    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", name='" + name + '\'' +
                ", accountNo=" + accountNo +
                ", cusId=" + cusId +
                '}';
    }

    public static class Transactions{
        private static int transIdGenerator = 1;
        private final int transId;
        private final String transType;
        private final double previousBalance;
        private final double currentBalance;
        private final LocalDateTime transactionDate;

        public Transactions(String transType, double previousBalance, double currentBalance){
            this.transactionDate = LocalDateTime.parse(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            this.transId = transIdGenerator++;
            this.transType = transType;
            this.previousBalance = previousBalance;
            this.currentBalance = currentBalance;
        }

        @Override
        public String toString() {
            return "Transactions{" +
                    ", transId=" + transId +
                    ", transType='" + transType + '\'' +
                    ", previousBalance=" + previousBalance +
                    ", currentBalance=" + currentBalance +
                    ", transactionDate=" + transactionDate +
                    '}';
        }
    }
}
