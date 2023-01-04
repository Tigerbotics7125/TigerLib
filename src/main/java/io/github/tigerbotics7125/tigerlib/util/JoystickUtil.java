/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.util;

import edu.wpi.first.math.Pair;

public class JoystickUtil {

    /**
     * Multiplies a x by an exponent, preserving sign.
     *
     * @param x input
     * @param s sensitivity
     * @return preserve sign power.
     */
    public static double ramp(double x, double s) {
	return Math.signum(x) * Math.pow(Math.abs(x), s);
    }

    /**
     * Applies a deadband such that: [-infinity, -deadband] {0} [deadband,
     * infinity].
     *
     * @param x input
     * @param d deadband
     * @return a deadbanded value
     */
    public static double deadband(double x, double d) {
	if (d < 0)
	    throw new IllegalArgumentException("Deadband must be non-negative");
	return Math.abs(x) <= d ? 0 : x;
    }

    /**
     * Deadzone inputs.
     * <p>
     * Visualization
     * <a href="https://www.desmos.com/calculator/enoyillzg6">...</a>
     *
     * @param input    Pair of inputs, to deadzone.
     * @param deadzone how much to deadzone.
     * @return A deadzoned pair.
     */
    public static Pair<Double, Double> deadzone(Pair<Double, Double> input, double deadzone) {
	return Pair.of(deadband(input.getFirst(), deadzone), deadband(input.getSecond(), deadzone));
    }

    /**
     * Applies a clamp such that: [min, max].
     *
     * @param x   input
     * @param min lower boundery
     * @param max upper boundery
     * @return x within mix and max
     */
    public static double clamp(double x, double min, double max) {
	if (min > max)
	    throw new IllegalArgumentException("Invalid bounds, min must be smaller than max.");
	return Math.max(min, Math.min(x, max));
    }

    /**
     * Convert joystick values to magnitude and angle values.
     *
     * @param x input
     * @param y input
     * @return A pair of outputs, (x, y).
     */
    public static Pair<Double, Double> mapToCircle(double x, double y) {
	double alpha = Math.atan2(y, x);
	double magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	return Pair.of(magnitude * Math.cos(alpha), magnitude * Math.sin(alpha));
    }
}
