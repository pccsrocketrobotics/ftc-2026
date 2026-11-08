package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import Ori.Coval.Logging.Logger.KoalaLog;

public class Transfer extends SubsystemBase {
    private final DcMotorEx transfer;
    public Transfer(HardwareMap hardwareMap) {
        transfer = hardwareMap.get(DcMotorEx.class, "transfer");

    }
    public void spin(double power) {
        transfer.setPower(power);
    }
    @Override
    public void periodic() {
        KoalaLog.log("Transfer/Power", transfer.getPower(), true);
        KoalaLog.log("Transfer/Speed", transfer.getVelocity(), true);
        KoalaLog.log("Transfer/Current", transfer.getCurrent(CurrentUnit.AMPS), true);
    }

    public Command inCommand() {
        return startEnd(
                () -> spin(1),
                () -> spin(0)
        );
    }

    public Command outCommand() {
        return startEnd(
                () -> spin(-1),
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
