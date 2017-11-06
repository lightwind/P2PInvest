package com.lightwind.p2pinvest.common;

/**
 * Function：配置网络请求相关的地址
 * Author：LightWind
 * Time：2017/10/27
 */

public class AppNetConfig {

    private static final String IP_ADDRESS = "192.168.1.103";

    private static final String BASE_URL = "http://" + IP_ADDRESS + ":8080/P2PInvest/";

    public static final String PRODUCT = BASE_URL + "product";//访问“全部理财”产品

    public static final String LOGIN = BASE_URL + "login";//登录

    public static final String INDEX = BASE_URL + "index";//访问“homeFragment”

    public static final String USER_REGISTER = BASE_URL + "UserRegister";//访问“homeFragment”

    public static final String FEEDBACK = BASE_URL + "FeedBack";//注册

    public static final String UPDATE = BASE_URL + "update.json";//更新应用

}
