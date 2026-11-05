package org.firstinspires.ftc.teamcode.robot.opmode;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

import java.util.jar.Attributes;

@TeleOp(name = "SinergyTeleOp", group = "Competition")
public class SinergyCompetition extends SinergyOpmode {
    private ShootingController shootingController;

    @Override
    public void init() {
        super.init();
        driveTrain.PreviousStartingPos();
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }

    @Override
    public void start() {
        schedule(
                blocker.blockPolen(),
                blocker.unblockNectar()
        );
        flyWheel.on().schedule();
    }

    @Override
    public void loop() {
        wraperloop(() -> {
            driveTrain.ManualDrive(
                    -gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    alliance()
            );
            shootingController.prepareShoot(
                    driveTrain.follower.pose(),
                    driveTrain.follower.velocity(),
                    Hives.getCurrentHive(),
                    alliance());
        });
    }
}
