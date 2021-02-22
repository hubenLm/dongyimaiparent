package com.dongyimai.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.dongyimai.pay.service.AliPayService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class AliPayServiceImpl implements AliPayService {
    @Autowired
    AlipayClient alipayClient;

    @Override
    public Map createNative(String out_trade_no, String total_fee) {
        Map<String,String> map=new HashMap<String, String>();
        //创建预下单请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"total_amount\":\""+total_fee+"\"," +
                "    \"subject\":\"测试购买商品001\"," +
                "    \"store_id\":\"xa_001\"," +
                "    \"timeout_express\":\"90m\"}");//设置业务参数
        //发出预下单业务请求
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            //从相应对象读取相应结果
            String code = response.getCode();
            System.out.println("响应码:"+code);
            //全部的响应结果
            String body = response.getBody();
            System.out.println("返回结果:"+body);

            if(code.equals("10000")){
                map.put("qrcode", response.getQrCode());
                map.put("out_trade_no", response.getOutTradeNo());
                map.put("total_fee",total_fee);
                System.out.println("qrcode:"+response.getQrCode());
                System.out.println("out_trade_no:"+response.getOutTradeNo());
                System.out.println("total_fee:"+total_fee);
            }else{
                System.out.println("预下单接口调用失败:"+body);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }


        return map;

    }

    /**
     * 交易查询接口alipay.trade.query：
     * 获取指定订单编号的，交易状态
     * @throws AlipayApiException
     */

    @Override
    public Map<String,String> queryPayStatus(String out_trade_no) {
        Map<String,String> map=new HashMap<String, String>();
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+out_trade_no+"\"," +
                "    \"trade_no\":\"\"}"); //设置业务参数

        //发出请求
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String code=response.getCode();
            System.out.println("返回值1:"+response.getBody());
            if(code.equals("10000")){

                //System.out.println("返回值2:"+response.getBody());
                map.put("out_trade_no", out_trade_no);
                map.put("tradestatus", response.getTradeStatus());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }



        return map;
    }
}
