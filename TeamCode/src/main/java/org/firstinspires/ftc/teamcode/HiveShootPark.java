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
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

@Autonomous(preselectTeleOp = "DriverControl")
public class HiveShootPark extends CommonOpMode{

    private Command auton;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees(); // add this

        Pose startPose = p.of(60,8,90);
        Pose shootPose = p.of(60,32,90);
        Pose parkPose = p.of(30,60,90);
        Pose parkControlPose = p.of(45, 20, 90);

        Follower follower = drive.getFollower();
        follower.setPose(startPose);

        Path shoot = line(startPose, shootPose);
        Path park = curve(shootPose, parkControlPose, parkPose);

        auton = new SequentialCommandGroup(
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    drive.follow(shoot),
                    new WaitCommand(4000)
                ),
                shooter.shootCommand()
            ),
            drive.follow(park)
        );
    }
    @Override
    public void preRun() {
        auton.schedule();
    }
}
