import JSEncrypt from 'jsencrypt'
import { publicKey, privateKey } from '~/constant';

export default class RSAUtil {

  public static encryptByPublicKey(data: { key: string, keyIV: string }): string | boolean {
    let plaintext = JSON.stringify(data);
    const encryptor = new JSEncrypt()
    encryptor.setPublicKey(publicKey)
    return encryptor.encrypt(plaintext)
  }

  public static decryptByPrivateKey(content: string): string | boolean {
    const decrypt = new JSEncrypt();
    decrypt.setPrivateKey(privateKey);
    const decryptedContent = decrypt.decrypt(content);
    return decryptedContent;
  }
}