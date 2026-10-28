package org.firstinspires.ftc.teamcode.util;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.*;

import java.util.List;

public abstract class SinergyOpmode extends OpMode {
    private static Alliance currentAlliance = Alliance.RED;
    protected FlyWheel flyWheel;
    protected DriveTrain driveTrain;
    protected Intake intake;
    protected Blocker blocker;
    protected Context context;


    protected static Alliance alliance() {
        return currentAlliance;
    }

    public static void setAlliance(Alliance alliance) {
        currentAlliance = alliance;
    }

    private List<LynxModule> hubs;

    @Override
    public void init() {
        Scheduler.reset();
        context = new Context(this);

        driveTrain = new DriveTrain(context);
        flyWheel = new FlyWheel(context);
        intake = new Intake(context);
        blocker = new Blocker(context);

        schedule(
                driveTrain.periodic(),
                flyWheel.periodic(),
                intake.periodic(),
                blocker.periodic()
        );

        hubs = hardwareMap.getAll(LynxModule.class);

        hubs.forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
    }

    @Override
    public void init_loop() {
        Scheduler.execute();

        context.telemetry.addData("Alliance", alliance());
        context.telemetry.update();
    }

    protected final void wraperloop(Runnable runnable) {
        hubs.forEach(LynxModule::clearBulkCache);
        driveTrain.update();
        Hives.swapHive(driveTrain.follower, alliance());
        context.telemetry.addData("Robot/alliance", alliance());

        runnable.run();

        Scheduler.execute();
        context.telemetry.update();
    }
}