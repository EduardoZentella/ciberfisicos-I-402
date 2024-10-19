// src/app/Dashboard/Analisis/page.tsx
'use client';
import { useEffect, useState, useRef } from 'react';
import { Chart, registerables, ChartConfiguration } from "chart.js";
import DatePicker, { DateObject } from 'react-multi-date-picker';
import 'react-multi-date-picker/styles/backgrounds/bg-dark.css';
import Styles from '../../UI/Dashboard/Analisis/Analisis.module.css';
import { useLanguage } from '../../lib/context/LanguageContext';
import RestHandler from '@/app/lib/rest';
import { Console, error } from 'console'
import Image from 'next/image';
import loader from '@/app/UI/images/loader.gif';

import { Proceso, Tarea, Pieza, Actividad, Lote} from '@/app/lib/models';

Chart.register(...registerables);

const AnalisisPage = () => {

  //Variable para almacenar los datos
  const [processes, setProcesses] = useState(new Map());
  const [tasks, setTasks] = useState(new Map()); 
  const [parts, setParts] = useState(new Map());
  const [data, setData] = useState(new Map()); 
  const [isActive, setIsActive] = useState(false);
  const [fetchedData, setFetchedData] = useState(false)

  //Fetch Procesos
  const fetchProcesses = async () => {
    try{
      let fetchedData, errorCount = 0, successCount = 0, 
          dataMap = new Map<any, any>(); 

      if (selectedDate==""){
        fetchedData = await RestHandler<Proceso[]>('/procesos', 'Proceso', 'GET'); 
      }else{
        console.log("porfecha");
        console.log(`/procesos/date/${selectedDate}`); 
        fetchedData = await RestHandler<Proceso[]>(`/procesos/date/${selectedDate}`, 'Proceso', 'GET'); 
        
      }
      fetchedData.forEach(data => {
        dataMap.set(data.Proceso_Id, {
          "ID" : data.Proceso_Id, "Nombre": data.Nombre, "Descripcion": data.Descripcion,
          "Elementos": [
            ["ID: ".concat(data.Proceso_Id.toString()), "#6610f2"],
            [data.Ini_Date.toLocaleDateString(), "#294d47"],
            [data.End_Date.toLocaleDateString(), "#464d29"], 
            [data.Status, (data.Status == "Done")? "#1c833b" : (data.Status == "Error")? "#83271c" : "#be963b"]
          ],
          "Examine" : "Proceso",     
        })
        errorCount = data.Status == "Error"? errorCount + 1 : errorCount; 
        successCount = data.Status == "Done"? successCount + 1 : successCount;
      })
      dataMap.set("ChartInfo",{
        'title': "Porcentaje de éxito en procesos", 
        'Done' : successCount,
        'Error': errorCount,
      })
      setProcesses(dataMap);
      setData(dataMap);
      console.log(dataMap); 
    } catch (error){
      console.error('Error fetching data', error);
    }
  }

  //Fetch Piezas
  const fetchTask = async () => {
    try{
      let fetchedData, errorCount = 0, successCount = 0, 
          dataMap = new Map<any, any>(); 

      fetchedData = await RestHandler<Tarea[]>('/tareas', 'Tarea', 'GET'); 
      fetchedData.forEach(data => {
        dataMap.set(data.Tarea_Id,{
          "Parent ID": data.Proceso_Id, "ID" : data.Tarea_Id, "Nombre": data.Nombre, "Descripcion": data.Descripcion,
          "Status": data.Status, "Elementos": [
            ["Proceso ID: ".concat(data.Proceso_Id.toString()), "#6610f2"],
            [data.Ini_Date.toLocaleDateString(), "#294d47"],
            [data.End_Date.toLocaleDateString(), "#464d29"], 
            [data.Status, (data.Status == "Done")? "#1c833b" : (data.Status == "Error")? "#83271c" : "#be963b"], 
          ],
          "Examine" : "Tarea",
        })
        errorCount = data.Status == "Done"? errorCount + 1 : errorCount; 
        successCount = data.Status == "Error"? successCount + 1 : successCount;
      })
      dataMap.set("ChartInfo",{
        'title': "Porcentaje de éxito en procesos", 
        'Done' : successCount,
        'Error': errorCount,
      })
      setTasks(dataMap);
    } catch (error){
      console.error('Error fetching data', error);
    }
  }

  //Fetch Tareas
  const fetchParts = async () => {
    try{
      let fetchedData, errorCount = 0, successCount = 0, 
          dataMap = new Map<any, any>(); 

      fetchedData = await RestHandler<Pieza[]>('/piezas', 'Pieza', 'GET'); 
      fetchedData.forEach(data => {
        dataMap.set(data.Pieza_Id,{
          "Parent ID": data.Lote, "ID" : data.Pieza_Id,
          "Nombre": `PZS${data.Type == "Green" ? "GRN" : "RD"}L${data.Lote}${data.Pieza_Id}`,
          "Descripcion" : `Pieza fabricada en el lote ${data.Lote}`,
          "Elementos": [
            ["PZ ID: ".concat(data.Pieza_Id.toString()),"#6610f2"],
            ["Lote:".concat(data.Lote.toString()),"#b20d5a"],
            [data.Type, data.Type == "Red" ? "#83271c" : "#1c833b"]],
          "Examine" : "Pieza",
        })
        errorCount = data.Type == "Red"? errorCount + 1 : errorCount; 
        successCount = data.Type == "Green"? successCount + 1 : successCount;
        })
        dataMap.set("ChartInfo",{
          'title': "Porcentaje de éxito en procesos", 
          'Done' : successCount,
          'Error': errorCount,
        })
        setParts(dataMap);
      }catch(error){
        console.error('Error fetching data', error);
      }
  }
         
  const handleSelectionClick = () => {
    setIsActive(!isActive);
  };

  const handleOptionClick = (selectedId: string) => {
    switch (selectedId) {
      case 'Proceso':
        console.log(processes); 
        setData(processes); 
        break; 
      case 'Tarea': 
        setData(tasks); 
        break; 
      case 'Pieza': 
        setData(parts); 
      default:
        break;
    } 
  };

  const [selectedDate, setSelectedDate] = useState<string>(""); 

  const handleDateChange = (date: DateObject) => {
    const date_string = date.year + "-" + date.month + "-" + date.day; 
    setSelectedDate(date_string); 
    fetchProcesses();
  }; 

  const { currentTranslations, changeLanguage, currentLanguage } = useLanguage(); 
  const chartRef = useRef<HTMLCanvasElement & { chart?: Chart }>(null); 
    
useEffect(() => {
  fetchProcesses(); 
  fetchTask(); 
  fetchParts();
  },[])

  const examine = async (typeData:string, event: React.MouseEvent<HTMLButtonElement>) => {
    let fetchedData, dataMap = new Map<any, any>(), errorCount = 0, successCount = 0;   
    const id = parseInt(event.currentTarget.id); 
    console.log(id); 
    console.log(tasks); 
    try{
      switch (typeData) {
        case 'Proceso': // Para procesos -> Exploramos tareas
          tasks.forEach((data, taskId) => {
            console.log(data["Parent ID"])
            if (data["Parent ID"] == id){
              dataMap.set(taskId,data); 
            }
            successCount = data["Status"] == "Done" ? successCount + 1 : successCount; 
            errorCount = data["Status"] == "Error" ? errorCount + 1 : errorCount; 
          })
          dataMap.set("ChartInfo",{
            'title': "Porcentaje de éxito en tareas", 
            'Done' : successCount,
            'Error': errorCount,
          })
           
          break;
        case 'Tarea': //Consultamos tareas
          fetchedData = await RestHandler<Actividad[]>('/actividades', 'Actividad', 'GET'); 
          fetchedData.forEach(data => {
            if(data.Tarea_Id == id){
              dataMap.set(data.Actividad_Id, {
                "ID": data.Actividad_Id, "Nombre": data.Nombre, "Descripcion": data.Descripcion,
                "Elementos": [
                  ["Tarea ID: ".concat(data.Tarea_Id.toString()), "#6610f2"],
                  
                ]
              })
            }})
       
          dataMap.set("ChartInfo",data.get("ChartInfo"))
          break; 
        case 'Pieza': //Buscamos el proceso correspondiente 
          console.log( typeof(id))
          console.log(parts.get(1)); 
          const lote_id = parts.get(id)["Parent ID"]; 
          fetchedData = await RestHandler<Lote>(`/lotes/${lote_id}`, 'Lote', 'GET'); 
          const process_id = fetchedData.Proceso_Id; 
          dataMap.set(process_id, processes.get(process_id))

      
          break; 
        default:
          break;
      }
    
      setData(dataMap);
    }catch(error){
      console.log(error); 
    }
          
  };

  useEffect(() => {
    const chartInfo = data.get("ChartInfo");   
    if (!chartInfo) {
      return;
    }
    
    if (chartRef.current) {
      if (!chartRef.current.chart) {
        const context = chartRef.current.getContext("2d"); 
        if (context) {
          const config: ChartConfiguration<'bar', number[], string> = {
            type: 'bar', 
            data: {
              labels: ["Done", "Error"], // Etiquetas de leyenda
              datasets: [
                {
                  data: [chartInfo.Done, chartInfo.Error], // Datos de Done y Error
                  backgroundColor: ['#17a2b8','#dc3545'],
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
                  labels: {
                    font: {
                      size: 14, 
                    },
                  },
                },
                title: {
                  display: true,
                  text: chartInfo.title,
                  font: {
                    size: 16, 
                  },
                },
              },
              scales: {
                y: {
                  beginAtZero: true,
                  title: {
                    display: true,
                    text: 'Número de procesos',  // Etiqueta para el eje Y
                  },
                },
              },
            }
          };
          const newChart = new Chart(context, config);
          chartRef.current.chart = newChart;  
        } 
      } else {
        chartRef.current.chart.data.datasets[0].data = [chartInfo.Done, chartInfo.Error];
        if (chartRef.current.chart.options.plugins && chartRef.current.chart.options.plugins.title) {
          chartRef.current.chart.options.plugins.title.text = chartInfo.title; // Actualizar título dinámicamente
        }
        chartRef.current.chart.update();  
      }
    }
  }, [data]);
  


if (processes.size == 0 || tasks.size == 0 || parts.size == 0) {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        width: '93vw',
        height: '90vh',
        backgroundColor: 'white',
      }}
    >
      <Image src={loader} alt="" style={{ width: '100px', height: 'auto' }} />
    </div>
  );
}


  return (
    <div
      style={{
        width: 'auto',
        margin: '15px',
      }}
    >
      <div className={Styles.title}>
        <p>{currentTranslations.analysis}</p>
      </div>

      <div style={{ display: 'flex', flexDirection: 'row', padding: '15px' }}>
        <div className={Styles.container} style={{ margin: '10px' }}>
          <div className={Styles.box}>
            <form action="a">
              <div className={Styles.input_box}>
                <input type="text" placeholder={currentTranslations.search} />
                <DatePicker
                  className="bg-dark" // Usa tu clase CSS personalizada
                  format="DD/MM/YYYY"
                  style={{ fontSize: '15px', color: '#112c4e', margin: '10px' }}
                  placeholder={currentTranslations.filter} 
                  onChange={handleDateChange}
                />

                <div
                  className={Styles.selection}
                  onClick={handleSelectionClick}
                >
                  {currentTranslations.selection}
                </div>
                <div
                  className={`${Styles.categories} ${isActive ? Styles.active : ''}`}
                >
                  <p className={Styles.option} onClick={()=>handleOptionClick('Proceso')}>{currentTranslations.processes}</p>
                  <p className={Styles.option} onClick={()=>handleOptionClick('Tarea')}>{currentTranslations.tasks}</p>
                  <p className={Styles.option} onClick={()=>handleOptionClick('Pieza')}>{currentTranslations.parts}</p>
                </div>
              </div>
            </form>
          </div>

          {/* Contenedor con scroll y box-shadow */}
          <div className={Styles.items_container}>
          {Array.from(data.entries())
            .filter(([key]) => key !== "ChartInfo") // Filtra el elemento "ChartInfo"
            .map(([key, element]) => (  
              <div key={element["ID"]} className={Styles.item} style={{display:'block'}}>
                <div style={{display: 'grid', gridTemplateColumns: 'repeat(5,1fr)', gridAutoRows: 'auto'}}>
                  <div style={{ gridColumn: '1 / 5' }}>
                    <div>
                      <h1>{element["Nombre"]}</h1>
                      <h2>{element["Descripcion"]}</h2>
                    </div>
                    <div style={{display: 'flex', flexDirection: 'row'}}>
                      {(element["Elementos"] || []).map((a:any, index:any) => (
                        <div key={`${element["ID"]}-${index}`} className={Styles.type} style={{backgroundColor: a[1]}}> 
                          {a[0]}
                        </div>
                      ))}
                    </div>
                  </div>
                  <div style={{ gridColumn: '5 / 6' , display: 'flex', alignItems: 'center', justifyContent: 'center'}}>      
                    <button className={Styles.examine} id={element["ID"]} onClick={(event) => examine(element["Examine"], event)}>
                      <p>Examine</p>
                    </button>            
                  </div>
                </div>
              </div>
            ))}
</div>

        </div>

        <div className={Styles.container} style={{ margin: '10px' }}>
           <div style={{width: '100%', height: '100%', position:'relative'}}>
            <canvas ref={chartRef}/>
           </div>
        </div>
      </div>
    </div>
  );
};

export default AnalisisPage;
