package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous(preselectTeleOp = "DriverControl")
public class LeftPark extends CommonOpMode{

    private Command auton;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees();

        Pose startPose = p.of(31.8,130.7,90);
        Pose parkPose = p.of(14.8,119.8,90);

        setStartPose(startPose);

        Path park = line(startPose, parkPose).linear(startPose, parkPose);

        auton = new SequentialCommandGroup(
                drive.follow(park)
        );
    }
    @Override
    public void preRun() {
        super.preRun();
        auton.schedule();
        KoalaLog.log("DS:autonomous", true, false);
    }

    @Override
    public void end() {
        super.end();
        KoalaLog.log("DS:autonomous", false, false);
    }
}
