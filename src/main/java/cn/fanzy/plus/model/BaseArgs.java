package cn.fanzy.plus.model;


import cn.fanzy.plus.utils.SpringUtils;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基本参数，分页查询。
 *
 * @author fanzaiyang
 * @since  2022-02-07
 */
public class BaseArgs {

    @ApiModelProperty(value = "当前页，默认：1", position = -2)
    private Integer pageNum;
    @ApiModelProperty(value = "每页条数，默认：15", position = -1)
    private Integer pageSize;

    public Integer getPageNum() {
        if (pageNum == null || pageNum < 1) {
            // 从query或posted form data中获取pageNum
            String pageNum = SpringUtils.getRequest().getParameter("pageNum");
            return NumberUtil.isNumber(pageNum) ? NumberUtil.parseInt(pageNum) : 1;
        }
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            // 从query或posted form data中获取pageSize
            String pageSize = SpringUtils.getRequest().getParameter("pageSize");
            return NumberUtil.isNumber(pageSize) ? NumberUtil.parseInt(pageSize) : 15;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
