package com.mygdx.gama6map.model;

import com.badlogic.gdx.scenes.scene2d.ui.List;

public class Transaction {
    private String id;
    private String apiId;
    private String componentType;
    private String address;
    private Float transactionAmountM2;
    private Float estimatedAmountM2;
    private Boolean isEstimatedAmount;
    // Assuming Gps class is defined elsewhere
    private Gps gps;
    private List<String> transactionItemsList;
    private Integer transactionSumParcelSizes;
    private String transactionDate; // You might want to use a date type
    private Float transactionAmountGross;
    private Float transactionTax;
    private Integer buildingYearBuilt;
    private Integer unitRoomCount;
    private Float unitRoomsSumSize;
    private String unitRooms;
    private String gpsId;


    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", apiId='" + apiId + '\'' +
                ", componentType='" + componentType + '\'' +
                ", address='" + address + '\'' +
                ", transactionAmountM2=" + transactionAmountM2 +
                ", estimatedAmountM2=" + estimatedAmountM2 +
                ", isEstimatedAmount=" + isEstimatedAmount +
                ", gps=" + gps +
                ", transactionItemsList=" + transactionItemsList +
                ", transactionSumParcelSizes=" + transactionSumParcelSizes +
                ", transactionDate='" + transactionDate + '\'' +
                ", transactionAmountGross=" + transactionAmountGross +
                ", transactionTax=" + transactionTax +
                ", buildingYearBuilt=" + buildingYearBuilt +
                ", unitRoomCount=" + unitRoomCount +
                ", unitRoomsSumSize=" + unitRoomsSumSize +
                ", unitRooms='" + unitRooms + '\'' +
                ", gpsId='" + gpsId + '\'' +
                '}';
    }

}


