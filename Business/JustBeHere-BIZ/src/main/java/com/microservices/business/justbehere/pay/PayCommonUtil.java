package com.microservices.business.justbehere.pay;

import com.microservices.common.utils.DateUtil;
import com.microservices.common.utils.MD5Util;

import java.util.*;

public class PayCommonUtil {

	/**
	 * 方法描述: [是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。]</br>
	 * 初始作者: YPHU<br/>
	 * 创建日期: 2018年1月16日-下午2:51:03<br/>
	 * 开始版本: 2.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param characterEncoding
	 * @param packageParams
	 * @param API_KEY
	 * @return
	 *         boolean
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {

		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		// 算出摘要
		String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
		String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();
		return tenpaySign.equals(mysign);
	}

	/**
	 * 方法描述: [sign签名.]</br>
	 * 初始作者: YPHU<br/>
	 * 创建日期: 2018年1月16日-下午2:51:25<br/>
	 * 开始版本: 2.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param characterEncoding
	 * @param packageParams
	 * @param API_KEY
	 * @return
	 *         String
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {

		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * 方法描述: [将请求参数转换为xml格式的string.]</br>
	 * 初始作者: YPHU<br/>
	 * 创建日期: 2018年1月16日-下午2:51:46<br/>
	 * 开始版本: 2.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param parameters
	 * @return
	 *         String
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String getRequestXml(SortedMap<Object, Object> parameters) {

		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 方法描述: [取出一个指定长度大小的随机正整数.]</br>
	 * 初始作者: YPHU<br/>
	 * 创建日期: 2018年1月16日-下午2:52:06<br/>
	 * 开始版本: 2.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @param length
	 * @return
	 *         int
	 */
	public static int buildRandom(int length) {

		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) (random * num);
	}

	/**
	 * 方法描述: [获取当前时间 yyyyMMddHHmmss.]</br>
	 * 初始作者: YPHU<br/>
	 * 创建日期: 2018年1月16日-下午2:52:34<br/>
	 * 开始版本: 2.0.0<br/>
	 * =================================================<br/>
	 * 修改记录：<br/>
	 * 修改作者 日期 修改内容<br/>
	 * ================================================<br/>
	 *
	 * @return
	 *         String
	 */
	public static String getCurrTime() {

		Date now = new Date();
		String s = DateUtil.dealDateFormat(now);
		return s;
	}
}
