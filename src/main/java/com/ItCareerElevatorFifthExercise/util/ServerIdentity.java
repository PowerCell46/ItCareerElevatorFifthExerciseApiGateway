package com.ItCareerElevatorFifthExercise.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
public class ServerIdentity {

    private final Environment environment;

    private String host;
    private String port;

    public String getHost() {
        if (host == null) {
            try {
                host = InetAddress
                        .getLocalHost()
                        .getHostName();

            } catch (UnknownHostException ex) {
                host = "unknown-host";
            }
        }

        return host;
    }

    public String getPort() {
        if (port == null) { // works with random or fixed ports once server is started
            port = environment.getProperty(
                    "local.server.port",
                    environment.getProperty("server.port", "8080")
            );
        }

        return port;
    }

    public String getInstanceAddress() {
        return getHost() + ":" + getPort();
    }
}
