package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Pose;

public class MirrorPosition {
    public static Pose mirror(Pose pose) {
        return new Pose(144 - pose.x(), pose.y(), Math.PI - pose.heading());
    }
}
