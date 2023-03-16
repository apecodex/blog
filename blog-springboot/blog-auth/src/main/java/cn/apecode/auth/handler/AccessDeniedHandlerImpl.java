package cn.apecode.auth.handler;

import cn.apecode.common.utils.ResponseCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.apecode.common.constant.CommonConst.APPLICATION_JSON;
import static cn.apecode.common.enums.StatusCodeEnum.AUTHORIZED;

/**
 * @description: 当访问接口没有权限时，自定义返回结果
 * @author: apecode
 * @date: 2022-05-28 19:21
 **/
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseCode.customize(false, AUTHORIZED)));
    }
}
