package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Transfer extends SubsystemBase {
    private final DcMotorEx transfer;
    private double transferPower = 0;
    public Transfer(HardwareMap hardwareMap) {
        transfer = hardwareMap.get(DcMotorEx.class, "intake");

    }
    public void spin(double power) {
        transfer.setPower(power);
    }

    public Command inCommand() {
        return startEnd(
                () -> spin(0.7),
                () -> spin(0)
        );
    }

    public Command outCommand() {
        return startEnd(
                () -> spin(-0.7),
                () -> spin(0)
        );
    }

    public Command stopCommand() {
        return startEnd(
                () -> spin(0),
                () -> {
                }
        );
    }
}
