package ch.aaap.assignment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import ch.aaap.assignment.helper.ModelInitializer;
import ch.aaap.assignment.model.Canton;
import ch.aaap.assignment.model.District;
import ch.aaap.assignment.model.Model;
import ch.aaap.assignment.raw.CSVPoliticalCommunity;
import ch.aaap.assignment.raw.CSVPostalCommunity;
import ch.aaap.assignment.raw.CSVUtil;

public class Application {

  private Model model = null;

  public Application() {
    initModel();
  }

  public static void main(String[] args) {
    new Application();
  }

  /** Reads the CSVs and initializes a in memory model */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();

    model = new ModelInitializer(politicalCommunities, postalCommunities).init();
  }
  /** @return model */
  public Model getModel() {
    return model;
  }

  /**
   * @param canton code of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    if (!isProperCantonCode(cantonCode)) {
      throw new IllegalArgumentException();
    }

    Set<String> districtsIds = model.getCantons().stream()
        .filter(it -> it.getCode().equals(cantonCode))
        .map(Canton::getDistrictsIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return model.getDistricts().stream()
        .filter(it -> districtsIds.contains(it.getNumber()))
        .map(District::getPoliticalCommunitiesIds)
        .mapToLong(Collection::size)
        .sum();
  }

  private boolean isProperCantonCode(String cantonCode) {
    return model.getCantons().stream()
        .anyMatch(it -> it.getCode().equals(cantonCode));
  }

  /**
   * @param canton code of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    if(!isProperCantonCode(cantonCode)) {
      throw new IllegalArgumentException();
    }

    return model.getCantons().stream()
        .filter(it -> it.getCode().equals(cantonCode))
        .map(Canton::getDistrictsIds)
        .mapToLong(Collection::size)
        .sum();
  }

  /**
   * @param district number of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    if(!isProperDistrictNumber(districtNumber)) {
      throw new IllegalArgumentException();
    }

    return model.getDistricts().stream()
        .filter(it -> it.getNumber().equals(districtNumber))
        .map(District::getPoliticalCommunitiesIds)
        .mapToLong(Collection::size)
        .sum();
  }

  private boolean isProperDistrictNumber(String districtNumber) {
    return model.getDistricts().stream()
        .anyMatch(it -> it.getNumber().equals(districtNumber));
  }

  /**
   * @param zip code 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * @param postal community name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    return model.getCantons().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    // TODO implementation
    throw new RuntimeException("Not yet implemented");
  }
}
