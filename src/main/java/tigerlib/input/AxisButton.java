/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib.input;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * A Button wrapper for axes.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 * @author Spectrum 3847
 */
public class AxisButton extends Button {
  private final GenericHID mJoystick;
  private final int mAxis;
  private boolean mInverted = false;
  private double mThreshold = 0.0;
  private ThresholdType mThresholdType = ThresholdType.kExact;

  public static enum ThresholdType {
    kExact,
    kLessThan,
    kGreaterThan,
    kDeadband;
  }

  /**
   * Constructs an AxisButton.
   *
   * <p><note> Invert param only inverts the returned value from the axis; if the axis would
   * naturally return .46, the interperated value is -.46. </note>
   *
   * @param joystick The HID device to read the axis from.
   * @param axis The axis index to read from.
   * @param threshold The threshold to determine if the axis is pressed.
   * @param thresholdType How to interpret the threshold.
   * @param invert Whether the axis is inverted.
   */
  public AxisButton(
      GenericHID joystick,
      int axis,
      double threshold,
      ThresholdType thresholdType,
      boolean invert) {
    mJoystick = joystick;
    mAxis = axis;
    mThreshold = threshold;
    mThresholdType = thresholdType;
    mInverted = invert;
  }

  /** @return The axis value * -1 if the axis is inverted. */
  public double getVal() {
    return mJoystick.getRawAxis(mAxis * (mInverted ? -1 : 1));
  }

  /** @return Whether the axis is pressed. */
  @Override
  public boolean get() {
    switch (this.mThresholdType) {
      case kExact:
        return getVal() == mThreshold;
      case kLessThan:
        return getVal() < mThreshold;
      case kGreaterThan:
        return getVal() > mThreshold;
      case kDeadband:
        return Math.abs(getVal()) > mThreshold;
      default:
        return false;
    }
  }
}
