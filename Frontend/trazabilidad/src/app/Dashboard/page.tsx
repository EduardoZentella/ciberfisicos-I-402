// Importa useRouter de Next.js
// Resto de los imports...
'use client'; 
import cardStyles from '../UI/Dashboard/Processes Menu/Card.module.css';
import Image from 'next/image';
import smartTrack_img from '../UI/images/Processes/home_1.jpg';
import { useLanguage } from '../lib/context/LanguageContext';
import { Proceso } from '../lib/models';
import RestHandler from '../lib/rest';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

import loader from '@/app/UI/images/loader.gif';

const DashboardHomePage = () => {
  const [procesos, setProcesos] = useState<Proceso[]>([]);
  const router = useRouter(); // Hook de Next.js para la navegación

  useEffect(() => {
    const fetchProcesos = async () => {
      try {
        const data = await RestHandler<Proceso[]>('/procesos', 'Proceso', 'GET');
        console.log('Raw response:', data);
        setProcesos(data);
      } catch (error) {
        console.error('Error fetching procesos', error);
      }
    };
    fetchProcesos();
  }, []);

  const { currentTranslations } = useLanguage();

  const imagesPaths = {
    'Smart Track': smartTrack_img,
  };

  // Si no hay procesos, muestra el loader
  if (procesos.length === 0) {
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

  // Función para manejar la navegación
  const handleViewDetails = (nombre: string) => {
    // Navega a /Dashboard/Process con el query parameter del nombre del proceso
    router.push(`/Dashboard/Process?pageName=${encodeURIComponent(nombre)}`);
  };

  return (
    <div style={{ height: '100vh' }}>
      <div className={cardStyles.title}>
        <p>{currentTranslations.processes}</p>
      </div>

      <div className={cardStyles.cardWrapper}>
        {/* Mapeamos los procesos */}
        <ul>
          {procesos.map((proceso) => (
            <div key={proceso.Nombre} className={cardStyles.card}>
              <div className={cardStyles.imgArea}>
                <Image src={imagesPaths[proceso.Nombre]} alt="" />
                <div className={cardStyles.overlay}>
                  <button
                    className={cardStyles.viewDetails}
                    onClick={() => handleViewDetails(proceso.Nombre)} // Navega al proceso específico con el query parameter
                  >
                    {currentTranslations.viewDetails}
                  </button>
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
