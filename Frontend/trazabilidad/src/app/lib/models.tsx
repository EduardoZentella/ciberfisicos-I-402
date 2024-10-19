// src/app/lib/models.tsx
export interface Usuario {
  Usuario_Id: number;
  Email: string;
  Contraseña: string;
  MasterKey: string;
  Version: string;
}

export interface Proceso {
  Proceso_Id: number;
  Nombre: string;
  Descripcion: string;
  Location: string; 
  Ini_Date: Date; 
  End_Date: Date; 
  Status: string; 
}

export interface Tarea {
  Tarea_Id: number;
  Nombre: string;
  Descripcion: string;
  Proceso_Id: number;
  Ini_Date: Date;
  End_Date: Date;
  Status: string;
}

export interface Actividad {
  Actividad_Id: number;
  Nombre: string;
  Descripcion: string;
  Tarea_Id: number;
}

export interface Robot {
  Robot_Id: number;
  Nombre: string;
  Descripcion: string;
  Type: string;
  Charge: string; 
}

export interface Robot_Proceso {
  Robot_Id: number;
  Proceso_Id: number;
  Ini_Date: Date;
  End_Date: Date;
  Status: string;
}

export interface Robot_Tarea {
  Robot_Id: number;
  Tarea_Id: number;
  Ini_Date: Date;
  End_Date: Date;
  Status: string;
}

export interface Robot_Actividad {
  Robot_Id: number;
  Actividad_Id: number;
  Ini_Date: Date;
  End_Date: Date;
  Status: string;
}

export interface Pieza {
  Pieza_Id: number; 
  Type: string; 
  Lote: number; 
}

export interface Lote {
  Proceso_Id: number;
}