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
    const itemRows = itemChecklist.querySelectorAll('.item-checklist-row[data-search]');
    const itemSelectedCount = document.getElementById('itemSelectedCount');

    const stepSelect = document.getElementById('handoverStepSelect');
    const stepSummary = document.getElementById('handoverStepSummary');
    const reviewBtn = document.getElementById('handoverReviewBtn');
    const backBtn = document.getElementById('handoverBackBtn');
    const confirmBtn = document.getElementById('handoverConfirmBtn');

    function checkedItemBoxes() {
        return itemChecklist.querySelectorAll('input[type="checkbox"]:checked');
    }

    function updateSelectedCount() {
        itemSelectedCount.textContent = checkedItemBoxes().length;
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

    // --- Cerca i selecció d'EPIs ---

    itemSearchInput.addEventListener('input', () => {
        const query = itemSearchInput.value.trim().toLowerCase();
        itemRows.forEach(row => {
            const matches = query.length === 0 || row.dataset.search.includes(query);
            row.classList.toggle('d-none', !matches);
        });
    });

    itemChecklist.addEventListener('change', (e) => {
        if (e.target.matches('input[type="checkbox"]')) {
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
        const checked = checkedItemBoxes();
        if (checked.length === 0) {
            alert('Selecciona almenys un EPI.');
            return;
        }

        document.getElementById('summaryPerson').textContent = personSearchInput.value;
        document.getElementById('summaryDate').textContent = handoverDateInput.value;
        document.getElementById('summaryCount').textContent = checked.length;

        const list = document.getElementById('summaryItemsList');
        list.innerHTML = '';
        checked.forEach(checkbox => {
            const label = itemChecklist.querySelector(`label[for="${checkbox.id}"]`);
            const li = document.createElement('li');
            li.textContent = label ? label.textContent : checkbox.value;
            list.appendChild(li);
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

    modal.addEventListener('hidden.bs.modal', () => {
        form.reset();
        personIdInput.value = '';
        personSelected.classList.add('d-none');
        personSearchResults.classList.add('d-none');
        itemSearchInput.value = '';
        itemRows.forEach(row => row.classList.remove('d-none'));
        updateSelectedCount();

        stepSummary.classList.add('d-none');
        stepSelect.classList.remove('d-none');
        backBtn.classList.add('d-none');
        confirmBtn.classList.add('d-none');
        reviewBtn.classList.remove('d-none');
    });
});
