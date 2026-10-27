package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Pose;

public class TurretPosition {
    public static double TurretOffsetInches = -5;

    public static Pose getOffset(Pose pose) {
        return pose.compose(new Pose(TurretOffsetInches, 0, 0));
    }
}
