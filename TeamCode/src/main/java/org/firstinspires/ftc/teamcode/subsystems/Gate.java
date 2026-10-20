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

    private Servo gate1;
    private Servo gate2;

    public static double GATE_OPENED = 0.75;
    public static double GATE_CLOSED = 1;

    public Gate(HardwareMap hardwareMap) {
        gate1 = hardwareMap.get(Servo.class, "gate1");
        gate2 = hardwareMap.get(Servo.class, "gate2");
    }

    public void gate(double position) {
        gate1.setPosition(position);
        gate2.setPosition(position);
    }

    @Override
    public void periodic() {
        KoalaLog.log("Gate1/Position", gate1.getPosition(), true);
        KoalaLog.log("Gate2/Position", gate2.getPosition(), true);
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
