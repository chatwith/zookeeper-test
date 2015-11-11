package zk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {
	private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

	private final String[] configLocations;
	private AbstractApplicationContext ctx;

	private BeanFactory() {
		configLocations = new String[]{
				"classpath*:zk-timer.xml"};
		try {
			ctx = new ClassPathXmlApplicationContext(configLocations);
			ctx.registerShutdownHook();
		} catch (Exception e) {
			logger.error("spring init error, system exit", e);
			System.exit(1);
		}
	}

	public static BeanFactory getInstance() {
		return BeanFactoryHolder.FACTORY;
	}

	public <T> T getBean(final Class<T> clazz) {
		return ctx.getBean(clazz);
	}

	public Object getBean(final String beanId) {
		return ctx.getBean(beanId);
	}

	private static class BeanFactoryHolder {
		private static final BeanFactory FACTORY = new BeanFactory();
	}
}
