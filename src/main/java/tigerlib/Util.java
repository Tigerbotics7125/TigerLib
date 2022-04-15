/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib;

public class Util {

    /**
     * Multiplies a x by an exponent, preserving sign.
     *
     * @param x input
     * @param s sensitivity
     * @return
     */
    public static double ramp(double x, double s) {
        return Math.signum(x) * Math.pow(Math.abs(x), s);
    }

    /**
     * Applies a deadband such that: [-infinity, -deadband] {0} [deadband, infinity].
     *
     * @param x input
     * @param d deadband
     */
    public static double deadband(double x, double d) {
        if (d < 0) throw new IllegalArgumentException("Deadband must be non-negative");
        return Math.abs(x) <= d ? 0 : x;
    }

    /**
     * Applies a clamp such that: [min, max].
     *
     * @param x input
     * @param min lower boundery
     * @param max upper boundery
     */
    public static double clamp(double x, double min, double max) {
        if (min > max)
            throw new IllegalArgumentException("Invalid bounds, min must be smaller than max.");
        return Math.max(min, Math.min(x, max));
    }

    /**
     * Applies a deadband such that: [-infinity, -deadband] {0} [deadband, infinity], while x^exp
     * preserving sign.
     *
     * @param x input
     * @param d deadband
     * @param s sensitivity
     */
    public static double smooth(double x, double d, double s) {
        return deadband(ramp(x, s), d);
    }

    /**
     * Applies {@link #smooth(x, d, s)} then {@link #clamp(x, min, max)}.
     *
     * @param x input
     * @param d deadband
     * @param s sensitivity
     * @param min lower boundery
     * @param max upper boundery
     */
    public static double smoothWClamp(double x, double d, double s, double min, double max) {
        return clamp(smooth(x, d, s), min, max);
    }

    /**
     * Changes the scale of a value from one range to another.
     *
     * @param x input
     * @param oldMin initial lower boundery
     * @param oldMax initial upper boundery
     * @param newMin new lower boundery
     * @param newMax new upper boundery
     */
    public static double scaleInput(
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
}
