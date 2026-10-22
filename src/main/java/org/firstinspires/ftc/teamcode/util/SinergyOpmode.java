package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

public abstract class SinergyOpmode extends OpMode {
    private static Alliance currentAlliance = Alliance.RED;

    protected static Alliance alliance() {
        return currentAlliance;
    }

    public static void setAlliance(Alliance alliance) {
        currentAlliance = alliance;
    }

    private List<LynxModule> hubs;

    @Override
    public void init () {
        hubs = hardwareMap.getAll(LynxModule.class);

        hubs.forEach(hub -> hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL));
    }

}
