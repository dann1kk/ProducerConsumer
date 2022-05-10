import java.util.concurrent.atomic.AtomicInteger;

public class ProductQueue {
   public volatile static  AtomicInteger numItem = new AtomicInteger(0);
}
