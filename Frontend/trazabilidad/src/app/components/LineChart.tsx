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
                type: "line", 
                data: {
                    labels: ["07-10", "10-13", "13-16s", "16-19", "19-22"],
                    datasets: [
                        {
                            label:"",
                            data: [15, 14, 5, 19, 25], 
                            backgroundColor: [
                                'rgba(255, 99, 132)', 
                            ], 
                            borderWidth: 1, 
                        }, 
                    ],
                }, 
                options: {
                    maintainAspectRatio: false,
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false,
                        },
                    },
                    scales: {
                        y: {
                            title: {
                                display: true, 
                                text: 'Tiempo Promedio (s)  '
                            },
                            ticks: {
                                autoSkip: true,
                                maxRotation: 0, 
                                minRotation: 0,
                            }
                        },
                        x: {
                            title: {
                                display: true, 
                                text: 'Intervalos de horarios', 
                            }
                        }
                    }
                    // Aquí puedes agregar opciones adicionales si es necesario
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
