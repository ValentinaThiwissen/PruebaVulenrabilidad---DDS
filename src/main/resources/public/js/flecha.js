 document.addEventListener("DOMContentLoaded", function () {
    const scrollToTopButton = document.querySelector(".scroll-to-top");

    // Mostrar el botón cuando se baja la página
    window.addEventListener("scroll", () => {
    if (window.scrollY > 300) { // Muestra el botón después de 300px
    scrollToTopButton.style.display = "block";
} else {
    scrollToTopButton.style.display = "none";
}
});

    // Agregar comportamiento de desplazamiento suave
    scrollToTopButton.addEventListener("click", (e) => {
    e.preventDefault(); // Evitar comportamiento por defecto
    window.scrollTo({
    top: 0,
    behavior: "smooth"
});
});
});
