document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('login-form'); // Get the login form by ID
    const loginButton = document.getElementById('log-in'); // Get the login button by ID

    // Attach event listener to the form submit event
    loginForm.addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent the default form submission

        // Get the username and password values
        const username = document.getElementById('username').value; // Correct ID
        const password = document.getElementById('password').value; // Correct ID

        // Create the data object to send in the request
        const data = {
            username: username,
            password: password
        };

        // Send data to the backend for login
        fetch('http://localhost:8080/req/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Invalid username or password.');
                }
                return response.text(); // Change to .text() to handle plain text response
            })
            .then(data => {
               // Display the server's response (e.g., "Login successful")
                // Redirect to the desired page after login
                console.log("user Logged in Succesfully");
                window.location.href = '/index';
            })
            .catch(error => {
                console.log('Error during login:', error);
                alert(error.message); // Show error message to the user
            });


    });
});