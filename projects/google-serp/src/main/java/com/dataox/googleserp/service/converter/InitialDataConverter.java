package com.dataox.googleserp.service.converter;

import com.dataox.googleserp.model.dto.InitialDataDTO;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.service.mapper.InitialDataMapper;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 19/04/2021
 */
@UtilityClass
public class InitialDataConverter {

    public Map<InitialData, Integer> toMap(List<InitialDataDTO> initialDataDTOList) {
        Map<InitialData, Integer> dataAndStep = new HashMap<>();
        for (InitialDataDTO initialDataDTO : initialDataDTOList) {
            InitialData initialData = InitialDataMapper.toInitialData(initialDataDTO);
            Integer searchStep = initialDataDTO.getSearchStep();
            dataAndStep.put(initialData, searchStep);
        }
        return dataAndStep;
    }
}
