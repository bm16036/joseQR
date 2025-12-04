# ==========================
# STAGE 1 - Build Angular App
# ==========================
FROM node:20-alpine AS build
WORKDIR /app

# Instalar dependencias
COPY package*.json ./
RUN npm install

# Copiar el resto del proyecto
COPY . .

# Compilar Angular — salida por defecto en /app/dist/menu-digital/browser
RUN npm run build

# ==========================
# STAGE 2 - Nginx Server
# ==========================
FROM nginx:1.25-alpine

# Limpiar archivos por defecto de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiar el build Angular al contenedor
COPY --from=build /app/dist/menu-digital/browser /usr/share/nginx/html

# 🔧 Renombrar index.csr.html si existe
RUN [ -f /usr/share/nginx/html/index.csr.html ] && mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html || true

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer el puerto
EXPOSE 80

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]

