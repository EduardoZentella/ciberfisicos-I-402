// ProcesoComponent.tsx
'use client';
import { Proceso } from '@/app/lib/models';
import RestHandler from '@/app/lib/rest';
import { useEffect, useState } from 'react';

const ProcesoComponent = () => {
  const [procesos, setProcesos] = useState<Proceso[]>([]);

  useEffect(() => {
    const fetchProcesos = async () => {
      try {
        const data = await RestHandler<Proceso[]>(
          '/procesos',
          'Proceso',
          'GET'
        );
        setProcesos(data);
      } catch (error) {
        console.error('Error fetching procesos:', error);
      }
    };

    fetchProcesos();
  }, []);

  if (procesos.length === 0) return <div>Loading...</div>;

  return (
    <div>
      <h1>Procesos</h1>
      <ul>
        {procesos.map((proceso) => (
          <li key={proceso.Proceso_Id}>
            {proceso.Nombre}: {proceso.Descripcion}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProcesoComponent;
