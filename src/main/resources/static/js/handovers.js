document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('addHandoverModal');
    if (!modal) return;

    const form = document.getElementById('handoverForm');

    const personSearchInput = document.getElementById('personSearchInput');
    const personSearchResults = document.getElementById('personSearchResults');
    const personOptions = personSearchResults.querySelectorAll('.select-search-option');
    const personIdInput = document.getElementById('personId');
    const personSelected = document.getElementById('personSelected');

    const handoverDateInput = document.getElementById('handoverDate');

    const itemSearchInput = document.getElementById('itemSearchInput');
    const itemChecklist = document.getElementById('itemChecklist');
    const itemSelectedCount = document.getElementById('itemSelectedCount');

    const stepSelect = document.getElementById('handoverStepSelect');
    const stepSummary = document.getElementById('handoverStepSummary');
    const reviewBtn = document.getElementById('handoverReviewBtn');
    const backBtn = document.getElementById('handoverBackBtn');
    const confirmBtn = document.getElementById('handoverConfirmBtn');

    // --- Funciones auxiliares para trabajar con cantidades ---

    function getQtyInputs() {
        return itemChecklist.querySelectorAll('.item-qty-input');
    }

    function getTotalSelectedItems() {
        let total = 0;
        getQtyInputs().forEach(input => {
            total += parseInt(input.value) || 0;
        });
        return total;
    }

    function updateSelectedCount() {
        itemSelectedCount.textContent = getTotalSelectedItems();
    }

    // --- Cerca de persona ---

    personSearchInput.addEventListener('focus', () => {
        if (personSearchInput.value.trim().length > 0) {
            personSearchResults.classList.remove('d-none');
        }
    });

    personSearchInput.addEventListener('input', () => {
        const query = personSearchInput.value.trim().toLowerCase();
        personIdInput.value = '';
        personSelected.classList.add('d-none');

        if (query.length === 0) {
            personSearchResults.classList.add('d-none');
            return;
        }

        let anyVisible = false;
        personOptions.forEach(option => {
            const matches = option.dataset.name.toLowerCase().includes(query);
            option.classList.toggle('d-none', !matches);
            if (matches) anyVisible = true;
        });
        personSearchResults.classList.toggle('d-none', !anyVisible);
    });

    personOptions.forEach(option => {
        option.addEventListener('click', () => {
            personIdInput.value = option.dataset.id;
            personSearchInput.value = option.dataset.name;
            personSelected.textContent = 'Seleccionat: ' + option.dataset.name;
            personSelected.classList.remove('d-none');
            personSearchResults.classList.add('d-none');
        });
    });

    document.addEventListener('click', (e) => {
        if (!personSearchInput.contains(e.target) && !personSearchResults.contains(e.target)) {
            personSearchResults.classList.add('d-none');
        }
    });

    // --- Cerca dinàmica d'EPIs i gestió de filtres i grups ---

    itemSearchInput.addEventListener('input', () => {
        const query = itemSearchInput.value.trim().toLowerCase();
        const rows = itemChecklist.querySelectorAll('.item-checklist-row[data-search]');

        rows.forEach(row => {
            const searchText = row.dataset.search || '';
            const matches = query === '' || searchText.includes(query);
            row.classList.toggle('d-none', !matches);
        });
    });

    itemChecklist.addEventListener('input', (e) => {
        if (e.target.classList.contains('item-qty-input')) {
            updateSelectedCount();
        }
    });

    // --- Pas de revisió / resum ---

    reviewBtn.addEventListener('click', () => {
        if (!personIdInput.value) {
            alert('Selecciona una persona.');
            return;
        }
        if (!handoverDateInput.value) {
            alert('Selecciona una data.');
            return;
        }

        const totalItems = getTotalSelectedItems();
        if (totalItems === 0) {
            alert('Selecciona almenys un EPI.');
            return;
        }

        document.getElementById('summaryPerson').textContent = personSearchInput.value;
        document.getElementById('summaryDate').textContent = handoverDateInput.value;
        document.getElementById('summaryCount').textContent = totalItems;

        const list = document.getElementById('summaryItemsList');
        list.innerHTML = '';

        getQtyInputs().forEach(input => {
            const qty = parseInt(input.value) || 0;
            if (qty > 0) {
                const label = input.closest('.item-checklist-row').querySelector('label');
                const li = document.createElement('li');
                li.textContent = `${label ? label.textContent.split('(')[0].trim() : 'EPI'} x${qty}`;
                list.appendChild(li);
            }
        });

        stepSelect.classList.add('d-none');
        stepSummary.classList.remove('d-none');
        reviewBtn.classList.add('d-none');
        backBtn.classList.remove('d-none');
        confirmBtn.classList.remove('d-none');
    });

    backBtn.addEventListener('click', () => {
        stepSummary.classList.add('d-none');
        stepSelect.classList.remove('d-none');
        backBtn.classList.add('d-none');
        confirmBtn.classList.add('d-none');
        reviewBtn.classList.remove('d-none');
    });

    // --- Generació dels inputs ocults al confirmar/enviar el formulari ---

    form.addEventListener('submit', () => {
        const selectedContainer = document.getElementById('selectedItemIdsContainer');
        if (selectedContainer) selectedContainer.innerHTML = '';

        getQtyInputs().forEach(input => {
            const qty = parseInt(input.value) || 0;
            if (qty > 0 && input.dataset.itemIds) {
                const availableIds = input.dataset.itemIds.split(',');
                for (let i = 0; i < qty && i < availableIds.length; i++) {
                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'itemIds';
                    hiddenInput.value = availableIds[i];
                    selectedContainer.appendChild(hiddenInput);
                }
            }
        });
    });

    modal.addEventListener('hidden.bs.modal', () => {
        form.reset();
        personIdInput.value = '';
        personSelected.classList.add('d-none');
        personSearchResults.classList.add('d-none');
        itemSearchInput.value = '';

        // Restablir la visibilitat de grups i subcategories
        itemChecklist.querySelectorAll('.subcat-group, .item-checklist-row').forEach(el => {
            el.classList.remove('d-none');
        });

        const selectedContainer = document.getElementById('selectedItemIdsContainer');
        if (selectedContainer) selectedContainer.innerHTML = '';

        updateSelectedCount();

        stepSummary.classList.add('d-none');
        stepSelect.classList.remove('d-none');
        backBtn.classList.add('d-none');
        confirmBtn.classList.add('d-none');
        reviewBtn.classList.remove('d-none');
    });
});