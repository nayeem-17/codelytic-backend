import random
import string

import requests

# Generate a random email and password
email = "bali@gmail.com"
password = "bali"

# Create a user using a POST request
user_data = {
    "name": "string",
    "email": email,
    "role": "USER",
    "password": password
}

response = requests.post('https://codelytic-dev.onrender.com/user/register', json=user_data)
user_id = response.json().get('id')

# Print the user ID
print(f"User ID: {user_id}")

# Authenticate and get a token
auth_data = {
    "username": email,
    "password": password
}

auth_response = requests.post('https://codelytic-dev.onrender.com/authenticate', json=auth_data)
token = auth_response.json().get('token')

# Print the token
print(f"Token: {token}")

# Post a course
course_data = {
  "title": "string",
  "icon": "string",
  "description": "",
  "tagIds": [
    0
  ]
}


course_creation_response = requests.post('https://codelytic-dev.onrender.com/course', json=course_data, headers={'Authorization': f'Bearer {token}'})

course_id=course_creation_response.json().get('id')

# create a subsection on this link https://codelytic-dev.onrender.com/course/subsection/{course_id}
subsection_data = "string"
# get a response
subsection_data_response=requests.post(f'https://codelytic-dev.onrender.com/course/subsection/{course_id}',  headers={'Authorization': f'Bearer {token}'},data=subsection_data)
subsectionId=subsection_data_response.json().get('subsectionId')

# create a lecture on this link https://codelytic-dev.onrender.com/course/lecture/{subsectionId}

lecture_data = {
  "id": 0,
  "title": "string",
  "body": "string",
  "live": True
}

lecture_data_response=requests.post(f'https://codelytic-dev.onrender.com/course/lecture/{subsectionId}', json=lecture_data, headers={'Authorization': f'Bearer {token}'})
lectureId=lecture_data_response.json().get('lectureId')

# create a quiz on this link https://codelytic-dev.onrender.com/course/quiz/{subsectionId}

quiz_data = {
    "id": 0,
  "questions": [
    {
      "id": 0,
      "question": "string",
      "options": [
        "string"
      ],
      "answer": "string"
    }
  ]
}

quiz_data_response=requests.post(f'https://codelytic-dev.onrender.com/course/quiz/{subsectionId}', json=quiz_data, headers={'Authorization': f'Bearer {token}'})
quizId=quiz_data_response.json().get('quizId')

# create a post on this link https://codelytic-dev.onrender.com/post

post_data = {
  "title": "string",
  "content": "string",
  "tagIds": [
    1
  ]
}

post_data_response=requests.post('https://codelytic-dev.onrender.com/post', json=post_data, headers={'Authorization': f'Bearer {token}'})
postId=post_data_response.json().get('id')

# create a comment on this link https://codelytic-dev.onrender.com/comment

comment_data = {
    "content": "string",
  "parentCommentId": 0,
  "postId": postId
}    

comment_data_response=requests.post('https://codelytic-dev.onrender.com/comment', json=comment_data, headers={'Authorization': f'Bearer {token}'})

commentId=comment_data_response.json().get('id')

# post a like in this link https://codelytic-dev.onrender.com/like/{commentId}?commentId=1
like_data_response=requests.post(f'https://codelytic-dev.onrender.com/like/{commentId}?commentId=1', headers={'Authorization': f'Bearer {token}'})



