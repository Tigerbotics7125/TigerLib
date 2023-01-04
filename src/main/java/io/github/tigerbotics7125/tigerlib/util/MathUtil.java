/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
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
     * @param x      input
     * @param oldMin initial lower boundery
     * @param oldMax initial upper boundery
     * @param newMin new lower boundery
     * @param newMax new upper boundery
     * @return x scaled from old to new.
     */
    public static double minMaxScaling(double x, double oldMin, double oldMax, double newMin, double newMax) {
	if (oldMin > oldMax)
	    throw new IllegalArgumentException("Invalid bounds, oldMin must be smaller than oldMax.");
	if (newMin > newMax)
	    throw new IllegalArgumentException("Invalid bounds, newMin must be smaller than newMax.");
	if (x < oldMin || x > oldMax)
	    throw new IllegalArgumentException("Input must be within old bounds.");

	return (x - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }
}
