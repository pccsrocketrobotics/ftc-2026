package org.firstinspires.ftc.teamcode.pedro;

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

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig
        (c -> {
            c.frontLeftName.set("lf");
            c.frontRightName.set("rf");
            c.backLeftName.set("lr");
            c.backRightName.set("rr");
            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
            c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        }
    );
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.887140138881413);
        c.yPodOffset.set(5.899807186577264);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.21486210327626354);
                Controller secondaryTranslationalForward = Controller.proportional(0.07938580593944225);
                Controller primaryTranslationalLateral = Controller.proportional(0.317921558158599);
                Controller secondaryTranslationalLateral = Controller.proportional(0.11746352071911331);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01718296416267656));
                c.brake.set(Controller.proportionalFeedforward(0.014605519538275076));

                c.headingFeedback.set(Controller.proportional(3.1267903081731365));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.060564310461774434, 0.006255486416539302));

                c.linearBrakeCoefficients.set(Matrix.diag(0.1287770937375441, 0.06381686638120239));
                c.quadraticBrakeCoefficients.set(Matrix.diag(7.345777578235545E-5, 0.001791723139090643));

                c.maxAchievableForwardVelocity.set(62.41620713640121);
                c.maxAchievableStrafeVelocity.set(52.12027533791403);
                c.naturalForwardDeceleration.set(46.29031392482986);
                c.naturalStrafeDeceleration.set(71.40121223501954);
            }
    );
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}