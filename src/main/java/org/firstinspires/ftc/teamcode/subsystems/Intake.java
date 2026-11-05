package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Context;

/**
 * Subsistema do INTAKE (coletor de peças).
 *
 * Funciona com uma "máquina de estados": os comandos (on/off/reverse) só
 * TROCAM o modo; quem realmente manda potência ao motor é o periodic(),
 * que roda todo ciclo do loop. Assim a lógica fica separada do hardware.
 */

public class Intake {
    // Potências ajustáveis (public static -> podem ser editadas no Dashboard)
    public static double reversepower = -1.0;// potência ao cuspir peças
    public static double offpower = 0.0; // potência desligado
    public static double onpower = 1.0; // potência coletando
    public static double shortReverseTime = 150;// duração (ms) da reversão curta
    
    private final Context context;// acesso a hardwareMap/telemetria
    private final DcMotorEx intakeMotor;// motor do coletor
    private Mode mode = Mode.OFF;// estado atual (começa desligado)

    // Estados possíveis do intake
    enum Mode {
        ON,
        OFF,
        REVERSE
    }

    public Intake(Context context) {
        this.context = context;
        intakeMotor = context.motor("Intake");// nome configurado no Driver Hub
    }
//Liga o intake. requiring(motor) evita dois comandos usando o motor ao mesmo tempo
    public Command on() {
        return instant(() -> mode = Mode.ON).requiring(intakeMotor);
    }
    //Desliga o intake.
    public Command off() {
        return instant(() -> mode = Mode.OFF).requiring(intakeMotor);
    }
    //Inverte o intake (desentupir / cuspir)
    public Command reverse() {
        return instant(() -> mode = Mode.REVERSE).requiring(intakeMotor);
    }
    /**
     * Reversão curta: inverte por shortReverseTime ms e volta a ligar
     * ATENÇÃO: do jeito que está, o instant(...) apenas CRIA a sequência dentro
     * da lambda e nunca a executa. O correto seria retornar direto
     * reverse().then(waitMs(...)).then(on()) com o ConflictBehavior
     */
    public Command shortReversed() {
        return instant(() -> reverse().then(waitMs(shortReverseTime)).then(on()).setConflictBehavior(ConflictBehavior.OVERRIDE));
    }
    //Alterna: se estiver desligado liga; caso contrário inverte.
    public Command toggle() {
        return conditional(() -> mode == Mode.OFF, on(), reverse());
    }
    //Roda todo ciclo: aplica ao motor a potência do modo atual
    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case ON:
                    intakeMotor.setPower(onpower);
                    break;
                case OFF:
                    intakeMotor.setPower(offpower);
                    break;
                case REVERSE:
                    intakeMotor.setPower(reversepower);
                    break;
            }
        });
    }
}
