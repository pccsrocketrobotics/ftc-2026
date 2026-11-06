package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.FlowerCollector;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.FlowerCollector;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.function.BooleanSupplier;

import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
public class DriverControl extends CommonOpMode {
    private boolean autoStart = true;
    @Override
    public void initialize() {
        if (gamepad1.x) {
            vision.setAlliance(Constants.Alliance.BLUE);
        } else if (gamepad1.b) {
            vision.setAlliance(Constants.Alliance.RED);
        }
        //setup subsystems
        super.initialize();
        PoseFactory p = PoseFactory.degrees();

        // 1. Wrap the gamepads
        GamepadEx driver = new GamepadEx(gamepad1);
        GamepadEx operater = new GamepadEx(gamepad2);



        // 2. Bind buttons to commands

        //Driver
        drive.setDefaultCommand(
                drive.manualDrive(gamepad1)
        );
        cheeseS.setDefaultCommand(
                cheeseS.inSideCommand()
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(flowerCollector.wristDownCommand());
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(flowerCollector.wristUpCommand());

        driver.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenHeld(drive.assist(p.of(55.2,26.3,-96)));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenHeld(drive.assist(p.of(55.2,115,96)));
        driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenHeld(drive.assist(p.of(12.5,117,90)));

        driver.getGamepadButton(GamepadKeys.Button.PS).whenPressed(new InstantCommand(() -> drive.resetFieldCentric()));

        //Operator

        operater.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(intake.outCommand().alongWith(transfer.outCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(intake.inCommand().alongWith(transfer.inCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(intake.stopCommand().alongWith(transfer.stopCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(intake.outCommand().alongWith(transfer.inCommand()));

        operater.getGamepadButton(GamepadKeys.Button.Y).whenPressed(shooter.stopCommand());
        operater.getGamepadButton(GamepadKeys.Button.X).whenPressed(shooter.shootCommand(() -> Shooter.SPEED1));
        operater.getGamepadButton(GamepadKeys.Button.A).whenPressed(shooter.shootCommand(() -> Shooter.SPEED2));
        operater.getGamepadButton(GamepadKeys.Button.B).whenPressed(shooter.shootCommand(() -> Shooter.SPEED3));

        operater.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(gate.gateOpenCommand());
        operater.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(gate.gateCloseCommand());
    }

    @Override
    public void initialize_loop() {
        super.initialize_loop();
        if(gamepad1.start || gamepad2.start){
            autoStart = false;
        }
        telemetry.addData( "autoStart", autoStart);
        telemetry.update();
    }

    @Override
    public void preRun() {
        super.preRun();
        if(autoStart) {
            schedule(gate.gateCloseCommand());
            schedule(shooter.shootCommand(()-> Shooter.SPEED2));
            schedule(intake.inCommand());
            schedule(transfer.inCommand());
        }

    }

    @Override
    public void end() {
        super.end();
        KoalaLog.stop();
    }
}