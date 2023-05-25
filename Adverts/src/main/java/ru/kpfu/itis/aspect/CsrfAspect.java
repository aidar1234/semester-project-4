package ru.kpfu.itis.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kpfu.itis.security.AccessToken;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class CsrfAspect {

    @Pointcut("execution(public * ru.kpfu.itis.controller.*.*(..))")
    public void addCsrf() {
    }

    @After("addCsrf()")
    public void after() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            AccessToken accessToken = (AccessToken) authentication.getPrincipal();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            request.setAttribute("_csrf", accessToken.getCsrf());
        }
    }
}
