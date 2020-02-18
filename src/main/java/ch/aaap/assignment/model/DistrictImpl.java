package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class DistrictImpl implements District {

  private final String number;
  private final String name;
  private final String cantonId;
  private final Set<String> politicalCommunitiesIds;
}
