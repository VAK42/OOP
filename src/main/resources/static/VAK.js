function openFilterModal() {
  document.getElementById('filterModal').classList.remove('hidden');
  document.getElementById('filterOverlay').classList.remove('hidden');
}

function closeFilterModal() {
  document.getElementById('filterModal').classList.add('hidden');
  document.getElementById('filterOverlay').classList.add('hidden');
}