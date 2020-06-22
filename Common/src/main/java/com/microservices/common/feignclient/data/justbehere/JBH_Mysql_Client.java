package com.microservices.common.feignclient.data.justbehere;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.justbehere.body.CartBody;
import com.microservices.common.feignclient.data.justbehere.body.GoodsOrderBody;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.*;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_just_be_here)
public interface JBH_Mysql_Client {


    /********  轮播图  *********/


    /**
     * 轮播图 列表获取
     *
     * @param body 通过carouselCode（轮播图位置编码）, code（对应编码）
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Carousel> selectCarouselList(@RequestBody JSONObject body);




    /********  配置项 首页  *********/

    /**
     * 配置项 列表获取
     *
     * @param type 通过主页配置项类型
     * @return
     */
    @RequestMapping(value = "setting/mainPage/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Setting_MainPage> selectMainPageSettingList(@RequestBody String type);






    /********  商品  *********/

    /**
     * 商品 通过code 获取指定数据
     *
     * @param code 商品编号
     * @return
     */
    @RequestMapping(value = "goods/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Goods> selectGoods(@RequestBody String code);

    /**
     * 商品 列表获取 通过classify，start（开始），number（个数）
     * 按照等级、编号升序排序，
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "goods/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Goods> selectGoodsList(@RequestBody JSONObject body);





    /********  商品分类  *********/

    /**
     * 商品分类 通过code 获取指定数据
     *
     * @param code 商品分类编码
     * @return
     */
    @RequestMapping(value = "goods/classify/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsClassify> selectGoodsClassify(@RequestBody String code);

    /**
     * 商品分类 列表获取
     * @param body 暂时为空
     * @return
     */
    @RequestMapping(value = "goods/classify/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsClassify> selectGoodsClassifyList(@RequestBody JSONObject body);



    /********  商品价格  *********/

    /**
     * 商品价格 通过商品ID 获取指定数据
     *
     * @param goodsID 商品ID
     * @return
     */
    @RequestMapping(value = "goods/price/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsPrice> selectGoodsPrice(@RequestBody String goodsID);




    /********  商品 订单  *********/

    /**
     * 商品订单 添加新数据
     * @param body
     * @return
     */
    @RequestMapping(value = "order/goods/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insertGoodsOrder(@RequestBody GoodsOrderBody body);

    /**
     * 商品订单 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "order/goods/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> selectGoodsOrder(@RequestBody String id);

    /**
     * 商品订单 列表获取 通过userID, status
     * @param body
     * @return
     */
    @RequestMapping(value = "order/goods/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsOrder> selectGoodsOrderList(@RequestBody JSONObject body);

    /**
     * 商品订单 更新
     * @param body  通过id, status, content
     * @return
     */
    @RequestMapping(value = "order/goods/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsOrder> updateGoodsOrder(@RequestBody JSONObject body);






    /********  服务 分类  *********/

    /**
     * 服务分类 通过code 获取指定数据
     *
     * @param code 服务分类编码
     * @return
     */
    @RequestMapping(value = "service/classify/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceClassify> selectServiceClassify(@RequestBody String code);






    /********  服务  *********/


    /**
     * 服务 通过code 获取指定数据
     *
     * @param code 服务编号
     * @return
     */
    @RequestMapping(value = "service/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Service> selectService(@RequestBody String code);

    /**
     * 服务 列表获取 通过classify，start（开始），number（个数）
     * 按照等级、编号升序排序，
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "service/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Service> selectServiceList(@RequestBody JSONObject body);


    /**
     * 服务详情 通过code 获取指定数据
     *
     * @param code 服务编号
     * @return
     */
    @RequestMapping(value = "service/detail/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceDetail> selectServiceDetail(@RequestBody String code);

    /**
     * 服务价格 通过服务ID 获取指定数据
     *
     * @param code 服务编码
     * @return
     */
    @RequestMapping(value = "service/price/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServicePrice> selectServicePrice(@RequestBody String code);







    /********  服务 订单  *********/

    /**
     * 服务订单 添加新数据
     * @param body
     * @return
     */
    @RequestMapping(value = "order/service/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insertServiceOrder(@RequestBody ServiceOrderBody body);

    /**
     * 服务订单 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "order/service/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> selectServiceOrder(@RequestBody String id);

    /**
     * 服务订单 列表获取 通过userID, status, serviceTime
     * @param body
     * @return
     */
    @RequestMapping(value = "order/service/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<ServiceOrder> selectServiceOrderList(@RequestBody JSONObject body);

    /**
     * 服务订单 更新
     * @param body
     * @return
     */
    @RequestMapping(value = "order/service/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<ServiceOrder> updateServiceOrder(@RequestBody ServiceOrderBody body);






    /********  购物车  *********/

    /**
     * 购物车 添加新数据
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "cart/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<String> insertCart(@RequestBody CartBody body);

    /**
     * 购物车 通过ID 获取指定数据
     *
     * @param id 表ID
     * @return
     */
    @RequestMapping(value = "cart/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> selectCart(@RequestBody String id);

    /**
     * 购物车 列表获取 通过userID, goodsID, typeID
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "cart/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<Cart> selectCartList(@RequestBody JSONObject body);

    /**
     * 购物车 更新
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "cart/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> updateCart(@RequestBody CartBody body);

    /**
     * 删除 购物车 具体数据
     * @param id
     * @return
     */
    @RequestMapping(value = "cart/delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<Cart> deleteFromCart(@RequestBody String id);
}
