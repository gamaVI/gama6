import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

function TransactionHistogram({ transactions }) {
  // Get maximum transaction amount
  const maxTransactionAmount = Math.max(...transactions.map(t => t.transactionAmountGross));
  
  // Define number of bins for histogram
  const numberOfBins = 10;
  
  // Define bin size based on max transaction amount
  const binSize = 100000;

  // Initialize counts
  const counts = Array.from({ length: numberOfBins }, () => 0);
  
  // Count transactions for each bin
  transactions.forEach(transaction => {
    const binIndex = Math.floor(transaction.transactionAmountGross / binSize);
    if (binIndex < counts.length) {
      counts[binIndex]++;
    }
  });

  // Prepare data for Recharts
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
