package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Vector2D;
import com.pedropathing.utils.Angle;
import com.pedropathing.utils.Utils;
@Config("Setting AngularVelScaling")
public class TurretAngle {
    public static double angularVelocityScaling = 0.0;

    public static double toTarget(Vector2D aimToGoal, double heading, double omega) {
        double toAngle = aimToGoal.toBodyFrame(heading).theta() - angularVelocityScaling * omega;
        return Angle.normalizeSigned(toAngle);
    }

    public static double normalizeAngle(double desired, double current, double min, double max) {
        double nearest = current + Angle.normalizeSigned(desired - current);
        if (nearest >= min && nearest <= max) return nearest;
        return Utils.clamp(Angle.normalizeSigned(desired), min, max);
    }

}
