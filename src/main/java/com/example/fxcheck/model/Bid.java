package com.example.fxcheck.model;

import java.util.Objects;

public class Bid {
    private final int numberOfProducer;
    private final int number;//номер сообщения

    public Bid(final int numberOfProducer,final int number){
        this.numberOfProducer = numberOfProducer;
        this.number = number;
    }

    public String getBidId(){
        return numberOfProducer + "." + number;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.number);
    }

    @Override
    public String toString() {
        return numberOfProducer + "-" + number;
    }
}