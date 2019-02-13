package com.example.testmvpapp.component.net;

public class ConstantService {

    // 服务器的基本请求域名地址
    public static final String BASE_URL = "http://120.76.240.104:8017";

    /// 公用链接
    // 版本更新
    public static final String UPDATE = BASE_URL + "/api/upgrade/get_version";
    // 短信
    public static final String SMS = BASE_URL + "/api/sms/send";
    // 文案
    public static final String OFFICER = BASE_URL + "/api/officer/get_officer";

    /// 功能
    // 首页
    public static final String INDEX = BASE_URL + "/api/index/index";
    // 电商服务中心
    public static final String SERVICECENTER_LIST = BASE_URL + "/api/servicecenter/getServicecenter";

    /// 个人中心
    // 登录
    public static final String LOGIN = BASE_URL + "/api/user/login";
    // 注册
    public static final String REGISTER = BASE_URL + "/api/user/register";
    // 退出登录
    public static final String LOGOUT = BASE_URL + "/api/user/logout";
    // 忘记密码
    public static final String FORGET_PASSWORD = BASE_URL + "/api/user/forgetPass";
    // 重置密码
    public static final String RESET_PASSWORD = BASE_URL + "/api/user/resetPass";
    // 更新用户信息
    public static final String UPDATE_USER_INFO = BASE_URL + "/api/user/updateUserInfo";
    // 反馈
    public static final String FEED_BACK = BASE_URL + " /api/message/leave_message";

    /// 客服
    public static final String CUSTOME_SERVICE_URL =  BASE_URL + "/api/my/getCustomServiceUrl";

    /// 消息
    public static final String MESSAGE_LIST = BASE_URL + "/api/message/get_message";
    
}
