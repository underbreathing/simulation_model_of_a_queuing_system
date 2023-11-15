package com.example.fxcheck.deviceManager;

import com.example.fxcheck.consumer.Consumer;

import java.util.ArrayList;

public class DeviceRing{

    public DeviceRing(){
        this.devicePointer = 0;
        this.size = 0;
    }
    public static int countOfFreeDevices = 0 ;
    private  int devicePointer ;
    private int size;
    private final static ArrayList<Consumer> deviceArrayList = new ArrayList<>();

    public synchronized void notifyAboutTheRelease(){
        countOfFreeDevices++;
    }
    //возможно придется отладить эту функцию
    public  Consumer getFreeDevice(){//return null if all devices is busy now
        for(int i = 0; i < size;++i){
            Consumer currentDevice = deviceArrayList.get(devicePointer);
            if(!currentDevice.isBusy){//если не занят - передвигаем и возвращаем прибор
                movePointer();
                return currentDevice;
            }
            else{//если занят - передвигаем
                movePointer();
            }

        }
        return null;
    }

    private void movePointer(){//передвинуть указатель (по кольцу)
        ++devicePointer;
        if(devicePointer == size){
            devicePointer = 0;
        }
    }
    public void addDevice(Consumer device){
        deviceArrayList.add(device);
        countOfFreeDevices++;
        size++;
    }
}
