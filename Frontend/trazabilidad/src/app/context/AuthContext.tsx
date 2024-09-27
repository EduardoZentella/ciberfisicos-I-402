// src/context/AuthContext.tsx
'use client';
import Cookies from 'js-cookie';
import React, { createContext, useContext, useEffect, useState } from 'react';

interface AuthContextProps {
  user: { email: string } | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextProps | undefined>(undefined);

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<{ email: string } | null>(null);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const savedToken = Cookies.get('token');
    const savedEmail = Cookies.get('email');
    if (savedToken && savedEmail) {
      setToken(savedToken);
      setUser({ email: savedEmail });
    }
  }, []);

  const login = async (email: string, password: string) => {
    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();
      if (data.token) {
        Cookies.set('token', data.token);
        Cookies.set('email', email);
        setToken(data.token);
        setUser({ email });
      }
    } catch (error) {
      console.error('Error during login:', error);
    }
  };

  const logout = () => {
    Cookies.remove('token');
    Cookies.remove('email');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

const useAuth = () => {
  const context = useContext(AuthContext);
  console.log(context);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export { AuthProvider, useAuth };
