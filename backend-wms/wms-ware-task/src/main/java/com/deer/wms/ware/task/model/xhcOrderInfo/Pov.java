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
public class Pov {

    private int heading;
    private int pitch;
    private int zoom;
    public void setHeading(int heading) {
         this.heading = heading;
     }
     public int getHeading() {
         return heading;
     }

    public void setPitch(int pitch) {
         this.pitch = pitch;
     }
     public int getPitch() {
         return pitch;
     }

    public void setZoom(int zoom) {
         this.zoom = zoom;
     }
     public int getZoom() {
         return zoom;
     }

}