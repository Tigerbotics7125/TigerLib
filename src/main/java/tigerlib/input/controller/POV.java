/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib.input.controller;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import java.util.EnumMap;

public class POV {

  private enum POVAngle {
    kCenter(-1),
    kUp(0),
    kUpRight(45),
    kRight(90),
    kDownRight(135),
    kDown(180),
    kDownLeft(225),
    kLeft(270),
    kUpLeft(315);

    public final int value;

    POVAngle(int value) {
      this.value = value;
    }
  }

  private final EnumMap<POVAngle, POVButton> mPovs = new EnumMap<>(POVAngle.class);
  private final GenericHID mHid;
  private final int mPovNumber;

  public POV(GenericHID hid) {
    this(hid, 0);
  }

  /**
   * Constructs a ControllerPOV.
   *
   * @param hid the HID controller to read the POV from.
   * @param povNumber The controller POV index to use.
   */
  public POV(GenericHID hid, int povNumber) {
    mHid = hid;
    mPovNumber = povNumber;
  }

  /**
   * Builds a {@link POVButton} for the given {@link POVAngle}.
   *
   * @param angle The POVAngle to build for.
   * @return Built POVButton.
   */
  private POVButton build(POVAngle angle) {
    return new POVButton(mHid, angle.value, mPovNumber);
  }

  /**
   * Returns the centered (not pressed) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton center() {
    return mPovs.computeIfAbsent(POVAngle.kCenter, this::build);
  }

  /**
   * Returns the upper (0 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton up() {
    return mPovs.computeIfAbsent(POVAngle.kUp, this::build);
  }

  /**
   * Returns the upper right (45 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton upRight() {
    return mPovs.computeIfAbsent(POVAngle.kUpRight, this::build);
  }

  /**
   * Returns the right (90 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton right() {
    return mPovs.computeIfAbsent(POVAngle.kRight, this::build);
  }

  /**
   * Returns the lower right (135 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton downRight() {
    return mPovs.computeIfAbsent(POVAngle.kDownRight, this::build);
  }

  /**
   * Returns the lower (180 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton down() {
    return mPovs.computeIfAbsent(POVAngle.kDown, this::build);
  }

  /**
   * Returns the lower left (225 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton downLeft() {
    return mPovs.computeIfAbsent(POVAngle.kDownLeft, this::build);
  }

  /**
   * Returns the left (270 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton left() {
    return mPovs.computeIfAbsent(POVAngle.kLeft, this::build);
  }

  /**
   * Returns the upper left (315 degrees) POVButton object.
   *
   * <p>To get its value, use {@link POVButton#get()}.
   */
  public POVButton upLeft() {
    return mPovs.computeIfAbsent(POVAngle.kUpLeft, this::build);
  }
}
