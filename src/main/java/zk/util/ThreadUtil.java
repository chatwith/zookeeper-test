package zk.util;

/**
 * Created by xingjun.zhang on 2015/11/9.
 */
public class ThreadUtil {

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
