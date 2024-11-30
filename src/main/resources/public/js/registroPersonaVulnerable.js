
document.getElementById('menores_a_cargo').addEventListener('change', function() {
    var seleccion = this.value;
    var campoCantidadMenores = document.getElementById('campo-cantidad-menores');
    if (seleccion === 'SI') {
        campoCantidadMenores.style.display = 'block';
    } else {
        campoCantidadMenores.style.display = 'none';
    }
});
