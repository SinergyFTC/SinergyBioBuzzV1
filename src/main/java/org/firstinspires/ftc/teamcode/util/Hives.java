package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class Hives {
    public static Pose RedHiveUpper = new Pose(57.5, 53.0, Math.toRadians(0));
    public static Pose RedHiveLower = new Pose(57.5, 83.0, Math.toRadians(0));
    public static Pose BlueHiveUpper = new Pose(83.5, 57.0, Math.toRadians(0));
    public static Pose BlueHiveLower = new Pose(83.5, 83.0, Math.toRadians(0));

    private static Pose currentHive = RedHiveUpper;

    public static void swapHive(Follower follower, Alliance alliance) {
        if (alliance == Alliance.RED) {
            if (follower.pose().y() > 72) {
                currentHive = RedHiveLower;
            } else {
                currentHive = RedHiveUpper;
            }
        } else {
            if (follower.pose().y() > 72) {
                currentHive = BlueHiveLower;
            } else {
                currentHive = BlueHiveUpper;
            }
        }
    }

    public Pose getCurrentHive() {
        return currentHive;
    }
}
