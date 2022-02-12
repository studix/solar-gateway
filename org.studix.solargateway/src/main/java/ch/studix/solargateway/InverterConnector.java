package ch.studix.solargateway;

import java.util.Optional;

public interface InverterConnector {
    Optional<InverterRealTimeData> getRealtimeData();
}
