package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

import java.util.function.DoubleSupplier;

/**
  subsistema BLOCKER: dois servos que seguram/liberam as peças (Pólen e Néctar)
  antes de elas chegarem ao flywheel
 
  as posições são public static para serem ajustadas ao vivo pelo Dashboard
  estão em 0.0 porque ainda precisam ser calibradas no robô
 */

@Config("Blocker Subsystem")
public final class Blocker {
    // Posições dos servos (0.0 a 1.0) - TODO: calibrar
    public static double blockPositionPolen = 1;
    public static double unblockPositionPolen = 0.4;
    public static double blockPositionNectar = 0.5;
    public static double unblockPositionNectar = 0.2;

    private final Context context;
    private final Servo blockerPolen;
    private final Servo blockerNectar;

    public Blocker(Context context) {
        this.context = context;
        blockerPolen = context.servo("blockerPolen");
        blockerNectar = context.servo("blockerNectar");
    }

    /**
     * metodo auxiliar: cria um comando que move UM servo para uma posição
     * Recebe um DoubleSupplier (e não um double) para ler o valor do Dashboard
     * no momento em que o comando executa, e não no momento em que é criado
     */

    private Command set(Servo servo, DoubleSupplier position) {
        return instant(() -> {
            servo.setPosition(position.getAsDouble());
        }).requiring(servo);
    }
    
    // Fecha o bloqueio doPolen
    public Command blockPolen() {
        return set(blockerPolen,() -> blockPositionPolen);
    }
    // Abre o bloqueio do pólen
    public Command unblockPolen() {
        return set(blockerPolen, () -> unblockPositionPolen);
    }
    //fecha o bloqueio do nectar
    public Command blockNectar() {
        return set(blockerNectar, () -> blockPositionNectar);
    }
    //Abre o bloqueio do nectar
    public Command unblockNectar() {
        return set(blockerNectar, () -> unblockPositionNectar);
    }

    //Roda todo ciclo: só envia a posição dos servos para a telemetria
    public Command periodic() {
        return infinite(() -> {
            context.telemetry.addData("Blocker/nectar", blockerNectar.getPosition());
            context.telemetry.addData("Blocker/Polen", blockerPolen.getPosition());
        });
    }
}
