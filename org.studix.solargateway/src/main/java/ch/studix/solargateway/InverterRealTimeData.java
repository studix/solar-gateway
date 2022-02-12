package ch.studix.solargateway;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @param dayEnergy day energy in unit Wh
 * @param currentPower current power in unit W
 */
public record InverterRealTimeData(double dayEnergy, double currentPower, ZonedDateTime valueDate){

}
