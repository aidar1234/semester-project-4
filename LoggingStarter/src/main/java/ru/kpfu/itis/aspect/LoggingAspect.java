package ru.kpfu.itis.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("@annotation(ru.kpfu.itis.annotation.Log)")
    public void loggingAnnotation() {
    }

    @After("loggingAnnotation()")
    public void after(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method called - method name: {}, method args: {}", methodName, args);
    }
}
