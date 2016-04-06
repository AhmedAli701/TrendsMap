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
     * @param vertex : the vertex.
     * @param edge : undirected edge for the given vertex.
     */
    public void addVertex(Object vertex, Object edge){
        addVertex((T)vertex);              // add vertex if not exist.
        addVertex((T)edge);                // add second vertex if not exist.
        addEdge((T)vertex, (T)edge);          // add undirected edge between those vertices.
    }

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

    // add undirected edge to those vertices.
    @Override
    public void addEdge(Object v, Object w) {
        graphMap.get(v).add((T)w);
        graphMap.get(w).add((T)v);
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
