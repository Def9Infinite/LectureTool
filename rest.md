# REST resources & endpoints
## Session
Created by a lecturer. Questions, users, etc. are all part of a session. Sessions have no overlap between each other.
#### Object:
```
power_token: 1235abc1235214 // Used for joining a session as a lecturer/mod
student_token: hj132981209 // Used for joining a session as a student
start_time: 2021-03-03T09:20:31Z //ISO date for when the session opens (probably not in first versions)
```
#### Endpoints
 - `GET /sessions/` - returns list of sessions
 - `POST /session/` - creates new session. Return session object.
 - `GET /session/{id}/` - returns session object with given id.

## Question
#### Object:
```
id: 1
user_id: 123
text: Is the sky blue?
answer: null
answered: false
post_time: 2021-03-03T09:20:31Z
```
#### Endpoints
- `GET /session/{id}/questions/` return list of all questions. Will include the possibility for filtering. Will be sorted by relevance.
- `POST /session/{id}/question/` create new question
- `PUT /session/{id}/question/{id}/` update question
- `POST /session/{id}/question/{id}/upvote` upvote question with `id`.
#### Example usage
Update a question to be answered during the lecture:
```
PUT /session/2/question/1/
{"answered": true}
```

## User
I think a user should only be created when joining, which means that there is standard `POST` call.  The user object should NOT return the token, since this is used for authentication in subsequent requests. Token should only be visisble in the response to the join call.
#### Object
```
id: 1
nickname: Nathan K
ip_adress: 182.133.191.017 //Not sure if we should return this, maybe only to admin users. Probably not something we really need to worry about though.
role: student
```
#### Endpoints
 - `GET /session/{id}/user/{id}` returns user object
 - `GET /session/{id}/users/` returns list of all users in the session
 - `PUT /session/{id}/users/` update user info

## Miscellaneous 
Join flow via URL might be a bit harder than we thought. Maybe in V1 just join by submitting a token in the application somewhere? Via a real URL we need to register some sort of intent with the OS I think. SOmething so that the OS lets us handle certain types of URLs in our app, isntead of the browser.
