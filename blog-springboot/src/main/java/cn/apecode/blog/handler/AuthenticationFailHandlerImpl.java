package cn.apecode.blog.handler;

import cn.apecode.blog.vo.ResponseCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.apecode.blog.constant.CommonConst.APPLICATION_JSON;

/**
 * @description: 登录失败处理
 * @author: apecode
 * @date: 2022-05-28 15:53
 **/
@Slf4j
@Component
public class AuthenticationFailHandlerImpl implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseCode.fail(exception.getMessage())));
    }
}
