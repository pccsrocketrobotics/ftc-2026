package org.firstinspires.ftc.teamcode.subsystems;

import static com.seattlesolvers.solverslib.command.Commands.startEnd;
import static com.seattlesolvers.solverslib.command.Commands.startRun;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Sweeper extends SubsystemBase {

    private Servo wrist;
    private CRServo grabber;

    public static double WRIST_DOWN = 0;
    public static double WRIST_UP = 180;
    private double grabberPower;

    public Sweeper(HardwareMap hardwareMap) {
        wrist = hardwareMap.get(Servo.class, "wrist");
        grabber = hardwareMap.get(CRServo.class, "grabber");
    }

    public void wrist(double position) {
        wrist.setPosition(position);
    }
    public void grab(double power) {
        grabberPower = power;
        grabber.setPower(power);
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

    public Command grabberSpinCommand() {
        return startEnd(
            () -> grab(0.5),
            () -> grab(0)
        );
}

}
