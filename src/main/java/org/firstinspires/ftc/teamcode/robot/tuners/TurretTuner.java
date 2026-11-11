package org.firstinspires.ftc.teamcode.robot.tuners;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@Config("TurretTuner")
@TeleOp(name = "Turret Tuner", group = "Tuners")
public class TurretTuner extends SinergyOpmode {
    public static double targetdegrees = 0;

    public void init() {
        super.init();
        turret.setStartingAngle(90);
    }
    public void loop() {
        wraperloop(() -> {
            turret.setTarget(Math.toRadians(targetdegrees));
        });
    }
}
