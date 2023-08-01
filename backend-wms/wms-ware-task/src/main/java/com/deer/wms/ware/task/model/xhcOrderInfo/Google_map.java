/**
  * Copyright 2021 bejson.com 
  */
package com.deer.wms.ware.task.model.xhcOrderInfo;

import lombok.ToString;

/**
 * Auto-generated: 2021-07-13 1:20:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@ToString
public class Google_map {

    private Pov pov;
    private String pano;
    private int is_streetview;
    public void setPov(Pov pov) {
         this.pov = pov;
     }
     public Pov getPov() {
         return pov;
     }

    public void setPano(String pano) {
         this.pano = pano;
     }
     public String getPano() {
         return pano;
     }

    public void setIs_streetview(int is_streetview) {
         this.is_streetview = is_streetview;
     }
     public int getIs_streetview() {
         return is_streetview;
     }

}