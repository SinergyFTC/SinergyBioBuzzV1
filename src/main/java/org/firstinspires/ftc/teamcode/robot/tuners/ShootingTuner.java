package org.firstinspires.ftc.teamcode.robot.tuners;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@TeleOp(name = "Shooting Tuner", group = "Tuners")
public class ShootingTuner extends SinergyOpmode {
    public static double shotTimer = 0.0;

    public Command shoot() {
        return blocker.unblockPolen()
                .then(waitMs(shotTimer))
                .then(blocker.blockPolen());
    }

    @Override
    public void init() {
        super.init();
        schedule(
                blocker.blockPolen(),
                blocker.blockNectar()
        );

    }
    @Override
    public void loop() {
        wraperloop(() -> {
            if (gamepad1.rightBumperWasPressed()) shoot().schedule();

            if(gamepad1.dpadDownWasPressed()) blocker.unblockPolen();
            if(gamepad1.dpadUpWasPressed()) blocker.unblockNectar();
        });
    }
}
