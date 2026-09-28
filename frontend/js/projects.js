let currentUser = null;

document.addEventListener("DOMContentLoaded", async () => {
    currentUser = await requireAuth();
    if (!currentUser) return;

    if (currentUser.role === 'CLIENT' || currentUser.role === 'CONTRACTOR' || currentUser.role === 'ORGANIZATION') {
        document.getElementById('btnCreateProject').style.display = 'inline-block';
        loadClientProjects();
    } else {
        document.getElementById('btnCreateProject').style.display = 'none';
        loadAvailableProjects();
    }
});

function toggleProjectForm() {
    const form = document.getElementById('projectForm');
    form.classList.toggle('hidden');
}

async function createProject(event) {
    event.preventDefault();
    const data = {
        title: document.getElementById('pTitle').value,
        description: document.getElementById('pDesc').value,
        projectType: document.getElementById('pType').value,
        location: document.getElementById('pLocation').value,
        budget: parseFloat(document.getElementById('pBudget').value),
        startDate: document.getElementById('pStartDate').value
    };

    try {
        await apiRequest('/projects', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        alert('Project created successfully!');
        toggleProjectForm();
        loadClientProjects();
    } catch (error) {
        alert(error.message);
    }
}

async function loadClientProjects() {
    try {
        const projects = await apiRequest(`/projects?clientId=${currentUser.id}`);
        renderProjects(projects, true);
    } catch (error) {
        console.error(error);
    }
}

async function loadAvailableProjects() {
    try {
        const projects = await apiRequest('/projects');
        renderProjects(projects, false);
    } catch (error) {
        console.error(error);
    }
}

function renderProjects(projects, isClient) {
    const grid = document.getElementById('projectsGrid');
    grid.innerHTML = '';

    if (projects.length === 0) {
        grid.innerHTML = '<p>No projects found.</p>';
        return;
    }

    projects.forEach(p => {
        let actions = '';
        if (isClient) {
            actions = `<button class="btn btn-outline btn-sm" onclick="alert('View applications for Project ID: ${p.id}')">View Applications</button>`;
        } else {
            actions = `<button class="btn btn-primary btn-sm" onclick="applyToProject(${p.id})">Apply Now</button>`;
        }

        const card = document.createElement('div');
        card.className = 'project-card card';
        card.innerHTML = `
            <div class="project-top">
                <h3>${p.title}</h3>
                <span class="badge badge-blue">${p.status}</span>
            </div>
            <p>${p.description}</p>
            <div class="meta">
                <span><i class="fa-solid fa-location-dot"></i> ${p.location}</span>
                <span><i class="fa-solid fa-calendar"></i> Starts: ${p.startDate}</span>
            </div>
            <div style="display:flex; justify-content:space-between; align-items:center; margin-top: 15px;">
                <div class="price">₹${p.budget}</div>
                ${actions}
            </div>
        `;
        grid.appendChild(card);
    });
}

async function applyToProject(projectId) {
    const msg = prompt("Enter a message for your application:");
    if (msg === null) return;
    
    try {
        await apiRequest('/applications', {
            method: 'POST',
            body: JSON.stringify({
                project: { id: projectId },
                message: msg
            })
        });
        alert('Application submitted successfully!');
    } catch (error) {
        alert(error.message);
    }
}
