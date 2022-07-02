/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A {@link Trigger} wrapper for POV.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class JoystickPOVTrigger extends Trigger {

    private final GenericHID mJoystick;
    private final int mPov;
    private final int mAngle;

    /**
     * @param joystick The HID device to read the POV from.
     * @param pov The pov index to read from.
     * @param angle The angle to read from.
     */
    public JoystickPOVTrigger(GenericHID joystick, int pov, int angle) {
        mJoystick = joystick;
        mPov = pov;
        mAngle = angle;
    }

    /** @return Whether the value of the POV matches the target angle. */
    @Override
    public boolean getAsBoolean() {
        return mJoystick.getPOV(mPov) == mAngle;
    }
}
