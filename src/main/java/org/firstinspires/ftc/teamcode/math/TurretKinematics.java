package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.math.Velocity;
/**
  Cinemática da torre: a torre NÃO está no centro do robô, então sua posição
  e velocidade no campo diferem das do robô (principalmente quando ele gira).
 */
public class TurretKinematics {
    // Posição da torre em relação ao centro do robô (polegadas, referencial do robô)
    public static double TurretOffsetInchesX = -5;
    public static double TurretOffsetInchesY = -3;

        /**
      Pose da torre no CAMPO. compose() gira o offset pelo heading do robô
      e soma à posição dele.
     */
    
    public static Pose getOffset(Pose pose) {
        return pose.compose(new Pose(TurretOffsetInchesX, TurretOffsetInchesY, 0));
    }
        /**
      Velocidade da torre no campo.
      Teoria: v_torre = v_robô + omega * perpendicular(r), onde r é o vetor do
      centro do robô até a torre.
      ATENÇÃO: o código atual soma a POSIÇÃO do robô (robotPose) ao vetor
      perpendicular e multiplica tudo por omega; o correto seria usar a
      velocidade linear do robô (velocity.toVector2D()) e multiplicar só o
      perpendicular por omega:
        velocity.toVector2D().plus(robot.perpendicularLeft().times(velocity.omega))
     */
    public static Vector2D getTurretVelocity(Pose robotPose, Pose turretPose, Velocity velocity){
        Vector2D robot = turretPose.toVector2D().minus(robotPose.toVector2D());
        return robotPose.toVector2D().plus(robot.perpendicularLeft()).times(velocity.omega);
    }
}
