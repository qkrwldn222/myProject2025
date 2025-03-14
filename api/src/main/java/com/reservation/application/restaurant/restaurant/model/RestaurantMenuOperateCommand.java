package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.valid.BaseValidation;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
public class RestaurantMenuOperateCommand implements BaseValidation {
  private List<RestaurantMenuCreateCommand> createCommands;
  private List<RestaurantMenuUpdateCommand> updateCommands;
  private List<Long> deleteCommands;

  @Override
  public void validate() {
    if (!CollectionUtils.isEmpty(createCommands)) {
      createCommands.stream().forEach(RestaurantMenuCreateCommand::validate);
    }
    if (!CollectionUtils.isEmpty(updateCommands)) {
      createCommands.stream().forEach(RestaurantMenuCreateCommand::validate);
    }
  }
}
