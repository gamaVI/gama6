"use client";
import React from "react";
import {
  ScatterChart,
  Scatter,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const CustomTooltip = ({ active, payload }) => {
  if (active) {
    return (
      <div
        className="custom-tooltip"
        style={{
          backgroundColor: "#ffff",
          padding: "5px",
          border: "1px solid #cccc",
          color: "#000000",
          borderRadius: "5px",
        }}
      >
        <label>{`Year Built: ${payload[0].payload.buildingYearBuilt}, Average price per M2: ${payload[0].payload.averagePricePerM2}`}</label>
      </div>
    );
  }
  return null;
};

export default function TransactionScatterPlot({ transactions }) {
  const data = [];

  for (const transaction of transactions) {
    // Check if building year is in data array
    const index = data.findIndex(
      (item) => item.buildingYearBuilt === transaction.buildingYearBuilt
    );

    // If building year is not in data array, add a new entry
    if (index === -1) {
      data.push({
        buildingYearBuilt: transaction.buildingYearBuilt,
        totalEstimatedAmountM2: transaction.estimatedAmountM2,
        count: 1,
      });
    } else {
      // If building year is already in the data array, update the total estimated amount and the count
      data[index].totalEstimatedAmountM2 += transaction.estimatedAmountM2;
      data[index].count += 1;
    }
  }

  // Calculate the average price per m2 for each year
  for (const item of data) {
    item.averagePricePerM2 = item.totalEstimatedAmountM2 / item.count;
    delete item.totalEstimatedAmountM2;
    delete item.count;
  }

  data.sort((a, b) => a.buildingYearBuilt - b.buildingYearBuilt);

  return (
    <ResponsiveContainer width="100%" height={400}>
      <ScatterChart
        width={400}
        height={400}
        margin={{
          top: 20,
          right: 20,
          bottom: 20,
          left: 20,
        }}
      >
        <CartesianGrid />
        <XAxis dataKey="buildingYearBuilt" name="Year Built" unit="yr" />
        <YAxis dataKey="averagePricePerM2" name="Price per M2" unit="€" />
        <Tooltip content={<CustomTooltip />} />
        <Scatter data={data} fill="#8884d8" />
      </ScatterChart>
    </ResponsiveContainer>
  );
}
