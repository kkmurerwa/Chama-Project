let functions = require('firebase-functions');

let admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/groups/{groupId}/chatroom/{chatmessageId}')
.onWrite((snap, context) => {
	
	console.log("System: starting");
	console.log("snapshot: ", snap);
	console.log("snapshot.after: ", snap.after);
	console.log("snapshot.after.val(): ", snap.after.val());
	
	//get the message that was written
	let message = snap.after.val().message;
	let messageUserId = snap.after.val().user_id;
	console.log("message: ", message);
	console.log("user_id: ", messageUserId);
	
	//get the group id
	let groupId = context.params.groupId;
	console.log("group_id: ", groupId );
	
	return snap.after.ref.parent.parent.once('value').then(snap => {
		let data = snap.child('users').val();
		console.log("data: ", data);
		
		//get the number of users in the group
		let length = 0;
		for(value in data){
			length++;
		}
		console.log("data length: ", length);
		
		//loop through each user currently in the group
		let tokens = [];
		let i = 0;
		for(var user_id in data){
			console.log("user_id: ", user_id);
			
			//get the token and add it to the array 
			let reference = admin.database().ref("/Users/" + user_id);
			reference.once('value').then(snap => {
				//get the token
				let token = snap.child('messaging_token').val();
				console.log('token: ', token);
				tokens.push(token);
				i++;
				
				//also check to see if the user_id we're viewing is the user who posted the message
				//if it is, then save that name so we can pre-pend it to the message
				let messageUserName = "";
				if(snap.child('user_id').val() === messageUserId){
					messageUserName = snap.child('fname').val();
					console.log("message user name: " , messageUserName);
					message = messageUserName + ": " + message;
				}
				
				//Once the last user in the list has been added we can continue
				if(i === length){
					
					console.log("Construction the notification message.");
					const payload = {
						
						data: {
							data_type: "data_type_chat_message",
							title: "Chama App",
							message: message,
							sender_id: messageUserId,
							group_id: groupId
						}
					};
					
					
					return admin.messaging().sendToDevice(tokens, payload)
						
                }
                throw new Error("Doesn't exist")
			}).catch(function(error) {
                console.log("Error sending message:", error);
              });
			
		}
        throw new Error("Doesn't exist")
	});
});

