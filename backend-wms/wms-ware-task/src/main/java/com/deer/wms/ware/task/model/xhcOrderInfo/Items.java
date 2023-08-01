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
public class Items {

    private long order_id;
    private long revision_id;
    private int qty;
    private int status;
    private int parent_item_id;
    private String row_total;
    private String new_row_total;
    private String note;
    private List<String> sub_items;
    private int row_price;
    private Revision revision;
    public void setOrder_id(long order_id) {
         this.order_id = order_id;
     }
     public long getOrder_id() {
         return order_id;
     }

    public void setRevision_id(long revision_id) {
         this.revision_id = revision_id;
     }
     public long getRevision_id() {
         return revision_id;
     }

    public void setQty(int qty) {
         this.qty = qty;
     }
     public int getQty() {
         return qty;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setParent_item_id(int parent_item_id) {
         this.parent_item_id = parent_item_id;
     }
     public int getParent_item_id() {
         return parent_item_id;
     }

    public void setRow_total(String row_total) {
         this.row_total = row_total;
     }
     public String getRow_total() {
         return row_total;
     }

    public void setNew_row_total(String new_row_total) {
         this.new_row_total = new_row_total;
     }
     public String getNew_row_total() {
         return new_row_total;
     }

    public void setNote(String note) {
         this.note = note;
     }
     public String getNote() {
         return note;
     }

    public void setSub_items(List<String> sub_items) {
         this.sub_items = sub_items;
     }
     public List<String> getSub_items() {
         return sub_items;
     }

    public void setRow_price(int row_price) {
         this.row_price = row_price;
     }
     public int getRow_price() {
         return row_price;
     }

    public void setRevision(Revision revision) {
         this.revision = revision;
     }
     public Revision getRevision() {
         return revision;
     }

}