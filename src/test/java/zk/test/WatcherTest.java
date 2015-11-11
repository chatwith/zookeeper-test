package zk.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.lock.ZkWatcher;
import zk.util.ThreadUtil;

/**
 * Created by xingjun.zhang on 2015/11/9.
 */
public class WatcherTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(WatcherTest.class);

    static ZkWatcher watcher = new ZkWatcher();

    @Before
    public void before() {
        watcher.connect("192.168.11.93:2181,192.168.11.93:2182,192.168.11.93:2183", 10000);
    }

    @Test
    public void testWatcherEvent() {
        ThreadUtil.sleep(3000);
        watcher.create("/zxj");
        ThreadUtil.sleep(3000);
        watcher.getData("/zxj");
        ThreadUtil.sleep(3000);
        watcher.exists("/zxj");
        ThreadUtil.sleep(1000);
        watcher.delete("/zxj");
    }

    @Test
    public void test() {
        Class[] cls = {StartUp.class, StartUp1.class, StartUp2.class};
        JUnitCore.runClasses(ParallelComputer.classes(), cls);

        //JUnitCore.runClasses(ParallelComputer.methods(), cls);
    }

    public static class StartUp {
        @Test
        public void test() {
            LOGGER.info(watcher.getData("/zxj"));
        }
    }

    public static class StartUp1 {
        @Test
        public void test() {
            LOGGER.info(JSONObject.toJSONString(watcher.exists("/zxj")));
        }
    }

    public static class StartUp2 {
        @Test
        public void test() {
            LOGGER.info("close");
            watcher.close();
        }
    }
}
