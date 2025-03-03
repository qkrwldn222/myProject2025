package com.reservation.adapter.store.mapper;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class StoreRequestMapperImpl implements FoodStoreRequestMapper {

  @Override
  public FoodStoreSearchCommand toStoreSearchCommand(
      String mgtNo, String bplcNm, String rdnWhlAddr) {
    if (mgtNo == null && bplcNm == null && rdnWhlAddr == null) {
      return null;
    }

    FoodStoreSearchCommand storeSearchCommand = new FoodStoreSearchCommand();

    if (mgtNo != null) {
      storeSearchCommand.setMgtNo(Long.parseLong(mgtNo));
    }
    storeSearchCommand.setBplcNm(bplcNm);
    storeSearchCommand.setRdnWhlAddr(rdnWhlAddr);

    return storeSearchCommand;
  }
}
