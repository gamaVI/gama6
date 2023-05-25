"use client";
/* eslint-disable */
import { useMemo } from 'react';
import { Bar, AreaChart, Area, ResponsiveContainer, XAxis, YAxis } from "recharts";
import dayjs from 'dayjs';

export default function Overview({transactions}) {

  const data = useMemo(() => {
    const groupedByDay = transactions.reduce((acc, transaction) => {
    const date = dayjs(transaction.transactionDate).format('YYYY-MM-DD');

      if (!acc[date]) {
        acc[date] = {
          name: date,
          total: transaction.transactionAmountM2,
          count: 1
        };
      } else {
        acc[date].total += transaction.transactionAmountM2;
        acc[date].count += 1;
      }

      return acc;
    }, {});

    // Calculate average price per square meter
    Object.keys(groupedByDay).forEach(key => {
      groupedByDay[key].total /= groupedByDay[key].count;
    });

    // Convert to array
    return Object.values(groupedByDay);
  }, [transactions]);

  return (
    <ResponsiveContainer width="100%" height={350}>
      <AreaChart data={data}>
        <XAxis
          dataKey="name"
          stroke="#888888"
          fontSize={12}
          tickLine={false}
          axisLine={false}
        />
        <YAxis
          stroke="#888888"
          fontSize={12}
          tickLine={false}
          axisLine={false}
          tickFormatter={(value) => `€${value.toFixed(2)}`}
        />
        <Area type="monotone" dataKey="total" fill="#adfa1d" stroke="#adfa1d" radius={[4, 4, 0, 0]} />
      </AreaChart>
    </ResponsiveContainer>
  );
}
