package org.firstinspires.ftc.teamcode.robot.tuners;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@TeleOp(name = "DriveTrain Tuner", group = "Tuners")
public class DriveTrainTuner extends SinergyOpmode {
    @Override
    public void loop() {
        wraperloop(() -> {
            driveTrain.ManualDrive(
                    -gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    alliance()
            );
        });
    }
}
