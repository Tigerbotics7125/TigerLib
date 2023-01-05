/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * This class is designed to abstract away the need to call the CommandScheduler
 * periodically.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class CommandRobot extends TimedRobot {

    @SuppressWarnings("resource")
    public CommandRobot() {
	super();
	this.addPeriodic(CommandScheduler.getInstance()::run, kDefaultPeriod);
    }

    @Override
    public void robotInit() {}

    @Override
    public void robotPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void testInit() {}

    @Override
    public void testPeriodic() {}

}
