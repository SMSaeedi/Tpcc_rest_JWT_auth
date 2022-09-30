package com.example.demo.service;


import com.example.demo.object.DeliveryInputParams;
import com.example.demo.object.DeliveryResult;

public interface Delivery {

    public DeliveryResult deliveryTransaction_Vx0(DeliveryInputParams input) throws Exception;
    public DeliveryResult deliveryTransaction_HardCode(DeliveryInputParams input) throws Exception;

}