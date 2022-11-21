/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input;

import io.github.tigerbotics7125.tigerlib.TigerLib;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * This class provides a way to track a boolean compared to time as a circuit would.
 *
 * <p>This class is meant to be a replacement for WPILib's version; however this class goes in a
 * different direction then WPILib; so to fully utilize the class will take extra work on the users
 * part.
 *
 * <p>The {@link io.github.tigerbotics7125.tigerlib.input.oi.OI} class is the intended use case for
 * this class, mapping button or other inputs to command execution.
 *
 * <p>The user must put {@code new TigerLib(this);} in their main {@code TimedRobot} class.
 *
 * @author Jeffrey Morris | 7125 Tigerbotics
 * @implNote TODO: Move Trigger groups to an interal class.
 */
public class Trigger implements BooleanSupplier {
    public static final String kDefaultGroup = "DEFAULT";

    private static Set<Trigger> mTriggers = new HashSet<>();
    private static Set<String> mActiveGroups = new HashSet<>();
    private static Set<String> mInactiveGroups = new HashSet<>();

    static {
        enableGroup(kDefaultGroup);
    }

    /**
     * Static call to update all {@link Trigger} Objects. Should only be called by {@link TigerLib},
     * not by the user.
     */
    public static void periodic() {
        // update last state so that transition conditions work properly.
        mTriggers.forEach((t) -> t.mLastState = t.getAsBoolean());
    }

    /**
     * Enable a Trigger group.
     *
     * @param group The group to enable.
     */
    public static void enableGroup(String group) {
        mActiveGroups.add(group);
        mInactiveGroups.remove(group);
    }

    /**
     * Enable a list of Trigger groups.
     *
     * @param groups The groups to enable.
     */
    public static void enableGroups(String... groups) {
        for (String g : groups) {
            Trigger.enableGroup(g);
        }
    }

    /**
     * Disable a Trigger group.
     *
     * @param group The group to disable.
     */
    public static void disableGroup(String group) {
        mInactiveGroups.add(group);
        mActiveGroups.remove(group);
    }

    /**
     * Disable a list of Trigger groups.
     *
     * @param groups The groups to disable.
     */
    public static void disableGroups(String... groups) {
        for (String g : groups) {
            Trigger.disableGroup(g);
        }
    }

    /**
     * Check if the Trigger group is enabled.
     *
     * @param group The group to check.
     * @return If the group is currently enabled.
     */
    public static boolean isGroupEnabled(String group) {
        return mActiveGroups.contains(group);
    }

    /**
     * Activation conditions for a Trigger.
     *
     * <p>These values are representative of different ways a circuit could change its state.
     */
    public enum ActivationCondition {
        WHILE_LOW,
        WHILE_HIGH,
        ON_FALLING,
        ON_RISING,
        ON_TRANSITION,
        ;
    }

    private final BooleanSupplier mInput;
    private final ActivationCondition mActivation;
    private boolean mDisabled;
    public boolean mLastState;

    /**
     * Create a Trigger object.
     *
     * @param input The boolean input.
     * @param activation The activation condition to control the trigger.
     */
    public Trigger(BooleanSupplier input, ActivationCondition activation) {
        mInput = input;
        mActivation = activation;
        mLastState = mInput.getAsBoolean();
        mTriggers.add(this);
    }

    /**
     * Equivalent to calling {@code new Trigger(input, ActivationCondition.WHILE_HELD)}
     *
     * @param input The boolean input.
     */
    public Trigger(BooleanSupplier input) {
        this(input, ActivationCondition.WHILE_HIGH);
    }

    /** @return The input to this Trigger. */
    public boolean getAsBoolean() {
        return mInput.getAsBoolean();
    }

    /** @return The state of this Trigger. */
    public boolean get() {
        boolean state = getAsBoolean();
        switch (mActivation) {
            case WHILE_LOW:
                return !state;
            case WHILE_HIGH:
                return state;
            case ON_FALLING:
                return !state && mLastState;
            case ON_RISING:
                return state && !mLastState;
            case ON_TRANSITION:
                return state != mLastState;
        }
        // mLastState = getAsBoolean();
        return false;
    }

    public Trigger trigger(Command cmd, String group) {
        // add command to scheduler.
        CommandScheduler.getInstance()
                .addButton(
                        () -> {
                            if (!mDisabled && get() && isGroupEnabled(group)) cmd.schedule();
                        });
        return this;
    }

    /**
     * Execute the given command when this Trigger is triggered.
     *
     * @param cmd
     * @return This Trigger for chaining methods.
     */
    public Trigger trigger(Command cmd) {
        return trigger(cmd, kDefaultGroup);
    }

    /**
     * @param activateOn
     * @return A Trigger with the same input, but with a different ActivationCondition.
     */
    public Trigger activate(ActivationCondition activateOn) {
        return new Trigger(mInput, activateOn);
    }

    /** @return If this Trigger is disabled. */
    public boolean isDisabled() {
        return mDisabled;
    }

    /** Disables this Trigger. */
    public void disable() {
        mDisabled = true;
    }

    /** Enables this Trigger. */
    public void enable() {
        mDisabled = true;
    }

    /**
     * Create a Trigger representing a logical AND with another Trigger.
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
     * @param other
     * @param activation
     * @return A Trigger representing logical AND, while following the given ActivationCondition.
     */
    public Trigger and(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> get() && other.get(), activation);
    }

    /**
     * Create a Trigger representing a logical AND with another Trigger.
     *
     * @param other
     * @return A Trigger representing logical AND, activating while high.
     */
    public Trigger and(Trigger other) {
        return and(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical NAND with another Trigger.
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
     * @param other
     * @param activation
     * @return A Trigger representing logical NAND, while following the givin ActivationCondition.
     */
    public Trigger nand(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> !(get() && other.get()), activation);
    }

    /**
     * Create a Trigger representing a logical NAND with another Trigger.
     *
     * @param other
     * @return A Trigger representing logical NAND, activating while high.
     */
    public Trigger nand(Trigger other) {
        return nand(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical OR with another Trigger.
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
     * @param other
     * @param activation
     * @return A Trigger representing logical OR, while following the given ActivationCondition.
     */
    public Trigger or(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> get() || other.get(), activation);
    }

    /**
     * Create a Trigger representing a logical OR with another trigger.
     *
     * @param other
     * @return A Trigger representing logical OR, activating while high.
     */
    public Trigger or(Trigger other) {
        return or(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical XOR with another Trigger.
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
     * @param other
     * @param activation
     * @return A Trigger representing logical XOR, while following the given ActivationCondition.
     */
    public Trigger xor(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> get() ^ other.get(), activation);
    }

    /**
     * Create a Trigger representing a logical XOR with another Trigger.
     *
     * @param other
     * @return A Trigger representing logical XOR, activating while high.
     */
    public Trigger xor(Trigger other) {
        return xor(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical NOR with another Trigger.
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
     * @param other
     * @param activation
     * @return A Trigger representing logical NOR, while following the given ActivationCondition.
     */
    public Trigger nor(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> !or(other).get(), activation);
    }

    /**
     * Create a Trigger representing a logical NOR with another Trigger.
     *
     * @param other
     * @return A Trigger representing logical NOR, activating while high.
     */
    public Trigger nor(Trigger other) {
        return nor(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical XNOR with another Trigger.
     *
     * <pre>
     * AND
     * |A|B||C|
     * |0|0||1|
     * |1|0||0|
     * |0|1||0|
     * |1|1||1|
     * </pre>
     *
     * @param other
     * @param activation
     * @return A Trigger representing logical XNOR, while following the given ActivationCondition.
     */
    public Trigger xnor(Trigger other, ActivationCondition activation) {
        return new Trigger(() -> !xor(other).get(), activation);
    }

    /**
     * Create a Trigger representing a logical XNOR with another Trigger.
     *
     * @param other
     * @return A Trigger representing logical XNOR, activating while high.
     */
    public Trigger xnor(Trigger other) {
        return xnor(other, ActivationCondition.WHILE_HIGH);
    }

    /**
     * Create a Trigger representing a logical NOT.
     *
     * <pre>
     * AND
     * |A||B|
     * |0||1|
     * |1||0|
     * </pre>
     *
     * @return A Trigger representing logical NOT, activating the same way this Trigger is.
     */
    public Trigger not() {
        return new Trigger(() -> !get(), mActivation);
    }
}
