package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import Ori.Coval.Logging.Logger.KoalaLog;

@TeleOp
public class DriverControl extends CommonOpMode {
    private boolean autoStart = true;
    @Override
    public void initialize() {
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
        driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(flowerCollector.wristDownCommand());
        driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(flowerCollector.wristUpCommand());

        // corner left
        new Trigger(() -> gamepad1.dpad_down && gamepad1.dpad_left).whileActiveOnce(drive.assist(p.of(45.4,115,120)));
        // corner right
        new Trigger(() -> gamepad1.dpad_down && gamepad1.dpad_right).whileActiveOnce(drive.assist(p.of(45.5,26,-120)));
        // middle left
        new Trigger(() -> gamepad1.dpad_left && !gamepad1.dpad_down).whileActiveOnce(drive.assist(p.of(58,115,96)));
        // middle right
        new Trigger(() -> gamepad1.dpad_right && !gamepad1.dpad_down).whileActiveOnce(drive.assist(p.of(58,26,-94)));

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
        operater.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(shooter.smartShoot());


        operater.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(gate.gateOpenCommand()).whenReleased(gate.gateCloseCommand());
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
            schedule(cheeseS.inSideCommand());
            schedule(gate.gateCloseCommand());
            schedule(shooter.smartShoot());
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