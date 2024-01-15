import java.util.Objects;

public class Account {
    private double balance;
    private int pin;
    private int accountNumber;

    public Account (int accountNumber, int pin, double balance){
        this.accountNumber= accountNumber;
        this.pin=pin;
        this.balance= balance;
    }
    public double getBalance(){
        return balance;
    }
    public void deposit(double amount){
        if (amount < 0) throw new IllegalArgumentException("Can't be negative.");
        balance += amount;
    }
    public void withdraw(double amount){
        if(amount < 0) throw new IllegalArgumentException("Can't be negative.");
        if(amount > this.balance) throw new IllegalArgumentException("Can't be greater han the balance");
           balance -= amount;
    }
    public boolean checkPin(int pin){
       if (this.pin == pin){
           return true;
        } 
           return false;
       }
    public int getAccountNumber() {
        return accountNumber;
    }
}
