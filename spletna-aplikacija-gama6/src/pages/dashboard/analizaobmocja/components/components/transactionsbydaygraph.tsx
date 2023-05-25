import { useMemo } from 'react';
import { BarChart, Bar, ResponsiveContainer, XAxis, YAxis } from "recharts";
import dayjs from 'dayjs';

export default function Overview({ transactions, dateFrom, dateTo }) {
  const startDate = dayjs(dateFrom);
  const endDate = dayjs(dateTo);

  const data = useMemo(() => {
    // Generate a list of dates within the date range
    const dateList = [];
    let currentDate = startDate;
    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      dateList.push(currentDate.format('YYYY-MM-DD'));
      currentDate = currentDate.add(1, 'day');
    }

    // Group transactions by day and count them
    const groupedByDay = transactions.reduce((acc, transaction) => {
      const date = dayjs(transaction.transactionDate).format('YYYY-MM-DD');

      if (!acc[date]) {
        acc[date] = {
          name: date,
          total: 0
        };
      }

      acc[date].total += 1;

      return acc;
    }, {});

    // Populate missing dates with zero counts
    dateList.forEach(date => {
      if (!groupedByDay[date]) {
        groupedByDay[date] = {
          name: date,
          total: 0
        };
      }
    });

    // Sort the data by date
    const sortedData = Object.values(groupedByDay).sort((a, b) => dayjs(a.name).unix() - dayjs(b.name).unix());

    return sortedData;
  }, [transactions, startDate, endDate]);

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
      </BarChart>
    </ResponsiveContainer>
  );
}
