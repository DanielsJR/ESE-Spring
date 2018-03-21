package nx.ESE;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ApiLogs {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PatchMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
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

}
