package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous(preselectTeleOp = "DriverControl")
public class HiveShootGarden extends CommonOpMode{

    private Command auton;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees();

        Pose startPose = p.of(54,10.2,-90);
        Pose shootPose = p.of(55.2,25.3,-96);
        Pose gardenControlPose = p.of(30 ,5 , 188);
        Pose gardenPose = p.of(12 ,5 ,188);
        Pose parkPose = p.of(10.6,96.6,-90);
        Pose parkControlPose = p.of(25.7,39.7,-90);

        Follower follower = drive.getFollower();
        follower.setPose(startPose);

        ForesightConfig c = drive.foresightConfig();
        Path shoot = line(startPose, shootPose).linear(startPose, shootPose);
        Path garden = curve(shootPose, gardenControlPose, gardenPose).linear(gardenControlPose, gardenPose).with(
            c.maxVelocityConstraint.at(20.0)
        );
        Path shoot2 = line(gardenPose, shootPose).linear(gardenPose, shootPose);
        Path park = curve(shootPose, parkControlPose, parkPose).linear(shootPose,parkPose);

        auton = new SequentialCommandGroup(
            new ParallelDeadlineGroup(
                new SequentialCommandGroup(
                    drive.follow(shoot),
                    gate.gateOpenCommand(),
                    new WaitCommand(1500),
                    transfer.inCommand().withTimeout(7000 ),
                    gate.gateCloseCommand(),
                    new ParallelDeadlineGroup(
                        new SequentialCommandGroup(
                            new ParallelDeadlineGroup(
                                new WaitCommand(5000),
                                drive.follow(garden)
                            ),
                            drive.follow(shoot2),
                            gate.gateOpenCommand(),
                            new WaitCommand(7000),
                            gate.gateCloseCommand()
                        ),
                        transfer.inCommand(),
                        intake.inCommand()
                    )
                ),
                shooter.shootCommand(() -> Shooter.SPEED2),
                cheeseS.inSideCommand()
            ),
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
