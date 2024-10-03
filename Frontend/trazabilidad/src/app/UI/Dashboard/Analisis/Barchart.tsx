'use client';
import { useRef, useEffect } from "react";
import { Chart, registerables, ChartConfiguration } from "chart.js";

Chart.register(...registerables);

export default function BarChart() {
    const chartRef = useRef<HTMLCanvasElement & { chart?: Chart }>(null); 

    useEffect(() => {
        if (chartRef.current) {
            if (chartRef.current.chart) {
                chartRef.current.chart.destroy(); 
            }

            const context = chartRef.current.getContext("2d"); 

            if (context) {
                const config: ChartConfiguration<'bar', number[], string> = {
                    type: "bar", 
                    data: {
                        labels: ["Task1", "Task2", "Task3", "Task4", "Task5"],
                        datasets: [
                            {
                                label: 'Éxitos', 
                                data: [15, 14, 5, 19, 25], 
                                backgroundColor: 'rgba(255, 99, 132)',
                                borderWidth: 1, 
                            }, 
                            {
                                label: 'Fallos',
                                data: [15, 1, 5, 5, 2], 
                                backgroundColor: 'rgba(54, 162, 235)',
                                borderWidth: 1,
                            }
                        ],
                    }, 
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: {
                                    display: true, 
                                    text: '%'
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
                                    text: 'Tareas / Actividades', 
                                }
                            }
                        }
                    }
                };
                const newChart = new Chart(context, config);
                chartRef.current.chart = newChart;  
            }
        }
    }, []);

    return (
        <div style={{ width: '100%', height: '100%', position: 'relative' }}> {/* Estilo en línea */}
            <canvas ref={chartRef} />
        </div>
    );
}
