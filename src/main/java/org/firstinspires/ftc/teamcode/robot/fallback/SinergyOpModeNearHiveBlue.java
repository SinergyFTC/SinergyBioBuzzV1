package org.firstinspires.ftc.teamcode.robot.fallback;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.Alliance;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@TeleOp(name = "TeleOpNearHiveBlue", group = "Fallback")
public class SinergyOpModeNearHiveBlue extends SinergyOpmode{
    private ShootingController shootingController;

    @Override
    public void init() {
        super.init();
        setAlliance(Alliance.BLUE);
        driveTrain.follower.setPose(new Pose(83.75, 133.0, Math.toRadians(270)));
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }

    @Override
    public void start() {
        schedule(
                blocker.blockPolen(),
                blocker.blockNectar()
        );
    }

    @Override
    public void loop() {
        wraperloop(() -> {
            driveTrain.ManualDrive(
                    -gamepad1.left_stick_y,
                    gamepad1.left_trigger - gamepad1.right_trigger,
                    -gamepad1.right_stick_x
            );

            shootingController.prepareShoot(
                    driveTrain.follower.pose(),
                    driveTrain.follower.velocity(),
                    Hives.getCurrentHive(),
                    alliance());
        });

        if (gamepad2.rightBumperWasPressed()) intake.on().schedule();
        if (gamepad2.rightBumperWasReleased()) intake.off().schedule();
        if (gamepad2.rightTriggerWasPressed()) intake.reverse().schedule();
        if (gamepad2.dpadLeftWasPressed()) setAlliance(Alliance.BLUE);
        if (gamepad2.dpadRightWasPressed()) setAlliance(Alliance.RED);

        if (gamepad2.leftBumperWasPressed()) flyWheel.toggle().schedule();
        if (gamepad2.leftTriggerWasPressed()) flyWheelNectar.toggle().schedule();


        if (gamepad2.triangleWasPressed()) blocker.unblockPolen();
        if (gamepad2.circleWasPressed()) blocker.unblockNectar();
    }
}