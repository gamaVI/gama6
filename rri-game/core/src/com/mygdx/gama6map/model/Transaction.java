package com.mygdx.gama6map.model;

import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.Date;

public class Transaction {
    private String id;
    private String address;
    // Add other fields
    private String apiId;
    private String componentType;
    private Double transactionAmountM2;
    private Double estimatedAmountM2;
    private Boolean isEstimatedAmount;
    //private List<TransactionItem> transactionItemsList; // Assuming TransactionItem is another class
    private Double transactionSumParcelSizes;
    private Date transactionDate; // Assuming standard java.util.Date
    private Double transactionAmountGross;
    private Double transactionTax;
    private Integer buildingYearBuilt;
    private Integer unitRoomCount;
    private Double unitRoomsSumSize;
    //private List<Room> unitRooms; // Assuming Room is another class
    private String gpsId;
    private GPS gps;
    // Constructor, getters, and setters


    public static class GPS {
        private Double lat;
        private Double lng;

        // Getters and setters for lat and lng


        public Double getLat() {
            return lat;
        }
        public Double getLng() {
            return lng;
        }

        @Override
        public String toString() {
            return "GPS{" +
                    "lat=" + lat +
                    ", lng=" + lng +
                    '}';
        }
    }
    public Transaction() {}

    public Transaction(String id, String address) {
        this.id = id;
        this.address = address;
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

    public void setAddress(String address) {
        this.address = address;
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
