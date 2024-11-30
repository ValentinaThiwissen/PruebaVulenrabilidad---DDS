document.addEventListener('DOMContentLoaded', function() {
    const campos = document.querySelectorAll('.input-text');
    const valoresOriginales = {};

    // Guardar el valor inicial de cada campo
    campos.forEach(campo => {
        valoresOriginales[campo.name] = campo.value;
    });

    // Habilitar los campos al editar
    const botonesEditar = document.querySelectorAll('.edit-button');

    botonesEditar.forEach((boton) => {
        boton.addEventListener('click', function(event) {
            const campoEditable = boton.closest('.campo');
            const inputText = campoEditable.querySelector('.input-text');
            const saveButton = document.createElement('button');

            saveButton.textContent = 'Guardar';
            saveButton.classList.add('save-button');
            campoEditable.appendChild(saveButton);

            inputText.disabled = false;  // Habilitar el campo de entrada
            inputText.focus();

            // Manejar el evento de clic en el botón de guardar
            saveButton.addEventListener('click', function() {
                inputText.disabled = true;  // Deshabilitar el campo una vez guardado
                saveButton.remove(); // Eliminar el botón de guardar
            });
        });
    });

    // Manejar el envío del formulario
    document.querySelector('form').addEventListener('submit', function(event) {
        campos.forEach(campo => {
            // Si no ha cambiado el valor, restaurar el valor original
            if (campo.value === valoresOriginales[campo.name]) {
                campo.value = valoresOriginales[campo.name];
            }
        });
    });
});
