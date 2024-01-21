package cn.apecode.crypto.config;

import cn.apecode.crypto.utils.RSAUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@Getter
public class CryptoConfig {

    @Value("${encrypt.rsa.privateKey}")
    private String privateKey;

    @Value("${encrypt.rsa.publicKey}")
    private String publicKey;

    @Value("${encrypt.showLog}")
    private boolean showLog = false;

    @Value("${encrypt.open}")
    private boolean open = false;

    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;

    @PostConstruct
    private void loadRSAKey() {
        rsaPrivateKey = RSAUtil.getRSAPrivateKeyByString(privateKey);
        rsaPublicKey = RSAUtil.getRSAPublicKeyByString(publicKey);
    }
}
