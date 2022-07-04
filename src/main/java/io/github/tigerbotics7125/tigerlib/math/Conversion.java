/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.math;

import java.util.function.Function;

/**
 * Represent a conversion factor from any measurement to another.
 *
 * <p>For instance you can have a {@code Conversion<Integer, Double>} which could convert between
 * encoder counts and degrees.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @author Myles Pasetsky | StuyPulse 694
 */
public interface Conversion<From, To> {

    /**
     * Create a {@link Conversion} object.
     *
     * @param <In> Input
     * @param <Out> Output
     * @param to Function to convert input to output.
     * @param from Function to produce output from input.
     * @return A {@link Conversion} object representing an ability to convert between input and
     *     output.
     */
    static <In, Out> Conversion<In, Out> create(Function<In, Out> to, Function<Out, In> from) {
        return new Conversion<>() {
            public Out to(In value) {
                return to.apply(value);
            }

            public In from(Out value) {
                return from.apply(value);
            }
        };
    }

    /**
     * Invert this {@link Conversion}.
     *
     * @return This conversion represented as {@code Conversion<To, From>}.
     */
    default Conversion<To, From> invert() {
        return create(this::from, this::to);
    }

    /**
     * Converts input to output.
     *
     * @param from The input.
     * @return The output.
     */
    To to(From from);

    /**
     * Produces output from input.
     *
     * @param to The output.
     * @return The input.
     */
    From from(To to);
}
