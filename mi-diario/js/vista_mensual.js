import {
  collection,
  getDocs,
  getFirestore,
  query,
  where,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";

const db = getFirestore();
const calendarContainer = document.getElementById("calendar");

// Obtener usuario actual
const user = JSON.parse(localStorage.getItem("user"));
if (!user) window.location.href = "index.html";

// Función para generar el calendario
async function generateCalendar() {
  calendarContainer.innerHTML = "";
  const today = new Date();
  const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
  const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);

  // Obtener entradas del usuario en el mes actual
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
      dayElement.addEventListener("click", () => alert(`Entrada: ${entriesByDate[dateStr].title}`));
    }

    calendarContainer.appendChild(dayElement);
  }
}

// Cargar calendario al abrir la página
generateCalendar();
