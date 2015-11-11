package zk.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import zk.annotation.Lock;
import zk.lock.DistributedLock;
import zk.util.AopUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by weihuang on 14-6-19.
 */
@Aspect
@Component
public class DistributedLockAspect {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    //@Around(value = "@annotation(zk.annotation.Lock)")
    public void tryLock(ProceedingJoinPoint pjp) throws Throwable {
        Lock lockAnnotation = AopUtils.getAnnotation(pjp, Lock.class);
        long expire = lockAnnotation.expire();
        boolean sync = lockAnnotation.sync();
        TimeUnit timeUnit = lockAnnotation.timeUnit();
        String key = lockAnnotation.key();
        try {
            if (DistributedLock.tryLock()) {
                logger.info("pjp");
                pjp.proceed();
            } else {
                logger.info("Not execute!");
            }
        } finally {
            DistributedLock.delete();
        }
    }
    @Around(value = "@annotation(zk.annotation.Lock)")
    public void getLock(ProceedingJoinPoint pjp) throws Throwable {
        Lock lockAnnotation = AopUtils.getAnnotation(pjp, Lock.class);
        long expire = lockAnnotation.expire();
        String key = lockAnnotation.key();
        try {
            if (DistributedLock.getLock(key,expire,TimeUnit.SECONDS)) {
                logger.info("getLock");
                pjp.proceed();
            } else {
                logger.info("Not execute!");
            }
        } finally {
            DistributedLock.clear(key);
        }

    }
}
