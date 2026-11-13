package org.firstinspires.ftc.teamcode.robot.tuners;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;
@Config("Angular Velocity")
@TeleOp(name = "Angular Velocity Tuner", group = "Tuners")
public class AngularVelocityTuner extends SinergyOpmode {
    private ShootingController shootingController;
    public static double heading = 0.0;

    public void init() {
        super.init();
        driveTrain.follower.setPose(new Pose(58.0, 8.0, Math.toRadians(90)));
        turret.setStartingAngle(0);
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }
    public void loop() {
        wraperloop(() -> {
            driveTrain.ManualDrive(
                    -gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x
            );

            shootingController.prepareShoot(
                    driveTrain.follower.pose(),
                    driveTrain.follower.velocity(),
                    Hives.getCurrentHive(),
                    alliance()
            );

            if (gamepad1.rightBumperWasPressed()) flyWheel.toggle().schedule();

            if(gamepad1.rightTriggerWasPressed()) intake.on().schedule();
            if(gamepad1.rightTriggerWasReleased()) intake.off().schedule();

         });
    }
}
