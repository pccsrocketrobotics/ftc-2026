package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

    private final DcMotorEx dcMotorEx;

    private double intakePower = 0;

    public IntakeSubsystem(DcMotorEx dcMotorEx) {dcMotorEx = HardwareMap.get(DcMotorEx.class, "intake");}

    public void spin(double power) {
    intakePower = power;
    dcMotorEx.setPower(power);
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
                () -> {}
            );
        }
}
