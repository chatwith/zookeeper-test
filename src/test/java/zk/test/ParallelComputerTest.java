package zk.test;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;
import zk.util.BeanFactory;

/**
 * Created by xingjun.zhang on 2015/11/6.
 */
public class ParallelComputerTest {
    @Test
    public void test() {
        Class[] cls = {StartUp.class, StartUp1.class};
        JUnitCore.runClasses(ParallelComputer.classes(), cls);

        //JUnitCore.runClasses(ParallelComputer.methods(), cls);
    }

    public static class StartUp {
        @Test
        public void test() {

        }
    }

    public static class StartUp1 {
        @Test
        public void test() {

        }
    }

}
