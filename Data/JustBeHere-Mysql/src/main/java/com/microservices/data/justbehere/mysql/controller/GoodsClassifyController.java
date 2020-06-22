package com.microservices.data.justbehere.mysql.controller;

import com.alibaba.fastjson.JSONObject;
import com.microservices.common.feignclient.data.justbehere.result.GoodsClassify;
import com.microservices.common.feignclient.data.justbehere.result.GoodsPrice;
import com.microservices.common.feignclient.data.justbehere.result.ServiceOrder;
import com.microservices.common.response.ResponseArrayModel;
import com.microservices.common.response.ResponseModel;
import com.microservices.common.utils.StringUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/goods/classify")
public class GoodsClassifyController {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private final Logger logger = LoggerFactory.getLogger(GoodsClassifyController.class);


    /**
     * 商品分类 通过code 获取指定数据
     *
     * @param code 商品分类编码
     * @return
     */
    @RequestMapping(value = "/select", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseModel<GoodsClassify> select(@RequestBody String code) {
        ResponseModel<GoodsClassify> responseModel = new ResponseModel<>();

        Map<String, Object> map = new HashMap<>();

        if (StringUtil.isEmpty(code)) {
            responseModel.setMessage("商品分类code为空");
            return responseModel;
        }

        map.put("code", code);

        GoodsClassify entity = sqlSessionTemplate.selectOne("com.microservices.data.justbehere.mysql.GoodsClassifyMapper.select", map);
        if (entity == null) {
            responseModel.setMessage("该用户商品分类不存在：" + code);
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entity);
        }

        return responseModel;
    }

    /**
     * 商品分类 列表获取
     * @param body 暂时为空
     * @return
     */
    @RequestMapping(value = "/selectList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseArrayModel<GoodsClassify> selectList(@RequestBody JSONObject body) {
        ResponseArrayModel<GoodsClassify> responseModel = new ResponseArrayModel<>();

        Map<String, Object> map = new HashMap<>();

        List<GoodsClassify> entities = sqlSessionTemplate.selectList("com.microservices.data.justbehere.mysql.GoodsClassifyMapper.selectList", map);
        if (entities == null) {
            responseModel.setMessage("商品分类数据：" + body.toJSONString());
        } else {
            responseModel.setSuccess(true);
            responseModel.setData(entities);
        }

        return responseModel;
    }
}
