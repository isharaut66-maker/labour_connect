async function login(event) {
    event.preventDefault();
    const form = event.target;
    const email = form.email.value;
    const password = form.password.value;

    try {
        const res = await apiRequest('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ email, password })
        });
        if (res.success) {
            window.location.href = 'pages/dashboard.html';
        } else {
            alert(res.message);
        }
    } catch (error) {
        alert(error.message);
    }
}

async function register(event) {
    event.preventDefault();
    const form = event.target;
    
    // Get the selected role
    let role = 'CLIENT';
    const selectedRoleEl = document.querySelector('.role-card.selected');
    if (selectedRoleEl) {
        role = selectedRoleEl.dataset.role;
    }

    const data = {
        name: form.name.value,
        email: form.email.value,
        mobile: form.mobile.value,
        password: form.password.value,
        role: role
    };

    if (data.password !== form.confirmPassword.value) {
        alert('Passwords do not match!');
        return;
    }

    try {
        const res = await apiRequest('/auth/register', {
            method: 'POST',
            body: JSON.stringify(data)
        });
        if (res.success) {
            alert('Registration successful! Please login.');
            toggleAuthForm('login');
        } else {
            alert(res.message);
        }
    } catch (error) {
        alert(error.message);
    }
}

async function logout() {
    try {
        await apiRequest('/auth/logout', { method: 'POST' });
        window.location.href = '../index.html';
    } catch (error) {
        console.error(error);
    }
}

function toggleAuthForm(type) {
    if (type === 'register') {
        document.getElementById('loginForm').classList.add('hidden');
        document.getElementById('registerForm').classList.remove('hidden');
        document.getElementById('authTitle').innerText = 'Create Your Account';
    } else {
        document.getElementById('loginForm').classList.remove('hidden');
        document.getElementById('registerForm').classList.add('hidden');
        document.getElementById('authTitle').innerText = 'Welcome Back';
    }
}

function selectRole(el) {
    document.querySelectorAll('.role-card').forEach(c => c.classList.remove('selected'));
    el.classList.add('selected');
}
