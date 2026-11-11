package org.firstinspires.ftc.teamcode.robot.auto;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

public class RedNearPark extends SinergyOpmode {
    private ShootingController shootingController;
    private AutoPaths paths;


    @Override
    public void init() {
        super.init();
        paths = new AutoPaths();
    }

    @Override
    public void loop() {

    }

    public class AutoPaths {

    }
}