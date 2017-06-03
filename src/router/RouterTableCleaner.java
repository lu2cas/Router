package router;

import java.util.concurrent.Semaphore;

public class RouterTableCleaner implements Runnable {

    private RouterTable routerTable;
    private Semaphore tableMutex;
    private Semaphore socketMutex;

    public RouterTableCleaner(RouterTable router_table, Semaphore table_mutex, Semaphore socket_mutex) {
        this.routerTable = router_table;
        this.tableMutex = table_mutex;
        this.socketMutex = socket_mutex;
    }

    @Override
    public void run() {
        while (true) {
            try {
                tableMutex.acquire();

                if (this.routerTable.removeInactiveRouters()) {
                    System.out.println(this.routerTable);
                    this.socketMutex.release();
                }

                tableMutex.release();

                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
