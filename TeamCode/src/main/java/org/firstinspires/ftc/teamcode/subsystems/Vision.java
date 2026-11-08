package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.pedro.Constants.Alliance;

import Ori.Coval.Logging.Logger.KoalaLog;

public class Vision extends SubsystemBase {
    private static final int CLUSTER_PIPELINE = 1;
    public static double CAMERA_PITCH = 45;

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
    private double roll = 0;
    private double latency = 0;

    public Vision(HardwareMap hardwareMap) {
        // Keep the robot running without vision if the Limelight isn't configured
        limelight = hardwareMap.tryGet(Limelight3A.class, "limelight");
        if (limelight != null) {
            limelight.pipelineSwitch(CLUSTER_PIPELINE);
            limelight.start();
        }
        KoalaLog.log("Vision/Connected", limelight != null, true);
    }

    public void setAlliance(Alliance alliance) {
        if (limelight == null) return;
        limelight.updatePythonInputs(new double[]{1, alliance.ordinal(), CAMERA_PITCH, 0, 0, 0, 0, 0});
        KoalaLog.log("Vision/Alliance", alliance.ordinal(), true);
    }

    @Override
    public void periodic() {
        LLResult result = (limelight != null) ? limelight.getLatestResult() : null;
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
            latency = result.getCaptureLatency() + result.getTargetingLatency();

            //        Math.abs(py[ROLL]) < 90 ? "right-side up" : "upside down");
        } else {
            clusterId = NO_TAG;
            numTags = 0;
            range = 0;
            bearing = 0;
            height = 0;
            elevation = 0;
            stickerPitch = 0;
            roll = 0;
            latency = 0;
        }
        KoalaLog.log("Vision/ClusterId", clusterId, true);
        KoalaLog.log("Vision/NumTags", numTags, true);
        KoalaLog.log("Vision/Range", range, true);
        KoalaLog.log("Vision/Bearing", bearing, true);
        KoalaLog.log("Vision/Height", height, true);
        KoalaLog.log("Vision/Elevation", elevation, true);
        KoalaLog.log("Vision/StickerPitch", stickerPitch, true);
        KoalaLog.log("Vision/Roll", roll, true);
        KoalaLog.log("Vision/Latency", latency, true);
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

    public double getLatency() {
        return latency;
    }

}

