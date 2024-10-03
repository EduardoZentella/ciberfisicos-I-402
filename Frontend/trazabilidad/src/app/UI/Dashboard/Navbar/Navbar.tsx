'use client';

import { useLanguage } from '@/app/lib/context/LanguageContext';
import Image from 'next/image';
import images from '../../images/smart_factory.png';
import styles from './Navbar.module.css';

const Navbar: React.FC = () => {
  const { changeLanguage, currentLanguage } = useLanguage();

  return (
    <nav className={styles.navbar}>
      <div className={styles.logoContainer}>
        <Image src={images} alt="SmartTec logo" className={styles.logoImage} />
        <span className={styles.logoText}>SmartTec</span>
      </div>

      <div className={styles.accesContainer}>
        <div className={styles.languageSwitcher}>
          <button
            onClick={() => changeLanguage('en')}
            className={`${styles.languageButton} ${currentLanguage === 'en' ? styles.active : ''}`}
          >
            EN
          </button>
          <button
            onClick={() => changeLanguage('es')}
            className={`${styles.languageButton} ${currentLanguage === 'es' ? styles.active : ''}`}
          >
            ES
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
