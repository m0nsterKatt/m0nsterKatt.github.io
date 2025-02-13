import { onAuthStateChanged } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";
import { auth, loginWithGoogle, logout } from "./firebase.js";

// Elementos del DOM
const loginBtn = document.getElementById("loginBtn");
const logoutBtn = document.getElementById("logoutBtn");
const userGreeting = document.getElementById("userGreeting");

// Verificar si el usuario está autenticado
onAuthStateChanged(auth, (user) => {
  if (user) {
    userGreeting.textContent = `Bienvenido, ${user.displayName}`;
    userGreeting.style.display = "block";
    loginBtn.style.display = "none";
    logoutBtn.style.display = "block";
  } else {
    userGreeting.style.display = "none";
    loginBtn.style.display = "block";
    logoutBtn.style.display = "none";
  }
});

// Iniciar sesión con Google
loginBtn.addEventListener("click", async () => {
  await loginWithGoogle();
});

// Cerrar sesión
logoutBtn.addEventListener("click", async () => {
  await logout();
});
