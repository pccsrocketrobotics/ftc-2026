package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Autonomous(group = "Setup")
public class CalibrateHeading extends CommonOpMode {
    private static final int BOTPOSE_PIPELINE = 0;
    private static final int SAMPLES = 30;

    private Limelight3A limelight;
    private double sumSin = 0, sumCos = 0;
    private int count = 0;

    @Override
    public void initialize() {
        super.initialize();
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(BOTPOSE_PIPELINE);
    }

    @Override
    public void run() {
        super.run();
        Follower f = drive.getFollower();
        LLResult r = limelight.getLatestResult();
        boolean valid = r != null && r.isValid() && r.getPipelineIndex() == BOTPOSE_PIPELINE;
        double yaw = valid ? r.getBotpose().getOrientation().getYaw(AngleUnit.RADIANS) : Double.NaN;

        if (valid && count < SAMPLES) {
            sumSin += Math.sin(yaw);
            sumCos += Math.cos(yaw);
            count++;
            if (count == SAMPLES) {
                f.setPose(new Pose(f.pose().x(), f.pose().y(), Math.atan2(sumSin, sumCos)));
                blackboard.put(HEADING_CALIBRATED, true);
            }
        }

        telemetry.addData("Status", "Calibrating %d / %d", count, SAMPLES);
        telemetry.addData("Robot heading", "%.2f°", Math.toDegrees(f.pose().heading()));
        telemetry.addData("Camera heading", valid ? String.format("%.2f°", Math.toDegrees(yaw)) : "no tags");
        telemetry.update();
    }
}
