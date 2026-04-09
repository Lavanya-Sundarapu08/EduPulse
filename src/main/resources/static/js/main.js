// ── Theme toggle ─────────────────────────────────────────────────
const toggle = document.getElementById('themeToggle');
if (toggle) {
  toggle.addEventListener('click', () => {
    document.body.classList.toggle('dark');
    toggle.textContent = document.body.classList.contains('dark') ? '☀️' : '🌙';
  });
}

// ── Custom select: highlight active option ────────────────────────
document.querySelectorAll('.custom-select').forEach(select => {
  select.addEventListener('change', function () {
    this.style.color = this.value ? '#1a1a1a' : '#999';
  });
});

// ── Rating buttons: highlight on click ───────────────────────────
document.querySelectorAll('.rating-radio').forEach(radio => {
  if (radio.checked) {
    radio.nextElementSibling.classList.add('selected');
  }
});