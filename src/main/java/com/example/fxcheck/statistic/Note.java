package com.example.fxcheck.statistic;

public class Note {
    public SpecialEvents typeOfSpecialEvent;
    public long time;
    public String bid;
    public int numberOfDevice;
    public long timeOfProcessing;
    public boolean allDevicesIsBusy;
    public Note(final SpecialEvents typeOfSpecialEvent,final long time, String bid,int numberOfDevice,long timeOfProcessing){
        this.typeOfSpecialEvent = typeOfSpecialEvent;
        this.time = time;
        this.bid = bid;
        this.numberOfDevice = numberOfDevice;
        this.timeOfProcessing = timeOfProcessing;
    }

    public Note(final SpecialEvents typeOfSpecialEvent,final long time, String bid, boolean allDevicesIsBusy){
        this.typeOfSpecialEvent = typeOfSpecialEvent;
        this.time = time;
        this.bid = bid;
        this.allDevicesIsBusy = allDevicesIsBusy;
        this.numberOfDevice = 0;
    }

    @Override
    public String toString() {
        if(numberOfDevice == 0 ){
            return typeOfSpecialEvent + " " + bid + " " + time + " allDevicesIsBusy = " + allDevicesIsBusy ;
        }
        else{
            return typeOfSpecialEvent + " " + bid + " " + time + " device " + numberOfDevice + " time of processing = " + timeOfProcessing;
        }
    }
}