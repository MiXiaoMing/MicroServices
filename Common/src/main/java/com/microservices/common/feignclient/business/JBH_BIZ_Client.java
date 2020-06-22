package com.microservices.common.feignclient.business;

import com.microservices.common.feignclient.ClientConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = ClientConstants.module_jbh_biz)
public interface JBH_BIZ_Client extends JBH_BIZ_UserClient, JBH_BIZ_OrderClient, JBH_BIZ_SettingClient,
                    JBH_BIZ_GoodsClient, JBH_BIZ_CartClient
{

}