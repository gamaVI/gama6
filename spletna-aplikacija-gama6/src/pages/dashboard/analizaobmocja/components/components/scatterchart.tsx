"use client";
import React from 'react';
import { ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const CustomTooltip = ({ active, payload }) => {
    if (active) {
        return (
            <div className="custom-tooltip" style={{ backgroundColor: '#ffff', padding: '5px', border: '1px solid #cccc' }}>
                <label>{`Year Built: ${payload[0].payload.buildingYearBuilt}, Transaction Amount: ${payload[0].payload.transactionAmountGross}`}</label>
            </div>
        );
    }
    return null;
};

export default function TransactionScatterPlot({ transactions }) {
    const data = transactions.map(transaction => ({
        buildingYearBuilt: transaction.buildingYearBuilt,
        transactionAmountGross: transaction.transactionAmountGross,
    }));

    return (
        <ResponsiveContainer width="100%" height={350}>
        <ScatterChart
            width={400}
            height={400}
            margin={{
                top: 20,
                right: 20,
                bottom: 20,
                left: 20,
            }}
        >
            <CartesianGrid />
            <XAxis dataKey="buildingYearBuilt" name="Year Built" unit="yr" />
            <YAxis dataKey="transactionAmountGross" name="Transaction Amount" unit="€" />
            <Tooltip content={<CustomTooltip />} />
            <Scatter data={data} fill="#8884d8" />
        </ScatterChart>
        </ResponsiveContainer>
    );
}