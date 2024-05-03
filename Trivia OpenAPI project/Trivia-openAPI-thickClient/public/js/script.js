let triviaQuestions = [];

function getTriviaQuestions() {

    //the api I'm using is a bit finnicky and sometimes doesn't generate questions on the first ask, in this case I'd like the function to try again, given a few secs so that the user doesn't have to click the submit button again
    
    let retryCount = 0;
    const maxRetries = 3; // Maximum number of retries
    const retryDelay = 1000; // Delay between retries in milliseconds
    
    const makeRequest = () => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    let response = JSON.parse(xhr.responseText);
                    if (response.length > 0) {
                        // Questions received successfully, update table
                        triviaQuestions = response;
                        updateTriviaQuestionsTable();
                        document.getElementById('submitAnswers').style.display = 'block'; // Show submit button
                    } else {
                        // If the response is empty, retry
                        retry();
                    }
                } else if (xhr.status == 429 && retryCount < maxRetries) {
                    // Retry if status code is 429 (Too Many Requests)
                    retry();
                } else {
                    // Handle other errors 
                    console.error('Error:', xhr.status);
                }
            }
        };
        xhr.open('GET', '/trivia?amount=' + document.getElementById('quantity').value, true);
        xhr.send();
    };
    
    const retry = () => {
        // Increment retry count
        retryCount++;
        // Retry after delay
        setTimeout(makeRequest, retryDelay);
    };

    // Start the initial request
    makeRequest();

}

function addToPlaylist(index) {
    let selectedQuestion = triviaQuestions[index];
    let question = { question: selectedQuestion.question, category: selectedQuestion.category };
    playlist.push(question);
    updatePlaylistTable();
    savePlaylistToLocalStorage();
}


function updateTriviaQuestionsTable() {
    let triviaDiv = document.getElementById('triviaResult');
    let tableHTML = '<table>';
    tableHTML += '<tr><th>Question</th><th>Category</th><th>Answers</th></tr>';
    triviaQuestions.forEach((question, questionIndex) => {
        tableHTML += `<tr>
                        <td>${question.question}</td>
                        <td>${question.category}</td>
                        <td>`;
        // Prepare all answers
        let allAnswers = question.incorrect_answers.concat(question.correct_answer);
        // Shuffle the answers to display randomly
        allAnswers.sort(() => Math.random() - 0.5);
        // Create a radio button for each answer with a unique name attribute based on question ID and question index
        allAnswers.forEach((answer, answerIndex) => {
            tableHTML += `<input type="radio" name="answer_${question.id}_${questionIndex}" value="${answer}">
                            <label>${answer}</label><br>`;
        });
        tableHTML += `</td></tr>`;
    });
    tableHTML += '</table>';
    triviaDiv.innerHTML = `<h1>Trivia Questions</h1>` + tableHTML;
}

function submitAnswers() {
    // Reset correctAnswersCount before counting correct answers for the current submission
    correctAnswersCount = 0;

    triviaQuestions.forEach((question, index) => {
        const selectedAnswer = document.querySelector(`input[name="answer_${question.id}_${index}"]:checked`);
        if (selectedAnswer) {
            const userAnswer = selectedAnswer.value;
            const correctAnswer = question.correct_answer;
            const answerLabels = document.querySelectorAll(`input[name="answer_${question.id}_${index}"] + label`);

            answerLabels.forEach(label => {
                if (label.textContent === userAnswer) {
                    label.style.color = 'red'; // Incorrect selection
                }
                if (label.textContent === correctAnswer) {
                    label.style.color = 'green'; // Correct answer
                    if (label.textContent === userAnswer)
                    correctAnswersCount++; // Increment the count of correct answers
                }
            });
        }
    });
 // Send the count of correct answers to the server
 incrementPoints(correctAnswersCount);
}


function incrementPoints(correctAnswersCount) {
    const xhr = new XMLHttpRequest();
    const url = '/submitAnswer';
    const data = JSON.stringify({ correctAnswersCount }); // Send the count of correct answers
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log('User points incremented successfully');
            } else {
                console.error('Error incrementing user points:', xhr.status);
            }
        }
    };
    xhr.send(data);
}


//event listeners
const ENTER=13

function handleKeyUp(event) {
event.preventDefault()
   if (event.keyCode === ENTER) {
      document.getElementById("get_trivia_button").click()
  }
}




document.addEventListener('DOMContentLoaded', function() {
  
  document.getElementById('submitAnswers').style.display = 'none';
  document.getElementById('get_trivia_button').addEventListener('click', getTriviaQuestions);
  document.getElementById('submitAnswers').addEventListener('click', submitAnswers);
  //add key handler for the document as a whole, not separate elements.
  document.addEventListener('keyup', handleKeyUp)
  
})

