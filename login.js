const mysql = require('mysql');
const express = require('express');
const http = require('http');
const morgan = require('morgan');
var dateTime = require('node-datetime');
var multer  = require('multer');
var fs  = require("fs");

var app = express();
const bodyparser = require('body-parser');
//app.use(bodyparser.urlencoded({ extended: true }));
app.use(bodyparser.json({limit: '50mb'}));
app.use(bodyparser.urlencoded({limit: '50mb', extended: true}));
app.use(express.static('node'));
app.use('/Ressources/',express.static(__dirname + '/Ressources'));
app.use(morgan('short'));



var mysqlConnection = mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'',
    database:'job_hunter'
});

mysqlConnection.connect((err)=>{
    if(!err)
    console.log('DB connection success.');
    else
    console.log('DB connect failed. \n ERROR :' +JSON.stringify(err,undefined,2));
});

//-----WEBSOCKET INITS
const server = http.createServer(app);
const io = require('socket.io').listen(server);

//--------------------
var onlineUsersIds = [];
var onlineUsersSockets = [];
//======WEBSOCKET.IO IMPLEMENTATION========
io.on('connection', (socket) => {

    console.log('user connected')
    
    socket.on('join', function(userId) {
        console.log(userId +" : has joined the chat "  );
        onlineUsersIds.push(userId);
        onlineUsersSockets.push(socket.id);
        socket.broadcast.emit('userjoinedthechat',userId +" : has   joined the chat ");
        console.log(onlineUsersIds);
        console.log(onlineUsersSockets);
    })

    socket.on('messagedetection', (senderId, receiverId, messageContent, socketID) => { //sent from client
        //--- Insert message in DB------------------------------------------
        var dt = dateTime.create();
        var formatted_date = dt.format('d/m/Y H:M:S');
        var sql = "INSERT INTO message (sender, receiver, content, sent_at) VALUES (?,?,?,NOW())";
        mysqlConnection.query(sql,[senderId,receiverId,messageContent],
        (err, rows, fields)=>{
            if(!err){    
                console.log("1 record inserted");
            }
            else{
                console.log(err);
            }   
        });
        //------------------------------------------------------------------
        //log the message in console 
        console.log(senderId+" : " +messageContent+"  TO : "+receiverId);
        //------
        var test_exist_id;
        var socket_rec;
        test_exist_id = onlineUsersIds.includes(receiverId);
        if(test_exist_id == true){
            socket_rec = onlineUsersSockets[onlineUsersIds.indexOf(receiverId)];
        }

        var test_exist_id2;
        var socket_rec2;
        test_exist_id2 = onlineUsersIds.includes(senderId);
        if(test_exist_id2 == true){
            socket_rec2 = onlineUsersSockets[onlineUsersIds.indexOf(senderId)];
        }

        if(!socket_rec){
            console.log("loooool");
            console.log("receiver id: "+receiverId)
            mysqlConnection.query("insert into notification(user_id, from_user, created) values(?,?, NOW())",[receiverId, senderId],function(err, rows, fields){});

            /*mysqlConnection.query("select count(*) as cnt from notification where user_id=?",[receiverId],(err, rows, fields)=>{
                if(!err){
                    //console.log("CCCOUUUUUUUUUUUUUUUUUUNT:  "+rows[0].cnt); 
                    io.to(socket_rec).emit('notif', {notif_count: rows[0].cnt, receiver_id: receiverId});
                }
                else
                console.log(err);
            });*/
        }
        
        console.log("Receiver Socket: "+socket_rec);
        console.log("Sender Socket: "+socket_rec2);
        
        //------
        io.to(socket_rec).emit('message',{ id_sender: senderId, id_receiver: receiverId, message: messageContent, socket_id: socket_rec }); //send to client
        io.to(socket_rec2).emit('message',{ id_sender: senderId, id_receiver: receiverId, message: messageContent, socket_id: socket_rec });
    })

    /*socket.on('join', function(userNickname) {
        console.log(userNickname +" : has joined the chat "  );
        onlineUsers.push(userNickname);
        socket.broadcast.emit('userjoinedthechat',userNickname +" : has   joined the chat ");
    })*/
    
    /*socket.on('messagedetection', (senderId, receiverId, messageContent) => {
        //log the message in console 
        console.log(senderId+" : " +messageContent+"  TO : "+receiverId);
        //create a message object 
        let  message = {"message":messageContent, "senderId":senderId, "receiverId":receiverId};
        // send the message to all users including the sender  using io.emit   
        io.emit('message',{ id_sender: senderId, id_receiver: receiverId, message: messageContent });  
        //io.emit('message', message );
    })*/
    
    socket.on('disconnect', function() {
        //console.log(userNickname +' has left ');
        //socket.broadcast.emit( "userdisconnect" ,' user has left');
        var test_exist_id;
        var socket_rec;
        test_exist_id = onlineUsersSockets.includes(socket.id);
        if(test_exist_id == true){
            onlineUsersIds.splice(onlineUsersSockets.indexOf(socket.id),1);
            onlineUsersSockets.splice(onlineUsersSockets.indexOf(socket.id),1);
        }
        console.log(onlineUsersIds);
        console.log(onlineUsersSockets);
    })
    
    
});
//=========================================
//--------------------
server.listen(3000,()=>console.log('express server is running on 3000'));




//Signin
app.get('/login/:email/:password',(req,res)=>{
    mysqlConnection.query("select user.*, cv.id as 'cv_id' from user LEFT JOIN cv on user.id = cv.user_id where email=? and password=?",[req.params.email,req.params.password],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " utilisateur non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
        
    });
});

//get user by id
app.get('/getuser/:id',(req,res)=>{
    mysqlConnection.query("select user.*, cv.id as 'cv_id' from user LEFT JOIN cv on user.id = cv.user_id where user.id=?",[req.params.id],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " utilisateur non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
        
    });
});

//Signup Applicant
app.get('/signupapplicant/:email/:password/:name/:lastname',(req,res)=>{
    var new_user_id;
    var dt = dateTime.create();
    var formatted_date = dt.format('d/m/Y');

    let user = req.params;
    var sql = "INSERT INTO user (email,password,name,last_name,type, picture) VALUES (?,?,?,?,?,?)";
    mysqlConnection.query(sql,[user.email,user.password,user.name,user.lastname,'a','profile-default.png'],
    (err, rows, fields)=>{
        new_user_id = rows.insertId;
        if(!err){   
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur ajouter" },undefined,2));
            var sql = "INSERT INTO cv (creation_date,user_id) VALUES (?,?)";
            mysqlConnection.query(sql,[formatted_date,new_user_id],
            (err, rows, fields)=>{
                if(!err){    
                    //console.log("inserted cv : user = "+new_user_id);
                }
                else{
                    //aaa  
                }   
            });
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        } 
    });
    
});

//Signup Company
app.get('/signupcompany/:email/:password/:name',(req,res)=>{
    let user = req.params;
    var sql = "INSERT INTO user (email,password,name,type) VALUES (?,?,?,?)";
    mysqlConnection.query(sql,[user.email,user.password,user.name,'c'],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur ajouter" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Get CV
app.get('/cv/:iduser',(req,res)=>{
    mysqlConnection.query("SELECT * FROM cv where user_id=?",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " cv trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " cv non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Jobs by company
app.get('/job/:iduser',(req,res)=>{
    mysqlConnection.query("SELECT * FROM job where user_id=?",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " jobs trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " jobs non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get All Jobs
app.get('/jobs',(req,res)=>{
    mysqlConnection.query("select job.*, user.name, user.picture, user.adress from job LEFT JOIN user on user.id = job.user_id",(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " jobs trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " jobs non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Internship by company
app.get('/internship/:iduser',(req,res)=>{
    mysqlConnection.query("SELECT * FROM internship where user_id=?",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " internships trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " internships non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get All Internships
app.get('/internships',(req,res)=>{
    mysqlConnection.query("select internship.*, user.name, user.picture, user.adress from internship LEFT JOIN user on user.id = internship.user_id",(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " internship trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " internship non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Add Job
app.get('/addjob/:label/:description/:startdate/:contracttype/:careerreq/:skills/:userid',(req,res)=>{
    let user = req.params;
    var sql = "INSERT INTO job (label,description,start_date,contract_type,career_req,skills, user_id) VALUES (?,?,?,?,?,?,?)";
    mysqlConnection.query(sql,[user.label,user.description,user.startdate,user.contracttype,user.careerreq,user.skills,user.userid],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " job ajouter" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Add Internship
app.get('/addinternship/:label/:description/:startdate/:educreq/:skills/:duration/:userid',(req,res)=>{
    let user = req.params;
    var sql = "INSERT INTO internship (label,description,start_date,educ_req,duration, skills, user_id) VALUES (?,?,?,?,?,?,?)";
    mysqlConnection.query(sql,[user.label,user.description,user.startdate,user.educreq, user.skills,user.duration,user.userid],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " internship ajouter" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Fill Applicant Profile
app.get('/userprofile/:iduser/:birthdate/:gender/:adress/:tel1/:tel2/:nationality/:description',(req,res)=>{
    let user = req.params;
    var sql = "UPDATE user SET birth_date=?,gender=?,adress=?,tel1=?,tel2=?,nationality=?,description=? WHERE id=?";
    mysqlConnection.query(sql,[user.birthdate,user.gender,user.adress,user.tel1,user.tel2,user.nationality,user.description,user.iduser],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur modifier" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Fill Company Profile
app.get('/companyprofile/:iduser/:adress/:tel1/:tel2/:fax/:description',(req,res)=>{
    let user = req.params;
    var sql = "UPDATE user SET adress=?,tel1=?,tel2=?,fax=?,description=? WHERE id=?";
    mysqlConnection.query(sql,[user.adress,user.tel1,user.tel2,user.fax,user.description,user.iduser],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur modifier" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Get Education by CV
app.get('/education/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM education where cv_id=? order by start_date desc",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " educations trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " educations non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Experience by CV
app.get('/experience/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM project where cv_id=? order by start_date desc",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " experience trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " experience non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Certification by CV
app.get('/certification/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM certification where cv_id=? order by cert_date desc",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " certification trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " certification non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Volunteer by CV
app.get('/volunteer/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM volunteer where cv_id=? order by start_date desc",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " volunteer trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " volunteer non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Language by CV
app.get('/language/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM language where cv_id=?",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " language trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " language non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get Volunteer by CV
app.get('/skills/:idcv',(req,res)=>{
    mysqlConnection.query("SELECT * FROM skill where cv_id=?",[req.params.idcv],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " skill trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " skill non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Add Application job
app.get('/addappjob/:iduser/:idjob',(req,res)=>{
    let app_job = req.params;
    var dt = dateTime.create();
    var formatted_date = dt.format('d/m/Y');

    var sql = "INSERT INTO user_job (id_user,id_job,creation_date) VALUES (?,?,?)";
    mysqlConnection.query(sql,[app_job.iduser,app_job.idjob,formatted_date],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " application job ajouter" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//Add Application Internship
app.get('/addappinternship/:iduser/:idinternship',(req,res)=>{
    //let app_internship = req.params;
    var dt = dateTime.create();
    var formatted_date = dt.format('d/m/Y');

    var sql = "INSERT INTO user_internship (id_user,id_internship,creation_date) VALUES (?,?,?)";
    mysqlConnection.query(sql,[req.params.iduser,req.params.idinternship,formatted_date],
    (err, rows, fields)=>{
        if(!err){    
        res.setHeader('Content-Type', 'application/json');
        res.send(JSON.stringify({ "success": 1 , "message" : " application internship ajouter" },undefined,2));
        }
        else{
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(err,undefined,2));   
        }   
    });
});

//get internship application by user id
app.get('/getintappbyuser/:iduser/:idinternship',(req,res)=>{
    mysqlConnection.query("select * from user_internship where id_user=? and id_internship=?",[req.params.iduser,req.params.idinternship],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get job application by user id
app.get('/getjobappbyuser/:iduser/:idjob',(req,res)=>{
    mysqlConnection.query("select * from user_job where id_user=? and id_job=?",[req.params.iduser,req.params.idjob],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get job by id
app.get('/getjobbyid/:idjob',(req,res)=>{
    mysqlConnection.query("select job.*, user.name, user.picture, user.adress from job LEFT JOIN user on user.id = job.user_id where job.id=?",[req.params.idjob],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " job trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " job non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get internship by id
app.get('/getinternshipbyid/:idinternship',(req,res)=>{
    mysqlConnection.query("select internship.*, user.name, user.picture, user.adress from internship LEFT JOIN user on user.id = internship.user_id where internship.id=?",[req.params.idinternship],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " internship trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " internship non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get job application by user
app.get('/getjobappbyuserid/:iduser',(req,res)=>{
    mysqlConnection.query("select user_job.*,job.*,user.name,user.picture from user_job left join job on user_job.id_job = job.id left join user on job.user_id = user.id where id_user=?",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get internship application by user
app.get('/getintappbyuserid/:iduser',(req,res)=>{
    mysqlConnection.query("select user_internship.*,internship.*,user.name, user.picture from user_internship left join internship on user_internship.id_internship = internship.id left join user on internship.user_id = user.id where id_user=?",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get Approuved job application by user
app.get('/getapprouvedjobappbyuserid/:iduser',(req,res)=>{
    mysqlConnection.query("select user_job.*,job.*,user.name,user.picture, user.type, user.last_name from user_job left join job on user_job.id_job = job.id left join user on job.user_id = user.id where id_user=? and state='a'",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//get Approuved job application by Companyid
app.get('/getapprouvedjobappbycompany/:iduser',(req,res)=>{
    mysqlConnection.query("select user_job.*,job.*,user.name,user.picture, user.type, user.last_name from user_job left join job on job.id = user_job.id_job left join user on user.id = user_job.id_user where job.user_id=? and user_job.state='a'",[req.params.iduser],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " application trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " application non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

/*//upload image
app.post('/updatePic',function(req,res){
    var id = req.body.iduser;
    var img = req.body.image;
    var data = {
        "Data":""
    };
    mysqlConnection.query("update user set picture=? WHERE id=?",[img,id],function(err, rows, fields){
        if(rows.length != 0){
            data["Data"] = "Successfully updated image..";
            res.json(data);
        }else{
            data["Data"] = "Failed update image..";
            res.json(data);
        }
    });
});*/

app.post('/uploadImage/profile', function(req, res) {
    var fileName = req.file["filename"];
    var upload = multer({ dest: 'Ressources/uploadedImages/'}).single('file')
    upload(req, res, function(err) {
        if (err) {
            console.log("Error uploading file: " + err)
            return
        }

        var originalName = req.file["originalname"];
        var arr = originalName.split(".");
        var extension = arr[arr.length-1];

        fs.rename('./Ressources/uploadedImages/' + fileName, './Ressources/uploadedImages/' + fileName + extension, function(err) {
            if ( err ) console.log('ERROR: ' + err);
        });
        
        fileName += req.file["filename"] ;
        mysqlConnection.query("update user set picture=? WHERE id=?",[fileName+"."+extension,req.body.user_id],function(err, rows, fields){});
        res.json(fileName + ".jpg");
    });
});

//Upload IOS
app.post('/uploadImageios/profile', function(req, res) {
    var fileName = "" ;
    var upload = multer({ dest: 'Ressources/uploadedImages/'}).single('file')
    upload(req, res, function(err) {
        console.log(req.body)
        if (err) {
            console.log("Error uploading file: " + err)
            return
        }

        var originalName = req.file["originalname"];
        var arr = originalName.split(".");
        var extension = arr[arr.length-1];

        fs.rename('./Ressources/uploadedImages/' + req.file["filename"], './Ressources/uploadedImages/' + req.file["filename"] + "." +extension, function(err) {
            if ( err ) console.log('ERROR: ' + err);
        });
        
        fileName += req.file["filename"] ;
        //mysqlConnection.query("update user set picture=? WHERE id=?",[fileName+"."+extension,req.body.user_id],function(err, rows, fields){});
        res.json(fileName + ".jpg");
    });
});

//Add Education
app.post('/addeduc', function(req, res) {
    mysqlConnection.query("insert into education(inst_name, start_date, end_planned_date, degree, domain, result, description, cv_id) values(?,?,?,?,?,?,?,?)",[req.body.inst_name, req.body.start_date, req.body.end_planned_date, req.body.degree, req.body.domain, req.body.result, req.body.description, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Add Exp
app.post('/addexp', function(req, res) {
    mysqlConnection.query("insert into project(label, type, place, description, start_date, end_date, still_going, establishment_name, cv_id) values(?,?,?,?,?,?,?,?,?)",[req.body.label, req.body.type, req.body.place, req.body.description, req.body.start_date, req.body.end_date, req.body.still_going, req.body.establishment_name, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Add Skills
app.post('/addskill', function(req, res) {
    mysqlConnection.query("insert into skill(label, cv_id) values(?,?)",[req.body.label, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Add Certif
app.post('/addcert', function(req, res) {
    mysqlConnection.query("insert into certification(label, cert_authority, licence_num, if_expire, cert_date, expire_date, cv_id) values(?,?,?,?,?,?,?)",[req.body.label, req.body.cert_authority, req.body.licence_num, req.body.if_expire, req.body.cert_date, req.body.expire_date, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Add Language
app.post('/addlang', function(req, res) {
    mysqlConnection.query("insert into language(label, level, cv_id) values(?,?,?,?,?,?,?,?)",[req.body.label, req.body.level, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Add Volunteer
app.post('/addevolun', function(req, res) {
    mysqlConnection.query("insert into volunteer(organisation, role, domain, start_date, end_date, still_going, description, cv_id) values(?,?,?,?,?,?,?,?)",[req.body.organisation, req.body.role, req.body.domain, req.body.start_date, req.body.end_date, req.body.still_going, req.body.description, req.body.cv_id],function(err, rows, fields){});
    res.json("Success");
});

//Get MEssages
app.get('/getmessages/:idsender/:idreceiver',(req,res)=>{
    mysqlConnection.query("select message.*, s.name as sender_name, r.name as receiver_name from message left join user s on s.id=message.sender left join user r on r.id=message.receiver where (sender=? and receiver=?) or (sender=? and receiver=?) order by sent_at asc",[req.params.idsender, req.params.idreceiver, req.params.idreceiver, req.params.idsender],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " message trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " message non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//delete Notifs
app.get('/deletenotifs/:sender/:receiver', function(req, res) {
    mysqlConnection.query("delete from notification where user_id=? and from_user=?",[req.params.receiver, req.params.sender],function(err, rows, fields){
        if(rows.length){ 
            res.json("success");
        } else {
            res.json("empty");
        }
    });
});

//Get Notifs count

app.get('/getnotifs/:id',(req,res)=>{
    mysqlConnection.query("select count(*) as cnt from notification where user_id=?",[req.params.id],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " message trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " message non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//Get full notifications

app.get('/getfullnotifs/:id',(req,res)=>{
    mysqlConnection.query("select distinct from_user from notification",[req.params.id],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " notif trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " notif non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
    });
});

//update profile
app.post('/updateprofile', function(req, res) {
    mysqlConnection.query("update user set name=?, last_name=?, nationality=?, adress=?, tel1=?, tel2=?, password=?, description=?, picture=? where id=?",
    [req.body.name, req.body.last_name, req.body.nationality, req.body.adress, req.body.tel1, req.body.tel2, req.body.password, req.body.picture, req.body.id],function(err, rows, fields){});
    res.json("Success");
});
//only pic
app.post('/updateprofile2', function(req, res) {
    mysqlConnection.query("update user set picture=? where id=?",
    [req.body.name, req.body.last_name, req.body.nationality, req.body.adress, req.body.tel1, req.body.tel2, req.body.password, req.body.picture, req.body.id],function(err, rows, fields){});
    res.json("Success");
});















//================================Nayer Webservices
/************************************************/

app.get('/job/applicants/:id',(req,res)=>{
    var user = {};
    var user_job = {};
  
      mysqlConnection.query("SELECT u.*,j.id_job,j.creation_date FROM user_job j,user u where id_job=? and u.id=j.id_user",[req.params.id],(err, rows, fields)=>{
          if(!err){
              if(rows.length){
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 1 , "message" : " applicants trouvé" , "result" : rows },undefined,2));
              }
              else{
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 0,  "message" : " applicants non trouvé"  },undefined,2));
              }
          }
          else
          console.log(err);
  
      });
  });
  
  /***************************************************************/
  app.get('/internship/applicants/:id',(req,res)=>{
    var user = {};
    var user_job = {};
  
      mysqlConnection.query("SELECT u.*,j.id_internship,j.creation_date FROM user_internship j,user u where id_internship=? and u.id=j.id_user",[req.params.id],(err, rows, fields)=>{
          if(!err){
              if(rows.length){
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 1 , "message" : " applicants trouvé" , "result" : rows },undefined,2));
              }
              else{
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 0,  "message" : " applicants non trouvé"  },undefined,2));
              }
          }
          else
          console.log(err);
  
      });
  });
  //update Job
  app.get('/updatejob/:id/:label/:description/:startdate/:contracttype/:careerreq/:skills',(req,res)=>{
      let job = req.params;
      var sql = "UPDATE job SET label=?,description=?,start_date=?,contract_type=?,career_req=?,skills=? where id=?";
      mysqlConnection.query(sql,[job.label,job.description,job.startdate,job.contracttype,job.careerreq,job.skills,job.id],
      (err, rows, fields)=>{
          if(!err){
          res.setHeader('Content-Type', 'application/json');
          res.send(JSON.stringify({ "success": 1 , "message" : " job modifié" },undefined,2));
          }
          else{
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify(err,undefined,2));
          }
      });
  });
  
  //delete Job
  app.get('/deletejob/:id',(req,res)=>{
    let job = req.params;
    var sql = "DELETE from job where id=?";
    mysqlConnection.query(sql,[job.id],
    (err, rows, fields)=>{
          if(!err){
          res.setHeader('Content-Type', 'application/json');
          res.send(JSON.stringify({ "success": 1 , "message" : " job supprimé" },undefined,2));
          }
          else{
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify(err,undefined,2));
          }
      });
  })
  
  //delete internship
  app.get('/deleteinternship/:id',(req,res)=>{
    let internship = req.params;
    var sql = "DELETE from internship where id=?";
    mysqlConnection.query(sql,[internship.id],
    (err, rows, fields)=>{
          if(!err){
          res.setHeader('Content-Type', 'application/json');
          res.send(JSON.stringify({ "success": 1 , "message" : "internship supprimé" },undefined,2));
          }
          else{
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify(err,undefined,2));
          }
      });
  })
  
  //update Internship
  app.get('/updateinternship/:id/:label/:description/:startdate/:educ_req/:skills/:duration',(req,res)=>{
      let internship = req.params;
      var sql = "UPDATE internship SET label=?,description=?,start_date=?,educ_req=?,skills=?,duration=? where id=?";
      mysqlConnection.query(sql,[internship.label,internship.description,internship.startdate,internship.educ_req,internship.skills,internship.duration,internship.id],
      (err, rows, fields)=>{
          if(!err){
          res.setHeader('Content-Type', 'application/json');
          res.send(JSON.stringify({ "success": 1 , "message" : " internship modifié" },undefined,2));
          }
          else{
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify(err,undefined,2));
          }
      });
  });
  
  
  //getUser by email
  
  app.get('/getuserbyemail/:email',(req,res)=>{
      mysqlConnection.query("select count(email) FROM user where email=?",[req.params.email],(err, rows, fields)=>{
          if(!err){
              if(rows.length){
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur trouvé" , "result" : rows },undefined,2));
              }
              else{
                  res.setHeader('Content-Type', 'application/json');
                  res.send(JSON.stringify({ "success": 0,  "message" : " utilisateur non trouvé"  },undefined,2));
              }
          }
          else
          console.log(err);
  
      });
  });
  
  //Signup Applicant from linkedin
  app.get('/signupapplicant_linkedin/:email/:password/:name/:lastname/:picture',(req,res)=>{
      var new_user_id;
      var dt = dateTime.create();
      var formatted_date = dt.format('d/m/Y');
  
      let user = req.params;
      var sql = "INSERT INTO user (email,password,name,last_name,picture,type) VALUES (?,?,?,?,?,?)";
      mysqlConnection.query(sql,[user.email,user.password,user.name,user.lastname,user.picture,'a'],
      (err, rows, fields)=>{
          new_user_id = rows.insertId;
          if(!err){
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur ajouter" },undefined,2));
              var sql = "INSERT INTO cv (creation_date,user_id) VALUES (?,?)";
              mysqlConnection.query(sql,[formatted_date,new_user_id],
              (err, rows, fields)=>{
                  if(!err){
                      //console.log("inserted cv : user = "+new_user_id);
                  }
                  else{
                      //aaa
                  }
              });
          }
          else{
              res.setHeader('Content-Type', 'application/json');
              res.send(JSON.stringify(err,undefined,2));
          }
      });
  
  });

//get full user likedin by email
app.get('/getuserlikedin/:email',(req,res)=>{
    mysqlConnection.query("select user.*, cv.id as 'cv_id' from user LEFT JOIN cv on user.id = cv.user_id where email=?",[req.params.email,req.params.password],(err, rows, fields)=>{
        if(!err){
            if(rows.length){ 
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 1 , "message" : " utilisateur trouvé" , "result" : rows },undefined,2));
            }
            else{
                res.setHeader('Content-Type', 'application/json');
                res.send(JSON.stringify({ "success": 0,  "message" : " utilisateur non trouvé"  },undefined,2));       
            }    
        }
        else
        console.log(err);
        
    });
});

