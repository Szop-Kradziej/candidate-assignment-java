package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PoliticalCommunityImpl implements PoliticalCommunity {

  private final String number;
  private final String name;
  private final String shortName;
  private final LocalDate lastUpdate;
  private final String districtId;
}
