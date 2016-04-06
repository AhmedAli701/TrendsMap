package com.cookiescode.trendsmap;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {

    public static DataSet dataSet;
    public static Event<DataSet> dataLoaderEvent;
    public static Event<SimpleIntegerProperty> loaderStarted;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Initialize Data Set.
        dataSet = new DataSet();
        DataLoader dataLoader = new DataLoader(new FileInputStream("D:\\data\\twitter_data.txt"), dataSet);
        dataLoaderEvent = dataLoader.dataLoaderEvent;
        loaderStarted = dataLoader.loaderStarted;

        // Initialize Main Scene and Show it.
        Parent root = FXMLLoader.load(getClass().getResource("layouts/Map.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Load Tweets.
        dataLoader.loadTweets();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
