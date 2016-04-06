package com.cookiescode.trendsmap;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Ahmed Ali on 31/03/2016.
 */
public class DataLoader {
    private HashMap<String, HashSet<Tweet>> tweets;
    private Graph<HashTag> graph;
    private InputStream in;
    public Event<DataSet> dataLoaderEvent;
    public Event<SimpleIntegerProperty> loaderStarted;
    private DataSet dataSet;
    private int tweetsCount = 0;
    private SimpleIntegerProperty tweetsCounter = new SimpleIntegerProperty(0);

    public DataLoader(InputStream in, DataSet dataSet){
        this.dataSet = dataSet;
        dataLoaderEvent = new Event<>();
        loaderStarted = new Event<>();

        // initialize new map for the hash tags.
        tweets = new HashMap<>();
        dataSet.setTweets(tweets);


        // initialize the Graph
        graph = new AdjacencyList<>();
        dataSet.setGraph(graph);

        this.in = in;
    }

    public void loadTweets() throws IOException{
        Task loadTask = createTask();
        tweetsCounter.bind(loadTask.workDoneProperty());
        new Thread(loadTask).start();
        loaderStarted.invoke(tweetsCounter);
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
                        String text = "";
                        String date = "";
                        String time = "";
                        List<String> hashs = new ArrayList<>();
                        user = Long.parseLong(parts[0]);
                        id = Long.parseLong(parts[1]);

                        for (int i = 2; i < parts.length - 3; i++) {

                            if(parts[i].contains("#"))
                                hashs.add(parts[i]);

                            text += parts[i];
                            if (i < parts.length - 4)
                                text += " ";
                        }



                        date = parts[parts.length - 3] + " " + parts[parts.length - 2];
                        String[] loc = parts[parts.length - 1].split(",");

                        Tweet tweet = new Tweet(
                                id, user, text, hashs, new Location(Double.parseDouble(loc[0]), Double.parseDouble(loc[1])));
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
                        ex.printStackTrace();
                        System.out.println(line);
                        break;
                    }
                }
                dataLoaderEvent.invoke(dataSet);
                return true;
            }
        };
    }
}
