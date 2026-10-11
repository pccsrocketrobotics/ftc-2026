package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import Ori.Coval.Logging.Logger.KoalaLog;

public class CommonOpMode extends CommandOpMode {
    public Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();

    protected Drive drive;
    protected Intake intake;
    protected Shooter shooter;
    protected Sweeper sweeper;
    protected Transfer transfer;

    @Override
    public void initialize() {
        KoalaLog.setup(hardwareMap);
        KoalaLog.start();

        // 1. Create subsystems
        drive = new Drive(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap, telemetry);
        sweeper = new Sweeper(hardwareMap);
        transfer = new Transfer(hardwareMap);

        // 2. Register them so periodic() runs
        register(drive, intake, shooter, sweeper, transfer);

    }

    @Override
    public void run() {
        super.run();
        dashboardTelemetry.update();
    }
}
