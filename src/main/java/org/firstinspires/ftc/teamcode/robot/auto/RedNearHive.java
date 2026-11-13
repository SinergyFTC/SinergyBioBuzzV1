package org.firstinspires.ftc.teamcode.robot.auto;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.control.ShootingController;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.Hives;
import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

import dev.frozenmilk.util.collections.Cons;

@Autonomous(name = "RedNearIntake", group = "Autonomous")
public class RedNearHive extends SinergyOpmode {
    private AutoPaths paths;
    private ShootingController shootingController;

    @Override
    public void init() {
        super.init();
        paths = new AutoPaths();
        turret.setStartingAngle(-90);
        driveTrain.localize(paths.start());
        shootingController = new ShootingController(turret, flyWheel, hood, context);
    }

    public Command shoot() {
        return blocker.unblockPolen()
                .then(waitMs(1500))
                .then(blocker.blockPolen());
    }

    @Override
    public void start() {
        schedule(
                sequential(
                        flyWheel.on(),
                        race(waitUntil(flyWheel::atTarget), waitMs(1000)),
                        shoot(),
                        intake.on(),
                        driveTrain.followPath(paths.toIntake()),
                        driveTrain.followPath(paths.toOtherSide()),
                        parallel(
                                driveTrain.followPath(paths.toSecondRowShoot()),
                                sequential(
                                        waitUntil(() -> driveTrain.follower.parametricCompletion() > 0.95),
                                        shoot()
                                )
                        ),
                        driveTrain.followPath(paths.toPark()),
                        intake.off(),
                        flyWheel.off()
                ));
    }

    @Override
    public void loop() {
        wraperloop(() -> {
            shootingController.prepareShoot(
                    driveTrain.follower.pose(),
                    driveTrain.follower.velocity(),
                    Hives.getCurrentHive(),
                    alliance()
            );
        });
    }

    private static class AutoPaths {
        private final PoseFactory poseFactory = PoseFactory.degrees();

        private final Pose start = poseFactory.of(62.2, 9.0, 180);
        private final Pose toIntake = poseFactory.of(12.5677, 11.4956, 270);
        private final Pose toOtherSideStart = poseFactory.of(12.5677, 11.4956, 270);
        private final Pose toOtherSide = poseFactory.of(61.7683, 31.0629, 270);
        private final Pose toSecondRowShootStart = poseFactory.of(61.7683, 31.0629, 270);
        private final Pose toSecondRowShoot = poseFactory.of(61.2019, 130.8335, 270);
        private final Pose toParkStart = poseFactory.of(61.2019, 130.8335, 270);
        private final Pose toPark = poseFactory.of(5.6556, 104.4911, 0);

        Pose start() {
            return start;
        }

        Path toIntake() {
            return Paths.line(start, toIntake).linear(start, toIntake);
        }

        Path toOtherSide() {
            return Paths.line(toIntake, toOtherSide).linear(toIntake, toOtherSide);
        }

        Path toSecondRowShoot() {
            return Paths.line(toOtherSide, toSecondRowShoot).linear(toOtherSide, toSecondRowShoot);
        }

        Path toPark() {
            return Paths.line(toSecondRowShoot, toPark).linear(toSecondRowShoot, toPark);
        }
    }
}