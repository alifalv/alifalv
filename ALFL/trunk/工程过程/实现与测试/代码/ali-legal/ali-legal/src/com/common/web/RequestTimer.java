package com.common.web;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.format.number.NumberFormatter;

import com.common.log.ExceptionLogger;

/**
 * 类型描述:请求用时计时器，打印出当前请求从开始（Controller方法执行时）到结束（Controller方法执行完），总共用了多少ms;
 * </br>创建时期: 2016年1月6日
 * 
 * @author hyq
 */
public class RequestTimer implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.nanoTime();
		Object rs = invocation.proceed();
		long end = System.nanoTime();
		String es = new DecimalFormat("##########.####")
				.format((end - start - 0.0) / (1000 * 1000));
		ExceptionLogger.writeLog(ExceptionLogger.DEBUG,
				"###############--本次请求("
						+ invocation.getThis().getClass().getSimpleName() + "."
						+ invocation.getMethod().getName() + ")用时:" + es
						+ "MS;--###############", null, this.getClass());
		return rs;
	}

}
