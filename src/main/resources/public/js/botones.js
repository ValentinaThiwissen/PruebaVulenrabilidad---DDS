 // Obtener el modal y los botones
    var modal = document.getElementById("modalEliminar");
    var eliminarBtns = document.querySelectorAll(".eliminar-btn");
    var closeBtn = document.querySelector(".close");
    var cancelarBtn = document.getElementById("cancelarBtn");

    // Mostrar el modal cuando se hace clic en "Eliminar"
    eliminarBtns.forEach(function(btn) {
    btn.addEventListener("click", function() {
        var usuarioId = this.getAttribute("data-id");
        document.getElementById("usuarioId").value = usuarioId;
        modal.style.display = "flex";
    });
});

    // Cerrar el modal cuando se hace clic en la "X"
    closeBtn.addEventListener("click", function() {
    modal.style.display = "none";
});

    // Cerrar el modal cuando se hace clic en "Cancelar"
    cancelarBtn.addEventListener("click", function() {
    modal.style.display = "none";
});

    // Cerrar el modal si se hace clic fuera del contenido
    window.addEventListener("click", function(event) {
    if (event.target === modal) {
    modal.style.display = "none";
}
});
