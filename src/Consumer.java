import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
    public static volatile AtomicBoolean consuming = new AtomicBoolean(false);
    public static volatile AtomicBoolean running = new AtomicBoolean(true);

    public static volatile AtomicBoolean start = new AtomicBoolean(false);
    private final int id;
    private final int minLimit;
    private final int maxLimit;

    private final int numProds;

    public Consumer(int id, int minLimit, int maxLimit, int numProds){
        this.id = id;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.numProds = numProds;
    }

    @Override
    public void run() {
        while (running.get()) {
            int currentProduct = ProductQueue.numItem.get();
            if (currentProduct >= maxLimit){
                start.set(true);
            }
            if (start.get()) {
                checkAndConsume(id, minLimit, numProds);
            }
        }
    }

    public static synchronized void checkAndConsume(int id, int minLimit, int numProds){
            if (consuming.get() && start.get()) {
                int currentProduct = ProductQueue.numItem.get();
                if (currentProduct <= minLimit && Producer.running.get()) {
                    System.out.println("Min limit reach, stop consuming| Consumer id: " + id);
                    consuming.set(false);
                } else if (currentProduct > numProds){
                    currentProduct = ProductQueue.numItem.addAndGet(-numProds);
                    System.out.println("Consumer id : " + id + "|Consume " + numProds + " product, current num product: " + currentProduct);
                }else if (currentProduct > 0){
                    currentProduct = ProductQueue.numItem.decrementAndGet();
                    System.out.println("Consumer id : " + id + "|Consume one product, current num product: " + currentProduct);
                }else {
                    Consumer.running.set(false);
                    System.out.println("Stop Consumer id: " + id);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (ProductQueue.numItem.get() > minLimit ) {
                    consuming.set(true);
                    System.out.println("Resume consuming| Consumer id: " + id);
                }
            }
    }
}
