import React from 'react';
import { ScatterChart, CartesianGrid, XAxis, YAxis, Tooltip, Scatter, ResponsiveContainer } from 'recharts';

function YearBuiltSizeScatterChart({ transactions }) {
  let data = transactions.map(transaction => ({
    yearBuilt: transaction.buildingYearBuilt,
    size: transaction.unitRoomsSumSize,
  }));

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
        <YAxis dataKey="size" name="Size" />
        <Tooltip cursor={{ strokeDasharray: '3 3' }} />
        <Scatter data={data} fill="#8884d8" />
      </ScatterChart>
    </ResponsiveContainer>
  );
}

export default YearBuiltSizeScatterChart;
