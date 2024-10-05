// src/app/Dashboard/Analisis/page.tsx
'use client';

import { useState } from 'react';
import DatePicker from 'react-multi-date-picker';
import 'react-multi-date-picker/styles/backgrounds/bg-dark.css';
import BarChart from '../../UI/Dashboard/Analisis/Barchart';
import Styles from '../../UI/Dashboard/Analisis/Analisis.module.css';
import { useLanguage } from '../../lib/context/LanguageContext';


const AnalisisPage = () => {

  const [isActive, setIsActive] = useState(false);
  const handleSelectionClick = () => {
    setIsActive(!isActive);
  };

  const { currentTranslations, changeLanguage, currentLanguage } = useLanguage(); 


  // Crear un array con 20 elementos
  const items = Array.from({ length: 20 }, (_, i) => ({
    id: i + 1,
    title: `Item ${i + 1}`,
    description: `Description for item ${i + 1}`,
    type: 'Type',
    status: 'Active',
    date: '01/10/2024',
  }));

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
                  <p className={Styles.option}>{currentTranslations.processes}</p>
                  <p className={Styles.option}>{currentTranslations.tasks}</p>
                  <p className={Styles.option}>{currentTranslations.activities}</p>
                  <p className={Styles.option}>Robots</p>
                </div>
              </div>
            </form>
          </div>

          {/* Contenedor con scroll y box-shadow */}
          <div className={Styles.items_container}>
            {items.map((item) => (
              <div key={item.id} className={Styles.item}>
                <div>
                  <h1>{item.title}</h1>
                  <h2>{item.description}</h2>
                </div>
                <div
                  className={Styles.type}
                  style={{ width: '70px', background: '#141b7b' }}
                >
                  {item.type}
                </div>
                <div
                  className={Styles.status}
                  style={{ width: '70px', background: 'green' }}
                >
                  {item.status}
                </div>
                <div
                  className={Styles.date}
                  style={{ width: '100px', background: '#736766' }}
                >
                  {item.date}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={Styles.container} style={{ margin: '10px' }}>
          <BarChart />
        </div>
      </div>
    </div>
  );
};

export default AnalisisPage;