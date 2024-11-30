function toggleMenoresFields() {
    const selectValue = document.getElementById('menor').value;
    const menoresContainer = document.getElementById('menoresContainer');

    if (selectValue === 'SI') {
        menoresContainer.style.display = 'block'; // Muestra los detalles si seleccionan "SI"
    } else {
        menoresContainer.style.display = 'none'; // Oculta los detalles si seleccionan "NO"
    }
}

function addMenor() {
    const menoresList = document.getElementById("menoresList");
    const menorDiv = document.createElement("div");
    menorDiv.className = "menor";
    menorDiv.innerHTML = `
        <input type="text" name="nombreMenor[]" placeholder="Nombre del menor">
        <input type="text" name="apellidoMenor[]" placeholder="Apellido del menor">
        <div>
            <p>Ingrese el tipo de documento</p>
            <select name="tipo_documento_menor[]">
                <option value="DNI" selected>DNI</option>
                <option value="LIBRETAENROLAMIENTO">Libreta enrolamiento</option>
                <option value="LIBRETACIVICA">Libreta civica</option>
            </select>
        </div>
        <input type="text" name="documentoMenor[]" placeholder="Documento del menor">
        <input type="date" name="nacimientoMenor[]" placeholder="Fecha de nacimiento">
        <button type="button" class="removeMenorBtn" onclick="removeMenor(this)">Eliminar</button>
    `;
    menoresList.appendChild(menorDiv);
}

function removeMenor(button) {
    const menorDiv = button.parentElement;
    menorDiv.remove();
}