document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById('globalSearchInput');
    const searchResults = document.getElementById('searchResults');
    const searchResultsList = document.getElementById('searchResultsList');

    if (!searchInput) return;

    let debounceTimer;

    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();

        clearTimeout(debounceTimer);

        if (query.length < 2) {
            searchResults.classList.add('d-none');
            return;
        }

        debounceTimer = setTimeout(() => {
            fetch(`/api/search?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    searchResultsList.innerHTML = '';

                    if (data.length === 0) {
                        searchResultsList.innerHTML = '<li class="list-group-item search-result-item text-muted text-center py-4">No results found</li>';
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

                            li.addEventListener('click', () => {
                                window.location.href = item.url;
                            });

                            searchResultsList.appendChild(li);
                        });
                    }
                    searchResults.classList.remove('d-none');
                })
                .catch(err => console.error(err));
        }, 300);
    });

    document.addEventListener('click', (e) => {
        if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.classList.add('d-none');
        }
    });
});