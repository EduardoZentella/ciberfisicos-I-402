'use client';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHome, faCog, faSignOutAlt, faUser } from '@fortawesome/free-solid-svg-icons';
import { useLanguage } from '../../../context/LanguageContext';
import styles from './Navbar.module.css';
import images from '../../images/smart_factory.png';
import Image from 'next/image';

const Navbar: React.FC = () => {
  const { currentTranslations, changeLanguage, currentLanguage } = useLanguage();

  const handleLogout = () => {
    // Implementar la lógica de cierre de sesión
    console.log("User logged out");
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.logoContainer}>
        <Image 
          src={images}
          alt="SmartTec logo"
          className={styles.logoImage}
        />
        <span className={styles.logoText}>SmartTec</span>
      </div>

      <div className={styles.accesContainer}>

        <div>Loco </div>
        <div>LOCO</div>
        <div className={styles.languageSwitcher}>
        <button onClick={() => changeLanguage('en')} className={`${styles.languageButton} ${currentLanguage === 'en' ? styles.active : ''}`}>EN</button>
        <button onClick={() => changeLanguage('es')} className={`${styles.languageButton} ${currentLanguage === 'es' ? styles.active : ''}`}>ES</button>
      </div>
      </div>
   
      
     
      
      
    </nav>
  );
};

export default Navbar;
