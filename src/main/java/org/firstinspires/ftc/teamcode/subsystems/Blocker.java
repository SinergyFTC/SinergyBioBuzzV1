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
    private final Servo blockerPolen;
    private final Servo blockerNectar;

    public Blocker(Context context) {
        this.context = context;
        blockerPolen = context.servo("blockerPolen");
        blockerNectar = context.servo("blockerNectar");
    }

    public Command block() {
        return instant(() -> {
            blockerPolen.setPosition(blockPosition);
            blockerNectar.setPosition(blockPosition);
        }).requiring(blockerPolen, blockerNectar);
    }

    public Command unblock() {
        return instant(() -> {
            blockerPolen.setPosition(unblockPosition);
            blockerNectar.setPosition(unblockPosition);
        }).requiring(blockerPolen, blockerNectar);
    }

    public Command periodic() {
        return infinite(() -> {
            context.telemetry.addData("Blocker/polen", blockerPolen.getPosition());
            context.telemetry.addData("Blocker/nectar", blockerNectar.getPosition());
        });
    }
}