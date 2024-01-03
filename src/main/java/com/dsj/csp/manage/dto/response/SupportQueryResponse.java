package com.dsj.csp.manage.dto.response;

import com.dsj.csp.manage.dto.SupportDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/29 16:12
 */
@Data
@Accessors(chain = true)
public class SupportQueryResponse implements Serializable {
    @Schema(description = "页码")
    private Integer pageNum;

    @Schema(description = "每页数量")
    private Integer pageSize;

    @Schema(description = "总数")
    private Long total;

    @Schema(description = "列表")
    private List<SupportDto> list;
}
