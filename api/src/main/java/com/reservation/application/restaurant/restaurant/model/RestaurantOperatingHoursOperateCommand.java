package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.valid.BaseValidation;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursOperateCommand implements BaseValidation {
  private List<RestaurantOperatingHoursCreateCommand> createCommands;
  private List<RestaurantOperatingHoursUpdateCommand> updateCommands;
  private List<Long> deleteCommands;

  @Override
  public void validate() {
    if (!CollectionUtils.isEmpty(createCommands)) {
      createCommands.stream().forEach(RestaurantOperatingHoursCreateCommand::validate);
    }
    if (!CollectionUtils.isEmpty(updateCommands)) {
      updateCommands.stream().forEach(RestaurantOperatingHoursUpdateCommand::validate);
    }
  }
}
