package com.example.fxcheck;

import com.example.fxcheck.bidBroker.BidBroker;
import com.example.fxcheck.consumer.Consumer;
import com.example.fxcheck.deviceManager.DeviceManager;
import com.example.fxcheck.deviceManager.DeviceRing;
import com.example.fxcheck.producer.Producer;
import com.example.fxcheck.statistic.ModelTimer;
import com.example.fxcheck.statistic.Statistic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("SMO");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Введите продолжительность генерации в секундах : ");
        Scanner scanner = new Scanner(System.in);
        long timeOfExperimentMillis = scanner.nextLong() * 1000;
        ModelTimer.initializeStartTime();
        final BidBroker bidBroker = new BidBroker(2);
        final DeviceRing deviceRing = new DeviceRing();
        final DeviceManager deviceManager = new DeviceManager(bidBroker,deviceRing);
        deviceManager.addConsumer(new Consumer(1,2000,deviceRing));
        deviceManager.addConsumer(new Consumer(2,3500,deviceRing));


        final Thread firstProducer = new Thread(new Producer(2000,bidBroker));
        final Thread secondProducer = new Thread(new Producer(3000,bidBroker));
        final Thread deviceManagerThread = new Thread(deviceManager);
        startThreads(firstProducer,secondProducer,deviceManagerThread);

        Thread.sleep(timeOfExperimentMillis);

        interruptThreads(firstProducer,secondProducer);

        Thread.sleep(15000);//чтобы приборы доработали
        deviceManagerThread.interrupt();
        System.out.println("Подождали, теперь выводим статистику генерации заявок - > ");
        Statistic.outputInformation();
        launch();
    }
    private static void interruptThreads(final Thread... threads){
        Arrays.stream(threads).forEach(Thread::interrupt);
    }

    private static void startThreads(final Thread... threads){
        Arrays.stream(threads).forEach(Thread::start);
    }

}