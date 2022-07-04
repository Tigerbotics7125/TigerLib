/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.util;

import java.util.function.Supplier;

/**
 * A Supplier which returns cleansed input.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
@FunctionalInterface
public interface CleanSupplier<T> extends Supplier<T> {

    T get();
}
