/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MathUtilTest {

    @Test
    public void scaleInputTest() {
	// oldMax < oldMin
	assertThrows(IllegalArgumentException.class, () -> MathUtil.minMaxScaling(0, 1, -1, 0, 0));
	// newMax < newMin
	assertThrows(IllegalArgumentException.class, () -> MathUtil.minMaxScaling(0, 0, 0, 1, -1));
	// x out of old bounds
	assertThrows(IllegalArgumentException.class, () -> MathUtil.minMaxScaling(-1, 0, 0, 0, 0));
	// x = 1, [0, 1] -> various new ranges
	assertEquals(0, MathUtil.minMaxScaling(1, 0, 1, -1, 0));
	assertEquals(.5, MathUtil.minMaxScaling(1, 0, 1, 0, .5));
	assertEquals(1, MathUtil.minMaxScaling(1, 0, 1, -1, 1));
	// x = 1 various old ranges -> [-1, 1]
	assertEquals(.5, MathUtil.minMaxScaling(1, -2, 2, -1, 1));
	assertEquals(0, MathUtil.minMaxScaling(1, 0, 2, -1, 1));
	assertEquals(1, MathUtil.minMaxScaling(1, -2, 1, -1, 1));
	// x = 1, various old ranges -> various new ranges
	assertEquals(0, MathUtil.minMaxScaling(1, -2, 2, 0, 0));
	assertEquals(.5, MathUtil.minMaxScaling(1, -1, 1, -2, .5));
	assertEquals(.125, MathUtil.minMaxScaling(1, 0, 2, -1.75, 2));
    }
}
