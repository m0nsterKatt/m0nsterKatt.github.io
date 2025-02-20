import {
  addDoc,
  collection,
  getDocs,
  getFirestore,
  query,
  where,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";
import { logout } from "./firebase.js";

const db = getFirestore();
const saveEntryBtn = document.getElementById("saveEntry");
const diaryEntry = document.getElementById("diaryEntry");
const entryTitle = document.getElementById("entryTitle");
const entryDate = document.getElementById("entryDate");
const currentDate = document.getElementById("currentDate");
const prevEntryBtn = document.getElementById("prevEntry");
const nextEntryBtn = document.getElementById("nextEntry");
const userProfileBtn = document.getElementById("userProfileBtn");
const logoutBtn = document.getElementById("logoutBtn");
const newEntryBtn = document.getElementById("newEntry");

// Obtener usuario actual
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

// Variables para entradas del diario
const entriesList = [];
let currentIndex = 0;

// Poner la fecha actual por defecto
const today = new Date().toISOString().split("T")[0];
entryDate.value = today;

// Cargar nombre del usuario en el menú lateral
const storedDisplayName = localStorage.getItem("displayName") || "Usuario";
userProfileBtn.textContent = `👤 ${storedDisplayName}`;

// Función para obtener las notas del usuario
async function loadEntries() {
  const q = query(collection(db, "diaryEntries"), where("userId", "==", user.uid));
  const snapshot = await getDocs(q);

  entriesList.length = 0; // Limpiar array
  snapshot.forEach((doc) => {
    entriesList.push({ id: doc.id, ...doc.data() });
  });

  if (entriesList.length > 0) {
    currentIndex = entriesList.length - 1;
    showEntry();
  }
}

// Mostrar una entrada específica
function showEntry() {
  if (entriesList.length > 0) {
    entryTitle.value = entriesList[currentIndex].title || "";
    diaryEntry.value = entriesList[currentIndex].text;
    entryDate.value = new Date(entriesList[currentIndex].date.toDate()).toISOString().split("T")[0];
    currentDate.textContent = `📅 ${new Date(entriesList[currentIndex].date.toDate()).toLocaleDateString()}`;
  } else {
    clearEntryForm();
  }
}

// Limpiar el formulario para escribir una nueva entrada
function clearEntryForm() {
  entryTitle.value = "";
  diaryEntry.value = "";
  entryDate.value = today;
  currentDate.textContent = "📅 Nueva Entrada";
}

// Guardar una nueva entrada
saveEntryBtn.addEventListener("click", async () => {
  if (!user || !diaryEntry.value.trim() || !entryTitle.value.trim()) return;

  await addDoc(collection(db, "diaryEntries"), {
    userId: user.uid,
    title: entryTitle.value,
    text: diaryEntry.value,
    date: new Date(entryDate.value),
  });

  // ✅ Mostrar mensaje de confirmación
  const saveFeedback = document.getElementById("saveFeedback");
  saveFeedback.style.display = "block";
  setTimeout(() => {
    saveFeedback.style.display = "none";
  }, 2000);

  // 🔄 Limpiar el formulario
  clearEntryForm();

  // 📅 Redirigir automáticamente a la vista mensual
  window.location.href = "vista_mensual.html";
});

// Navegación entre notas
prevEntryBtn.addEventListener("click", () => {
  if (currentIndex > 0) {
    currentIndex--;
    showEntry();
  }
});

nextEntryBtn.addEventListener("click", () => {
  if (currentIndex < entriesList.length - 1) {
    currentIndex++;
    showEntry();
  }
});

// Cargar entradas al abrir la página
loadEntries();

// Redirigir a "Nueva Entrada"
newEntryBtn?.addEventListener("click", () => {
  clearEntryForm();
});

// Cerrar sesión correctamente
logoutBtn.addEventListener("click", async () => {
  await logout();
  localStorage.removeItem("user");
  localStorage.removeItem("displayName");
  window.location.href = "index.html";
});
