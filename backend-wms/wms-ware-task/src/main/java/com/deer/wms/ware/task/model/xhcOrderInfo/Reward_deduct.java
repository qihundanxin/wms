/**
  * Copyright 2022 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;
import java.util.List;

import lombok.ToString;

/**
 * Auto-generated: 2022-03-03 15:27:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Reward_deduct {

    private List<Integer> rating_reward_amount;
    private int rating_deduct_amount;
    private int late_deduct_amount;
    private int qd_late_deduct_amount;
    public void setRating_reward_amount(List<Integer> rating_reward_amount) {
         this.rating_reward_amount = rating_reward_amount;
     }
     public List<Integer> getRating_reward_amount() {
         return rating_reward_amount;
     }

    public void setRating_deduct_amount(int rating_deduct_amount) {
         this.rating_deduct_amount = rating_deduct_amount;
     }
     public int getRating_deduct_amount() {
         return rating_deduct_amount;
     }

    public void setLate_deduct_amount(int late_deduct_amount) {
         this.late_deduct_amount = late_deduct_amount;
     }
     public int getLate_deduct_amount() {
         return late_deduct_amount;
     }

    public void setQd_late_deduct_amount(int qd_late_deduct_amount) {
         this.qd_late_deduct_amount = qd_late_deduct_amount;
     }
     public int getQd_late_deduct_amount() {
         return qd_late_deduct_amount;
     }

}