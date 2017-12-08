package com.vastio.rest.aop;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

/**
 * 此类描述的是：审计日志aspect类
 */
@Aspect
public class AuditLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class.getName());

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

//    @Autowired
//    AuditLogService logService;

    @Pointcut("@annotation(com.vastio.rest.aop.AuditLog)")
    public void methodAuditPointcut() {}

    @Around("methodAuditPointcut()")
    public Object methodAuditPointcut(ProceedingJoinPoint joinPoint) {
        Object result = null;

        logger.debug("Audit log start");
        Object[] args = joinPoint.getArgs();
        try {
            result = joinPoint.proceed();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = methodSignature.getMethod();
            AuditLog methodCache = targetMethod.getAnnotation(AuditLog.class);
            AuditLogType type = methodCache.remark();
            if (type == AuditLogType.USER_LOGIN) {
                @SuppressWarnings("rawtypes")
                Map map = (Map) result;
                String pass = "验证通过";
                if (!pass.equals(map.get("message"))) {
                    //logService.create(type.getName(), "", getClientIp(), "0", "");
                } else {
                    //logService.create(type.getName(), "", getClientIp(), "1", "");

                }
            } else {
                String[] strings = methodSignature.getParameterNames();
                StringBuffer condition = new StringBuffer();
                for (int i = 0; i < strings.length; i++) {
                    if (args[i] instanceof Model) {
                        if (i == strings.length - 1) {
                            condition.delete(condition.length() - 5, condition.length() - 1);
                        }
                        continue;
                    }
                    condition.append(strings[i] + " = ");
                    condition.append(args[i]);
                    if (i < strings.length - 1) {
                        condition.append(" and ");
                    }
                }
//                logService.create(type.getName(), type.getDescription(), getClientIp(), "1",
//                        condition.toString());

            }
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    private String getClientIp() {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
