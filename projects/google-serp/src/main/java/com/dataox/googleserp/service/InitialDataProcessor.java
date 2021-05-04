package com.dataox.googleserp.service;

import com.dataox.googleserp.model.dto.InitialDataDTO;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.service.mapper.InitialDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dmitriy Lysko
 * @since 20/04/2021
 */
@Service
@RequiredArgsConstructor
public class InitialDataProcessor {

    private final InitialDataRepository initialDataRepository;

    public Map<InitialData, Integer> processInitialData(List<InitialDataDTO> initialDataDTOS) {
        Map<InitialData, Integer> dataAndStep = new HashMap<>();
        for (InitialDataDTO initialDataDTO : initialDataDTOS) {
            InitialData initialData = getInitialData(initialDataDTO);
            dataAndStep.put(initialData, initialDataDTO.getSearchStep());
        }
        return dataAndStep;
    }

    @Transactional
    protected InitialData getInitialData(InitialDataDTO initialDataDTO) {
        Optional<InitialData> initialData = initialDataRepository.findByDenovoId(initialDataDTO.getDenovoId());
        return initialData.orElseGet(() -> initialDataRepository.save(InitialDataMapper.toInitialData(initialDataDTO)));
    }
}
