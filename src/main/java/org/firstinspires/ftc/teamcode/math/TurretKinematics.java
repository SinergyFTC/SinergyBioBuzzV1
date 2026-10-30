package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.math.Velocity;

public class TurretKinematics {
    public static double TurretOffsetInchesX = -5;
    public static double TurretOffsetInchesY = -3;

    public static Pose getOffset(Pose pose) {
        return pose.compose(new Pose(TurretOffsetInchesX, TurretOffsetInchesY, 0));
    }
    public static Vector2D getTurretVelocity(Pose robotPose, Pose turretPose, Velocity velocity){
        Vector2D robot = turretPose.toVector2D().minus(robotPose.toVector2D());
        return robotPose.toVector2D().plus(robot.perpendicularLeft()).times(velocity.omega);
    }
}
