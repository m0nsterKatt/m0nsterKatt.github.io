import { onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";
import { auth, loginWithGoogle, logout } from "./firebase.js";

// Obtener elementos del DOM
const loginBtn = document.getElementById("loginBtn");
const logoutBtn = document.getElementById("logoutBtn");
const userGreeting = document.getElementById("userGreeting");

// Verificar si el usuario está autenticado
onAuthStateChanged(auth, (user) => {
  if (user) {
    console.log("Usuario autenticado:", user);
    localStorage.setItem("user", JSON.stringify(user)); // Guardamos el usuario en localStorage
    userGreeting.textContent = `Bienvenido, ${user.displayName}`;
    userGreeting.style.display = "block";
    loginBtn.style.display = "none";
    logoutBtn.style.display = "block";
  } else {
    console.log("Ningún usuario autenticado.");
    userGreeting.style.display = "none";
    loginBtn.style.display = "block";
    logoutBtn.style.display = "none";
    localStorage.removeItem("user"); // Borrar datos del usuario si no está autenticado
  }
});

// Iniciar sesión con Google
loginBtn?.addEventListener("click", async () => {
  await loginWithGoogle();
  // Redirigir a la página del diario después del login
  window.location.href = "diario.html";
});

// Cerrar sesión
logoutBtn?.addEventListener("click", async () => {
  await logout();
  localStorage.removeItem("user");
  window.location.href = "index.html"; // Volver al login
});
