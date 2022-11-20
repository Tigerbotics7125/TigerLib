/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.control.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

import java.util.function.DoubleSupplier;

/**
 * Basic control class for a mecanum drivetrain.
 *
 * <p>If PID control and feed forward are desired; this class should be extended and the
 * functionality added per use case.
 *
 * @author Jeff Morris | Tigerbotics 7125
 */
public class MecanumDrivetrain {
    private final MotorController kFL;
    private final MotorController kRL;
    private final MotorController kFR;
    private final MotorController kRR;
    private final Gyro kGyro;
    private final PIDController kHeadingPID;

    private boolean mFieldOriented = true;
    private boolean mMaintainHeading = false;

    private Rotation2d mDesiredHeading;

    /**
     * @param frontLeft Front left controller.
     * @param rearLeft Rear left controller.
     * @param frontRight Front right controller.
     * @param rearRight Rear right controller.
     * @param gyro The gyroscope.
     * @param headingPID PID controller for rotation while maintaining heading.
     */
    public MecanumDrivetrain(
            MotorController frontLeft,
            MotorController rearLeft,
            MotorController frontRight,
            MotorController rearRight,
            Gyro gyro,
            PIDController headingPID) {
        kFL = frontLeft;
        kRL = rearLeft;
        kFR = frontRight;
        kRR = rearRight;
        kGyro = gyro;
        kHeadingPID = headingPID;
    }

    /**
     * Drive based off of clean input.
     *
     * @param xSpeed Robot x (forward) speed [-1,1].
     * @param ySpeed Robot y (strafe) speed [-1,1].
     * @param zSpeed Robot z (rotate) speed [-1.1].
     */
    public void drive(DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier zSpeed) {
        drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), zSpeed.getAsDouble());
    }

    /**
     * Drive based off of joystick input.
     *
     * @param xSpeed Robot x (forward) speed [-1,1].
     * @param ySpeed Robot y (strafe) speed [-1,1].
     * @param zSpeed Robot z (rotate) speed [-1.1].
     */
    public void drive(double xSpeed, double ySpeed, double zSpeed) {

        if (mMaintainHeading) {
            // negate PID to go closer to desired heading, not farther.
            zSpeed = -kHeadingPID.calculate(kGyro.getAngle(), mDesiredHeading.getDegrees());
        }

        MecanumDrive.WheelSpeeds targetSpeeds =
                MecanumDrive.driveCartesianIK(
                        ySpeed, xSpeed, zSpeed, mFieldOriented ? -kGyro.getAngle() : 0.0);

        setSpeeds(targetSpeeds);
    }

    /**
     * Manually set the speeds of each wheel.
     *
     * @param speeds The wheel speeds.
     */
    public void setSpeeds(MecanumDrive.WheelSpeeds speeds) {
        kFL.set(speeds.frontLeft);
        kRL.set(speeds.rearLeft);
        kFR.set(speeds.frontRight);
        kRR.set(speeds.rearRight);
    }

    /**
     * Controls whether this drivetrain will attempt to maintain its current heading or not.
     *
     * <p>Upon setting this method to true, the drivetrain will store its current heading, and
     * attempt to maintain it.
     *
     * <p>This also means that if you need to reset the maintained heading, simply set this method
     * to true again.
     *
     * @param maintainHeading Whether to maintain the current heading or not.
     */
    public void setMaintainHeading(boolean maintainHeading) {
        mMaintainHeading = maintainHeading;
        if (mMaintainHeading) {
            mDesiredHeading = kGyro.getRotation2d();
        }
    }

    /**
     * Controls whether this drivetrain will operate under field oriented controls.
     *
     * @param fieldOriented Whether to operate under field oriented controls.
     */
    public void setFieldOriented(boolean fieldOriented) {
        mFieldOriented = fieldOriented;
    }

    /** @return If this drivetrain is attempting to maintain its heading. */
    public boolean isMaintainHeading() {
        return mMaintainHeading;
    }

    /** @return If this drivetrain is operating field oriented. */
    public boolean isFieldOriented() {
        return mFieldOriented;
    }
}
