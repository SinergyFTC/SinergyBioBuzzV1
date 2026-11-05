package org.firstinspires.ftc.teamcode.robot.auto;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.util.SinergyOpmode;

public class BlueNearHive extends SinergyOpmode {
    @Override
    public void init() {
        super.init();
    }
    public Command shoot() {
        return blocker.unblockPolen()
                .then(waitMs(1500))
                .then(blocker.blockPolen());
    }
    @Override
    public void loop() {

    }
}
