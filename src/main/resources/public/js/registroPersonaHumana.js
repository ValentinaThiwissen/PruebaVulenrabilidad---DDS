const addContactoBtn = document.getElementById('add-contacto-btn');
const contactoContainer = document.getElementById('contacto-container');
const contactoLimiteMensaje = document.getElementById('contacto-limite');

const MAX_CONTACTOS = 5;

function contarContactos() {
    return contactoContainer.querySelectorAll('.contacto-grupo').length;
}

addContactoBtn.addEventListener('click', function() {
    if (contarContactos() < MAX_CONTACTOS) {
        const nuevoContacto = document.createElement('div');
        nuevoContacto.classList.add('contacto-grupo');
        nuevoContacto.innerHTML = `
                <div>
                    <select name="tipo_contacto[]" required>
                        <option value="Whatsapp">Whatsapp</option>
                        <option value="Telefono">Telefono</option>
                        <option value="Telegram">Telegram</option>
                        <option value="Mail">Mail</option>
                    </select>
                </div>
                <div>
                    <input type="text" name="contacto[]" placeholder="Contacto" required>
                </div>
            `;
        contactoContainer.appendChild(nuevoContacto);
    }

    if (contarContactos() === MAX_CONTACTOS) {
        addContactoBtn.disabled = true;
        contactoLimiteMensaje.style.display = 'block';
    }
});
