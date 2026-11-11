package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.TurretAngle;

/**
 * Subsistema TURRET: torre giratória movida por dois servos (espelhados).
 * Trabalha em RADIANOS, com 0 = virada para a frente do robô.
 * Fluxo: o ShootingController define o alvo -> periodic() limita o ângulo
 * (TurretAngle) -> converte para posição de servo -> envia aos dois servos.
 */

 @Config("Turret")
public class Turret {
    public static double centerPosition = 0.667;// posição do servo com a torre centralizada

 // Limites físicos da torre (evitam enrolar fios / bater na estrutura)
    public static double minAngle = Math.toRadians(-185);
    public static double maxAngle = Math.toRadians(185);

    private final Context context;
    private final Servo turretServoR;
    private final Servo turretServoL;

    private double targetAngle = 0.0;// ângulo desejado (rad)
    private double currentAngle = 0.0; // ângulo comandado após limites (rad)
    private static double previousAngle = 0.0;

    public Turret(Context context) {
        this.context = context;
        turretServoR = context.servo("turretServoR", 500, 2500);
        turretServoL = context.servo("turretServoL", 500, 2500);
    }
    public static void localize(double angle) {
        previousAngle = angle;
    }

    public void setStartingAngle(double angle) {
        previousAngle = angle;
        currentAngle = angle;
        targetAngle = angle;
    }

    public void usePreviousStartingAngle() {
        currentAngle = previousAngle;
        targetAngle = previousAngle;
    }
    //Define o ângulo alvo (usado pelo ShootingController todo ciclo).
    public void setTarget(double radians) {
        targetAngle = radians;
    }

    public double getTarget() {
        return targetAngle;
    }

    public double getAngle() {
        return currentAngle;
    }
// Comando: aponta a torre para um ângulo
    public Command setAngle(double radians) {
        return instant(() -> setTarget(radians)).requiring(turretServoR);
    }
    /**
     * Todo ciclo: normaliza o ângulo dentro de [minAngle, maxAngle]
     * (escolhendo o caminho equivalente válido), converte e move os servos.
     * Nota: servo não informa posição real, então currentAngle é o valor COMANDADO.
     */
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
    /**
     * Converte ângulo da torre (rad) em posição de servo (0..1):
     * ângulo * gearRatio = ângulo do servo; divide pelo curso total e soma o centro.
     */
    private double angleToServo(double angle) {
        double servoAngle = angle * (64.0 / 22.0);
        double position = centerPosition + servoAngle / Math.toRadians(1800);
        return Utils.clamp(position, 0.0, 1.0);
    }
}
