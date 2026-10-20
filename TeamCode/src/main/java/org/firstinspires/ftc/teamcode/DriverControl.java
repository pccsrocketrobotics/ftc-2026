package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.api.PoseFactory;
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
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;

import java.util.function.BooleanSupplier;

import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
public class DriverControl extends CommonOpMode {
    @Override
    public void initialize() {
        //setup subsystems
        super.initialize();
        drive.getFollower().setPose(PoseFactory.degrees().of(0,0,90));
        boolean robot = Constants.isPracticeBot();

        // 1. Wrap the gamepads
        GamepadEx driver = new GamepadEx(gamepad1);
        GamepadEx operater = new GamepadEx(gamepad2);

        // 2. Bind buttons to commands

        //Driver
        drive.setDefaultCommand(
                drive.manualDrive(gamepad1)
        );
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(sweeper.wristDownCommand());
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(sweeper.wristUpCommand());
        driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenHeld(sweeper.grabberSpinCommand());

        driver.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> drive.resetFieldCentric()));

        //Operator

        operater.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(intake.outCommand().alongWith(transfer.outCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(intake.inCommand().alongWith(transfer.inCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(intake.stopCommand().alongWith(transfer.stopCommand()));
        operater.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(intake.stopCommand().alongWith(transfer.stopCommand()));

        operater.getGamepadButton(GamepadKeys.Button.A).whenPressed(shooter.stopCommand());
        operater.getGamepadButton(GamepadKeys.Button.B).whenPressed(shooter.shootCommand(() -> Shooter.SPEED2));
        operater.getGamepadButton(GamepadKeys.Button.X).whenPressed(shooter.shootCommand(() -> Shooter.SPEED3));
        operater.getGamepadButton(GamepadKeys.Button.Y).whenPressed(shooter.stopCommand());

//        operater.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(gate.gateOpenCommand());
//        operater.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(gate.gateCloseCommand());


    }

    @Override
    public void preRun() {
        super.preRun();
        schedule(gate.gateCloseCommand());
    }
}