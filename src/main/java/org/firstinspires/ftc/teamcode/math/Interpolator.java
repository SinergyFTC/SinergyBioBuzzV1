package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Vector2D;

import org.firstinspires.ftc.teamcode.util.Alliance;

import smile.interpolation.Interpolation2D;

public class Interpolator {
    private static double interpolate(Interpolation2D closeTarget, Interpolation2D farTarget, Vector2D pose, Alliance alliance) {
        final Vector2D newPose;
        if(alliance == Alliance.BLUE) {
            newPose = MirrorPosition.mirror(pose.toPose()).toVector2D();
        } else {
            newPose = pose;
        }
        if (newPose.y() > 72) {
            return closeTarget.interpolate(newPose.x(), newPose.y());
        } else {
            return farTarget.interpolate(newPose.x(), newPose.y());
        }
    }
}
