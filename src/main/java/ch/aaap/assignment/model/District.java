package ch.aaap.assignment.model;

import java.util.Set;

public interface District {

  public String getNumber();

  public String getName();

  public String getCantonId();

  public Set<String> getPoliticalCommunitiesIds();
}
