package com.deer.wms.quartz.task;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.base.system.model.Item.TongBuSkuCriteria;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.constant.QueueKey;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.sync.pojo.SyncParam.LockMode;
import com.deer.wms.project.root.util.QueueTool;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.xhcOrderInfo.Order;
import com.deer.wms.ware.task.service.CpfrTaskService;
import com.deer.wms.ware.task.service.So2AsnConnector;
import com.deer.wms.ware.task.service.SoMasterService;

import lombok.extern.slf4j.Slf4j;

@Component("deerTask")
@Slf4j
public class DeerTask {

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private CpfrTaskService cpfrTaskService;

    @Autowired
    private DeerTaskExtension that;

    @WorkLogger
    public void tongbuSKU() throws IOException {
        TongBuSkuCriteria criteria = new TongBuSkuCriteria("001","17243");
        itemInfoService.xhcSku(criteria);
    }

    @WorkLogger
    public void cpfr() {
        /*
         * 缺货订单+补货配置 补货配置 取消补货任务
         */
        cpfrTaskService.create();
    }

    @Deprecated
    @WorkLogger
    public void xhcOrder() {
        log.error("订单处理任务不再使用认识任务触发，请关闭此定时任务：xhcOrder");
    }

    @PostConstruct
    public void init() {
        log.error("记录到DeerTaskBean构建，项目启动中。");
        that.xhcOrder(1);
        that.xhcOrder(2);
    }

    @PreDestroy
    public void down() {
        log.error("记录到DeerTaskBean销毁，项目停机中。");
    }
}

@Component
@Slf4j
class DeerTaskExtension {

    @Autowired
    private DeerTaskExtension2 that;

    @Autowired
    private QueueTool queue;

    @Async
    public void xhcOrder(int threadNo) {
        Long lastErrorTime = 0L;
        int errorNumber = 0;
        for (;;) {
            try {
                // 每次队列操作单独获取队列信息，防止Queue对象偶发不可用导致的消费任务崩溃
                BlockingQueue<Order> xhcOrderBlockingQueue = queue.getBlockingQueue(QueueKey.PQ_WEBHOOK_XHC_ORDER);
                Order xhcOrder = xhcOrderBlockingQueue.take();
                log.info(threadNo + "当前开始处理订单" + xhcOrder.getId() + " " + xhcOrder.getStatus() + " PQ_WEBHOOK_XHC_ORDER 队列剩余：" + xhcOrderBlockingQueue.size());
                if (xhcOrder != null) {
                    that.xhcOrderForTimes(xhcOrder);
                }
            } catch (org.redisson.RedissonShutdownException e) {
                log.error(threadNo + "xhcOrder停止运行，Redisson已停止：", e);
                break;
            } catch (InterruptedException e) {
                log.error(threadNo + "xhcOrder出现线程中断信号，已停止运行");
                break;
            } catch (Throwable t) {
                log.error(threadNo + "xhcOrder出现异常，记录并继续执行：", t);
                Long now = System.currentTimeMillis();
                if (now - lastErrorTime < 2000) {
                    errorNumber++;
                } else {
                    lastErrorTime = now;
                    errorNumber = 0;
                }
                if (errorNumber >= 10) {
                    log.error(threadNo + "xhcOrder：2秒内收集到10个以上的异常，任务已停止！");
                    break;
                }
            }
        }
    }
}

@Component
@Slf4j
class DeerTaskExtension2 {
    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private So2AsnConnector so2AsnConnector;

    @Synchronized(lockMode = LockMode.FAIR, lockName = LockName.XHC_ORDER, value = "#order.id", timeout = 600)
    public boolean xhcOrderForTimes(Order order) {
        long begin = System.currentTimeMillis();
        for (int i = 1; i <= 10; i++) {
            log.info("开始处理订单（第" + i + "次尝试），订单号：" + order.getId() + ",状态：" + order.getStatus() + ",店铺：" + order.getShop_id());
            try {
                this.xhcOrderByOne(order);
                log.info("订单第" + i + "次处理完成，耗时" + (System.currentTimeMillis() - begin) + "，单号：" + order.getId());
                return true;
            } catch (ServiceException e) {
                if(e.getCode() != null && e.getCode() == CommonCode.SYSTEM_ERROR) {
                    log.info("订单第" + i + "次处理完成，业务未处理，单号：" + order.getId(), e);
                }else {
                    log.info("订单第" + i + "次处理完成，业务未处理，单号：" + order.getId(), e);
                }
                return true;
            } catch (Exception e) {
                log.error("订单第" + i + "次处理失败！单号：" + order.getId(), e);
                continue;
            }
        }
        log.error("订单" + order.getId() + "失败10次，耗时" + (System.currentTimeMillis() - begin) + "，不再处理：" + JSON.toJSONString(order));
        return false;
    }

    /**
     * process one order
     * 
     * @param order
     * @return
     */
    public void xhcOrderByOne(Order order) {
        /*
         *
         * 当前状态为-1 WMS处理为整张订单取消： a.判断当前订单号已存入WMS，且已存储订单状态为10、11时，WMS通知库管人员将商品取回货架。b. 判断当前订单号已存入WMS，且已存储订单状态为15 12 18 20时，WMS处理为退货单，走出库商品退货流程。
         *
         * 当前状态为1、0时，WMS系统不处理。 当前状态为10且订单号在WMS系统中不存在时，WMS处理为接收到一张新订单。 当前状态为10、11这2种情况且订单号在WMS已存在下，则WMS需要根据当前库内订单和最新订单内Item比对出增加、删除的商品和商品下单数量变更，并通知库管人员拣货、或取回货架。 当前状态为12、18、20，WMS处理为退货单，走出库商品退货流程。
         */
        Integer shop_daily_count = order.getShop_daily_count();

        if (shop_daily_count == 0) {
            // 0是补单 不发货
            log.info("补单不处理，订单号：" + order.getId());
            return;
        }
        int status = order.getStatus();
        if (status == 0 || status == 1) {
            // 不处理
            log.info("状态为0或1，不处理订单，订单号：" + order.getId());
            return;
        } else if (status == 10) {
            // 新订单创建或者订单修改
            soMasterService.getOrder11(order);
            log.info("成功处理订单，订单号：" + order.getId());
            return;
        } else if (status == 11) {
            // 部分取消
            // 未分配 修改订单
            // 已经分配未拣 修改订单 修改拣货任务
            // 已经拣货 修改订单 多余的货生成还货任务
            // 已经拣货，已经打印单据 多余的货生成还货任务 重新打印单据（标志） 0-没有 1-有 2-重新打过
            // 发运时校验是否 重新打印单据（标志）
            log.info("状态为11 处理订单，订单号：" + order.getId() + " 金额：" + order.getGrand_total());
            soMasterService.getOrder11(order);
            log.info("成功处理订单，订单号：" + order.getId());
            return;
        }
        // 退货入库：状态12已取货/18已送达/20已完成。detail数量相比当前有下调。
        else if (status == 12 || status == 18 || status == 20) {
            log.info("状态为12已取货/18已送达/20已完成，订单号：" + order.getId() + "判断SKU数量并生成、修改退货单");
            String returnAsnNumber = so2AsnConnector.autoSaveReturnAsn(order);
            if (returnAsnNumber == null) {
                log.info("成功处理订单并生成、修改退货单，订单号：" + order.getId() + "，无退货信息");
            }
            log.info("成功处理订单并生成、修改退货单，订单号：" + order.getId() + "，退货入库单号：" + returnAsnNumber);
            return;
        }
        // 退货
        else if (status == -1) {
            log.info("状态为-1 处理订单，订单号：" + order.getId());
            Long orderId = order.getId();
            SoMaster soMaster = soMasterService.findBy("allotBillNo", orderId.toString());
            if (soMaster != null) {
                soMasterService.cancel(soMaster, null);
            }
            log.info("成功处理订单，订单号：" + order.getId());
            return;

        } else {
            log.info("状态为" + status + ",未处理订单号：" + order.getId());
            return;
        }
    }
}
