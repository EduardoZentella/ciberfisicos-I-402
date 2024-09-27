import { Inter } from 'next/font/google';
import { LanguageProvider } from './context/LanguageContext';
import ClientWrapper from './UI/clientWrapper/ClientWrapper';

const inter = Inter({ subsets: ['latin'] });

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <LanguageProvider>
          <ClientWrapper>{children}</ClientWrapper>
        </LanguageProvider>
      </body>
    </html>
  );
}
