package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous(preselectTeleOp = "DriverControl")
public class HiveShootPark extends CommonOpMode{

    private Command auton;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees(); // add this

        Pose startPose = p.of(60,8,90);
        Pose shootPose = p.of(60,20,90);
        Pose parkPose = p.of(12,90,90);
        Pose parkControlPose = p.of(30, 20, 90);

        Follower follower = drive.getFollower();
        follower.setPose(startPose);

        Path shoot = line(startPose, shootPose).linear(startPose, shootPose);
        Path park = curve(shootPose, parkControlPose, parkPose).linear(shootPose,parkPose);

        auton = new SequentialCommandGroup(
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    drive.follow(shoot),
                    gate.gateOpenCommand(),
                    transfer.inCommand().withTimeout(8000),
                    gate.gateCloseCommand()
                ),
                shooter.shootCommand(() -> Shooter.SPEED2)
            ),
            drive.follow(park)
        );
    }
    @Override
    public void preRun() {
        auton.schedule();
        KoalaLog.start();
    }
}
