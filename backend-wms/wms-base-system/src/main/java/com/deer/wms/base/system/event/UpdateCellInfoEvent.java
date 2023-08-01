package com.deer.wms.base.system.event;

import org.springframework.context.ApplicationEvent;

import com.deer.wms.base.system.model.Cell.CellInfo;

/**
 * 修改货位信息事件
 * 
 * @author xuesinuo
 *
 */
public class UpdateCellInfoEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private CellInfo source;
    private CellInfo target;

    public UpdateCellInfoEvent(CellInfo source, CellInfo target) {
        super(target);
        this.source = source;
        this.target = target;
    }

    /**
     * @return 更新前的对象
     */
    public CellInfo getSource() {
        return source;
    }

    /**
     * @return 更新后的结果
     */
    public CellInfo getTarget() {
        return target;
    }
}
