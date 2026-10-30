package org.firstinspires.ftc.teamcode.robot.opmode;

import static com.pedropathing.ivy.Scheduler.schedule;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

public class SinergyCompetition extends SinergyOpmode {
    @Override
    public void init() {
        super.init();
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
        });
    }
}
