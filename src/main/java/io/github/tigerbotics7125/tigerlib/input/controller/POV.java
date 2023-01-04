/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.controller;

import io.github.tigerbotics7125.tigerlib.input.trigger.JoystickPOVTrigger;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.EnumMap;

public class POV {

    private enum POVAngle {
	CENTER(-1), UP(0), UP_RIGHT(45), RIGHT(90), DOWN_RIGHT(135), DOWN(180), DOWN_LEFT(225), LEFT(270), UP_LEFT(315);

	public final int value;

	POVAngle(int value) {
	    this.value = value;
	}
    }

    private final EnumMap<POVAngle, JoystickPOVTrigger> mPovs = new EnumMap<>(POVAngle.class);
    private final GenericHID mJoystick;
    private final int mPovNumber;

    /** @param joystick The HID device to read POV 0 from. */
    public POV(GenericHID joystick) {
	this(joystick, 0);
    }

    /**
     * @param joystick  the HID controller to read the POV from.
     * @param povNumber The controller POV index to use.
     */
    public POV(GenericHID joystick, int povNumber) {
	mJoystick = joystick;
	mPovNumber = povNumber;
    }

    /**
     * Builds a {@link JoystickPOVTrigger} for the given {@link POVAngle}.
     *
     * @param angle The POVAngle to build for.
     * @return Built JoystickPOVTrigger.
     */
    private JoystickPOVTrigger build(POVAngle angle) {
	return new JoystickPOVTrigger(mJoystick, mPovNumber, angle.value);
    }

    /** @return The centered (not pressed) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger center() {
	return mPovs.computeIfAbsent(POVAngle.CENTER, this::build);
    }

    /** @return The upper (0 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger up() {
	return mPovs.computeIfAbsent(POVAngle.UP, this::build);
    }

    /** @return The upper right (45 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger upRight() {
	return mPovs.computeIfAbsent(POVAngle.UP_RIGHT, this::build);
    }

    /** @return The right (90 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger right() {
	return mPovs.computeIfAbsent(POVAngle.RIGHT, this::build);
    }

    /** @return The lower right (135 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger downRight() {
	return mPovs.computeIfAbsent(POVAngle.DOWN_RIGHT, this::build);
    }

    /** @return The lower (180 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger down() {
	return mPovs.computeIfAbsent(POVAngle.DOWN, this::build);
    }

    /** @return The lower left (225 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger downLeft() {
	return mPovs.computeIfAbsent(POVAngle.DOWN_LEFT, this::build);
    }

    /** @return The left (270 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger left() {
	return mPovs.computeIfAbsent(POVAngle.LEFT, this::build);
    }

    /** @return The upper left (315 degrees) {@link JoystickPOVTrigger}. */
    public JoystickPOVTrigger upLeft() {
	return mPovs.computeIfAbsent(POVAngle.UP_LEFT, this::build);
    }
}
