# IAM
This is the service responsible for authorization and authentication.

## Endpoints
* POST /signin -> Authorization: Basic [Base64]
* POST /signout -> Authorization: Bearer [Token]
* POST /token -> Authorization: Bearer [Token] (Not implemented)

### POST /signin
This is used to signin (login) the user using the Basic Authorization scheme.

The value of the header can be:
```text
Basic username:password 
or
Basic dXNlcm5hbWU6cGFzc3dvcmQ= (Base64)
```

Responses:
* **200**
```json
{
  "user": [user id],
  "token": [Token]
}
```
* **401**
```json
{
  "message": "User is not authorized"
}
``` 

This is only demo so this is not quite ready for production.
### Token
The token that is used by the system for authorization.
This token is only used for demo purposes and should not be used in a
production environment.  It is inspired by [JWT](https://jwt.io/)

Format:
```json
{
  "iss": "Wylie College",
  "sub": [username],
  "name": [user's name],
  "role": [student, registrar, professor],
  "exp": [ISO 8601 timestamp]
}
```
