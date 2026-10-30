package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class FlowerCollector extends SubsystemBase {

    private Servo wristL;
    private Servo wristR;
    public static double WRIST_DOWN = 0;
    public static double WRIST_UP = 1;

    public FlowerCollector(HardwareMap hardwareMap) {
        wristL = hardwareMap.get(Servo.class, "wristL");
        wristR = hardwareMap.get(Servo.class, "wristR");
    }

    public void wrist(double position) {
        wristL.setPosition(position);
        wristR.setPosition(position);
    }

    @Override
    public void periodic() {
        KoalaLog.log("WristL/Power", wristL.getPosition(), true);
        KoalaLog.log("WristR/Power", wristR.getPosition(), true);
    }
    public Command wristDownCommand() {
        return startRun(
            () -> wrist(WRIST_DOWN),
            () -> {}
        );
    }

    public Command wristUpCommand() {
        return startRun(
            () -> wrist(WRIST_UP),
            () -> {}
        );
    }
}
