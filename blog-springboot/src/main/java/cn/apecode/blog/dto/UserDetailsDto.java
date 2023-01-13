package cn.apecode.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: 用户账号
 * @author: apecode
 * @date: 2022-05-27 01:33
 **/
@Data
@Builder
public class UserDetailsDto implements Serializable, UserDetails {

    private static final long serialVersionUID = 5836508892088533626L;

    @ApiModelProperty("用户账号id")
    private Integer id;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户信息id")
    private Integer userInfoId;

    @ApiModelProperty("登录类型")
    private Integer loginType;

    @ApiModelProperty("登录ip")
    private String ipAddress;

    @ApiModelProperty("ip来源")
    private String ipSource;

    @ApiModelProperty("是否启用（1是/0否）")
    private Boolean enable;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户唯一id")
    private String uid;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("文章点赞集合")
    private Set<Object> articleLikeSet;

    @ApiModelProperty("评论点赞集合")
    private Set<Object> commentLikeSet;

    @ApiModelProperty("说说点赞集合")
    private Set<Object> talkLikeSet;

    @ApiModelProperty("用户角色")
    private List<String> roleList;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty(value = "最后登录时间", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("Jwt Token")
    private String token;

    @ApiModelProperty("Token Head")
    private String tokenHead;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
