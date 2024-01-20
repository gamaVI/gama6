package com.mygdx.gama6map.model;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.Date;

public class Transaction {
    private String id;
    private String address;

    private String apiId;
    private String componentType;
    private Double transactionAmountM2;
    private Double estimatedAmountM2;
    private Boolean isEstimatedAmount;
    private Double transactionSumParcelSizes;
    private Date transactionDate;
    private Double transactionAmountGross;
    private Double transactionTax;
    private Integer buildingYearBuilt;
    private Integer unitRoomCount;
    private Double unitRoomsSumSize;

    private String gpsId;
    private GPS gps;



    public static class GPS {
        private Double lat;
        private Double lng;



        public Double getLat() {
            return lat;
        }
        public Double getLng() {
            return lng;
        }

        public String getLatString() {
            return lat.toString();
        }
        public String getLngString() {
            return lng.toString();
        }

        @Override
        public String toString() {
            return  "\nSirina=" + lat +"\n"+"Dolzina=" + lng;
        }
    }
    public Transaction() {}

    public Transaction(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public Boolean getEstimatedAmount() {
        return isEstimatedAmount;
    }

    public String getApiId() {
        return apiId;
    }

    public String getComponentType() {
        return componentType;
    }

    public Double getTransactionAmountM2() {
        return transactionAmountM2;
    }

    public Double getEstimatedAmountM2() {
        return estimatedAmountM2;
    }

    public Double getTransactionSumParcelSizes() {
        return transactionSumParcelSizes;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Double getTransactionAmountGross() {
        return transactionAmountGross;
    }

    public Double getTransactionTax() {
        return transactionTax;
    }

    public Integer getBuildingYearBuilt() {
        return buildingYearBuilt;
    }

    public Integer getUnitRoomCount() {
        return unitRoomCount;
    }

    public Double getUnitRoomsSumSize() {
        return unitRoomsSumSize;
    }

    public String getGpsId() {
        return gpsId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public GPS getGps() {
        return gps;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", apiId='" + apiId + '\'' +
                ", componentType='" + componentType + '\'' +
                ", transactionAmountM2=" + transactionAmountM2 +
                ", estimatedAmountM2=" + estimatedAmountM2 +
                ", isEstimatedAmount=" + isEstimatedAmount +
                ", transactionSumParcelSizes=" + transactionSumParcelSizes +
                ", transactionDate=" + transactionDate +
                ", transactionAmountGross=" + transactionAmountGross +
                ", transactionTax=" + transactionTax +
                ", buildingYearBuilt=" + buildingYearBuilt +
                ", unitRoomCount=" + unitRoomCount +
                ", unitRoomsSumSize=" + unitRoomsSumSize +
                ", gps=" + (gps != null ? gps.toString() : null) +
                '}';
    }
}
