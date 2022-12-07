/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.controller;

import io.github.tigerbotics7125.tigerlib.input.joystick.JoystickAxis;
import io.github.tigerbotics7125.tigerlib.input.trigger.JoystickTrigger;

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
        LEFT_X(0),
        LEFT_Y(1),
        LT(2),
        RT(3),
        RIGHT_X(4),
        RIGHT_Y(5);

        public final int value;

        XboxAxis(int value) {
            this.value = value;
        }
    }

    /* Represents a digital button on the joystick. */
    public enum XboxButton {
        A(1),
        B(2),
        X(3),
        Y(4),
        LB(5),
        RB(6),
        BACK(7),
        START(8),
        LEFT_STICK(9),
        RIGHT_STICK(10);

        public final int value;

        XboxButton(int value) {
            this.value = value;
        }
    }

    public final POV pov;

    private final EnumMap<XboxButton, JoystickTrigger> mButtons = new EnumMap<>(XboxButton.class);
    private final EnumMap<XboxAxis, JoystickAxis> mAxes = new EnumMap<>(XboxAxis.class);

    /** @param port The port on the Driver Station that the joystick is plugged into */
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
     * Builds a {@link JoystickAxis} for this controller from the provided {@link XboxAxis}.
     *
     * @param axis The Axis to build for.
     * @return The JoystickAxis.
     */
    private JoystickAxis build(XboxAxis axis, boolean inverted) {
        return new JoystickAxis(this, axis.value, inverted);
    }

    /** @return The a {@link JoystickTrigger}. */
    public JoystickTrigger a() {
        return mButtons.computeIfAbsent(XboxButton.A, this::build);
    }

    /** @return The b {@link JoystickTrigger}. */
    public JoystickTrigger b() {
        return mButtons.computeIfAbsent(XboxButton.B, this::build);
    }

    /** @return The x {@link JoystickTrigger}. */
    public JoystickTrigger x() {
        return mButtons.computeIfAbsent(XboxButton.X, this::build);
    }

    /** @return The y {@link JoystickTrigger}. */
    public JoystickTrigger y() {
        return mButtons.computeIfAbsent(XboxButton.Y, this::build);
    }

    /** @return The left bumper's {@link JoystickTrigger}. */
    public JoystickTrigger lb() {
        return mButtons.computeIfAbsent(XboxButton.LB, this::build);
    }

    /** @return The right bumper's {@link JoystickTrigger}. */
    public JoystickTrigger rb() {
        return mButtons.computeIfAbsent(XboxButton.RB, this::build);
    }

    /** @return The back {@link JoystickTrigger}. */
    public JoystickTrigger back() {
        return mButtons.computeIfAbsent(XboxButton.BACK, this::build);
    }

    /** @return The start {@link JoystickTrigger}. */
    public JoystickTrigger start() {
        return mButtons.computeIfAbsent(XboxButton.START, this::build);
    }

    /** @return The left stick's {@link JoystickTrigger} */
    public JoystickTrigger lStick() {
        return mButtons.computeIfAbsent(XboxButton.LEFT_STICK, this::build);
    }

    /** @return The right stick's {@link JoystickTrigger}. */
    public JoystickTrigger rStick() {
        return mButtons.computeIfAbsent(XboxButton.RIGHT_STICK, this::build);
    }

    /**
     * East is considered positive.
     *
     * @return The left stick's x {@link JoystickAxis}
     */
    public JoystickAxis leftX() {
        return mAxes.computeIfAbsent(XboxAxis.LEFT_X, (axis) -> build(axis, true));
    }

    /**
     * North is considered positive.
     *
     * @return The left stick's y {@link JoystickAxis}.
     */
    public JoystickAxis leftY() {
        return mAxes.computeIfAbsent(XboxAxis.LEFT_Y, (axis) -> build(axis, true));
    }

    /** @return The left trigger's {@link JoystickAxis}. */
    public JoystickAxis lt() {
        return mAxes.computeIfAbsent(XboxAxis.LT, (axis) -> build(axis, false));
    }

    /** @return The right trigger's {@link JoystickAxis}. */
    public JoystickAxis rt() {
        return mAxes.computeIfAbsent(XboxAxis.RT, (axis) -> build(axis, false));
    }

    /**
     * East is considered positive.
     *
     * @return The right stick's x {@link JoystickAxis}.
     */
    public JoystickAxis rightX() {
        return mAxes.computeIfAbsent(XboxAxis.RIGHT_X, (axis) -> build(axis, true));
    }

    /**
     * North is considered positive.
     *
     * @return The right stick's y {@link JoystickAxis}.
     */
    public JoystickAxis rightY() {
        return mAxes.computeIfAbsent(XboxAxis.RIGHT_Y, (axis) -> build(axis, true));
    }
}
