/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib;

import io.github.tigerbotics7125.tigerlib.input.Trigger;

import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * This class contains a collection of library classes which would on their own, be setup in {@code
 * TimedRobot.robotInit()} or run periodicly in {@code TimedRobot.robotPeriodic()}; but instead can
 * simply be run through one call in this class.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public final class TigerLib {

    /** Library initialization calls. */
    public static void init() {}

    /** Library periodic calls. */
    public static void periodic() {
        Trigger.periodic();

        // Scheduler is here to simplify user code.
        CommandScheduler.getInstance().run();
    }
}
