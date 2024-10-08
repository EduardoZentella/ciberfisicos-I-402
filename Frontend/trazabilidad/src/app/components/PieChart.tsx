'use client'; 
import { useRef, useEffect } from "react";
import { Chart } from "chart.js/auto";

export default function PieChart() {
    const chartRef = useRef(null); 

    useEffect(() => {
        if (chartRef.current) {
            if (chartRef.current.chart) {
                chartRef.current.chart.destroy(); 
            }

            const context = chartRef.current.getContext("2d"); 

            const newChart = new Chart(context, {
                type: "doughnut", 
                data: {
                    labels: ["Quality Parts", "Defective Parts"], 
                    datasets: [
                        {
                            data: [20, 2], 
                            backgroundColor: [
                                'rgba(255, 99, 132)', 
                                'rgba(54, 162, 235)',
                            ], 
                            borderWidth: 1, 
                        }, 
                    ],
                }, 
                options: {
                    responsive:true,
                }, 
            });

            chartRef.current.chart = newChart;  
        }
    }, []);

    return (
        <div >
            <canvas ref={chartRef} />
        </div>
    );
}
