import {RouteRecordRaw} from "vue-router";

export const constantRoutes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'HomeNav',
        component: () => import('@/views/home/Home.vue'),
        meta: {
            title: "首页",
            isLogin: false
        }
    },
    {
        path: '/article/:articleId',
        name: 'ArticleNav',
        component: () => import("@/views/article/Article.vue"),
        meta: {
            title: "文章",
            isLogin: false
        }
    },
    {
        path: '/archives',
        name: 'ArchiveNav',
        component: () => import("@/views/archive/Archive.vue"),
        meta: {
            title: "时间轴",
            isLogin: false
        }
    },
    {
        path: '/albums',
        name: 'AlbumNav',
        component: () => import("@/views/album/Album.vue"),
        meta: {
            title: "相册",
            isLogin: false
        }
    },
    {
        path: "/album/:albumId/pictures",
        name: "PictureNav",
        component: () => import("@/views/picture/Picture.vue"),
        meta: {
            title: "图片",
            isLogin: false
        }
    },
    {
        path: '/talks',
        name: 'TalkListNav',
        component: () => import("@/views/talk/TalkList.vue"),
        meta: {
            title: "说说列表",
            isLogin: false
        }
    },
    {
        path: '/talk/:talkId',
        name: 'TalkNav',
        component: () => import("@/views/talk/Talk.vue"),
        meta: {
            title: "说说",
            isLogin: false
        }
    },
    {
        path: '/links',
        name: 'FriendLinkNav',
        component: () => import("@/views/friendLink/FriendLink.vue"),
        meta: {
            title: "友链",
            isLogin: false
        }
    },
    {
        path: '/messages',
        name: 'MessageNav',
        component: () => import("@/views/message/Message.vue"),
        meta: {
            title: "留言",
            isLogin: false
        }
    },
    {
        path: '/about',
        name: 'AboutNav',
        component: () => import("@/views/about/About.vue"),
        meta: {
            title: "关于",
            isLogin: false
        }
    },
    {
        path: '/category',
        name: 'CategoryNav',
        component: () => import("@/views/category/Category.vue"),
        meta: {
            title: "分类",
            isLogin: false
        }
    },
    {
        path: '/categories/:categoryId',
        name: 'CategoriesNav',
        component: () => import("@/views/category/ArticleList.vue"),
        meta: {
            title: "分类文章",
            isLogin: false
        },
    },
    {
        path: '/tags',
        name: 'TagNav',
        component: () => import("@/views/tag/Tag.vue"),
        meta: {
            title: "标签",
            isLogin: false
        }
    },
    {
        path: '/tags/:tagId',
        name: 'TagsNav',
        component: () => import("@/views/tag/ArticleList.vue"),
        meta: {
            title: "标签文章",
            isLogin: false
        },
    },
    {
        path: '/profile',
        name: 'ProfileNav',
        component: () => import("@/views/profile/Profile.vue"),
        redirect: {name: "ProfileInfoNav"},
        meta: {
            title: "个人中心",
            isLogin: true
        },
        children: [
            {
                path: '/profile',
                name: 'ProfileInfoNav',
                component: () => import("@/views/profile/children/UserInfo.vue"),
                meta: {
                    title: "个人中心",
                    isLogin: true
                }
            },
            {
                path: '/profile/notices',
                name: 'ProfileNoticeNav',
                component: () => import("@/views/profile/children/Notice.vue"),
                meta: {
                    title: "个人中心 | 通知",
                    isLogin: true
                }
            },
            {
                path: '/profile/friendLink',
                name: 'ProfileFriendLinkNav',
                component: () => import("@/views/profile/children/FriendLink.vue"),
                meta: {
                    title: "个人中心 | 友链",
                    isLogin: true
                },
            },
            {
                path: '/profile/password',
                name: 'ProfilePasswordNav',
                component: () => import("@/views/profile/children/Password.vue"),
                meta: {
                    title: "个人中心 | 修改密码",
                    isLogin: true
                },
            }
        ]
    },
    {
        path: "/oauth/qq/callback",
        name: "OauthLoginQQ",
        component: () => import("@/components/OauthLogin.vue")
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: () => import("@/views/error/PageNotFound.vue"),
        meta: {
            title: "404",
            isLogin: false
        }
    }
]