// src/context/LanguageContext.tsx
'use client';

import React, {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from 'react';
import enTranslations from '../UI/translations/en.yaml';
import esTranslations from '../UI/translations/es.yaml';

interface Translations {
  [key: string]: string;
}

const translations: { [key: string]: Translations } = {
  en: enTranslations,
  es: esTranslations,
};

interface LanguageContextProps {
  currentLanguage: string;
  currentTranslations: Translations;
  changeLanguage: (_lang: string) => void;
}

const LanguageContext = createContext<LanguageContextProps | null>(null);

interface LanguageProviderProps {
  children: ReactNode;
}

export const LanguageProvider: React.FC<LanguageProviderProps> = ({
  children,
}) => {
  const [currentLanguage, setLanguage] = useState<string>(
    navigator.language.startsWith('es') ? 'es' : 'en'
  );
  const [currentTranslations, setCurrentTranslations] = useState<Translations>(
    translations[currentLanguage]
  );

  useEffect(() => {
    setCurrentTranslations(translations[currentLanguage]);
  }, [currentLanguage]);

  const changeLanguage = (lang: string) => {
    setLanguage(lang);
  };

  return (
    <LanguageContext.Provider
      value={{ currentLanguage, currentTranslations, changeLanguage }}
    >
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = () => {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguage must be used within a LanguageProvider');
  }
  return context;
};
