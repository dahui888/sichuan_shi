package com.campuscard.app;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 接口地址和常量配置文件
 */
public class Constant {

    /**
     * 静态变量
     */
    public static final String USER_INFO_TAG = "USER_INFO";
    public static final String TOKEN = "TOKEN";
    public static final int PAGE_SIZE = 20;
    public static final String BIND_SHOW = "BIND_SHOW";
    public static final String ACCOUNT = "ACCOUNT";


    /**
     * 项目基础地址
     */
//    public static final String BASE_HOST = "https://yeager.vip/";//测试地址
    public static final String BASE_HOST = "http://202.115.196.122/";//正式地址


    //获取短信
    public static final String GET_YZM = BASE_HOST + "snu/end-user-service/app/endUser/getSMSCode";
    //获取图形验证码
    public static final String GET_TXYZM = BASE_HOST + "snu/kaptcha-service/kaptcha/get";
    //注册
    public static final String REGISTER = BASE_HOST + "snu/end-user-service/app/endUser/registry";
    //找回密码、修改密码
    public static final String FIND_PWD = BASE_HOST + "snu/end-user-service/app/endUser/resetLoginPwd";
    //登录
    public static final String LOGIN = BASE_HOST + "snu/end-user-service/app/endUser/loginWithPwd";
    //验证码登录
    public static final String YZMLOGIN = BASE_HOST + "snu/end-user-service/app/endUser/loginWithSMS";
    //退出登录
    public static final String LOGIN_OUT = BASE_HOST + "app/user/logout";


    //分页新闻
    public static final String NEWS_PAGE = BASE_HOST + "snu/news-service/app/news/page";
    //新闻详情
    public static final String NEWS_INFO = BASE_HOST + "snu/news-service/app/news/get/";

    //通告
    public static final String NOTICE_PAGE = BASE_HOST + "snu/announcement-service/app/announcement/page";

    //通告详情
    public static final String NOTICE_INFO = BASE_HOST + "snu/announcement-service/app/announcement/get/";

    //物品分类接口
    public static final String WUPIN_CLASS = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundTypeThings/list";
    //失物招领领
    public static final String SWZL = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/page";

    //失物招领领详情
    public static final String SWZL_INFO = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/get/";

    //发布失物招领
    public static final String RELEASE = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/save";
    //修改失物招领
    public static final String UPDATERELEASE = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/update";
    //删除失物招领
    public static final String DELETE = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/delete";

    //上传图片
    public static final String UPLOAD_IMG = BASE_HOST + "snu/file-service/file/upload";

    //用户反馈
    public static final String ADVICE = BASE_HOST + "snu/common-service/app/feedback/save";
    //关于我们
    public static final String ABOUT = BASE_HOST + "snu/common-service/app/aboutUs/get";
    //版本
    public static final String VERSION = BASE_HOST + "snu/common-service/app/version/getVersion";
    //常见问题列表
    public static final String QUESTION_LIST = BASE_HOST + "snu/common-service/app/commonProblem/list";

    //常见问题详情
    public static final String QUESTION_INFO = BASE_HOST + "snu/common-service/app/commonProblem/get/";
    //我的发布
    public static final String MY_RELEASE = BASE_HOST + "snu/lost-and-found-service/app/lostAndFoundInfo/my";
    //获取未读消息条数
    public static final String NO_READ_MSG_NUM = BASE_HOST + "snu/message-service/app/message/getTypeCount";

    //分页查询推送信息
    public static final String GET_MSG_LIST = BASE_HOST + "snu/message-service/app/message/page";
    //获取信息详情
    public static final String GET_MSG_DETAILS = BASE_HOST + "snu/message-service/app/message/get/";
    //标记推送信息为已读
    public static final String MSG_IS_READE = BASE_HOST + "snu/message-service/app/message/read/";

    //一卡通绑定
    public static final String BIND_CARD = BASE_HOST + "snu/end-user-service/app/endUser/bind";

    //用户查询一卡通余额信息
    public static final String CARD_USE_INFO = BASE_HOST + "snu/end-user-service/app/endUser/queryEcardBalance";

    //使用指南
    public static final String USE_ZHINAN = BASE_HOST + "snu/common-service/app/operationGuide/get";

    //用户查询一卡通挂失记录
    public static final String ECARD_LOSEE_RECORD = BASE_HOST + "snu/end-user-service/app/endUser/queryEcardLossRec";

    //用户查询一卡通圈存记录--为零补助记录
    public static final String TRANSFER_RECORD = BASE_HOST + "snu/end-user-service/app/endUser/waitTransfer";
    //获取用户自己信息
    public static final String GETPERSONAL = BASE_HOST + "snu/end-user-service/app/endUser/getUserInfo";
    //更换昵称
    public static final String CHANGNICKNAME = BASE_HOST + "snu/end-user-service/app/endUser/changeUserName";
    //更换头像
    public static final String CHANGHEADPICTURE = BASE_HOST + "snu/end-user-service/app/endUser/changeHeadportraid";

    //一卡通消费记录
    public static final String CARD_RECODE = BASE_HOST + "snu/end-user-service/app/endUser/consumePage";

    //一卡通解绑
    public static final String UNBIND_CARD = BASE_HOST + "snu/end-user-service/app/endUser/unbindEcard";

    //一卡通充值记录
    public static final String CARD_RECORD = BASE_HOST + "snu/order-service/app/order/pageECard";
    //一卡通充值
    public static final String CARD_RECHARGE = BASE_HOST + "snu/order-service/app/order/saveECardOrder";
    //一卡通支付
    public static final String CARD_PAY = BASE_HOST + "snu/order-service/app/order/updateOrder";

    //一卡通电费保存
    public static final String ELECT_SAVE = BASE_HOST + "snu/order-service/app/order/saveElectricity";
    //电费记录
    public static final String ELECT_RECORD = BASE_HOST + "snu/order-service/app/order/pageElectricity";

    //获取校区
    public static final String SCHOOL = BASE_HOST + "snu/order-service/app/room/campus";

    //获取公寓
    public static final String FOLLER = BASE_HOST + "snu/order-service/app/room/buildings";

    //获取电费
    public static final String GET_DIANFEI = BASE_HOST + "snu/order-service/app/room/query";

    //一卡通挂失
    public static final String CARD_LOSS = BASE_HOST + "snu/end-user-service/app/endUser/ecardLoss";

    //首页轮播图
    public static final String ADV = BASE_HOST + "snu/common-service/app/ad/banner";

    //预充值
    public static final String PRE_RECHARGE = BASE_HOST + "snu/order-service/app/order/preRecharge/";

    //校园卡充值记录详情
    public static final String CARD_RECORD_INFO = BASE_HOST + "snu/order-service/app/order/getEcartRec/";
    //电费充值记录详情
    public static final String ELC_RECORD_INFO = BASE_HOST + "snu/order-service/app/order/getElectricityRec/";

    //更新订单
    public static final String UPDATE_ORDER = BASE_HOST + "snu/order-service/app/order/updateOrder";

    //分页获取捡卡信息
    public static final String JIANKA_INFO_PAGE = BASE_HOST + "snu/end-user-service/app/endUser/pickUpEcardPage";

    //账单筛选
    public static final String BILL = BASE_HOST + "snu/end-user-service/app/endUser/listConsume";

    //注册协议
    public static final String ZHUCE_XIEYI = BASE_HOST + "snu/common-service/protocal.html";


}