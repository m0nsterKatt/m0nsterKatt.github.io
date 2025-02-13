// Importar Firebase
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import {
  getAuth,
  GoogleAuthProvider,
  signInWithPopup,
  signOut,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";

// Configuración de Firebase (Usa tus datos reales)
const firebaseConfig = {
  apiKey: "AIzaSyDSZZvxh9k6AAtMw8AgjXO04zac21orbbM",
  authDomain: "diario-bd979.firebaseapp.com",
  projectId: "diario-bd979",
  storageBucket: "diario-bd979.firebasestorage.app",
  messagingSenderId: "63678041693",
  appId: "1:63678041693:web:8a8cf6d01c2ba7d9dec7b4",
  measurementId: "G-EG16YK73MB",
};

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

// Funciones para autenticación
export const loginWithGoogle = async () => {
  try {
    const result = await signInWithPopup(auth, provider);
    return result.user;
  } catch (error) {
    console.error("Error al iniciar sesión:", error);
  }
};

export const logout = async () => {
  try {
    await signOut(auth);
  } catch (error) {
    console.error("Error al cerrar sesión:", error);
  }
};

// Exportar auth para usarlo en script.js
export { auth };
