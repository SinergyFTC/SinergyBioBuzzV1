package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;

/**
 *
 * Centraliza o acesso ao hardware e à telemetria. Cada OpMode cria um Context
 * e passa a mesma instância para todos os subsistemas (injeção de dependência),
 * assim nenhum subsistema precisa buscar o hardwareMap por conta própria.
 */
@Config("Configuration")
public class Context {

    public HardwareMap hardwareMap;
    public Telemetry telemetry;

    //Cria o contexto a partir do Opmode em execução
    public Context(OpMode opmode) {
        hardwareMap = opmode.hardwareMap;
        telemetry = new MultipleTelemetry(opmode.telemetry,
                FtcDashboard.getInstance().getTelemetry());

    }
    /**
     * Devolve um servo com cache de escrita.
     * O CachingServo só envia a posição ao hardware quando ela muda,
     * o que reduz o tempo de cada loop.
     *
     * @param name nome do servo na configuração do robô
     */
    public Servo servo(String name) {
        return new CachingServo(hardwareMap.servo.get(name));
    }

    /**
     * Devolve um motor com cache de escrita.
     * O CachingDcMotorEx só envia a potência ao hardware quando ela muda.
     *
     * @param name nome do motor na configuração do robô
     */
    public DcMotorEx motor(String name) {
        return new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, name));
    }
}