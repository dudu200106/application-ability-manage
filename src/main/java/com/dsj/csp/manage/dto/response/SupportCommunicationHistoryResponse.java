package com.dsj.csp.manage.dto.response;

import com.dsj.csp.manage.dto.SupportCommunicationDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "应用ID")
    private Long appId;

    @Schema(description = "是否刷新")
    private Boolean refresh;

    @Schema(description = "工单沟通记录")
    private List<SupportCommunicationDto> communicationList;
}
