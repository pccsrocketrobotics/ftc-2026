package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;

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



    public Command manualDrive(Gamepad gamepad) {
        return run(
            () -> {
                double forward = -gamepad.left_stick_y;
                double lateral = gamepad.left_stick_x;
                double turn =   gamepad.right_trigger - gamepad.left_trigger;

                follower.manual(forward, lateral, turn);
            }
        );
    }
}
