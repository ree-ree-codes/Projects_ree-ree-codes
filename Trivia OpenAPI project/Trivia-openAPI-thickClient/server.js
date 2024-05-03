/*
(c) 2024 Reesau Avis



NOTE: You need to install the npm modules by executing >npm install
before running this server

Simple express server porviding trivia questions from the open trivia databse API, the data base will generate up to 50 questions

(if no input or invalid is provided it defaults to 10 questions)
To test:
http://localhost:3000
or
http://localhost:3000/weather?city=Ottawa
to just set JSON response. (Note it is helpful to add a JSON formatter extension, like JSON Formatter, to your Chrome browser for viewing just JSON data.)
*/
const express = require('express') //express framework
const path = require('path');
const hbs = require('hbs'); // Import Handlebars
const axios   = require('axios'); // module that does smth similar to fetch, found it online after fetch was acting weird
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('data/db_TriviaUsers'); //database to store users

const PORT = process.env.PORT || 3000 //allow environment variable to possible set PORT

/*An api-key is not needed for this api, instead a fetch request will be sent to the generated URL
*/

let API_URL= 'https://opentdb.com/api.php?amount=50'

const app = express()

// Set up Handlebars as the view engine
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));

//asynchronurs function, used to fetch trivia questions, takes in the amount of questions to be generated

async function fetchTriviaQuestions(amount) {
  try {
    const response = await axios.get(`https://opentdb.com/api.php?amount=${amount}&type=multiple`);
    if (!response.data.results) {
      throw new Error("No questions found.");
    }
    return response.data.results;
  } catch (error) {
    console.error("Error fetching questions:", error.message);
    return [];
  }
}

//Middleware

//static server
app.use(express.static(path.join(__dirname, 'public')));
app.use((req, res, next) => {
  if (req.url.endsWith('.js')) {
    res.setHeader('Content-Type', 'text/javascript');
  }
  next();
});

// Middleware for parsing JSON and URL-encoded bodies
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

//middleware to redirect
app.use((request, response, next) => {
  const requestedUrl = request.originalUrl;
  if (requestedUrl == '/index.html') {
      return response.redirect('/');
  }
  next();
});

// Middleware for login authentication
const requireAuth = (req, res, next) => {
  const auth = req.headers.authorization;

  if (!auth) {
    res.setHeader('WWW-Authenticate', 'Basic realm="Need to login"');
    return res.sendStatus(401); // Terminate the request
  }

  const credentials = Buffer.from(auth.split(' ')[1], 'base64').toString().split(':');
  const username = credentials[0];
  const password = credentials[1];

  // Authenticate user (you may replace this with your own authentication logic)
  db.get('SELECT * FROM users WHERE userid = ? AND password = ?', [username, password], (err, row) => {
    if (err || !row) {
      res.setHeader('WWW-Authenticate', 'Basic realm="Unauthorized"');
      return res.sendStatus(401); // Terminate the request
    }
    // Store user details in request for later use
    req.user = row;

    
    next(); // Proceed to next middleware/route
  });

  

};





// Middleware to fetch trivia questions
app.use(async (request, response, next) => {
  try {
    let amount = request.query.amount || 10; // Default to 10 questions if amount is not specified in the query
    if (amount>50){
        amount = 50;
    }
    request.triviaQuestions = await fetchTriviaQuestions(amount);
    next(); // Call the next middleware function
  } catch (error) {
    console.error("Error fetching trivia questions:", error.message);
    response.status(500).send("Server busy, try again?");
  }
});



//Routes
app.get('/', requireAuth,(request, response) => {
  //response.sendFile(__dirname + '/views/index.html');
  //render with handlebars
  const currentUserID = request.user.userid;

  response.render('index', { triviaQuestions: request.triviaQuestions,currentUserID: currentUserID});
});

app.get('/trivia', (request, response) => {
  const amount = parseInt(request.query.amount) || 10; // Default to 10 questions if amount is not specified or invalid
  const triviaSubset = request.triviaQuestions.slice(0, amount); // Take the first 'amount' questions
  response.json(triviaSubset);
});

app.get('/login', (request, response) => {
  
  response.render('login', { title: 'Welcome new User'});
});

// Route for user page, accessible only to admin users
app.get('/users', requireAuth, (req, res, next) => {
  // Check if user is admin
  if (req.user.role !== 'admin') {
    // If not admin, prompt for credentials again
    res.setHeader('WWW-Authenticate', 'Basic realm="Need to login as admin"');
    return res.sendStatus(401); // Unauthorized
  }

  // If user is authenticated and admin, proceed to next middleware/route
  next();
}, (req, res) => {
  // Render user page only if user is admin
  db.all('SELECT * FROM users', (err, rows) => {
    if (err) {
      console.error('Error fetching users:', err);
      res.status(500).send('Error fetching users');
    } else {
      res.render('users', { title: 'User Page', userEntries: rows });
    }
  });
});


app.post('/submitAnswer', requireAuth, (req, res) => {
  const { correctAnswersCount } = req.body;

  // Assuming you have a user object stored in req.user
  const { userid } = req.user;

  // Update the user's points by adding the correctAnswersCount
  db.run('UPDATE users SET points = points + ? WHERE userid = ?', [correctAnswersCount, userid], (err) => {
      if (err) {
          console.error('Error incrementing points:', err);
          res.sendStatus(500);
      } else {
          console.log('User points incremented successfully');
          res.sendStatus(200);
      }
  });
});

app.post('/login', (req, res) => {
  const { username, password } = req.body;

  // Check if the user exists in the database
  db.get('SELECT * FROM users WHERE userid = ?', [username], (err, row) => {
    if (err) {
      console.error('Error fetching user:', err);
      return res.sendStatus(500);
    }

    if (!row) {
      // If the user doesn't exist, it's a new user
      // Add the new user to the database
      db.run('INSERT INTO users (userid, password, role, points) VALUES (?, ?, ?, ?)', 
        [username, password, 'guest', 0], 
        (err) => {
          if (err) {
            console.error('Error adding new user:', err);
            return res.sendStatus(500);
          }
          
          // Pass the newly created user's information to the home page
          res.render('index', { currentUser: { username, role: 'guest', points: 0 } });
      });
    } else {
      // If the user already exists, just render the home page with their information
      res.render('index', { currentUser: row });
    }
  });
});


//start server
app.listen(PORT, err => {
  if(err) console.log(err)
  else {
    console.log(`Server listening on port: ${PORT}`)
    console.log(`To Test:`)
    console.log(`http://localhost:3000`)
    console.log(`http://localhost:3000/index.html`)
    console.log(`http://localhost:3000/trivia`)
    console.log(`http://localhost:3000/users`)
    console.log(`http://localhost:3000/login`)
  }
})

