import java.util.concurrent.atomic.AtomicBoolean;

public class Producer implements Runnable{
    public static volatile AtomicBoolean producing = new AtomicBoolean(true);
    public static volatile AtomicBoolean running = new AtomicBoolean(true);
    private final int maxLimit;
    private final int id;
    private final int numProds;

    public Producer(int id, int maxLimit, int numProds){
        this.id = id;
        this.maxLimit = maxLimit;
        this.numProds = numProds;
    }

    @Override
    public void run() {
        while (running.get()) {
            checkAndProduce(id, maxLimit, numProds);
        }
    }
    public static synchronized void checkAndProduce(int id, int maxLimit, int numProds){
            if (producing.get()) {
                int currentProduct = ProductQueue.numItem.get();
                if (currentProduct > maxLimit) {
                    producing.set(false);
                    System.out.println("Max limit reach, stop producing| Producer id: " + id + "| Current prod: " + currentProduct);
                } else if (currentProduct > maxLimit - numProds) {
                    currentProduct = ProductQueue.numItem.incrementAndGet();
                    System.out.println("Producer id : " + id + "|Product one product, current num product: " + currentProduct);
                } else {
                    currentProduct = ProductQueue.numItem.addAndGet(numProds);
                    System.out.println("Producer id : " + id + "|Product " + numProds + " product, current num product: " + currentProduct);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                int currentProduct = ProductQueue.numItem.get();
                if (currentProduct < maxLimit){
                    producing.set(true);
                    System.out.println("Resume producing| Produce id: " + id);
                }
            }
    }
}
