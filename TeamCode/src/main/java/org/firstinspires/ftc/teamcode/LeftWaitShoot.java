package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SelectCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import Ori.Coval.Logging.Logger.KoalaLog;

@Autonomous(preselectTeleOp = "DriverControl")
public class LeftWaitShoot extends CommonOpMode{

    private Command auton;
    private int startDelay = 10;

    @Override
    public void initialize() {
        super.initialize();

        PoseFactory p = PoseFactory.degrees();

        Pose startPose = p.of(57,130.7,90);
        Pose shootPose = p.of(55.2,25.3,-96);//TUNE!!
        Pose parkPose = p.of(12.5,117,90);

        Follower follower = drive.getFollower();
        follower.setPose(startPose);

        Path shoot = line(startPose, shootPose).linear(startPose, shootPose);
        Path park = curve(shootPose, parkPose).linear(shootPose,parkPose);

        auton = new SequentialCommandGroup(
                new ParallelDeadlineGroup(
                        new SequentialCommandGroup(
                                drive.follow(shoot),
                                new SelectCommand(() -> new WaitCommand(startDelay * 1000)),
                                gate.gateOpenCommand(),
                                new WaitCommand(1500),
                                transfer.inCommand().withTimeout(3000),
                                gate.gateCloseCommand()
                        ),
                        shooter.shootCommand(() -> Shooter.SPEED2)
                ),
                drive.follow(park)
        );
    }

    @Override
    public void initialize_loop() {
        super.initialize_loop();
        telemetry.addData("Delay at start of auton", startDelay);
        if (gamepad1.dpadUpWasPressed()) {
            startDelay++;
        } else if (gamepad1.dpadDownWasPressed()) {
            startDelay--;
            if (startDelay < 0) {
                startDelay = 0;
            }
        }
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