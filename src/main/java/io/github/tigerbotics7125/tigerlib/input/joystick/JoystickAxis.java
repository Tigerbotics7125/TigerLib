package io.github.tigerbotics7125.tigerlib.input.joystick;

import java.util.function.Function;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.GenericHID;
import io.github.tigerbotics7125.tigerlib.util.JoystickUtil;

public class JoystickAxis implements Sendable {
  private final GenericHID mJoystick;
  private final int mAxis;
  private final boolean mInverted;

  private Function<Double, Double> mInputCleaner = (input) -> {
      input = JoystickUtil.deadband(input, .075);
      input = JoystickUtil.ramp(input, 3);
      input = JoystickUtil.clamp(input, -1, 1);
      return input;
  };

  public JoystickAxis(GenericHID joystick, int axis, boolean inverted) {
      mJoystick = joystick;
      mAxis = axis;
      mInverted = inverted;
  }

  public double get() {
      return mInputCleaner.apply(getRaw());
  }

  public double getRaw() {
      return mJoystick.getRawAxis(mAxis) * (mInverted ? -1 : 1);
  }

  public void setCleaner(Function<Double, Double> inputCleanerFunction) {
      mInputCleaner = inputCleanerFunction;
  }

  public JoystickAxis invert() {
    return new JoystickAxis(mJoystick, mAxis, !mInverted);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("raw", this::getRaw, null);
    builder.addDoubleProperty("val", this::get, null);
  }
}
