package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;

import Ori.Coval.Logging.Logger.KoalaLog;

@Config
public class Shooter extends SubsystemBase {
    private final DcMotorEx shooter;
    public static double SPEED1 = 1100;
    public static double SPEED2 = 1200;
    public static double SPEED3 = 1300;
    public static double P = 400, I = 0, D = 0, F = 13.8;
    public static double RMIN = 27;
    public static double RMAX = 47;
    public static double  SMIN = 1100;
    public static double SMAX = 1300;
    private double shooterSpeed;
    private final Vision vision;


    public Shooter(HardwareMap hardwareMap, Vision vision) {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));
        this.vision = vision;
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

    public Command smartShoot(){
        return new SmartShoot();
    }

    class SmartShoot extends CommandBase{
        private final ElapsedTime timeout = new ElapsedTime();

        public SmartShoot(){
            addRequirements(Shooter.this);
        }

        @Override
        public void initialize() {
            shoot(SPEED2);
        }

        @Override
        public void execute() {
            if (vision.getClusterId() != Vision.NO_TAG) {
                double x = (vision.getRange() - RMIN)/(RMAX-RMIN);
                double speed = x*(SMAX-SMIN)+SMIN;
                shoot(speed);
                timeout.reset();
            } else if (timeout.seconds() >= 5){
                shoot(SPEED2);
            }
        }

        @Override
        public void end(boolean interrupted) {
            shoot(0);
        }
    }
}
