-- --------------------------------------------------------
-- 主机:                           192.168.1.17
-- 服务器版本:                        10.6.8-MariaDB-1:10.6.8+maria~focal - mariadb.org binary distribution
-- 服务器操作系统:                      debian-linux-gnu
-- HeidiSQL 版本:                  12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 blog 的数据库结构
CREATE DATABASE IF NOT EXISTS `blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `blog`;

-- 导出  表 blog.t_article 结构
CREATE TABLE IF NOT EXISTS `t_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `categoryId` int(11) DEFAULT NULL COMMENT '分类id',
  `articleTitle` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `articleCover` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '文章缩略图',
  `articleContent` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文章内容',
  `type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '文章类型(1原创/2转载/3翻译)',
  `originalUrl` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原文链接',
  `isTop` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶(0否/1是)',
  `status` tinyint(1) NOT NULL DEFAULT 2 COMMENT '状态值(1公开/2私密/3草稿)',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `article_ibfk_1` (`userId`),
  KEY `article_ibfk_2` (`categoryId`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`),
  CONSTRAINT `article_ibfk_2` FOREIGN KEY (`categoryId`) REFERENCES `t_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章信息';

-- 正在导出表  blog.t_article 的数据：~1 rows (大约)
INSERT INTO `t_article` (`id`, `userId`, `categoryId`, `articleTitle`, `articleCover`, `articleContent`, `type`, `originalUrl`, `isTop`, `status`, `isDelete`, `createTime`, `updateTime`) VALUES
	(1, 1, 1, 'hi,this is my first article', 'https://apecode.oss-cn-chengdu.aliyuncs.com/article/6a499a0f172c53febe85fe1811731e44.jpg', '这是我的第一篇文章。\n😘', 1, NULL, 1, 1, 0, '2022-12-18 02:48:30', '2022-12-18 03:22:18');

-- 导出  表 blog.t_article_tag 结构
CREATE TABLE IF NOT EXISTS `t_article_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `articleId` int(11) NOT NULL COMMENT '文章id',
  `tagId` int(11) NOT NULL COMMENT '标签id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `article_tag_ibfk_1` (`articleId`),
  KEY `article_tag_ibfk_2` (`tagId`),
  CONSTRAINT `article_tag_ibfk_1` FOREIGN KEY (`articleId`) REFERENCES `t_article` (`id`),
  CONSTRAINT `article_tag_ibfk_2` FOREIGN KEY (`tagId`) REFERENCES `t_tag` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章所属标签';

-- 正在导出表  blog.t_article_tag 的数据：~1 rows (大约)
INSERT INTO `t_article_tag` (`id`, `articleId`, `tagId`, `createTime`, `updateTime`) VALUES
	(1, 1, 1, '2022-12-18 02:48:30', '2022-12-18 02:48:30');

-- 导出  表 blog.t_category 结构
CREATE TABLE IF NOT EXISTS `t_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章分类';

-- 正在导出表  blog.t_category 的数据：~2 rows (大约)
INSERT INTO `t_category` (`id`, `name`, `createTime`, `updateTime`) VALUES
	(1, '生活', '2022-12-18 02:48:30', '2022-12-18 02:48:30');

-- 导出  表 blog.t_comment 结构
CREATE TABLE IF NOT EXISTS `t_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `topicId` int(11) NOT NULL COMMENT '主题id',
  `commentContent` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `parentId` int(11) DEFAULT NULL COMMENT '父评论id',
  `type` tinyint(1) NOT NULL COMMENT '评论类型(1文章/2说说)',
  `replyUserId` int(11) DEFAULT NULL COMMENT '回复用户id',
  `replyCommentId` int(11) DEFAULT NULL COMMENT '回复评论id',
  `rectangle` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '经纬度',
  `ipAddress` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论IP',
  `ipSource` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '评论来源',
  `browser` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '访问浏览器',
  `os` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '操作系统',
  `isReview` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否审核(0否/1是)',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除(0不删除/1删除)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `comment_ibfk_1` (`userId`),
  KEY `comment_ibfk_2` (`replyUserId`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`),
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`replyUserId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章评论';

-- 正在导出表  blog.t_comment 的数据：~0 rows (大约)

-- 导出  表 blog.t_daily_visit 结构
CREATE TABLE IF NOT EXISTS `t_daily_visit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `viewsCount` int(11) NOT NULL COMMENT '访问量',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日均访问量';

-- 正在导出表  blog.t_daily_visit 的数据：~0 rows (大约)

-- 导出  表 blog.t_friend_link 结构
CREATE TABLE IF NOT EXISTS `t_friend_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `linkName` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '友链名称',
  `linkAvatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '友链头像',
  `linkIntro` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '友链简介',
  `linkUrl` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '友链地址',
  `isReview` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否审核(0否/1是)',
  `remark` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `friends_link_ibfk_1` (`userId`),
  CONSTRAINT `friend_link_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='友情链接';

-- 正在导出表  blog.t_friend_link 的数据：~0 rows (大约)

-- 导出  表 blog.t_login_log 结构
CREATE TABLE IF NOT EXISTS `t_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `loginType` tinyint(1) NOT NULL COMMENT '登录类型',
  `ipAddress` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录ip',
  `ipSource` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP来源',
  `rectangle` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '经纬度',
  `browser` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '浏览器信息',
  `os` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作系统',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `login_log_ibfk_1` (`userId`),
  CONSTRAINT `login_log_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志';

-- 正在导出表  blog.t_login_log 的数据：~0 rows (大约)

-- 导出  表 blog.t_login_qq 结构
CREATE TABLE IF NOT EXISTS `t_login_qq` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `openId` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户openId',
  `userAuthId` int(11) NOT NULL COMMENT '用户信息id',
  PRIMARY KEY (`id`),
  KEY `FK_t_login_qq_t_user_auth` (`userAuthId`) USING BTREE,
  CONSTRAINT `FK_t_login_qq_t_user_auth` FOREIGN KEY (`userAuthId`) REFERENCES `t_user_auth` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='QQ登录';

-- 正在导出表  blog.t_login_qq 的数据：~0 rows (大约)

-- 导出  表 blog.t_mail_log 结构
CREATE TABLE IF NOT EXISTS `t_mail_log` (
  `msgId` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息id',
  `email` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '接收邮箱',
  `type` tinyint(1) NOT NULL COMMENT '邮件发送类型(1验证码/2评论/3通知/4留言)',
  `topicId` int(11) DEFAULT NULL COMMENT '主题id',
  `status` int(1) DEFAULT NULL COMMENT '状态（0:消息投递中 1:投递成功 2:投递失败）',
  `routeKey` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由键',
  `exchange` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交换机',
  `count` int(1) DEFAULT NULL COMMENT '重试次数',
  `tryTime` datetime DEFAULT NULL COMMENT '重试时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `msgId` (`msgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件发送记录';

-- 正在导出表  blog.t_mail_log 的数据：~0 rows (大约)

-- 导出  表 blog.t_menu 结构
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '后台菜单id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单标识',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单路径',
  `component` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组件',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `parentId` int(11) DEFAULT NULL COMMENT '父id',
  `orderNum` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `isEnable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COMMENT='后台菜单';

-- 正在导出表  blog.t_menu 的数据：~30 rows (大约)
INSERT INTO `t_menu` (`id`, `name`, `title`, `path`, `component`, `icon`, `parentId`, `orderNum`, `isEnable`, `createTime`, `updateTime`) VALUES
	(1, 'HomeMenu', '首页', '/', '/home/Home.vue', 'dashboard', NULL, 1, 1, '2022-04-15 23:35:13', '2022-09-24 16:16:27'),
	(2, 'ArticleSubMenu', '文章管理', '/article-submenu', 'Layout', 'BookOne', NULL, 2, 1, '2022-04-16 00:00:37', '2022-09-24 22:12:32'),
	(3, 'MessageSubMenu', '消息管理', '/message-submenu', 'Layout', 'message', NULL, 3, 1, '2022-04-16 00:02:35', '2022-09-24 22:12:26'),
	(4, 'RecordsSubMenu', '说说管理', '/talk-submenu', 'Layout', 'sunny', NULL, 4, 1, '2022-04-16 00:10:59', '2022-04-16 00:11:00'),
	(5, 'UsersSubMenu', '用户管理', '/users-submenu', 'Layout', 'data-user', NULL, 5, 1, '2022-04-16 00:17:59', '2022-04-16 00:17:59'),
	(6, 'AlbumSubMenu', '相册管理', '/album-submenu', 'Layout', 'photograph', NULL, 6, 1, '2022-04-16 00:22:37', '2022-04-16 00:22:37'),
	(7, 'PermissionSubMenu', '权限管理', '/permission-submenu', 'Layout', 'permissions', NULL, 7, 1, '2022-04-16 00:24:59', '2022-04-16 00:24:59'),
	(8, 'SystemSubMenu', '系统管理', '/system-submenu', 'Layout', 'system', NULL, 8, 1, '2022-04-16 00:32:00', '2022-04-16 00:32:01'),
	(9, 'LogSubMenu', '日志管理', '/log-submenu', 'Layout', 'log', NULL, 9, 1, '2022-04-16 00:42:47', '2022-04-16 00:42:47'),
	(10, 'PersonalMenu', '个人中心', '/personal', '/personal/Personal.vue', 'user', NULL, 10, 1, '2022-04-16 00:51:49', '2022-09-25 09:38:05'),
	(11, 'ArticlesMenu', '发布文章', '/articles/:articleId?', '/article/Article.vue', 'write', 2, 1, 1, '2022-04-16 00:57:49', '2022-09-24 15:45:28'),
	(13, 'ArticleListMenu', '文章列表', '/article-list', '/article/ArticleList.vue', 'list-top', 2, 2, 1, '2022-04-16 00:59:26', '2022-09-24 22:04:06'),
	(14, 'CategoriesMenu', '分类管理', '/categories', '/category/Category.vue', 'category-management', 2, 3, 1, '2022-04-16 01:01:02', '2022-09-24 22:04:15'),
	(15, 'TagsMenu', '标签管理', '/tags', '/tag/Tag.vue', 'tag', 2, 4, 1, '2022-04-16 01:05:27', '2022-09-24 22:04:21'),
	(16, 'CommentsMenu', '评论管理', '/comments', '/comment/Comment.vue', 'comments', 3, 1, 1, '2022-04-16 01:06:23', '2022-04-16 01:06:23'),
	(17, 'MessagesMenu', '留言管理', '/messages', '/message/Message.vue', 'communication', 3, 2, 1, '2022-04-16 01:07:19', '2022-04-16 01:07:20'),
	(18, 'UsersMenu', '用户列表', '/users', '/users/Users.vue', 'every-user', 5, 1, 1, '2022-04-16 01:08:48', '2022-04-16 01:08:49'),
	(19, 'AlbumsMenu', '相册列表', '/albums/:albumId?', '/album/Album.vue', 'picture', 6, 1, 1, '2022-04-16 01:09:59', '2022-04-16 01:09:59'),
	(21, 'PhotosDeleteMenu', '图片回收站', '/photos/delete', '/album/Delete.vue', 'recycle-bin', 6, 2, 1, '2022-04-16 01:16:05', '2022-09-24 22:05:57'),
	(22, 'RolesMenu', '角色管理', '/roles', '/role/Role.vue', 'level', 7, 1, 1, '2022-04-16 01:18:29', '2022-09-24 22:12:44'),
	(23, 'ResourcesMenu', '接口管理', '/resources', '/resource/Resource.vue', 'api', 7, 2, 1, '2022-04-16 01:19:49', '2022-04-16 01:19:50'),
	(24, 'MenusMenu', '菜单管理', '/menus', '/menu/Menu.vue', 'application-menu', 7, 3, 1, '2022-04-16 01:26:14', '2022-04-16 01:26:14'),
	(25, 'LinksMenu', '友链管理', '/links', '/friendLink/FriendLink.vue', 'link-one', 8, 2, 1, '2022-04-16 01:28:11', '2022-09-24 22:24:30'),
	(26, 'WebsiteMenu', '网站管理', '/website', '/website/Website.vue', 'setting', 8, 1, 1, '2022-04-16 01:29:07', '2022-09-24 22:24:38'),
	(28, 'AboutMenu', '关于我', '/about', '/about/About.vue', 'book-open', 8, 3, 1, '2022-04-16 01:33:40', '2022-09-25 10:59:28'),
	(29, 'OperationLogMenu', '操作日志', '/operation/log', '/log/Operation.vue', 'optional', 9, 1, 1, '2022-04-16 01:35:15', '2022-04-16 01:35:16'),
	(30, 'LoginLogMenu', '登录日志', '/login/log', '/log/Login.vue', 'history', 9, 2, 1, '2022-04-16 01:39:05', '2022-04-16 01:39:05'),
	(31, 'MailLogMenu', '邮件日志', '/mail/log', '/log/Mail.vue', 'mail', 9, 3, 1, '2022-04-16 01:39:50', '2022-04-16 01:39:50'),
	(32, 'PostTalkMenu', '发布说说', '/talks/:talkId?', '/talk/Talk.vue', 'send', 4, 1, 1, '2022-04-16 01:41:20', '2022-04-16 01:41:21'),
	(33, 'TalkListMenu', '说说列表', '/talk-list', '/talk/TalkList.vue', 'view-grid-list', 4, 2, 1, '2022-04-16 01:50:07', '2022-04-16 01:50:08');

-- 导出  表 blog.t_message 结构
CREATE TABLE IF NOT EXISTS `t_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `theme` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'default' COMMENT '留言主题样式',
  `isReview` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否审核(0否/1是)',
  `ipAddress` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP地址',
  `ipSource` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP来源',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `message_ibfk_1` (`userId`),
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='留言';

-- 正在导出表  blog.t_message 的数据：~0 rows (大约)

-- 导出  表 blog.t_notice 结构
CREATE TABLE IF NOT EXISTS `t_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知id',
  `userId` int(11) DEFAULT NULL COMMENT '接收通知的用户id',
  `title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知标题',
  `content` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '通知内容',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '跳转的url',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已阅(0否/1是)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `notice_ibfk_1` (`userId`),
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知';

-- 正在导出表  blog.t_notice 的数据：~0 rows (大约)

-- 导出  表 blog.t_operation_log 结构
CREATE TABLE IF NOT EXISTS `t_operation_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `optModule` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作模块',
  `optType` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `optUrl` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作url',
  `optMethod` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作方法',
  `optDesc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作描述',
  `requestParam` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求参数',
  `requestMethod` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求方式',
  `responseData` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '返回数据',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `idAddress` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作ip',
  `ipSource` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作地址',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `operation_log_ibfk_1` (`userId`),
  CONSTRAINT `operation_log_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作记录';

-- 正在导出表  blog.t_operation_log 的数据：~0 rows (大约)

-- 导出  表 blog.t_photo_album 结构
CREATE TABLE IF NOT EXISTS `t_photo_album` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '相册id',
  `userId` int(11) NOT NULL COMMENT '用户Id',
  `albumName` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '相册名称',
  `albumDesc` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '相册描述',
  `albumCover` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '相册封面',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否公开(0否/1是)',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除(0不删除/1删除)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `album_ibfk_1` (`userId`),
  CONSTRAINT `album_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='相册';

-- 正在导出表  blog.t_photo_album 的数据：~0 rows (大约)

-- 导出  表 blog.t_picture 结构
CREATE TABLE IF NOT EXISTS `t_picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `userId` int(11) NOT NULL COMMENT '用户Id',
  `albumId` int(11) DEFAULT NULL COMMENT '相册id',
  `pictureName` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片名称',
  `pictureSrc` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片路径',
  `width` int(11) NOT NULL DEFAULT 0 COMMENT '图片宽度',
  `height` int(11) NOT NULL DEFAULT 0 COMMENT '图片高度',
  `size` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片大小',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除(0不删除/1删除)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `picture_ibfk_1` (`userId`),
  KEY `picture_ibfk_2` (`albumId`),
  CONSTRAINT `picture_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`),
  CONSTRAINT `picture_ibfk_2` FOREIGN KEY (`albumId`) REFERENCES `t_photo_album` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片';

-- 正在导出表  blog.t_picture 的数据：~1 rows (大约)

-- 导出  表 blog.t_resource 结构
CREATE TABLE IF NOT EXISTS `t_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源信息id',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '资源名称',
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源路径',
  `requestMethod` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求方式',
  `parentId` int(11) DEFAULT NULL COMMENT '父资源id',
  `isEnable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1是/0否）',
  `isAnonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否匿名访问（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源信息';

-- 正在导出表  blog.t_resource 的数据：~134 rows (大约)
INSERT INTO `t_resource` (`id`, `name`, `url`, `requestMethod`, `parentId`, `isEnable`, `isAnonymous`, `createTime`, `updateTime`) VALUES
	(1, '分类模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:41:39', '2022-09-25 10:49:56'),
	(2, '博客信息模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:42:01', '2022-04-16 10:42:02'),
	(3, '友链模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:42:14', '2022-04-16 10:42:15'),
	(4, '文章模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:42:23', '2022-04-16 10:42:24'),
	(5, '日志模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:42:42', '2022-04-16 10:42:43'),
	(6, '标签模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:42:50', '2022-04-16 10:42:50'),
	(7, '图片模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:43:44', '2022-04-16 10:43:44'),
	(8, '用户信息模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:43:53', '2022-04-16 10:43:54'),
	(9, '用户账号模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:44:03', '2022-04-16 10:44:04'),
	(10, '留言模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:44:50', '2022-04-16 10:44:50'),
	(11, '相册模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:45:21', '2022-04-16 10:45:21'),
	(12, '菜单模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:45:29', '2022-04-16 10:45:29'),
	(13, '角色模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:45:36', '2022-04-16 10:45:37'),
	(14, '评论模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:45:45', '2022-04-16 10:45:46'),
	(15, '资源模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:45:51', '2022-04-16 10:45:51'),
	(17, '说说模块', NULL, NULL, NULL, 1, 0, '2022-04-16 10:46:06', '2022-04-16 10:46:06'),
	(18, '获取后台分类列表', '/admin/categories', 'GET', 1, 1, 0, '2022-04-16 10:48:18', '2022-09-25 10:51:55'),
	(19, '添加或修改分类', '/admin/category', 'POST', 1, 1, 0, '2022-04-16 10:49:29', '2022-04-16 10:49:29'),
	(20, '删除分类', '/admin/category', 'DELETE', 1, 1, 0, '2022-04-16 10:50:04', '2022-04-16 10:50:05'),
	(21, '搜索文章分类', '/admin/category/search', 'GET', 1, 1, 0, '2022-04-16 10:50:58', '2022-04-16 10:50:58'),
	(22, '获取分类列表', '/categories', 'GET', 1, 1, 1, '2022-04-16 10:53:08', '2022-04-16 10:53:09'),
	(23, '获取博客信息', '/', 'GET', 2, 1, 1, '2022-04-16 10:53:45', '2022-09-23 16:18:00'),
	(24, '获取关于我信息', '/about', 'GET', 2, 1, 1, '2022-04-16 10:54:24', '2022-04-16 10:54:25'),
	(25, '获取后台信息', '/admin', 'GET', 2, 1, 0, '2022-04-16 10:54:47', '2022-04-16 10:54:47'),
	(26, '修改关于我信息', '/admin/about', 'PUT', 2, 1, 0, '2022-04-16 10:55:27', '2022-04-16 10:55:27'),
	(27, '获取网站配置', '/admin/website/config', 'GET', 2, 1, 0, '2022-04-16 10:56:00', '2022-04-16 10:56:00'),
	(28, '更新网站配置', '/admin/website/config', 'PUT', 2, 1, 0, '2022-04-16 10:56:30', '2022-04-16 10:56:31'),
	(29, '上传博客配置图片', '/admin/config/images', 'POST', 2, 1, 0, '2022-04-16 10:57:17', '2022-04-16 10:57:18'),
	(30, '获取后台友链列表', '/admin/links', 'GET', 3, 1, 0, '2022-04-16 10:57:48', '2022-04-16 10:57:48'),
	(31, '保存或修改友链', '/admin/link', 'POST', 3, 1, 0, '2022-04-16 10:58:26', '2022-04-16 10:58:26'),
	(32, '删除友链', '/admin/link/*', 'DELETE', 3, 1, 0, '2022-04-16 10:58:43', '2022-04-16 10:58:43'),
	(33, '获取友链列表', '/links', 'GET', 3, 1, 1, '2022-04-16 10:59:08', '2022-04-16 10:59:08'),
	(34, '用户保存或修改友链', '/user/link', 'POST', 3, 1, 0, '2022-04-16 10:59:33', '2022-04-16 10:59:33'),
	(35, '用户删除友链', '/user/link', 'DELETE', 3, 1, 0, '2022-04-16 11:00:13', '2022-04-16 11:00:14'),
	(36, '根据用户id获取用户友链', '/user/link', 'GET', 3, 1, 0, '2022-04-16 11:01:49', '2022-04-16 11:01:49'),
	(37, '审核友链', '/admin/links', 'PUT', 3, 1, 0, '2022-04-16 11:02:26', '2022-04-16 11:02:26'),
	(38, '获取后台文章', '/admin/articles', 'GET', 4, 1, 0, '2022-04-16 11:04:13', '2022-04-16 11:04:13'),
	(39, '添加或修改文章', '/admin/article', 'POST', 4, 1, 0, '2022-04-16 11:10:02', '2022-04-16 11:10:02'),
	(40, '恢复或删除文章', '/admin/article', 'PUT', 4, 1, 0, '2022-04-16 11:11:23', '2022-04-16 11:11:23'),
	(41, '物理删除文章', '/admin/article', 'DELETE', 4, 1, 0, '2022-04-16 11:11:56', '2022-04-16 11:11:56'),
	(42, '上传文章封面', '/admin/article/cover', 'POST', 4, 1, 0, '2022-04-16 11:12:45', '2022-04-16 11:12:46'),
	(43, '修改文章置顶', '/admin/article/top', 'PUT', 4, 1, 0, '2022-04-16 11:14:47', '2022-04-16 11:14:47'),
	(44, '根据id获取后台文章', '/admin/article/*', 'GET', 4, 1, 0, '2022-04-16 11:17:15', '2022-04-16 11:17:15'),
	(45, '获取首页文章', '/articles', 'GET', 4, 1, 1, '2022-04-16 11:17:37', '2022-04-16 11:17:37'),
	(46, '获取文章归档', '/article/archives', 'GET', 4, 1, 1, '2022-04-16 11:18:38', '2022-04-16 11:18:38'),
	(48, '搜索', '/search', 'GET', 4, 1, 1, '2022-04-16 11:20:11', '2022-04-16 11:20:11'),
	(49, '根据id获取文章', '/article/*', 'GET', 4, 1, 1, '2022-04-16 11:20:31', '2022-04-16 11:20:31'),
	(50, '点赞文章', '/articles/*/like', 'POST', 4, 1, 0, '2022-04-16 11:21:02', '2022-04-16 11:21:03'),
	(52, '获取操作日志', '/admin/operation/logs', 'GET', 5, 1, 0, '2022-04-16 11:23:06', '2022-04-16 11:23:06'),
	(53, '删除操作日志', '/admin/operation/logs', 'DELETE', 5, 1, 0, '2022-04-16 11:23:24', '2022-04-16 11:23:24'),
	(54, '获取登录日志', '/admin/login/logs', 'GET', 5, 1, 0, '2022-04-16 11:24:24', '2022-04-16 11:24:25'),
	(55, '删除登录日志', '/admin/login/logs', 'DELETE', 5, 1, 0, '2022-04-16 11:24:47', '2022-04-16 11:24:48'),
	(56, '获取邮件日志', '/admin/email/logs', 'GET', 5, 1, 0, '2022-04-16 11:25:05', '2022-04-16 11:25:06'),
	(57, '删除邮件日志', '/admin/email/logs', 'DELETE', 5, 1, 0, '2022-04-16 11:25:22', '2022-04-16 11:25:23'),
	(58, '查询后台标签列表', '/admin/tags', 'GET', 6, 1, 0, '2022-04-16 11:26:07', '2022-04-16 11:26:07'),
	(59, '添加标签', '/admin/tags', 'POST', 6, 1, 0, '2022-04-16 11:26:35', '2022-04-16 11:26:36'),
	(60, '删除标签', '/admin/tags', 'DELETE', 6, 1, 0, '2022-04-16 11:26:59', '2022-04-16 11:26:59'),
	(61, '搜索文章标签', '/tags/search', 'GET', 6, 1, 1, '2022-04-16 11:27:19', '2022-04-16 11:27:20'),
	(62, '查询标签列表', '/tags', 'GET', 6, 1, 1, '2022-04-16 11:28:03', '2022-04-16 11:28:04'),
	(63, '修改标签', '/admin/tags', 'PUT', 6, 1, 0, '2022-04-16 11:29:01', '2022-04-16 11:29:01'),
	(64, '根据相册路径获取图片列表（后台）', '/admin/pictures', 'GET', 7, 1, 0, '2022-04-16 11:29:49', '2022-04-16 11:29:49'),
	(65, '保存图片', '/admin/picture', 'POST', 7, 1, 0, '2022-04-16 11:30:22', '2022-04-16 11:30:23'),
	(66, '更新图片信息', '/admin/picture', 'PUT', 7, 1, 0, '2022-04-16 11:30:37', '2022-04-16 11:30:41'),
	(67, '删除图片', '/admin/picture', 'DELETE', 7, 1, 0, '2022-04-16 11:31:04', '2022-04-16 11:31:05'),
	(68, '移动图片相册', '/admin/pictures/album', 'PUT', 7, 1, 0, '2022-04-16 11:31:20', '2022-04-16 11:31:20'),
	(69, '更新图片删除状态', '/admin/pictures/delete', 'PUT', 7, 1, 0, '2022-04-16 11:31:39', '2022-04-16 11:31:40'),
	(70, '根据相册路径获取图片列表', '/album/*/pictures', 'GET', 7, 1, 1, '2022-04-16 11:31:58', '2022-04-16 11:31:59'),
	(71, '上传图片', '/admin/picture/upload', 'POST', 7, 1, 0, '2022-04-16 11:32:22', '2022-04-16 11:32:23'),
	(72, '获取回收站图片', '/admin/pictures/delete', 'GET', 7, 1, 0, '2022-04-16 11:32:41', '2022-04-16 11:32:41'),
	(73, '修改用户禁用状态', '/admin/user/enable', 'PUT', 9, 1, 0, '2022-04-16 11:33:48', '2022-04-16 11:33:48'),
	(74, '修改用户角色', '/admin/user/role', 'PUT', 8, 1, 0, '2022-04-16 11:34:12', '2022-04-16 11:34:12'),
	(75, '更新用户头像', '/user/avatar', 'POST', 8, 1, 0, '2022-04-16 11:34:27', '2022-04-16 11:34:28'),
	(76, '绑定用户邮箱', '/user/email/bind', 'POST', 8, 1, 0, '2022-04-16 11:34:56', '2022-04-16 11:34:56'),
	(77, '更新用户信息', '/user/info', 'PUT', 8, 1, 0, '2022-04-16 11:35:47', '2022-04-16 11:35:48'),
	(78, '获取用户信息', '/user/info', 'GET', 8, 1, 0, '2022-04-16 11:37:43', '2022-04-16 11:37:44'),
	(80, '获取后台用户列表', '/admin/users', 'GET', 9, 1, 0, '2022-04-16 11:38:57', '2022-04-16 11:38:57'),
	(81, '修改密码', '/user/password', 'PUT', 9, 1, 0, '2022-04-16 11:39:16', '2022-04-16 11:39:16'),
	(82, '用户注册', '/register', 'POST', 9, 1, 1, '2022-04-16 11:39:35', '2022-04-16 11:39:35'),
	(83, '发送邮箱验证码', '/user/code', 'GET', 9, 1, 1, '2022-04-16 11:39:56', '2022-04-16 11:39:56'),
	(84, '找回密码', '/findPassword', 'PUT', 9, 1, 1, '2022-04-16 11:40:17', '2022-04-16 11:40:18'),
	(86, 'qq登录', '/oauth/qq/callback', 'POST', 9, 1, 1, '2022-04-16 11:41:10', '2022-04-16 11:41:10'),
	(87, '获取后台留言列表', '/admin/messages', 'GET', 10, 1, 0, '2022-04-16 11:41:58', '2022-04-16 11:41:59'),
	(88, '删除留言', '/admin/message', 'DELETE', 10, 1, 0, '2022-04-16 11:42:55', '2022-04-16 11:42:55'),
	(89, '审核留言', '/admin/message', 'PUT', 10, 1, 0, '2022-04-16 11:43:13', '2022-04-16 11:43:13'),
	(90, '获取留言列表', '/messages', 'GET', 10, 1, 1, '2022-04-16 11:43:34', '2022-04-16 11:43:35'),
	(91, '添加留言', '/message', 'POST', 10, 1, 1, '2022-04-16 11:43:57', '2022-04-16 11:43:57'),
	(92, '获取后台相册列表', '/admin/albums', 'GET', 11, 1, 0, '2022-04-16 11:44:35', '2022-04-16 11:44:35'),
	(93, '添加相册', '/admin/album', 'POST', 11, 1, 0, '2022-04-16 11:45:03', '2022-04-16 11:45:03'),
	(94, '上传相册封面', '/admin/album/cover', 'POST', 11, 1, 0, '2022-04-16 11:45:29', '2022-04-16 11:45:29'),
	(95, '根据id删除相册', '/admin/picture/album/*', 'DELETE', 11, 1, 0, '2022-04-16 11:45:52', '2022-04-16 11:45:52'),
	(97, '获取相册列表', '/albums', 'GET', 11, 1, 1, '2022-04-16 11:46:50', '2022-04-16 11:46:51'),
	(98, '修改相册', '/admin/album', 'PUT', 11, 1, 0, '2022-04-16 11:47:22', '2022-04-16 11:47:23'),
	(99, '获取菜单列表', '/admin/menus', 'GET', 12, 1, 0, '2022-04-16 11:47:56', '2022-04-16 11:47:56'),
	(100, '保存或更新菜单', '/admin/menu', 'POST', 12, 1, 0, '2022-04-16 11:48:14', '2022-04-16 11:48:14'),
	(101, '删除菜单', '/admin/menu/*', 'DELETE', 12, 1, 0, '2022-04-16 11:48:28', '2022-04-16 11:48:29'),
	(102, '获取角色菜单选项', '/admin/role/menus', 'GET', 12, 1, 0, '2022-04-16 11:48:48', '2022-04-16 11:48:49'),
	(103, '获取当前用户菜单', '/admin/user/menus', 'GET', 12, 1, 0, '2022-04-16 11:49:06', '2022-04-16 11:49:07'),
	(104, '保存或更新角色', '/admin/role', 'POST', 13, 1, 0, '2022-04-16 11:49:37', '2022-04-16 11:49:38'),
	(105, '查询角色列表', '/admin/roles', 'GET', 13, 1, 0, '2022-04-16 11:49:56', '2022-04-16 11:49:56'),
	(106, '删除角色', '/admin/role', 'DELETE', 13, 1, 0, '2022-04-16 11:50:13', '2022-04-16 11:50:13'),
	(107, '查询用户角色选项', '/admin/user/roles', 'GET', 13, 1, 0, '2022-04-16 11:50:32', '2022-04-16 11:50:33'),
	(108, '查询后台评论', '/admin/comments', 'GET', 14, 1, 0, '2022-04-16 11:53:17', '2022-04-16 11:53:18'),
	(109, '删除评论', '/admin/comment', 'DELETE', 14, 1, 0, '2022-04-16 11:53:37', '2022-04-16 11:53:37'),
	(110, '审核评论', '/admin/comment', 'PUT', 14, 1, 0, '2022-04-16 11:54:37', '2022-04-16 11:54:38'),
	(111, '查询评论', '/comments', 'GET', 14, 1, 1, '2022-04-16 11:55:07', '2022-04-16 11:55:08'),
	(112, '添加评论', '/comment', 'POST', 14, 1, 0, '2022-04-16 11:55:25', '2022-04-16 11:55:26'),
	(113, '查询评论下的回复', '/comment/*/replies', 'GET', 14, 1, 1, '2022-04-16 11:56:00', '2022-04-16 11:55:59'),
	(114, '获取资源列表', '/admin/resources', 'GET', 15, 1, 0, '2022-04-16 11:56:55', '2022-04-16 11:56:55'),
	(115, '新增或修改资源', '/admin/resource', 'POST', 15, 1, 0, '2022-04-16 11:57:12', '2022-04-16 11:57:13'),
	(116, '删除资源', '/admin/resource/*', 'DELETE', 15, 1, 0, '2022-04-16 11:58:52', '2022-04-16 11:58:52'),
	(117, '获取角色资源选项', '/admin/role/resources', 'GET', 15, 1, 0, '2022-04-16 11:59:13', '2022-04-16 11:59:14'),
	(123, '查看说说列表', '/talks', 'GET', 17, 1, 1, '2022-04-16 12:02:51', '2022-04-16 12:02:51'),
	(124, '根据id查看说说', '/talk/*', 'GET', 17, 1, 1, '2022-04-16 12:03:09', '2022-04-16 12:03:10'),
	(125, '点赞说说', '/user/talk/*/like', 'POST', 17, 1, 0, '2022-04-16 12:03:26', '2022-04-16 12:03:26'),
	(127, '保存或修改说说', '/admin/talk', 'POST', 17, 1, 0, '2022-04-16 12:04:17', '2022-04-16 12:04:17'),
	(128, '删除说说', '/admin/talk', 'DELETE', 17, 1, 0, '2022-04-16 12:05:20', '2022-04-16 12:05:21'),
	(129, '查看后台说说', '/admin/talks', 'GET', 17, 1, 0, '2022-04-16 12:05:46', '2022-04-16 12:05:46'),
	(132, '点赞评论', '/comment/*/like', 'POST', 14, 1, 0, '2022-07-09 00:55:51', '2022-07-09 00:55:51'),
	(133, '物理删除图片', '/admin/pictures/delete', 'DELETE', 7, 1, 0, '2022-07-15 13:19:16', '2022-07-15 13:19:16'),
	(134, '消息通知模块', NULL, NULL, NULL, 1, 0, '2022-07-15 16:28:13', '2022-07-15 16:28:13'),
	(135, '发送通知', '/admin/notice', 'POST', 134, 1, 0, '2022-07-15 16:30:08', '2022-07-15 16:30:08'),
	(136, '更新通知已阅', '/user/notice', 'PUT', 134, 1, 0, '2022-07-15 16:31:29', '2022-07-15 16:31:29'),
	(137, '删除通知', '/admin/notice', 'DELETE', 134, 1, 0, '2022-07-15 16:33:57', '2022-07-15 16:33:58'),
	(138, '根据id获取通知', '/user/notice/*', 'GET', 134, 1, 0, '2022-07-15 16:37:30', '2022-07-15 16:37:30'),
	(139, '获取用户通知列表', '/user/notice', 'GET', 134, 1, 0, '2022-07-15 16:39:05', '2022-07-15 16:39:06'),
	(140, '获取后台通知列表', '/admin/notice', 'GET', 134, 1, 0, '2022-07-15 16:48:37', '2022-07-15 16:48:38'),
	(141, '获取用户未阅通知数量', '/user/notice/noread', 'GET', 134, 1, 0, '2022-07-17 01:23:40', '2022-07-17 01:23:41'),
	(143, '上传说说配图', '/admin/talk/upload', 'POST', 17, 1, 0, '2022-09-11 16:21:15', '2022-09-11 16:21:16'),
	(144, '根据id获取后台说说', '/admin/talk/*', 'GET', 17, 1, 0, '2022-09-12 14:35:28', '2022-09-12 14:35:28'),
	(145, '修改说说顶置', '/admin/talk/top', 'PUT', 17, 1, 0, '2022-09-13 13:32:11', '2022-09-13 13:32:11'),
	(160, '根据搜索条件获取文章', '/article/search/condition', 'GET', 4, 1, 1, '2022-10-16 16:51:35', '2022-10-16 16:51:35'),
	(161, '解绑用户邮箱', '/user/email/unbind', 'PUT', 8, 1, 0, '2023-01-06 01:21:20', '2023-01-06 01:21:20'),
	(162, '绑定QQ', '/user/qq/bind', 'POST', 8, 1, 0, '2023-01-06 11:04:13', '2023-01-06 11:04:14'),
	(163, '解绑QQ', '/user/qq/unbind', 'PUT', 8, 1, 0, '2023-01-06 11:07:05', '2023-01-06 11:07:05');

-- 导出  表 blog.t_role 结构
CREATE TABLE IF NOT EXISTS `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `roleAuth` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色权限',
  `roleDesc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `isEnable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='角色';

-- 正在导出表  blog.t_role 的数据：~3 rows (大约)
INSERT INTO `t_role` (`id`, `roleAuth`, `roleDesc`, `isEnable`, `createTime`, `updateTime`) VALUES
	(1, 'admin', '管理员', 1, '2022-04-15 23:12:27', '2022-09-24 20:07:34'),
	(2, 'user', '用户', 1, '2022-04-15 23:16:02', '2022-10-03 16:27:53'),
	(3, 'test', '测试', 1, '2022-04-15 23:16:32', '2022-11-16 15:24:16');

-- 导出  表 blog.t_role_menu 结构
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单角色权限id',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `menuId` int(11) NOT NULL COMMENT '菜单id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `role_menu_ibfk_1` (`roleId`),
  KEY `role_menu_ibfk_2` (`menuId`),
  CONSTRAINT `role_menu_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`),
  CONSTRAINT `role_menu_ibfk_2` FOREIGN KEY (`menuId`) REFERENCES `t_menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单角色权限';

-- 正在导出表  blog.t_role_menu 的数据：~60 rows (大约)
INSERT INTO `t_role_menu` (`id`, `roleId`, `menuId`, `createTime`, `updateTime`) VALUES
	(1, 1, 1, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(2, 1, 2, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(3, 1, 3, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(4, 1, 4, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(5, 1, 5, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(6, 1, 6, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(7, 1, 7, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(8, 1, 8, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(9, 1, 9, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(10, 1, 10, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(11, 1, 11, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(12, 1, 13, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(13, 1, 14, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(14, 1, 15, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(15, 1, 16, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(16, 1, 17, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(17, 1, 18, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(18, 1, 19, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(19, 1, 21, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(20, 1, 22, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(21, 1, 23, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(22, 1, 24, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(23, 1, 25, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(24, 1, 26, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(25, 1, 28, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(26, 1, 29, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(27, 1, 30, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(28, 1, 31, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(29, 1, 32, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(30, 1, 33, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(31, 3, 1, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(32, 3, 2, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(33, 3, 3, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(34, 3, 4, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(35, 3, 5, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(36, 3, 6, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(37, 3, 7, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(38, 3, 8, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(39, 3, 9, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(40, 3, 10, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(41, 3, 11, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(42, 3, 13, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(43, 3, 14, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(44, 3, 15, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(45, 3, 16, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(46, 3, 17, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(47, 3, 18, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(48, 3, 19, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(49, 3, 21, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(50, 3, 22, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(51, 3, 23, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(52, 3, 24, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(53, 3, 25, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(54, 3, 26, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(55, 3, 28, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(56, 3, 29, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(57, 3, 30, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(58, 3, 31, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(59, 3, 33, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(60, 3, 32, '2022-11-16 15:24:16', '2022-11-16 15:24:16');

-- 导出  表 blog.t_role_resource 结构
CREATE TABLE IF NOT EXISTS `t_role_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `resourceId` int(11) NOT NULL COMMENT '资源id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `role_resource_ibfk_1` (`roleId`),
  KEY `role_resource_ibfk_2` (`resourceId`),
  CONSTRAINT `role_resource_ibfk_1` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`),
  CONSTRAINT `role_resource_ibfk_2` FOREIGN KEY (`resourceId`) REFERENCES `t_resource` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=227 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源角色权限';

-- 正在导出表  blog.t_role_resource 的数据：~226 rows (大约)
INSERT INTO `t_role_resource` (`id`, `roleId`, `resourceId`, `createTime`, `updateTime`) VALUES
	(1, 1, 1, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(2, 1, 2, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(3, 1, 3, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(4, 1, 4, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(5, 1, 5, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(6, 1, 6, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(7, 1, 7, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(8, 1, 8, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(9, 1, 9, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(10, 1, 10, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(11, 1, 11, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(12, 1, 12, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(13, 1, 13, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(14, 1, 14, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(15, 1, 15, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(16, 1, 17, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(17, 1, 18, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(18, 1, 19, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(19, 1, 20, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(20, 1, 21, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(21, 1, 22, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(22, 1, 23, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(23, 1, 24, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(24, 1, 25, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(25, 1, 26, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(26, 1, 27, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(27, 1, 28, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(28, 1, 29, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(29, 1, 30, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(30, 1, 31, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(31, 1, 32, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(32, 1, 33, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(33, 1, 34, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(34, 1, 35, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(35, 1, 36, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(36, 1, 37, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(37, 1, 38, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(38, 1, 39, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(39, 1, 40, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(40, 1, 41, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(41, 1, 42, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(42, 1, 43, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(43, 1, 44, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(44, 1, 45, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(45, 1, 46, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(46, 1, 48, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(47, 1, 49, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(48, 1, 50, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(49, 1, 52, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(50, 1, 53, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(51, 1, 54, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(52, 1, 55, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(53, 1, 56, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(54, 1, 57, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(55, 1, 58, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(56, 1, 59, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(57, 1, 60, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(58, 1, 61, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(59, 1, 62, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(60, 1, 63, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(61, 1, 64, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(62, 1, 65, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(63, 1, 66, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(64, 1, 67, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(65, 1, 68, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(66, 1, 69, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(67, 1, 70, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(68, 1, 71, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(69, 1, 72, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(70, 1, 73, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(71, 1, 74, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(72, 1, 75, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(73, 1, 76, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(74, 1, 77, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(75, 1, 78, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(76, 1, 80, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(77, 1, 81, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(78, 1, 82, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(79, 1, 83, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(80, 1, 84, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(81, 1, 86, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(82, 1, 87, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(83, 1, 88, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(84, 1, 89, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(85, 1, 90, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(86, 1, 91, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(87, 1, 92, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(88, 1, 93, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(89, 1, 94, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(90, 1, 95, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(91, 1, 97, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(92, 1, 98, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(93, 1, 99, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(94, 1, 100, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(95, 1, 101, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(96, 1, 102, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(97, 1, 103, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(98, 1, 104, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(99, 1, 105, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(100, 1, 106, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(101, 1, 107, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(102, 1, 108, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(103, 1, 109, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(104, 1, 110, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(105, 1, 111, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(106, 1, 112, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(107, 1, 113, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(108, 1, 114, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(109, 1, 115, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(110, 1, 116, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(111, 1, 117, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(112, 1, 123, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(113, 1, 124, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(114, 1, 125, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(115, 1, 127, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(116, 1, 128, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(117, 1, 129, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(118, 1, 132, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(119, 1, 133, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(120, 1, 134, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(121, 1, 135, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(122, 1, 136, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(123, 1, 137, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(124, 1, 138, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(125, 1, 139, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(126, 1, 140, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(127, 1, 141, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(128, 1, 143, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(129, 1, 144, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(130, 1, 145, '2022-09-24 20:07:34', '2022-09-24 20:07:34'),
	(131, 2, 22, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(132, 2, 23, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(133, 2, 24, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(134, 2, 33, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(135, 2, 34, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(136, 2, 35, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(137, 2, 36, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(138, 2, 45, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(139, 2, 46, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(140, 2, 48, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(141, 2, 49, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(142, 2, 50, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(143, 2, 62, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(144, 2, 70, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(145, 2, 75, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(146, 2, 76, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(147, 2, 77, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(148, 2, 78, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(149, 2, 82, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(150, 2, 83, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(151, 2, 84, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(152, 2, 86, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(153, 2, 90, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(154, 2, 91, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(155, 2, 97, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(156, 2, 111, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(157, 2, 112, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(158, 2, 113, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(159, 2, 123, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(160, 2, 124, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(161, 2, 125, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(162, 2, 81, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(163, 2, 132, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(164, 2, 136, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(165, 2, 138, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(166, 2, 139, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(167, 2, 141, '2022-10-03 16:27:53', '2022-10-03 16:27:53'),
	(168, 3, 18, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(169, 3, 21, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(170, 3, 22, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(171, 3, 23, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(172, 3, 24, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(173, 3, 25, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(174, 3, 27, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(175, 3, 30, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(176, 3, 33, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(177, 3, 38, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(178, 3, 44, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(179, 3, 45, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(180, 3, 46, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(181, 3, 48, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(182, 3, 49, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(183, 3, 52, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(184, 3, 54, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(185, 3, 56, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(186, 3, 58, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(187, 3, 61, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(188, 3, 62, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(189, 3, 64, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(190, 3, 70, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(191, 3, 72, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(192, 3, 78, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(193, 3, 80, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(194, 3, 82, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(195, 3, 87, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(196, 3, 92, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(197, 3, 99, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(198, 3, 102, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(199, 3, 103, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(200, 3, 105, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(201, 3, 107, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(202, 3, 108, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(203, 3, 114, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(204, 3, 117, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(205, 3, 125, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(206, 3, 129, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(207, 3, 144, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(208, 3, 138, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(209, 3, 139, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(210, 3, 140, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(211, 3, 141, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(212, 3, 132, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(213, 3, 50, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(214, 3, 160, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(215, 3, 90, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(216, 3, 97, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(217, 3, 113, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(218, 3, 111, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(219, 3, 123, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(220, 3, 124, '2022-11-16 15:24:16', '2022-11-16 15:24:16'),
	(221, 1, 161, '2023-01-06 01:22:21', '2023-01-06 01:22:22'),
	(222, 2, 161, '2023-01-06 01:22:37', '2023-01-06 01:22:38'),
	(223, 1, 162, '2023-01-06 11:07:47', '2023-01-06 11:07:48'),
	(224, 1, 163, '2023-01-06 11:08:01', '2023-01-06 11:08:02'),
	(225, 2, 162, '2023-01-06 11:08:23', '2023-01-06 11:08:23'),
	(226, 2, 163, '2023-01-06 11:08:31', '2023-01-06 11:08:31');

-- 导出  表 blog.t_tag 结构
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签';

-- 正在导出表  blog.t_tag 的数据：~2 rows (大约)
INSERT INTO `t_tag` (`id`, `name`, `createTime`, `updateTime`) VALUES
	(1, '测试标签', '2022-12-18 02:48:30', '2022-12-18 02:48:30');

-- 导出  表 blog.t_talk 结构
CREATE TABLE IF NOT EXISTS `t_talk` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '说说id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `content` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '说说内容',
  `isTop` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否顶置',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(1公开/2私密/3草稿)',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `talk_ibfk_1` (`userId`),
  CONSTRAINT `talk_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='说说';

-- 正在导出表  blog.t_talk 的数据：~0 rows (大约)

-- 导出  表 blog.t_talk_picture_video 结构
CREATE TABLE IF NOT EXISTS `t_talk_picture_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `talkId` int(11) NOT NULL COMMENT '说说id',
  `src` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片或视频地址',
  `fileName` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
  `orderNum` tinyint(1) NOT NULL DEFAULT 0 COMMENT '图片顺序',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `talk_picture_video_ibfk_1` (`talkId`),
  CONSTRAINT `talk_picture_video_ibfk_1` FOREIGN KEY (`talkId`) REFERENCES `t_talk` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='说说的图片和视频';

-- 正在导出表  blog.t_talk_picture_video 的数据：~0 rows (大约)

-- 导出  表 blog.t_user_auth 结构
CREATE TABLE IF NOT EXISTS `t_user_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户账号id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户密码',
  `userInfoId` int(11) NOT NULL COMMENT '用户信息id',
  `loginType` tinyint(1) NOT NULL DEFAULT 0 COMMENT '登录类型',
  `ipAddress` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录IP',
  `ipSource` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录来源',
  `rectangle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '经纬度',
  `browser` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问浏览器',
  `os` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作系统',
  `lastLoginTime` datetime NOT NULL COMMENT '最后一次登录时间',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用（1是/0否）',
  `isDelete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_auth_ibfk_1` (`userInfoId`) USING BTREE,
  CONSTRAINT `FK_t_user_auth_t_user_info` FOREIGN KEY (`userInfoId`) REFERENCES `t_user_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户账号';

-- 正在导出表  blog.t_user_auth 的数据：~2 rows (大约)
INSERT INTO `t_user_auth` (`id`, `username`, `password`, `userInfoId`, `loginType`, `ipAddress`, `ipSource`, `rectangle`, `browser`, `os`, `lastLoginTime`, `enable`, `isDelete`, `createTime`, `updateTime`) VALUES
	(1, 'blog@qq.com', '$2a$10$3K6tu0sp7gYXQCy9Rfe0Xu0hz6unLuxHl9VNJABUuHYZgGghmzo9q', 1, 0, '192.168.1.4', '局域网', 'null,null', 'Chrome 10', 'Windows 10', '2023-01-12 00:12:06', 1, 0, '2022-12-18 00:34:37', '2023-01-12 00:12:06');

-- 导出  表 blog.t_user_info 结构
CREATE TABLE IF NOT EXISTS `t_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户信息id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `uid` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户唯一id',
  `email` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户头像',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户简介',
  `webSite` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '用户网站',
  `isEmailNotice` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知消息(0否/1是)',
  `bindQQ` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否绑定QQ（1是/0否）',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';

-- 正在导出表  blog.t_user_info 的数据：~2 rows (大约)
INSERT INTO `t_user_info` (`id`, `nickname`, `uid`, `email`, `avatar`, `intro`, `webSite`, `isEmailNotice`, `bindQQ`, `createTime`, `updateTime`) VALUES
	(1, 'blog', '00000000', 'blog@qq.com', 'https://apecode.oss-cn-chengdu.aliyuncs.com/config/ede43e676677ac0c781b4c0df218df3d.jpeg', '', '', 0, 0, '2022-12-18 00:34:37', '2023-01-07 17:20:23');

-- 导出  表 blog.t_user_role 结构
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户权限id',
  `userId` int(11) NOT NULL COMMENT '用户id',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `FK_t_user_role_t_user_auth` (`userId`),
  KEY `FK_t_user_role_t_role` (`roleId`),
  CONSTRAINT `FK_t_user_role_t_role` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FK_t_user_role_t_user_auth` FOREIGN KEY (`userId`) REFERENCES `t_user_auth` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户权限';

-- 正在导出表  blog.t_user_role 的数据：~2 rows (大约)
INSERT INTO `t_user_role` (`id`, `userId`, `roleId`, `createTime`, `updateTime`) VALUES
	(1, 1, 1, '2022-12-18 00:35:50', '2022-12-18 00:35:50'),
	(2, 1, 2, '2022-12-18 00:35:50', '2022-12-18 00:35:50');

-- 导出  表 blog.t_website 结构
CREATE TABLE IF NOT EXISTS `t_website` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `config` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '配置信息',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客设置';

-- 正在导出表  blog.t_website 的数据：~1 rows (大约)
INSERT INTO `t_website` (`id`, `config`, `createTime`, `updateTime`) VALUES
	(1, '{"alipayQRCode":"https://apecode.oss-cn-chengdu.aliyuncs.com/config/8d96313dc3b2ba39afd22956f69371f2.png","defaultAvatar":"https://apecode.oss-cn-chengdu.aliyuncs.com/config/ede43e676677ac0c781b4c0df218df3d.jpeg","homeTyperTexts":["i\'m apecode.","我是后端工程师","我热爱后端开发","渴望进大厂"],"isAutoPlayer":false,"isCommentReview":false,"isEmailNotice":true,"isMessageNotice":true,"isMessageReview":false,"qq":"1473018671","receiveEmail":"apecode@qq.com","socialLogin":{"QQ":true},"touristAvatar":"https://apecode.oss-cn-chengdu.aliyuncs.com/config/4381fe8188b20b77eec9e4ae56c3b040.jpeg","url":"http://127.0.0.1:8081","websiteAuthor":"apecode.","websiteAvatar":"https://apecode.oss-cn-chengdu.aliyuncs.com/config/0dd72faa4ad73753f5d90ce3313730d2.jpg","websiteBackgroundImages":["https://apecode.oss-cn-chengdu.aliyuncs.com/config/6206350df97708dee13eafeb38f69a6d.jpg","https://apecode.oss-cn-chengdu.aliyuncs.com/config/763aaae18ec9c814f3d6820371e2ea06.jpg","https://apecode.oss-cn-chengdu.aliyuncs.com/config/9feab3b2cde0664505d9763abb18d042.jpg","https://apecode.oss-cn-chengdu.aliyuncs.com/config/625417f86886d29e670475333d5891fe.jpg","https://apecode.oss-cn-chengdu.aliyuncs.com/config/fbb81deae22a68e696535dc265434653.jpg","https://apecode.oss-cn-chengdu.aliyuncs.com/config/e3e13157778a4017d2f01da04e5c47a2.jpg"],"websiteCreateTime":"1671292800","websiteIntro":"[挥手]hi,i\'m apecode.[嘿嘿]","websiteName":"apecode","websiteNotice":"🥳这是公告[喜笑颜开]","websiteRecordNo":" 滇ICP备2022008178号","wechat":"apecodex","weiXinQRCode":"https://apecode.oss-cn-chengdu.aliyuncs.com/config/c579441702c702f9d8e336b58007bfd8.png"}', '2022-07-14 17:22:56', '2023-01-07 02:34:26');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
