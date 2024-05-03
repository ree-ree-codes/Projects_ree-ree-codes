
Author   : Reesau Avis
Student #: 101264010


REESAU AVIS, 101264010


                    ### Assignment 5 ###

About: webapp allows you to play and store points from a trivia game. It's built on top of the Open Trivia Databse. It's accessed by URL rather than an API key, more info can be found here: https://opentdb.com/api_config.php 

rendering has been done with the Handlebars module.



To install npm modules execute:
>npm install

This will install the modules listed as dependencies in the package.json file.


###Launch Instructions:
(the file is within the demo-code directory)
Launch from terminal with the command: 
node server.js
or 
npm start


###Testing Instuctions:
(these will also appear in terminal, when running the server file, you can clink the link there rather than copy pasting)

use these to log in:

       UserId        Pword
Admin: Reese_A       nunyaB

Guest: ldnel         secret

for a guest without points, you may add your own or try:
Guest: noob          1234
Guest: test2         test
Guest: player 1      lego


###Notes: bugs of note
1) the database is a little slow sometimes and more often than not will send error 429 to the server, In that case I've told the server to just wait a few and ask again, this sometimes results in the questions taking a few seconds to load. (I'd rather you didn't spam the submit button, but you do you)

2)there's currently a bit of an issue with being signed is once a new user is made, but the users are in the data base once created.

To Test:
http://localhost:3000
http://localhost:3000/index.html
http://localhost:3000/trivia
http://localhost:3000/users
http://localhost:3000/login


Thankyou for reading :D