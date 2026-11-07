package org.firstinspires.ftc.teamcode.robot.tuners;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Hood;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;
@TeleOp(name = "Interpolation Tuner", group = "Tuners")
@Config("Interpolation Tuner")
public class InterpolationTuner extends SinergyOpmode {
    public static double x = 0.0;
    public static double y = 0.0;
    public static double HoodPosition = 0.0;
    public static double flyWheelVelocity = 0.0;

        @Override
        public void init() {
            super.init();
        }

        @Override
        public void loop() {
            wraperloop(() -> {
            hood.setPosition(HoodPosition);
            flyWheel.setTarget(flyWheelVelocity);

                if (gamepad1.rightTriggerWasPressed()) {
                    schedule(
                        blocker.unblockPolen(),
                            hood.startShot()
                    );
                }

                if (gamepad1.rightTriggerWasReleased()) {
                    schedule(
                            blocker.blockNectar(),
                                hood.endShot()
                    );
                }

                if (gamepad1.rightBumperWasPressed()) intake.toggle().schedule();
                if (gamepad1.leftBumperWasPressed()) intake.shortReversed().schedule();

                if (gamepad1.triangleWasPressed()) flyWheel.toggle().schedule();
            });
        }
}
