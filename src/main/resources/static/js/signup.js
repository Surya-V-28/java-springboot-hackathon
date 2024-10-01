document.addEventListener('DOMContentLoaded', function () {
    const signupForm = document.querySelector('section');
    signupForm.style.opacity = 0;

    setTimeout(() => {
        signupForm.style.transition = 'opacity 1s ease-in-out';
        signupForm.style.opacity = 1;
    }, 500);

    const signupButton = document.getElementById('submit'); // Get the button by ID
    signupButton.addEventListener('click', function (event) {
        event.preventDefault();  // Prevent default form submission

        const emailInput = document.getElementById('email'); // Get email input by ID
        const passwordInput = document.getElementById('password'); // Get password input by ID
        const confirmPasswordInput = document.getElementById('passwordcon'); // Get confirm password input by ID

        // Check for a valid email and password (you can add your validation logic here)
        const isValid = emailInput.checkValidity() && passwordInput.checkValidity() && confirmPasswordInput.checkValidity();

        if (!isValid) {
            signupForm.classList.add('shake');
            setTimeout(() => {
                signupForm.classList.remove('shake');
            }, 1000);
            return; // Stop further execution if the form is invalid
        }

        // Proceed with data submission if validation passes
        const username = document.getElementById('username').value;
        const password = passwordInput.value; // Use the value directly from password input

        if (password !== confirmPasswordInput.value) {
            alert('Passwords do not match!');
            return;
        }

        const data = {
            username,
            email: emailInput.value,
            password
        };

        // Send data to backend on port 4000
        fetch('http://localhost:4000/req/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error during signup. Please try again.'); // Throw an error if response is not ok
                }
                return response.json();
            })
            .then(data => {
                alert('Signup successful!');
                console.log(data);  // Optional: Log the server response
                // You can redirect or clear the form here if needed
            })
            .catch(error => {
                console.log(error);
                alert(error.message); // Show error message to user
            });
    });
});
