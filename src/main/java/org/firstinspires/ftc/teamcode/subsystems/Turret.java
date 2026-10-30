package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.TurretAngle;

 @Config("Turret")
public class Turret {
    public static double servoRangeDegrees = 1800;
    public static double gearRatio = 60.0 / 22.0;
    public static double centerPosition = 0.5;

    public static double minAngle = Math.toRadians(-185);
    public static double maxAngle = Math.toRadians(185);

    private final Context context;
    private final Servo turretServoR;
    private final Servo turretServoL;

    private double targetAngle = 0.0;
    private double currentAngle = 0.0; // ângulo COMANDADO (servo não tem feedback)

    public Turret(Context context) {
        this.context = context;
        turretServoR = context.servo("turretServoR");
        turretServoL = context.servo("turretServoL");
    }

    public void setTarget(double radians) {
        targetAngle = radians;
    }

    public double getTarget() {
        return targetAngle;
    }

    public double getAngle() {
        return currentAngle;
    }

    public Command setAngle(double radians) {
        return instant(() -> setTarget(radians)).requiring(turretServoR);
    }

    public Command center() {
        return setAngle(0.0);
    }

    public Command nudge(double radians) {
        return instant(() -> setTarget(currentAngle + radians)).requiring(turretServoR);
    }

    public Command periodic() {
        return infinite(() -> {
            currentAngle = TurretAngle.normalizeAngle(targetAngle, currentAngle, minAngle, maxAngle);

            double position = angleToServo(currentAngle);
            turretServoR.setPosition(position);
            turretServoL.setPosition(position);

            context.telemetry.addData("Turret/angle (deg)", Math.toDegrees(currentAngle));
            context.telemetry.addData("Turret/servo position", position);
        });
    }

    private double angleToServo(double angle) {
        double servoAngle = angle * gearRatio;
        double position = centerPosition + servoAngle / Math.toRadians(servoRangeDegrees);
        return Utils.clamp(position, 0.0, 1.0);
    }
}