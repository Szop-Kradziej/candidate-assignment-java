package ch.aaap.assignment;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ch.aaap.assignment.helper.ModelInitializer;
import ch.aaap.assignment.model.*;
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

  /**
   * Reads the CSVs and initializes a in memory model
   */
  private void initModel() {
    Set<CSVPoliticalCommunity> politicalCommunities = CSVUtil.getPoliticalCommunities();
    Set<CSVPostalCommunity> postalCommunities = CSVUtil.getPostalCommunities();

    model = new ModelInitializer(politicalCommunities, postalCommunities).init();
  }

  /**
   * @return model
   */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
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
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    if (!isProperCantonCode(cantonCode)) {
      throw new IllegalArgumentException();
    }

    return model.getCantons().stream()
        .filter(it -> it.getCode().equals(cantonCode))
        .map(Canton::getDistrictsIds)
        .mapToLong(Collection::size)
        .sum();
  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {
    if (!isProperDistrictNumber(districtNumber)) {
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
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    if (!isProperZipCode(zipCode)) {
      return new HashSet<>();
    }

    Set<String> politicalCommunitiesIds = model.getPostalCommunities().stream()
        .filter(it -> it.getZipCode().equals(zipCode))
        .map(PostalCommunity::getPoliticalCommunitiesIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    Set<String> districtsIds = model.getPoliticalCommunities().stream()
        .filter(it -> politicalCommunitiesIds.contains(it.getNumber()))
        .map(PoliticalCommunity::getDistrictId)
        .collect(Collectors.toSet());

    return model.getDistricts().stream()
        .filter(it -> districtsIds.contains(it.getNumber()))
        .map(District::getName)
        .collect(Collectors.toSet());
  }

  private boolean isProperZipCode(String zipCode) {
    return model.getPostalCommunities().stream()
        .anyMatch(it -> it.getZipCode().equals(zipCode));
  }

  /**
   * @param postalCommunityName of postal community
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(
      String postalCommunityName) {
    if (!isProperPostalCommunityName(postalCommunityName)) {
      throw new IllegalArgumentException();
    }

    Set<String> politicalCommunitiesIds = model.getPostalCommunities().stream()
        .filter(it -> it.getName().equals(postalCommunityName))
        .map(PostalCommunity::getPoliticalCommunitiesIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return model.getPoliticalCommunities().stream()
        .filter(it -> politicalCommunitiesIds.contains(it.getNumber()))
        .max(Comparator.comparing(PoliticalCommunity::getLastUpdate))
        .map(PoliticalCommunity::getLastUpdate)
        .orElseThrow(IllegalStateException::new);
  }

  private boolean isProperPostalCommunityName(String postalCommunityName) {
    return model.getPostalCommunities().stream()
        .anyMatch(it -> it.getName().equals(postalCommunityName));
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
    Set<String> politicalCommunitiesWithPostalCommunities = model.getPostalCommunities().stream()
        .map(PostalCommunity::getPoliticalCommunitiesIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return model.getPoliticalCommunities().stream()
        .filter(it -> !politicalCommunitiesWithPostalCommunities.contains(it.getNumber()))
        .count();
  }
}
