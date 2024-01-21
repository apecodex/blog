package cn.apecode.crypto.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apecode.
 * @description: AES密钥
 * @date 2024/1/19 0:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AESKey {

    private String key;
    private String keyIV;
}
