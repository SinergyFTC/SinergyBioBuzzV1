package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

public class FlyWheel {
    private final Context context;
    private final DcMotorEx FlyWheelMotorR;
    private final DcMotorEx FlyWheelMotorL;
    private double offpower = 0;
    private double target = 0;
    private Mode mode = Mode.OFF;

    public static int toleranceVel = 25;
    public static double kP = 0;
    public static double kV = 0;
    public static double kS = 0;

    public FlyWheel(Context context) {
        this.context = context;
        FlyWheelMotorR = context.motor("FlywheelRight");
        FlyWheelMotorL = context.motor("FlywheelLeft");
    }

    public Command on() {
        return instant(() -> mode = Mode.ON).requiring(FlyWheelMotorR, FlyWheelMotorL);
    }

    public Command off() {
        return instant(() -> mode = Mode.OFF).requiring(FlyWheelMotorR, FlyWheelMotorL);
    }

    public Command toggle() {
        return conditional(() -> mode == Mode.OFF, on(), off());
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getTarget(double target) {
        return target;
    }

    public double getVelocity() {
        return FlyWheelMotorR.getVelocity();
    }

    private void setPower(double power) {
        FlyWheelMotorR.setPower(power);
        FlyWheelMotorL.setPower(power);
    }

    public boolean atTarget() {
        return Math.abs(target - getVelocity()) <= toleranceVel;
    }

    public Command periodic() {
        return infinite(() -> {
            switch(mode) {
                case OFF:
                    setPower(offpower);
                    return;
                case ON:
                    setPower(kP * (target - getVelocity()) + kV * target + kS * Math.signum(target));
                    break;
            }
                    context.telemetry.addData("Flywheel/velocity", getVelocity());
                    context.telemetry.addData("Flywheel/target", target);
                    context.telemetry.addData("Flywheel/power", FlyWheelMotor.getPower());
            }});
    }

    enum Mode {
        OFF,
        ON
    }
}
