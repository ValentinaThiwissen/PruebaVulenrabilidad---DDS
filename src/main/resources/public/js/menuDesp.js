// Obtener los elementos
const menuToggle = document.querySelector('.menu-toggle');
const menu = document.querySelector('.menu');

// Asegurarse de que el menú esté oculto al principio
window.onload = function() {
    menu.style.display = 'none';
};

// Mostrar u ocultar el menú al hacer clic en el botón de menú
menuToggle.addEventListener('click', (event) => {
    event.stopPropagation(); // Evita que se cierre al hacer clic en el botón
    if (menu.style.display === 'none' || menu.style.display === '') {
        menu.style.display = 'block'; // Muestra el menú
    } else {
        menu.style.display = 'none'; // Oculta el menú
    }
});

// Cerrar el menú si se hace clic fuera de él
document.addEventListener('click', (e) => {
    if (!menu.contains(e.target) && !menuToggle.contains(e.target)) {
        menu.style.display = 'none'; // Cierra el menú si se hace clic fuera de él
    }
});