package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drive extends SubsystemBase {

    private Follower follower;
    public Drive(HardwareMap hardwareMap) {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void periodic() {
        follower.update();
    }


    public double cube(double num) {
        return num*num*num;
    }
    public Command manualDrive(Gamepad gamepad) {
        return run(
            () -> {
                double forward = -cube(gamepad.left_stick_y);
                double lateral = -cube(gamepad.left_stick_x);
                double turn = -cube(gamepad.right_trigger + gamepad.left_trigger);
                DrivePowers powers = ManualDrive.fieldCentric(
                    forward,
                    lateral,
                    turn,
                    follower.pose().heading()
                );
                follower.manual(forward, lateral, turn);
            }
        );
    }
}
