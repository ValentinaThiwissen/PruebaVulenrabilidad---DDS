document.addEventListener('DOMContentLoaded', function() {
    // Obtén todos los botones de ver PDF y de descargar
    const botonesVer = document.querySelectorAll('.verPdf');
    const botonesDescargar = document.querySelectorAll('.descargarPdf');

    const button = document.querySelector(".provisorio_generar_reporte");

    console.log('Botones de Ver:', botonesVer);
    console.log('Botones de Descargar:', botonesDescargar);

    // Manejador para abrir el PDF en una nueva ventana
    botonesVer.forEach(boton => {
        boton.addEventListener('click', function(event) {
            const ruta = boton.getAttribute('data-ruta');
            console.log(`Abriendo PDF: ${ruta}`);
            window.open(ruta, '_blank');  // Abre el PDF en una nueva ventana
        });
    });

    // Manejador para descargar el PDF
    botonesDescargar.forEach(boton => {
        boton.addEventListener('click', function(event) {
            const ruta = boton.getAttribute('data-ruta');
            console.log(`Descargando PDF: ${ruta}`);
            const link = document.createElement('a');
            link.href = ruta;
            link.download = ruta.split('/').pop();  // Extrae el nombre del archivo
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        });
    });
});
