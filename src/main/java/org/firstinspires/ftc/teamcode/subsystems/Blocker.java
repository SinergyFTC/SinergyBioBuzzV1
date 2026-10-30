package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

import java.util.function.DoubleSupplier;

@Config("Blocker Subsystem")
public final class Blocker {
    public static double blockPositionPolen = 0.0;
    public static double unblockPositionPolen = 0.0;
    public static double blockPositionNectar = 0.0;
    public static double unblockPositionNectar = 0.0;

    private final Context context;
    private final Servo blockerPolen;
    private final Servo blockerNectar;

    public Blocker(Context context) {
        this.context = context;
        blockerPolen = context.servo("blockerPolen");
        blockerNectar = context.servo("blockerNectar");
    }

    private Command set(Servo servo, DoubleSupplier position) {
        return instant(() -> {
            servo.setPosition(position.getAsDouble());
        }).requiring(servo);
    }
    public Command blockPolen() {
        return set(blockerPolen,() -> blockPositionPolen);
    }

    public Command unblockPolen() {
        return set(blockerPolen, () -> unblockPositionPolen);
    }

    public Command blockNectar() {
        return set(blockerNectar, () -> blockPositionNectar);
    }

    public Command unblockNectar() {
        return set(blockerNectar, () -> unblockPositionNectar);
    }


    public Command periodic() {
        return infinite(() -> {
            context.telemetry.addData("Blocker/nectar", blockerNectar.getPosition());
            context.telemetry.addData("Blocker/Polen", blockerPolen.getPosition());
        });
    }
}