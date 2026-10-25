package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Pose2dExtKt;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.lang.annotation.Target;
import java.util.HashMap;

import Ori.Coval.Logging.Logger.KoalaLog;

public class Drive extends SubsystemBase {

    private Follower follower;

    public Drive(HardwareMap hardwareMap, HashMap<String, Object> blackboard) {
        follower = (Follower) blackboard.get("follower");
        if (follower == null) {
            follower = Constants.create(hardwareMap);
            blackboard.put("follower", follower);
        } else {
            follower.stop();
        }

    }

    public Follower getFollower() {
        return follower;
    }

    @Override
    public void periodic() {
        follower.update();
        KoalaLog.logPose2d("Drive/Pose", follower.pose().x() * .0254, follower.pose().y() * .0254, follower.pose().heading(), true);
        KoalaLog.log("Drive/IsBusy", follower.isBusy(), true);
    }



    public double cube(double num) {
        return num*num*num;
    }
    public Command manualDrive(Gamepad gamepad) {
        return run(
            () -> {
                double forward = -cube(gamepad.left_stick_y);
                double lateral = -cube(gamepad.left_stick_x);
                double turn = -cube(gamepad.right_trigger - gamepad.left_trigger);
                DrivePowers powers = ManualDrive.fieldCentric(
                    forward,
                    lateral,
                    turn,
                    follower.pose().heading()
                );
                follower.manual(powers);
            }
        );
    }
    public Command assist(Pose target) {
        return startEnd(
                () -> {
                    Path path = line(follower.pose(), target).linear(follower.pose(), target);
                    follower.follow(path);
                },
                () -> {
                    follower.stop();
                }
        );
    }

    public void resetFieldCentric() {
        follower.localizer.reset();
    }

    public Command follow(Path path) {
     return new FollowPathCommand(follower, path);
    }

}
