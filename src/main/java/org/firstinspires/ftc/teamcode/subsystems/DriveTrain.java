package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.Alliance;
import org.firstinspires.ftc.teamcode.util.Context;

public class DriveTrain {
    public Follower follower;
    public static Pose previousPose = Pose.zero();
    public Context context;

    public DriveTrain(Context context) {
        this.context = context;
        follower = Constants.create(context.hardwareMap);
    }
    public static void localize(Pose pose) {
        previousPose = pose;
    }

    public void PreviousStartingPos() {
        follower.setPose(previousPose);
    }

    public void ManualDrive(double forward, double lateral, double turn, Alliance alliance) {
        double offset = (alliance == Alliance.BLUE) ? Math.PI : 0;
        DrivePowers powers = ManualDrive.fieldCentric(
                forward,
                lateral,
                turn,
                follower.pose().heading(),
                offset
        );
        ManualDrive.driveOrHold(follower, powers);
    }

    public void update() {
        follower.update();
    }


    public Command periodic() {
        return infinite(() -> {
            previousPose = follower.pose();

            context.telemetry.addData("Current X", follower.pose().x());
            context.telemetry.addData("Current Y", follower.pose().y());
            context.telemetry.addData("Current Heading", Math.toDegrees(follower.pose().heading()));
        });
    }
}
