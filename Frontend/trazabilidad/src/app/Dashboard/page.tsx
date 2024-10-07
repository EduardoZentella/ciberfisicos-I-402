// src/app/Dashboard/page.tsx
'use client'; 
import cardStyles from '../UI/Dashboard/Processes Menu/Card.module.css'
import Image from 'next/image';
import smartTrack_img from '../UI/images/Processes/home_1.jpg'; 
import { useLanguage } from '../lib/context/LanguageContext';
import { Proceso} from '../lib/models'
import RestHandler from '../lib/rest'
import {useEffect, useState} from 'react'
import { card } from '@nextui-org/theme';


const DashboardHomePage = () => {

  const[procesos, setProcesos] = useState<Proceso[]>([]); 

  useEffect(() => {
    const fetchProcesos = async () => {
      try{
        const data = await RestHandler<Proceso[]>(
          '/procesos/status', 
          'Proceso', 
          'GET'
        );
        console.log('Raw response:', data);
        setProcesos(data); 
      } catch (error) {
        console.error('Error fetching procesos', error);
      }
    }; 
    fetchProcesos(); 
  }, []); 

  

  const { currentTranslations, changeLanguage, currentLanguage } = useLanguage(); 

  const imagesPaths = {
    'Smart Track': smartTrack_img,
  } 

  return (
    <div style={{height: "100vh"}}>

      <div className={cardStyles.title}>
        <p>{currentTranslations.processes}</p>
        
      </div>
      
      <div className={cardStyles.cardWrapper}>

        {/* Mapeamos los procesos*/}
        <ul>
          {procesos.map((proceso) => (
            <div className={cardStyles.card}>
              <div className={cardStyles.imgArea}>
                <Image src={imagesPaths[proceso.Nombre]} alt=""/>
                <div className={cardStyles.overlay}>
                  <a href="/Dashboard/Process/">
                    <button className={cardStyles.viewDetails}>{currentTranslations.viewDetails}</button>
                  </a>
                </div>
              </div>
              <div className={cardStyles.info}>
                <h3>{proceso.Nombre}</h3>
                <p>{proceso.Location}</p>
                <h4>{proceso.Descripcion}</h4>
              </div>
            </div>
          ))}
        </ul>
      </div>  
    </div>
  );
};

export default DashboardHomePage;