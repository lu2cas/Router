package router;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RouterTableCleaner implements Runnable {

    private RouterTable routerTable;
    private Semaphore mutex;

    public RouterTableCleaner(RouterTable router_table, Semaphore mutex) {
        this.routerTable = router_table;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        while (true) {
            try {
                mutex.acquire();

                this.routerTable.removeInactiveRouters();

                mutex.release();

                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
