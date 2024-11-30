document.addEventListener('DOMContentLoaded', function() {
    const categoriaItems = document.querySelectorAll('.categoria_item');
    const productos = document.querySelectorAll('.producto-item');

    categoriaItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault(); // Previene el comportamiento por defecto del enlace
            const categoria = this.getAttribute('data-categoria');

            // Remover la clase "ct_item-active" de todas las categorías
            categoriaItems.forEach(ci => ci.classList.remove('ct_item-active'));

            // Añadir la clase "ct_item-active" a la categoría seleccionada
            this.classList.add('ct_item-active');

            // Filtrar productos
            productos.forEach(producto => {
                producto.classList.remove('active');
                if (categoria === 'all' || producto.getAttribute('data-categoria') === categoria) {
                    producto.classList.add('active');
                }
            });
        });
    });
});

function showForm(formId, buttonId) {
    // Mostrar el formulario seleccionado
    const selectedForm = document.getElementById(formId);
    if (selectedForm) {
        selectedForm.style.display = 'block';
    }

    // Ocultar el botón que activó el formulario
    const button = document.getElementById(buttonId);
    if (button) {
        button.style.display = 'none';
    }
}
