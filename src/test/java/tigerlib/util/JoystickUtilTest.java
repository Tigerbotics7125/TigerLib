/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package tigerlib.util;

import static io.github.tigerbotics7125.tigerlib.util.JoystickUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JoystickUtilTest {

    @Test
    public void rampTest() {
        // x = 0, various sensitivities
        assertEquals(Double.NaN, ramp(0, -5));
        assertEquals(Double.NaN, ramp(0, -1));
        assertEquals(0, ramp(0, 0));
        assertEquals(0, ramp(0, 1));
        assertEquals(0, ramp(0, 5));
        // x = 1, various sensitivities
        assertEquals(1, ramp(1, -5));
        assertEquals(1, ramp(1, -1));
        assertEquals(1, ramp(1, 0));
        assertEquals(1, ramp(1, 1));
        assertEquals(1, ramp(1, 5));
        // x = 5, various sensitivities
        assertEquals(0.00032, ramp(5, -5));
        assertEquals(0.2, ramp(5, -1));
        assertEquals(1, ramp(5, 0));
        assertEquals(5, ramp(5, 1));
        assertEquals(3125, ramp(5, 5));
    }

    @Test
    public void deadbandTest() {
        // negative deadband
        assertThrows(IllegalArgumentException.class, () -> deadband(1, -5));
        // x = 0, various deadbands
        assertEquals(0, deadband(0, 0));
        assertEquals(0, deadband(0, .1));
        assertEquals(0, deadband(0, .5));
        assertEquals(0, deadband(0, 1));
        // x = .3, various deadbands
        assertEquals(.3, deadband(.3, 0));
        assertEquals(.3, deadband(.3, .1));
        assertEquals(0, deadband(.3, .5));
        assertEquals(0, deadband(.3, 1));
        // x = .5, various deadbands
        assertEquals(.5, deadband(.5, 0));
        assertEquals(.5, deadband(.5, .1));
        assertEquals(0, deadband(.5, .5));
        assertEquals(0, deadband(.5, 1));
    }

    // No deadzone test as it is just a pair of deadband.

    @Test
    public void clampTest() {
        // max < min
        assertThrows(IllegalArgumentException.class, () -> clamp(1, 5, 3));
        // x = .3, various bounds
        assertEquals(.3, clamp(.3, 0, 1));
        assertEquals(.3, clamp(.3, -.5, .5));
        assertEquals(-.3, clamp(.3, -.5, -.3));
        assertEquals(0, clamp(.3, -.5, 0));
    }
}
