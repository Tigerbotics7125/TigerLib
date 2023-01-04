/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.controller;

import io.github.tigerbotics7125.tigerlib.input.trigger.JoystickAxisTrigger;
import io.github.tigerbotics7125.tigerlib.input.trigger.JoystickAxisTrigger.ThresholdType;
import io.github.tigerbotics7125.tigerlib.input.trigger.JoystickTrigger;

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
	X(0), Y(1), Z(2), Throttle(3);

	public final int value;

	Axis(int value) {
	    this.value = value;
	}
    }

    /** Represents a digital button on the joystick */
    public enum Button {
	TRIGGER(1), THUMB(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), ELEVEN(11),
	TWELVE(12);

	public final int value;

	Button(int value) {
	    this.value = value;
	}
    }

    public final POV pov;

    private final EnumMap<Axis, JoystickAxisTrigger> mAxes = new EnumMap<>(Axis.class);
    private final EnumMap<Button, JoystickTrigger> mButtons = new EnumMap<>(Button.class);

    /**
     * @param port The port index on Driver Station that the joystick is plugged
     *             into.
     */
    public GExtreme3DPro(int port) {
	super(port);
	pov = new POV(this);

	// Tell WPI we are using a Joystick.
	HAL.report(tResourceType.kResourceType_Joystick, port + 1);
    }

    /**
     * Builds a {@link JoystickTrigger} for this controller from the provide
     * {@link Button}.
     *
     * @param button The Button to build for.
     * @return The JoystickTrigger.
     */
    private JoystickTrigger build(Button button) {
	return new JoystickTrigger(this, button.value);
    }

    /**
     * Builds a {@link JoystickAxisTrigger} for this controller from the
     * provided {@link Axis}.
     *
     * @param axis The axis to build for.
     * @return The JoystickAxisTrigger.
     */
    private JoystickAxisTrigger build(Axis axis, double threshold, ThresholdType thresholdType, boolean inverted) {
	return new JoystickAxisTrigger(this, axis.value, threshold, thresholdType, inverted);
    }

    /** @return The trigger's {@link JoystickTrigger}. */
    public JoystickTrigger trigger() {
	return mButtons.computeIfAbsent(Button.TRIGGER, this::build);
    }

    /** @return The thumb's {@link JoystickTrigger}. */
    public JoystickTrigger thumb() {
	return mButtons.computeIfAbsent(Button.THUMB, this::build);
    }

    /** @return The 3 button's {@link JoystickTrigger}. */
    public JoystickTrigger three() {
	return mButtons.computeIfAbsent(Button.THREE, this::build);
    }

    /** @return The 4 button's {@link JoystickTrigger}. */
    public JoystickTrigger four() {
	return mButtons.computeIfAbsent(Button.FOUR, this::build);
    }

    /** @return The 5 button's {@link JoystickTrigger}. */
    public JoystickTrigger five() {
	return mButtons.computeIfAbsent(Button.FIVE, this::build);
    }

    /** @return The 6 button's {@link JoystickTrigger}. */
    public JoystickTrigger six() {
	return mButtons.computeIfAbsent(Button.SIX, this::build);
    }

    /** @return The 7 button's {@link JoystickTrigger}. */
    public JoystickTrigger seven() {
	return mButtons.computeIfAbsent(Button.SEVEN, this::build);
    }

    /** @return The 8 button's {@link JoystickTrigger}. */
    public JoystickTrigger eight() {
	return mButtons.computeIfAbsent(Button.EIGHT, this::build);
    }

    /** @return The 9 button's {@link JoystickTrigger}. */
    public JoystickTrigger nine() {
	return mButtons.computeIfAbsent(Button.NINE, this::build);
    }

    /** @return The 10 button's {@link JoystickTrigger}. */
    public JoystickTrigger ten() {
	return mButtons.computeIfAbsent(Button.TEN, this::build);
    }

    /** @return The 11 button's {@link JoystickTrigger}. */
    public JoystickTrigger eleven() {
	return mButtons.computeIfAbsent(Button.ELEVEN, this::build);
    }

    /** @return The 12 button's {@link JoystickTrigger}. */
    public JoystickTrigger twelve() {
	return mButtons.computeIfAbsent(Button.TWELVE, this::build);
    }

    /** @return The x axis' {@link JoystickAxisTrigger}. */
    public JoystickAxisTrigger x() {
	return mAxes.computeIfAbsent(Axis.X, (axis) -> build(axis, .98, ThresholdType.Deadband, false));
    }

    /** @return The y axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger y() {
	return mAxes.computeIfAbsent(Axis.Y, (axis) -> build(axis, .98, ThresholdType.Deadband, false));
    }

    /** @return The z axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger z() {
	return mAxes.computeIfAbsent(Axis.Z, (axis) -> build(axis, .98, ThresholdType.Deadband, false));
    }

    /** @return The throttle axis' {@link JoystickAxisTrigger} */
    public JoystickAxisTrigger throttle() {
	return mAxes.computeIfAbsent(Axis.Throttle, (axis) -> build(axis, .98, ThresholdType.Deadband, false));
    }
}
