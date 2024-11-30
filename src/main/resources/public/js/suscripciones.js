function mostrarFormulario(idFormulario) {
    // Oculta todos los formularios
    document.getElementById("formulario1").style.display = "none";
    document.getElementById("formulario2").style.display = "none";
    document.getElementById("formulario3").style.display = "none";

    // Muestra el formulario correspondiente
    document.getElementById(idFormulario).style.display = "block";
}