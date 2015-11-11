package zk.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by weihuang on 14-6-21.
 */
public class AopUtils {

	public static <T extends Annotation> T getAnnotation(JoinPoint jp, Class<T> clazz) {
		return getMethod(jp).getAnnotation(clazz);
	}

	public static Method getMethod(final JoinPoint jp) {
		final MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		return methodSignature.getMethod();
	}

	public static String getMethodName(final JoinPoint jp) {
		StringBuilder sb = new StringBuilder();
		sb.append(jp.getTarget().getClass().getName()).append(".").append(getMethod(jp).getName());
		return sb.toString();
	}
}
