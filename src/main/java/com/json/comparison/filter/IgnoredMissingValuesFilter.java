/************************************************************************
 Copyright 2019 eBay Inc.
 Author/Developer(s): Ahmad Mahagna

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **************************************************************************/
package com.json.comparison.filter;

import java.util.List;
import java.util.stream.Collectors;

import com.json.comparison.comprator.model.JsonComparatorResultImpl;
import com.json.comparison.comprator.model.Paths;
import com.json.comparison.comprator.model.api.FieldComparison;
import com.json.comparison.comprator.model.api.JsonComparatorResult;

/**
 * A IgnoreMissingValuesFilter class represents filter that remove all the 'missing values' in comparator result that match provided paths
 */
public class IgnoredMissingValuesFilter extends ComparatorFilter {

  public IgnoredMissingValuesFilter(Paths ignoredPaths) {
    super(ignoredPaths);
  }

  /**
   * Filter all the 'missing values' in the given result that match the provided paths
   * @See ComparatorFilter
   * @param jsonComparatorResult comparator result
   */
  @Override
  public JsonComparatorResult filter(JsonComparatorResult jsonComparatorResult) {

    return JsonComparatorResultImpl.builder()
        .modifiedFields(jsonComparatorResult.getModifiedFields())
        .missingFields(createMissingFieldAfterIgnoredValues(jsonComparatorResult))
        .newFields(jsonComparatorResult.getNewFields())
        .build();
  }

  /**
   * Filter all missing fields that match the ignored paths
   * @param jsonComparatorResult
   * @return all fieldComparisons exclude match ones
   */
  private List<FieldComparison> createMissingFieldAfterIgnoredValues(JsonComparatorResult jsonComparatorResult) {
   return jsonComparatorResult.getMissingFields()
        .stream()
        .filter(failure -> !isMatch(concatFullFieldPath(failure.getField(),failure.getExpected().toString())))
        .collect(Collectors.toList());
  }

}
