import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PrimitiveIterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static Scanner scanner;
    public static PrintStream writer;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService executor = Executors.newFixedThreadPool(3);

     while(true)
     {
         Socket socket = serverSocket.accept();
         executor.execute(new ServerThread(socket));
      }
    }
  }
