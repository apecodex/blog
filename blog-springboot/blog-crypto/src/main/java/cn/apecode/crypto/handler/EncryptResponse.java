package cn.apecode.crypto.handler;

import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.crypto.config.CryptoConfig;
import cn.apecode.crypto.utils.AESUtil;
import cn.apecode.crypto.utils.RSAUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @author apecode.
 * @description: 响应体加密
 * @date 2024/1/18 23:25
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
public class EncryptResponse implements ResponseBodyAdvice<ResponseCode> {

    private final CryptoConfig cryptoConfig;
    private boolean isEncode;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        isEncode = returnType.hasMethodAnnotation(Encrypt.class) && cryptoConfig.isOpen();
        return isEncode;
    }

    @Override
    public ResponseCode<?> beforeBodyWrite(ResponseCode body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (isEncode) {
            String key = AESUtil.getKey();
            String keyIV = AESUtil.getKey();
            AESKey aesKey = AESKey.builder().key(key).keyIV(keyIV).build();
            if (body.getData() != null) {
                // 通过aes密钥和aes偏移量加密响应体
                String aesEncodeContent = AESUtil.encrypt(key, JSON.toJSONString(body.getData()), keyIV);
                // 通过rsa加密aes密钥
                String aesEncryptByPrivateKey = RSAUtil.encryptByPublicKey(JSON.toJSONString(aesKey), cryptoConfig.getRsaPublicKey());
                RespDataExtend dataExtend = RespDataExtend.builder()
                        .body(aesEncodeContent)
                        .sym(aesEncryptByPrivateKey)
                        .build();
                if (cryptoConfig.isShowLog()) {
                    log.info("加密前数据：{}，加密后：{}", JSON.toJSONString(body.getData()), aesEncodeContent);
                }
                body.setData(dataExtend);
            }
        }
        return body;

    }
}
