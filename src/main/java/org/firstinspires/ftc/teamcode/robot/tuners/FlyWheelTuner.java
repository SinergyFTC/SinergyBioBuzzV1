package org.firstinspires.ftc.teamcode.robot.tuners;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

@TeleOp(name = "Flywheel Tuner", group = "Tuners")
@Config("Flywheel Tuner")
public class FlyWheelTuner extends SinergyOpmode {
    public static boolean on = false;
    public static double target = 0;

    @Override
    public void loop() {
        wraperloop(() -> {
            if(on) {
                flyWheel.on();
                flyWheel.setTarget(target);
            } else {
                flyWheel.off();
            }
        });
    }
}