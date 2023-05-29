import React from "react";
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";

const COLORS = ["#8884d8", "#82ca9d", "#FFBB28", "#FF8042", "#AF19FF"];

const CustomTooltip = ({ active, payload, label }) => {
  if (active && payload && payload.length) {
    return (
      <div
        className="custom-tooltip"
        style={{
          backgroundColor: "white",
          color: "black",
          padding: "5px",
          border: "1px solid #cccc",
          borderRadius: "5px"
        }}
      >
        <label>{`${payload[0].name} : ${payload[0].value.toFixed(2)}%`}</label>
      </div>
    );
  }
  return null;
};

const PieRechartComponent = ({ transactions }) => {
  let componentTypes = {};

  transactions.forEach((transaction) => {
    if (!componentTypes[transaction.componentType]) {
      componentTypes[transaction.componentType] = 0;
    }
    componentTypes[transaction.componentType]++;
  });

  const totalTransactions = transactions.length;
  const pieData = Object.keys(componentTypes).map((type) => ({
    name: type,
    value: (componentTypes[type] / totalTransactions) * 100
  }));

  return (
    <ResponsiveContainer width="100%" height={350}>
    <PieChart >
      <Pie
        data={pieData}
        color="#000000"
        dataKey="value"
        nameKey="name"
        cx="50%"
        cy="50%"
        outerRadius={120}
        fill="#8884d8"
      >
        {pieData.map((entry, index) => (
          <Cell
            key={`cell-${index}`}
            fill={COLORS[index % COLORS.length]}
          />
        ))}
      </Pie>
      <Tooltip content={<CustomTooltip />} />
      <Legend />
    </PieChart>
    </ResponsiveContainer>
  );
};

export default PieRechartComponent;
