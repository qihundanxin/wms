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
public class Communication_language {

    private boolean chinese;
    private boolean english;
    private boolean cantonese;
    public void setChinese(boolean chinese) {
         this.chinese = chinese;
     }
     public boolean getChinese() {
         return chinese;
     }

    public void setEnglish(boolean english) {
         this.english = english;
     }
     public boolean getEnglish() {
         return english;
     }

    public void setCantonese(boolean cantonese) {
         this.cantonese = cantonese;
     }
     public boolean getCantonese() {
         return cantonese;
     }

}