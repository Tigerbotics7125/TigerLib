/*
 * Copyright (c) 2022-2023 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.trigger;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * This class provides a way to track a boolean compared to time as a circuit would.
 *
 * <p>This class is meant to be a replacement for WPILib's version. Anything that uses a WPILib
 * Trigger can easily be converted to work with this class.
 *
 * @author Jeffrey Morris | 7125 Tigerbotics
 */
public class Trigger implements BooleanSupplier, Sendable {

    /**
     * Activation conditions for a {@link Trigger}.
     *
     * <p>These values are representative of different ways a circuit could change its state.
     */
    public enum ActivationCondition {
        WHILE_LOW,
        WHILE_HIGH,
        ON_FALLING,
        ON_RISING;

        public boolean isWhile() {
            if (this == WHILE_LOW || this == WHILE_HIGH) return true;
            return false;
        }
    }

    // Event loop used to bind and poll triggers.
    private static final EventLoop mEventLoop =
            CommandScheduler.getInstance().getDefaultButtonLoop();

    // The "raw" input supplier.
    private final BooleanSupplier mInput;
    // List of commands, these get canceled if this Trigger gets disabled.
    private final List<Command> mCommands;
    // Whether or not this trigger is enabled / disabled.
    private boolean mEnabled;

    /**
     * Create a new {@link Trigger} Object.
     *
     * @param input The boolean input.
     */
    public Trigger(BooleanSupplier input) {
        mInput = input;
        mCommands = new ArrayList<>();
        mEnabled = true;
    }

    /**
     * Create a {@link Trigger} Object with a debouncer on a raw input.
     *
     * @param input The boolean input.
     * @param debouncer The debouncer object to pad bounces with.
     */
    public Trigger(BooleanSupplier input, Debouncer debouncer) {
        mInput = () -> debouncer.calculate(input.getAsBoolean());
        mCommands = new ArrayList<>();
        mEnabled = true;
    }

    /** @return The input to this {@link Trigger}. */
    @Override
    public boolean getAsBoolean() {
        return mInput.getAsBoolean();
    }

    /** */
    public Trigger join(TriggerGroup triggerGroup) {
        triggerGroup.enroll(this);
        return this;
    }

    public Trigger leave(TriggerGroup triggerGroup) {
        triggerGroup.dismiss(this);
        return this;
    }

    /**
     * Execute the given command when/while this {@link Trigger} is triggered.
     *
     * @param cmd The Command to trigger.
     * @return This {@link Trigger} for chaining methods.
     */
    public Trigger trigger(ActivationCondition activateFor, Command cmd) {
        Runnable trigger =
                new Runnable() {
                    // Variable which stores the previous input state.
                    private boolean lastState = getAsBoolean();

                    @Override
                    public void run() {
                        // If this trigger is disabled, do nothing.
                        if (!mEnabled) return;

                        // The current input state.
                        boolean currentState = getAsBoolean();
                        // Whether or not the command should be scheduled.
                        boolean toActivate =
                                switch (activateFor) {
                                    case WHILE_LOW -> !currentState;
                                    case WHILE_HIGH -> currentState;
                                    case ON_FALLING -> !currentState && lastState;
                                    case ON_RISING -> currentState && !lastState;
                                };

                        // If the command should be triggered and isnt scheduled yet, schedule it.
                        if (toActivate && !cmd.isScheduled()) cmd.schedule();
                        // IF the command should no longer be triggered, and should be canceled when
                        // false (ie: a "WHILE" condition), cancel the command.
                        else if (!toActivate && activateFor.isWhile()) cmd.cancel();

                        // update the last state.
                        lastState = currentState;
                    }
                };

        // Add this trigger to the event loop so it will get polled periodically.
        mEventLoop.bind(trigger);
        // Add this command to the command list to cancel any commands when the
        mCommands.add(cmd);
        return this;
    }

    /** Enables this {@link Trigger}. */
    public void enable() {
        mEnabled = true;
    }

    /** Disables this {@link Trigger}. */
    public void disable() {
        mEnabled = false;
        // Cancel commands if the trigger gets disabled.
        mCommands.forEach(Command::cancel);
    }

    /** @return If this {@link Trigger} is enabled. */
    public boolean isEnabled() {
        return mEnabled;
    }

    /** @return If this {@link Trigger} is disabled. */
    public boolean isDisabled() {
        return !mEnabled;
    }

    /**
     * Create a {@link Trigger} representing a logical AND with another {@link Trigger}.
     *
     * <pre>
     * AND
     * |A|B||C|
     * |0|0||0|
     * |1|0||0|
     * |0|1||0|
     * |1|1||1|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical AND.
     */
    public Trigger and(Trigger other) {
        return new Trigger(() -> this.getAsBoolean() && other.getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical NAND with another {@link Trigger}.
     *
     * <pre>
     * NAND
     * |A|B||C|
     * |0|0||1|
     * |1|0||1|
     * |0|1||1|
     * |1|1||0|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical NAND.
     */
    public Trigger nand(Trigger other) {
        return new Trigger(() -> !and(other).getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical OR with another {@link Trigger}.
     *
     * <pre>
     * OR
     * |A|B||C|
     * |0|0||0|
     * |1|0||1|
     * |0|1||1|
     * |1|1||1|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical OR.
     */
    public Trigger or(Trigger other) {
        return new Trigger(() -> getAsBoolean() || other.getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical XOR with another {@link Trigger}.
     *
     * <pre>
     * XOR
     * |A|B||C|
     * |0|0||0|
     * |1|0||1|
     * |0|1||1|
     * |1|1||0|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical XOR.
     */
    public Trigger xor(Trigger other) {
        return new Trigger(() -> getAsBoolean() ^ other.getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical NOR with another {@link Trigger}.
     *
     * <pre>
     * NOR
     * |A|B||C|
     * |0|0||1|
     * |1|0||0|
     * |0|1||0|
     * |1|1||0|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical NOR.
     */
    public Trigger nor(Trigger other) {
        return new Trigger(() -> !or(other).getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical XNOR with another {@link Trigger}.
     *
     * <pre>
     * XNOR
     * |A|B||C|
     * |0|0||1|
     * |1|0||0|
     * |0|1||0|
     * |1|1||1|
     * </pre>
     *
     * @param other The other {@link Trigger}.
     * @return A {@link Trigger} representing logical XNOR.
     */
    public Trigger xnor(Trigger other) {
        return new Trigger(() -> !xor(other).getAsBoolean());
    }

    /**
     * Create a {@link Trigger} representing a logical NOT.
     *
     * <pre>
     * NOT
     * |A||B|
     * |0||1|
     * |1||0|
     * </pre>
     *
     * @return A {@link Trigger} representing logical NOT.
     */
    public Trigger not() {
        return new Trigger(() -> !getAsBoolean());
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Trigger");
        builder.addBooleanProperty("enabled", this::isEnabled, null);
        builder.addBooleanProperty("output", this::getAsBoolean, null);
    }
}
