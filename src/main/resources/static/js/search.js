document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById('globalSearchInput');
    const searchResults = document.getElementById('searchResults');

    if (!searchInput) return;

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim().toLowerCase();
        const tableCards = document.querySelectorAll('.table-card');

        if (tableCards.length > 0) {
            if (searchResults) searchResults.classList.add('d-none');

            tableCards.forEach(card => {
                const cardText = card.textContent.toLowerCase();
                const matches = query === '' || cardText.includes(query);

                // Ocultar/mostrar tarjeta
                card.classList.toggle('d-none', !matches);

                const collapseElement = card.querySelector('.collapse');
                const toggleHeader = card.querySelector('.subcategory-header');

                if (collapseElement && toggleHeader) {
                    if (query !== '' && matches) {
                        // Forzamos la apertura directa sin entrar en conflicto con la API de Bootstrap
                        collapseElement.classList.add('show');
                        toggleHeader.classList.add('expanded');
                        toggleHeader.setAttribute('aria-expanded', 'true');
                    } else if (query === '') {
                        // Si se limpia el buscador, cerramos
                        collapseElement.classList.remove('show');
                        toggleHeader.classList.remove('expanded');
                        toggleHeader.setAttribute('aria-expanded', 'false');
                    }
                }
            });
            return;
        }

        // Búsqueda flotante estándar para otras páginas
        if (query.length < 2) {
            if (searchResults) searchResults.classList.add('d-none');
            return;
        }

        fetch(`/api/search?q=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                const searchResultsList = document.getElementById('searchResultsList');
                if (!searchResultsList || !searchResults) return;

                searchResultsList.innerHTML = '';
                if (data.length === 0) {
                    searchResultsList.innerHTML = '<li class="list-group-item search-result-item text-muted text-center py-4">No hi ha resultats</li>';
                } else {
                    data.forEach(item => {
                        const li = document.createElement('li');
                        li.className = 'list-group-item search-result-item d-flex justify-content-between align-items-center py-3 px-4';
                        li.innerHTML = `
                            <div>
                                <div class="fw-bold text-white mb-1">${item.title}</div>
                                <small class="text-muted">${item.subtitle}</small>
                            </div>
                            <span class="search-badge badge-${item.type.toLowerCase()}">${item.type}</span>
                        `;
                        li.addEventListener('click', () => window.location.href = item.url);
                        searchResultsList.appendChild(li);
                    });
                }
                searchResults.classList.remove('d-none');
            })
            .catch(err => console.error(err));
    });

    document.addEventListener('click', (e) => {
        if (searchResults && !searchInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.classList.add('d-none');
        }
    });
});