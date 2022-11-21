/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import static io.github.tigerbotics7125.tigerlib.input.Trigger.ActivationCondition.ON_FALLING;
import static io.github.tigerbotics7125.tigerlib.input.Trigger.ActivationCondition.ON_RISING;
import static io.github.tigerbotics7125.tigerlib.input.Trigger.ActivationCondition.ON_TRANSITION;
import static io.github.tigerbotics7125.tigerlib.input.Trigger.ActivationCondition.WHILE_HIGH;
import static io.github.tigerbotics7125.tigerlib.input.Trigger.ActivationCondition.WHILE_LOW;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.tigerbotics7125.tigerlib.TigerLib;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;

import java.util.function.BooleanSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TriggerTest {
    private boolean b;
    private final BooleanSupplier bool = () -> b;

    @BeforeEach
    public void setup() {
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().enable();
        CommandScheduler.getInstance().clearButtons();
        setDSEnable(true);
    }

    public void setDSEnable(boolean enabled) {
        DriverStationSim.setDsAttached(true);

        DriverStationSim.setEnabled(true);
        DriverStationSim.notifyNewData();
        while (DriverStation.isEnabled() != enabled) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    @Test
    public void periodicTest() {
        b = false;
        Trigger t = new Trigger(bool);

        assertFalse(t.mLastState);
        assertFalse(t.getAsBoolean());

        b = true;

        assertFalse(t.mLastState);
        assertTrue(t.getAsBoolean());

        Trigger.periodic();

        assertTrue(t.mLastState);
        assertTrue(t.getAsBoolean());

        b = false;

        assertTrue(t.mLastState);
        assertFalse(t.getAsBoolean());

        Trigger.periodic();

        assertFalse(t.mLastState);
        assertFalse(t.getAsBoolean());
    }

    @Test
    public void activationTest() {
        b = false;
        Trigger low = new Trigger(bool, WHILE_LOW);
        Trigger high = new Trigger(bool, WHILE_HIGH);
        Trigger fall = new Trigger(bool, ON_FALLING);
        Trigger rise = new Trigger(bool, ON_RISING);
        Trigger tran = new Trigger(bool, ON_TRANSITION);

        assertTrue(low.get());
        assertFalse(high.get());
        assertFalse(fall.get());
        assertFalse(rise.get());
        assertFalse(tran.get());

        Trigger.periodic();
        b = true;

        assertFalse(low.get());
        assertTrue(high.get());
        assertFalse(fall.get());
        assertTrue(rise.get());
        assertTrue(tran.get());

        Trigger.periodic();

        assertFalse(low.get());
        assertTrue(high.get());
        assertFalse(fall.get());
        assertFalse(rise.get());
        assertFalse(tran.get());

        Trigger.periodic();
        b = false;

        assertTrue(low.get());
        assertFalse(high.get());
        assertTrue(fall.get());
        assertFalse(rise.get());
        assertTrue(tran.get());
    }

    @Test
    public void compositionTest() {
        Trigger t = new Trigger(() -> false);
        Trigger t2 = new Trigger(() -> true);

        assertFalse(t.and(t2).get());
        assertTrue(t.nand(t2).get());
        assertTrue(t.or(t2).get());
        assertTrue(t.xor(t2).get());
        assertFalse(t.nor(t2).get());
        assertFalse(t.xnor(t2).get());
        assertTrue(t.not().get());
        assertFalse(t2.not().get());
    }

    /*
     * Should probably write a test to combine changing values and composition
     * but I'm lazy and I don't even know what to test, I guess just one of every combination; but thats alot.
     */

    @Test
    public void triggerGroupTest() {
        b = true;
        Trigger t = new Trigger(bool);
        Command c = new RunCommand(() -> {});

        t.trigger(c);
        TigerLib.periodic();
        assertTrue(c.isScheduled());

        Command c2 = new RunCommand(() -> {}).perpetually();
        t.trigger(c2, "Group2");

        assertFalse(c2.isScheduled());

        Trigger.enableGroup("Group2");
        TigerLib.periodic();

        assertTrue(c2.isScheduled());
    }
}
