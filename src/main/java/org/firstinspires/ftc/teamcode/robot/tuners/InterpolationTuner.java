package org.firstinspires.ftc.teamcode.robot.tuners;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;
@TeleOp(name = "Interpolation Tuner", group = "Tuners")
@Config("Interpolation Tuner")
public class InterpolationTuner extends SinergyOpmode {
    public static double x = 0.0;
    public static double y = 0.0;
    public static double heading = 0.0;
    public static double HoodPosition = 0.0;
    public static double flyWheelVelocity = 0.0;
    public static double targetdegrees = 0.0;
    private ShootingController shootingController;

        @Override
        public void init() {
            super.init();
            flyWheel.on().schedule();
            hood.enable();
            driveTrain.follower.setPose(new Pose(22.0, 33.0, Math.toRadians(90)));

            blocker.blockPolen().schedule();
            blocker.blockNectar().schedule();
            shootingController = new ShootingController(turret, flyWheel, hood, context);

        }

        @Override
        public void loop() {
            wraperloop(() -> {
                driveTrain.ManualDrive(
                        -gamepad1.left_stick_y,
                        gamepad1.left_trigger - gamepad1.right_trigger,
                        gamepad1.right_stick_x
                );

                shootingController.prepareShoot(
                        driveTrain.follower.pose(),
                        driveTrain.follower.velocity(),
                        Hives.getCurrentHive(),
                        alliance()
                );

                hood.setPosition(HoodPosition);
                flyWheel.setTarget(flyWheelVelocity);

                if (gamepad1.rightTriggerWasPressed()) {
                    hood.startShot();
                    schedule(
                            blocker.unblockPolen()
                    );
                }

                if (gamepad1.rightTriggerWasReleased()) {
                    hood.endShot();
                    schedule(
                            blocker.blockPolen()
                    );
                }

                if (gamepad1.rightBumperWasPressed()) intake.toggle().schedule();
                if (gamepad1.leftBumperWasPressed()) intake.shortReversed().schedule();

                if (gamepad1.triangleWasPressed()) flyWheel.toggle().schedule();
                if (gamepad1.dpadDownWasPressed()) hood.decrement();
                if (gamepad1.dpadUpWasPressed()) hood.increment();
            });
        }
}
