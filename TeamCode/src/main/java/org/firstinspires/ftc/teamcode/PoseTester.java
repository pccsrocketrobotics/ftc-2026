package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.DashboardCore;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;


import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Utility
public class PoseTester extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        PoseFactory p = PoseFactory.degrees();
        Follower follower = Constants.create(hardwareMap);
        Pose startPose = p.of(6.7,6.4,0);
        follower.setPose(startPose);
        if(opModeIsActive()) {
            while (opModeIsActive()) {
                Pose currentPose = follower.pose();
                telemetry.addData("poseX", currentPose.x());
                telemetry.addData("poseY", currentPose.y());
                telemetry.addData("poseHeading", Math.toDegrees(currentPose.heading()));
                telemetry.update();
                follower.update();
            }
        }
    }
}
