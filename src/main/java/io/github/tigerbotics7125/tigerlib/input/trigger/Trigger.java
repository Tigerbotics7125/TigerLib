/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.trigger;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * This class provides a way to track a boolean compared to time as a circuit
 * would.
 * <p>
 * This class is meant to be a replacement for WPILib's version. Anything that
 * uses a WPILib Trigger can easily be converted to work with this class.
 *
 * @author Jeffrey Morris | 7125 Tigerbotics
 */
public class Trigger implements BooleanSupplier, Sendable {

    /**
     * Activation conditions for a {@link Trigger}.
     * <p>
     * These values are representative of different ways a circuit could change
     * its state.
     */
    public enum ActivationCondition {
        WHILE_LOW(0), WHILE_HIGH(1), ON_FALLING(2), ON_RISING(3),;

        private final int mType;

        private ActivationCondition(int type) {
            mType = type;
        }

        public boolean isWhile() {
            if (mType == WHILE_LOW.mType || mType == WHILE_HIGH.mType)
                return true;
            else
                return false;
        }
    }

    private final BooleanSupplier mInput;
    private final ActivationCondition mActivation;
    private final List<Command> mCommands;
    private boolean mEnabled;

    /**
     * Create a new {@link Trigger} Object.
     * <p>
     * NOTE: This Constructor is <b>private</b> to simplify user code.
     *
     * @param input      The boolean input.
     * @param activateOn The {@link ActivationCondition} to follow.
     */
    private Trigger(BooleanSupplier input, ActivationCondition activateOn) {
        mInput = input;
        mActivation = activateOn;
        mCommands = new ArrayList<>();
        mEnabled = true;
    }

    /**
     * Create a new {@link Trigger} object.
     *
     * @param input The boolean input.
     */
    public Trigger(BooleanSupplier input) {
        this(input, ActivationCondition.ON_RISING);
    }

    /** @return The input to this {@link Trigger}. */
    public boolean getAsBoolean() {
        return get();
    }

    /** @return The state of this {@link Trigger}. */
    public boolean get() {
        return mInput.getAsBoolean();
    }

    public Trigger join(TriggerGroup triggerGroup) {
        triggerGroup.enroll(this);
        return this;
    }

    public Trigger leave(TriggerGroup triggerGroup) {
        triggerGroup.dismiss(this);
        return this;
    }

    public Trigger trigger(Runnable toRun) {
        return trigger(new InstantCommand(toRun));
    }

    /**
     * Execute the given command when/while this {@link Trigger} is triggered.
     *
     * @param cmd The Command to trigger.
     * @return This {@link Trigger} for chaining methods.
     */
    public Trigger trigger(Command cmd) {
        CommandScheduler.getInstance().addButton(new Runnable() {
            private boolean mLastState = get();

            @Override
            public void run() {
                boolean state = get();
                boolean toActivate = switch (mActivation) {
                    case WHILE_LOW -> !state;
                    case WHILE_HIGH -> state;
                    case ON_FALLING -> !state && mLastState;
                    case ON_RISING -> state && !mLastState;
                };

                if (toActivate && mEnabled && !cmd.isScheduled())
                    cmd.schedule();
                // cancel "while" commands when they should no longer be active.
                else if (!toActivate && cmd.isScheduled() && mActivation.isWhile())
                    cmd.cancel();

                mLastState = state;
            }
        });

        mCommands.add(cmd);
        return this;
    }

    /**
     * @param activateOn The {@link ActivationCondition} to use.
     * @return A {@link Trigger} with the same input, but with a different
     *         ActivationCondition.
     */
    public Trigger activate(ActivationCondition activateOn) {
        return new Trigger(mInput, activateOn);
    }

    /**
     * Private setter method for {@link Sendable}.
     *
     * @param enabled Whether to enable the trigger.
     */
    private void setState(boolean enabled) {
        mEnabled = enabled;
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
     * Creates a new {@link Trigger} which bases its input of the debounced
     * input of this {@link Trigger}.
     * <p>
     * Depending on the {@link ActivationCondition} of this {@link Trigger}, the
     * returned debounced {@link Trigger} will debounce in different ways.
     * <p>
     * rising and falling {@link Trigger}s will debounce is the same way, but
     * other will debounce in both directions.
     *
     * @param debounceTimeSeconds Time in seconds to debounce the input.
     * @return A debounced {@link Trigger}.
     */
    public Trigger debounce(double debounceTimeSeconds) {
        DebounceType t = switch (mActivation) {
            case WHILE_LOW, WHILE_HIGH -> DebounceType.kBoth;
            case ON_FALLING -> DebounceType.kFalling;
            case ON_RISING -> DebounceType.kRising;
        };
        // Java 11
        /*
         * switch (mActivation) { case WHILE_LOW: case WHILE_HIGH: case
         * ON_TRANSITION: t = DebounceType.kBoth; break; case ON_FALLING: t =
         * DebounceType.kFalling; break; case ON_RISING: t =
         * DebounceType.kRising; break; default: t = DebounceType.kBoth; break;
         * }
         */

        Debouncer d = new Debouncer(debounceTimeSeconds, t);
        return new Trigger(() -> d.calculate(this.getAsBoolean()));
    }

    /**
     * Create a {@link Trigger} representing a logical AND with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical AND, activating as this
     *         {@link Trigger} does.
     */
    public Trigger and(Trigger other) {
        return new Trigger(() -> get() && other.get(), mActivation);
    }

    /**
     * Create a {@link Trigger} representing a logical NAND with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical NAND, activating as this
     *         {@link Trigger} does.
     */
    public Trigger nand(Trigger other) {
        return new Trigger(() -> !and(other).get(), mActivation);
    }

    /**
     * Create a {@link Trigger} representing a logical OR with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical OR, activating as this
     *         {@link Trigger} does.
     */
    public Trigger or(Trigger other) {
        return new Trigger(() -> get() || other.get(), mActivation);
    }

    /**
     * Create a {@link Trigger} representing a logical XOR with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical XOR, activating as this
     *         {@link Trigger} does.
     */
    public Trigger xor(Trigger other) {
        return new Trigger(() -> get() ^ other.get(), mActivation);
    }

    /**
     * Create a {@link Trigger} representing a logical NOR with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical NOR, activating as this
     *         {@link Trigger} does.
     */
    public Trigger nor(Trigger other) {
        return new Trigger(() -> !or(other).get(), mActivation);
    }

    /**
     * Create a {@link Trigger} representing a logical XNOR with another
     * {@link Trigger}.
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
     * @return A {@link Trigger} representing logical XNOR, activating as this
     *         {@link Trigger} does.
     */
    public Trigger xnor(Trigger other) {
        return new Trigger(() -> !xor(other).get(), mActivation);
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
     * @return A {@link Trigger} representing logical NOT, activating as this
     *         {@link Trigger} does.
     */
    public Trigger not() {
        return new Trigger(() -> !get(), mActivation);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setActuator(true);
        builder.setSafeState(this::disable);
        builder.setSmartDashboardType("Trigger");
        builder.addBooleanProperty("enabled", this::isEnabled, this::setState);
        builder.addBooleanProperty("output", this::get, null);
    }
}
