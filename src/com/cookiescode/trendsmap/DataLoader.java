package com.cookiescode.trendsmap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Ahmed Ali on 31/03/2016.
 */
public class DataLoader {
    private InputStream in;                                  // data set stream.
    private DataSet dataSet;                                 // data set instance.
    private HashMap<String, HashSet<Tweet>> tweets;          // tweets map key : hash tag - value : tweets.
    public Event<DataSet> dataLoaderFinished;
    public Event<SimpleIntegerProperty> dataLoaderStarted;
    private int tweetsCount = 0;
    private SimpleIntegerProperty tweetsCounter = new SimpleIntegerProperty(0);

    public DataLoader(InputStream in, DataSet dataSet){
        this.dataSet = dataSet;
        dataLoaderFinished = new Event<>();
        dataLoaderStarted = new Event<>();

        // initialize new map for the hash tags.
        tweets = new HashMap<>();
        dataSet.setTweets(tweets);

        this.in = in;
    }

    public void loadTweets() throws IOException{
        Task loadTask = createTask();
        tweetsCounter.bind(loadTask.workDoneProperty());
        new Thread(loadTask).start();
        dataLoaderStarted.invoke(tweetsCounter);
    }

    private Task createTask(){
        return new Task() {
            @Override
            protected Object call() throws Exception {

                Scanner scanner = new Scanner(in);
                while (scanner.hasNext()){
                    String line = scanner.nextLine();
                    String[] parts = line.split("[\\p{Blank}]+");

                    try {
                        long user, id;
                        String date = "";
                        String time = "";
                        List<String> hashs = new ArrayList<>();
                        user = Long.parseLong(parts[0]);
                        id = Long.parseLong(parts[1]);

                        for (int i = 2; i < parts.length - 3; i++) {
                            if(parts[i].contains("#"))
                                hashs.add(parts[i]);
                        }

                        date = parts[parts.length - 3] + " " + parts[parts.length - 2];
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        DateTime dt = formatter.parseDateTime(date);

                        String[] loc = parts[parts.length - 1].split(",");

                        Tweet tweet = new Tweet(
                                id, user, hashs,
                                new Location(Double.parseDouble(loc[0]), Double.parseDouble(loc[1])), dt);
                        tweetsCount++;

                        hashs.forEach(ha -> {
                            if(tweets.containsKey(ha))
                                tweets.get(ha).add(tweet);
                            else {
                                HashSet<Tweet> tw = new HashSet<>();
                                tw.add(tweet);
                                tweets.put(ha, tw);
                            }
                            updateProgress(tweetsCount, tweetsCount);
                        });
                    }
                    catch (Exception ex){
                        /*
                        ex.printStackTrace();
                        System.out.println(line);
                        break;
                        */
                    }
                }
                dataLoaderFinished.invoke(dataSet);
                return true;
            }
        };
    }
}
