document.getElementById('cargar_csv').addEventListener('click', function () {
    const fileInput = document.getElementById('csvfile');
    //const colaboradorIdInput = document.getElementById('colaboradorId');
    const file = fileInput.files[0];
    const mensajeDiv = document.getElementById('mensaje');


    const formData = new FormData();
    formData.append('archivo', file);

    alert('El archivo se esta subiendo, te informaremos cuando concluya la migracion');

    console.log("FormData:", Array.from(formData.entries()));
    fetch('http://localhost:8080/cargar_csv', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert('Archivo cargado exitosamente. Se enviará un correo de confirmación.');
            } else {
                alert('Error al cargar el archivo.');
            }
        })
        .catch(error => {
            console.error('Error en la solicitud:', error);
        });
});
