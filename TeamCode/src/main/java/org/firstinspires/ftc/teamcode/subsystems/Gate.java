package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class Gate extends SubsystemBase {

    private Servo gate;

    public static double GATE_OPENED = 0.75;
    public static double GATE_CLOSED = 1;

    public Gate(HardwareMap hardwareMap) {
        gate = hardwareMap.get(Servo.class, "gate");
    }

    public void gate(double position) {
        gate.setPosition(position);
    }

    @Override
    public void periodic() {
        KoalaLog.log("Gate/Position", gate.getPosition(), true);
    }
    public Command gateOpenCommand() {
        return runOnce(
                () -> gate(GATE_OPENED)
        );
    }

    public Command gateCloseCommand() {
        return runOnce(
                () -> gate(GATE_CLOSED)
        );
    }


}
