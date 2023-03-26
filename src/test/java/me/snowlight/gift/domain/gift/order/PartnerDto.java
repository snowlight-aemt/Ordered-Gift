package me.snowlight.gift.domain.gift.order;

import org.apache.commons.lang3.RandomStringUtils;

public class PartnerDto {
    public static class RegisterPartner {
        private String partnerName = RandomStringUtils.randomAlphanumeric(5);
        private String businessNo = RandomStringUtils.randomAlphanumeric(10);
        private String email = RandomStringUtils.randomAlphanumeric(5) + "@gmail.com";

        public String getPartnerName() {
            return partnerName;
        }

        public String getBusinessNo() {
            return businessNo;
        }

        public String getEmail() {
            return email;
        }

        public void setPartnerName(String partnerName) {
            this.partnerName = partnerName;
        }

        public void setBusinessNo(String businessNo) {
            this.businessNo = businessNo;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
