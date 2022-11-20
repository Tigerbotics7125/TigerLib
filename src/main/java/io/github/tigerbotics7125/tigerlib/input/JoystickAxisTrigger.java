/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import io.github.tigerbotics7125.tigerlib.util.JoystickUtil;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

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
    private final double mThreshold;
    private final ThresholdType mThresholdType;

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
        mJoystick = joystick;
        mAxis = axis;
        mThreshold = threshold;
        mThresholdType = thresholdType;
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

    public double getCleanValue() {
        double value = getValue();
        value = JoystickUtil.deadband(value, .075);
        value = JoystickUtil.clamp(value, -1, 1);
        return value;
    }

    /** @return Whether the axis is determined as active or not. */
    @Override
    public boolean getAsBoolean() {
        switch (this.mThresholdType) {
            case Exact:
                return getValue() == mThreshold;
            case LessThan:
                return getValue() < mThreshold;
            case GreaterThan:
                return getValue() > mThreshold;
            case Deadband:
                return Math.abs(getValue()) > mThreshold;
            default:
                return false;
        }
    }
}
