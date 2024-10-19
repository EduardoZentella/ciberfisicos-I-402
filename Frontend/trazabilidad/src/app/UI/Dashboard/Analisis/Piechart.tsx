'use client'; 
import { useRef, useEffect, MutableRefObject, useState } from "react";
import RestHandler from '@/app/lib/rest'
import {Pieza} from '@/app/lib/models'
import { Chart } from "chart.js/auto";

export default function PieChart() {
    const chartRef = useRef() as MutableRefObject<HTMLCanvasElement & { chart?: Chart<"pie", number[], string> }>; 

    const[tareas, setPiezas] = useState<Pieza[]>([]);

    useEffect(() => {
        const fetchTareas = async () => {
          try{
            const data = await RestHandler<Pieza[]>(
              '/tareas/last/8', 
              'Tarea', 
              'GET'
            );
            console.log('Raw response:', data);
            setPiezas(data); 
          } catch (error) {
            console.error('Error fetching tareas', error);
          }
        }; 
        fetchTareas(); 
        const interval = setInterval(fetchTareas, 1500); 
        return () => clearInterval(interval); 
      }, []);




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
                                data: [qualityCount, defectiveCount], // Usar el estado para los datos
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
    }, [qualityCount, defectiveCount]); // Agregar estados como dependencias

    useEffect(() => {
        const interval = setInterval(() => {
            setQualityCount(prev => prev + 1); // Incrementar piezas de calidad
        }, 5000); // Cada 5 segundos

        return () => clearInterval(interval); // Limpiar el intervalo al desmontar
    }, []);

    return (
        <div>
            <canvas ref={chartRef} />
        </div>
    );
}
