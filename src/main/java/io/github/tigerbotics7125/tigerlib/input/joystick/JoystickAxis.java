/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.joystick;

import io.github.tigerbotics7125.tigerlib.util.JoystickUtil;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.GenericHID;

import java.util.function.Function;

public class JoystickAxis implements Sendable {
    private final GenericHID mJoystick;
    private final int mAxis;

    private boolean mInverted;
    private Function<Double, Double> mInputCleaner = (input) -> {
	input = JoystickUtil.deadband(input, .075);
	input = JoystickUtil.ramp(input, 3);
	input = JoystickUtil.clamp(input, -1, 1);
	return input;
    };

    /**
     * Create a new {@link JoystickAxis} Object.
     *
     * @param joystick The HID controller to read the axis from.
     * @param axis     The axis index.
     * @param inverted Whether to invert the axis from its raw reading.
     */
    public JoystickAxis(GenericHID joystick, int axis, boolean inverted) {
	mJoystick = joystick;
	mAxis = axis;
	mInverted = inverted;
    }

    /** @return Cleaning function applied to {@link JoystickAxis#getRaw()}. */
    public double get() {
	return mInputCleaner.apply(getRaw());
    }

    /** @return Raw axis input, iverted if set. */
    public double getRaw() {
	return mJoystick.getRawAxis(mAxis) * (mInverted ? -1 : 1);
    }

    /** @param inputCleanerFunction Function to clean the raw input. */
    public void setCleaner(Function<Double, Double> inputCleanerFunction) {
	mInputCleaner = inputCleanerFunction;
    }

    /** Invert the input from the current state. */
    public void invert() {
	mInverted = !mInverted;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
	builder.addDoubleProperty("raw", this::getRaw, null);
	builder.addDoubleProperty("val", this::get, null);
    }
}
