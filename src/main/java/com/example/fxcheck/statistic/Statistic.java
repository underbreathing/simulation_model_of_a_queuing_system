package com.example.fxcheck.statistic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Statistic {
    private static final Queue<Note> notes = new LinkedList<>();

    public static synchronized void  recordAnEvent(final Note note){
        notes.add(note);
    }

    public static void outputInformation(){
        for(Note note : notes){
            System.out.println(note);
        }
    }

    public static LinkedList<Note> getStatistic(){
        return new LinkedList<>(notes);
    }
}
