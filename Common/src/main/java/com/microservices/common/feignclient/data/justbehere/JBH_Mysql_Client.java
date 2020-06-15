package com.microservices.common.feignclient.data.justbehere;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.ClientConstants;
import com.microservices.common.feignclient.data.justbehere.body.ServiceOrderBody;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ClientConstants.module_data_just_be_here)
public interface JBH_Mysql_Client {


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
}
