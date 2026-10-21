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

@Config("Configuration")
public class Context {
    public static boolean AdvantageScope = false;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;

    public Context(OpMode opmode){
        hardwareMap = opmode.hardwareMap;
        telemetry = new MultipleTelemetry(telemetry,
                FtcDashboard.getInstance().getTelemetry());

    }
    public void addPose(String name, Pose pose) {
        if(AdvantageScope) {
            telemetry.addData(name + "x", 144 - pose.x());
            telemetry.addData(name + "y", 144 - pose.y());
            telemetry.addData(name + "heading (°)", 180 - Math.toDegrees(pose.heading()));
        } else {
            telemetry.addData(name + "x",  pose.x());
            telemetry.addData(name + "y", pose.y());
            telemetry.addData(name + "heading (°)", Math.toDegrees(pose.heading()));
        }
    }
    public Servo servo(String name) {
        return new CachingServo(hardwareMap.servo.get(name));
    }

    public DcMotorEx motor(String name) {
        return new CachingDcMotorEx(hardwareMap.get(DcMotorEx.class, name));
    }

    public VoltageSensor voltagesensor(){
        return hardwareMap.voltageSensor.iterator().next();
    }

}
