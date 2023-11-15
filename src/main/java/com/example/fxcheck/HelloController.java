package com.example.fxcheck;

import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.fxcheck.model.Bid;
import com.example.fxcheck.statistic.Note;
import com.example.fxcheck.statistic.SpecialEvents;
import com.example.fxcheck.statistic.Statistic;
import com.example.fxcheck.ui.RectangleWithText;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox BufferHBox;
    @FXML
    private Label BufferLabel;

    @FXML
    private Button ButtonDoStep;

    @FXML
    private Group Producer1;

    @FXML
    private Label StepByStepLabel;

    @FXML
    private Label TModelChangeable;

    @FXML
    private Label TModelLabel;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    void initialize() {
        int sizeOfBuffer = 2;
        double widthRectangleWithText = 80;
        BufferHBox.setPrefWidth(80*sizeOfBuffer);
        RectangleWithText rectangleProducer1 = generateRectangleWithText("И-1",widthRectangleWithText);
        RectangleWithText rectangleProducer2 = generateRectangleWithText("И-2",widthRectangleWithText);
        RectangleWithText rectangleConsumer1 = generateRectangleWithText("П-1",widthRectangleWithText);
        RectangleWithText rectangleConsumer2 = generateRectangleWithText("П-2",widthRectangleWithText);




        VBox producerVbox = new VBox(10);
        VBox consumerVbox = new VBox(10);
        configureTheVBox(consumerVbox,710,120,5,10);
        configureTheVBox(producerVbox,40,120,5,10);

        producerVbox.getChildren().addAll(new Group(rectangleProducer1.rectangle,rectangleProducer1.text));
        producerVbox.getChildren().addAll(new Group(rectangleProducer2.rectangle,rectangleProducer2.text));
        consumerVbox.getChildren().addAll(new Group(rectangleConsumer1.rectangle,rectangleConsumer1.text));
        consumerVbox.getChildren().addAll(new Group(rectangleConsumer2.rectangle,rectangleConsumer2.text));
        anchorPane.getChildren().add(producerVbox);
        anchorPane.getChildren().add(consumerVbox);
        LinkedList<Note> statistic = Statistic.getStatistic();
        ButtonDoStep.setOnAction(event -> {
            if(statistic.size() == 0){
                TModelChangeable.setText("КОНЕЦ МОДЕЛИРОВАНИЯ");
                return;
            }
            Note currentNote = statistic.poll();
            TModelChangeable.setText(String.valueOf(currentNote.time) + "мс");
            int currentSizeOfHBuffer = BufferHBox.getChildren().size();
            switch (currentNote.typeOfSpecialEvent){
                case GENERATION -> {
                    RectangleWithText bidToBuffer = generateRectangleWithText(currentNote.bid, widthRectangleWithText);
                    Group bidToBufferGroup = new Group(bidToBuffer.rectangle, bidToBuffer.text);
                    if(currentNote.allDevicesIsBusy){
                        if(currentSizeOfHBuffer > sizeOfBuffer-1){//если буфер заполнен
                            pollBuffer();
                        }
                        putInBuffer(bidToBufferGroup);
                    }
                    else{//если есть хотя бы один свободный девайс.
                        if(currentSizeOfHBuffer == 0){
                            //putInBuffer(bidToBufferGroup);
//                            BufferHBox.setStyle("-fx-background-color: dodgerblue;");
//                            try {
//                                Thread.sleep(5000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            BufferHBox.setStyle("-fx-background-color: #1a162a;");

                            // pollBuffer();
                            int numberOfConsumer = findConsumerInNotesByBidId(statistic,currentNote.bid);
                            if(numberOfConsumer == 1){
                                rectangleConsumer1.rectangle.setFill(Color.RED);
                            }
                            else if(numberOfConsumer == 2){
                                rectangleConsumer2.rectangle.setFill(Color.RED);
                            }
                            else{
                                throw new RuntimeException("Опачки. Не нашел он прибора в статистике");
                            }
                        }
                        else{
                            throw new RuntimeException("Странная херня, что за коллизии?");
                        }
                    }
                }
                case DEVICE_RELEASE -> {
                    if (currentSizeOfHBuffer > 0){//пусть он берет ее из буффера
                        pollBuffer();
                    }
                    else {
                        int numberOfCurrentDevice = currentNote.numberOfDevice;
                        if(numberOfCurrentDevice == 1){
                            rectangleConsumer1.rectangle.setFill(Color.DODGERBLUE);
                        }
                        else if(numberOfCurrentDevice == 2){
                            rectangleConsumer2.rectangle.setFill(Color.DODGERBLUE);
                        }
                        else{
                            throw new RuntimeException("Какой то бред");
                        }
                    }
                }
            }
        });

    }

    private void putInBuffer(final Group group){
        BufferHBox.getChildren().add(0,group);
    }
    private void pollBuffer(){
        BufferHBox.getChildren().remove(0);
    }

    private int findConsumerInNotesByBidId(final LinkedList<Note> statistic, final String bidId){//надо протестить
        for(Note note : statistic){
            if(Objects.equals(note.bid, bidId)){
                return note.numberOfDevice;
            }
        }
        return 0;
    }
    private RectangleWithText generateRectangleWithText(final String text,final double widthOfRectangleWithText){
        Rectangle rectangle = generateRectangle(widthOfRectangleWithText);
        return new RectangleWithText(rectangle,
                generateText(rectangle.getX(),rectangle.getY(),text)
        );
    }
    private  Rectangle generateRectangle(final double widthOfRectangleWithText){
        final double widthOfRectangle = widthOfRectangleWithText;
        final double heightOfRectangle = 50;
        Rectangle rectangle = new Rectangle(widthOfRectangle, heightOfRectangle); // Ширина: 100, Высота: 50
        rectangle.setFill(Color.DODGERBLUE); // Установка цвета прямоугольника
        return rectangle;
    }

    public Text generateText(double x, double y, String label){

        // Создание текста
        Text text = new Text(label);
        text.setFont(Font.font("Arial", 12)); // Установка шрифта текста
        text.setLayoutX(x + 10); // Смещение текста по X относительно прямоугольника
        text.setLayoutY(y + 30); // Смещение текста по Y относительно прямоугольника
        return text;
    }
    public void configureTheVBox(final VBox vbox ,double x, double y,double spacing, int padding){
        vbox.setLayoutX(x);
        vbox.setLayoutY(y);
        vbox.setSpacing(spacing);
        vbox.setPadding(new Insets(padding));
    }
}
