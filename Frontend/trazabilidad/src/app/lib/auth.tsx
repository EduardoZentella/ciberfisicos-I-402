// src/app/lib/auth.ts
'use server';
import { cookies } from 'next/headers';

export async function authenticateUser(email: string, password: string) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/auth/authenticate`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, contraseña: password }),
    }
  );

  if (response.ok) {
    const data = await response.json();
    const { token } = data;
    return { token, success: true };
  } else {
    return { token: null, success: false };
  }
}

export async function loginHandler(email: string, password: string) {
  const { token, success } = await authenticateUser(email, password);

  if (success) {
    const cookieStore = cookies();
    const cookieValue = JSON.stringify({ token, password, email });
    cookieStore.set('auth', cookieValue, {
      httpOnly: true,
      secure: process.env.NODE_ENV !== 'development',
      maxAge: 60 * 60 * 24, // 1 día
      sameSite: 'strict',
      path: '/',
    });

    return { message: 'Authenticated' };
  } else {
    throw new Error('Authentication failed');
  }
}
