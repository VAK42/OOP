window.addEventListener("DOMContentLoaded", function () {
  const chartDataEl = document.getElementById("chart-data");
  if (!chartDataEl) {
    console.error("Data Not Found In DOM!");
    return;
  }
  let monthlyRevenueData = [];
  let topProductsData = [];
  try {
    monthlyRevenueData = JSON.parse(chartDataEl.dataset.months || "[]");
    topProductsData = JSON.parse(chartDataEl.dataset.products || "[]");
  } catch (e) {
    console.error("Error Parsing JSON Data From DOM: ", e);
    return;
  }
  const monthlyLabels = monthlyRevenueData.map(item => item.month);
  const monthlyValues = monthlyRevenueData.map(item => Number(item.revenue) || 0);
  const monthlyCanvas = document.getElementById("monthlyRevenueChart");
  if (monthlyCanvas && window.Chart) {
    const monthlyCtx = monthlyCanvas.getContext("2d");
    new Chart(monthlyCtx, {
      type: 'line', data: {
        labels: monthlyLabels, datasets: [{
          label: "Revenue",
          data: monthlyValues,
          borderColor: 'rgba(54, 162, 235, 1)',
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          tension: 0.4,
          fill: true
        }]
      }, options: {
        responsive: true, plugins: {
          legend: {position: 'top'},
        }, scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  } else {
    console.warn("Sth Went Wrong!");
  }
  const productLabels = topProductsData.map(p => p.name);
  const productQuantities = topProductsData.map(p => Number(p.quantity) || 0);
  const topProductsCanvas = document.getElementById("topProductsChart");
  if (topProductsCanvas && window.Chart) {
    const topCtx = topProductsCanvas.getContext("2d");
    const backgroundColors = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40', '#8E44AD', '#1ABC9C', '#F39C12', '#2ECC71'];
    new Chart(topCtx, {
      type: 'pie', data: {
        labels: productLabels, datasets: [{
          label: "Quantity",
          data: productQuantities,
          backgroundColor: backgroundColors.slice(0, productLabels.length)
        }]
      }, options: {
        responsive: true, plugins: {
          legend: {position: 'right'},
        }
      }
    });
  } else {
    console.warn("Sth Went Wrong!");
  }
});