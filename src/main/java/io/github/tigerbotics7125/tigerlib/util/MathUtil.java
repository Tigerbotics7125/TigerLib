/*
 * Copyright (c) 2022-2023 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.util;

/**
 * Math formulas.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class MathUtil {
    /**
     * Changes the scale of a value from one range to another.
     *
     * @param x input
     * @param oldMin initial lower boundery
     * @param oldMax initial upper boundery
     * @param newMin new lower boundery
     * @param newMax new upper boundery
     * @return x scaled from old to new.
     */
    public static double minMaxScaling(
            double x, double oldMin, double oldMax, double newMin, double newMax) {
        if (oldMin > oldMax)
            throw new IllegalArgumentException(
                    "Invalid bounds, oldMin must be smaller than oldMax.");
        if (newMin > newMax)
            throw new IllegalArgumentException(
                    "Invalid bounds, newMin must be smaller than newMax.");
        if (x < oldMin || x > oldMax)
            throw new IllegalArgumentException("Input must be within old bounds.");

        return (x - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }

    /**
     * Generic PNPOLY algo for doubles.
     *
     * @param numVerts Number of verticies.
     * @param vertXs Static array of vertex x points.
     * @param vertYs Static array of vertex y points.
     * @param testX Test point X value.
     * @param testY Test point Y value.
     * @return Wheter the supplied (x, y) coordinate lies within the supplied polygon.
     */
    public static boolean pnpoly(
            int numVerts, double[] vertXs, double[] vertYs, double testX, double testY) {
        int i, j = 0;
        boolean c = false;
        for (i = 0, j = numVerts - 1; i < numVerts; j = i++) {
            if (((vertYs[i] > testY) != (vertYs[j] > testY))
                    && (testX
                            < (vertXs[j] - vertXs[i])
                                            * (testY - vertYs[i])
                                            / (vertYs[j] - vertYs[i])
                                    + vertXs[i])) c = !c;
        }
        return c;
    }

    /**
     * Generic PNPOLY algo for ints.
     *
     * @param numVerts Number of verticies.
     * @param vertXs Static array of vertex x points.
     * @param vertYs Static array of vertex y points.
     * @param testX Test point X value.
     * @param testY Test point Y value.
     * @return Wheter the supplied (x, y) coordinate lies within the supplied polygon.
     */
    public static boolean pnpoly(
            int numVerts, int[] vertXs, int[] vertYs, double testX, double testY) {
        int i, j = 0;
        boolean c = false;
        for (i = 0, j = numVerts - 1; i < numVerts; j = i++) {
            if (((vertYs[i] > testY) != (vertYs[j] > testY))
                    && (testX
                            < (vertXs[j] - vertXs[i])
                                            * (testY - vertYs[i])
                                            / (vertYs[j] - vertYs[i])
                                    + vertXs[i])) c = !c;
        }
        return c;
    }

    /**
     * @param x1
     * @param x2
     * @return The Euclidean distance between two points in 1D space.
     */
    public static double distance(double x1, double x2) {
        return Math.sqrt(Math.pow(x2 - x1, 2));
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return The Euclidean distance between two points in 2D space.
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return The Euclidean distance between two points in 3D space.
     */
    public static double distance(
            double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }
}
