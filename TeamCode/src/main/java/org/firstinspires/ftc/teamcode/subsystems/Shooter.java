package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class Shooter extends SubsystemBase {
    private final DcMotorEx shooter;
//    public static double SPEED1 = 700;
    public static double SPEED2 = 1140;
    public static double SPEED3 = 1150;
    public static double SPEED4 = 1075;



    private double shooterSpeed;


    public Shooter(HardwareMap hardwareMap) {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(500, 0, 0,1));
    }


    @Override
    public void periodic() {
        KoalaLog.log("Shooter/Speed", shooter.getVelocity(), true);
        KoalaLog.log("Shooter/Target", shooterSpeed, true);
    }

    public void shoot (double speed) {
        shooter.setVelocity(speed);
        shooterSpeed = speed;
    }
    public Command shootCommand(DoubleSupplier speed) {
        return startEnd(
            () -> shoot(speed.getAsDouble()),
            () -> shoot(0)
        );
    }
    public Command stopCommand() {
        return startEnd(
                () -> {},
                () -> {}
        );
    }
}
