package cn.apecode.auth.handler;

import cn.apecode.common.utils.ResponseCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.apecode.common.constant.CommonConst.APPLICATION_JSON;
import static cn.apecode.common.enums.StatusCodeEnum.UNAUTHORIZED;

/**
 * @description: 用户未登录处理
 * @auther apecode
 * @date 2022/5/28 15:41
*/
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseCode.customize(false, UNAUTHORIZED)));
    }
}
