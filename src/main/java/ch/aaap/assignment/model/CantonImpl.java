package ch.aaap.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CantonImpl implements Canton {

  private final String code;
  private final String name;
  private final Set<String> districtsIds;
}
