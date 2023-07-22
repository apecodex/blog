<p align=center>
  <a href="https://codeape.cn">
    <img src="https://codeape.cn/sleaves.svg" width="120" height="120" alt="apecode博客">
  </a>
</p>
<p align="center">
基于SpringBoot + vue3的前后端分离博客
</p>
<p align="center">
    <a href="https://codeape.cn" target="_blank">
        <img src="https://img.shields.io/badge/license-MIT%2FApache--2.0-blue"/>
        <img src="https://img.shields.io/badge/JDK-1.8%2B-green"/>
        <img src="https://img.shields.io/badge/springboot-2.7.0-brightgreen"/>
        <img src="https://img.shields.io/badge/vue-3.2.40-yellowgreen"/>
        <img src="https://img.shields.io/badge/pinia-2.0.22-orange"/>
        <img src="https://img.shields.io/badge/vite-3.1.4-blue"/>
        <img src="https://img.shields.io/badge/windcss-3.5.6-blue"/>
        <img src="https://img.shields.io/badge/naive--ui-2.32.2-orange"/>
        <img src="https://img.shields.io/badge/redis-6.2.7-green"/>
        <img src="https://img.shields.io/badge/mariadb-10.6.8-red"/>
        <img src="https://img.shields.io/badge/rabbitmq-3.9.18--management-blue"/>
        <img src="https://img.shields.io/badge/nginx-1.23.3-yellowgreen"/>
        <img src="https://img.shields.io/badge/mybatis--plus-3.5.2-yellow"/>
        <img src="https://img.shields.io/badge/anji--plus-1.3.0-red"/>
    </a>
</p>
<p align="center">
  <a href="#在线体验">在线体验</a> | <a href="#技术介绍">技术介绍</a> | <a href="#项目特点">项目特点</a> | <a href="#目录结构">目录结构</a> | <a href="#运行环境">运行环境</a> | <a href="#开发环境">开发环境</a> | <a href="#项目截图">项目截图</a> | <a href="#部署教程">部署教程</a> | <a href="#开发总结">开发总结</a> | <a href="#待开发功能">待开发功能</a>
</p>


## 在线体验

**首页链接：** [https://codeape.cn](https://codeape.cn)

**后台链接：** [https://admin.codeape.cn](https://admin.codeape.cn)



**测试账号：**`test@qq.com`

**测试密码：**`test123`

可登录后台查看。

## 技术介绍

### 后端

* SpringBoot
* SpringSecurity
* Redis
* RabbitMQ
* Mariadb
* MybatisPlus
* JWT
* sensitive-word
* anji-plus
* knife4j（Swagger3）
* WebSocket

### 前端

#### 前台

- vue3
- vue-router
- pinia
- vite
- axios
- windicss
- md-editor-v3
- icon-park/vue-next
- @kyvg/vue3-notification
- sockjs-client

#### 后台

- vue3
- vue-router
- pinia
- vite
- axios
- naive-ui
- windicss
- echarts
- md-editor-v3
- @icon-park/vue-next

### 其他

* 第三方QQ登录

### 部署

* Docker
* nginx

### 开发工具

IDEA、WebStorm、HeidiSQL、RDM、XShell、Xftp

## 项目特点

* 前台采用的是拟态风格，支持黑白主题切换。
* 后台用的是[Naive UI](https://www.naiveui.com/)，有暗黑及亮白两种主题，支持全局设置修改界面样式。
* 文章编辑采用Markdown，语法简洁。
* 评论加入了动态表情包，体验更加有趣。
* 图片懒加载显示，避免网路不畅导致图片加载时空白。
* 留言采用贴纸效果，可选择留言主题。
* 前后端分离部署。
* 用户申请友链，后台可控，用户可修改。
* 动态修改文章预览样式。
* 支持QQ登录和邮箱绑定，绑定后可互相登录。
* 支持发表说说，分享日常生活。
* 采用JWT进行用户授权，降低服务器查询数据库的次数。
* 支持搜索结果高亮。
* 上传图片（oss或本地）。
* 基于RBAC模型，动态修改用户权限，根据权限限制操作。
* AOP注解实现日志管理。
* 后台可修改轮播背景图片，博客配置等信息。
* 后端多模块设计，方便后期升级维护。
* 日志拆分记录，快速定位问题。
* 支持https配置
* 即时聊天室

## 目录结构

```sh
├─blog-api  # 接口模块，对外提供接口
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─cn
│      │  │      └─apecode
│      │  │          ├─aspect      # AOP
│      │  │          └─controller  # 控制层
├─blog-auth  # 登录认证模块
│  └─src
│      └─main
│          ├─java
│          │  └─cn
│          │      └─apecode
│          │          └─auth
│          │              ├─filter   # 过滤器
│          │              └─handler  # 处理器
├─blog-common  # 公共模块
│  └─src
│      └─main
│          ├─java
│          │  └─cn
│          │      └─apecode
│          │          └─common
│          │              ├─annotation   # 自定义注解
│          │              ├─constant     # 常量
│          │              ├─enums        # 枚举
│          │              ├─exception    # 自定义全局异常
│          │              └─utils        # 工具类
├─blog-config  # 配置模块
│  └─src
│      └─main
│          ├─java
│          │  └─cn
│          │      └─apecode
│          │          ├─config    # 配置类
│          │          └─handler   # 处理器
│          └─resources
│              └─META-INF
│                  └─services
├─blog-mail  # 邮件模块
│  └─src
│      └─main
│          ├─java
│          │  └─cn
│          │      └─apecode
│          │          └─mail
│          │              └─consumer    # MQ消费模块
├─blog-pojo  # 实体模块
│  └─src
│      └─main
│          ├─java
│          │  └─cn
│          │      └─apecode
│          │          ├─dto       # DTO
│          │          ├─entity    # 实体类
│          │          └─vo        # VO
└─blog-service  # 服务模块
    └─src
        └─main
            ├─java
            │  └─cn
            │      └─apecode
            │          ├─dto         # DTO
            │          ├─handler     # 处理器
            │          ├─mapper      # mapper
            │          ├─schedule    # 定时
            │          ├─service     # 服务层
            │          │  └─impl     # 服务层实现类
            │          ├─strategy    # 策略模式（登录、上传）
            │          └─utils       # 工具类
            └─resources
                └─mapper             # CURD
```

## 运行环境

| 名称     | 说明           |
| -------- | -------------- |
| 服务器   | 腾讯云2核4G    |
| CDN      | 阿里云全站加速 |
| 对象存储 | 阿里oss        |

## 开发环境

| 开发环境 | 版本   |
| -------- | ------ |
| JDK      | 1.8    |
| Mariadb  | 10.6.8 |
| Redis    | 6.2.7  |
| RabbitMQ | 3.9.18 |



## 项目截图

![1](https://apecode.oss-cn-chengdu.aliyuncs.com/article/0d1983d1fefa356ae214e886f5c685c2.png)

![2](https://apecode.oss-cn-chengdu.aliyuncs.com/article/d3bd777837b7f98bdfd013533889235f.png)



## 部署教程

**博客部署教程：**[https://codeape.cn/article/da02991d5130234a](https://codeape.cn/article/da02991d5130234a)

## 开发总结

此项目开发时间约半年，因为是边学边写，还要上课，过程中也学习了很多大佬的开发思路及方法，用的都是比较新的技术点。

## 待开发功能

- [ ] 音乐播放器

- [x] 右键菜单功能
