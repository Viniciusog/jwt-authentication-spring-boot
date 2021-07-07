# jwt-authentication-spring-boot
JWT authentication with spring boot

#### How to run? 
```git clone https://github.com/Viniciusog/jwt-authentication-spring-boot.git```
Run the application in your IDE

#### /signup

```json
{
  "name": "John",
  "username": "userjohn",
  "email": "john@gmail.com",
  "password": "password"
}
```

result

```json
{
  "success: true,
  "message": "User registered successfully"
}
```

#### /signin

```json
{
  "accessToken": "43DXvhrwMGeLLlP4P4izjgsBB2yrpo82oiUPhADakLs",
  "tokenType": "Bearer"
}
```

Header Authentication

```json
{
  "Authentication": "Bearer 43DXvhrwMGeLLlP4P4izjgsBB2yrpo82oiUPhADakLs"
}
```
