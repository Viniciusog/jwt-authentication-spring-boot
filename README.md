# jwt-authentication-spring-boot
JWT authentication with spring boot

#### How to run? 
```git clone https://github.com/Viniciusog/jwt-authentication-spring-boot.git``` <br>
Run the application in your IDE <br>

```http://localhost:5000/```

#### /signup

request

```json
{
  "name": "John",
  "username": "userjohn",
  "email": "john@gmail.com",
  "password": "johnpassword"
}
```

response

```json
{
  "success": true,
  "message": "User registered successfully"
}
```

#### /signin

request

```json
{
  "usernameOrEmail": "john@gmail.com",
  "password": "johnpassword"
}
```

response

```json
{
  "accessToken": "eyJpc3MiOiJodHRwOi8vdHJ1c3R5YXBwLmNvbS8iLCJleHAiOjEzMDA4MTkzODAsInN1YiI6InVzZXJzLzg5ODM0NjIiLCJzY29wZSI6InNlbGYgYXBpL2J1eSJ9",
  "tokenType": "Bearer"
}
```

Header Authentication - for any request

```json
{
  "Authentication": "Bearer eyJpc3MiOiJodHRwOi8vdHJ1c3R5YXBwLmNvbS8iLCJleHAiOjEzMDA4MTkzODAsInN1YiI6InVzZXJzLzg5ODM0NjIiLCJzY29wZSI6InNlbGYgYXBpL2J1eSJ9"
}
```
