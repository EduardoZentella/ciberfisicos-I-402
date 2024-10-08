// src/app/lib/rest.tsx
'use server';
import { cookies } from 'next/headers';
import {
  Actividad,
  Proceso,
  Robot,
  Robot_Actividad,
  Robot_Proceso,
  Robot_Tarea,
  Tarea,
  Usuario,
} from './models';

type DataType =
  | Usuario
  | Proceso
  | Tarea
  | Actividad
  | Robot
  | Robot_Proceso
  | Robot_Tarea
  | Robot_Actividad;

type RequestMethod = 'GET' | 'POST' | 'PUT' | 'DELETE';

const convertData = (data: any, type: string): DataType | DataType[] => {
  const parseDate = (dateString: string): Date => {
    return new Date(dateString);
  };

  const convertSingle = (item: any): DataType => {
    switch (type) {
      case 'Usuario':
        return {
          Usuario_Id: item.usuarioid,
          Email: item.email,
          Contraseña: item.contraseña,
          MasterKey: item.masterKey,
          Version: item.masterKeyVersion,
        } as Usuario;
      case 'Proceso':
        return {
          Proceso_Id: item.procesoid,
          Nombre: item.name,
          Location: item.location,
          Descripcion: item.description,
          Ini_Date: parseDate(item.iniDate),
          End_Date: parseDate(item.endDate), 
          Status: item.status,
        } as Proceso;
      case 'Tarea':
        return {
          Tarea_Id: item.tareaid,
          Nombre: item.name,
          Descripcion: item.description,
          Proceso_Id: item.procesoid,
          Ini_Date: parseDate(item.iniDate),
          End_Date: parseDate(item.endDate),
          Status: item.status,
        } as Tarea;
      case 'Actividad':
        return {
          Actividad_Id: item.actividadid,
          Nombre: item.name,
          Descripcion: item.desciption,
          Tarea_Id: item.tareaid,
        } as Actividad;
      case 'Robot':
        return {
          Robot_Id: item.robotid,
          Nombre: item.name,
          Type: item.type,
          Charge: item.charge,
        } as Robot;
      case 'Robot_Proceso':
        return {
          Robot_Id: item.id.robotid,
          Proceso_Id: item.id.procesoid,
          Ini_Date: parseDate(item.iniDate),
          End_Date: parseDate(item.endDate),
          Status: item.status,
        } as Robot_Proceso;
      case 'Robot_Tarea':
        return {
          Robot_Id: item.id.robotid,
          Tarea_Id: item.id.tareaid,
          Ini_Date: parseDate(item.iniDate),
          End_Date: parseDate(item.endDate),
          Status: item.status,
        } as Robot_Tarea;
      case 'Robot_Actividad':
        return {
          Robot_Id: item.id.robotid,
          Actividad_Id: item.id.actividadid,
          Ini_Date: parseDate(item.iniDate),
          End_Date: parseDate(item.endDate),
          Status: item.status,
        } as Robot_Actividad;
      default:
        throw new Error('Unknown data type');
    }
  };

  if (Array.isArray(data)) {
    return data.map(convertSingle);
  } else {
    return convertSingle(data);
  }
};

const RestHandler = async <T extends DataType | DataType[]>(
  apiPath: string,
  type: string,
  method: RequestMethod,
  body?: any
): Promise<T> => {
  const cookieStore = cookies();
  const authCookie = cookieStore.get('auth')?.value;

  if (!authCookie) {
    throw new Error('No authorization token found');
  }

  const { token } = JSON.parse(authCookie);

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_URL}${apiPath}`,
      {
        method,
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: method !== 'GET' ? JSON.stringify(body) : undefined,
      }
    );
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }

    const data = await response.json();
    console.log(data)
    return convertData(data, type) as T;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data');
  }
};

export default RestHandler;
