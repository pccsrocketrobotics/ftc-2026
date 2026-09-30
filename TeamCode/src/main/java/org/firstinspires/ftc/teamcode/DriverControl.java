package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.StartEndCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Drive;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.function.BooleanSupplier;

@TeleOp
public class DriverControl extends CommandOpMode {
    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        // 1. Create subsystems
        Drive drive = new Drive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Shooter shooter = new Shooter(hardwareMap, telemetry);

        // 2. Register them so periodic() runs
        register(drive, intake,shooter);

        // 3. Wrap the gamepads
        GamepadEx operator = new GamepadEx(gamepad1);

        // 4. Bind buttons to commands
        drive.setDefaultCommand(
                drive.manualDrive(gamepad1)
        );
        operator.getGamepadButton(GamepadKeys.Button.B).whenHeld(intake.outCommand());
        operator.getGamepadButton(GamepadKeys.Button.A).whenHeld(intake.inCommand());
        operator.getGamepadButton(GamepadKeys.Button.X).whenHeld(intake.stopCommand());

        operator.getGamepadButton(GamepadKeys.Button.BACK).whenHeld(shooter.shootCommand());

        //        intake.setDefaultCommand(intake.inCommand());
    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}