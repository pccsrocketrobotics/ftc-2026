package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class Gate extends SubsystemBase {

    private Servo gateL;
    private Servo gateR;
    private AnalogInput gateLpos;
    private AnalogInput gateRpos;

    public static double GATEL_OPENED = 0.5;
    public static double GATEL_CLOSED = 0.9;
    public static double GATER_CLOSED = 0.6;
    public static double GATER_OPENED = 1;

    public Gate(HardwareMap hardwareMap) {
        gateL = hardwareMap.get(Servo.class, "gateL");
        gateR = hardwareMap.get(Servo.class, "gateR");
        gateLpos = hardwareMap.get(AnalogInput.class, "gateLpos");
        gateRpos = hardwareMap.get(AnalogInput.class, "gateRpos");
    }

    public void gate(double position1, double position2) {
        gateL.setPosition(position1);
        gateR.setPosition(position2);
    }

    @Override
    public void periodic() {
        KoalaLog.log("GateL/Target", gateL.getPosition(), true);
        KoalaLog.log("GateR/Target", gateR.getPosition(), true);
        KoalaLog.log("GateL/Position", gateLpos.getVoltage(), true);
        KoalaLog.log("GateR/Position", gateRpos.getVoltage(), true);

    }
    public Command gateOpenCommand() {
        return runOnce(
                () -> gate(GATEL_OPENED, GATER_OPENED)
        );
    }

    public Command gateCloseCommand() {
        return runOnce(
                () -> gate(GATEL_CLOSED, GATER_CLOSED)
        );
    }


}
