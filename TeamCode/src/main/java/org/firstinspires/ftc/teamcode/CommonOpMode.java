package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.List;

import Ori.Coval.Logging.Logger.KoalaLog;

public class CommonOpMode extends CommandOpMode {
    public Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();

    protected Drive drive;
    protected Intake intake;
    protected Shooter shooter;
    protected Sweeper sweeper;
    protected Transfer transfer;

    private List<LynxModule> allHubs;

    @Override
    public void initialize() {
        KoalaLog.setup(hardwareMap);
        KoalaLog.start();
        setupBulkReading();

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
    public void initialize_loop() {
        clearBulkReading();
    }

    @Override
    public void run() {
        clearBulkReading();
        super.run();
        dashboardTelemetry.update();
    }
    private void setupBulkReading() {
        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }
    private void clearBulkReading() {
        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }
}
