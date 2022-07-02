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
 * An input wrapper for the Xbox controller.
 *
 * <p>Saves RAM on the roborio by only instantiating a button when its used. CPU time is cheaper
 * than RAM in this scenario.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class XboxController extends GenericHID {

    /* Represents an analog axis on the joystick. */
    public enum XboxAxis {
        kLeftX(0),
        kLeftY(1),
        kLT(2),
        kRT(3),
        kRightX(4),
        kRightY(5);

        public final int value;

        XboxAxis(int value) {
            this.value = value;
        }
    }

    /* Represents a digital button on the joystick. */
    public enum XboxButton {
        kA(1),
        kB(2),
        kX(3),
        kY(4),
        kLB(5),
        kRB(6),
        kBack(7),
        kStart(8),
        kLStick(9),
        kRStick(10);

        public final int value;

        XboxButton(int value) {
            this.value = value;
        }
    }

    public final POV pov;

    private final EnumMap<XboxButton, JoystickTrigger> mButtons = new EnumMap<>(XboxButton.class);
    private final EnumMap<XboxAxis, JoystickAxisTrigger> mAxes = new EnumMap<>(XboxAxis.class);

    /**
     * Constructs a new instance of the joystick
     *
     * @param port The port on the Driver Station that the joystick is plugged into
     */
    public XboxController(int port) {
        super(port);
        pov = new POV(this);

        // Tell WPI we are using an Xbox controller.
        HAL.report(tResourceType.kResourceType_XboxController, port + 1);
    }

    /**
     * Builds a {@link JoystickTrigger} for this controller from the provided {@link XboxButton}.
     *
     * @param button The XboxButton to build for.
     * @return The JoystickTrigger.
     */
    private JoystickTrigger build(XboxButton button) {
        return new JoystickTrigger(this, button.value);
    }

    /**
     * Builds a {@link JoystickAxisTrigger} for this controller from the provided {@link XboxAxis}.
     *
     * @param axis The Axis to build for.
     * @return The JoystickAxisButton.
     */
    private JoystickAxisTrigger build(
            XboxAxis axis, double threshold, ThresholdType thresholdType, boolean inverted) {
        return new JoystickAxisTrigger(this, axis.value, threshold, thresholdType, inverted);
    }

    /** @return The a {@link JoystickTrigger}. */
    public JoystickTrigger a() {
        return mButtons.computeIfAbsent(XboxButton.kA, this::build);
    }

    /** @return The b {@link JoystickTrigger}. */
    public JoystickTrigger b() {
        return mButtons.computeIfAbsent(XboxButton.kB, this::build);
    }

    /** @return The x {@link JoystickTrigger}. */
    public JoystickTrigger x() {
        return mButtons.computeIfAbsent(XboxButton.kX, this::build);
    }

    /** @return The y {@link JoystickTrigger}. */
    public JoystickTrigger y() {
        return mButtons.computeIfAbsent(XboxButton.kY, this::build);
    }

    /** @retunr The left bumper's {@link JoystickTrigger}. */
    public JoystickTrigger lb() {
        return mButtons.computeIfAbsent(XboxButton.kLB, this::build);
    }

    /** @return The right bumper's {@link JoystickTrigger}. */
    public JoystickTrigger rb() {
        return mButtons.computeIfAbsent(XboxButton.kRB, this::build);
    }

    /** @return The back {@link JoystickTrigger}. */
    public JoystickTrigger back() {
        return mButtons.computeIfAbsent(XboxButton.kBack, this::build);
    }

    /** @return The start {@link JoystickTrigger}. */
    public JoystickTrigger start() {
        return mButtons.computeIfAbsent(XboxButton.kStart, this::build);
    }

    /** @return The left stick's {@link JoystickTrigger} */
    public JoystickTrigger lStick() {
        return mButtons.computeIfAbsent(XboxButton.kLStick, this::build);
    }

    /** @return The right stick's {@link JoystickTrigger}. */
    public JoystickTrigger rStick() {
        return mButtons.computeIfAbsent(XboxButton.kRStick, this::build);
    }

    /**
     * East is considered positive.
     *
     * <p>Note: This axis is inverted.
     *
     * @return The left stick's x {@link JoystickAxisTrigger}
     */
    public JoystickAxisTrigger leftX() {
        return mAxes.computeIfAbsent(
                XboxAxis.kLeftX, (axis) -> build(axis, .98, ThresholdType.kDeadband, true));
    }

    /**
     * North is considered positive.
     *
     * @return The left stick's y {@link JoystickAxisTrigger}.
     */
    public JoystickAxisTrigger leftY() {
        return mAxes.computeIfAbsent(
                XboxAxis.kLeftY, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }

    /** Get the left trigger's {@link JoystickAxisTrigger}. */
    public JoystickAxisTrigger lt() {
        return mAxes.computeIfAbsent(
                XboxAxis.kLT, (axis) -> build(axis, 0.02, ThresholdType.kGreaterThan, false));
    }

    /** @return The right trigger's {@link JoystickAxisTrigger}. */
    public JoystickAxisTrigger rt() {
        return mAxes.computeIfAbsent(
                XboxAxis.kRT, (axis) -> build(axis, 0.02, ThresholdType.kGreaterThan, false));
    }

    /**
     * East is considered positive.
     *
     * <p>Note: This axis is inverted.
     *
     * @return The right stick's x {@link JoystickAxisTrigger}.
     */
    public JoystickAxisTrigger rightX() {
        return mAxes.computeIfAbsent(
                XboxAxis.kRightX, (axis) -> build(axis, .98, ThresholdType.kDeadband, true));
    }

    /**
     * North is considered positive.
     *
     * @return The right stick's y {@link JoystickAxisTrigger}.
     */
    public JoystickAxisTrigger rightY() {
        return mAxes.computeIfAbsent(
                XboxAxis.kRightY, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
    }
}
