// src/app/login/page.tsx
import styles from '@/app/UI/Login/Login.module.css';
import LoginForm from '@/app/UI/Login/LoginForm/LoginForm';

const LoginPage = () => {
  return (
    <div className={styles.container}>
      <LoginForm />
    </div>
  );
};

export default LoginPage;
