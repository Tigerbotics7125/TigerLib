/*
 * Copyright (c) 2022-2023 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import static io.github.tigerbotics7125.tigerlib.input.trigger.Trigger.ActivationCondition.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import io.github.tigerbotics7125.tigerlib.input.trigger.Trigger;
import io.github.tigerbotics7125.tigerlib.input.trigger.TriggerGroup;
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
        CommandScheduler.getInstance().getActiveButtonLoop().clear();
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
    public void whileLowTest() {
        Trigger t = new Trigger(bool);
        Command c = new RunCommand(() -> {});
        t.trigger(WHILE_LOW, c);

        // not on when expected.
        b = true;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        // on when expected.
        b = false;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());

        // cancels when expected.
        b = true;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());
    }

    @Test
    public void whileHighTest() {
        Trigger t = new Trigger(bool);
        Command c = new RunCommand(() -> {});
        t.trigger(WHILE_HIGH, c);

        // not on when expected.
        b = false;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        // on when expected.
        b = true;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());

        // cancel when expected.
        b = false;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());
    }

    @Test
    public void onFallingTest() {
        b = false;
        Trigger t = new Trigger(bool);
        Command c = new RunCommand(() -> {}).repeatedly();
        t.trigger(ON_FALLING, c);

        // not on when expected.
        b = false;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        b = true;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        // on when expected.
        b = false;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());

        // does not cancel, as expected
        b = true;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());
    }

    @Test
    public void onRisingTest() {
        b = true;
        Trigger t = new Trigger(bool);
        Command c = new RunCommand(() -> {}).repeatedly();
        t.trigger(ON_RISING, c);

        // not on when expected.
        b = true;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        b = false;
        CommandScheduler.getInstance().run();
        assertFalse(c.isScheduled());

        // on when expected.
        b = true;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());

        // does not cancel, as expected
        b = false;
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());
    }

    @Test
    public void compositionTest() {
        Trigger f = new Trigger(() -> false);
        Trigger t = new Trigger(() -> true);

        assertFalse(f.getAsBoolean());
        assertTrue(t.getAsBoolean());

        assertFalse(f.and(t).getAsBoolean());
        assertTrue(f.nand(t).getAsBoolean());
        assertTrue(f.or(t).getAsBoolean());
        assertTrue(f.xor(t).getAsBoolean());
        assertFalse(f.nor(t).getAsBoolean());
        assertFalse(f.xnor(t).getAsBoolean());
        assertTrue(f.not().getAsBoolean());
        assertFalse(t.not().getAsBoolean());
    }

    /** This is also the TriggerGroup test. */
    @Test
    public void cancelCommandOnDisable() {
        b = true;
        Trigger t = new Trigger(bool);
        TriggerGroup tg = new TriggerGroup();
        Command c = new RunCommand(() -> {});

        t.trigger(WHILE_HIGH, c).join(tg);
        CommandScheduler.getInstance().run();

        assertTrue(c.isScheduled());

        // disabled triggers should immediately cancel any commands they are
        // triggering.
        t.disable();
        assertFalse(c.isScheduled());

        // re enable
        t.enable();
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());

        // disabled trigger groups should disable all triggers, consequentially
        // caneling commands.
        tg.disable();
        assertFalse(c.isScheduled());

        tg.enable();
        CommandScheduler.getInstance().run();
        assertTrue(c.isScheduled());
    }
}
