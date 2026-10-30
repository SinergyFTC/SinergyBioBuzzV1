package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.util.Alliance;

@Config("config SOTM")
public final class SOTM {
    public static boolean enabled = true;
    public static double timeOfFlight = .25;
    public static int iterations = 5;
    public static double radialGain = 1.0;
    public static double tangentialGain = 1.0;

    public static double flightTime(double distance) {
        return timeOfFlight;
    }

    public static Vector2D getTurretVelocity(Pose robotPose, Pose turretPose, Velocity robotVelocity) {
        Vector2D offset = turretPose.toVector2D().minus(robotPose.toVector2D());
        Vector2D rotation = offset.perpendicularLeft().times(robotVelocity.omega);
        return robotVelocity.toVector2D().plus(rotation);
    }

    public static Vector2D getAimVector(Pose turretPose, Vector2D turretVelocity, Pose goal, Alliance alliance) {
        Vector2D toGoal = goal.toVector2D().minus(turretPose.toVector2D());
        if (!enabled) return toGoal;

        double angleToGoal = toGoal.theta();
        double vRad = turretVelocity.dot(Vector2D.unit(angleToGoal));
        double vTan = turretVelocity.dot(Vector2D.unit(angleToGoal + Math.PI / 2));

        Vector2D aim = toGoal;
        double tof = flightTime(toGoal.magnitude());

        for (int i = 0; i < iterations; i++) {
            Vector2D shift = Vector2D
                    .cartesian(vRad * tof * radialGain, vTan * tof * tangentialGain)
                    .rotate(angleToGoal);

            aim = toGoal.minus(shift);
            tof = flightTime(aim.magnitude());
        }

        return aim;
    }
}