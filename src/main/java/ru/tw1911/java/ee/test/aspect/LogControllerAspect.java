package ru.tw1911.java.ee.test.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LogControllerAspect {

    private static final Logger log = LoggerFactory.getLogger(LogControllerAspect.class);
    @Before("execution(* ru.tw1911.java.ee.test.controllers.OperationTypeController.*(..))")
    public void beforeControllerMethod(JoinPoint jp){
        StringBuilder sb = new StringBuilder();
        sb.append(jp.getKind())
                .append(" ")
                .append(jp.getTarget().getClass().getSimpleName())
                .append(".")
                .append(jp.getSignature().getName())
                .append(" with args: ");
        for (Object o: jp.getArgs()){
            sb.append(o.toString());
        }
        log.info(sb.toString());    }


}
