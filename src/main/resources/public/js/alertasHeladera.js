//ejemplo para que muestre como se veria
const alertas = [
    { nombre: 'Heladera Medrano', tipo: 'Temperatura Alta', distancia: '300m', fecha: '2024-08-12', hora: '15:54' },
    { nombre: 'Heladera de las Girls', tipo: 'Sensor Movimiento', distancia: '1500m', fecha: '2024-08-12', hora: '14:41' },
    { nombre: 'Heladera Nati', tipo: 'Puerta Abierta', distancia: '4000m', fecha: '2024-08-12', hora: '00:00' }
];

function agregarAlertas() {
    const tbody = document.querySelector("#alertaTable tbody");
    tbody.innerHTML = ''; 

    alertas.forEach(alerta => {
        const row = document.createElement('tr');

        Object.values(alerta).forEach(value => {
            const cell = document.createElement('td');
            cell.textContent = value;
            row.appendChild(cell);
        });

        tbody.appendChild(row);
    });
}

// Renderizar listado al cargar la página
document.addEventListener('DOMContentLoaded', agregarAlertas);
