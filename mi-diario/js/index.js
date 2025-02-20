import { onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";
import { auth, login } from "./firebase.js";

// Obtener elementos del DOM
const loginBtn = document.getElementById("loginBtn");
const userGreeting = document.getElementById("userGreeting");

// Verificar si el usuario está autenticado
onAuthStateChanged(auth, (user) => {
  if (user) {
    console.log("Usuario autenticado:", user);
    localStorage.setItem("user", JSON.stringify(user)); // Guardar el usuario en localStorage

    // Mostrar mensaje de bienvenida si existe el elemento
    if (userGreeting) {
      userGreeting.textContent = `Bienvenido, ${user.displayName}`;
      userGreeting.style.display = "block";
    }

    // Si estamos en `index.html`, redirigir al diario
    if (window.location.pathname.includes("index.html")) {
      window.location.href = "diario.html";
    }
  } else {
    console.log("Ningún usuario autenticado.");
    localStorage.removeItem("user"); // Borrar datos del usuario si no está autenticado

    if (userGreeting) {
      userGreeting.style.display = "none";
    }

    // Si el usuario no está autenticado y no estamos en index.html, redirigirlo
    if (!window.location.pathname.includes("index.html")) {
      window.location.href = "index.html";
    }
  }
});

// Iniciar sesión con Google
loginBtn?.addEventListener("click", async () => {
  await login();
});
