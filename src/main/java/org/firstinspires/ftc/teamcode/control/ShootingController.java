package org.firstinspires.ftc.teamcode.control;

import com.pedropathing.math.*;

import org.firstinspires.ftc.teamcode.subsystems.*;

import org.firstinspires.ftc.teamcode.math.SOTM;
import org.firstinspires.ftc.teamcode.math.TurretKinematics;
import org.firstinspires.ftc.teamcode.util.Alliance;
import org.firstinspires.ftc.teamcode.util.Context;

public class ShootingController {
    private final Context context;
    private final Turret turret;
    private final FlyWheel flyWheel;
    private final Hood hood;

    public ShootingController(Turret turret, FlyWheel flyWheel, Hood hood, Context context) {
        this.turret = turret;
        this.flyWheel = flyWheel;
        this.hood = hood;
        this.context = context;
    }
    public void prepareShoot(Pose robotPose, Velocity robotVelocity , Pose goal, Alliance alliance) {
        Pose turretPose = TurretKinematics.getOffset(robotPose);
        Vector2D turretVelocity = TurretKinematics.getTurretVelocity(robotPose, turretPose, robotVelocity);

        Vector2D aim = SOTM.getAimVector(turretPose, turretVelocity, goal, alliance);
        Vector2D virtualPosition = goal.toVector2D().minus(aim);
    }

}
