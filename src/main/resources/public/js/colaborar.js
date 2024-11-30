function mostrarSeccion(id) {
    // Obtiene todas las secciones
    var secciones = document.querySelectorAll('.seccion-colaboracion');
    
    // Oculta todas las secciones
    secciones.forEach(function(seccion) {
        seccion.style.display = 'none';
    });
    
    // Muestra la sección seleccionada
    var seccionSeleccionada = document.getElementById(id);
    if (seccionSeleccionada) {
        seccionSeleccionada.style.display = 'block';
    }
}
// Función para realizar la donación
function realizarDonacion() {
    var monto = document.getElementById('montoDonacion').value;
    alert('Gracias por tu donación de ' + monto + '!');
}

// Función para publicar el producto
function publicarProducto() {
    var rubro = document.getElementById('rubroSelect').value;
    var marca = document.getElementById('marca').value;
    var modelo = document.getElementById('modelo').value;
    var foto = document.getElementById('fotoProducto').files[0] ? document.getElementById('fotoProducto').files[0].name : 'No foto seleccionada';

    alert('Producto publicado:\nRubro: ' + rubro + '\nMarca: ' + marca + '\nModelo: ' + modelo + '\nFoto: ' + foto);
}