# RECICLAPP - Aplicación de Reciclaje para Bogotá

## Descripción
RECICLAPP es una aplicación móvil diseñada para facilitar y promover el reciclaje en Bogotá, conectando recicladores, empresas y personas interesadas en el manejo responsable de residuos reciclables.

## Características Principales

### 1. Gestión de Usuarios
- Registro de usuarios con diferentes roles:
  - Empresas
  - Recicladores
  - Personas
  - Administradores
- Autenticación segura
- Perfiles personalizados

### 2. Gestión de Materiales
- Publicación de materiales reciclables
- Búsqueda y filtrado de materiales
- Sistema de valoración (1-5 estrellas)
- Ubicación de puntos de reciclaje
- Descripción detallada de materiales

### 3. Tips de Reciclaje
- Consejos para diferentes tipos de materiales
- Guías de separación
- Mejores prácticas de reciclaje
- Contenido educativo

### 4. Sistema de Transacciones
- Gestión de intercambios
- Historial de transacciones
- Estados de transacciones (pendiente, completado, cancelado)
- Notificaciones en tiempo real

## Requisitos Técnicos

### Requisitos del Sistema
- Android SDK 24 o superior
- Java 8
- Gradle 7.0 o superior

### Dependencias Principales
- AndroidX Core Libraries
- Material Design Components
- Room Database
- Google Play Services Location
- Glide para manejo de imágenes

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/reciclapp.git
```

2. Abrir el proyecto en Android Studio

3. Sincronizar el proyecto con Gradle

4. Ejecutar la aplicación en un emulador o dispositivo físico

## Estructura del Proyecto

```
RECICLAPP/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/recicapp/
│   │   │   │   ├── activities/
│   │   │   │   ├── adapters/
│   │   │   │   ├── database/
│   │   │   │   └── managers/
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       ├── menu/
│   │   │       ├── values/
│   │   │       └── xml/
│   │   └── androidTest/
│   └── build.gradle
└── build.gradle
```

## Base de Datos

### Tablas Principales
1. **users**
   - Información de usuarios
   - Roles y permisos

2. **materials**
   - Detalles de materiales reciclables
   - Ubicación y cantidad

3. **transactions**
   - Registro de intercambios
   - Estados y fechas

4. **ratings**
   - Valoraciones de materiales
   - Comentarios

5. **recycling_tips**
   - Tips de reciclaje
   - Categorización por tipo de material

## Seguridad
- Autenticación de usuarios
- Validación de datos
- Permisos basados en roles
- Protección de datos sensibles

## Contribución
1. Fork el proyecto
2. Cree una rama para su característica (`git checkout -b feature/AmazingFeature`)
3. Commit sus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abra un Pull Request

## Licencia
Este proyecto está bajo la Licencia MIT - vea el archivo LICENSE.md para detalles

## Contacto
- Equipo de Desarrollo: dev@reciclapp.com
- Soporte: support@reciclapp.com
- Sitio Web: https://www.reciclapp.com

## Agradecimientos
- A la comunidad de recicladores de Bogotá
- A las empresas colaboradoras
- A todos los usuarios que hacen posible esta iniciativa
