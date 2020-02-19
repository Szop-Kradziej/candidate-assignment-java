package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class PostalCommunityImpl implements PostalCommunity {

  private final String zipCode;
  private final String zipCodeAddition;
  private final String name;
  private final Set<String> politicalCommunitiesIds;
}
