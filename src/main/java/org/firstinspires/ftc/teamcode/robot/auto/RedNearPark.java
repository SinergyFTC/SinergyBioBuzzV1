package org.firstinspires.ftc.teamcode.robot.auto;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;
@Autonomous(name = "RedNearPark", group = "Autonomous")
public class RedNearPark extends SinergyOpmode {
    private AutoPaths paths;
    private ShootingController shootingController;

    public Command shoot() {
        return blocker.unblockPolen()
                .then(waitMs(2000))
                .then(blocker.blockPolen());
    }

    @Override
    public void init() {
        super.init();
        paths = new AutoPaths();
        turret.setStartingAngle(0);
        // driveTrain.localize();
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }
    @Override
    public void start() {
        schedule(
                sequential(
                        flyWheel.on(),
                        waitUntil(()-> flyWheel.getVelocity() >= flyWheel.getTarget()),
                        waitMs(10000),
                        shoot()
                )
        );
    }
    @Override
    public void loop() {
        shootingController.prepareShoot(
                driveTrain.follower.pose(),
                driveTrain.follower.velocity(),
                Hives.getCurrentHive(),
                alliance());
    }

    public class AutoPaths {
        private final PoseFactory poseFactory = PoseFactory.degrees();

        private final Pose start = poseFactory.of(58.4247, 128.5412, 270);
        private final Pose path1 = poseFactory.of(5.8185, 104.7831, 90);

        Pose start() {
            return start;
        }
        public Path path1() {
            return Paths.line(start, path1).linear(start, path1);
        }
    }

}
