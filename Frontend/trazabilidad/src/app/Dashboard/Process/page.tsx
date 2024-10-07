'use client'; 

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

    const toggleRobots = () => {
      setIsRobotsActive(!isRobotsActive)
    }

    //Query a las Tareas / Tasks

    const[tareas, setTareas] = useState<Tarea[]>([]);

    useEffect(() => {
      const fetchProcesos = async () => {
        try{
          const data = await RestHandler<Tarea[]>(
            '/tareas', 
            'Tarea', 
            'GET'
          );
          console.log('Raw response:', data);
          setTareas(data); 
        } catch (error) {
          console.error('Error fetching procesos', error);
        }
      }; 
      fetchProcesos(); 
    }, []); 

    //Query a los Robots 

    const[robots, setRobots] = useState<Robot[]>([]); 

    useEffect(() => {
      const fetchProcesos = async () => {
        try{
          const data = await RestHandler<Robot[]>(
            '/robots', 
            'Proceso', 
            'GET'
          );
          console.log('Raw response:', data);
          setRobots(data); 
        } catch (error) {
          console.error('Error fetching procesos', error);
        }
      }; 
      fetchProcesos(); 
    }, []); 

   

    const imagesRobots = {
      'B1': b1Icon,
      'x-Arm': xArm, 
      'Omron-LD': omronIcon, 
      'Omron-TM S': omronArm,
    } 

    return (
    
      <div style={{width: 'auto', margin: '15px'}}>
        {/*Titulo del proceso*/}
        <div className={Styles.process}> <p>{currentTranslations.process}: Nombre del proceso</p> </div>
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
              
              <div className={Styles.content} style={{display: 'block', overflowY: 'auto', maxHeight: '350px'}}>
                {tareas.map((tarea) => (
                    <div className={Styles.elementList}>
                      <div>
                        <h1>{tarea.Nombre}</h1>
                        <h2>{tarea.Descripcion}</h2>
                      </div>
                      {/*
                      <div style={{display:'flex', alignItems: 'right'}}>
                        <div style={{backgroundColor: '#cd9b37'}} className={Styles.type}> Type</div>
                        <div style={{backgroundColor:'#ca4646'}} className={Styles.status}> Status</div>
                        <div style={{backgroundColor:'#675e5e'}} className={Styles.date}> Date</div>
                      </div> */}
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

              <div className={Styles.chartContainer} style={{height: '100px'}}>
                <div style={{position: 'relative'}}>
                  Pendiente
                </div>
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
                              <div style={{width:  `${70}%` ,
                                backgroundColor: 70 > 50 ? 'green': 70 > 20 ? 'orange' : 'red',
                                }} className={Styles.batteryFill}>
                              </div>
                              <span className={Styles.batteryPercentage}>{70}%</span>
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
                <BarChart />
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
                <h1>Quality rate</h1>
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