document.addEventListener("DOMContentLoaded", async () => {
    const user = await requireAuth();
    if (!user) return;
    
    // Load all labourers on load
    searchLabourers();
});

async function searchLabourers() {
    const skill = document.getElementById('searchSkill').value;
    const location = document.getElementById('searchLocation').value;
    
    let url = '/labourers?';
    if (skill) url += `skill=${encodeURIComponent(skill)}&`;
    if (location) url += `location=${encodeURIComponent(location)}`;
    
    try {
        const profiles = await apiRequest(url);
        renderLabourers(profiles);
    } catch (error) {
        console.error(error);
    }
}

function renderLabourers(profiles) {
    const grid = document.getElementById('labourersGrid');
    grid.innerHTML = '';
    
    if (profiles.length === 0) {
        grid.innerHTML = '<p>No labourers found matching your criteria.</p>';
        return;
    }
    
    profiles.forEach(p => {
        // Simple mock matching score based on if they have rating/experience
        const score = Math.min(100, (p.rating * 10) + (p.experience * 2) + 20);
        
        const card = document.createElement('div');
        card.className = 'project-card card';
        card.innerHTML = `
            <div style="display:flex; align-items:center; gap: 15px; margin-bottom: 15px;">
                <img src="https://i.pravatar.cc/150?u=${p.user.id}" alt="${p.user.name}" style="width: 60px; height: 60px; border-radius: 50%;">
                <div>
                    <h3>${p.user.name}</h3>
                    <p style="color: var(--orange); font-weight: 600;">${p.skill}</p>
                </div>
                <div style="margin-left:auto; text-align:right;">
                    <div class="badge badge-green">${Math.round(score)}% Match</div>
                    <div style="font-size:12px; margin-top:5px;"><i class="fa-solid fa-star text-orange"></i> ${p.rating}</div>
                </div>
            </div>
            <div style="font-size:13px; color:var(--muted); margin-bottom: 15px;">
                <p><i class="fa-solid fa-location-dot"></i> ${p.location}</p>
                <p><i class="fa-solid fa-briefcase"></i> ${p.experience} years exp</p>
                <p><i class="fa-solid fa-indian-rupee-sign"></i> ${p.dailyRate}/day</p>
            </div>
            <button class="btn btn-outline btn-sm" style="width:100%" onclick="alert('In a real app, this would open the booking modal or profile view for User ID: ${p.user.id}')">View Profile / Book</button>
        `;
        grid.appendChild(card);
    });
}
