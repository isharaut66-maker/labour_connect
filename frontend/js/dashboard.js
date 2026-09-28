document.addEventListener("DOMContentLoaded", async () => {
    const user = await requireAuth();
    if (!user) return;

    document.getElementById('sidebarName').innerText = user.name;
    document.getElementById('sidebarRole').innerText = user.role;
    document.getElementById('welcomeText').innerText = `Welcome back, ${user.name.split(' ')[0]}`;

    // Setup menus based on role
    if (user.role === 'CLIENT' || user.role === 'CONTRACTOR' || user.role === 'ORGANIZATION') {
        document.getElementById('clientMenu').classList.remove('hidden');
    } else if (user.role === 'LABOURER') {
        document.getElementById('labourerMenu').classList.remove('hidden');
    }

    loadDashboardStats();
});

async function loadDashboardStats() {
    try {
        const stats = await apiRequest('/dashboard');
        const container = document.getElementById('dashboardStats');
        container.innerHTML = '';

        if (stats.totalProjects !== undefined) {
            container.innerHTML += createStatCard('Total Projects', stats.totalProjects, 'fa-building', 'blue');
        }
        if (stats.totalApplications !== undefined) {
            container.innerHTML += createStatCard('Applications', stats.totalApplications, 'fa-file-signature', 'purple');
        }
        if (stats.activeBookings !== undefined) {
            container.innerHTML += createStatCard('Active Bookings', stats.activeBookings, 'fa-calendar-check', 'orange');
        }
        if (stats.completedBookings !== undefined) {
            container.innerHTML += createStatCard('Completed Jobs', stats.completedBookings, 'fa-circle-check', 'green');
        }
    } catch (error) {
        console.error('Failed to load stats', error);
    }
}

function createStatCard(label, value, icon, colorClass) {
    return `
    <div class="stat-card card">
        <div class="stat-info">
            <p>${label}</p>
            <h3 style="font-size: 24px; color: var(--navy);">${value}</h3>
        </div>
        <div class="stat-icon" style="color: var(--${colorClass}); font-size: 24px; opacity: 0.8;">
            <i class="fa-solid ${icon}"></i>
        </div>
    </div>`;
}
