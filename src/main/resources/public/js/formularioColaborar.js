function showForm(formId) {
    // Ocultar todos los formularios
    const forms = document.querySelectorAll('.hidden-form');
    forms.forEach(form => {
        form.style.display = 'none';
    });

    // Mostrar el formulario seleccionado
    const selectedForm = document.getElementById(formId);
    if (selectedForm) {
        selectedForm.style.display = 'block';
    }
}

// Lógica para agregar productos a "Donación de Viandas"
document.getElementById('addViandaBtn').addEventListener('click', function () {
    const container = document.getElementById('vianda-container');
    const limit = 10; // Limite máximo de viandas
    const currentItems = container.querySelectorAll('.producto-grupo').length;

    if (currentItems < limit) {
        const newGroup = document.createElement('div');
        newGroup.classList.add('producto-grupo');
        newGroup.innerHTML = `
            <input name="nombreComida[]" placeholder="Descripción de comida" required>
            <input name="peso[]" placeholder="Peso">
            <input name="calorias[]" placeholder="Calorias">
            <input name="caducidad[]" placeholder="Fecha de caducidad de la vianda">
        `;
        container.appendChild(newGroup);
    } else {
        document.querySelector('.producto-limite').style.display = 'block';
    }
});

function addHeladera() {
    const container = document.getElementById('heladera-container');
    const limit = 3;
    const currentItems = container.querySelectorAll('.heladera-grupo').length;
    if (currentItems < limit) {
        const newGroup = document.createElement('div');
        newGroup.classList.add('heladera-grupo');
        newGroup.innerHTML = `
            <label for="heladera">Seleccionar heladera destino:</label>
        <select name="heladera[]" class="heladera" required>
            ${getHeladeraOptions()}
        </select>

        <label for="cantidad">Elige la cantidad de viandas a distribuir:</label>
        <select name="cantidad[]" class="cantidad" required>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
        </select>
            `;
        container.appendChild(newGroup);
    } else {
        document.querySelector('.heladera-limite').style.display = 'block';
    }
}
/*function addProducto() {
    const container = document.getElementById('oferta-container');
    const limit = 5;
    const currentItems = container.querySelectorAll('.producto-grupo').length;
    if (currentItems < limit) {
        const newGroup = document.createElement('div');
        newGroup.classList.add('producto-grupo');
        newGroup.innerHTML = `
            <input name="nombreProducto[]" placeholder="Nombre del producto" required> 
 
         `;
        container.appendChild(newGroup);
    } else {
        document.querySelector('.oferta-limite').style.display = 'block';
    }
}*/
function getHeladeraOptions() {
    const heladeras = [...document.querySelectorAll('#heladeraOrigen option')];
    return heladeras.map(option => `<option value="${option.value}">${option.text}</option>`).join('');
}














/*

document.getElementById('addHeladeraBtn').addEventListener('click', function () {
    const container = document.getElementById('heladera-container');
    const limit = 3;
    const currentItems = container.querySelectorAll('.heladera-grupo').length;

    if (currentItems < limit) {
        const newGroup = document.createElement('div');
        newGroup.classList.add('heladera-grupo');
        newGroup.innerHTML = `
            <label for="heladera">Seleccionar heladera destino:</label>
            <select name="heladera[]" class="heladera" required>
                ${getHeladeraOptions()}
            </select>

            <label for="cantidad">Elige la cantidad de viandas a distribuir:</label>
            <select name="cantidad[]" class="cantidad" required>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select>
        `;
        container.appendChild(newGroup);
    } else {
        document.querySelector('.heladera-limite').style.display = 'block';
    }
});**/


