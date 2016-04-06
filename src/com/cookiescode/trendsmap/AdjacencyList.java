package com.cookiescode.trendsmap;

import java.util.HashMap;
import java.util.HashSet;

/**
 * this class represents an undirected graph,
 * uses an adjacency-list to representation.
 * Created by Ahmed Ali on 28/03/2016.
 */
public class AdjacencyList<T> extends Graph {

    private HashMap<T, HashSet<T>> graphMap;
    private int numVertices;
    private int numEdges;

    /**
     * Initializes an empty graph.
     */
    public AdjacencyList(){
        graphMap = new HashMap<>(numVertices);
        numVertices = 0;
        numEdges = 0;
    }

    /**
     * add new vertex and it's edge to the graph.
     * @param start the vertex to start from.
     * @param end second vertex.
     * @param isDirected true for directed edge, false for undirected.
     */
    @Override
    public void addVertex(Object start, Object end, boolean isDirected){
        addEdge(start, end, isDirected);
    }

    /**
     * add new vertex to the Graph.
     * @param vertex the vertex you want to add.
     * @return true if vertex were added or false if it's already exist.
     */
    @Override
    public boolean addVertex(Object vertex){
        if(!graphMap.containsKey(vertex)) {
            graphMap.put((T)vertex, new HashSet<>());
            numVertices++;
            return true;
        }
        else return false;
    }

    /**
     * get Number of vertices in the whole
     * graph.
     * @return : vertices number.
     */
    @Override
    public int getNumVertices(){
        return numVertices;
    }

    /**
     * get Number of edges in the whole
     * graph.
     * @return : edges number.
     */
    @Override
    public int getNumEdges(){
        return numEdges;
    }


    /**
     * add an edge between two vertices in the graph,
     * if any of the vertices doesn't exist it will
     * added automatically to the graph.
     * @param start first vertex.
     * @param end second vertex.
     * @param isDirected true for directed edge or false for undirected.
     */
    @Override
    public void addEdge(Object start, Object end, boolean isDirected) {
        if(!graphMap.containsKey(start))
            addVertex(start);
        if (!graphMap.containsKey(end))
            addVertex(end);

        if(isDirected){
            graphMap.get(start).add((T)end);
        }
        else {
            graphMap.get(start).add((T)end);
            graphMap.get(end).add((T)start);
        }
        numEdges++;
    }

    /**
     * Returns the vertices adjacent to vertex.
     * @param vertex the vertex
     * @return the vertices adjacent to vertex
     */
    @Override
    public HashSet<T> getAdjacent(Object vertex) {
        return graphMap.get(vertex);
    }

    /**
     * Returns the degree of vertex.
     * @param vertex the vertex.
     * @return the degree of vertex.
     */
    @Override
    public int getDegree(Object vertex) {
        return graphMap.get(vertex).size();
    }

    @Override
    public HashSet<T> getVertices() {
        return new HashSet<>(graphMap.keySet());
    }

    @Override
    public void clear() {
        graphMap.clear();
        numVertices = 0;
        numEdges = 0;
    }

    @Override
    public HashMap<T, HashSet<T>> exportGraph() {
        return new HashMap<>(this.graphMap);
    }

    /**
     * Returns a string representation of this graph.
     * @return the number of vertices,
     * followed by the number of edges,
     * followed by the adjacency lists.
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Vertices : " + numVertices + " Edges : " + numEdges + "\n");
        graphMap.forEach((v, w) -> {
            s.append("vertex " + v + " : ");
            w.forEach(edge -> s.append(" " + edge));
            s.append("\n");
        });
        return s.toString();
    }
}
