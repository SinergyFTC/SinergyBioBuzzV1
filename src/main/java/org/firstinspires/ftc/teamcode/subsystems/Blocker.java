package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

@Config("Blocker Subsystem")
public final class Blocker {
    public static double blockPosition = 0.0;
    public static double unblockPosition = 0.0;

    private final Context context;
    private final Servo blocker;

    public Blocker(Context context) {
        this.context = context;
        blocker = context.servo("blocker");
    }

    public Command block() {
        return instant(() -> {
            blocker.setPosition(blockPosition);
        }).requiring(blocker);
    }

    public Command unblock() {
        return instant(() -> {
            blocker.setPosition(unblockPosition);
        }).requiring(blocker);
    }

    public Command periodic() {
        return infinite(() -> {
            context.telemetry.addData("Blocker/nectar", blocker.getPosition());
        });
    }
}