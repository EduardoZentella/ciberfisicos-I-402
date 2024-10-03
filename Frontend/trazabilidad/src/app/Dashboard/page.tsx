// src/app/Dashboard/page.tsx
import cardStyles from '../UI/Dashboard/Card.module.css'
import Image from 'next/image';
import processImage from '../UI/images/Home/home_1.jpg'; 

const DashboardHomePage = () => {
  return (
    <div style={{backgroundColor:'#d9d9de', height: "100vh"}}>

      <div className={cardStyles.title}>
        Processes
      </div>
      
      <div className={cardStyles.cardWrapper}>
        <div className={cardStyles.card}>
          <div className={cardStyles.imgArea}>
            <Image src={processImage} alt=""/>
            <div className={cardStyles.overlay}>
              <a href='/Dashboard/Process/1'>
                <button className={cardStyles.viewDetails}>View Details</button>
              </a>
            </div>
          </div>
          <div className={cardStyles.info}>
          <h3>Smart Track</h3>
          <p>Smart Factory Tec</p>
          <h4>Transporte de componentes y clasificación de piezas mediante robots manipuladores</h4>
        </div>
        </div>
      </div>   
    
    </div>
  );
};

export default DashboardHomePage;