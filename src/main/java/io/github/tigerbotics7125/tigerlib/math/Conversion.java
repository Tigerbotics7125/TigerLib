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
 * <p>For instance you can have a {@code Conversion<Double, Double>} which could convert between
 * motor RPM to wheel MPS
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @author Myles Pasetsky | StuyPulse 694
 */
public interface Conversion<Input, Output> {

    /**
     * Creates a {@code Conversion<I, O>} object.
     *
     * @param <I> Input class type.
     * @param <O> Output class type.
     * @param inputToOutput {@code Function<I, O>} which converts input measurement to output
     *     measurement.
     * @param outputToInput {@code Function<O, I>} which converts output measurement to input
     *     measurement.
     * @return A {@link Conversion} object representing the two functions.
     */
    public static <I, O> Conversion<I, O> create(
            Function<I, O> inputToOutput, Function<O, I> outputToInput) {
        return new Conversion<>() {
            @Override
            public O fromInput(I input) {
                return inputToOutput.apply(input);
            }

            @Override
            public I fromOutput(O output) {
                return outputToInput.apply(output);
            }
        };
    }

    /**
     * @return A new {@link Conversion} object which flips the input and output of this object such
     *     that {@code Conversion<I, O>.invert()} would return {@code Conversion<O, I>}.
     */
    default Conversion<Output, Input> invert() {
        return create(this::fromOutput, this::fromInput);
    }

    /**
     * @param input Measurement representing type {@code Input} to convert to type {@code Output}.
     * @return Type {@code Output} converted from {@code Input}.
     */
    public Output fromInput(Input input);

    /**
     * @param output Measurement representing type {@code Output} to convert to type {@code Input}.
     * @return Type {@code Input} converted from {@code Output}.
     */
    public Input fromOutput(Output output);
}
