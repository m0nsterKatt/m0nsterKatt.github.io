// Importar Firebase
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import {
  getAuth,
  GoogleAuthProvider,
  signInWithPopup,
  signOut,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";
import { getFirestore } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";

// Configuración de Firebase (Reemplaza con tus datos)
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
const db = getFirestore(app);
const provider = new GoogleAuthProvider();

// Función para iniciar sesión con Google
export async function login() {
  try {
    const result = await signInWithPopup(auth, provider);
    const user = result.user;
    localStorage.setItem("user", JSON.stringify(user));
    window.location.href = "diario.html"; // Redirigir al diario
  } catch (error) {
    console.error("Error al iniciar sesión:", error);
  }
}

// Función para cerrar sesión
export async function logout() {
  await signOut(auth);
  localStorage.removeItem("user");
  localStorage.removeItem("displayName");
  window.location.href = "index.html";
}

// Exportar módulos
export { auth, db };
