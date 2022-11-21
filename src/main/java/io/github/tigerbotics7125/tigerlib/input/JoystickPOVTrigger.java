/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * A {@link Trigger} wrapper for POV.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class JoystickPOVTrigger extends Trigger {

    /**
     * @param joystick The HID device to read the POV from.
     * @param pov The pov index to read from.
     * @param angle The angle to read from.
     */
    public JoystickPOVTrigger(GenericHID joystick, int pov, int angle) {
        super(() -> joystick.getPOV(pov) == angle);
    }
}
