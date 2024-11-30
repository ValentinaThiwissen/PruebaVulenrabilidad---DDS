function addProducto() {
    const ofertaContainer = document.getElementById("oferta-container");
    const newInput = document.createElement("input");
    newInput.setAttribute("name", "nombreProducto[]");
    newInput.setAttribute("placeholder", "Nombre del producto");
    newInput.setAttribute("required", "true");
    ofertaContainer.appendChild(newInput);
}