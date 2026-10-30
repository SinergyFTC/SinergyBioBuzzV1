package org.firstinspires.ftc.teamcode.subsystems;


import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Context;

@Config("Hood Subsystem")
public class Hood {
    public static double minPosition = 0.0;
    public static double maxPosition = 1.0;
    public static double servoIncrement = 0.01;
    public static double shotScalar = 0;
    public static double maximumDrop = 0;
    public static double dropDelay = 0;

    private final Context context;
    private final Servo hoodServo;
    private final ElapsedTime shotTimer = new ElapsedTime();

    private boolean enabled = true;
    private boolean shooting = false;
    private double target = 0;
    private double output = 0;
    public Hood(Context context) {
        this.context = context;
        hoodServo = context.servo("hood");
    }

    public void setPosition(double position) {
        target = Utils.clamp(position, minPosition, maxPosition);
    }

    public double getPosition() {
        return target;
    }

    public Command increment() {
        return instant(() -> setPosition(target + servoIncrement)).requiring(hoodServo);
    }

    public Command decrement() {
        return instant(() -> setPosition(target - servoIncrement)).requiring(hoodServo);
    }

    public Command startShot() {
        return instant(() -> {
            shooting = true;
            shotTimer.reset();
        });
    }

    public Command endShot() {
        return instant(() -> shooting = false);
    }

    public Command enable() {
        return instant(() -> enabled = true);
    }

    public Command disable() {
        return instant(() -> enabled = false);
    }

    public Command periodic() {
        return infinite(() -> {
            output = Utils.clamp(target - shotDrop(), minPosition, maxPosition);
            if (enabled) hoodServo.setPosition(output);

            context.telemetry.addData("Hood/target", target);
            context.telemetry.addData("Hood/output", output);
        });
    }

    private double shotDrop() {
        if (!shooting) return 0;
        double time = Math.max(0, shotTimer.seconds() - dropDelay);
        return Math.min(time * shotScalar, maximumDrop);
    }
}