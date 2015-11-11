package zk.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zk.annotation.Lock;

import java.util.concurrent.TimeUnit;

@Component
public class LockTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockTimer.class);

    @Lock(expire = 10, timeUnit = TimeUnit.SECONDS)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {

        try {
            LOGGER.info("I am timer start execute!");
            Thread.sleep(20000);
            LOGGER.info("I am timer end execute!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
