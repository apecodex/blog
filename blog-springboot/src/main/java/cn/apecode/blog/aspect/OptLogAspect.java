package cn.apecode.blog.aspect;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.entity.OperationLog;
import cn.apecode.blog.mapper.OperationLogMapper;
import cn.apecode.blog.utils.CommonUtils;
import cn.apecode.blog.utils.IpUtils;
import cn.apecode.blog.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description: 操作日志切面
 * @author: apecode
 * @date: 2022-07-09 23:03
 **/
@Aspect
@Component
public class OptLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * @description: 设置操作日志切入点，记录操作日志，在注解的位置切入代码
     * @auther apecode
     * @date 2022/7/9 23:20
    */
    @Pointcut("@annotation(cn.apecode.blog.annotation.OptLog)")
    public void optLogPointCut() {}

    /**
     * @description: 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行，如果连接点抛出异常，则不会执行
     * @param joinPoint
     * @param keys
     * @auther apecode
     * @date 2022/7/9 23:06
    */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {
        // 获取requestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request =  (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        // 从切面织入点处通过反射机制获取织入点出的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        // 操作模块
        operationLog.setOptModule(api.tags()[0]);
        // 操作类型
        operationLog.setOptType(optLog.optType());
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
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // 返回结果
        operationLog.setResponseData(JSON.toJSONString(keys));
        // 请求用户id
        operationLog.setUserId(UserUtils.getLoginUser().getUserInfoId());
        // 请求用户
        operationLog.setUserId(UserUtils.getLoginUser().getUserInfoId());
        // 请求IP
        String ipAddress = IpUtils.getIpAddress(request);
        operationLog.setIdAddress(ipAddress);
        operationLog.setIpSource(IpUtils.getIpSource(ipAddress));
        // 请求URL
        operationLog.setOptUrl(request.getRequestURI());
        operationLog.setUpdateTime(CommonUtils.getLocalDateTime());
        operationLogMapper.insert(operationLog);
    }
}
