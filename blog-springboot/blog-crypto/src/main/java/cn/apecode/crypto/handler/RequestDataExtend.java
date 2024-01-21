package cn.apecode.crypto.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apecode.
 * @description: 请求内容扩展
 * @date 2024/1/19 11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDataExtend {

    // 请求体密文
    private String data;

    // RSA私钥加密后的AES密文
    private String sym;
}
