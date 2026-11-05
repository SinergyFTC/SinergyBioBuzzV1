package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.Alliance;
import org.firstinspires.ftc.teamcode.util.Context;

/**
 * subsistema DRIVETRAIN: encapsula o Follower do Pedro Pathing v3
 * (localização por odometria + controle do chassi mecanum)
 */

public class DriveTrain {
    public Follower follower; // objeto principal do Pedro Pathing
    public static Pose previousPose = Pose.zero();// ultima pose conhecida (sobrevive entre OpModes)
    public Context context;

    public DriveTrain(Context context) {
        this.context = context;
        // constants.create monta Follower = Pinpoint + Mecanum + Foresight
        follower = Constants.create(context.hardwareMap);
    }
    //salva uma pose (ex.: no fim do autônomo) para o TeleOp continuar dali
    public static void localize(Pose pose) {
        previousPose = pose;
    }
    //Define a pose inicial do robô como a última pose salva
    public void PreviousStartingPos() {
        follower.setPose(previousPose);
    }
    /**
     * Direção manual FIELD-CENTRIC (o "frente" do joystick é sempre o mesmo
     * lado do campo, não importa para onde o robô aponta)
     * Na aliança azul soma PI ao ângulo, invertendo a referência
     * driveOrHold: se o joystick estiver parado, o robô segura a posição
     */
    public void ManualDrive(double forward, double lateral, double turn, Alliance alliance) {
        double offset = (alliance == Alliance.BLUE) ? Math.PI : 0;
        DrivePowers powers = ManualDrive.fieldCentric(
                forward,
                lateral,
                turn,
                follower.pose().heading(),
                offset
        );
        ManualDrive.driveOrHold(follower, powers);
    }

    public void update() {
        follower.update();
    }


    public Command periodic() {
        return infinite(() -> {
            previousPose = follower.pose();

            context.telemetry.addData("Current X", follower.pose().x());
            context.telemetry.addData("Current Y", follower.pose().y());
            context.telemetry.addData("Current Heading", Math.toDegrees(follower.pose().heading()));
        });
    }
}
