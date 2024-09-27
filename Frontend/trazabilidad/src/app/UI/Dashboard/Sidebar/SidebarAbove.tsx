/* 'use client';

import { faArrowLeft, faBars } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import Image from 'next/image';
import React from 'react';
import SmartTecLogo from '../../assets/images/SmartTec_logo.ico';

interface SidebarAboveProps {
  isExpanded: boolean;
  toggleSidebar: () => void;
}

const SidebarAbove: React.FC<SidebarAboveProps> = ({
  isExpanded,
  toggleSidebar,
}) => {
  return (
    <>
      <div
        className="sidebar-header flex items-center justify-between p-4 fixed top-0 left-0 z-10 bg-purple-700 transition-all duration-300"
        style={{ width: isExpanded ? '16rem' : '5.5rem', overflow: 'hidden' }}
      >
        <div
          className={`logo-container flex items-center ${isExpanded ? 'logo-expanded justify-start' : 'logo-collapsed justify-center'} w-full`}
        >
          <div
            className={`logo relative ${isExpanded ? 'w-12 h-12' : 'w-10 h-10'} rounded-full overflow-hidden`}
          >
            <Image
              src={SmartTecLogo}
              alt="SmartTec Logo"
              fill
              className="logo-image object-cover rounded-full"
            />
          </div>
          {isExpanded && (
            <div className="logo-text ml-4 text-lg font-semibold">SmartTec</div>
          )}
        </div>
        <button
          onClick={toggleSidebar}
          className="toggle-button focus:outline-none ml-2"
        >
          <FontAwesomeIcon icon={isExpanded ? faArrowLeft : faBars} size="lg" />
        </button>
      </div>
      <div
        className="divider border-b border-white my-4 shadow-md shadow-white transition-all duration-300"
        style={{
          position: 'fixed',
          top: isExpanded ? '5rem' : '4rem',
          width: isExpanded ? '16rem' : '5.5rem',
        }}
      ></div>
    </>
  );
};

export default SidebarAbove;
 */
