package com.offcn.util;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class SmsListener implements MessageListener {

    @Autowired
    private SmsUtil SmsUtil;

    @Override
    public void onMessage(Message message) {
        try {
            MapMessage mapMessage = (MapMessage)message;
            String mobile = mapMessage.getString("mobile");
            String code = mapMessage.getString("code");

            HttpResponse response = SmsUtil.sendSms(mobile,code);
            System.out.println("response : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
