"use strict";
import { useMemo } from "react";
import {
  AreaChart,
  Area,
  ResponsiveContainer,
  XAxis,
  YAxis,
  Tooltip
} from "recharts";
import dayjs from "dayjs";
import { eachDayOfInterval, format } from "date-fns";

function calculateMovingAverage(data, windowSize) {
  const result = [];
  const halfWindowSize = Math.floor(windowSize / 2);
  for (let i = halfWindowSize; i < data.length - halfWindowSize; i++) {
    const window = data.slice(i - halfWindowSize, i + halfWindowSize + 1);
    const average = window.reduce((acc, point) => acc + point.total, 0) / window.length;
    result.push(average);
  }
  return result;
}


export default function Overview({ transactions }) {
  const data = useMemo(() => {
    transactions.sort((a, b) => new Date(a.transactionDate) - new Date(b.transactionDate));
    const startDate = transactions[0].transactionDate;
    const endDate = transactions[transactions.length - 1].transactionDate;
    const range = eachDayOfInterval({
      start: new Date(startDate),
      end: new Date(endDate)
    });

    let previousTotal = transactions[0].estimatedAmountM2;
    const dailyTotals = transactions.reduce((acc, transaction) => {
      const date = dayjs(transaction.transactionDate).format("YYYY-MM-DD");
      if (acc[date]) {
        acc[date].total += transaction.estimatedAmountM2;
        acc[date].count += 1;
      } else {
        acc[date] = {
          name: date,
          total: transaction.estimatedAmountM2,
          count: 1
        };
        previousTotal = transaction.estimatedAmountM2;
      }
      return acc;
    }, {});

    range.forEach((date) => {
      const formattedDate = format(date, "yyyy-MM-dd");
      if (!dailyTotals[formattedDate]) {
        dailyTotals[formattedDate] = {
          name: formattedDate,
          total: previousTotal,
          count: 0
        };
      } else {
        previousTotal = dailyTotals[formattedDate].total / dailyTotals[formattedDate].count;
      }
    });

    let dailyTotalsArray = Object.values(dailyTotals).sort((a, b) => new Date(a.name) - new Date(b.name));
    let movingAverages = calculateMovingAverage(dailyTotalsArray,30);

    dailyTotalsArray = dailyTotalsArray.map((item, index) => {
      const movingAverage = movingAverages[index - Math.floor(14 / 2)] ;
      return {
        ...item,
        movingAverage,
      };
    });
    

    return dailyTotalsArray;
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
        <Area
          type="monotone"
          dataKey="total"
          stroke="#4285F460"
          fill=""
        />
        <Area
          type="monotone"
          dataKey="movingAverage"
          stroke="#adfa1d"
          fill=""
        />
        <Tooltip />
      </AreaChart>
    </ResponsiveContainer>
  );
}
