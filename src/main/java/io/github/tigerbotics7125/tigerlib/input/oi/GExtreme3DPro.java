/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.oi;

import io.github.tigerbotics7125.tigerlib.input.JoystickAxisTrigger;
import io.github.tigerbotics7125.tigerlib.input.JoystickAxisTrigger.ThresholdType;
import io.github.tigerbotics7125.tigerlib.input.JoystickTrigger;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.GenericHID;

import java.util.EnumMap;

/**
 * An input wrapper for the logitech GExtreme3DPro joystick.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class GExtreme3DPro extends GenericHID {

    /** Represents an analog axis on the joystick */
    public enum Axis {
        kX(0),
        kY(1),
        kZ(2),
        kThrottle(3);

        public final int value;

        Axis(int value) {
            this.value = value;
        }
    }

    /** Represents a digital button on the joystick */
    public enum Button {
        kTrigger(1),
        kThumb(2),
        k3(3),
        k4(4),
        k5(5),
        k6(6),
        k7(7),
        k8(8),
        k9(9),
        k10(10),
        k11(11),
        k12(12);

        public final int value;

        Button(int value) {
            this.value = value;
        }
    }

    public final POV pov;

    private final EnumMap<Axis, JoystickAxisTrigger> mAxes = new EnumMap<>(Axis.class);
    private final EnumMap<Button, JoystickTrigger> mButtons = new EnumMap<>(Button.class);

    /** @param port The port index on Driver Station that the joystick is plugged into. */
    public GExtreme3DPro(int port) {
        super(port);
        pov = new POV(this);

        // Tell WPI we are using a Joystick.
        HAL.report(tResourceType.kResourceType_Joystick, port + 1);
    }

    /**
     * Builds a {@link JoystickTrigger} for this controller from the provide {@link Button}.
     *
     * @param button The Button to build for.
     * @return The JoystickTrigger.
     */
    private JoystickTrigger build(Button button) {
        return new JoystickTrigger(this, button.value);
    }

    /**
     * Builds a {@link JoystickAxisTrigger} for this controller from the provided {@link Axis}.
     *
     * @param axis The axis to build for.
     * @return The JoystickAxisTrigger.
     */
    private JoystickAxisTrigger build(
            Axis axis, double threshold, ThresholdType thresholdType, boolean inverted) {
        return new JoystickAxisTrigger(this, axis.value, threshold, thresholdType, inverted);
    }

    /** @return The trigger's {@link JoystickTrigger}. */
    public JoystickTrigger trigger() {
        return mButtons.computeIfAbsent(Button.kTrigger, this::build);
    }

    /** @return The thumb's {@link JoystickTrigger}. */
    public JoystickTrigger thumb() {
        return mButtons.computeIfAbsent(Button.kThumb, this::build);
    }

    /** @return The 3 button's {@link JoystickTrigger}. */
    public JoystickTrigger three() {
        return mButtons.computeIfAbsent(Button.k3, this::build);
    }

    /** @return The 4 button's {@link JoystickTrigger}. */
    public JoystickTrigger four() {
        return mButtons.computeIfAbsent(Button.k4, this::build);
    }

    /** @return The 5 button's {@link JoystickTrigger}. */
    public JoystickTrigger five() {
        return mButtons.computeIfAbsent(Button.k5, this::build);
    }

    /** @return The 6 button's {@link JoystickTrigger}. */
    public JoystickTrigger six() {
        return mButtons.computeIfAbsent(Button.k6, this::build);
    }

    /** @return The 7 button's {@link JoystickTrigger}. */
    public JoystickTrigger seven() {
        return mButtons.computeIfAbsent(Button.k7, this::build);
    }

    /** @return The 8 button's {@link JoystickTrigger}. */
    public JoystickTrigger eight() {
        return mButtons.computeIfAbsent(Button.k8, this::build);
    }

    /** @return The 9 button's {@link JoystickTrigger}. */
    public JoystickTrigger nine() {
        return mButtons.computeIfAbsent(Button.k9, this::build);
    }

    /** @return The 10 button's {@link JoystickTrigger}. */
    public JoystickTrigger ten() {
        return mButtons.computeIfAbsent(Button.k10, this::build);
    }

    /** @return The 11 button's {@link JoystickTrigger}. */
    public JoystickTrigger eleven() {
        return mButtons.computeIfAbsent(Button.k11, this::build);
    }

    /** @return The 12 button's {@link JoystickTrigger}. */
    public JoystickTrigger twelve() {
        return mButtons.computeIfAbsent(Button.k12, this::build);
    }

    /** @return The x axis' {@link JoystickAxisTrigger}. */
    public JoystickAxisTrigger x() {
        return mAxes.computeIfAbsent(
                Axis.kX, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }

    /** @return The y axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger y() {
        return mAxes.computeIfAbsent(
                Axis.kY, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }

    /** @return The z axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger z() {
        return mAxes.computeIfAbsent(
                Axis.kZ, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }

    /** @return The throttle axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger throttle() {
        return mAxes.computeIfAbsent(
                Axis.kThrottle, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }
}
