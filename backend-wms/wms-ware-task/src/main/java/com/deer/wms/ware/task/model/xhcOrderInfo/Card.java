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
public class Card {

    private String brand;
    private Checks checks;
    private String country;
    private int exp_month;
    private int exp_year;
    private String fingerprint;
    private String funding;
    private String installments;
    private int last4;
    private String network;
    private String three_d_secure;
    private String wallet;
    public void setBrand(String brand) {
         this.brand = brand;
     }
     public String getBrand() {
         return brand;
     }

    public void setChecks(Checks checks) {
         this.checks = checks;
     }
     public Checks getChecks() {
         return checks;
     }

    public void setCountry(String country) {
         this.country = country;
     }
     public String getCountry() {
         return country;
     }

    public void setExp_month(int exp_month) {
         this.exp_month = exp_month;
     }
     public int getExp_month() {
         return exp_month;
     }

    public void setExp_year(int exp_year) {
         this.exp_year = exp_year;
     }
     public int getExp_year() {
         return exp_year;
     }

    public void setFingerprint(String fingerprint) {
         this.fingerprint = fingerprint;
     }
     public String getFingerprint() {
         return fingerprint;
     }

    public void setFunding(String funding) {
         this.funding = funding;
     }
     public String getFunding() {
         return funding;
     }

    public void setInstallments(String installments) {
         this.installments = installments;
     }
     public String getInstallments() {
         return installments;
     }

    public void setLast4(int last4) {
         this.last4 = last4;
     }
     public int getLast4() {
         return last4;
     }

    public void setNetwork(String network) {
         this.network = network;
     }
     public String getNetwork() {
         return network;
     }

    public void setThree_d_secure(String three_d_secure) {
         this.three_d_secure = three_d_secure;
     }
     public String getThree_d_secure() {
         return three_d_secure;
     }

    public void setWallet(String wallet) {
         this.wallet = wallet;
     }
     public String getWallet() {
         return wallet;
     }

}