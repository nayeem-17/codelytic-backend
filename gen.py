import requests
import random
import string

# Generate a random email and password
email = ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(10)) + "@gmail.com"
password = ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(10))

# Create a user using a POST request
user_data = {
    "name": "string",
    "email": email,
    "role": "USER",
    "password": password
}

response = requests.post('http://localhost:8000/user/register', json=user_data)
user_id = response.json().get('id')

# Print the user ID
print(f"User ID: {user_id}")

# Authenticate and get a token
auth_data = {
    "username": email,
    "password": password
}

auth_response = requests.post('http://localhost:8000/authenticate', json=auth_data)
token = auth_response.json().get('token')

# Print the token
print(f"Token: {token}")
