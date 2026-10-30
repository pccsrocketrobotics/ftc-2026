package org.firstinspires.ftc.teamcode;

import static com.seattlesolvers.solverslib.command.Commands.startEnd;

import com.seattlesolvers.solverslib.command.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class CheeseS extends SubsystemBase {

    private CRServo sideIntakeL;
    private CRServo sideIntakeR;
    private double sideIntakePower;

    public CheeseS(HardwareMap hardwareMap) {
        sideIntakeL = hardwareMap.get(CRServo.class, "cheeseL");
        sideIntakeR = hardwareMap.get(CRServo.class, "cheeseR");
    }
    public void sideIntakeSpin(double power) {
        sideIntakePower = power;
        sideIntakeL.setPower(-power);
        sideIntakeR.setPower(power);
    }
    public Command inSideCommand() {
        return startEnd(
                () -> sideIntakeSpin(1),
                () -> sideIntakeSpin(0)
        );
    }
    public Command outSideCommand() {
        return startEnd(
                () -> sideIntakeSpin(-1),
                () -> sideIntakeSpin(0)
        );
    }
    public Command stopSideCommand() {
        return startEnd(
                () -> sideIntakeSpin(0),
                () -> {}
        );
    }
}
