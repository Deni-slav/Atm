import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private static ArrayList<Account> accounts;
    private HashMap<Data, Object> sessionData;
    private Socket socket;
    private Scanner reader;
    private PrintStream writer;
    public ServerThread(Socket server) throws IOException {

        accounts = new ArrayList<Account>() {{
            add(new Account(123456789, 1234, 100));
            add(new Account(987654321, 4321, 10000));
            add(new Account(11111111, 1111, 1000));
        }};
        sessionData = new HashMap<Data, Object>();
        socket = server;
    }
    @Override
    public void run() {
        try {
            var out = socket.getOutputStream();
            var in = socket.getInputStream();

            Scanner scanner = new Scanner(System.in);
            reader = new Scanner(in);
            writer = new PrintStream(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerLogic();
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SendMessage(Commands command, String message) {
        // var thread = Thread.currentThread();
        System.out.println("(" + command + ":" + message + ")");
        writer.printf("%s:%s \n", command, message);
    }
    public void SendStatus(Commands command, String status) {
        // var thread = Thread.currentThread();
        System.out.println("(" + command + ":" + status + ")");
        writer.printf("%s:%s \n", command, status);
    }
    public void GetMessage() {
        String wholeMessage = reader.nextLine();
        String[] commandArray = wholeMessage.split(":");

        Commands cmd = Commands.valueOf(commandArray[0]);
        String args = commandArray[1];
        System.out.println("(" + cmd + ")" + args);
        ProcessMessage(cmd, args);
    }
    public void ProcessMessage(Commands cmd, String args) {
        switch (cmd) {
            case WELCOME:
                SendStatus(Commands.WELCOME, "OK");
                break;
            case ACCOUNT_NUMBER:
                SendStatus(Commands.ACCOUNT_NUMBER, args);
                break;
            case PIN:
                int accountNumber = (int) sessionData.get(Data.ACCOUNT_NUMBER);
                var account = accounts.stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst().get();
                SendStatus(Commands.PIN, args);
                break;
            case WITHDRAW:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Withdrawing the amount
                    account.withdraw(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, String.valueOf(Status.OK));
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.WITHDRAW, String.valueOf(Status.ERROR));
                    break;
                }
                break;
            case DEPOSIT:
                try {
                    // Getting the account from the session data
                    account = (Account) sessionData.get(Data.ACCOUNT);
                    // Depositing the amount
                    account.deposit(Double.parseDouble(args));
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, String.valueOf(Status.OK));
                } catch (Exception e) {
                    // Sending the status to the client
                    SendStatus(Commands.DEPOSIT, String.valueOf(Status.ERROR));
                    break;
                }
                break;
            case GET_BALANCE:
                // SendStatus(Commands.GET_BALANCE, Status.OK);
                break;
            default:
                SendStatus(Commands.ERROR, String.valueOf(Status.ERROR));
                break;
        }
    }
    public void ServerLogic() {
        SendMessage(Commands.WELCOME, "Welcome to the ATM");
        GetMessage();
    }
}