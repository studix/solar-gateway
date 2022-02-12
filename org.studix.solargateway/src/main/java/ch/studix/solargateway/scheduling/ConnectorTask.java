package ch.studix.solargateway.scheduling;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import ch.studix.solargateway.InverterConnector;
import ch.studix.solargateway.InverterRealTimeData;
import io.quarkus.scheduler.Scheduled;

import java.util.Optional;

@ApplicationScoped
public class ConnectorTask {

    @Inject
    Logger log;
    private Instance<InverterConnector> connectorInstance;

    public ConnectorTask(Instance<InverterConnector> connectorInstance){

        this.connectorInstance = connectorInstance;
    }
    @Scheduled(every="10s")
     void getInverterData() {
        connectorInstance.select().forEach(this::fetchRealtimedata);
    }

    private void fetchRealtimedata(InverterConnector inverterConnector){
        Optional<InverterRealTimeData> realtimeData = inverterConnector.getRealtimeData();
        if(realtimeData.isPresent()){
            log.info(realtimeData.get());
        }
    }
}
