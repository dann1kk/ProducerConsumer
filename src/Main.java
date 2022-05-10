import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter num producers: ");
        int numProds = scanner.nextInt();
        System.out.println("Enter num product each producer produce: ");
        int numProProducts = scanner.nextInt();
        System.out.println("Enter num consumers: ");
        int numCons = scanner.nextInt();
        System.out.println("Enter num product each consumer consume: ");
        int numConProducts = scanner.nextInt();
        System.out.println("Enter limit max: ");
        int maxLimit = scanner.nextInt();
        System.out.println("Enter limit min: ");
        int minLimit = scanner.nextInt();
        System.out.println("Enter num initial products: ");
        int initialNumProduct = scanner.nextInt();
        ProductQueue.numItem.set(initialNumProduct);

        List<Thread> allThreads = new ArrayList<>();
        for (int i = 0; i < numProds; i ++){
           Thread a = new Thread(new Producer(i, maxLimit, numProProducts));
           a.start();
           allThreads.add(a);
        }
        for (int i = 0; i < numCons; i ++){
            Thread a = new Thread(new Consumer(i, minLimit, maxLimit, numConProducts));
            a.start();
            allThreads.add(a);
        }

        Thread stopThread = new Thread(() -> {
            System.out.println("Press S to stop");
            String exitKey = scanner.nextLine();
            while (!exitKey.equals("S")){
                System.out.println("Press S to stop");
                exitKey = scanner.nextLine();
            }
            Producer.running.set(false);
        });
        stopThread.start();
       allThreads.add(stopThread);
       for (Thread thread: allThreads){
           thread.join();
       }

    }
}
