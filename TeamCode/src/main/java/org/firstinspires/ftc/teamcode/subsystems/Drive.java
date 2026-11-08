package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.PIDController;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.geometry.Pose2dExtKt;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.lang.annotation.Target;
import java.util.HashMap;

import Ori.Coval.Logging.Logger.KoalaLog;

public class Drive extends SubsystemBase {

    private Follower follower;
    private Vision vision;
    private boolean headingLock = false;
    private double targetHeading = 0;
    // Same motor objects the Mecanum drivetrain uses (its own array is private), for logging only
    private static final String[] WHEEL_NAMES = {"LF", "RF", "LR", "RR"};
    private final DcMotorEx[] wheels = new DcMotorEx[4];
    private final GoBildaPinpointDriver pinpoint;
    public Drive(HardwareMap hardwareMap, HashMap<String, Object> blackboard, Vision vision) {
        follower = (Follower) blackboard.get("follower");
        if (follower == null) {
            follower = Constants.create(hardwareMap);
            blackboard.put("follower", follower);
        } else {
            follower.stop();
        }
        this.vision = vision;

        Mecanum mecanum = (Mecanum) follower.drivetrain;
        wheels[0] = hardwareMap.get(DcMotorEx.class, mecanum.config.frontLeftName.get());
        wheels[1] = hardwareMap.get(DcMotorEx.class, mecanum.config.frontRightName.get());
        wheels[2] = hardwareMap.get(DcMotorEx.class, mecanum.config.backLeftName.get());
        wheels[3] = hardwareMap.get(DcMotorEx.class, mecanum.config.backRightName.get());

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.localizerConfig.name.get());
        pinpoint.setBulkReadScope(
            GoBildaPinpointDriver.Register.DEVICE_STATUS,
            GoBildaPinpointDriver.Register.LOOP_TIME,
            GoBildaPinpointDriver.Register.X_ENCODER_VALUE,
            GoBildaPinpointDriver.Register.Y_ENCODER_VALUE,
            GoBildaPinpointDriver.Register.X_POSITION,
            GoBildaPinpointDriver.Register.Y_POSITION,
            GoBildaPinpointDriver.Register.H_ORIENTATION,
            GoBildaPinpointDriver.Register.X_VELOCITY,
            GoBildaPinpointDriver.Register.Y_VELOCITY,
            GoBildaPinpointDriver.Register.H_VELOCITY,
            GoBildaPinpointDriver.Register.PITCH,
            GoBildaPinpointDriver.Register.ROLL
        );
    }

    public Follower getFollower() {
        return follower;
    }

    public ForesightConfig foresightConfig() {
        return ((Foresight) follower.algorithm()).config;
    }

    @Override
    public void periodic() {
        follower.update();
        KoalaLog.logPose2d("Drive/Pose", follower.pose().x() * .0254, follower.pose().y() * .0254, follower.pose().heading(), true);
        KoalaLog.logPose2d("Drive/PoseInch", follower.pose().x(), follower.pose().y(), Math.toDegrees(follower.pose().heading()), true);
        KoalaLog.log("Drive/IsBusy", follower.isBusy(), true);
        Mecanum mecanum = (Mecanum) follower.drivetrain;
        for (int i = 0; i < wheels.length; i++) {
            String name = WHEEL_NAMES[i];
            KoalaLog.log("Drive/" + name + "Power", mecanum.wheelPowers[i], true);
            KoalaLog.log("Drive/" + name + "Speed", wheels[i].getVelocity(), true);
        }
        logPinpoint();
    }

    private void logPinpoint() {
        KoalaLog.log("Pinpoint/Status", pinpoint.getDeviceStatus(), true);
        KoalaLog.log("Pinpoint/EncoderX", pinpoint.getEncoderX(), true);
        KoalaLog.log("Pinpoint/EncoderY", pinpoint.getEncoderY(), true);
        KoalaLog.log("Pinpoint/HeadingVelocity", pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES), true);
        KoalaLog.log("Pinpoint/LoopTime", pinpoint.getLoopTime(), true);
        KoalaLog.log("Pinpoint/Frequency", pinpoint.getFrequency(), true);
        KoalaLog.log("Pinpoint/Heading", pinpoint.getHeading(AngleUnit.DEGREES), true);
        KoalaLog.log("Pinpoint/Pitch", pinpoint.getPitch(AngleUnit.DEGREES), true);
        KoalaLog.log("Pinpoint/Roll", pinpoint.getRoll(AngleUnit.DEGREES), true);
    }



    public double cube(double num) {
        return num*num*num;
    }
    public Command manualDrive(Gamepad gamepad) {
        return run(
            () -> {
                double forward = -cube(gamepad.left_stick_y);
                double lateral = -cube(gamepad.left_stick_x);
                double turn = -cube(gamepad.right_trigger - gamepad.left_trigger) * 0.7;
                double heading = follower.pose().heading();
                DrivePowers robotPowers = new DrivePowers(forward, lateral, turn);
                logPowers("ManualDrive/RobotCentric", robotPowers);
                KoalaLog.log("ManualDrive/Heading", Math.toDegrees(heading), true);
                DrivePowers powers = ManualDrive.fieldCentric(robotPowers, heading);
                logPowers("ManualDrive/FieldCentric", powers);
                if (gamepad.a) {
                    if (vision.getClusterId() != Vision.NO_TAG && !headingLock) {
                        targetHeading = follower.pose().heading() + Math.toRadians(vision.getBearing());
                        headingLock = true;
                    }
                } else {
                    headingLock = false;
                }
                if (headingLock) {
                    powers = ManualDrive.headingLock(
                        follower,
                        foresightConfig().headingFeedback.get(),
                        powers,
                        targetHeading,
                        foresightConfig().headingBrakeCoefficients.get().x(),
                        foresightConfig().headingBrakeCoefficients.get().y(),
                        1.0);
                }
                KoalaLog.log("ManualDrive/HeadingLock", headingLock, true);
                KoalaLog.log("ManualDrive/TargetHeading", Math.toDegrees(targetHeading), true);
                logPowers("ManualDrive/Follower", powers);
                follower.manual(powers);
            }
        );
    }
    private void logPowers(String prefix, DrivePowers powers) {
        KoalaLog.log(prefix + "Forward", powers.forward(), true);
        KoalaLog.log(prefix + "Strafe", powers.strafe(), true);
        KoalaLog.log(prefix + "Turn", powers.turn(), true);
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
        KoalaLog.log("Drive/HeadingBeforeReset", Math.toDegrees(follower.pose().heading()), true);
        follower.setPose(new Pose(follower.pose().x(), follower.pose().y(), 0));
    }

    public Command follow(Path path) {
     return new FollowPathCommand(follower, path);
    }

}
