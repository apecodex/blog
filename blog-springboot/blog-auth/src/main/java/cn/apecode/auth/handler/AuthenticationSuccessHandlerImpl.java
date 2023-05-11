package cn.apecode.auth.handler;

import cn.apecode.common.enums.LoginTypeEnum;
import cn.apecode.common.utils.BeanCopyUtils;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.dto.IpSourceDto;
import cn.apecode.dto.UserInfoDto;
import cn.apecode.entity.LoginLog;
import cn.apecode.entity.UserAuth;
import cn.apecode.mapper.LoginLogMapper;
import cn.apecode.mapper.UserAuthMapper;
import cn.apecode.service.RedisService;
import cn.apecode.utils.IpUtils;
import cn.apecode.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static cn.apecode.common.constant.CommonConst.APPLICATION_JSON;
import static cn.apecode.common.constant.RedisPrefixConst.USER_CACHE;

/**
 * @description: 登录成功处理
 * @author: apecode
 * @date: 2022-05-28 15:59
 **/
@RequiredArgsConstructor
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserAuthMapper userAuthMapper;
    private final LoginLogMapper loginLogMapper;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserInfoDto userInfoDto = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoDto.class);
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseCode.ok(userInfoDto)));
        // 更新用户ip，最近登录时间及登录记录
        updateUserLoginInfo();
        // 删除Redis用户信息，以便下次获取数据时准确
        redisService.hDel(USER_CACHE, UserUtils.getLoginUser().getUsername());
    }

    /**
     * @description: 更新登录记录
     * @auther apecode
     * @date 2022/5/28 18:03
    */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLoginInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .loginType(LoginTypeEnum.EMAIL.getType())
                .ipAddress(UserUtils.getLoginUser().getIpAddress())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .browser(UserUtils.getLoginUser().getBrowser())
                .os(UserUtils.getLoginUser().getOs())
                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())
                .build();
        // 保存登录记录
        LoginLog loginLog = LoginLog.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .loginType(UserUtils.getLoginUser().getLoginType())
                .ipAddress(UserUtils.getLoginUser().getIpAddress())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .browser(UserUtils.getLoginUser().getBrowser())
                .os(UserUtils.getLoginUser().getOs())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        IpSourceDto ipSourceFromAmap = IpUtils.getIpSourceFromAmap(UserUtils.getLoginUser().getIpAddress());
        if (Objects.nonNull(ipSourceFromAmap)) {
            String source = IpUtils.cutProvince(ipSourceFromAmap);
            userAuth.setIpSource(source);
            loginLog.setIpSource(source);
            if (Objects.nonNull(ipSourceFromAmap.getLocation()) && !ipSourceFromAmap.getLocation().split(",")[0].equals("null")) {
                String location = ipSourceFromAmap.getLocation();
                userAuth.setRectangle(location);
                loginLog.setRectangle(location);
            }
        }
        userAuthMapper.updateById(userAuth);
        loginLogMapper.insert(loginLog);
    }
}
