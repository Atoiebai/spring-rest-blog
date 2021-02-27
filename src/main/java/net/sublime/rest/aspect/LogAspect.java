package net.sublime.rest.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    public LogAspect() {
        System.out.println("Created LogAspect");
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("within(net.sublime.rest.service.user..* )")
    public void logPointcut() {}

    @Before("logPointcut()")
    public void beforeLogging(@NotNull JoinPoint joinPoint) {
        logger.info("Invoke method " + joinPoint.getSignature());

    }

    @After("logPointcut()")
    public void afterLogging(JoinPoint joinPoint) {
        logger.info("Complete method " + joinPoint.getSignature());
    }
}