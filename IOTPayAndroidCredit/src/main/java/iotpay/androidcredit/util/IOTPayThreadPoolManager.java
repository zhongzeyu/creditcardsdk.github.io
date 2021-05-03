package iotpay.androidcredit.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOTPayThreadPoolManager {
    private ExecutorService cachePool = Executors.newCachedThreadPool();

    private ExecutorService singlePool = Executors.newSingleThreadExecutor();

    private static final class SingletonHolder {
        private static final IOTPayThreadPoolManager instance = new IOTPayThreadPoolManager();
    }

    private IOTPayThreadPoolManager() {
    }

    public static void executeInSinglePool(Runnable r) {
        if (r != null) {
            SingletonHolder.instance.singlePool.execute(r);
        }
    }

    public static void executeInCachePool(Runnable r) {
        if (r != null) {
            SingletonHolder.instance.cachePool.execute(r);
        }
    }
}
