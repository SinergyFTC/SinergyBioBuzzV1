package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

public class Turret {
    private final Context context;
    private final Servo turretServoR;
    private final Servo turretServoL;

    public Turret(Context context) {
        this.context = context;
        turretServoL = context.servo("turretServoL");
        turretServoR = context.servo("turretServoR");
    }
}
