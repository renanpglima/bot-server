const PAGE_ACCESS_TOKEN = process.env.PAGE_ACCESS_TOKEN;
const HTTP_SERVER = "http://botserver-renanpglima.boxfuse.io:9000";

var menu = {
  "setting_type" : "call_to_actions",
  "thread_state" : "existing_thread",
  "call_to_actions":[
    {
      "type":"postback",
      "title":"Lojas",
      "payload":"stores"
    },
    {
      "type":"postback",
      "title":"Filmes",
      "payload":"movies"
    }
  ]
}

'use strict';

// Imports dependencies and set up http server
const 
  http = require('http'),
  request = require('request'),
  express = require('express'),
  body_parser = require('body-parser'),
  app = express().use(body_parser.json()); // creates express http server

// Sets server port and logs message on success
app.listen(process.env.PORT || 1337, () => console.log('webhook is listening'));


// Accepts POST requests at /webhook endpoint
app.post('/webhook', (req, res) => {  

  // Parse the request body from the POST
  let body = req.body;

  // Check the webhook event is from a Page subscription
  if (body.object === 'page') {

    // Iterate over each entry - there may be multiple if batched
    body.entry.forEach(function(entry) {

      // Get the webhook event. entry.messaging is an array, but 
      // will only ever contain one event, so we get index 0
      let webhook_event = entry.messaging[0];
      console.log(webhook_event);
      
      // Get the sender PSID
      let sender_psid = webhook_event.sender.id;
      console.log('Sender PSID: ' + sender_psid);
      
      // Check if the event is a message or postback and
      // pass the event to the appropriate handler function
      if (webhook_event.message) {
        handleMessage(sender_psid, webhook_event.message);        
      } else if (webhook_event.postback) {
        handlePostback(sender_psid, webhook_event.postback);
      }
      
    });

    // Return a '200 OK' response to all events
    res.status(200).send('EVENT_RECEIVED');

  } else {
    // Return a '404 Not Found' if event is not from a page subscription
    res.sendStatus(404);
  }

});

// Accepts GET requests at the /webhook endpoint
app.get('/webhook', (req, res) => {
  
  /** UPDATE YOUR VERIFY TOKEN **/
  const VERIFY_TOKEN = "TOKEN_TEST3";
  
  // Parse params from the webhook verification request
  let mode = req.query['hub.mode'];
  let token = req.query['hub.verify_token'];
  let challenge = req.query['hub.challenge'];
    
  // Check if a token and mode were sent
  if (mode && token) {
  
    // Check the mode and token sent are correct
    if (mode === 'subscribe' && token === VERIFY_TOKEN) {
      
      // Respond with 200 OK and challenge token from the request
      console.log('WEBHOOK_VERIFIED');
      res.status(200).send(challenge);
    
    } else {
      // Responds with '403 Forbidden' if verify tokens do not match
      res.sendStatus(403);      
    }
  }
});

var lastStoreIndex = 0;
var lastMovieIndex = 0;
// Handles messages events
function handleMessage(sender_psid, received_message) {
  let response;

  // Check if the message contains text
  if (received_message.text) {    
    
    console.log("MENSAGEM" + received_message);
    
    if (received_message.quick_reply){
      if (received_message.quick_reply.payload == "morestores"){
        getTittles("stores", lastStoreIndex , (currentIndex,stores) => {
          callSendAPI(sender_psid, stores);
          lastStoreIndex = currentIndex;
        });
      }else if (received_message.quick_reply.payload == "moremovies"){
        getTittles("movies", lastMovieIndex, (currentIndex, movies) => {
          callSendAPI(sender_psid, movies);
        });
      }
    }else{
      response = { "text": "Infelizmente ainda não entendo bem o que humanos falam. Use o menu para conversar comigo!" };
    }
  
    callSendAPI(sender_psid, response);
  }
}

function getTittles(type, currentIndex, cb){
  let response;
  
  http.get(`${HTTP_SERVER}/${type}`, res => {
      
    console.log( `${HTTP_SERVER}/${type}`);
    res.setEncoding("utf8");
      let tittleContents = "";
      res.on("data", data => tittleContents += data );
      res.on("end", () => {
        tittleContents = JSON.parse(tittleContents);
        
        var resp = "";
        var max = currentIndex+10;
        
        for (var i = currentIndex; i < max; i++){
          if (i == tittleContents.length){
            currentIndex = 0;
            break;
          }
          resp += tittleContents[i].title + "\n";
          currentIndex++;
        }
        
        var btnMessage;
        if (type == "stores")
          btnMessage = "mais lojas";
        else if (type == "movies")
          btnMessage = "mais filmes";
          
        
        response = {
          "text": `${resp}`,
          "quick_replies":
          [
            {
              "content_type":"text",
              "title":btnMessage,
              "payload":`more${type}`
            }
          ]
        }
        cb(currentIndex, response);
      });
      
    }).on("error", (err) => {
        console.log("Error: " + err.message);
    });
}

// Handles messaging_postbacks events
function handlePostback(sender_psid, received_postback) {
  let response;
  
  // Get the payload for the postback
  let payload = received_postback.payload;

  // Set the response based on the postback payload
  if (payload === 'bemvindo'){
    response = { "text": "Olá, bem vindo! Sou um bot de teste, então não se assuste se eu não entender bem o que você quer :)" }
    callSendAPI(sender_psid, response);
  }else if (payload === 'movies') {
    getTittles("movies", 0 , (currentIndex,movies) => {
      callSendAPI(sender_psid, movies);
      lastMovieIndex = currentIndex;
    });
  } else if (payload === 'stores') {
    getTittles("stores", 0 , (currentIndex,stores) => {
      callSendAPI(sender_psid, stores);
      lastStoreIndex = currentIndex;
    });
  }
  
}

// Sends response messages via the Send API
function callSendAPI(sender_psid, response) {
  // Construct the message body
  let request_body = {
    "recipient": {
      "id": sender_psid
    },
    "message": response
  }

  // Send the HTTP request to the Messenger Platform
  request({
    "uri": "https://graph.facebook.com/v2.6/me/messages",
    "qs": { "access_token": PAGE_ACCESS_TOKEN },
    "method": "POST",
    "json": request_body
  }, (err, res, body) => {
    if (!err) {
      console.log('message sent!' + sender_psid + ' - ' + response);
    } else {
      console.error("Unable to send message:" + err);
    }
  }); 
}

