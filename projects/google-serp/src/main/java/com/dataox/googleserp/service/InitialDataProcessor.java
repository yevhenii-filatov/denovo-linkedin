package com.dataox.googleserp.service;

import com.dataox.googleserp.model.dto.InitialDataDTO;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.service.mapper.InitialDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

        if (initialDataDTOS.size() != dataAndStep.size()) {
            throw new IllegalArgumentException("Wrong JSON file: input JSON has same denovoId's");
        }

        return dataAndStep;
    }

    @Transactional
    protected InitialData getInitialData(InitialDataDTO initialDataDTO) {
        waitForInitialData(3);
        Optional<InitialData> initialData = initialDataRepository.findByDenovoId(initialDataDTO.getDenovoId());
        initialData.ifPresent(initialDataRepository::delete);
        return initialDataRepository.save(InitialDataMapper.toInitialData(initialDataDTO));
    }

    public void waitForInitialData(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
