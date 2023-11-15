package com.example.fxcheck.bidBroker;

import com.example.fxcheck.deviceManager.DeviceRing;
import com.example.fxcheck.model.Bid;
import com.example.fxcheck.statistic.ModelTimer;
import com.example.fxcheck.statistic.Note;
import com.example.fxcheck.statistic.SpecialEvents;
import com.example.fxcheck.statistic.Statistic;

import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.currentThread;

public class BidBroker {
    private final Queue<Bid> messageQueue;
    private final int maxStoredBids;
    private boolean allDevicesIsBusy;
    public BidBroker(final int maxStoredMessages){
        this.messageQueue = new LinkedList<>();
        this.maxStoredBids = maxStoredMessages;
        this.allDevicesIsBusy = false;
    }

    public synchronized void put(final Bid bid){
        if(messageQueue.size() >= maxStoredBids){
            System.out.println(messageQueue.poll() + " ушла в отказ");
            messageQueue.add(bid);
            // wait();
        }
        else {
            this.messageQueue.add(bid);


            notifyAll();
        }
        allDevicesIsBusy = DeviceRing.countOfFreeDevices <= 0;
        Statistic.recordAnEvent(new Note(SpecialEvents.GENERATION, ModelTimer.getCurrentTime(),bid.toString(),allDevicesIsBusy));
        //System.out.println(bid + " received in buffer ");
    }

    public synchronized Bid get(){
        while(messageQueue.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                currentThread().interrupt();
                throw new RuntimeException();
            }
        }
        final Bid bid =  messageQueue.poll();
        //System.out.println(bid + " was taken from the buffer");
        return bid;
    }
}