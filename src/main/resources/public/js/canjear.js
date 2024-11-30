// Cargar los detalles del producto desde el localStorage
const nombreProducto = localStorage.getItem('nombreProducto');
const puntosProducto = localStorage.getItem('puntosProducto');
const imagenProducto = localStorage.getItem('imagenProducto');

// Mostrar los detalles en la página
document.getElementById('nombreProducto').textContent = nombreProducto;
document.getElementById('puntosProducto').textContent = puntosProducto;
document.getElementById('imagenProducto').src = '/assets/img/' + imagenProducto;

// Pasar los valores al formulario
document.getElementById('nombreProductoInput').value = nombreProducto;
document.getElementById('puntosProductoInput').value = puntosProducto;
document.getElementById('imagenProductoInput').value = imagenProducto;