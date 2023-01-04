/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.input.trigger;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for all of the group managing features of this
 * {@link Trigger} class.
 * <p>
 * This class provides a way to manage a group of {@link Trigger}s.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class TriggerGroup implements Sendable {
    private final List<Trigger> mTriggers;
    private boolean mEnabled;

    /** Create a new {@link TriggerGroup} Object. */
    public TriggerGroup() {
	mTriggers = new ArrayList<>();
	mEnabled = true;
    }

    /**
     * Private setter method for {@link Sendable}.
     *
     * @param enabled Whether to enable the trigger.
     */
    private void setState(boolean enabled) {
	if (enabled)
	    enable();
	else
	    disable();
    }

    /** Enable all {@link Trigger}s withing this {@link TriggerGroup}. */
    public void enable() {
	mEnabled = true;
	mTriggers.forEach(Trigger::enable);
    }

    /** Disable all {@link Trigger}s withing this {@link TriggerGroup}. */
    public void disable() {
	mEnabled = false;
	mTriggers.forEach(Trigger::disable);
    }

    /** @return If this {@link TriggerGroup} is enabled. */
    public boolean isEnabled() {
	return mEnabled;
    }

    /** @return If this {@link TriggerGroup} is disabled. */
    public boolean isDisabled() {
	return !mEnabled;
    }

    /**
     * Enroll a {@link Trigger} to follow this {@link TriggerGroup}.
     *
     * @param trigger The {@link Trigger} to enroll.
     */
    public void enroll(Trigger trigger) {
	mTriggers.add(trigger);

	// ensure trigger follows this groups state.
	if (mEnabled)
	    trigger.enable();
	else
	    trigger.disable();
    }

    /**
     * Dissmiss a {@link Trigger} from following this {@link TriggerGroup}.
     *
     * @param trigger The {link Trigger} to dismiss.
     */
    public void dismiss(Trigger trigger) {
	mTriggers.remove(trigger);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
	builder.setActuator(true);
	builder.setSafeState(this::disable);
	builder.addBooleanProperty("enabled", this::isEnabled, this::setState);
    }
}
