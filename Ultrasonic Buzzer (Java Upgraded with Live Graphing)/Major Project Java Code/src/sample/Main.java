package sample;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;
import sample.DataController;
import sample.SerialPortService;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {launch(args);}

//    setting up data table which contains the voltage levels at specific times
    private static TableView<XYChart.Data<Number, Number>> getTableView() {

        var table = new TableView<XYChart.Data<Number, Number>>(); //1
        var time = new TableColumn<XYChart.Data<Number,Number> , Number>("x");//2
        time.setCellValueFactory(row -> row.getValue().XValueProperty()); //3
        var dateFormat = DateFormat.getTimeInstance();//4
        var converter = new FormatStringConverter<Number>(dateFormat); //5
        time.setCellFactory(column -> new TextFieldTableCell<>(converter)); //6
        var value = new TableColumn<XYChart.Data<Number,Number> , Number> ("y");
        value.setCellValueFactory(row -> row.getValue().YValueProperty()); //7
        table.getColumns().setAll(List.of(time, value));
        return table;

    }

    @Override
    public void start(Stage primaryStage) {

        var controller = new DataController(OutputStream.nullOutputStream());

        //setting up the serial port
        var sp = SerialPortService.getSerialPort("COM8");
        var outputStream = sp.getOutputStream();
        sp.addDataListener(controller);




        //creating the data table
        var table = getTableView();
        table.setItems(controller.getDataPoints());

        var vbox = new VBox(table);


        //name the window
        primaryStage.setTitle("Ultrasonic Distance Program");
        //create main pane
        var pane = new BorderPane();

        //find current time
        var now = System.currentTimeMillis();

        //setting up axis for the chart
        var xAxis = new NumberAxis("time (ms since Jan 1, 1970)", now, now + 100000, 10000); // creates the x-axis (which automatically updates)
        var yAxis = new NumberAxis("Distance Measured (cm)", 0, 500, 1); // creates the y-axis


        var series = new XYChart.Series<>(controller.getDataPoints()); // creates the series (all the data)

        
        var lineChart = new LineChart<>(xAxis, yAxis, FXCollections.singletonObservableList(series)); // creates the chart
        lineChart.setTitle("Distance Measured Vs Time");





        //create button that allows you to activate the pump manually
        var button = new Button("Blink LED");

        button.setOnMousePressed(value -> {
            try{
                outputStream.write(255);
                System.out.println("Blink LED");
            }catch(IOException e){
                e.printStackTrace();
            }
        });

        button.setOnMouseReleased(value -> {
            try{
                outputStream.write(254);
            }catch(IOException e){
                e.printStackTrace();
            }
        });


        pane.setTop(button);//add button to the pane
        pane.setLeft(lineChart); // add line chart to the pane
        pane.setCenter(vbox); //add table to the pane
        pane.setPadding(new Insets(0, 30, 0, 30));

        var scene = new Scene(pane, 750, 500);


        primaryStage.setScene(scene);

        primaryStage.show();

    }
}

