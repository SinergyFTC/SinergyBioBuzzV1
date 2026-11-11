package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Vector2D;

import org.firstinspires.ftc.teamcode.util.Alliance;

import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

public final class Interpolator {

    private static final Interpolation2D flywheelVelocity = new BilinearInterpolation(
            new double[]{22.0, 70.75},
            new double[]{10.7, 33.0},
            new double[][]{
                    {895, 860},
                    {880, 900}
            });

    private static double interpolate(Interpolation2D interpolation, Vector2D pose, Alliance alliance) {
        // 1) aliança: azul espelha o x
        final Vector2D allianceSide;
        if (alliance == Alliance.BLUE) {
            allianceSide = MirrorPosition.mirror(pose.toPose()).toVector2D();
        } else {
            allianceSide = pose;
        }

        // 2) metade do campo: em cima espelha o y
        final Vector2D newPose;
        if (allianceSide.y() > 72) {
            newPose = MirrorPosition.mirrorY(allianceSide.toPose()).toVector2D();
        } else {
            newPose = allianceSide;
        }

        return interpolation.interpolate(newPose.x(), newPose.y());
    }

    public static double getFlywheelVelocity(Vector2D pose, Alliance alliance) {
        return interpolate(flywheelVelocity, pose, alliance);
    }
}
