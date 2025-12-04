# Proyecto: Dockerización y Primer Incremento del Sistema

## Descripción General
Este proyecto corresponde al primer incremento funcional del Sistema de Gestión de Menús  El objetivo es permitir la administración centralizada de:
- Empresas
- Usuarios
- Menús
- Categorías
- Productos

Todo el sistema se encuentra dockerizado para permitir su puesta en marcha mediante un solo comando utilizando **Docker Compose**, asegurando portabilidad, estandarización e independencia de configuraciones locales.

---

## Tecnologías Utilizadas

| Capa | Tecnología |
|------|------------|
| Frontend | React + Vite, JavaScript, CSS Modules |
| Backend | Java 17, Spring Boot, Spring Security, Spring Data JPA |
| Base de Datos | PostgreSQL (Imagen oficial Docker) |
| Contenedores | Docker |
| Orquestación | Docker Compose |
| Control de Versiones | Git / GitHub |

---

## Incremento Frontend (Interfaz Administrativa)

### Descripción
El frontend es una aplicación desarrollada en **React + Vite**, orientada a la gestión operativa desde una interfaz administrativa. La aplicación se comunica con el backend mediante solicitudes HTTP y utiliza token JWT para mantener la sesión activa.

### Contenido del Repositorio
```
men--digital-main/
├── src/
│   ├── auth/          # Módulos de autenticación y control de acceso
│   ├── services/      # Llamadas a la API REST
│   ├── components/    # Componentes reutilizables
│   ├── views/         # Pantallas CRUD del sistema
│   └── styles/        # Estilos globales
├── Dockerfile          # Imagen Docker del frontend
└── vite.config.js
```

### Funcionalidades Incluidas
- Inicio de sesión con token JWT.
- Panel general de administración.
- CRUD completo de **Empresas**, **Usuarios**, **Menús**, **Categorías** y **Productos**.
- Control de roles y acceso restringido mediante guardias de rutas.

### Configuración del Backend en el Frontend
Archivo:
```
men--digital-main/src/config/api.js
```
Valor inicial sugerido:
```js
export const API_BASE_URL = "http://localhost:8080";
```

---

## Incremento Backend (API REST)

### Descripción
El backend fue desarrollado utilizando **Spring Boot**, proporcionando los servicios REST necesarios para la gestión del sistema. Se implementa autenticación con **JWT** y acceso a la base de datos mediante **JPA**.

### Contenido del Repositorio
```
menu-backend-public/
├── controller/     # Endpoints REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
├── entity/         # Modelado de tablas
├── security/       # Autenticación y autorización con JWT
├── resources/
│   └── application.yml
└── Dockerfile       # Imagen Docker del backend
```

### Funcionalidades Implementadas
| Módulo | Funcionalidad |
|--------|--------------|
| Autenticación | Inicio de sesión y validación mediante JWT |
| Empresas | CRUD completo |
| Usuarios | CRUD con control de rol y estado |
| Menús | CRUD vinculado a empresa |
| Categorías | CRUD vinculado a menús |
| Productos | CRUD vinculado a categorías |

---

## Base de Datos
- Motor: **PostgreSQL**
- Tablas generadas mediante entidades JPA
- Persistencia garantizada utilizando volúmenes Docker

---

## Dockerización del Sistema

El archivo principal para levantar todo el sistema es:
```
docker-compose.yml
```

### Levantar el Proyecto
```bash
git clone -b main --single-branch https://github.com/Steven2623/men--digital.git

cd men--digital

docker compose up --build
```

### Acceso a los Servicios

| Servicio | URL |
|---------|------|
| Frontend | http://localhost:4200 |
| Backend | http://localhost:8080 |
| PostgreSQL | localhost:5432 |


## Resultado del Incremento

| Elemento | Estado | Descripción |
|---------|--------|-------------|
| Frontend | Completo | Interfaz administrativa funcional |
| Backend |  Completo | API REST operativa y protegida |
| Base de Datos |  Configurada | Persistencia estable y estructurada |
| Dockerización |  Finalizada | El sistema se ejecuta con un solo comando |

---

