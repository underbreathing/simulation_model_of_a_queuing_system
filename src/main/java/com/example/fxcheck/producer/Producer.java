package com.example.fxcheck.producer;

import com.example.fxcheck.bidBroker.BidBroker;
import com.example.fxcheck.model.Bid;

public class Producer implements Runnable{
    public static int amountOfProducers;

    private final int numberOfProducer;
    private int numberOfGeneratedBid;
    private final BidBroker bidBroker;
    private final long sleepTime;


    public Producer(final long sleepTime, final BidBroker bidBroker){
        numberOfGeneratedBid = 1;
        this.sleepTime = sleepTime;
        this.bidBroker = bidBroker;
        this.numberOfProducer = ++amountOfProducers;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            final Bid bid = create();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            bidBroker.put(bid);
        }
    }

    private Bid create(){
        return new Bid(numberOfProducer, numberOfGeneratedBid++);
    }


}
