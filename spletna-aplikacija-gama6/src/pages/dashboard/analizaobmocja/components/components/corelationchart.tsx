import { parse } from "path";
import React from "react";
import {
  ScatterChart,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  Scatter,
  ResponsiveContainer,
} from "recharts";

function ParcelSizePriceScatterChart({ transactions }) {
  let tempData = {};

  for (const transaction of transactions) {
    transaction.unitRoomsSumSize = parseInt(transaction.unitRoomsSumSize);
    // Check if size exists in tempData
    if (tempData[transaction.unitRoomsSumSize]) {
      // If it does, add the current price to the total and increment the count
      tempData[transaction.unitRoomsSumSize].totalPrice +=
        transaction.transactionAmountGross;
      tempData[transaction.unitRoomsSumSize].count += 1;
    } else {
      // If it doesn't, create a new entry with the current price and count 1
      tempData[transaction.unitRoomsSumSize] = {
        totalPrice: transaction.transactionAmountGross,
        count: 1,
      };
    }
  }

  let data = [];

  // Calculate the average price for each size and store it in the final data array
  for (const size in tempData) {
    data.push({
      parcelSize: parseInt(size),
      averagePrice: tempData[size].totalPrice / tempData[size].count,
    });
  }

  // sort data by parcel size
  data = data.sort((a, b) => a.parcelSize - b.parcelSize);

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
        <XAxis dataKey="parcelSize" name="Parcel Size" />
        <YAxis dataKey="averagePrice" name="Average price" />
        <Tooltip cursor={{ strokeDasharray: "3 3" }} />
        <Scatter data={data} fill="#8884d8" />
      </ScatterChart>
    </ResponsiveContainer>
  );
}
export default ParcelSizePriceScatterChart;
