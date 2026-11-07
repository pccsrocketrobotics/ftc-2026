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
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.FlowerCollector;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.intellij.lang.annotations.Flow;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import Ori.Coval.Logging.Logger.KoalaLog;

public class CommonOpMode extends CommandOpMode {
    public Telemetry dashboardTelemetry = FtcDashboard.getInstance().getTelemetry();

    protected Drive drive;
    protected Intake intake;
    protected Shooter shooter;
    protected FlowerCollector flowerCollector;
    protected Transfer transfer;
    protected Gate gate;
    protected CheeseS cheeseS;
    protected Vision vision;

    private List<LynxModule> allHubs;

    @Override
    public void initialize() {
        if(!KoalaLog.isRunning()) {
            KoalaLog.setup(hardwareMap);
            KoalaLog.start();
        }
        setupBulkReading();

        vision = new Vision(hardwareMap);
        drive = new Drive(hardwareMap, blackboard, vision);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        flowerCollector = new FlowerCollector(hardwareMap);
        transfer = new Transfer(hardwareMap);
        gate = new Gate(hardwareMap);
        cheeseS = new CheeseS(hardwareMap);

    }

    @Override
    public void initialize_loop() {
        super.initialize_loop();
        clearBulkReading();

        if (gamepad1.x) {
            vision.setAlliance(Constants.Alliance.BLUE);
        } else if (gamepad1.b) {
            vision.setAlliance(Constants.Alliance.RED);
        }
    }

    @Override
    public void preRun() {
        super.preRun();
        KoalaLog.log("DS:enabled", true, false);
    }

    @Override
    public void run() {
        clearBulkReading();
        super.run();
        dashboardTelemetry.update();
    }

    @Override
    public void end() {
        super.end();
        KoalaLog.log("DS:enabled", false, false);
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
