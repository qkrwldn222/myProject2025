package com.reservation.adapter.store.mapper;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class FoodStoreRequestMapperImpl implements FoodStoreRequestMapper {

  @Override
  public FoodStoreSearchCommand toStoreSearchCommand(
      String mgtNo, String bplcNm, String rdnWhlAddr, String trdStateGbn) {
    if (mgtNo == null && bplcNm == null && rdnWhlAddr == null && trdStateGbn == null) {
      return null;
    }

    FoodStoreSearchCommand foodStoreSearchCommand = new FoodStoreSearchCommand();

    if (mgtNo != null) {
      foodStoreSearchCommand.setMgtNo(Long.parseLong(mgtNo));
    }
    foodStoreSearchCommand.setBplcNm(bplcNm);
    foodStoreSearchCommand.setRdnWhlAddr(rdnWhlAddr);
    foodStoreSearchCommand.setTrdStateGbn(trdStateGbn);

    return foodStoreSearchCommand;
  }
}
