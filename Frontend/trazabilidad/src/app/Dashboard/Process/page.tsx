'use client'; 
import { useSearchParams } from 'next/navigation';
import Styles from "@/app/UI/Dashboard/Process.module.css"
import Image from 'next/image';
import robotIcon from '@/app/UI/images/Dashboard/Robot_ico.png';
import omronIcon from '@/app/UI/images/Dashboard/Robots/omron.png'
import omronArm from '@/app/UI/images/Dashboard/Robots/omron_cobot.png'
import b1Icon from '@/app/UI/images/Dashboard/Robots/b1.jpg'
import xArm from '@/app/UI/images/Dashboard/Robots/x-Arm.png'
import historyIcon from '@/app/UI/images/Dashboard/history_ico.png'
import detailsIcon from '@/app/UI/images/Dashboard/view_more.png'
import expandIcon from '@/app/UI/images/Dashboard/expand.png'
import collapseIcon from '@/app/UI/images/Dashboard/collapse.png'
import warningsIcon from '@/app/UI/images/Dashboard/warning_ico.png'
import successIcon from '@/app/UI/images/Dashboard/rate_success.png'
import qualityIcon from '@/app/UI/images/Dashboard/quality_ico.png'
import timeIcon from '@/app/UI/images/Dashboard/time_avg.png'
import safe from '@/app/UI/images/Dashboard/safe.gif'
import warn from '@/app/UI/images/Dashboard/warning.gif'
import loader from '@/app/UI/images/loader.gif'
import React, {useEffect, useState} from 'react'; 
import PieChart from "@/app/UI/Dashboard/Analisis/Piechart"
import LineChart from "@/app/UI/Dashboard/Analisis/Linechart"
import BarChart from "@/app/UI/Dashboard/Analisis/Barchart"
import { useLanguage } from '../../lib/context/LanguageContext';
import {Tarea} from '../../lib/models'
import {Robot} from '../../lib/models'
import RestHandler from '../../lib/rest'

const Process = () => {
  
    const { currentTranslations, changeLanguage, currentLanguage } = useLanguage(); 

    const [isRobotsActive, setIsRobotsActive] = useState(true); 

    const [robotsWarnings, setRobotsWarnings] = useState<string[]>([]);

    const [processWarnings, setProcesssWarnings] = useState<string[]>([]);

    const toggleRobots = () => {
      setIsRobotsActive(!isRobotsActive)
    }

    const searchParams = useSearchParams(); 
    const [processName, setProcessName] = useState<string | null>(null); 

  useEffect(() => {
    const pName = searchParams.get('pageName'); 

    if(pName) {
      setProcessName(pName); 
    }
  }, [searchParams]); 

    const[tareas, setTareas] = useState<Tarea[]>([]);

    useEffect(() => {
      const fetchTareas = async () => {
        try{
          const data = await RestHandler<Tarea[]>(
            '/tareas/last/8', 
            'Tarea', 
            'GET'
          );
          
          console.log('Raw response:', data);
          setTareas(data); 
        } catch (error) {
          console.error('Error fetching tareas', error);
        }
      }; 
      fetchTareas(); 
      const interval = setInterval(fetchTareas, 1500); 
      return () => clearInterval(interval); 
    }, []); 

    const[robots, setRobots] = useState<Robot[]>([]); 

    useEffect(() => {
      const fetchRobots = async () => {
        try{
          const data = await RestHandler<Robot[]>(
            '/robots', 
            'Robot', 
            'GET'
          );
          console.log('Raw response:', data);
          setRobots(data); 


          const newWarnings = data
          .filter(robot => parseInt(robot.Charge.slice(0,-1)) < 25)
          .map(robot => `${robot.Nombre},(${robot.Charge}).`);
        
        setRobotsWarnings(newWarnings);
        } catch (error) {
          console.error('Error fetching  robots', error);
        }
      }; 
      fetchRobots(); 
    
      const interval = setInterval(fetchRobots, 1500); 
      return () => clearInterval(interval); 
    }, []); 
    
   

    const imagesRobots = {
      'B1': b1Icon,
      'x-Arm': xArm, 
      'Omron-LD': omronIcon, 
      'Omron-TM S': omronArm,
    } 

  if (tareas.length === 0 || robots.length === 0) {
    return <div style={{
      display: 'flex',
      justifyContent: 'center', // Centra horizontalmente
      alignItems: 'center',     // Centra verticalmente
      width: '93vw',
      height: '90vh',
      backgroundColor: 'white',
    }}
  >
    <Image src={loader} alt="" style={{width: '100px', height: 'auto'}}/>
  </div>
  }

    return (
    
      <div style={{width: 'auto', margin: '15px'}}>
        {/*Titulo del proceso*/}
        <div className={Styles.process}> <p>{currentTranslations.process}: {processName}</p> </div>
        {/*Grid*/}
        <div className={Styles.gridWrapper}>
        {/*Primera columna para tareas recientes y warnings*/}
          <div style={{gridColumn:1}}>
            {/*Tareas recientes*/}
            <div className={Styles.container}>

              <div className={Styles.containerButton}>
                <Image src={historyIcon} alt="" className={Styles.iconButton}/>
                {currentTranslations.history}
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
                </div>
              </div>
              
              <div className={Styles.content} style={{ display: 'grid', overflowY: 'auto', maxHeight: '350px', gridTemplateColumns: '1fr' }}>
            {tareas.map((tarea) => (
              <div className={Styles.elementList} style={{display: 'grid', gridTemplateColumns: '3fr 1fr', gap: '10px', alignItems: 'center' }}>
                <div>
                  <h1>{tarea.Nombre}</h1>
                  <h2>{tarea.Descripcion} </h2>
                </div>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '10px' }}>
                  
                <div  style={{ backgroundColor: tarea.Status === 'In Progress' ? '#007bff' :
                                                tarea.Status === 'Completed' ? '#28a745' : 
                                                tarea.Status === 'Error' ? '#dc3545' : '#6c757d'}} className={Styles.addInfo} > 
                  {tarea.Status}
                </div>
                  
                  <div style={{backgroundColor: '#6610f2'}} className={Styles.addInfo}> 
                    {tarea.Ini_Date.getDate()}/{tarea.Ini_Date.getMonth() + 1} {/* Fecha de Inicio */}
                  </div>

                  <div style={{backgroundColor: '#17a2b8'}} className={Styles.addInfo}> 
                    {tarea.Ini_Date.getHours()}:{tarea.Ini_Date.getMinutes()}:{tarea.Ini_Date.getSeconds()} {/* Hora de Inicio */}
                  </div>

                  <div style={{backgroundColor: '#6f42c1'}} className={Styles.addInfo}> 
                    {tarea.End_Date.getHours()}:{tarea.End_Date.getMinutes()}:{tarea.End_Date.getSeconds()} {/* Hora de Fin */}
                  </div>
                </div>
              </div>
            ))}
          </div>

            </div>

            {/*Warnings*/}
            <div className={Styles.container} style={{gridColumn:1, gridRow:3, marginTop: '20px'}}>
              <div className={Styles.containerButton}>
                <Image src={warningsIcon} alt="" className={Styles.iconButton}/>
                <h1 style={{marginLeft: '10px'}}>{currentTranslations.warnings}</h1>
              
              </div>

              <div className={Styles.chartContainer} style={{height: 'auto', minHeight:'140px', alignItems: 'left', flexDirection: 'row', maxHeight: '200px'}}>
                <div style={{position: 'relative'}}>
                  <Image src={robotsWarnings.length== 0 && processWarnings.length == 0 ? safe: warn} alt="" style={{height: '100px', width: 'auto'}}/>
                </div>
                <h1 style={{textAlign: 'left', marginLeft: '20px', fontFamily: 'Bebas Neue', fontSize: '17px', fontWeight: 'bold', color: (robotsWarnings.length==0 && processWarnings.length==0) ? '#0c4716': '#4e0d0d'}}>   
                  {robotsWarnings.map((item) => (
                    <p>{currentTranslations.warningsRobots.replace('{robotName}', item.split(',')[0]).replace('{charge}', item.split(',')[1])}</p>
                  ))}
                  <p style={{display: robotsWarnings.length==0 && processWarnings.length== 0 ? 'Block': 'None'}}>No hay ninga advertencia en el proceso !!</p>
                </h1>
              </div>

            </div> 
          </div>     
        {/*Segunda columna para Robots y grafica de procesos*/}     
          <div style={{gridColumn:2}}>
            {/*Robots*/}
            <div className={Styles.container} style={{gridRow:2, gridColumn:2}}>
              <div className={Styles.containerButton} style={{borderRadius: isRobotsActive ? '10px 10px 0px 0px' : '10px'}}>
                <Image src={robotIcon} alt="" className={Styles.iconButton}/>
                Robots
                <div className={Styles.overlay}>
                  <button  style={{ height:'50px'}} onClick={toggleRobots}>
                    <Image  src={isRobotsActive ? collapseIcon : expandIcon} alt="" className={Styles.iconButton}/>
                  </button>
           
            
                </div>
              </div>

              <div className={Styles.content} style={{display: isRobotsActive ? 'block' : 'none', overflowY:'auto',maxHeight: '300px'}}>
                {robots.map((robot) => (
                    <div className={Styles.elementList}>
                      <Image src={imagesRobots[robot.Nombre]} alt="" className={Styles.contentImage}/>
                      <div>
                        <h1>{robot.Nombre}</h1>
                        <h2>{robot.Descripcion}</h2>
                      </div>

                      <div style={{display:'flex', alignItems: 'right'}}>
                        <div className={Styles.batteryContainer}>
                          <div className={Styles.batteryStatus}>
                            <div className={Styles.batteryLevel}>
                              <div style={{width:  `${robot.Charge}` ,
                                backgroundColor: parseInt(robot.Charge.slice(0,-1)) > 50 ? '#4CAF50': parseInt(robot.Charge.slice(0,-1)) > 20 ? 'orange' : 'red',
                                }} className={Styles.batteryFill}>
                              </div>
                              <span className={Styles.batteryPercentage}>{robot.Charge}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                ))}
              </div>          
            </div>

            {/*Process Rate*/}
            <div className={Styles.container} style={{marginTop: '20px', height: isRobotsActive ? 'auto': '80%'}}>
              <div className={Styles.containerButton}>
              <Image src={successIcon} alt="" className={Styles.iconButton}/>

                <h1 style={{marginLeft: '10px'}}>{currentTranslations.succesRate}</h1>
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
            
                </div>
              </div>

              <div className={Styles.chartContainer} style={{ height: isRobotsActive ? '180px' : '90%' }}>
                <BarChart/>
            </div>

            </div> 
          </div>
        
        {/*Tercera columna para piezas y promedio de tiempo*/} 
          <div style={{gridColumn:3}}>
          {/*Grafica de pastel*/}
            <div className={Styles.container} style={{gridColumn:3, gridRow:2}}>
              <div className={Styles.containerButton}>
              <Image src={qualityIcon} alt="" className={Styles.iconButton}/>
                <h1 style={{marginLeft: '10px'}}>{currentTranslations.qualityRate}</h1>
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
            
                </div>
              </div>

              <div className={Styles.chartContainer}>
                <div style={{position: 'relative'}}>
                  <PieChart/>
                </div>
              </div>
            </div>  
          {/*Grafica de tiempo proceso*/}
            <div className={Styles.container} style={{gridColumn:2, gridRow:3, marginTop: '20px'}}>
              <div className={Styles.containerButton}>
              <Image src={timeIcon} alt="" className={Styles.iconButton}/>

                <h1 style={{marginLeft: '10px'}}>{currentTranslations.timeAverage}</h1>
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
                </div>
              </div>

              

              <div className={Styles.chartContainer} style={{gridColumn:2, gridRow:3}}>
                <div style={{position: 'relative', height: '100%', width: '100%'}}>
                  <LineChart/>
                </div>
              </div>  
            </div>  
          </div>      
      </div>
    </div>
    );
  };
  
export default Process;