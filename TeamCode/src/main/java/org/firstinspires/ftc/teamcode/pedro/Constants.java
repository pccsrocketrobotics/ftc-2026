package org.firstinspires.ftc.teamcode.pedro;

import android.os.Build;
import android.util.Log;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.CommonOpMode;

public class Constants {

    public enum Alliance {
        ANY,
        BLUE,
        RED
    }
    public static String PRACTICE_BOT_SERIAL = "a991cf9f16920959";

    public static MecanumConfig drivetrainConfig = new MecanumConfig
        (c -> {
            if (isPracticeBot()) {
                c.frontLeftName.set("lf");
                c.frontRightName.set("rf");
                c.backLeftName.set("lr");
                c.backRightName.set("rr");
                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
            } else {
                c.frontLeftName.set("lf");
                c.frontRightName.set("rf");
                c.backLeftName.set("lr");
                c.backRightName.set("rr");
                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
            }

        }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        if (isPracticeBot()) {
            c.name.set("pinpoint");
            c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            c.xPodOffset.set(0.610705661022757);
            c.yPodOffset.set(-8.764261561115896);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        } else {
            c.name.set("pinpoint");
            c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            c.xPodOffset.set(0.887140138881413);
            c.yPodOffset.set(5.899807186577264);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        }
        c.resetMode.set(PinpointLocalizer.ResetMode.RESET_AND_RECALIBRATE_IMU);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                if (isPracticeBot()) {
                    Controller primaryTranslationalForward = Controller.proportional(0.23262862803332646);
                    Controller secondaryTranslationalForward = Controller.proportional(0.08595006210689227);
                    Controller primaryTranslationalLateral = Controller.proportional(0.31333027870011454);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.11576716564044989);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.010797934233429852));
                    c.brake.set(Controller.proportionalFeedforward(0.009178244098415373));

                    c.headingFeedback.set(Controller.proportional(5.0473394685378645));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06501055633299058, 0.0051027599626544025));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.11559719835766356, 0.06431994946604934));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(0.0010047319469216024, 0.0017828212857865003));

                    c.maxAchievableForwardVelocity.set(79.68685256493126);
                    c.maxAchievableStrafeVelocity.set(62.31774699302131);
                    c.naturalForwardDeceleration.set(33.860201047363674);
                    c.naturalStrafeDeceleration.set(72.79937360104523);
                } else {

                    Controller primaryTranslationalForward = Controller.proportional(0.33538662264385316);
                    Controller secondaryTranslationalForward = Controller.proportional(0.12391639537129678);
                    Controller primaryTranslationalLateral = Controller.proportional(0.5790563268301009);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.21394584009370515);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.013404393356336166));
                    c.brake.set(Controller.proportionalFeedforward(0.01139373435288574));

                    c.headingFeedback.set(Controller.proportional(3.1267903081731365));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.060564310461774434, 0.006255486416539302));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.12927567813008664, 0.08744660262213126));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(0.0011333198781023203, 0.00167989182137901));

                    c.maxAchievableForwardVelocity.set(74.27073219888578);
                    c.maxAchievableStrafeVelocity.set(58.98486847745488);
                    c.naturalForwardDeceleration.set(37.14684168410849);
                    c.naturalStrafeDeceleration.set(82.77633731598101);
                }
            }
    );
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
    public static boolean isPracticeBot() {
        return false;
//        Log.i("robotSerial", Build.SERIAL);
//        return Build.SERIAL.equals(PRACTICE_BOT_SERIAL);
    }
}