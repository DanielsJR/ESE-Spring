package nx.ESE;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ApiLogs {

	@Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
	public void allResources() {
	}

	@Before("allResources()")
	public void apiRequestLog(JoinPoint jp) {
		LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName())
				.info("------------------------- o -------------------------");
		String log = jp.getSignature().getName();
		for (Object arg : jp.getArgs()) {
			log += " >>>> arguments: " + arg;
		}
		LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).info(log);

	}

	@AfterReturning(pointcut = "allResources()", returning = "result")
	public void apiResponseLog(JoinPoint jp, Object result) {
		String log = jp.getSignature().getName() + " >>>> return : " + result;
		LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).info(log);
	}

	@AfterThrowing(pointcut = "allResources()", throwing = "exception")
	public void apiResponseExceptionLog(JoinPoint jp, Exception exception) {
		String log = jp.getSignature().getName() + " >>>> return: " + exception.getClass().getSimpleName() + " -> "
				+ exception.getMessage();
		LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).error(log);
	}

	@Around("allResources()")
	public Object processTimeLog(ProceedingJoinPoint pjp) throws Throwable {
		Calendar before = Calendar.getInstance();
		Object obj = pjp.proceed();
		Calendar now = Calendar.getInstance();
		LogManager.getLogger(pjp.getSignature().getDeclaringTypeName())
				.info("Processing time: " + (now.getTimeInMillis() - before.getTimeInMillis()) + "ms");
		return obj;
	}

}
