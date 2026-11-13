package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.util.Alliance;
/**
  SOTM = Shoot On The Move (atirar em movimento).
 
  Ideia: enquanto a peça voa (tempo de voo), o robô continua andando, então
  mirar direto no gol erra. Calculamos um PONTO DE MIRA deslocado, oposto ao
  movimento, para a peça cair no gol. Como o tempo de voo depende da distância
  (que muda com a mira), repetimos o cálculo algumas vezes (iterações).
 */

@Config("config SOTM")
public final class   SOTM {
    public static boolean enabled = false ;// liga/desliga a compensação
    public static double timeOfFlight = .25; // tempo de voo fixo (s) - TODO: modelar por distância
    public static int iterations = 10; // nº de refinamentos
    public static double radialGain = 1.7; // ganho da compensação na direção do gol
    public static double tangentialGain = 2.1;// ganho da compensação lateral
    //Tempo de voo em função da distância (por enquanto constante)
    public static double flightTime(double distance) {
        return timeOfFlight;
    }
        /**
     * Calcula o vetor de mira (da torre até o ponto de mira).
     *
     * @param turretPose     pose da torre no campo
     * @param turretVelocity velocidade da torre no campo (já com a rotação do robô)
     * @param goal           pose do gol
     * @param alliance       aliança (não usado no cálculo atual)
     */

    public static Vector2D getAimVector(Pose turretPose, Vector2D turretVelocity, Pose goal, Alliance alliance) {
        // Vetor direto torre -> gol
        Vector2D toGoal = goal.toVector2D().minus(turretPose.toVector2D());
        if (!enabled) return toGoal;// sem SOTM, mira direto

        // Decompõe a velocidade em: radial (aproximando/afastando do gol)
        // e tangencial (andando de lado em relação ao gol)

        double angleToGoal = toGoal.theta();
        double vRad = turretVelocity.dot(Vector2D.unit(angleToGoal));
        double vTan = turretVelocity.dot(Vector2D.unit(angleToGoal + Math.PI / 2));

        Vector2D aim = toGoal;
        double tof = flightTime(toGoal.magnitude());

        // Iteração de ponto fixo: o deslocamento depende do tempo de voo,
        // e o tempo de voo depende da distância até a nova mira.

        for (int i = 0; i < iterations; i++) {
            // Quanto o robô anda durante o voo, no referencial (radial, tangencial),
            // girado de volta para o referencial do campo
            Vector2D shift = Vector2D
                    .cartesian(vRad * tof * radialGain, vTan * tof * tangentialGain)
                    .rotate(angleToGoal);

            aim = toGoal.minus(shift);// mira compensada
            Vector2D virtual = goal.toVector2D().minus(aim);
            tof = flightTime(aim.magnitude());// atualiza o tempo de voo
        }

        return aim;
    }
}
