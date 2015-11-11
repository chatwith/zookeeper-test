package zk.lock;


import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.util.NetworkUtil;
import zk.util.RedisUtil;
import zk.util.ZKUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式锁实现.
 * <p/>
 * 这种实现的原理是,创建某一个任务的节点,比如 /lock/tasckname 然后获取对应的值,如果是当前的Ip,那么获得锁,如果不是,则没获得
 * .如果该节点不存在,则创建该节点,并把改节点的值设置成当前的IP
 */
public class DistributedLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);
    private static ZKClient zkClient;

    private static AtomicInteger isExecute = new AtomicInteger();

    public static final String LOCK_ROOT = "/lock";
    private static String lockName;

    static {
        conn();
        lockName = "/0001";
    }

    public static void conn() {
        //先创建zk链接.
        try {
            createConnection("192.168.11.93:2181,192.168.11.93:2182,192.168.11.93:2183", 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty() {
        return zkClient == null;
    }

    /**
     * redis 锁内存
     *
     * @param lockKey
     * @param timeout
     * @return
     */
    public static boolean getLock(String lockKey, long timeout, TimeUnit unit) {
        boolean lock = false;
        return doLock(lockKey, unit.toNanos(timeout));
    }

    public static boolean doLock(String lockKey, long nanosTimeout) {
        long lastTime = System.nanoTime();
        for (; ; ) {
            System.out.println(isExecute.addAndGet(1));
            if (RedisUtil.getJedis().setnx(lockKey, "getLock") > 0) {
                return true;
            }
            if (nanosTimeout <= 0) {
                return false;
            }
            long now = System.nanoTime();
            nanosTimeout -= now - lastTime;
            lastTime = now;
            if (Thread.interrupted())
                break;
        }
        return false;
    }

    public static void clear(String lockKey) {
        RedisUtil.getJedis().del(lockKey);
    }

    public static boolean tryLock() {
        String path = ZKUtil.contact(LOCK_ROOT, lockName);
        String localIp = NetworkUtil.getNetworkAddress();
        try {
            try {
                zkClient.createPathIfAbsent(path, false, localIp);
                System.out.println(isExecute.get());
            } catch (InterruptedException e) {
                LOGGER.info("1---------");
                return false;
            } catch (KeeperException e) {
                LOGGER.info("2---------");
                return false;
            }
            if (zkClient.exists(path)) {
                String ownnerIp = zkClient.getData(path);
                if (localIp.equals(ownnerIp)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 创建zk连接
     */
    protected static void createConnection(String connectString, int sessionTimeout) throws Exception {
        if (zkClient != null) {
            return;
            //releaseConnection();
        }
        zkClient = new ZKClient(connectString, sessionTimeout);
        zkClient.createPathIfAbsent(LOCK_ROOT, true, "lock");
    }

    /**
     * 关闭ZK连接
     */
    public static void delete() throws Exception {
        if (zkClient != null) {
            zkClient.delete("/lock" + lockName);
        }
    }

    /**
     * 关闭ZK连接
     */
    public static void releaseConnection() throws InterruptedException {
        if (zkClient != null) {
            zkClient.close();
        }
    }

}
