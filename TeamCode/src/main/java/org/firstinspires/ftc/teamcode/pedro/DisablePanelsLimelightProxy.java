package org.firstinspires.ftc.teamcode.pedro;

import androidx.annotation.Keep;

import com.bylazar.limelightproxy.LimelightProxyConfig;

/**
 * Work around the issue of both FTC Dashboard and Panels starting a proxy for the Limelight
 * HTTP ports by disabling the proxy in Panels. The Limelight views won't be available there.
 */
@Keep
public class DisablePanelsLimelightProxy extends LimelightProxyConfig {
    public DisablePanelsLimelightProxy() {
        setEnabled(false);
    }
}