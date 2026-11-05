package org.firstinspires.ftc.teamcode.subsystems;


import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.pedropathing.utils.Utils;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Context;
/**
 * Subsistema HOOD: servo que ajusta o ângulo de saída da peça.
 * Também compensa a "queda" do hood durante o tiro: depois de dropDelay
 * segundos, o hood desce progressivamente (shotScalar por segundo) até maximumDrop.
 */

@Config("Hood Subsystem")
public class Hood {
    public static double minPosition = 0.0;// limite mínimo do servo
    public static double maxPosition = 1.0; // limite máximo do servo
    public static double servoIncrement = 0.01;// passo dos botões increment/decrement
    public static double shotScalar = 0;// velocidade da queda durante o tiro
    public static double maximumDrop = 0;// queda máxima permitida
    public static double dropDelay = 0;// atraso (s) antes de começar a cair

    private final Context context;
    private final Servo hoodServo;
    private final ElapsedTime shotTimer = new ElapsedTime();// tempo desde o início do tiro

    private boolean enabled = true;// se false, o servo não recebe posição
    private boolean shooting = false;// true durante um tiro
    private double target = 0;// posição desejada
    private double output = 0;// posição enviada (alvo - queda)
    public Hood(Context context) {
        this.context = context;
        hoodServo = context.servo("hood");
    }
    //Habilita o envio de posição ao servo
    public Command enable() {
        return instant(() -> enabled = true);
    }
    //Desabilita o envio de posição ao servo
    public Command disable() {
        return instant(() -> enabled = false);
    }
    //Define o alvo, limitado entre min e max
    public void setPosition(double position) {
        target = Utils.clamp(position, minPosition, maxPosition);
    }

    public double getPosition() {
        return target;
    }
    //Sobe o hood um passo (ajuste fino pelo operador)
    public Command increment() {
        return instant(() -> setPosition(target + servoIncrement)).requiring(hoodServo);
    }
//Desce o hood um passo
    public Command decrement() {
        return instant(() -> setPosition(target - servoIncrement)).requiring(hoodServo);
    }
// /Marca o início do tiro e zera o cronômetro
    public Command startShot() {
        return instant(() -> {
            shooting = true;
            shotTimer.reset();
        });
    }
    //Marca o fim do tiro
    public Command endShot() {
        return instant(() -> shooting = false);
    }
    //Todo ciclo: calcula alvo - queda, limita e envia ao servo
    public Command periodic() {
        return infinite(() -> {
            output = Utils.clamp(target - shotDrop(), minPosition, maxPosition);
            if (enabled) hoodServo.setPosition(output);

            context.telemetry.addData("Hood/target", target);
            context.telemetry.addData("Hood/output", output);
        });
    }
    //Quanto o hood deve "cair" agora: 0 fora do tiro; depois cresce linearmente até maximumDrop
    private double shotDrop() {
        if (!shooting) return 0;
        double time = Math.max(0, shotTimer.seconds() - dropDelay);
        return Math.min(time * shotScalar, maximumDrop);
    }
}
