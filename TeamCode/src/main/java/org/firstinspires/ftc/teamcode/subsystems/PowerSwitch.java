package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import Ori.Coval.Logging.Logger.KoalaLog;

public class PowerSwitch extends SubsystemBase {
    public static double FLOODGATE_MAX_VOLTS = 3.3;
    public static double FLOODGATE_MAX_AMPS = 80;
    public static long VOLTAGE_PERIOD_MS = 250;

    private final AnalogInput floodgate;
    private final VoltageSensor voltageSensor;
    private long lastVoltageRead = 0;

    public PowerSwitch(HardwareMap hardwareMap) {
        floodgate = hardwareMap.tryGet(AnalogInput.class, "floodgate");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
    }

    @Override
    public void periodic() {
        if (floodgate != null) {
            double volts = floodgate.getVoltage();
            KoalaLog.log("Power/FloodgateVolts", volts, true);
            KoalaLog.log("Power/SwitchCurrent", volts / FLOODGATE_MAX_VOLTS * FLOODGATE_MAX_AMPS, true);
        }
        long now = System.currentTimeMillis();
        if (now - lastVoltageRead >= VOLTAGE_PERIOD_MS) {
            lastVoltageRead = now;
            KoalaLog.log("Power/Voltage", voltageSensor.getVoltage(), true);
        }
    }
}
