package cn.apecode.blog.handler;

import cn.apecode.blog.annotation.AccessLimit;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.utils.IpUtils;
import cn.apecode.blog.vo.ResponseCode;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static cn.apecode.blog.constant.CommonConst.APPLICATION_JSON;
import static cn.apecode.blog.enums.StatusCodeEnum.ACCESS_LIMIT;


/**
 * @description: 请求访问限制
 * @author: apecode
 * @date: 2022-12-24 20:58
 **/
public class RequestAccessLimit implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 判断该方法上是否有该注解
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (Objects.nonNull(accessLimit)) {
                long seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                // 生成redis key
                String key = IpUtils.getIpAddress(request) + handlerMethod.getMethod().getName();
                try {
                    // 自增1后返回
                    Long incrCount = redisService.incrExpire(key, seconds);
                    // 如果请求数大于设定的数，则判断为请求频繁
                    if (incrCount > maxCount) {
                        render(response, ResponseCode.customize(false, ACCESS_LIMIT));
                        return false;
                    }
                    return true;
                } catch (RedisConnectionFailureException e) {
                    return false;
                }
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, ResponseCode<String> responseCode) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(responseCode));
    }
}
