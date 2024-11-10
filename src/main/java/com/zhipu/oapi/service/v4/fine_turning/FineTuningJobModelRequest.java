package com.zhipu.oapi.service.v4.fine_turning;


import com.zhipu.oapi.core.model.ClientRequest;
import com.zhipu.oapi.service.v4.batchs.BatchCreateParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FineTuningJobModelRequest implements ClientRequest<Map<String, Object>> {

    private String fineTunedModel;

    @Override
    public Map<String, Object> getOptions() {
        Map<String,Object> map = new HashMap<>();
        map.put("fine_tuned_model", fineTunedModel);
        return map;
    }
}
