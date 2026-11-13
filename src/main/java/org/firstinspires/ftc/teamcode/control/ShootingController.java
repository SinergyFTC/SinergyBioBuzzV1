package org.firstinspires.ftc.teamcode.control;

import com.pedropathing.math.*;

import org.firstinspires.ftc.teamcode.math.Interpolator;
import org.firstinspires.ftc.teamcode.subsystems.*;

import org.firstinspires.ftc.teamcode.math.SOTM;
import org.firstinspires.ftc.teamcode.math.TurretKinematics;
import org.firstinspires.ftc.teamcode.util.Alliance;
import org.firstinspires.ftc.teamcode.util.Context;
import org.firstinspires.ftc.teamcode.util.TurretAngle;
/**
 * Controlador de TIRO: junta os subsistemas (Turret, FlyWheel, Hood) com a
 * matemática (TurretKinematics + SOTM) - é a "cola" da composição por POO.
 * Hoje ele só aponta a torre; flywheel e hood ainda não são ajustados aqui.
 */
public class  ShootingController {
    private final Context context;
    private final Turret turret;
    private final FlyWheel flyWheel;
    private final Hood hood;

    public ShootingController(Turret turret, FlyWheel flyWheel, Hood hood, Context context) {
        this.turret = turret;
        this.flyWheel = flyWheel;
        this.hood = hood;
        this.context = context;
    }

      /**
     * Prepara o tiro (chamar todo ciclo do loop).
     * Passos:
     * 1) pose da torre no campo (considera o offset)
     * 2) velocidade da torre (considera a rotação do robô)
     * 3) vetor de mira compensado pelo movimento (SOTM)
     * 4) converte para ângulo relativo ao robô e manda a torre girar
     */
    
    public void prepareShoot(Pose robotPose, Velocity robotVelocity , Pose goal, Alliance alliance) {
        Pose turretPose = TurretKinematics.getOffset(robotPose);
        Vector2D turretVelocity = TurretKinematics.getTurretVelocity(robotPose, turretPose, robotVelocity);

        Vector2D aim = SOTM.getAimVector(turretPose, turretVelocity, goal, alliance);
        // Posição virtual do alvo (ainda não usada; servirá para calcular distância -> flywheel/hood)
        Vector2D virtualPosition = goal.toVector2D().minus(aim);

        double angle = TurretAngle.toTarget(aim, robotPose.heading(), robotVelocity.omega);

        flyWheel.setTarget(Interpolator.getFlywheelVelocity(virtualPosition, alliance));
        turret.setTarget(angle);
}

}
