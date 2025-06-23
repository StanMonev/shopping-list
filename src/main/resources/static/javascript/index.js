document.addEventListener('DOMContentLoaded', init);

function init() {
    /* ---------- global “add” button & form ---------- */
    const addBtn  = document.getElementById('showForm');
    const addForm = document.getElementById('editForm');
    addBtn?.addEventListener('click', () => addForm.classList.toggle('open'));
    addForm?.querySelector('.btn-circle.red')
        ?.addEventListener('click', () => addForm.classList.remove('open'));

    /* ---------- per-card behaviour ---------- */
    document.querySelectorAll('.note-stack .card').forEach(card => {
        /* toggle strike-through */
        card.addEventListener('click', ev => {
            if (ev.target.closest('.actions') || card.classList.contains('editing'))
                return;
            card.classList.toggle('done');
        });

        /* EDIT ---------------------------------------------------- */
        card.querySelector('.actions .btn-circle.yellow')
            .addEventListener('click', ev => openEdit(ev, card));

        card.querySelector('.card-edit .btn-circle.red')
            .addEventListener('click', ev => cancelEdit(ev, card));

        /* DELETE / CONFIRM --------------------------------------- */
        card.querySelector('.actions .btn-circle.red')
            .addEventListener('click', ev => openConfirm(ev, card));

        card.querySelector('.card-confirm .secondary')
            .addEventListener('click', ev => confirmCancel(ev, card));

        card.querySelector('.card-confirm .primary')
            .addEventListener('click', ev => confirmDelete(ev, card));
    });
}

/* ========== edit helpers ====================================== */
function openEdit(ev, card) {
    ev.stopPropagation();
    const f = card.querySelector('.card-edit');
    f.id.value       = card.dataset.id;
    f.name.value     = card.dataset.name;
    f.quantity.value = card.dataset.qty;
    card.classList.add('editing');
}
function cancelEdit(ev, card) {
    ev.stopPropagation();
    card.classList.remove('editing');
}

/* ========== delete helpers ==================================== */
function openConfirm(ev, card) {
    ev.preventDefault();
    ev.stopPropagation();
    card.classList.add('confirming');
}
function confirmCancel(ev, card) {
    ev.stopPropagation();
    card.classList.remove('confirming');
}
function confirmDelete(ev, card) {
    ev.stopPropagation();
    card.querySelector('.card-confirm').submit();
}