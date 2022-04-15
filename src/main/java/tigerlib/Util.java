/**
 * Copyright (C) 2022, Tigerbotics' team members and all other contributors.
 * Open source software; you can modify and/or share this software.
 */
package tigerlib;

public class Util {

  /**
   * Multiplies a x by an exponent, preserving sign.
   *
   * @param x input
   * @param exp exponent
   * @return
   */
  public static double ramp(double x, double exp) {
    return Math.signum(x) * Math.pow(Math.abs(x), exp);
  }

  /**
   * Applies a deadband such that: [-infinity, -deadband] {0} [deadband, infinity].
   *
   * @param x input
   * @param d deadband
   */
  public static double deadband(double x, double d) {
    return Math.abs(x) < d ? 0 : x;
  }

  /**
   * Applies a clamp such that: [min, max].
   *
   * @param x input
   * @param low lower boundery
   * @param high upper boundery
   */
  public static double clamp(double x, double low, double high) {
    return Math.max(low, Math.min(x, high));
  }

  /**
   * Applies a deadband such that: [-infinity, -deadband] {0} [deadband, infinity], while x^exp
   * preserving sign.
   *
   * @param x input
   * @param d deadband
   * @param s sensitivity
   * @return where x < -deadband, -(x^s), where -deadband <= x <= deadband, 0, and where deadband <
   *     x, x^s
   */
  public static double smooth(double x, double d, double s) {

    x = deadband(x, d);

    if (x < 0) {
      return -Math.pow(Math.abs(x), s);
    } else {
      return Math.pow(x, s);
    }
  }

  /**
   * Applies {@link #smooth(x, d, s)} then {@link #clamp(x, min, max)}.
   *
   * @param x input
   * @param d deadband
   * @param s sensitivity
   * @param min lower boundery
   * @param max upper boundery
   */
  public static double smoothWClamp(double x, double d, double s, double min, double max) {
    return clamp(smooth(x, d, s), min, max);
  }

  /**
   * Changes the scale of a value from one range to another.
   *
   * @param x input
   * @param oldMin initial lower boundery
   * @param oldMax initial upper boundery
   * @param newMin new lower boundery
   * @param newMax new upper boundery
   */
  public static double scaleInput(
      double x, double oldMin, double oldMax, double newMin, double newMax) {
    return (x - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
  }
}
