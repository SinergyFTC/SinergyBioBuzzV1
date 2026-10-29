package org.firstinspires.ftc.teamcode.robot.tuners;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@TeleOp(name = "Tuning TeleOp", group = "Tuners")
@Config("TeleOp Tuner")
public class TuningTeleOp extends SinergyOpmode {
    public static double flywheelTarget = 0;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        wraperloop(() -> {
            driveTrain.ManualDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, -gamepad1.right_stick_x, alliance());

            if(gamepad1.crossWasPressed()) flyWheel.toggle().schedule();
            flyWheel.setTarget(flywheelTarget);

            if (gamepad1.rightBumperWasPressed()) intake.on().schedule();
            if (gamepad1.rightBumperWasReleased()) intake.off().schedule();

            if (gamepad1.leftBumperWasPressed()) intake.reverse().schedule();
            if (gamepad1.leftBumperWasReleased()) intake.off().schedule();

            if(gamepad1.dpadDownWasPressed()) blocker.unblock().schedule();
            if(gamepad1.dpadUpWasPressed()) blocker.block().schedule();
        });
    }
}