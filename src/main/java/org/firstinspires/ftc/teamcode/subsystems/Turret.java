package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.TurretAngle;
import org.firstinspires.ftc.teamcode.util.Context;

/**
 * Torreta com 2 servos goBILDA multiturn (ambos FORWARD, mesma posição).
 * Só hardware, conversão ângulo -> posição do servo e limites.
 * A mira é calculada em SOTM e TurretAngle e coordenada pelo ShootingController.
 * Ângulos em radianos: 0 = frente do robô, anti-horário positivo.
 */
@Config("Turret")
public class Turret {
    // conversão: torreta -> servo -> posição (0 a 1)
    public static double servoRangeDegrees = 1800;       // faixa de pulso 500-2500 µs x 0,90°/µs
    public static double gearRatio = 60.0 / 22.0;        // dentes da torreta / dentes do servo (negativo se girar ao contrário)
    public static double centerPosition = 0.5;           // TODO: posição com a torreta para a frente

    // limites pelo cabo (alcance máximo do servo: ±330°)
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

    // ---------------- controle direto (ShootingController, a cada loop) ----------------

    public void setTarget(double radians) {
        targetAngle = radians;
    }

    // ---------------- comandos ----------------

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
        return infinite(this::update);
    }

    // ---------------- estado ----------------

    public double getAngle() {
        return currentAngle;
    }

    public double getTarget() {
        return targetAngle;
    }

    // ---------------- loop ----------------

    private void update() {
        currentAngle = TurretAngle.normalizeAngle(targetAngle, currentAngle, minAngle, maxAngle);

        double position = angleToServo(currentAngle);
        turretServoR.setPosition(position);
        turretServoL.setPosition(position);

        context.telemetry.addData("Turret/angle (deg)", Math.toDegrees(currentAngle));
        context.telemetry.addData("Turret/servo position", position);
    }

    // ---------------- conversão ----------------

    private double angleToServo(double angle) {
        double servoAngle = angle * gearRatio;
        double position = centerPosition + servoAngle / Math.toRadians(servoRangeDegrees);
        return Utils.clamp(position, 0.0, 1.0);
    }
}