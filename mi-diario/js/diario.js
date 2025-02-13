import {
  addDoc,
  collection,
  getDocs,
  getFirestore,
  query,
  where,
} from "https://www.gstatic.com/firebasejs/10.4.0/firebase-firestore.js";

const db = getFirestore();
const saveEntryBtn = document.getElementById("saveEntry");
const diaryEntry = document.getElementById("diaryEntry");
const entryTitle = document.getElementById("entryTitle");
const entryDate = document.getElementById("entryDate");
const currentDate = document.getElementById("currentDate");
const prevEntryBtn = document.getElementById("prevEntry");
const nextEntryBtn = document.getElementById("nextEntry");

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

// Guardar nueva entrada con título y fecha
saveEntryBtn.addEventListener("click", async () => {
  if (!user || !diaryEntry.value.trim() || !entryTitle.value.trim()) return;

  await addDoc(collection(db, "diaryEntries"), {
    userId: user.uid,
    title: entryTitle.value,
    text: diaryEntry.value,
    date: new Date(entryDate.value), // Guardamos la fecha elegida
  });

  loadEntries();
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

// Cargar notas al iniciar
loadEntries();
