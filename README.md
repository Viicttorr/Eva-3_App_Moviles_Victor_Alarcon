# 📱 Evaluación 3 - Aplicación Android con Firebase

Aplicación desarrollada en **Android Studio** como continuación de la **Evaluación 2**, integrando nuevas funcionalidades y conexión con **Firebase Firestore**.  
El proyecto consolida el flujo de autenticación, navegación y gestión de noticias, aplicando buenas prácticas de UI/UX.

---

## 🚀 Características principales

- **Splash Screen + Ícono personalizado** (heredado de EV2).
- **Login con Firebase Authentication (Google)**.
- **Recuperar contraseña**: valida usuario en Firebase y asigna nueva clave en pantalla.
- **Registrar cuenta**: crea usuario en Firebase Authentication.
- **Home (Listado de noticias)**: muestra noticias desde **Cloud Firestore** en una lista.
- **Ver noticia**: despliega el detalle completo de una noticia seleccionada.
- **Agregar noticia**: formulario para crear una noticia con:
  - Título
  - Resumen
  - Contenido
  - Autor
  - Fecha
- **Cerrar sesión** con botón dedicado.
- **AlertDialog** para confirmaciones y errores.
- **Diseño personalizado**: colores, tipografías y estilos consistentes.

---

## 🎯 Objetivos de aprendizaje

1. Integrar una app Android con **Firebase Firestore** (lectura y escritura).
2. Implementar navegación entre **Activities**.
3. Aplicar buenas prácticas de **UI/UX** y validaciones.
4. Consolidar el flujo completo:  
   `Splash → Login → Home → Ver/Agregar noticia`.
5. Conectar la app a un **BackEnd**.

---

## 🛠️ Tecnologías utilizadas

- **Android Studio**
- **Java/Kotlin**
- **Firebase Authentication (Google)**
- **Firebase Firestore**

---

## 📂 Estructura del proyecto

- `/app/Kotlin+java/com.example.eva_2_app_moviles/...` → Código fuente (Activities, lógica de negocio).
- `/app/res/layout` → Interfaces gráficas (XML).
- `/app/res/drawable` → Íconos e imágenes.
- `/app/res/values` → Colores, estilos y tipografías.

---

## 📖 Instalación y ejecución

1. Clonar el repositorio.
2. Abrir el proyecto en Android Studio.
3. Configurar conexión a Firebase:
   - Agregar archivo google-services.json en /app.
   - Habilitar Authentication (Google) y Firestore en Firebase Console.
6. Ejecutar desde el menú Run.

---

## 👨‍💻 Autor
- Victor Alarcon — Desarrollo y diseño de la aplicación.
- Proyecto académico — Inacap, 2025.

---

## 📜 Licencia
Este proyecto se distribuye bajo la licencia MIT.
Puedes usarlo, modificarlo y compartirlo libremente, siempre mencionando la autoría original.
