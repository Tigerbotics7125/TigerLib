/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib.input.controller;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * An input wrapper for the logitech flight joystick that actually makes sense.
 *
 * <p>Saves RAM on the roborio by only instantiating a button when its used. CPU time is cheaper
 * than RAM in this scenario.
 *
 * @author Jeffrey Morris | Tigerbotics 7125
 */
public class GExtreme3DProJoystick extends GenericHID {

  /** Represents an analog axis on the joystick */
  public enum Axis {
    kX(0),
    kY(1),
    kZ(2),
    kThrottle(3),
    kDpadX(5),
    kDpadY(6);

    public final int value;

    Axis(int value) {
      this.value = value;
    }
  }

  /** Represents a digital button on the joystick */
  public enum Button {
    kTrigger(1),
    kThumb(2),
    kTop3(3),
    kTop4(4),
    kTop5(5),
    kTop6(6),
    kBottom7(7),
    kBottom8(8),
    kBottom9(9),
    kBottom10(10),
    kBottom11(11),
    kBottom12(12);

    public final int value;

    Button(int value) {
      this.value = value;
    }
  }

  private JoystickButton _triggerButton;
  private JoystickButton _thumbButton;
  private JoystickButton _3Button;
  private JoystickButton _4Button;
  private JoystickButton _5Button;
  private JoystickButton _6Button;
  private JoystickButton _7Button;
  private JoystickButton _8Button;
  private JoystickButton _9Button;
  private JoystickButton _10Button;
  private JoystickButton _11Button;
  private JoystickButton _12Button;
  private POVButton _uButton;
  private POVButton _urButton;
  private POVButton _rButton;
  private POVButton _drButton;
  private POVButton _dButton;
  private POVButton _dlButton;
  private POVButton _lButton;
  private POVButton _ulButton;

  /**
   * Constructs a new instance of the joystick
   *
   * @param port The port index on Driver Station that the joystick is plugged into.
   */
  public GExtreme3DProJoystick(int port) {
    super(port);
  }

  /**
   * Returns the trigger's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton trigger() {
    if (_triggerButton == null) {
      _triggerButton = new JoystickButton(this, Button.kTrigger.value);
    }
    return _triggerButton;
  }

  /**
   * Returns the thumb's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton thumb() {
    if (_thumbButton == null) {
      _thumbButton = new JoystickButton(this, Button.kThumb.value);
    }
    return _thumbButton;
  }

  /**
   * Returns the 3 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton three() {
    if (_3Button == null) {
      _3Button = new JoystickButton(this, Button.kTop3.value);
    }
    return _3Button;
  }

  /**
   * Returns the 4 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton four() {
    if (_4Button == null) {
      _4Button = new JoystickButton(this, Button.kTop4.value);
    }
    return _4Button;
  }

  /**
   * Returns the 5 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton five() {
    if (_5Button == null) {
      _5Button = new JoystickButton(this, Button.kTop5.value);
    }
    return _5Button;
  }

  /**
   * Returns the 6 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton six() {
    if (_6Button == null) {
      _6Button = new JoystickButton(this, Button.kTop6.value);
    }
    return _6Button;
  }

  /**
   * Returns the 7 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton seven() {
    if (_7Button == null) {
      _7Button = new JoystickButton(this, Button.kBottom7.value);
    }
    return _7Button;
  }

  /**
   * Returns the 8 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton eight() {
    if (_8Button == null) {
      _8Button = new JoystickButton(this, Button.kBottom8.value);
    }
    return _8Button;
  }

  /**
   * Returns the 9 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton nine() {
    if (_9Button == null) {
      _9Button = new JoystickButton(this, Button.kBottom9.value);
    }
    return _9Button;
  }

  /**
   * Returns the 10 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton ten() {
    if (_10Button == null) {
      _10Button = new JoystickButton(this, Button.kBottom10.value);
    }
    return _10Button;
  }

  /**
   * Returns the 11 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton eleven() {
    if (_11Button == null) {
      _11Button = new JoystickButton(this, Button.kBottom11.value);
    }
    return _11Button;
  }

  /**
   * Returns the 12 button's {@link JoystickButton}.
   *
   * <p>To get its value, use {@link JoystickButton#get()}.
   */
  public JoystickButton twelve() {
    if (_12Button == null) {
      _12Button = new JoystickButton(this, Button.kBottom12.value);
    }
    return _12Button;
  }

  /**
   * Returns the upper (0 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton up() {
    if (_uButton == null) {
      _uButton = new POVButton(this, 0);
    }
    return _uButton;
  }

  /**
   * Returns the upper-right (45 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton upRight() {
    if (_urButton == null) {
      _urButton = new POVButton(this, 45);
    }
    return _urButton;
  }

  /**
   * Returns the right (90 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton right() {
    if (_rButton == null) {
      _rButton = new POVButton(this, 90);
    }
    return _rButton;
  }

  /**
   * Returns the downwards-right (135 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton downRight() {
    if (_drButton == null) {
      _drButton = new POVButton(this, 135);
    }
    return _drButton;
  }

  /**
   * Returns the downwards (180 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton down() {
    if (_dButton == null) {
      _dButton = new POVButton(this, 180);
    }
    return _dButton;
  }

  /**
   * Returns the downwards-left (225 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton downLeft() {
    if (_dlButton == null) {
      _dlButton = new POVButton(this, 225);
    }
    return _dlButton;
  }

  /**
   * Returns the left (270 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton left() {
    if (_lButton == null) {
      _lButton = new POVButton(this, 270);
    }
    return _lButton;
  }

  /**
   * Returns the upper-left (315 degrees) button's {@link POVButton}.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton upLeft() {
    if (_ulButton == null) {
      _ulButton = new POVButton(this, 315);
    }
    return _ulButton;
  }

  /**
   * Get the X value of the joystick.
   *
   * <p>Right is positive.
   *
   * @return The X value of the joystick.
   */
  public double xAxis() {
    return getRawAxis(Axis.kX.value);
  }

  /**
   * Get the Y value of the joystick.
   *
   * <p>Forward is positive.
   *
   * @return The Y value of the joystick.
   */
  public double yAxis() {
    return -getRawAxis(Axis.kY.value);
  }

  /**
   * Get the Z value of the joystick.
   *
   * <p>Clcok-wise is positive.
   *
   * @return The Z value of the joystick.
   */
  public double zAxis() {
    return getRawAxis(Axis.kZ.value);
  }

  /**
   * Get the throttle of the joystick.
   *
   * <p>Up Is positive.
   *
   * @return The throttle position of the joystick.
   */
  public double throttleAxis() {
    return -super.getRawAxis(Axis.kThrottle.value);
  }

  /**
   * Get the magnitude of the direction vector formed by the joystick's current position relative to
   * its origin.
   *
   * @return The magnitude of the direction vector
   */
  public double getMagnitude() {
    return Math.sqrt(Math.pow(xAxis(), 2) + Math.pow(yAxis(), 2));
  }

  /**
   * Get the direction of the vector formed by the joystick and its origin in radians.
   *
   * @return The direction of the vector in radians
   */
  public double getDirectionRadians() {
    return Math.atan2(xAxis(), -yAxis());
  }

  /**
   * Get the direction of the vector formed by the joystick and its origin in degrees.
   *
   * @return The direction of the vector in degrees
   */
  public double getDirectionDegrees() {
    return Math.toDegrees(getDirectionRadians());
  }
}
