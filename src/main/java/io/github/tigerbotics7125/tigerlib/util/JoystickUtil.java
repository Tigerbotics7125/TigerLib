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
     * Applies a deadband such that: [-infinity, -deadband] {0} [deadband, infinity].
     *
     * @param x input
     * @param d deadband
     * @return a deadbanded value
     */
    public static double deadband(double x, double d) {
        if (d < 0) throw new IllegalArgumentException("Deadband must be non-negative");
        return Math.abs(x) <= d ? 0 : x;
    }

    /**
     * Deadzone inputs.
     *
     * <p>Visualization <a href="https://www.desmos.com/calculator/enoyillzg6">...</a>
     *
     * @param input Pair of inputs, to deadzone.
     * @param deadzone how much to deadzone.
     * @return A deadzoned pair.
     */
    public static Pair<Double, Double> deadzone(Pair<Double, Double> input, double deadzone) {
        return Pair.of(deadband(input.getFirst(), deadzone), deadband(input.getSecond(), deadzone));
    }

    /**
     * Applies a clamp such that: [min, max].
     *
     * @param x input
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
     * Converts the typical joystick "diamond" which is a square rotated 45 degrees with lines going
     * from (0,1) to (1, 0) and so forth for the other quadrents, to a square which spans (-1, -1)
     * to (1, 1).
     *
     * @param input Original joystick input [-1, 1].
     * @return A Pair object with the converted input.
     */
    public static Pair<Double, Double> mapToSquare(Pair<Double, Double> input) {
        double x = input.getFirst();
        double y = input.getSecond();

        // get the quadrent the joystick is in currently
        int quadrent = 1;
        if (x > 0 && y > 0) quadrent = 1;
        else if (x < 0 && y > 0) quadrent = 2;
        else if (x < 0 && y < 0) quadrent = 3;
        else if (x > 0 && y < 0) quadrent = 4;

        // Convert joystick diamond to [(-1, -1), (1, 1)] square.
        return switch (quadrent) {
            case 1 -> {
                double a = 1;
                double b = 1;
                double c = -1;
                double d =
                        Math.abs((a * x) + (b * y) - c)
                                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

                double x2 = x / (d + x);
                double y2 = y / (d + y);

                yield Pair.of(x2, y2);
            }
            case 2 -> {
                double a = 1;
                double b = -1;
                double c = 1;
                double d =
                        Math.abs((a * x) + (b * y) - c)
                                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

                double x2 = x / (d - x);
                double y2 = y / (d + y);

                yield Pair.of(x2, y2);
            }
            case 3 -> {
                double a = 1;
                double b = 1;
                double c = 1;
                double d =
                        Math.abs((a * x) + (b * y) - c)
                                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

                double x2 = x / (d - x);
                double y2 = y / (d - y);

                yield Pair.of(x2, y2);
            }
            case 4 -> {
                double a = -1;
                double b = 1;
                double c = 1;
                double d =
                        Math.abs((a * x) + (b * y) - c)
                                / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

                double x2 = x / (d - x);
                double y2 = y / (d + y);

                yield Pair.of(x2, y2);
            }
            default -> Pair.of(0.0, 0.0);
        };
    }
}
