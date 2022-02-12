package ch.studix.solargateway.connectors.inverters.fronius;

import ch.studix.solargateway.InverterConnector;
import ch.studix.solargateway.InverterRealTimeData;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Dependent
public class FroniusInverterConnector implements InverterConnector {

   private ExecutorService executorService = Executors.newCachedThreadPool();
    private Client client;
    private String basePath = "/solar_api/v1/";

    @ConfigProperty(name = "inverter.hostname")
    private String hostname;

    public FroniusInverterConnector(){
        client = ClientBuilder.newBuilder()
                .executorService(executorService)
                .build();
    }

    @Override
    public Optional<InverterRealTimeData> getRealtimeData() {
        try(Response response = client.target(hostname + basePath + "GetInverterRealtimeData.cgi")
                .queryParam("Scope", "System").request().get()){
            if(response.getStatusInfo().getStatusCode() == Response.Status.OK.getStatusCode()){
                JsonArray payload = response.readEntity(JsonArray.class);
                JsonObject data = payload.get(0).asJsonObject().getJsonObject("Body").getJsonObject("Data");
                int dayEnergy = getIntValue(data, "DAY_ENERGY");
                int pac = getIntValue(data, "PAC");
                ZonedDateTime timestamp = ZonedDateTime.parse(payload.get(0).asJsonObject().getJsonObject("Head").getString("Timestamp"));
                return Optional.of(new InverterRealTimeData(dayEnergy, pac, timestamp));
            }
            else{
                // TODO logging
            }
        }
        return Optional.empty();
    }

    private int getIntValue(JsonObject data, String attributeName) {
        return data.getJsonObject(attributeName).getJsonObject("Values").getJsonNumber("1").intValue();
    }
}
