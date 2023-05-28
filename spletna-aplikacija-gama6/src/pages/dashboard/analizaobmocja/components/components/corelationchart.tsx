import React from 'react';
import { ScatterChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Scatter, ResponsiveContainer } from 'recharts';

function ParcelSizePriceScatterChart({ transactions }) {
  let  data = transactions.map(transaction => ({
    parcelSize: transaction.unitRoomsSumSize,
    price: transaction.transactionAmountGross,
  }));

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
      <YAxis dataKey="price" name="Price" />
      <Tooltip cursor={{ strokeDasharray: '3 3' }} />
      <Scatter data={data} fill="#8884d8" />
    </ScatterChart>
    </ResponsiveContainer>
  );
}

export default ParcelSizePriceScatterChart;
