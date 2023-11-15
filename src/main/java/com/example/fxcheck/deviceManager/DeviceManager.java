package com.example.fxcheck.deviceManager;

import com.example.fxcheck.bidBroker.BidBroker;
import com.example.fxcheck.consumer.Consumer;
import com.example.fxcheck.model.Bid;

public class DeviceManager implements Runnable {

    DeviceRing deviceRing;
    private final BidBroker bidBroker;
    public DeviceManager(final BidBroker bidBroker, final DeviceRing deviceRing){
        this.bidBroker = bidBroker;
        this.deviceRing = deviceRing;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            if(deviceRing.countOfFreeDevices > 0) {
                Consumer freeConsumer = deviceRing.getFreeDevice();
                //if (freeConsumer != null) {
                final Bid receivedBid = bidBroker.get();
                Thread consumer = new Thread(freeConsumer);
                deviceRing.countOfFreeDevices--;
                freeConsumer.start(consumer, receivedBid);//он сам себя запускает с установкой карент-заявки
                // }
            }

        }
    }

    public void addConsumer(Consumer consumer){
        deviceRing.addDevice(consumer);
    }




}
