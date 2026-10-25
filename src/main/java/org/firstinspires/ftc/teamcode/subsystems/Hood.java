package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Context;

public class Hood {
    private final Context context;
    private final Servo HoodServo;
    public static double incrementServo = 0.01;

    public Hood(Context context) {
        this.context = context;
        HoodServo = context.servo("Hood");
    }
}
