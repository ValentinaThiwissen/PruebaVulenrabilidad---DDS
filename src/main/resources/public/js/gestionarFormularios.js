function mostrarFormulario(idFormulario) {
    const contenedorPreguntas = document.getElementById('contenedorPreguntas');
    const listaPreguntas = document.getElementById('listaPreguntas');
    const tituloFormulario = document.getElementById('tituloFormulario');

    // Actualizar el título del formulario
    const nombreFormulario = document.querySelector(`.card[onclick='mostrarFormulario(${idFormulario})'] .card-title`).textContent;
    tituloFormulario.textContent = `Preguntas del Formulario: ${nombreFormulario}`;

    // Limpiar la lista anterior
    listaPreguntas.innerHTML = '';

    // Obtener las preguntas del <ul> correspondiente y copiarlas en la lista principal
    const preguntasUl = document.getElementById(`preguntas-${idFormulario}`);
    preguntasUl.querySelectorAll('li').forEach(pregunta => {
        listaPreguntas.appendChild(pregunta.cloneNode(true));
    });

    // Mostrar la sección de preguntas
    contenedorPreguntas.style.display = 'block';
}
