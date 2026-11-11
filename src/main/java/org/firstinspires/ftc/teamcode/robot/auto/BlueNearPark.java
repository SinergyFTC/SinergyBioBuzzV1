package org.firstinspires.ftc.teamcode.robot.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

public class BlueNearPark extends SinergyOpmode {
    private AutoPaths paths;
    private ShootingController shootingController;

    @Override
    public void init() {
        super.init();
        paths = new AutoPaths();
        turret.setStartingAngle(-90);
        // driveTrain.localize();
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }
    @Override
    public void loop() {
        shootingController.prepareShoot(
                driveTrain.follower.pose(),
                driveTrain.follower.velocity(),
                Hives.getCurrentHive(),
                alliance());
    }

    public class AutoPaths {

    }
}
