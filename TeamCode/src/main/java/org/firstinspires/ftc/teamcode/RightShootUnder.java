package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous(preselectTeleOp = "DriverControl")
public class RightShootUnder extends CommonOpMode{

    private Command auton;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees();

        Pose startPose = p.of(54,10.2,-90);
        Pose shootPose = p.of(55.2,25.3,-96);
        Pose gardenControlPose = p.of(30 ,5 , 200);
        Pose gardenPose = p.of(12 ,5 ,188);
        Pose parkPose = p.of(12.6,96.6,-90);
        Pose parkControlPose = p.of(25.7,33.7,-90);

        Pose otherSidePose = p.of(64,110,90);
        Pose otherSidePoseC1 = p.of(65,20,90);
        Pose otherSidePoseC2 = p.of(64,42,90);

        Pose hive2Pose = p.of(46,118.5,110);
        Pose park2Pose = p.of(12 ,97 , 90);
        Pose park2PoseC1 = p.of(29,115 , 90);
        Pose park2PoseC2 = p.of(53,50 , 90);
        Pose park2PoseC3 = p.of(7,75 , 90);

        setStartPose(startPose);

        ForesightConfig c = drive.foresightConfig();
        Path shoot = line(startPose, shootPose).linear(startPose, shootPose);
        Path garden = curve(shootPose, gardenControlPose, gardenPose).linear(gardenControlPose, gardenPose).with(
            c.maxVelocityConstraint.at(20.0)
        );
        Path shoot2 = line(gardenPose, shootPose).linear(gardenPose, shootPose);
        Path park = curve(shootPose, parkControlPose, parkPose).linear(shootPose, parkPose);

        Path changeSides = curve(shootPose, otherSidePoseC1, otherSidePoseC2, otherSidePose).constant(otherSidePose);
        Path hive2 = line(otherSidePose, hive2Pose).linear(otherSidePose , hive2Pose);
        Path park2 = curve(hive2Pose, park2PoseC1, park2PoseC2, park2PoseC3, park2Pose).constant(park2Pose);

        Command finishTipOne = new SequentialCommandGroup(
            gate.gateOpenCommand(),
            new WaitCommand(3000),
            gate.gateCloseCommand(),
            transfer.instantStopCommand(),
            intake.instantStopCommand(),
            shooter.instantStopCommand(),
            drive.follow(park)
        );
        Command startTipTwo = new SequentialCommandGroup(
            intake.instantOutCommand(),
            drive.follow(changeSides),
            drive.follow(hive2),
            gate.gateOpenCommand(),
            new WaitCommand(3000),
            gate.gateCloseCommand(),
            transfer.instantStopCommand(),
            intake.instantStopCommand(),
            shooter.instantStopCommand(),
            drive.follow(park2)
        );

        Vision.DetectHive detect = vision.detectHive();

        auton = new SequentialCommandGroup(
            cheeseS.instantInSideCommand(),
            shooter.instantShootCommand(() -> Shooter.SPEED2 -20),
            drive.follow(shoot),
            gate.gateOpenCommand(),
            new WaitCommand(1500),
            transfer.inCommand().withTimeout(3000 ),
            gate.gateCloseCommand(),
            transfer.instantInCommand(),
            intake.inatantInCommand(),
            new ParallelDeadlineGroup(
                new WaitCommand(5000),
                drive.follow(garden)
            ),
            drive.follow(shoot2),
            detect,
            new ConditionalCommand(finishTipOne, startTipTwo, detect::isHiveUp)
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
