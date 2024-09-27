/* 'use client';

import {
  faChevronDown,
  faChevronUp,
  faCog,
  faSignOutAlt,
  faUser,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';

interface SidebarBelowProps {
  isExpanded: boolean;
  isUserMenuOpen: boolean;
  toggleUserMenu: () => void;
  currentTranslations: any;
  currentLanguage: string;
  changeLanguage: (_lang: string) => void;
}

const SidebarBelow: React.FC<SidebarBelowProps> = ({
  isExpanded,
  isUserMenuOpen,
  toggleUserMenu,
  currentTranslations,
  currentLanguage,
  changeLanguage,
}) => {
  return (
    <>
      <div
        className="user-section fixed bottom-0 left-0 z-10 bg-purple-700 transition-all duration-300"
        style={{ width: isExpanded ? '16rem' : '5.5rem' }}
      >
        <div className="divider border-b border-white my-4 shadow-md shadow-white transition-all duration-300"></div>
        <div className="menu-items flex flex-col">
          <div
            className="menu-item user-menu flex items-center justify-between p-4 cursor-pointer hover:bg-purple-600 rounded-md"
            onClick={toggleUserMenu}
          >
            <div className="user-menu-header flex items-center">
              <FontAwesomeIcon icon={faUser} size={isExpanded ? 'lg' : '2x'} />
              {isExpanded ? (
                <div className="user-menu-text ml-4 text-lg flex items-center">
                  {currentTranslations.user}
                  <FontAwesomeIcon
                    icon={isUserMenuOpen ? faChevronUp : faChevronDown}
                    className="user-menu-icon ml-2 transition-transform duration-300"
                  />
                </div>
              ) : (
                <FontAwesomeIcon
                  icon={isUserMenuOpen ? faChevronUp : faChevronDown}
                  className="user-menu-icon ml-2 transition-transform duration-300"
                />
              )}
            </div>
          </div>
          <div
            className={`user-menu-items flex flex-col p-2 transition-all duration-300 ${isUserMenuOpen ? 'user-menu-open max-h-40 opacity-100' : 'user-menu-closed max-h-0 opacity-0 overflow-hidden'}`}
          >
            <div
              className={`menu-item flex items-center p-2 cursor-pointer hover:bg-purple-600 rounded-md ${isExpanded ? 'justify-start' : 'justify-center'}`}
            >
              <FontAwesomeIcon icon={faCog} size={isExpanded ? 'lg' : '2x'} />
              {isExpanded && (
                <span className="menu-text ml-4 text-lg">
                  {currentTranslations.settings}
                </span>
              )}
            </div>
            <div
              className={`menu-item flex items-center p-4 cursor-pointer hover:bg-purple-600 rounded-md ${isExpanded ? 'justify-start' : 'justify-center'}`}
            >
              <FontAwesomeIcon
                icon={faSignOutAlt}
                size={isExpanded ? 'lg' : '2x'}
              />
              {isExpanded && (
                <span className="menu-text ml-4 text-lg whitespace-nowrap">
                  {currentTranslations.logout}
                </span>
              )}
            </div>
          </div>
        </div>
        <div className="language-switcher flex justify-center mt-4 mb-4">
          {isExpanded ? (
            <div className="inline-flex rounded-md shadow-sm">
              <button
                onClick={() => changeLanguage('en')}
                className={`px-4 py-2 rounded-l-md border border-gray-200 ${currentLanguage === 'en' ? 'bg-white text-purple-700' : 'bg-purple-600 text-white'}`}
              >
                EN
              </button>
              <button
                onClick={() => changeLanguage('es')}
                className={`px-4 py-2 rounded-r-md border border-gray-200 ${currentLanguage === 'es' ? 'bg-white text-purple-700' : 'bg-purple-600 text-white'}`}
              >
                ES
              </button>
            </div>
          ) : (
            <button
              onClick={() =>
                changeLanguage(currentLanguage === 'en' ? 'es' : 'en')
              }
              className="mx-2 px-4 py-2 bg-white text-purple-700 rounded-md hover:bg-gray-200 transition"
            >
              {currentLanguage.toUpperCase()}
            </button>
          )}
        </div>
      </div>
    </>
  );
};

export default SidebarBelow;
 */
