/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.math.graph;

import io.github.tigerbotics7125.tigerlib.math.Tuple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class represents a collection of vertecies and edges defining traditional weighted graph.
 *
 * @since 2023
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class Graph<T> {
    // maps a Vertex to a list of other verticies with the respective weight.
    private Map<Vertex<T>, List<Tuple<Vertex<T>, Double>>> mVertexEdgeMap;

    /** Creates a new Graph. */
    public Graph() {
        mVertexEdgeMap = new HashMap<>();
    }

    /** @return A Stream object of all the verticies in this Graph Object. */
    public Stream<Vertex<T>> getVerticies() {
        return mVertexEdgeMap.keySet().stream();
    }

    /**
     * @param vertex Vertex to get successors from.
     * @return The successors or neighbors of the given Vertex.
     */
    public List<Tuple<Vertex<T>, Double>> getSuccessors(Vertex<T> vertex) {
        return mVertexEdgeMap.get(vertex);
    }

    /**
     * @param vertexVal Value to be searched for.
     * @return Whether this Graph contains a Vertex with the given value.
     */
    public boolean contains(T vertexVal) {
        return contains(Vertex.of(vertexVal));
    }

    /**
     * @param vertex Vertex to be searched for.
     * @return Whether this Graph contains the provided Vertex.
     */
    public boolean contains(Vertex<T> vertex) {
        return mVertexEdgeMap.containsKey(vertex);
    }

    /**
     * Adds the value as a Vertex to this Graph.
     *
     * @param vertexVal Value to add.
     */
    public void addVertex(T vertexVal) {
        addVertex(Vertex.of(vertexVal));
    }

    /**
     * Adds the Vertex to this Graph.
     *
     * @param vertex Vertex to add.
     */
    public void addVertex(Vertex<T> vertex) {
        mVertexEdgeMap.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Add an edge to this Graph between two verticies defined by the provided values.
     *
     * @param leftVal Value of the left Vertex.
     * @param rightVal Value of the right Vertex.
     * @param weight Travel cost between left and right, equal both ways.
     */
    public void addEdge(T leftVal, T rightVal, double weight) {
        addEdge(leftVal, rightVal, weight, weight);
    }

    /**
     * Add an edge to this Graph between two verticies defined by the provided values.
     *
     * @param leftVal Value of the left Vertex.
     * @param rightVal Value of the right Vertex.
     * @param weightLR Travel cost from left to right.
     * @param weightRL Travel cost from right to left.
     */
    public void addEdge(T leftVal, T rightVal, double weightLR, double weightRL) {
        Vertex<T> left = Vertex.of(leftVal);
        Vertex<T> right = Vertex.of(rightVal);

        addEdge(left, right, weightLR, weightRL);
    }

    /**
     * Add an edge to this Graph between two verticies.
     *
     * @param left Left Vertex.
     * @param right Right Vertex.
     * @param weight Travel cost between left and right, equal both ways.
     */
    public void addEdge(Vertex<T> left, Vertex<T> right, double weight) {
        addEdge(left, right, weight, weight);
    }

    /**
     * Add an edge to the Graph between two verticies.
     *
     * @param left Left Vertex.
     * @param right Right Vertex.
     * @param weightLR Travel cost from left to right.
     * @param weightRL Travel cost from right to left.
     */
    public void addEdge(Vertex<T> left, Vertex<T> right, double weightLR, double weightRL) {
        if (weightLR > 0.0) mVertexEdgeMap.get(left).add(Tuple.of(right, weightLR));
        if (weightRL > 0.0) mVertexEdgeMap.get(right).add(Tuple.of(left, weightRL));
    }
}
