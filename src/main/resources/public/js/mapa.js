document.addEventListener("DOMContentLoaded", function() {

    let map = L.map('map').setView([-34.6, -58.45], 10);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Hacer la solicitud a la API para obtener las heladeras
    fetch('http://localhost:8080/heladera')
        .then(response => response.json())
        .then(data => {
            data.forEach(heladera => {
                const marker = L.marker([heladera.latitud, heladera.longitud]).addTo(map);
                marker.bindPopup(`<b>${heladera.nombreHeladera}</b>`).openPopup();
            });
        })
        .catch(error => {
            console.error('Error al obtener las heladeras:', error);
        });
});
