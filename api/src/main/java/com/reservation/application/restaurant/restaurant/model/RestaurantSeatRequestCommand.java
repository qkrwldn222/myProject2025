package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.valid.BaseValidation;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@Data
@NoArgsConstructor
public class RestaurantSeatRequestCommand implements BaseValidation {
  private List<RestaurantSeatCreateCommand> createCommands;
  private List<RestaurantSeatUpdateCommand> updateCommands;
  private List<Long> deleteCommands;

  @Override
  public void validate() {
    if (!CollectionUtils.isEmpty(createCommands)) {
      createCommands.stream().forEach(RestaurantSeatCreateCommand::validate);
    }
    if (!CollectionUtils.isEmpty(updateCommands)) {
      updateCommands.stream().forEach(RestaurantSeatUpdateCommand::validate);
    }
  }
}
