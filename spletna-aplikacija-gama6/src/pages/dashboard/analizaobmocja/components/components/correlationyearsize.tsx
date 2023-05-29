import React from "react";
import {
  ScatterChart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Scatter,
  ResponsiveContainer,
} from "recharts";

function YearBuiltSizeScatterChart({ transactions }) {
  let tempData = {};

  for (const transaction of transactions) {
    // Check if building year exists in tempData
    if (tempData[transaction.buildingYearBuilt]) {
      // If it does, add the current size to the total and increment the count
      tempData[transaction.buildingYearBuilt].totalSize +=
        transaction.unitRoomsSumSize;
      tempData[transaction.buildingYearBuilt].count += 1;
    } else {
      // If it doesn't, create a new entry with the current size and count 1
      tempData[transaction.buildingYearBuilt] = {
        totalSize: transaction.unitRoomsSumSize,
        count: 1,
      };
    }
  }

  let data = [];

  // Calculate the average size for each year and store it in the final data array
  for (const year in tempData) {
    data.push({
      yearBuilt: parseInt(year),
      averageSize: tempData[year].totalSize / tempData[year].count,
    });
  }

  // Sort data by yearBuilt
  data = data.sort((a, b) => a.yearBuilt - b.yearBuilt);

  return (
    <ResponsiveContainer width="100%" height={400}>
      <ScatterChart
        width={500}
        height={400}
        margin={{
          top: 20,
          right: 20,
          bottom: 20,
          left: 20,
        }}
      >
        <CartesianGrid stroke="#f5f5f5" />
        <XAxis dataKey="yearBuilt" name="Year Built" />
        <YAxis dataKey="averageSize" name="Average size" />
        <Tooltip cursor={{ strokeDasharray: "3 3" }} />
        <Scatter data={data} fill="#8884d8" />
      </ScatterChart>
    </ResponsiveContainer>
  );
}

export default YearBuiltSizeScatterChart;
