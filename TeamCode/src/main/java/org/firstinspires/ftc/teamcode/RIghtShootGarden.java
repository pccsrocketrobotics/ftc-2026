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
public class RIghtShootGarden extends CommonOpMode{

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

        setStartPose(startPose);

        ForesightConfig c = drive.foresightConfig();
        Path shoot = line(startPose, shootPose).linear(startPose, shootPose);
        Path garden = curve(shootPose, gardenControlPose, gardenPose).linear(gardenControlPose, gardenPose).with(
            c.maxVelocityConstraint.at(20.0)
        );
            Path shoot2 = line(gardenPose, shootPose).linear(gardenPose, shootPose);
            Path park = curve(shootPose, parkControlPose, parkPose).linear(shootPose,parkPose);

        Vision.DetectHive detect = vision.detectHive();
        auton = new SequentialCommandGroup(
                cheeseS.instantInSideCommand(),
                shooter.instantShootCommand(() -> Shooter.SPEED2 -20),
                drive.follow(shoot),
                gate.gateOpenCommand(),
                transfer.inCommand().withTimeout(3000 ),
                gate.gateCloseCommand(),
                transfer.instantInCommand(),
                intake.inatantInCommand(),
                drive.follow(garden).withTimeout(5000),
                drive.follow(shoot2),
                new ConditionalCommand(gate.gateOpenCommand().withTimeout(3000), gate.gateCloseCommand(), detect::isHiveUp),
                gate.gateCloseCommand(),
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
