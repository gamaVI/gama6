import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

function TransactionHistogram({ transactions }) {
  const maxTransactionAmount = Math.max(...transactions.map(t => t.transactionAmountGross));
  const numberOfBins = 20;
  const binSize = 50000;
  const counts = Array.from({ length: numberOfBins }, () => 0);
  transactions.forEach(transaction => {
    const binIndex = Math.floor(transaction.transactionAmountGross / binSize);
    if (binIndex < counts.length) {
      counts[binIndex]++;
    }
  });

  const data = counts.map((count, i) => ({ 
    name: `${i * binSize}-${(i + 1) * binSize - 1}`, 
    count 
  }));
  
  return (
    <ResponsiveContainer width="100%" height={400}>
      <BarChart
        data={data}
        margin={{
          top: 5,
          right: 30,
          left: 20,
          bottom: 5,
        }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="count" fill="#699918" />
      </BarChart>
    </ResponsiveContainer>
  );
}

export default TransactionHistogram;
