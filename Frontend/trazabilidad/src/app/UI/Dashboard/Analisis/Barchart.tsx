'use client';
import { useRef, useEffect } from "react";
import { Chart, registerables, ChartConfiguration } from "chart.js";
import { useState } from "react";
import RestHandler from "@/app/lib/rest";
import { Proceso } from "@/app/lib/models";
import Image from "next/image";
import loader from "@/app/UI/images/chartLoader.gif"
Chart.register(...registerables);

export default function BarChart() {
    const chartRef = useRef<HTMLCanvasElement & { chart?: Chart }>(null); 

    const [errorProcess, setErrorProcess] = useState<number>(0);
    const [doneProcess, setDoneProcess] = useState<number>(0);

    useEffect(() => {
        const fetchPiezas = async () => {
            try { //Done
                const done = await RestHandler<Proceso[]>('/piezas/type/1', 'Pieza', 'GET');
                console.log('Raw response:', done);
                setDoneProcess(done.length); 
            } catch (error) {
                console.error('Error fetching piezas', error);
            }

            try { //Error
                const rpzs = await RestHandler<Proceso[]>('/piezas/type/2', 'Pieza', 'GET');
                setErrorProcess(rpzs.length); 
            } catch (error) {
                console.error('Error fetching piezas', error);
            }
        }; 

        fetchPiezas(); 
        const interval = setInterval(fetchPiezas, 1500); 
        return () => clearInterval(interval); 
    }, []);

    useEffect(() => {
        if (chartRef.current) {
            if (!chartRef.current.chart) {
                
                const context = chartRef.current.getContext("2d"); 
                if (context) {
                    const config: ChartConfiguration<'bar', number[], string> = {
                        type: 'bar', 
                        data: {
                            labels: ["Done", "Error"],
                            datasets: [
                                {
                                    data: [errorProcess, doneProcess], 
                                    backgroundColor: [
                                        '#17a2b8',
                                        '#dc3545',
                                    ],
                                    borderWidth: 1, 
                                }, 
                            ],
                        }, 
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {
                                    display: false,
                                },
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Número de process',  // Etiqueta para el eje Y
                                    },
                                },
                            },
                        }
                    };
                    const newChart = new Chart(context, config);
                    chartRef.current.chart = newChart;  
                } 

        }else{
            chartRef.current.chart.data.datasets[0].data = [doneProcess, errorProcess]
            chartRef.current.chart.update();  
        }
}}, [doneProcess, errorProcess]);

    if(doneProcess==0 || errorProcess==0){
        return (
            <div>
              <Image src={loader} alt="" />
            </div>
        )
    }

    return (
        <div style={{ width: '100%', height: '100%', position: 'relative' }}> {/* Estilo en línea */}
            <canvas ref={chartRef} />
        </div>
    );
}
