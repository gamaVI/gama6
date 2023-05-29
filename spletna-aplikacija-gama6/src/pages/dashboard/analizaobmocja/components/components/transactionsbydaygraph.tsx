import { useMemo } from 'react';
import { BarChart, Bar, ResponsiveContainer, XAxis, YAxis,Tooltip } from "recharts";
import dayjs from 'dayjs';

export default function Overview({ transactions, dateFrom, dateTo }) {


  const data = useMemo(() => {
    // Generate a list of dates within the date range
    const dateList = [];
    let currentDate = dayjs(dateFrom);
    const startDate = dayjs(dateFrom);
    const endDate = dayjs(dateTo);
    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      dateList.push({
        name: currentDate.format('YYYY-MM-DD'),
        total: 0
      });
      currentDate = currentDate.add(1, 'day');
    }

    // iterate over transactions and check if they have the same date as the dateList
    transactions.forEach(transaction => {
      const date = dayjs(transaction.transactionDate).format('YYYY-MM-DD');
      const dateIndex = dateList.findIndex(dateObj => dateObj.name === date);
      if (dateIndex !== -1) {
        dateList[dateIndex].total += 1;
      }
    }
    );

    return dateList;
  }, [transactions, dateFrom, dateTo]);

  return (
    <ResponsiveContainer width="100%" height={350}>
      <BarChart data={data}>
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
        />
        <Bar dataKey="total" fill="#adfa1d" />
        <Tooltip />
      </BarChart>
    </ResponsiveContainer>
  );
}
