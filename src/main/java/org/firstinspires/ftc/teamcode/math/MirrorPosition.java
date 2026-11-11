package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.math.Pose;

public class MirrorPosition {
    public static Pose mirror(Pose pose) {
        return new Pose(141.5 - pose.x(), pose.y(), Math.PI - pose.heading());
    }
    public static Pose mirrorY(Pose pose) {
        return new Pose(pose.x(), 141.5 - pose.y(), -pose.heading());
    }
}
