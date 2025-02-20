import {
  deleteDoc,
  doc,
  getDoc,
  getFirestore,
  setDoc,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";
import { logout } from "./firebase.js";

const db = getFirestore();
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

const userProfileForm = document.getElementById("userProfileForm");
const saveProfileBtn = document.getElementById("saveProfile");
const deleteAccountBtn = document.getElementById("deleteAccount");
const saveFeedback = document.getElementById("saveFeedback");
const userProfileBtn = document.getElementById("userProfileBtn");

// Cargar perfil del usuario desde Firebase y actualizar el menú lateral
async function loadUserProfile() {
  const userDoc = await getDoc(doc(db, "userProfiles", user.uid));
  let displayName = "Usuario"; // Valor por defecto

  if (userDoc.exists()) {
    const data = userDoc.data();
    document.getElementById("name").value = data.name || "";
    document.getElementById("surname").value = data.surname || "";
    document.getElementById("nickname").value = data.nickname || "";
    document.getElementById("birthdate").value = data.birthdate || "";
    document.getElementById("gender").value = data.gender || "";
    document.getElementById("zodiac").value = data.zodiac || "";

    // Definir nombre a mostrar en el menú lateral
    displayName = data.nickname?.trim() || data.name?.trim() || "Usuario";
  }

  userProfileBtn.textContent = `👤 ${displayName}`;
  localStorage.setItem("displayName", displayName);
}

// Guardar perfil en Firebase y actualizar el menú lateral
userProfileForm.addEventListener("submit", async (e) => {
  e.preventDefault();

  const profileData = {
    name: document.getElementById("name").value,
    surname: document.getElementById("surname").value,
    nickname: document.getElementById("nickname").value,
    birthdate: document.getElementById("birthdate").value,
    gender: document.getElementById("gender").value,
    zodiac: getZodiacSign(document.getElementById("birthdate").value),
  };

  await setDoc(doc(db, "userProfiles", user.uid), profileData);

  // ✅ Mostrar el mensaje de confirmación
  saveFeedback.style.display = "block";
  setTimeout(() => {
    saveFeedback.style.display = "none";
  }, 2000);

  // ✅ Actualizar el nombre en el menú lateral y en localStorage
  const displayName = profileData.nickname?.trim() || profileData.name?.trim() || "Usuario";
  userProfileBtn.textContent = `👤 ${displayName}`;
  localStorage.setItem("displayName", displayName);
});

// Obtener signo zodiacal automáticamente
function getZodiacSign(date) {
  if (!date) return "";
  const birthDate = new Date(date);
  return birthDate.getMonth() === 3 && birthDate.getDate() >= 21 ? "Aries" : "Otro";
}

// Eliminar cuenta del usuario
deleteAccountBtn.addEventListener("click", async () => {
  if (confirm("⚠️ ¿Seguro que quieres eliminar tu cuenta? Esto no se puede deshacer.")) {
    await deleteDoc(doc(db, "userProfiles", user.uid));
    alert("✅ Cuenta eliminada con éxito.");

    // Cerrar sesión y limpiar `localStorage`
    localStorage.removeItem("user");
    localStorage.removeItem("displayName");
    logout();
  }
});

// Cargar datos del usuario al iniciar la página
loadUserProfile();
