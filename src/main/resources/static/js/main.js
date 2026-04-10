// ── Theme toggle with localStorage ───────────────────────────────
const toggle = document.getElementById('themeToggle');

// Apply saved theme on page load
if (localStorage.getItem('theme') === 'dark') {
  document.body.classList.add('dark');
  if (toggle) toggle.textContent = '☀️';
} else {
  if (toggle) toggle.textContent = '🌙';
}

if (toggle) {
  toggle.addEventListener('click', () => {
    document.body.classList.toggle('dark');
    const isDark = document.body.classList.contains('dark');
    toggle.textContent = isDark ? '☀️' : '🌙';
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
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