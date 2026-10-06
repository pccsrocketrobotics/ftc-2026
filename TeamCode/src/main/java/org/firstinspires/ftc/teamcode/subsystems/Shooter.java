package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class Shooter extends SubsystemBase {
    private final DcMotorEx shooter;
    public static double TARGET_SPEED = 500;
    private double shooterSpeed;

    private final Telemetry telemetry;


    public Shooter(HardwareMap hardwareMap, Telemetry telemetry) {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        double currentVelocity = shooter.getVelocity();
        double targetVelocity = shooterSpeed;
        telemetry.addData("Target speed", targetVelocity);
        telemetry.addData("Current speed", currentVelocity);
        KoalaLog.log("Current speed", currentVelocity, false);
        KoalaLog.log("Target speed", targetVelocity, false);

    }

    public void shoot (double speed) {
        shooter.setVelocity(speed);
        shooterSpeed = speed;
    }
    public Command shootCommand() {
        return startEnd(
            () -> shoot(TARGET_SPEED),
            () -> shoot(0)
        );
    }
}
