/*
 * Copyright (c) 2023 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.math.graph;

import io.github.tigerbotics7125.tigerlib.math.Tuple;
import java.util.*;
import java.util.function.Function;

/**
 * This class represents an A* algorithm as a class, which can be used continueously over its Graph.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @since 2023
 */
public class AStar<T> {

    // Graph of which this AStar instance will operate over.
    private Graph<T> mGraph;

    // Function which takes in two verticies and returns their calculated heuristic.
    private Function<Tuple<Vertex<T>, Vertex<T>>, Double> mHeuristicFunction;

    // Map between a vertex and its costs, the tuple is in order of (f, g).
    private Map<Vertex<T>, Tuple<Double, Double>> mVertexCostMap;

    // Map representing which Vertex "cameFrom" which Vertex.
    private Map<Vertex<T>, Vertex<T>> mCameFrom;

    // PriorityQueue is used to track open values as it auto sorts the values (yay!)
    // and is eff%!
    private PriorityQueue<Vertex<T>> mOpenQueue;

    /**
     * @param graph The Graph of which this AStar instance will operate over.
     * @param hFunc The function which calculates the heuristic from any two Verticies.
     */
    public AStar(Graph<T> graph, Function<Tuple<Vertex<T>, Vertex<T>>, Double> hFunc) {
        mGraph = graph;
        mHeuristicFunction = hFunc;
        mVertexCostMap = new HashMap<>();
        mCameFrom = new HashMap<>();
        mOpenQueue =
                new PriorityQueue<>(
                        new Comparator<Vertex<T>>() {
                            @Override
                            public int compare(Vertex<T> v1, Vertex<T> v2) {
                                var defVal =
                                        Tuple.of(
                                                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                                double f1 = mVertexCostMap.getOrDefault(v1, defVal).getFirst();
                                double f2 = mVertexCostMap.getOrDefault(v2, defVal).getFirst();
                                return Double.compare(f1, f2);
                            }
                        });
    }

    /**
     * Shortcut for accessing the heuristic function.
     *
     * @param v1
     * @param v2
     * @return The heuristic between the two Verticies.
     */
    public double h(Vertex<T> v1, Vertex<T> v2) {
        return mHeuristicFunction.apply(Tuple.of(v1, v2));
    }

    /**
     * Generate the path from their Vertex to Vertex mapping.
     *
     * @param cameFrom Map representing which Vertex "cameFrom" which Vertex.
     * @param vertex The last vertex in the list.
     * @return The ordered list of verticies held in the map.
     */
    public List<Vertex<T>> genPath(Map<Vertex<T>, Vertex<T>> cameFrom, Vertex<T> vertex) {
        LinkedList<Vertex<T>> path = new LinkedList<>();
        path.add(vertex);

        while (cameFrom.containsKey(vertex)) {
            Vertex<T> parent = cameFrom.get(vertex);

            path.addFirst(parent);
            vertex = parent;
        }

        return path;
    }

    /**
     * @param start The starting Vertex.
     * @param end The ending, goal Vertex.
     * @return The list of Verticies, start to end, which represents the most cost effecient way of
     *     traversing the graph.
     */
    public List<Vertex<T>> astar(Vertex<T> start, Vertex<T> end) {

        if (!mGraph.contains(start))
            throw new IllegalArgumentException("Graph must contain starting vertex");
        if (!mGraph.contains(end))
            throw new IllegalArgumentException("Graph must contain ending vertex");

        // Clear values from previous runs.
        mVertexCostMap.clear();
        mCameFrom.clear();
        mOpenQueue.clear();

        // Fill map and set default to +inf.
        mGraph.getVerticies()
                .forEach(
                        v ->
                                mVertexCostMap.put(
                                        v,
                                        Tuple.of(
                                                Double.POSITIVE_INFINITY,
                                                Double.POSITIVE_INFINITY)));

        // start node f score is simply the heuristic of start and end. g cost is zero
        // obviously,
        // its the first Vertex.
        mVertexCostMap.put(start, Tuple.of(h(start, end), 0.0));

        // Give the open queue starting Vertex.
        mOpenQueue.offer(start);

        // Main A* logic loop.
        while (!mOpenQueue.isEmpty()) {
            // Get current node, its the node with the smallest f cost.
            Vertex<T> currentVertex = mOpenQueue.poll();

            // If current vertex is the final vertex, we're done!
            if (currentVertex.equals(end)) return genPath(mCameFrom, currentVertex);

            // Add successors (verticies connected to the current one) to the queue if
            // applicable.
            // Loop iterates over "currentVertex"s children and the g cost to reach them.
            for (Tuple<Vertex<T>, Double> child : mGraph.getSuccessors(currentVertex)) {

                // g(start -> this) = g(start -> parent) + g(parent -> this).
                double tentativeG = /* Parent */
                        mVertexCostMap.get(currentVertex).getFirst()
                                +
                                /* child */ child.getSecond();

                // If travel cost is lower than any previously calculated cost (or default),
                // replace
                // that info.
                if (tentativeG < mVertexCostMap.get(child.getFirst()).getSecond()) {

                    mCameFrom.put(child.getFirst(), currentVertex);

                    double fCost = tentativeG + h(child.getFirst(), end);
                    mVertexCostMap.put(child.getFirst(), Tuple.of(fCost, tentativeG));

                    // Re-add the child to the queue now that its updated again. (does nothing if
                    // queue already contains child).
                    mOpenQueue.offer(child.getFirst());
                }
            }
        }
        throw new IllegalArgumentException(
                "Graph does not contain a valid path between start and end.");
    }
}
