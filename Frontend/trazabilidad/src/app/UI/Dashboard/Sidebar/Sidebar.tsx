'use client';

import {
  faChartBar,
  faChevronDown,
  faChevronLeft,
  faChevronRight,
  faChevronUp,
  faCog,
  faHome,
  faSignOutAlt,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import Link from 'next/link';
import React, { useEffect, useRef, useState } from 'react';
import { useLanguage } from '../../../lib/context/LanguageContext';
import SmartTecLogo from '../../images/SmartTec_logo.ico';
import styles from './Sidebar.module.css';

const Sidebar: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);

  const sidebarRef = useRef<HTMLDivElement>(null);
  const { currentTranslations, changeLanguage, currentLanguage } =
    useLanguage();

  const toggleSidebar = () => {
    setIsExpanded(!isExpanded);
  };

  const toggleUserMenu = () => {
    setIsUserMenuOpen(!isUserMenuOpen);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        sidebarRef.current &&
        !sidebarRef.current.contains(event.target as Node)
      ) {
        setIsExpanded(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const menuItems = [
    {
      title: currentTranslations.pages,
      list: [
        {
          title: currentTranslations.dashboard,
          path: '/Dashboard',
          icon: faHome,
        },
        {
          title: currentTranslations.analysis,
          path: '/Dashboard/Analisis',
          icon: faChartBar,
        },
      ],
    },
    {
      title: currentTranslations.users,
      list: [
        {
          title: currentTranslations.user,
          icon: faUser,
          subItems: [
            {
              title: currentTranslations.settings,
              path: '/Dashboard/settings',
              icon: faCog,
            },
            {
              title: currentTranslations.logout,
              path: '/logout',
              icon: faSignOutAlt,
            },
          ],
        },
      ],
    },
  ];

  return (
    <div className={styles.container}>
      {isExpanded && <div className={styles.overlay}></div>}
      <div
        ref={sidebarRef}
        className={`${styles.sidebar} ${isExpanded ? styles.sidebarExpanded : styles.sidebarCollapsed}`}
      >
        <div className={styles.logoContainer}>
          <div className={styles.logo}>
            <Image
              src={SmartTecLogo}
              alt="SmartTec Logo"
              className={styles.logoImage}
            />
          </div>
          {isExpanded && <div className={styles.logoText}>SmartTec</div>}
        </div>
        <ul className={styles.menu}>
          {menuItems.map((category, catIndex) => (
            <li key={catIndex} className={styles.category}>
              <span className={styles.categoryTitle}>{category.title}</span>
              <ul>
                {category.list.map((item, itemIndex) => (
                  <li key={itemIndex}>
                    <Link
                      href={'path' in item ? item.path : '#'}
                      className={styles.link}
                    >
                      <div
                        className={styles.item}
                        onClick={
                          'subItems' in item ? toggleUserMenu : undefined
                        }
                      >
                        <FontAwesomeIcon
                          icon={item.icon}
                          className={`${styles.icon} ${styles.transition}`}
                        />
                        {isExpanded && (
                          <span className={styles.itemText}>{item.title}</span>
                        )}
                        {'subItems' in item && (
                          <FontAwesomeIcon
                            icon={isUserMenuOpen ? faChevronUp : faChevronDown}
                            className={`${styles.icon} ${styles.transition} ${styles.userMenuIcon}`}
                          />
                        )}
                      </div>
                    </Link>
                    {'subItems' in item && (
                      <ul
                        className={`${styles.subMenu} ${isUserMenuOpen ? styles.subMenuOpen : ''}`}
                      >
                        {item.subItems.map((subItem, subItemIndex) => (
                          <li key={subItemIndex}>
                            <Link href={subItem.path} className={styles.link}>
                              <div className={styles.subItem}>
                                <FontAwesomeIcon
                                  icon={subItem.icon}
                                  className={`${styles.icon} ${styles.transition} ${!isExpanded ? styles.collapsedIcon : ''}`}
                                />
                                {isExpanded && (
                                  <span className={styles.subItemText}>
                                    {subItem.title}
                                  </span>
                                )}
                              </div>
                            </Link>
                          </li>
                        ))}
                      </ul>
                    )}
                  </li>
                ))}
              </ul>
            </li>
          ))}
        </ul>
        <div className={styles.languageSwitcher}>
          {isExpanded ? (
            <div className="inline-flex rounded-md shadow-sm">
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
          ) : (
            <button
              onClick={() =>
                changeLanguage(currentLanguage === 'en' ? 'es' : 'en')
              }
              className={`${styles.languageButton} ${styles.collapsed}`}
            >
              {currentLanguage.toUpperCase()}
            </button>
          )}
        </div>
        <div className={styles.toggleButtonLip}>
          <button onClick={toggleSidebar} className={styles.toggleButton}>
            <FontAwesomeIcon
              icon={isExpanded ? faChevronLeft : faChevronRight}
              className={styles.icon}
            />
          </button>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
