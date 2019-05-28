const express = require('express'),
http = require('http'),
app = express(),
server = http.createServer(app),
io = require('socket.io').listen(server);
io.on('connection', (socket) => {
    const _id = socket.id
    console.log('user connected with : '+_id)

    socket.on('join', function(userNickname) {
            console.log(userNickname +" : has joined the chat "  )
            socket.broadcast.emit('userjoinedthechat',userNickname +" : has joined the chat ")
            socket.user = userNickname;
    });

    socket.on('messagedetection', (senderNickname,messageContent) => {
        //log the message in console 
        console.log(senderNickname+" :" +messageContent)
        //create a message object
        let  message = {"message":messageContent, "senderNickname":senderNickname}
         // send the message to the client side  
        io.emit('message', message )
    });
    
    socket.on('disconnect', function() {
        console.log( socket.user+ ' has left ')
        socket.broadcast.emit( "userdisconnect" ,socket.user + ' has left')
    });
});

var port = process.env.PORT || '3000';

app.get('/', (req, res) => {
    res.send('Chat Server is running on port '+port);
});


server.listen(port,()=>{
    console.log('Node app is running on port '+port);
});