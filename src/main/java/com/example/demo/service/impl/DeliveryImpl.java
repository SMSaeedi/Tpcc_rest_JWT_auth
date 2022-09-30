package com.example.demo.service.impl;


import com.example.demo.model.Delivery_Vx0;
import com.example.demo.object.DeliveryInputParams;
import com.example.demo.object.DeliveryResult;
import com.example.demo.service.Delivery;
import org.springframework.stereotype.Service;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

@Service
public class DeliveryImpl implements Delivery {

    @Override
    public DeliveryResult deliveryTransaction_Vx0(DeliveryInputParams input) {
        return new Delivery_Vx0().deliveryTransactionTest(input);
    }

    @Override
    public DeliveryResult deliveryTransaction_HardCode(DeliveryInputParams input) throws Exception {
        return new Delivery_Vx0().deliveryTransactionTest(input);
    }

}