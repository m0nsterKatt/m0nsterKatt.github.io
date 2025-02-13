import {
  deleteDoc,
  doc,
  getDoc,
  getFirestore,
  setDoc,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";
import { logout } from "./firebase.js";

const db = getFirestore();
const userProfileBtn = document.getElementById("userProfileBtn");
const userProfileModal = document.getElementById("userProfileModal");
const closeProfileModal = document.querySelector(".close");
const userProfileForm = document.getElementById("userProfileForm");
const deleteAccountBtn = document.getElementById("deleteAccount");

// Obtener usuario actual
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

// Abrir y cerrar el modal del perfil
userProfileBtn.addEventListener("click", () => {
  userProfileModal.style.display = "flex";
  loadUserProfile();
});

closeProfileModal.addEventListener("click", () => {
  userProfileModal.style.display = "none";
});

// Cargar perfil del usuario
async function loadUserProfile() {
  const userDoc = await getDoc(doc(db, "userProfiles", user.uid));
  if (userDoc.exists()) {
    const data = userDoc.data();
    document.getElementById("name").value = data.name || "";
    document.getElementById("surname").value = data.surname || "";
    document.getElementById("nickname").value = data.nickname || "";
    document.getElementById("birthdate").value = data.birthdate || "";
    document.getElementById("gender").value = data.gender || "";
    document.getElementById("zodiac").value = data.zodiac || "";
  }
}

// Guardar perfil en Firebase
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
  alert("Perfil actualizado con éxito.");
});

// Obtener signo zodiacal automáticamente
function getZodiacSign(date) {
  if (!date) return "";
  const birthDate = new Date(date);
  const month = birthDate.getMonth() + 1;
  const day = birthDate.getDate();

  if ((month === 3 && day >= 21) || (month === 4 && day <= 19)) return "Aries";
  if ((month === 4 && day >= 20) || (month === 5 && day <= 20)) return "Tauro";
  if ((month === 5 && day >= 21) || (month === 6 && day <= 20)) return "Géminis";
  return "Otro";
}

// Eliminar cuenta
deleteAccountBtn.addEventListener("click", async () => {
  if (confirm("¿Seguro que quieres eliminar tu cuenta? Esto no se puede deshacer.")) {
    await deleteDoc(doc(db, "userProfiles", user.uid));
    alert("Cuenta eliminada.");
    logout();
  }
});
