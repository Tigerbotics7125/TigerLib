/*
 * Copyright (c) 2022 Tigerbotics and it's members. All rights reserved.
 * This work is licensed under the terms of the GNU GPLv3 license
 * found in the root directory of this project.
 */
package io.github.tigerbotics7125.tigerlib.vision;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonTargetSortMode;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

/**
 * This class removes a lot of boiler plate code when designing vison logic, and allows for some
 * comfort features such as caching results.
 *
 * @author Jeffrey Morris | Tigerboitcs 7125
 */
public class SnakeEyes {

    protected final PhotonCamera mCam;
    protected final Map<Integer, Pose3d> mAprilTags;

    /** Transformation from robots center (x, y), and the floor (z) */
    protected Transform3d mRobotToCamera;

    protected PhotonPipelineResult mCachedResult;

    public SnakeEyes(String cameraName, Transform3d robotToCamera) {
        this(NetworkTableInstance.getDefault(), cameraName, robotToCamera);
    }

    public SnakeEyes(
            NetworkTableInstance ntInstance, String cameraName, Transform3d robotToCamera) {
        mCam = new PhotonCamera(ntInstance, cameraName);
        mAprilTags = new HashMap<>();
        mRobotToCamera = robotToCamera;
    }

    //
    // STATIC
    //

    /** Port forward the vision dashboard over rio usb connection. */
    public static void portforward() {
        PortForwarder.add(5800, "10.71.25.11", 5800);
    }

    //
    // SETTERS
    //

    /**
     * Add an AprilTag to the vision systems known tag locations.
     *
     * @param tagId
     * @param tagPose
     */
    public void addAprilTag(int tagId, Pose3d tagPose) {
        mAprilTags.put(tagId, tagPose);
    }

    /** Turn the LEDs on. */
    public void ledsOn() {
        mCam.setLED(VisionLEDMode.kOn);
    }

    /** Turn the LEDs off. */
    public void ledsOff() {
        mCam.setLED(VisionLEDMode.kOff);
    }

    /** Blink the LEDs. */
    public void ledsBlink() {
        mCam.setLED(VisionLEDMode.kBlink);
    }

    /** Set the LEDs to their default mode. */
    public void ledsDefault() {
        mCam.setLED(VisionLEDMode.kDefault);
    }

    /**
     * Set the LEDs to the given mode.
     *
     * @param ledMode
     */
    public void setLeds(VisionLEDMode ledMode) {
        mCam.setLED(ledMode);
    }

    /**
     * Set the pipeline via its index.
     *
     * @param index
     */
    public void setPipeline(int index) {
        mCam.setPipelineIndex(index);
    }

    //
    // GETTERS
    //

    public Transform3d getRobotToCamera() {
        return mRobotToCamera;
    }

    /**
     * @param tagId The tag to get.
     * @return the Pose3d of the tag, null if tag has not been added.
     */
    public Pose3d getTagPose(int tagId) {
        return mAprilTags.get(tagId);
    }

    /**
     * Returns and <b>caches</b> the latest result. Allows users to make this call without
     * performing actions on the returned value.
     *
     * @return The latest result.
     */
    public PhotonPipelineResult getLatestResult() {
        mCachedResult = mCam.getLatestResult();
        return mCachedResult;
    }

    private PhotonPipelineResult getCachedResult() {
        return mCachedResult == null ? getLatestResult() : mCachedResult;
    }

    /** @return List of seen targets, if there are none, an empty list. */
    public List<PhotonTrackedTarget> getTargets() {
        var results = getCachedResult();
        return results.hasTargets() ? results.getTargets() : List.of();
    }

    /** @return If any targets are seen. */
    public boolean hasTargets() {
        return !getTargets().isEmpty();
    }

    /** @return The latency of the vision system in milli seconds. */
    public double getLatency() {
        var results = getCachedResult();
        return results.getLatencyMillis();
    }

    /** @return The timestamp of the latest result in seconds. */
    public double getTimestamp() {
        var results = getCachedResult();
        return results.getTimestampSeconds();
    }

    /**
     * @param target The target to estimate robot pose by.
     * @return the Pose3d of the robot as known by the target.
     * @throws IllegalArgumentException if target fiducial id is not in known tags map.
     */
    public Pose3d getRobotPose(PhotonTrackedTarget target) {
        Pose3d targetPose = mAprilTags.get(target.getFiducialId());

        if (targetPose == null)
            throw new IllegalArgumentException("Target does not have known position.");

        Transform3d targetToCam = target.getBestCameraToTarget().inverse();

        return targetPose.transformBy(targetToCam).transformBy(mRobotToCamera.inverse());
    }

    /**
     * @param targetToFace
     * @param robotPose
     * @return The heading the robot should be at if it were to face the target head-on.
     */
    public Rotation2d getFaceTargetAngle(PhotonTrackedTarget targetToFace, Pose2d robotPose) {
        // get tags pose
        Pose2d tagPose = getTagPose(targetToFace.getFiducialId()).toPose2d();

        // get the triangle from robot to tag
        Transform2d robotToTag = new Transform2d(robotPose, tagPose);

        // determine the reference angle
        double angleToRotate = Math.atan2(robotToTag.getY(), robotToTag.getX());

        // the heading which will make the robot face the tag is its current
        // rotation
        // rotated by the reference.
        return robotPose.getRotation().rotateBy(new Rotation2d(angleToRotate));
    }

    /**
     * Removes tags which have ambiguities larger than the provided threshold.
     *
     * @param tags List of tags to edit.
     * @param ambiguityThreshold tags with ambiguity values greater than this will be removed.
     * @return The list of tags with ambiguous tags removed.
     */
    public List<PhotonTrackedTarget> removeAmbiguousTags(
            List<PhotonTrackedTarget> tags, double ambiguityThreshold) {
        List<PhotonTrackedTarget> ambiguousTags = new ArrayList<>();
        tags.forEach(
                (t) -> {
                    if (t.getPoseAmbiguity() > ambiguityThreshold) ambiguousTags.add(t);
                });
        ambiguousTags.forEach(tags::remove);
        return tags;
    }

    /**
     * @param sortMode
     * @param ambiguityThreshold
     * @return The best tag
     */
    public PhotonTrackedTarget getBestTag(
            PhotonTargetSortMode sortMode, double ambiguityThreshold) {
        List<PhotonTrackedTarget> targets = new ArrayList<>(getTargets());

        // remove tags which are too ambiguous
        targets = removeAmbiguousTags(targets, ambiguityThreshold);

        targets.sort(sortMode.getComparator());

        // after sorting, the best target is first in the list.
        return targets.get(0);
    }

    /**
     * @param ambiguityThreshold
     * @return A list of estimated robot poses.
     */
    public List<Pose3d> getRobotPoseEstimates(double ambiguityThreshold) {
        List<PhotonTrackedTarget> targets = getTargets();
        targets = removeAmbiguousTags(targets, ambiguityThreshold);

        List<Pose3d> robotPoses = new ArrayList<>();
        targets.forEach(
                (target) -> {
                    robotPoses.add(this.getRobotPose(target));
                });

        return robotPoses;
    }
}
