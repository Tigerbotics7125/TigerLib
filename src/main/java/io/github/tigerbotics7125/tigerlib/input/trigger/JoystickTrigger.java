/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.trigger;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * A {@link Trigger} wrapper for Joystick buttons.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class JoystickTrigger extends Trigger {

    /**
     * @param joystick The HID device to read the button from.
     * @param button   The button index to read from.
     */
    public JoystickTrigger(GenericHID joystick, int button) {
	super(() -> joystick.getRawButton(button));
    }
}
