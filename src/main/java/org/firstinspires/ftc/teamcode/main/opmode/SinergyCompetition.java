package org.firstinspires.ftc.teamcode.main.opmode;

import static com.pedropathing.ivy.Scheduler.schedule;

import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;
import org.firstinspires.ftc.teamcode.subsystems.*;

public class SinergyCompetition extends SinergyOpmode {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        blocker.block().schedule();
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

        });
    }
}
