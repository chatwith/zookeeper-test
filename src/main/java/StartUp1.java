import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zk.util.BeanFactory;

public class StartUp1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartUp1.class);

    public static void main(String[] args) {
        BeanFactory.getInstance();
        LOGGER.info("timer started!");
    }
}
