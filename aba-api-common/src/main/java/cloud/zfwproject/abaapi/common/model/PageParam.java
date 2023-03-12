package cloud.zfwproject.abaapi.common.model;

import cloud.zfwproject.abaapi.common.constant.CommonConstant;
import lombok.Data;

/**
 * @author 46029
 * @version 1.0
 * @description 分页请求参数
 * @date 2023/1/20 11:35
 */
@Data
public class PageParam {

    private static final Long DEFAULT_CURRENT_PAGE = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 当前页码
     */
    private Long current = DEFAULT_CURRENT_PAGE;

    /**
     * 页面大小
     */
    private Long pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
