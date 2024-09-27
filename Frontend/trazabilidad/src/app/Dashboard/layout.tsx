// src/app/layout.tsx
import Styles from '../UI/Dashboard/Dashboard.module.css';
import Navbar from '../UI/Dashboard/Navbar/Navbar';
import Sidebar from '../UI/Dashboard/Sidebar/Sidebar';
import '../UI/styles/globals.css';

const RootLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className={Styles.container}>
      <div className={Styles.menu}>
        <Sidebar />
      </div>
      <div className={Styles.content}>
        <Navbar />
        {children}
      </div>
    </div>
  );
};

export default RootLayout;
