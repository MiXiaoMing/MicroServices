package com.microservices.common.constants;

public class Constants {

    /*******  用户分类  ********/

    // 用户端
    public final static String user_type_customer = "01";
    // 商户端
    public final static String user_type_merchant = "02";




    /*******  数据分类  ********/

    // 服务分类
    public final static String data_service_classify = "01";
    // 服务详情
    public final static String data_service = "02";
    // 商品分类
    public final static String data_goods_classify = "03";
    // 商品详情
    public final static String data_goods = "04";




    /*******  订单状态  ********/

    // 未支付
    public final static String order_status_unpay = "01";
    // 支付成功，待服务
    public final static String order_status_undone = "02";
    // 服务中
    public final static String order_status_ing = "03";
    // 已完成
    public final static String order_status_done = "04";
    // 已取消
    public final static String order_status_cancel = "05";



    /*******  轮播图  ********/

    // 主页
    public final static String carousel_main_page = "00";
    // 服务分类
    public final static String carousel_service_classify = "01";
    // 服务项
    public final static String carousel_service = "02";
    // 商品分类
    public final static String carousel_goods_classify = "03";
    // 商品
    public final static String carousel_goods = "04";



    /*******  主页 配置数据  ********/

    // 五大项
    public final static String setting_main_page_five = "01";
    // 十小项
    public final static String setting_main_page_ten = "02";
    // 推荐
    public final static String setting_main_page_recommend = "03";
    // 推荐分类
    public final static String setting_main_page_recommend_service_classify = "04";

}
