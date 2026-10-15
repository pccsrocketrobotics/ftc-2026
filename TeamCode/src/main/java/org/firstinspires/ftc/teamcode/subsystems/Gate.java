package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Gate extends SubsystemBase {

    private Servo gate;

    public static double GATE_OPENED = 0;
    public static double GATE_CLOSED = 180;

    public Gate(HardwareMap hardwareMap) {
        gate = hardwareMap.get(Servo.class, "gate");
    }

    public void gate(double position) {
        gate.setPosition(position);
    }
    public Command gateOpenCommand() {
        return startRun(
                () -> gate(GATE_OPENED),
                () -> {}
        );
    }

    public Command gateCloseCommand() {
        return startRun(
                () -> gate(GATE_CLOSED),
                () -> {}
        );
    }


}
