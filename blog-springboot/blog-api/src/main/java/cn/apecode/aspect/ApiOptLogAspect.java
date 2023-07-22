package cn.apecode.aspect;

import cn.apecode.entity.OperationLog;
import cn.apecode.common.utils.IpUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author apecode
 * @description 接口操作日志切面
 * @date 3/4/2023 PM2:13
 */

@Aspect
@Component
@Slf4j
public class ApiOptLogAspect {

    /**
     * @description: 设置接口操作日志切入点，记录接口操作日志，在controller包下的所有方法
     * @auther apecode
     * @date 3/4/2023 PM2:19
     */
    @Pointcut("execution(* cn.apecode.controller.*.*(..))")
    public void apiOptLogPointCut() {
    }

    @After("apiOptLogPointCut()")
    public void recordApiOptLog(JoinPoint joinPoint) {
        // 获取requestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // 从切面织入点处通过反射机制获取织入点出的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        // 操作模块
        operationLog.setOptModule(api.tags()[0]);
        // 操作描述
        operationLog.setOptDesc(apiOperation.value());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        // 请求方法
        operationLog.setOptMethod(methodName);
        // 请求参数
        String parameters = excludeParameters(joinPoint.getArgs());
        operationLog.setRequestParam(parameters);
        // 请求IP
        String ipAddress = IpUtils.getIpAddress(request);
        operationLog.setIdAddress(ipAddress);
        // 请求URL
        operationLog.setOptUrl(request.getRequestURI());
        log.info("操作模块：[ {} ]  - 操作描述：[ {} ] - 请求方法：[ {} ] -  操作URL：[ {} ] - 请求IP：[ {} ] -  操作方法：{} - 请求参数：{}", operationLog.getOptModule(), operationLog.getOptDesc(), operationLog.getRequestMethod(), operationLog.getOptUrl(), operationLog.getIdAddress(), operationLog.getOptMethod(), operationLog.getRequestParam());
    }

    /**
     * @param args
     * @return {@link String}
     * @description: 处理不能序列化的
     * MultipartFile不能序列化
     * @auther apecode
     * @date 5/4/2023 PM1:11
     */
    private String excludeParameters(Object[] args) {
        List<Object> arguments = new ArrayList<>();
        for (Object arg : args) {
            // 上传文件无法序列化
            if (arg instanceof MultipartFile) {
                Map<String, Object> fileInfo = new HashMap<>();
                MultipartFile file = (MultipartFile) arg;
                fileInfo.put("文件名", file.getOriginalFilename());
                fileInfo.put("大小", file.getSize());
                fileInfo.put("类型", file.getContentType());
                arguments.add(fileInfo);
                continue;
            }
            arguments.add(arg);
        }
        String paramter = "[]";
        if (!arguments.isEmpty()) {
            try {
                paramter = JSON.toJSONString(arguments);
            } catch (Exception e) {
                paramter = arguments.toString();
            }
        }
        return paramter;
    }
}
