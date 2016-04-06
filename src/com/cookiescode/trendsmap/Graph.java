package com.cookiescode.trendsmap;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Ahmed Ali on 28/03/2016.
 */
public abstract class Graph<T> {
    public abstract void addVertex(T vertex, T edge);
    public abstract boolean addVertex(T vertex);
    public abstract void addEdge(T v, T w);
    public abstract int getNumVertices();
    public abstract int getNumEdges();
    public abstract HashSet<T> getAdjacent(T vertex);
    public abstract int getDegree(T vertex);
    public abstract HashSet<T> getVertices();
    public abstract void clear();
    public abstract HashMap<T, HashSet<T>> exportGraph();
}
