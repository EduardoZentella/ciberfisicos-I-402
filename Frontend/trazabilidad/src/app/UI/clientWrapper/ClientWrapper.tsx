'use client';

import React, { useEffect } from 'react';
import { useLanguage } from '../../context/LanguageContext';

const HtmlLangUpdater: React.FC = () => {
  const { currentLanguage } = useLanguage();

  useEffect(() => {
    document.documentElement.lang = currentLanguage;
  }, [currentLanguage]);

  return null;
};

const ClientWrapper: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  return (
    <>
      <HtmlLangUpdater />
      {children}
    </>
  );
};

export default ClientWrapper;
