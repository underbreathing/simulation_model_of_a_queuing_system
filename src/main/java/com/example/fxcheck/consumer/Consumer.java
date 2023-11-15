package com.example.fxcheck.consumer;

import com.example.fxcheck.deviceManager.DeviceRing;
import com.example.fxcheck.model.Bid;
import com.example.fxcheck.statistic.ModelTimer;
import com.example.fxcheck.statistic.Note;
import com.example.fxcheck.statistic.SpecialEvents;
import com.example.fxcheck.statistic.Statistic;

public class Consumer implements Runnable{
    public boolean isBusy;
    private Bid currentBid;
    public int number;
    private DeviceRing deviceRing;
    private final long averageProcessingTime;

    public Consumer(final int number, final long processingTime, final DeviceRing deviceRing){
        this.deviceRing = deviceRing;
        this.number = number;
        this.isBusy = false;
        this.averageProcessingTime = processingTime;
    }

    @Override
    public void run() {
        try {
            isBusy = true;
            long timeOfProcessing = generateProcessingTime();
            Thread.sleep(timeOfProcessing);
            Statistic.recordAnEvent(new Note(SpecialEvents.DEVICE_RELEASE, ModelTimer.getCurrentTime(),currentBid.toString(),number,timeOfProcessing));
            isBusy = false;
            deviceRing.notifyAboutTheRelease();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException();
        }
    }

    public void start(final Thread consumerThread, final Bid bid){
        currentBid = bid;
        consumerThread.start();
    }

    private long generateProcessingTime(){//генерация времени по экспоненциальному закону
        return (long) (-averageProcessingTime * Math.log(Math.random()));
    }

}