'use client'; 

import Styles from "../../../UI/Dashboard/Process.module.css"
import Image from 'next/image';
import robotIcon from '../../../UI/images/Dashboard/robot_ico.png';
import onromIcon from '../../../UI/images/Dashboard/onrom.png' 
import historyIcon from '../../../UI/images/Dashboard/history_ico.png'
import detailsIcon from '../../../UI/images/Dashboard/view_more.png'
import expandIcon from '../../../UI/images/Dashboard/expand.png'
import collapseIcon from '../../../UI/images/Dashboard/collapse.png'
import React, {useState} from 'react'; 
import PieChart from "../../../components/PieChart"
import LineChart from "../../../components/LineChart"
import BarChart from "../../../components/BarChart"

const Process = () => {
    const [isRobotsActive, setIsRobotsActive] = useState(true); 

    const toggleRobots = () => {
      setIsRobotsActive(!isRobotsActive)
    }

    const tasks = [
      { id: 1, name: 'Tarea 1', description: 'Descripción de la tarea 1' },
      { id: 2, name: 'Tarea 2', description: 'Descripción de la tarea 2' },
      { id: 3, name: 'Tarea 3', description: 'Descripción de la tarea 3' },
      { id: 4, name: 'Tarea 4', description: 'Descripción de la tarea 4' },
      { id: 5, name: 'Tarea 5', description: 'Descripción de la tarea 5' },
      { id: 6, name: 'Tarea 5', description: 'Descripción de la tarea 5' },
      { id: 6, name: 'Tarea 5', description: 'Descripción de la tarea 5' },
      { id: 6, name: 'Tarea 5', description: 'Descripción de la tarea 5' },
  

    ];

    const robots = [
      { id: 1, name: 'B1', description: 'Descripción de la tarea 1', battery: 100 },
      { id: 2, name: 'Onrom', description: 'Descripción de la tarea 2', battery: 80},
      { id: 3, name: 'X-Arm', description: 'Descripción de la tarea 3', battery: 50 },
      { id: 4, name: 'Onrom Arm', description: 'Descripción de la tarea 4', battery: 10},
    ];

    return (
    
      <div style={{backgroundColor: '#d4d8de', height: '100vh', width: 'auto', margin: '15px'}}>
        {/*Titulo del proceso*/}
        <div className={Styles.process}> <p>Process</p> </div>
        {/*Grid*/}
        <div className={Styles.gridWrapper}>
        {/*Primera columna para tareas recientes y warnings*/}
          <div style={{gridColumn:1}}>
            {/*Tareas recientes*/}
            <div className={Styles.container}>

              <div className={Styles.containerButton}>
                <Image src={historyIcon} alt="" className={Styles.iconButton}/>
                History
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
                </div>
              </div>
              
              <div className={Styles.content} style={{display: 'block', overflowY: 'auto', maxHeight: '350px'}}>
                {tasks.map((task) => (
                  <div key={task.id}>
                    <div className={Styles.elementList}>
                      <div>
                        <h1>{task.name}</h1>
                        <h2>{task.description}</h2>
                      </div>

                      <div style={{display:'flex', alignItems: 'right'}}>
                        <div style={{backgroundColor: '#cd9b37'}} className={Styles.type}> Type</div>
                        <div style={{backgroundColor:'#ca4646'}} className={Styles.status}> Status</div>
                        <div style={{backgroundColor:'#675e5e'}} className={Styles.date}> Date</div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/*Warnings*/}
            <div className={Styles.container} style={{gridColumn:1, gridRow:3, marginTop: '20px'}}>
              <div className={Styles.containerButton}>
                <h1 style={{marginLeft: '10px'}}>Warnings</h1>
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
            
                </div>
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
              <div className={Styles.containerButton}>
                <Image src={robotIcon} alt="" className={Styles.iconButton}/>
                Robots
                <div className={Styles.overlay}>
                  <button  style={{ height:'50px'}} onClick={toggleRobots}>
                    <Image  src={isRobotsActive ? collapseIcon : expandIcon} alt="" className={Styles.iconButton}/>
                  </button>
           
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
                </div>
              </div>

              <div className={Styles.content} style={{display: isRobotsActive ? 'block' : 'none', overflowY:'auto',maxHeight: '300px'}}>
                {robots.map((robot) => (
                  <div key={robot.id}>
                    <div className={Styles.elementList}>
                      <Image src={onromIcon} alt="" className={Styles.contentImage}/>
                      <div>
                        <h1>{robot.name}</h1>
                        <h2>{robot.description}</h2>
                      </div>

                      <div style={{display:'flex', alignItems: 'right'}}>
                        <div className={Styles.batteryContainer}>
                          <div className={Styles.batteryStatus}>
                            <div className={Styles.batteryLevel}>
                              <div style={{width:  `${robot.battery}%` ,
                                backgroundColor: robot.battery > 50 ? 'green': robot.battery > 20 ? 'orange' : 'red',
                                }} className={Styles.batteryFill}>
                              </div>
                              <span className={Styles.batteryPercentage}>{robot.battery}%</span>
                            </div>
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
                <h1 style={{marginLeft: '10px'}}>Process Succes Rate</h1>
                <div className={Styles.overlay}>
                  <a href='/Dashboard/Analisis'>
                    <button  style={{ height:'50px'}}>
                      <Image src={detailsIcon} alt="" className={Styles.iconButton}/>
                    </button>
                  </a>
            
                </div>
              </div>

              <div className={Styles.chartContainer} style={{ height: isRobotsActive ? '180px' : '100%', transition: 'height 0.3s' }}>
                <BarChart />
            </div>

            </div> 
          </div>
        
        {/*Tercera columna para piezas y promedio de tiempo*/} 
          <div style={{gridColumn:3}}>
          {/*Grafica de pastel*/}
            <div className={Styles.container} style={{gridColumn:3, gridRow:2}}>
              <div className={Styles.containerButton}>
                <h1 style={{marginLeft: '10px'}}>Quality rate</h1>
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
            <div className={Styles.container} style={{gridColumn:2, gridRow:3}}>
              <div className={Styles.containerButton}>
                <h1 style={{marginLeft: '10px'}}>Time Average Analysis</h1>
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