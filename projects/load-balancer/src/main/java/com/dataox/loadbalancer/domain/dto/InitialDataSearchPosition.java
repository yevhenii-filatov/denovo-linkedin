package com.dataox.loadbalancer.domain.dto;

import com.dataox.loadbalancer.domain.entities.InitialData;
import lombok.Value;

@Value
public class InitialDataSearchPosition {
    InitialData initialData;
    int searchPosition;
}
