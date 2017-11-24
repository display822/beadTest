package com.dreamwallet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DOY on 2017/7/21.
 */
public class LoansEntity implements Serializable {


        private String label;
        private String dailyInterestRate;
        private String sortName;
        private String lendSpeed;
        private String quato;
        private String maxCycle;
        private String logo;
        private String platformId;
        private String abbreviation;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            label = label;
        }

        public String getDailyInterestRate() {
            return dailyInterestRate;
        }

        public void setDailyInterestRate(String dailyInterestRate) {
            this.dailyInterestRate = dailyInterestRate;
        }

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public String getLendSpeed() {
            return lendSpeed;
        }

        public void setLendSpeed(String lendSpeed) {
            this.lendSpeed = lendSpeed;
        }

        public String getQuato() {
            return quato;
        }

        public void setQuato(String quato) {
            this.quato = quato;
        }

        public String getMaxCycle() {
            return maxCycle;
        }

        public void setMaxCycle(String maxCycle) {
            this.maxCycle = maxCycle;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getPlatformId() {
            return platformId;
        }

        public void setPlatformId(String platformId) {
            this.platformId = platformId;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

}
