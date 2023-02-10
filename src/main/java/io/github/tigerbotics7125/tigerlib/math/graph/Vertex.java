/*
 * Copyright (c) 2023 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.math.graph;

import java.util.Objects;

/**
 * A generic class which holds data for a graph.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @since 2023
 */
public class Vertex<T> {

    // Storage of this vertex.
    private T mData;

    /**
     * Creates a new Vertex.
     *
     * @param data The data to store, it must implement a valid equals and hashCode method.
     */
    public Vertex(T data) {
        mData = data;
    }

    /**
     * A shortcut for the constructor.
     *
     * @param <T> Type of the returned Vertex.
     * @param data The data to store, it must implement a valid equals and hashCode method.
     * @return
     */
    public static <T> Vertex<T> of(T data) {
        return new Vertex<T>(data);
    }

    /** @return The data stored in this Vertex. */
    public T getData() {
        return mData;
    }

    public String toString() {
        return "Vertex: {" + mData.toString() + "}";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object other) {
        if (other instanceof Vertex<?> v) {
            try {
                // warns unchecked.
                Vertex<T> vertex = (Vertex<T>) v;
                return vertex.getData().equals(mData);
            } catch (ClassCastException cce) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mData);
    }
}
