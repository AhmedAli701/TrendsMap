package com.cookiescode.trendsmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ahmed Ali on 06/04/2016.
 */
public class GraphLoader {
    public static Graph<Location> loadGraph(TagInfo tagInfo){
        // create empty Graph of Integer represent Tweet indices in a list.
        Graph<Location> hashGraph = new AdjacencyList<>();

        // get all tweets contains given hash tag and put them into array list to sort.
        List<Tweet> sortedList = new ArrayList<>(
                Main.dataSet.getTweets().get(tagInfo.getName()).stream().collect(Collectors.toList()));

        // sort tweets using date time.
        Collections.sort(sortedList);

        if(sortedList.size() > 1) {
            for (int i = 1; i < sortedList.size(); i++) {
                hashGraph.addVertex(sortedList.get(i - 1).getLocation(), sortedList.get(i).getLocation(), true);
            }
        }
        else if(sortedList.size() == 1) {
            hashGraph.addVertex(sortedList.get(0).getLocation());      // only one tweet in the list.
        }

        return hashGraph;
    }
}
