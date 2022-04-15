/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib.input.controller;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.EnumMap;
import tigerlib.input.AxisButton;
import tigerlib.input.AxisButton.ThresholdType;

/**
 * An input wrapper for the Xbox controller.
 *
 * <p>Saves RAM on the roborio by only instantiating a button when its used. CPU time is cheaper
 * than RAM in this scenario.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class XboxController extends GenericHID {

  /* Represents an analog axis on the joystick. */
  public enum Axis {
    kLeftX(0),
    kLeftY(1),
    kLT(2),
    kRT(3),
    kRightX(4),
    kRightY(5);

    public final int value;

    Axis(int value) {
      this.value = value;
    }
  }

  /* Represents a digital button on the joystick. */
  public enum Button {
    kA(1),
    kB(2),
    kX(3),
    kY(4),
    kLB(5),
    kRB(6),
    kBack(7),
    kStart(8),
    kLStick(9),
    kRStick(10);

    public final int value;

    Button(int value) {
      this.value = value;
    }
  }

  private final EnumMap<Button, JoystickButton> mButtons = new EnumMap<>(Button.class);
  private final EnumMap<Axis, AxisButton> mAxes = new EnumMap<>(Axis.class);
  public final POV pov;

  /**
   * Constructs a new instance of the joystick
   *
   * @param port The port on the Driver Station that the joystick is plugged into
   */
  public XboxController(int port) {
    super(port);
    pov = new POV(this);

    // tell wpi what were doing.
    HAL.report(tResourceType.kResourceType_XboxController, port + 1);
  }

  /**
   * Builds a {@link JoystickButton} for this controller from the provided {@link Button}
   *
   * @param button The Button to build for.
   * @return The JoystickButton.
   */
  private JoystickButton build(Button button) {
    return new JoystickButton(this, button.value);
  }

  /**
   * Builds a {@link AxisButton} for this controller from the provided {@link Axis}
   *
   * @param axis The Axis to build for.
   * @return The AxisButton.
   */
  private AxisButton build(
      Axis axis, double threshold, ThresholdType thresholdType, boolean inverted) {
    return new AxisButton(this, axis.value, threshold, thresholdType, inverted);
  }

  /**
   * Returns the a button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton a() {
    return mButtons.computeIfAbsent(Button.kA, this::build);
  }

  /**
   * Returns the b button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton b() {
    return mButtons.computeIfAbsent(Button.kB, this::build);
  }

  /**
   * Returns the x button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton x() {
    return mButtons.computeIfAbsent(Button.kX, this::build);
  }

  /**
   * Returns the y button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton y() {
    return mButtons.computeIfAbsent(Button.kY, this::build);
  }

  /**
   * Returns the left bumper button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton lb() {
    return mButtons.computeIfAbsent(Button.kLB, this::build);
  }

  /**
   * Returns the right bumper button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton rb() {
    return mButtons.computeIfAbsent(Button.kRB, this::build);
  }

  /**
   * Returns the back button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton back() {
    return mButtons.computeIfAbsent(Button.kBack, this::build);
  }

  /**
   * Returns the start button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton start() {
    return mButtons.computeIfAbsent(Button.kStart, this::build);
  }

  /**
   * Returns the left stick button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton lStick() {
    return mButtons.computeIfAbsent(Button.kLStick, this::build);
  }

  /**
   * Returns the right stick button's JoystickButton object
   *
   * <p>to get its value, use {@link JoystickButton#get()}
   */
  public JoystickButton rStick() {
    return mButtons.computeIfAbsent(Button.kRStick, this::build);
  }

  /**
   * Get the left stick's x {@link AxisButton}
   *
   * <p>to determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getValue()}
   *
   * <p>Note: This axis is inverted.
   */
  public AxisButton leftX() {
    return mAxes.computeIfAbsent(
        Axis.kLeftX, (axis) -> build(axis, .98, ThresholdType.kDeadband, true));
  }

  /**
   * Get the left stick's y {@link AxisButton}
   *
   * <p>to determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getValue()}
   */
  public AxisButton leftY() {
    return mAxes.computeIfAbsent(
        Axis.kLeftY, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
  }

  /**
   * Get the left trigger's {@link AxisButton}
   *
   * <p>To Determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getVal()}
   */
  public AxisButton lt() {
    return mAxes.computeIfAbsent(
        Axis.kLT, (axis) -> build(axis, 0.02, ThresholdType.kGreaterThan, false));
  }

  /**
   * Get the right trigger's {@link AxisButton}
   *
   * <p>To Determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getVal()}
   */
  public AxisButton rt() {
    return mAxes.computeIfAbsent(
        Axis.kRT, (axis) -> build(axis, 0.02, ThresholdType.kGreaterThan, false));
  }

  /**
   * Get the right stick's x {@link AxisButton}
   *
   * <p>to determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getValue()}
   *
   * <p>Note: This axis is inverted.
   */
  public AxisButton rightX() {
    return mAxes.computeIfAbsent(
        Axis.kRightX, (axis) -> build(axis, .98, ThresholdType.kDeadband, true));
  }

  /**
   * Get the right stick's y {@link AxisButton}
   *
   * <p>to determine if it is pressed, use {@link AxisButton#get()}
   *
   * <p>To get its value, use {@link AxisButton#getValue()}
   */
  public AxisButton rightY() {
    return mAxes.computeIfAbsent(
        Axis.kRightY, (axis) -> build(axis, .98, ThresholdType.kDeadband, false));
  }
}
