package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

/**
 * Subsistema FLYWHEEL: dois motores que lançam a peça.
 * Controle de velocidade = P + feedforward:
 *   potência = kP*(alvo - velocidade) + kV*alvo + kS*sinal(alvo)
 *   kP corrige o erro, kV compensa a velocidade desejada, kS vence o atrito estático.
 */    

public class FlyWheel {
    private final Context context;
    private final DcMotorEx FlyWheelMotorR;
    private final DcMotorEx FlyWheelMotorL;
    private double offpower = 0;// potência quando desligado
    private double target = 0;// velocidade alvo (ticks/s)
    private Mode mode = Mode.OFF;

    public static int toleranceVel = 25;// erro máximo aceito para "chegou no alvo"
    // Ganhos do controle - TODO: calibrar (estão zerados)
    public static double kP = 0;
    public static double kV = 0;
    public static double kS = 0;

    public FlyWheel(Context context) {
        this.context = context;
        FlyWheelMotorR = context.motor("FlywheelRight");
        FlyWheelMotorL = context.motor("FlywheelLeft");
    }
    // Liga o controle de velocidade.
    public Command on() {
        return instant(() -> mode = Mode.ON).requiring(FlyWheelMotorR, FlyWheelMotorL);
    }

    public Command off() {
        return instant(() -> mode = Mode.OFF).requiring(FlyWheelMotorR, FlyWheelMotorL);
    }
    //Alterna entre ligado e desligado.
    public Command toggle() {
        return conditional(() -> mode == Mode.OFF, on(), off());
    }
    //Define a velocidade alvo (usado pelo ShootingController)
    public void setTarget(double target) {
        this.target = target;
    }
    /**
     * ATENÇÃO: o parâmetro "target" sombreia o campo, então este método
     * devolve o valor recebido e não o alvo salvo. O ideal é remover o parâmetro.
     */
    public double getTarget(double target) {
        return target;
    }
    //Velocidade atual medida no encoder do motor direito
    public double getVelocity() {
        return FlyWheelMotorR.getVelocity();
    }
    //Aplica a mesma potência aos dois motores
    private void setPower(double power) {
        FlyWheelMotorR.setPower(power);
        FlyWheelMotorL.setPower(power);
    }
//true quando a velocidade está dentro da tolerância do alvo (pronto para atirar)
    public boolean atTarget() {
        return Math.abs(target - getVelocity()) <= toleranceVel;
    }
    //Todo ciclo: calcula a potência conforme o modo e envia telemetria
    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case OFF:
                    setPower(offpower);
                    return;// sai antes da telemetria
                case ON:
                    // P + feedforward (veja o cabeçalho da classe)
                    setPower(kP * (target - getVelocity()) + kV * target + kS * Math.signum(target));
                    break;
            }
            context.telemetry.addData("Flywheel/velocity", getVelocity());
            context.telemetry.addData("Flywheel/target", target);
            context.telemetry.addData("Flywheel/power", FlyWheelMotorL.getPower());
        });
        }
    }
//Estados do flywheel. (Fica fora da classe no arquivo original; funciona, pois é do mesmo pacote.)
    enum Mode {
        OFF,
        ON
}
