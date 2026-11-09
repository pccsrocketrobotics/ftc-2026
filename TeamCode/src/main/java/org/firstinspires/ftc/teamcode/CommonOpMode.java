package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.CheeseS;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.FlowerCollector;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.PowerSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

import java.util.List;

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
    protected PowerSwitch powerSwitch;

    private List<LynxModule> allHubs;
    private long lastLoopStart = 0;

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
        powerSwitch = new PowerSwitch(hardwareMap);

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
        long loopStart = System.nanoTime();
        if (lastLoopStart != 0) {
            KoalaLog.log("Loop/IntervalMs", (loopStart - lastLoopStart) / 1e6, true);
        }
        lastLoopStart = loopStart;
        clearBulkReading();
        logGamepad("Gamepad1", gamepad1);
        logGamepad("Gamepad2", gamepad2);
        super.run();
        dashboardTelemetry.update();
        KoalaLog.log("Loop/RunMs", (System.nanoTime() - loopStart) / 1e6, true);
    }

    @Override
    public void end() {
        super.end();
        KoalaLog.log("DS:enabled", false, false);
    }

    private void logGamepad(String prefix, Gamepad gamepad) {
        KoalaLog.log(prefix + "/LeftStickX", gamepad.left_stick_x, true);
        KoalaLog.log(prefix + "/LeftStickY", gamepad.left_stick_y, true);
        KoalaLog.log(prefix + "/RightStickX", gamepad.right_stick_x, true);
        KoalaLog.log(prefix + "/RightStickY", gamepad.right_stick_y, true);
        KoalaLog.log(prefix + "/LeftTrigger", gamepad.left_trigger, true);
        KoalaLog.log(prefix + "/RightTrigger", gamepad.right_trigger, true);
        KoalaLog.log(prefix + "/A", gamepad.a, true);
        KoalaLog.log(prefix + "/B", gamepad.b, true);
        KoalaLog.log(prefix + "/X", gamepad.x, true);
        KoalaLog.log(prefix + "/Y", gamepad.y, true);
        KoalaLog.log(prefix + "/DpadUp", gamepad.dpad_up, true);
        KoalaLog.log(prefix + "/DpadDown", gamepad.dpad_down, true);
        KoalaLog.log(prefix + "/DpadLeft", gamepad.dpad_left, true);
        KoalaLog.log(prefix + "/DpadRight", gamepad.dpad_right, true);
        KoalaLog.log(prefix + "/LeftBumper", gamepad.left_bumper, true);
        KoalaLog.log(prefix + "/RightBumper", gamepad.right_bumper, true);
        KoalaLog.log(prefix + "/LeftStickButton", gamepad.left_stick_button, true);
        KoalaLog.log(prefix + "/RightStickButton", gamepad.right_stick_button, true);
        KoalaLog.log(prefix + "/Back", gamepad.back, true);
        KoalaLog.log(prefix + "/Start", gamepad.start, true);
        KoalaLog.log(prefix + "/Guide", gamepad.guide, true);
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
