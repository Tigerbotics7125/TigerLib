/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import io.github.tigerbotics7125.tigerlib.util.JoystickUtil;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * A {@link Trigger} wrapper for axes.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @author Spectrum 3847
 */
public class JoystickAxisTrigger extends Trigger {
    private final GenericHID mJoystick;
    private final int mAxis;
    private final boolean mInverted;

    public enum ThresholdType {
        /**
         * Considers the input active if;
         *
         * <p>The input equals the threshold.
         */
        Exact,
        /**
         * Considers the input active if:
         *
         * <p>The input is less than the threshold.
         */
        LessThan,
        /**
         * Considers the input active if:
         *
         * <p>The input is greater than the threshold.
         */
        GreaterThan,
        /**
         * Considers the input active if:
         *
         * <p>The aboslute value of the input is greater than the threshold.
         */
        Deadband;
    }

    /**
     * Note: Invert param only inverts the returned value from the axis; if the axis would naturally
     * return .46, the interperated value is -.46.
     *
     * @param joystick The HID device to read the axis from.
     * @param axis The axis index to read from.
     * @param threshold The threshold to determine if the axis is pressed.
     * @param thresholdType How to interpret the threshold.
     * @param invert Whether the axis is inverted.
     */
    public JoystickAxisTrigger(
            GenericHID joystick,
            int axis,
            double threshold,
            ThresholdType thresholdType,
            boolean invert) {

        super(
                () -> {
                    double input = joystick.getRawAxis(axis) * (invert ? -1 : 1);
                    switch (thresholdType) {
                        case Exact:
                            return input == threshold;
                        case LessThan:
                            return input < threshold;
                        case GreaterThan:
                            return input > threshold;
                        case Deadband:
                            return Math.abs(input) > threshold;
                        default:
                            return false;
                    }
                });

        mJoystick = joystick;
        mAxis = axis;
        mInverted = invert;
    }

    /** @return The axis value without inversion. */
    public double getRawValue() {
        return mJoystick.getRawAxis(mAxis);
    }

    /**
     * @return The axis value with inversion.
     *     <p>Note: If inversion was set to false, this method returns {@link #getRawValue()}.
     */
    public double getValue() {
        return getRawValue() * (mInverted ? -1 : 1);
    }

    /**
     * Unless overriden, the default implementation is to:
     *
     * <p>{@link JoystickUtil#deadband(double, double)} the value by .075.
     *
     * <p>{@link JoystickUtil#clamp(double, double, double)} the value [-1, 1].
     *
     * @return A cleansed joystick input.
     */
    public double getCleanValue() {
        double value = getValue();
        value = JoystickUtil.deadband(value, .075);
        value = JoystickUtil.clamp(value, -1, 1);
        return value;
    }

    /**
     * Create a new {@link JoystickAxisTrigger} representing the same joystick axis, but with a
     * different threshold condition.
     *
     * @param thresholdType The new {@link ThresholdType}.
     * @param threshold The new threshold.
     * @param invert Whether to invert the new {@link JoystickAxisTrigger} from this one.
     * @return A {@link JoystickAxisTrigger} with a new threshold.
     */
    public JoystickAxisTrigger withThreshold(
            ThresholdType thresholdType, double threshold, boolean invert) {
        return new JoystickAxisTrigger(
                mJoystick, mAxis, threshold, thresholdType, invert ^ mInverted);
    }
}
