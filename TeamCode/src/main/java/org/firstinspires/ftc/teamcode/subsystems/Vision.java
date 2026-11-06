package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.pedro.Constants.Alliance;

public class Vision extends SubsystemBase {
    private static final int CLUSTER_PIPELINE = 1;
    public static double CAMERA_PITCH = 30;

    public static final int NO_TAG = -1;
    private static final int CLUSTER_ID = 0, NUM_TAGS = 1, RANGE = 2, BEARING = 3,
            HEIGHT = 4, ELEVATION = 5, STICKER_PITCH = 6, ROLL = 7;
    private Limelight3A limelight;
    private int clusterId = NO_TAG;
    private int numTags = 0;
    private double range = 0;
    private double bearing = 0;
    private double height = 0;
    private double elevation = 0;
    private double stickerPitch = 0;
    private double roll = 7;

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(CLUSTER_PIPELINE);
        limelight.start();
    }

    public void setAlliance(Alliance alliance) {
        limelight.updatePythonInputs(new double[]{alliance.ordinal(), CAMERA_PITCH, 0, 0, 0, 0, 0, 0});
    }

    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();
        double[] py = (result != null) ? result.getPythonOutput() : null;

        if (py != null && py.length >= 8 && py[CLUSTER_ID] >= 0) {
            clusterId = (int) py[CLUSTER_ID];
            numTags = (int) py[NUM_TAGS];
            range = py[RANGE];
            bearing = py[BEARING];
            height = py[HEIGHT];
            elevation = py[ELEVATION];
            stickerPitch = py[STICKER_PITCH];
            roll = py[ROLL];

            //        Math.abs(py[ROLL]) < 90 ? "right-side up" : "upside down");
        } else {
            clusterId = NO_TAG;
        }
    }

    public int getClusterId() {
        return clusterId;
    }
    public int getNumTags() {
        return numTags;
    }
    public double getRange() {
        return range;
    }

    public double getBearing() {
        return bearing;
    }

    public double getHeight() {
        return height;
    }

    public double getElevation() {
        return elevation;
    }

    public double getStickerPitch() {
        return stickerPitch;
    }

    public double getRoll() {
        return roll;
    }

}

