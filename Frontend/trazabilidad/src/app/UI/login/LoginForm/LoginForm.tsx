// src/UI/login/loginForm/LoginForm.tsx
'use client';

import { loginHandler } from '@/app/lib/auth';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import styles from './loginForm.module.css';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await loginHandler(email, password);
      router.push('/Dashboard'); // Redirigir a la página de dashboard
    } catch (error) {
      if (error instanceof Error) {
        console.error('Error:', error.message);
      } else {
        console.error('An unknown error occurred');
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <h1>Login</h1>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
        name="email"
        className={styles.input}
        required
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
        name="password"
        className={styles.input}
        required
      />
      <button type="submit" className={styles.button}>
        Login
      </button>
    </form>
  );
};

export default LoginForm;
