package com.dsj.csp.manage.dto.response;

import com.dsj.csp.manage.dto.SupportDto;
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
    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private List<SupportDto> list;
}
