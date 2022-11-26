/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.trigger;

import io.github.tigerbotics7125.tigerlib.input.joystick.JoystickAxis;
import io.github.tigerbotics7125.tigerlib.util.JoystickUtil;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.GenericHID;

import java.util.function.Function;

/**
 * A {@link Trigger} wrapper for axes.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @author Spectrum 3847
 */
public class JoystickAxisTrigger extends Trigger {

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

    private final JoystickAxis mJoystickAxis;
    private final double mThreshold;
    private final ThresholdType mThresholdType;

    /**
     * Create a new {@link JoystickAxisTrigger} Object.
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
        this(new JoystickAxis(joystick, axis, invert), threshold, thresholdType);
    }

    private JoystickAxisTrigger(
            JoystickAxis joystickAxis, double threshold, ThresholdType thresholdType) {
        super(
                () -> {
                    double val = joystickAxis.get();
                    return switch (thresholdType) {
                        case Exact -> val == threshold;
                        case LessThan -> val < threshold;
                        case GreaterThan -> val > threshold;
                        case Deadband -> Math.abs(val) > threshold;
                        default -> false;
                    };
                    // Java 11
                    /*
                    switch (thresholdType) {
                        case Exact:
                        return val == threshold;
                        case LessThan:
                        return val < threshold;
                        case GreaterThan:
                        return val > threshold;
                        case Deadband:
                        return Math.abs(val) > threshold;
                        default:
                        return false;
                    }
                    */
                });
        mJoystickAxis = joystickAxis;
        mThreshold = threshold;
        mThresholdType = thresholdType;
    }

    /** @return The axis value without inversion. */
    public double getRaw() {
        return mJoystickAxis.get();
    }

    /**
     * Unless overriden, the default implementation is to:
     *
     * <p>{@link JoystickUtil#deadband(double, double)} the value by .075.
     *
     * <p>{@link JoystickUtil#ramp(double, double)} the value by 3.
     *
     * <p>{@link JoystickUtil#clamp(double, double, double)} the value [-1, 1].
     *
     * @return A cleansed joystick input.
     */
    public double getVal() {
        double value = getRaw();
        value = JoystickUtil.deadband(value, .075);
        value = JoystickUtil.ramp(value, 3);
        value = JoystickUtil.clamp(value, -1, 1);
        return value;
    }

    public void clean(Function<Double, Double> cleaningFunction) {
        mJoystickAxis.setCleaner(cleaningFunction);
    }

    /**
     * Create a new {@link JoystickAxisTrigger} representing the same joystick axis, but with a
     * different threshold condition.
     *
     * @param thresholdType The new {@link ThresholdType}.
     * @param threshold The new threshold.
     * @return A {@link JoystickAxisTrigger} with a new threshold.
     */
    public JoystickAxisTrigger withThreshold(ThresholdType thresholdType, double threshold) {
        return new JoystickAxisTrigger(mJoystickAxis, threshold, thresholdType);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        mJoystickAxis.initSendable(builder);
        builder.addDoubleProperty("Threshold", () -> mThreshold, null);
        builder.addStringProperty("ThresholdType", () -> mThresholdType.name(), null);
    }
}
