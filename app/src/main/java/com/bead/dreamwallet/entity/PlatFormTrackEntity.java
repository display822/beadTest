package com.bead.dreamwallet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DOY on 2017/7/24.
 */
public class PlatFormTrackEntity implements Serializable {




        private String quato;
        private String logo;
        private String platformId;
        private String label;
        private String abbreviation;
        private String accessTime;

        public String getQuato() {
            return quato;
        }

        public void setQuato(String quato) {
            this.quato = quato;
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

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public void setAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(String accessTime) {
            this.accessTime = accessTime;
        }
}
