package cn.apecode.crypto.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apecode.
 * @description: 响应体内容扩展
 * @date 2024/1/19 0:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespDataExtend {

    // 响应体密文
    private Object body;

    // RSA私钥加密后的AES密文
    private String sym;

}
