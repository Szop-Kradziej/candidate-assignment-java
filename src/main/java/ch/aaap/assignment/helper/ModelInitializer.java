package ch.aaap.assignment.helper;

import ch.aaap.assignment.model.*;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelInitializer {

  private final Set<CSVPoliticalCommunity> rawPoliticalCommunities;
  private final Set<CSVPostalCommunity> rawPostalCommunities;
  private Set<Canton> cantons;
  private Set<District> districts;
  private Set<PoliticalCommunity> politicalCommunities;
  private Set<PostalCommunity> postalCommunities;

  public ModelInitializer(Set<CSVPoliticalCommunity> rawPoliticalCommunities, Set<CSVPostalCommunity> rawPostalCommunities) {
    this.rawPoliticalCommunities = rawPoliticalCommunities;
    this.rawPostalCommunities = rawPostalCommunities;
  }

  public Model init() {
    parseRawPoliticalCommunities();
    parseRawPostalCommunities();

    return new ModelImpl(cantons, districts, politicalCommunities, postalCommunities);
  }

  private void parseRawPoliticalCommunities() {
    Map<String, Canton> cantonsMap = new HashMap<>();
    Map<String, District> districtsMap = new HashMap<>();
    Map<String, PoliticalCommunity> politicalCommunitiesMap = new HashMap<>();

    for (CSVPoliticalCommunity rawPC : rawPoliticalCommunities) {
      upsertCanton(rawPC, cantonsMap);
      upsertDistrict(rawPC, districtsMap);
      insertPoliticalCommunity(rawPC, politicalCommunitiesMap);
    }

    cantons = new HashSet<>(cantonsMap.values());
    districts = new HashSet<>(districtsMap.values());
    politicalCommunities = new HashSet<>(politicalCommunitiesMap.values());
  }

  private void upsertCanton(CSVPoliticalCommunity rawPC, Map<String, Canton> cantonsMap) {
    if (cantonsMap.containsKey(rawPC.getCantonCode())) {
      Canton curr = cantonsMap.get(rawPC.getCantonCode());
      curr.getDistrictsIds().add(rawPC.getDistrictNumber());
    } else {
      Set<String> districtsIds = new HashSet<>();
      districtsIds.add(rawPC.getDistrictNumber());

      Canton newCanton = new CantonImpl(
          rawPC.getCantonCode(),
          rawPC.getCantonName(),
          districtsIds);

      cantonsMap.put(rawPC.getCantonCode(), newCanton);
    }
  }

  private void upsertDistrict(CSVPoliticalCommunity rawPC, Map<String, District> districtsMap) {
    if (districtsMap.containsKey(rawPC.getDistrictNumber())) {
      District curr = districtsMap.get(rawPC.getDistrictNumber());
      curr.getPoliticalCommunitiesIds().add(rawPC.getNumber());
    } else {
      Set<String> politicalCommunitiesIds = new HashSet<>();
      politicalCommunitiesIds.add(rawPC.getNumber());

      District newDistrict = new DistrictImpl(
          rawPC.getDistrictNumber(),
          rawPC.getDistrictName(),
          rawPC.getCantonCode(),
          politicalCommunitiesIds);

      districtsMap.put(rawPC.getDistrictNumber(), newDistrict);
    }
  }

  private void insertPoliticalCommunity(CSVPoliticalCommunity rawPC, Map<String, PoliticalCommunity> politicalCommunitiesMap) {
    if (politicalCommunitiesMap.containsKey(rawPC.getNumber())) {
      //TODO: Change to Slf4j warn
      System.out.println("The same political communities");
    } else {
      PoliticalCommunity newPoliticalCommunity = new PoliticalCommunityImpl(
          rawPC.getNumber(),
          rawPC.getName(),
          rawPC.getShortName(),
          rawPC.getLastUpdate(),
          rawPC.getDistrictNumber(),
          new HashSet<>());

      politicalCommunitiesMap.put(rawPC.getNumber(), newPoliticalCommunity);
    }
  }

  private void parseRawPostalCommunities() {
    Map<String, PostalCommunity> map = new HashMap<>();

    for (CSVPostalCommunity row : rawPostalCommunities) {
      String key = row.getZipCode() + row.getZipCodeAddition() + row.getName();

      if (map.containsKey(key)) {
        PostalCommunity curr = map.get(key);
        curr.getPoliticalCommunitiesIds().add(row.getPoliticalCommunityNumber());
      } else {
        Set<String> politicalCommunitiesIds = new HashSet<>();
        politicalCommunitiesIds.add(row.getPoliticalCommunityNumber());

        PostalCommunity newPostalCommunity = new PostalCommunityImpl(
            row.getZipCode(),
            row.getZipCodeAddition(),
            row.getName(),
            politicalCommunitiesIds);

        map.put(key, newPostalCommunity);
      }
    }

    postalCommunities = new HashSet<>(map.values());
  }
}
