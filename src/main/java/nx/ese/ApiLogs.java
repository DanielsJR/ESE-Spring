package nx.ese;

import java.text.MessageFormat;
import java.util.Calendar;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.LogManager;
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
        StringBuilder log = new StringBuilder(jp.getSignature().getName());
        for (Object arg : jp.getArgs()) {
            log.append(" >>>> arguments: ").append(arg);
        }
        LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).info(log.toString());

    }

    @AfterReturning(pointcut = "allResources()", returning = "result")
    public void apiResponseLog(JoinPoint jp, Object result) {
        String log = MessageFormat.format("{0} >>>> return : {1}", jp.getSignature().getName(), result);
        LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).info(log);
    }

    @AfterThrowing(pointcut = "allResources()", throwing = "exception")
    public void apiResponseExceptionLog(JoinPoint jp, Exception exception) {
        String log = MessageFormat.format("{0} >>>> return: {1} -> {2}", jp.getSignature().getName(), exception.getClass().getSimpleName(), exception.getMessage());
        LoggerFactory.getLogger(jp.getSignature().getDeclaringTypeName()).error(log);
    }

    @Around("allResources()")
    public Object processTimeLog(ProceedingJoinPoint pjp) throws Throwable {
        Calendar before = Calendar.getInstance();
        Object obj = pjp.proceed();
        Calendar now = Calendar.getInstance();
        long pt = now.getTimeInMillis() - before.getTimeInMillis();
        LogManager.getLogger(pjp.getSignature().getDeclaringTypeName()).info("Processing time: {} ms", pt);
        return obj;
    }

}
