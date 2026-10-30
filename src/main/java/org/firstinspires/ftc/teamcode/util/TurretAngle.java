package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Vector2D;
import com.pedropathing.utils.Angle;
import com.pedropathing.utils.Utils;

public class TurretAngle {
    public static double angularVelocityScaling = 0.0;

    public static double toTarget(Vector2D aimToGoal, double heading, double omega) {
        double toAngle = aimToGoal.toBodyFrame(heading).theta() - angularVelocityScaling * omega;
        return Angle.normalizeSigned(toAngle);
    }

    public static double normalizeAngle(double desired, double min, double max) {
        return Utils.clamp(Angle.normalizeSigned(desired), min, max);
    }

}
