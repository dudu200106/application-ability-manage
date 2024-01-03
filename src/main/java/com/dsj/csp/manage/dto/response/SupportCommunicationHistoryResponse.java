package com.dsj.csp.manage.dto.response;

import com.dsj.csp.manage.dto.SupportCommunicationDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:10
 */
@Data
@Accessors(chain = true)
public class SupportCommunicationHistoryResponse implements Serializable {
    private Long appId;

    private Boolean refresh;

    private List<SupportCommunicationDto> communicationList;
}
