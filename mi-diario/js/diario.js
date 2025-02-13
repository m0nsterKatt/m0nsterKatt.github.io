import {
  addDoc,
  collection,
  doc,
  getDoc,
  getDocs,
  getFirestore,
  query,
  setDoc,
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
const monthlyViewBtn = document.getElementById("viewMonthly");
const monthlyViewModal = document.getElementById("monthlyViewModal");
const closeBtn = document.querySelector(".close");
const calendarContainer = document.getElementById("calendar");
const userProfileBtn = document.getElementById("userProfileBtn");
const userProfileModal = document.getElementById("userProfileModal");
const closeProfileModal = document.querySelector(".close");
const userProfileForm = document.getElementById("userProfileForm");
const deleteAccountBtn = document.getElementById("deleteAccount");
const toggleNotificationsBtn = document.getElementById("toggleNotifications");
const notifStatus = document.getElementById("notifStatus");
const toggleThemeBtn = document.getElementById("toggleTheme");
const logoutBtn = document.getElementById("logoutBtn");

const entriesList = [];
let currentIndex = 0;

// Obtener usuario actual
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

// Poner la fecha actual por defecto
const today = new Date().toISOString().split("T")[0];
entryDate.value = today;

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
    entryTitle.value = "";
    diaryEntry.value = "";
    entryDate.value = today;
    currentDate.textContent = "📅 No hay notas aún.";
  }
}

saveEntryBtn.addEventListener("click", async () => {
  if (!user || !diaryEntry.value.trim() || !entryTitle.value.trim()) return;

  // Guardar la entrada en Firebase
  await addDoc(collection(db, "diaryEntries"), {
    userId: user.uid,
    title: entryTitle.value,
    text: diaryEntry.value,
    date: new Date(entryDate.value), // Guardamos la fecha elegida
  });

  // ✅ Mostrar el mensaje de confirmación
  const saveFeedback = document.getElementById("saveFeedback");
  saveFeedback.style.display = "block";

  setTimeout(() => {
    saveFeedback.style.display = "none";
  }, 2000); // El mensaje desaparece después de 2 segundos

  // 🔄 Cerrar la vista de edición
  entryTitle.value = "";
  diaryEntry.value = "";
  entryDate.value = today;
  currentDate.textContent = "📅 No hay notas aún.";

  // 📅 Abrir el calendario automáticamente
  monthlyViewModal.style.display = "flex";
  generateCalendar();
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

// Vista Mensual - Generar el Calendario
async function generateCalendar() {
  calendarContainer.innerHTML = "";
  const today = new Date();
  const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
  const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

  const q = query(collection(db, "diaryEntries"), where("userId", "==", user.uid));
  const snapshot = await getDocs(q);

  const entriesByDate = {};
  snapshot.forEach((doc) => {
    const entryDate = new Date(doc.data().date.toDate()).toISOString().split("T")[0];
    entriesByDate[entryDate] = doc.data();
  });

  for (let day = 1; day <= lastDay.getDate(); day++) {
    const dateStr = new Date(today.getFullYear(), today.getMonth(), day).toISOString().split("T")[0];
    const dayElement = document.createElement("div");

    dayElement.classList.add("calendar-day");
    dayElement.textContent = day;

    if (entriesByDate[dateStr]) {
      dayElement.classList.add("has-entry");
      dayElement.addEventListener("click", () => loadEntryFromCalendar(dateStr));
    }

    calendarContainer.appendChild(dayElement);
  }
}

monthlyViewBtn.addEventListener("click", () => {
  monthlyViewModal.style.display = "flex";
  generateCalendar();
});

closeBtn.addEventListener("click", () => {
  monthlyViewModal.style.display = "none";
});

// Notificaciones
async function requestNotificationPermission() {
  const permission = await Notification.requestPermission();
  return permission === "granted";
}

async function loadNotificationSettings() {
  const userDoc = await getDoc(doc(db, "userProfiles", user.uid));
  if (userDoc.exists() && userDoc.data().notifications) {
    notifStatus.textContent = "ON";
  } else {
    notifStatus.textContent = "OFF";
  }
}

toggleNotificationsBtn.addEventListener("click", async () => {
  const isEnabled = notifStatus.textContent === "ON";

  if (!isEnabled) {
    const permissionGranted = await requestNotificationPermission();
    if (!permissionGranted) return;
  }

  await setDoc(doc(db, "userProfiles", user.uid), { notifications: !isEnabled }, { merge: true });

  notifStatus.textContent = isEnabled ? "OFF" : "ON";
});

loadNotificationSettings();

// Modo Oscuro
async function toggleDarkMode() {
  document.body.classList.toggle("dark-mode");
  const isDarkMode = document.body.classList.contains("dark-mode");

  await setDoc(doc(db, "userProfiles", user.uid), { darkMode: isDarkMode }, { merge: true });

  toggleThemeBtn.textContent = isDarkMode ? "☀️ Modo Claro" : "🌙 Modo Oscuro";
}

async function loadDarkModeSetting() {
  const userDoc = await getDoc(doc(db, "userProfiles", user.uid));
  if (userDoc.exists() && userDoc.data().darkMode) {
    document.body.classList.add("dark-mode");
    toggleThemeBtn.textContent = "☀️ Modo Claro";
  }
}

toggleThemeBtn.addEventListener("click", toggleDarkMode);
loadDarkModeSetting();

// Cerrar sesión
logoutBtn.addEventListener("click", async () => {
  await logout();
  localStorage.removeItem("user");
  window.location.href = "index.html";
});
