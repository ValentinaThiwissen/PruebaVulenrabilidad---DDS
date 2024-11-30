 document.getElementById("modelo").addEventListener("change", function() {
    // Obtener el modelo seleccionado
    var modeloSeleccionado = this.options[this.selectedIndex];

    // Obtener los valores de temperatura mínima y máxima
    var temperaturaMinima = modeloSeleccionado.getAttribute("data-min");
    var temperaturaMaxima = modeloSeleccionado.getAttribute("data-max");

    // Rellenar los campos de temperatura con los valores correspondientes
    document.getElementById("minima").value = temperaturaMinima;
    document.getElementById("maxima").value = temperaturaMaxima;
});
