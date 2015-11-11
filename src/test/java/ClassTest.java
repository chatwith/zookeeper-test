import org.junit.Test;

/**
 * Created by xingjun.zhang on 2015/11/11.
 */
public class ClassTest {

    @Test
    public void isAssignable() {

        Integer rType =2;
        int cls =5;
        Integer lType =0;

        System.out.println(int.class.isPrimitive());
        System.out.println(lType.getClass().isAssignableFrom(rType.getClass()));
    }
}
