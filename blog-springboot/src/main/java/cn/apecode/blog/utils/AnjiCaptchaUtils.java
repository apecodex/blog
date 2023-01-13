package cn.apecode.blog.utils;

import cn.apecode.blog.exception.BizException;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 滑动验证码
 * @author: apecode
 * @date: 2023-01-05 23:35
 **/
@Component
public class AnjiCaptchaUtils {

    @Autowired
    private CaptchaService captchaService;

    /**
     * @description: 检查滑动验证码是否通过
     * @param captchaVerification
     * @auther apecode
     * @date 2023/1/5 23:37
    */
    public void checkCaptcha(String captchaVerification) {
        if (StringUtils.isBlank(captchaVerification)) throw new BizException("请正确滑动验证码");
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            throw new BizException(response.getRepMsg());
        }
    }
}
