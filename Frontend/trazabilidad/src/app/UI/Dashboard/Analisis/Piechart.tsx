'use client'; 
import { useRef, useEffect, MutableRefObject, useState } from "react";
import { Chart } from "chart.js/auto";
import { Pieza } from "@/app/lib/models";
import RestHandler from "@/app/lib/rest";
import loader from "@/app/UI/images/chartLoader.gif"
import Image from "next/image";

export default function PieChart() {
    const chartRef = useRef() as MutableRefObject<HTMLCanvasElement & { chart?: Chart<"pie", number[], string> }>; 

    const [greenPzs, setGreenPiezas] = useState<number>(0);
    const [redPzs, setRedPiezas] = useState<number>(0);

    useEffect(() => {
        const fetchPiezas = async () => {
            try {
                const gpzs = await RestHandler<Pieza[]>('/piezas/type/1', 'Pieza', 'GET');
                console.log('Raw response:', gpzs);
                setGreenPiezas(gpzs.length); 
            } catch (error) {
                console.error('Error fetching piezas', error);
            }

            try {
                const rpzs = await RestHandler<Pieza[]>('/piezas/type/2', 'Pieza', 'GET');
                console.log('Raw response:', rpzs);
                setRedPiezas(rpzs.length); 
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
                // Crear el gráfico la primera vez
                const context = chartRef.current.getContext("2d"); 
                if (context) {
                    const newChart = new Chart(context, {
                        type: "pie",
                        data: {
                            labels: ["Quality Parts", "Defective Parts"],
                            datasets: [
                                {
                                    data: [greenPzs, redPzs],
                                    backgroundColor: [
                                        '#17a2b8', // Color para piezas defectuosas
                                        '#6f42c1', // Color para piezas de calidad
                                        //'#112c4e'
                                    ],
                                    borderWidth: 1,
                                },
                            ],
                        },
                        options: {
                            responsive: true,
                        },
                    });
                    chartRef.current.chart = newChart;
                }
            } else {
                // Si el gráfico ya existe, actualizar los datos
                chartRef.current.chart.data.datasets[0].data = [greenPzs, redPzs];
                chartRef.current.chart.update();
            }
        }
    }, [greenPzs, redPzs]); // Actualizar cuando cambian las piezas

    if (greenPzs==0 || redPzs==0){
        return(
            <div>
                <Image src={loader} alt="" />
            </div>
        )
    }
    return (
        <div>
            <canvas ref={chartRef} />
        </div>
    );
}
