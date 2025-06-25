package com.genx.dto.request;

import com.genx.enums.ESampleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KitCodeRequest {
    private String kitCode;
    private ESampleType sampleType;
}
