/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A {@link Trigger} wrapper for Joystick buttons.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class JoystickTrigger extends Trigger {
    private final GenericHID mJoystick;
    private final int mButton;

    /**
     * @param joystick The HID device to read the button from.
     * @param button The button index to read from.
     */
    public JoystickTrigger(GenericHID joystick, int button) {
        mJoystick = joystick;
        mButton = button;
    }

    /** @return The value of the joystick's button. */
    @Override
    public boolean getAsBoolean() {
        return mJoystick.getRawButton(mButton);
    }
}
