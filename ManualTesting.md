# Manual Testing Guide
The first step for all tests is to start the server application called demoapplication inside our project.
Then we start 2 client applications called mainapp inside our project.
### Creating and joining session moderator
- Click on the create session link
- Enter any text in the Course/Lecture name box
- Press the Create lecture button
- In the next screen press join as moderator
- Enter any text in the name box and press join lecture

If you now see the moderator view screen, the first test is complete.
From here on out this application will be refered to as moderator

### Joining session student
- Copy the student link from the moderator view
- In your other client application enter any text in the namefield
- Paste the link in the invite code field
- Press the join lecture button

If you now see the student view screen, the second test is complete.
From here on out this application will be refered to as student.

### Asking a question
- Enter text in the question field of the student
- Press the Ask button
- Check on the moderator to see if the question appears.

If the question appears the third test is complete.

### Asking a question error
- Enter no text in the question field of the student
- Press the Ask button
- Enter a text with >250 characters in the question field of the student
- Press the Ask button

If no questions were added to the view and both times a pop up appeared explaining what went wrong then the fourth test is complete.

### Upvoting a question
- Click on the triangle next to the question on the student view

If the triangle turned blue and the number below the triangle is 1 for both student and moderator test 5 is complete.

### Removing an upvote
- Click again on the triangle next to the question on the student view

If the triangle goes back to being white and the number below the triangle is 0 again for both student and moderator test 6 is complete.

### Question Order
- Ask another question on the student view
- Upvote this question

If the recently created question is higher in the list than the previous question test 7 is complete.

### Marking own question as answered
- On the client navigate to the your question tab
- On one of the questions press the checkmark

If the question now appears in the answered question tabs of both student and moderator test 8 is complete.

### Marking answer status moderator
- On the moderator press the checkmark of the question on the unanswered tab

If that question now appears in the answered question tabs of both student and moderator test 9 is complete.

### Delete own question
- On the client create another question
- Navigate to the your question tab
- Press the trashcan icon

If the question is no longer visible on both the student and the moderator test 10 is complete

### Deleting question moderator
- Create new question on the client
- In the moderator view unanswered tab press the trash can icon

If the question is no longer visible on both the student and the moderator test 11 is complete

### Lecture feedback
- On the client press any of the lecture feedback buttons

If a pop up appeared at the bottom of the client and the graph updated on the moderator test 12 is complete

### Editing question
- Create a new question on the client
- In the moderator view press the pen icon
- In the text box containing the orignal text type something new
- Press the arrow icon

If the question was changed on both client and moderator test 13 is complete

### Answering question
- Below the question you just created type something
- Press the arrow button

If the question is in the answered tab and has the answer in blue beneath the question for both the student and the moderator test 14 is complete

### Banning a user
- Press the red person button on the moderator
- Attempt to create a new question on the student

If a notification popped up and the question is not visible for both moderator and client test 15 is complete.

### Scheduling session
- Close both moderator and client, then open up a new client
- Click the create a lecture link and then the schedule a lecture link
- Type something in for the name and select a date/time combination in the past
- CLick schedule lecture button.
- If a notification was visible continue otherwise this test failed
- Now select a date/time combination that is 2 minutes in the future
- Click the schedule button
- If no notification was visible continue otherwise this test failed
- Click join as moderator, enter a name and click join lecture.
- The time now should still be before the scheduled time, if this is the case a notification should pop up telling you the time it starts, if so continue.
- Wait until the scheduled time and press join again

If you are now in the moderator view test 16 is complete.

### Exporting questions
- Create a new client view and join the session of the moderator
- Create a new question
- On the moderator view press export questions
- Check at the location of the application if there is a directory QuestionExports and within that check if there is a file containing the question just created.

If these are both the case test 17 is a succes

### Creating SessionLog
- In the moderator view press Create log
- Check at the location of the application if there is a directory SessionLogs and within that a file with the log of the session

If these are both the case test 18 is a succes.

### Creating a poll
- In the moderator view write some text in the question field
- Write some answers
- Click the create button

If on the moderator there is a Poll 1 next to the create new poll tab and there is a poll on the student test 19 is a succes.

### Respond to poll
- On the client press one of the poll options

If the option turns blue and on the moderator it says count 1 with the corresponding option test 20 is a succes.

### Close poll
- On the moderator press the close poll button at the bottom of the poll
- On the client attempt to pick a different option

If on the client it now shows the count of 1 next to the picked option and the picked option did not change test 21 is a succes.

### Presenter View
- In the moderator view press switch to presenter

If you are now in the presenter view test 22 is a succes.

### Close the Session
- In the moderator press on the switch to moderator view
- Click on the close session button
- On the client attempt to send a new question

If a notification said the session was closed test 23 is a succes.

### Join a closed session as a moderator
- Open a new mainapp and attempt to join the same session using the moderator token

If the moderator view shows up test 24 is a succes

