/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void rampTest() {
        // x = 0, various sensitivities
        assertEquals(Double.NaN, Util.ramp(0, -5));
        assertEquals(Double.NaN, Util.ramp(0, -1));
        assertEquals(0, Util.ramp(0, 0));
        assertEquals(0, Util.ramp(0, 1));
        assertEquals(0, Util.ramp(0, 5));
        // x = 1, various sensitivities
        assertEquals(1, Util.ramp(1, -5));
        assertEquals(1, Util.ramp(1, -1));
        assertEquals(1, Util.ramp(1, 0));
        assertEquals(1, Util.ramp(1, 1));
        assertEquals(1, Util.ramp(1, 5));
        // x = 5, various sensitivities
        assertEquals(0.00032, Util.ramp(5, -5));
        assertEquals(0.2, Util.ramp(5, -1));
        assertEquals(1, Util.ramp(5, 0));
        assertEquals(5, Util.ramp(5, 1));
        assertEquals(3125, Util.ramp(5, 5));
    }

    @Test
    public void deadbandTest() {
        // negative deadband
        assertThrows(IllegalArgumentException.class, () -> Util.deadband(1, -5));
        // x = 0, various deadbands
        assertEquals(0, Util.deadband(0, 0));
        assertEquals(0, Util.deadband(0, .1));
        assertEquals(0, Util.deadband(0, .5));
        assertEquals(0, Util.deadband(0, 1));
        // x = .3, various deadbands
        assertEquals(.3, Util.deadband(.3, 0));
        assertEquals(.3, Util.deadband(.3, .1));
        assertEquals(0, Util.deadband(.3, .5));
        assertEquals(0, Util.deadband(.3, 1));
        // x = .5, various deadbands
        assertEquals(.5, Util.deadband(.5, 0));
        assertEquals(.5, Util.deadband(.5, .1));
        assertEquals(0, Util.deadband(.5, .5));
        assertEquals(0, Util.deadband(.5, 1));
    }

    @Test
    public void clampTest() {
        // max < min
        assertThrows(IllegalArgumentException.class, () -> Util.clamp(1, 5, 3));
        // x = .3, various bounds
        assertEquals(.3, Util.clamp(.3, 0, 1));
        assertEquals(.3, Util.clamp(.3, -.5, .5));
        assertEquals(-.3, Util.clamp(.3, -.5, -.3));
        assertEquals(0, Util.clamp(.3, -.5, 0));
    }

    @Test
    public void smoothTest() {
        // negative deadband
        assertThrows(IllegalArgumentException.class, () -> Util.smooth(1, -5, 1));
        // various x, .2 deadband, 2.5 sensitivity
        assertEquals(-1, Util.smooth(-1, .2, 2.5)); // -1^s is itself
        assertEquals(-.57, Util.smooth(-.8, .2, 2.5), .01); // long decimal
        assertEquals(-.28, Util.smooth(-.6, .2, 2.5), .01); // long decimal
        assertEquals(0, Util.smooth(-.2, .2, 2.5)); // under deadband
        assertEquals(0, Util.smooth(0, .2, 2.5)); // under deadband
        assertEquals(0, Util.smooth(.2, .2, 2.5)); // under deadband
        assertEquals(0.28, Util.smooth(.6, .2, 2.5), .01); // long decimal
        assertEquals(0.57, Util.smooth(.8, .2, 2.5), .01); // long decimal
        assertEquals(1, Util.smooth(1, .2, 2.5)); // 1^s is itself
    }

    @Test
    public void smoothWClampTest() {
        // negative deadband
        assertThrows(IllegalArgumentException.class, () -> Util.smoothWClamp(0, -1, 0, 0, 0));
        // max < min
        assertThrows(IllegalArgumentException.class, () -> Util.smoothWClamp(0, 0, 0, 1, -1));
        // various x, .2 deadband, 2.5 sensitivity, -.4 min, .4 max
        assertEquals(-0.4, Util.smoothWClamp(-1, .2, 2.5, -.4, .4)); // -1^s is itself, clamp to -.4
        assertEquals(-0.4, Util.smoothWClamp(-.8, .2, 2.5, -.4, .4)); // long decimal, clamp to -.4
        assertEquals(-0.28, Util.smoothWClamp(-.6, .2, 2.5, -.4, .4), .01); // long decimal
        assertEquals(0, Util.smoothWClamp(-.2, .2, 2.5, -.4, .4)); // under deadband
        assertEquals(0, Util.smoothWClamp(0, .2, 2.5, -.4, .4)); // under deadband
        assertEquals(0, Util.smoothWClamp(.2, .2, 2.5, -.4, .4)); // under deadband
        assertEquals(0.28, Util.smoothWClamp(.6, .2, 2.5, -.4, .4), .01); // long decimal
        assertEquals(0.4, Util.smoothWClamp(.8, .2, 2.5, -.4, .4)); // long decimal, clamp to .4
        assertEquals(0.4, Util.smoothWClamp(1, .2, 2.5, -.4, .4)); // 1^s is itself, clamp to .4
    }

    @Test
    public void scaleInputTest() {
        // oldMax < oldMin
        assertThrows(IllegalArgumentException.class, () -> Util.scaleInput(0, 1, -1, 0, 0));
        // newMax < newMin
        assertThrows(IllegalArgumentException.class, () -> Util.scaleInput(0, 0, 0, 1, -1));
        // x out of old bounds
        assertThrows(IllegalArgumentException.class, () -> Util.scaleInput(-1, 0, 0, 0, 0));
        // x = 1, [0, 1] -> various new ranges
        assertEquals(0, Util.scaleInput(1, 0, 1, -1, 0));
        assertEquals(.5, Util.scaleInput(1, 0, 1, 0, .5));
        assertEquals(1, Util.scaleInput(1, 0, 1, -1, 1));
        // x = 1 various old ranges -> [-1, 1]
        assertEquals(.5, Util.scaleInput(1, -2, 2, -1, 1));
        assertEquals(0, Util.scaleInput(1, 0, 2, -1, 1));
        assertEquals(1, Util.scaleInput(1, -2, 1, -1, 1));
        // x = 1, various old ranges -> various new ranges
        assertEquals(0, Util.scaleInput(1, -2, 2, 0, 0));
        assertEquals(.5, Util.scaleInput(1, -1, 1, -2, .5));
        assertEquals(.125, Util.scaleInput(1, 0, 2, -1.75, 2));
    }
}
