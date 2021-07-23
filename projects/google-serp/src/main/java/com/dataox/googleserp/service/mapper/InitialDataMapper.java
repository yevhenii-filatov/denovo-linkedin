package com.dataox.googleserp.service.mapper;

import com.dataox.googleserp.model.dto.InitialDataDTO;
import com.dataox.googleserp.model.entity.InitialData;
import lombok.experimental.UtilityClass;

/**
 * @author Dmitriy Lysko
 * @since 19/04/2021
 */
@UtilityClass
public class InitialDataMapper {

    public InitialData toInitialData(InitialDataDTO initialDataDTO) {
        InitialData initialData = new InitialData();
        initialData.setDenovoId(initialDataDTO.getDenovoId());
        initialData.setFirstName(initialDataDTO.getFirstName());
        initialData.setMiddleName(initialDataDTO.getMiddleName());
        initialData.setLastName(initialDataDTO.getLastName());
        initialData.setFirmName(initialDataDTO.getFirmName());
        initialData.setLinkedinUrl(initialDataDTO.getLinkedinUrl());
        initialData.setSearched(false);
        initialData.setNoResults(false);
        return initialData;
    }
}
