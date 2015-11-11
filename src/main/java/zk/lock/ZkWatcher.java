package zk.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * Created by xingjun.zhang on 2015/11/9.
 */
public class ZkWatcher implements Watcher {

    ZooKeeper zooKeeper = null;

    @Override
    public void process(WatchedEvent event) {
        System.out.println("zk 触发事件: " + event.getType());
    }

    /**
     * connect
     *
     * @param ip
     * @param timeout
     */
    public void connect(String ip, int timeout) {
        try {
            zooKeeper = new ZooKeeper(ip, timeout, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create
     *
     * @param path
     */
    public void create(String path) {
        try {
            zooKeeper.create(path, path.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete
     *
     * @param path
     */
    public void delete(String path) {
        try {
            zooKeeper.delete(path, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * getData
     *
     * @param path
     * @return
     */
    public String getData(String path) {
        try {
            byte[] bytes = zooKeeper.getData(path, this, null);
            return new String(bytes);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * exists
     *
     * @param path
     */
    public Stat exists(String path) {
        try {
            Stat stat = zooKeeper.exists(path, true);
            return stat;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * close
     */
    public void close() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
