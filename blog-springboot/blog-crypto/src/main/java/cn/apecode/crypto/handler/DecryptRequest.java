package cn.apecode.crypto.handler;

import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.config.CryptoConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author apecode.
 * @description: 请求体解密
 * @date 2024/1/19 11:09
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class DecryptRequest extends RequestBodyAdviceAdapter {

    private final CryptoConfig cryptoConfig;
    private boolean isDecode;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        isDecode = (methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class)) && cryptoConfig.isOpen();
        return isDecode;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (isDecode) {
            return new DecryptHttpInputMessage(inputMessage, cryptoConfig);
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
