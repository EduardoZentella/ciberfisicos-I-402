'use client'; 
import { useRef, useEffect, MutableRefObject } from "react";
import { Chart } from "chart.js/auto";

export default function PieChart() {
    const chartRef = useRef() as MutableRefObject<HTMLCanvasElement & { chart?: Chart<"pie", number[], string> }>; 

    useEffect(() => {
        if (chartRef.current) {
            if (chartRef.current.chart) {
                chartRef.current.chart.destroy(); 
            }

            const context = chartRef.current.getContext("2d"); 

            if (context) {
                const newChart = new Chart(context, {
                type: "pie", 
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
                    responsive: true,
                    // Aquí puedes agregar opciones adicionales si es necesario
                }
            });

            chartRef.current.chart = newChart;  
            }
        }
    }, []); // Added dependency array

    return (
        <div >
            <canvas ref={chartRef} />
        </div>
    );
}