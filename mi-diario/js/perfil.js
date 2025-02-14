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

// Cargar perfil del usuario desde Firebase
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

  // ✅ Mostrar el mensaje de confirmación
  saveFeedback.style.display = "block";
  setTimeout(() => {
    saveFeedback.style.display = "none";
  }, 2000);
});

// Obtener signo zodiacal
function getZodiacSign(date) {
  if (!date) return "";
  const birthDate = new Date(date);
  return birthDate.getMonth() === 3 && birthDate.getDate() >= 21 ? "Aries" : "Otro";
}

// Eliminar cuenta
deleteAccountBtn.addEventListener("click", async () => {
  if (confirm("¿Seguro que quieres eliminar tu cuenta? Esto no se puede deshacer.")) {
    await deleteDoc(doc(db, "userProfiles", user.uid));
    alert("Cuenta eliminada.");
    logout();
  }
});

loadUserProfile();
