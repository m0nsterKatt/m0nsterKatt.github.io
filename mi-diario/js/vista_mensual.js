import {
  collection,
  doc,
  getDoc,
  getDocs,
  getFirestore,
  query,
  where,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";

const db = getFirestore();
const calendarContainer = document.getElementById("calendar");
const currentMonthDisplay = document.getElementById("currentMonth");
const prevMonthBtn = document.getElementById("prevMonth");
const nextMonthBtn = document.getElementById("nextMonth");
const userProfileBtn = document.getElementById("userProfileBtn");

// Obtener usuario actual
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

// Variables para el mes y año actual
let currentMonth = new Date().getMonth();
let currentYear = new Date().getFullYear();

// Función para actualizar el nombre del usuario en el menú lateral
async function loadUserName() {
  const userDoc = await getDoc(doc(db, "userProfiles", user.uid));
  if (userDoc.exists()) {
    const data = userDoc.data();
    userProfileBtn.textContent = `👤 ${data.nickname || data.name || "Usuario"}`;
  }
}

// Función para actualizar la vista del mes
function updateMonthDisplay() {
  const months = [
    "Enero",
    "Febrero",
    "Marzo",
    "Abril",
    "Mayo",
    "Junio",
    "Julio",
    "Agosto",
    "Septiembre",
    "Octubre",
    "Noviembre",
    "Diciembre",
  ];
  currentMonthDisplay.textContent = `${months[currentMonth]} ${currentYear}`;
}

// Función para generar el calendario dinámico
async function generateCalendar() {
  calendarContainer.innerHTML = ""; // Limpiar calendario
  updateMonthDisplay(); // Actualizar el mes y año mostrado

  const firstDay = new Date(currentYear, currentMonth, 1);
  const lastDay = new Date(currentYear, currentMonth + 1, 0);

  // Obtener entradas del usuario en el mes actual
  const q = query(collection(db, "diaryEntries"), where("userId", "==", user.uid));
  const snapshot = await getDocs(q);

  const entriesByDate = {};
  snapshot.forEach((doc) => {
    const entryDate = new Date(doc.data().date.toDate()).toISOString().split("T")[0];
    entriesByDate[entryDate] = (entriesByDate[entryDate] || 0) + 1;
  });

  // Crear los días vacíos antes del primer día del mes
  for (let i = 0; i < firstDay.getDay(); i++) {
    const emptyDay = document.createElement("div");
    emptyDay.classList.add("calendar-day", "empty");
    calendarContainer.appendChild(emptyDay);
  }

  // Crear los días del mes
  for (let day = 1; day <= lastDay.getDate(); day++) {
    const dateStr = new Date(currentYear, currentMonth, day).toISOString().split("T")[0];
    const dayElement = document.createElement("div");

    dayElement.classList.add("calendar-day");
    dayElement.textContent = day;

    if (entriesByDate[dateStr]) {
      dayElement.classList.add("has-entry");
      const entryCount = document.createElement("span");
      entryCount.textContent = `📖 ${entriesByDate[dateStr]}`;
      entryCount.classList.add("entry-count");
      dayElement.appendChild(entryCount);

      dayElement.addEventListener("click", () => {
        alert(`📅 ${dateStr}\n📖 ${entriesByDate[dateStr]} entrada(s) guardadas.`);
      });
    }

    calendarContainer.appendChild(dayElement);
  }
}

// Event Listeners para cambiar de mes
prevMonthBtn.addEventListener("click", () => {
  currentMonth--;
  if (currentMonth < 0) {
    currentMonth = 11;
    currentYear--;
  }
  generateCalendar();
});

nextMonthBtn.addEventListener("click", () => {
  currentMonth++;
  if (currentMonth > 11) {
    currentMonth = 0;
    currentYear++;
  }
  generateCalendar();
});

// Cargar datos al abrir la página
loadUserName();
generateCalendar();
