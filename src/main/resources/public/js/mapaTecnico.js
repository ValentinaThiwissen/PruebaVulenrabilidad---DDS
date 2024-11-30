document.addEventListener("DOMContentLoaded", function() {

    let map = L.map('map').setView([-34.6, -58.45], 10);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Hacer la solicitud a la API para obtener los técnicos y sus áreas de cobertura
    fetch('http://localhost:8080/tecnicos')
        .then(response => response.json())
        .then(data => {
            data.forEach(tecnico => {
                // Crear un círculo para el área de cobertura del técnico
                const circle = L.circle([tecnico.latitud, tecnico.longitud], {
                    color: 'blue',         // Color del borde del círculo
                    fillColor: '#add8e6',  // Color de relleno del círculo
                    fillOpacity: 0.5,      // Opacidad del relleno
                    radius: tecnico.radio  // Radio del círculo en metros
                }).addTo(map);

                // Añadir un popup con el nombre del técnico
                circle.bindPopup(`<b>${tecnico.nombre}</b>`);
            });
        })
        .catch(error => {
            console.error('Error al obtener los técnicos:', error);
        });
});

