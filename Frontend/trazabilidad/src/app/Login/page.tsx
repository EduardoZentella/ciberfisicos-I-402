// src/app/login/page.tsx
import styles from '../UI/Login/Login.module.css';
import LoginForm from '../UI/Login/LoginForm/LoginForm';

const LoginPage = () => {
  return (
    <div className={styles.container}>
      <LoginForm />
    </div>
  );
};

export default LoginPage;
