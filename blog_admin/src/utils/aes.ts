import CryptoJS from 'crypto-js';

const KEY_LENGTH: number = 16;

export default class AESUtil {

  public static getKey(): string {
    let uid = "";
    for (let i = 0; i < KEY_LENGTH; i++) {
      const randomNum = Math.floor(Math.random() * 3);
      switch (randomNum) {
        case 0:
          uid += Math.floor(Math.random() * 10).toString();
          break;
        case 1:
          uid += String.fromCharCode(Math.floor(Math.random() * 26) + 65);
          break;
        case 2:
          uid += String.fromCharCode(Math.floor(Math.random() * 26) + 97);
          break;
        default:
          break;
      }
    }
    return uid;
  }

  public static encrypt(key: string, content: string, keyIV: string): string | null {
    const secretKey = CryptoJS.enc.Utf8.parse(key);
    const iv = CryptoJS.enc.Utf8.parse(keyIV);
    const cipherText = CryptoJS.AES.encrypt(content, secretKey, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    });
    return cipherText.toString();
  }

  public static decrypt(key: string, content: string, keyIV: string): string | null {
    const secretKey = CryptoJS.enc.Utf8.parse(key);
    const iv = CryptoJS.enc.Utf8.parse(keyIV);
    const decryptedContent = CryptoJS.AES.decrypt(content, secretKey, {
      iv: iv,
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    });
    return decryptedContent.toString(CryptoJS.enc.Utf8);
  }
}