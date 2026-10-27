package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Context;

public class Intake {
    public static double reversepower = -1.0;
    public static double offpower = 0.0;
    public static double onpower = 1.0;
    private final Context context;
    private final DcMotorEx intakeMotor;
    private Mode mode = Mode.OFF;

    enum Mode {
        ON,
        OFF,
        REVERSE
    }

    public Intake(Context context) {
        this.context = context;
        intakeMotor = context.motor("Intake");
    }

    public Command on() {
        return instant(() -> mode = Mode.ON).requiring(intakeMotor);
    }

    public Command off() {
        return instant(() -> mode = Mode.OFF).requiring(intakeMotor);
    }

    public Command reverse() {
        return instant(() -> mode = Mode.REVERSE).requiring(intakeMotor);
    }

    public Command toggle() {
        return conditional(() -> mode == Mode.OFF, on(), reverse());
    }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case ON:
                    intakeMotor.setPower(onpower);
                    break;
                case OFF:
                    intakeMotor.setPower(offpower);
                    break;
                case REVERSE:
                    intakeMotor.setPower(reversepower);
                    break;
            }
        });
    }
}
