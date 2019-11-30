package com.example.demo.util.constant;


public class BizConstants {

    public static final String RESULT_MSG_WEB = "message";

    public static final String MICROSRV_NAME = "Operation-Activity-GreenPlant";

    public static final String GREEN_PLANT = "act-green-plant";

    public static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public static final String WX_ACCESS_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    public static final String WX_APP_ID = "wxf400672ca9f3656d";

    public static final String WX_SECRET = "63030bc2d62ae9ca7171a4f51d19b888";

    public static final String WX_GRANT_TYPE = "authorization_code";

    public static final class HAS_RECEIVED {
        public static final Integer NO = 0;
        public static final Integer YES = 1;

        public HAS_RECEIVED() {
        }
    }

    public static final class HAS_HELPED {
        public static final Integer NO = 0;
        public static final Integer YES = 1;

        public HAS_HELPED() {
        }
    }

    public static final class IS_ACTIVITY_NEW {
        public static final Integer NO = 0;
        public static final Integer YES = 1;

        public IS_ACTIVITY_NEW() {
        }
    }

    public static final class RESULT_CODE_NO_USER {
        public static final String KEY = "4000";
        public static final String MSG = "用户不存在！";

        public RESULT_CODE_NO_USER() {
        }
    }
    public static final class RESULT_CODE_REPEAT_REQUEST {
        public static final String KEY = "4001";
        public static final String MSG = "重复请求！";

        public RESULT_CODE_REPEAT_REQUEST() {
        }
    }

    public static final class RESULT_CODE_HAS_RECEIVED {
        public static final String KEY = "4002";
        public static final String MSG = "已领取过奖品！";

        public RESULT_CODE_HAS_RECEIVED() {
        }
    }

    public static final class RESULT_CODE_NO_INVITER {
        public static final String KEY = "4003";
        public static final String MSG = "被助力用户不存在！";

        public RESULT_CODE_NO_INVITER() {
        }
    }

    public static final class RESULT_CODE_HAS_HELPED{
        public static final String KEY = "4004";
        public static final String MSG = "已助力过其他人！";

        public RESULT_CODE_HAS_HELPED() {
        }
    }

    public static final class RESULT_CODE_HELP_ENOUGH {
        public static final String KEY = "4005";
        public static final String MSG = "被助力用户以满足条件，不需要助力！";

        public RESULT_CODE_HELP_ENOUGH() {
        }
    }

    public static final class RESULT_CODE_HELP_NOT_ENOUGH {
        public static final String KEY = "4006";
        public static final String MSG = "未助力成功，不能领取！";

        public RESULT_CODE_HELP_NOT_ENOUGH() {
        }
    }

    public static final class RESULT_CODE_NO_ACTIVITY_NEW {
        public static final String KEY = "4007";
        public static final String MSG = "不是本活动专属新用户，不能助力！";

        public RESULT_CODE_NO_ACTIVITY_NEW() {
        }
    }

    public static final class RESULT_CODE_EMPTY_FILE {
        public static final String KEY = "4008";
        public static final String MSG = "上传文件为空！";

        public RESULT_CODE_EMPTY_FILE() {
        }
    }

}
