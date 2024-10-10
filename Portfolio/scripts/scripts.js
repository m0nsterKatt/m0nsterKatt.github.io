// Smooth scrolling for navigation links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Scroll animation for sections
const sections = document.querySelectorAll('.section');
const options = {
    threshold: 0.5
};

const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('appear');
        }
    });
}, options);

sections.forEach(section => {
    observer.observe(section);
});

// Apply animation styles via CSS
const css = `
.section {
    opacity: 0;
    transform: translateY(50px);
    transition: opacity 0.6s ease-out, transform 0.6s ease-out;
}

.section.appear {
    opacity: 1;
    transform: translateY(0);
}

/* Estilos para las palabras destacadas */
.highlight {
    font-weight: normal;
    transition: all 0.3s ease;
}

.highlight:hover {
    font-weight: bold;
    font-size: 1.2em;
    color: #ff6347; /* Cambia este color según tu preferencia */
}
`;

const style = document.createElement('style');
style.appendChild(document.createTextNode(css));
document.head.appendChild(style);

// Función máquina de escribir para "Sobre Mí"
document.addEventListener("DOMContentLoaded", () => {
    const paragraphs = [
        'Soy desarrolladora web con una gran pasión por el diseño de experiencias de usuario (<span class="highlight">UX/UI</span>) y un enfoque especializado en el desarrollo <span class="highlight">frontend</span>. Actualmente, estoy cursando la carrera de <span class="highlight">Ingeniería Informática</span>, lo que me permite complementar mis habilidades técnicas con una sólida base teórica.',
        'Me encanta enfrentar desafíos que me permiten resolver problemas complejos y mejorar la funcionalidad y estética de las <span class="highlight">aplicaciones web</span>. Mi enfoque está en crear <span class="highlight">interfaces</span> intuitivas y eficientes, siempre manteniendo un equilibrio entre la <span class="highlight">usabilidad</span> y el diseño visual.',
        'A lo largo de mi carrera, he adquirido experiencia en las tecnologías más demandadas del <span class="highlight">frontend</span> y sigo perfeccionando mis habilidades mientras exploro nuevas herramientas y tendencias en el desarrollo web. Soy una aprendiz constante, motivada por el deseo de estar al día con las innovaciones del sector, y siempre busco cómo aplicar ese conocimiento en proyectos que generen impacto.',
        'Si buscas a alguien comprometido con la excelencia, con la capacidad de adaptarse rápidamente y con una pasión por el <span class="highlight">clean code</span>, estaré encantada de formar parte de tu equipo.'
    ];

    let container = document.getElementById("typewriter-container");
    let cursor = document.getElementById("cursor");

    let paragraphIndex = 0;
    let charIndex = 0;
    let typingSpeed = 30;  // Velocidad de la máquina de escribir (ms entre caracteres)
    let paragraphDelay = 1000; // Retraso entre párrafos

    function typeParagraph() {
        if (paragraphIndex < paragraphs.length) {
            // Obtener el párrafo completo como HTML, con las etiquetas de span ya incluidas
            let currentParagraph = paragraphs[paragraphIndex];
            
            // Añadir un nuevo contenedor de párrafo al div
            let paragraphElement = document.createElement('div');
            paragraphElement.className = 'typed-paragraph';
            paragraphElement.innerHTML = currentParagraph; // Añadimos el párrafo completo con los spans
            container.appendChild(paragraphElement); // Añadir el párrafo al contenedor

            // Ahora escribimos el texto letra por letra dentro del nuevo párrafo
            function typeCharacter() {
                if (charIndex < currentParagraph.length) {
                    paragraphElement.innerHTML = currentParagraph.substring(0, charIndex + 1);
                    charIndex++;
                    setTimeout(typeCharacter, typingSpeed);
                } else {
                    // Terminamos el párrafo, pasamos al siguiente
                    charIndex = 0;
                    paragraphIndex++;
                    if (paragraphIndex < paragraphs.length) {
                        setTimeout(typeParagraph, paragraphDelay); // Empezar el próximo párrafo después del retraso
                    } else {
                        cursor.style.display = 'inline'; // Mostrar el cursor al final
                    }
                }
            }
            typeCharacter();
        }
    }

    // Ocultar el cursor inicialmente hasta que se termine el último párrafo
    cursor.style.display = 'none';
    setTimeout(typeParagraph, 1000); // Iniciar la animación después de 1 segundo
});

// Animar las barras de progreso cuando la sección de habilidades esté en pantalla
document.addEventListener('DOMContentLoaded', () => {
    const skillSection = document.querySelector('#skills');
    const progressBars = document.querySelectorAll('.progress');

    const observerOptions = {
        threshold: 0.75  // Cuando el 50% de la sección sea visible
    };

    const skillObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                // Recorrer todas las barras de progreso
                progressBars.forEach(bar => {
                    const skillLevel = bar.getAttribute('data-skill-level');
                    bar.style.width = skillLevel + '%'; // Asignar el ancho según el nivel de habilidad
                });
                // Dejar de observar después de la primera animación
                observer.disconnect();
            }
        });
    }, observerOptions);

    // Iniciar la observación de la sección de habilidades
    skillObserver.observe(skillSection);
});
