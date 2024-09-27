/* 'use client';

import { faChartBar, faHome } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React from 'react';

interface SidebarInsideProps {
  isExpanded: boolean;
  currentTranslations: any;
}

const SidebarInside: React.FC<SidebarInsideProps> = ({
  isExpanded,
  currentTranslations,
}) => {
  return (
    <div
      className={`flex-1 custom-scrollbar ${isExpanded ? 'overflow-y-auto' : 'hover:overflow-y-auto'}`}
      style={{ marginTop: isExpanded ? '6rem' : '5rem' }}
    >
      <div className="menu-items flex flex-col mt-4 transition-all duration-300">
        <div
          className={`menu-item flex items-center p-4 cursor-pointer hover:bg-purple-600 rounded-md transition-all duration-300 ${isExpanded ? 'justify-start' : 'justify-center'}`}
        >
          <FontAwesomeIcon
            icon={faHome}
            size={isExpanded ? 'lg' : '2x'}
            className="transition-all duration-300"
          />
          {isExpanded && (
            <span className="menu-text ml-4 text-lg transition-all duration-300">
              {currentTranslations.home}
            </span>
          )}
        </div>
        <div
          className={`menu-item flex items-center p-4 cursor-pointer hover:bg-purple-600 rounded-md transition-all duration-300 ${isExpanded ? 'justify-start' : 'justify-center'}`}
        >
          <FontAwesomeIcon
            icon={faChartBar}
            size={isExpanded ? 'lg' : '2x'}
            className="transition-all duration-300"
          />
          {isExpanded && (
            <span className="menu-text ml-4 text-lg transition-all duration-300">
              {currentTranslations.analysis}
            </span>
          )}
        </div>
      </div>
    </div>
  );
};

export default SidebarInside;
 */
