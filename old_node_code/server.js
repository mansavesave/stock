var http = require("http");
var url = require("url");
var fs = require("fs");
var formidable = require("formidable");

var body = '<html>'+
    '<head>'+
    '<meta http-equiv="Content-Type" content="text/html; '+
    'charset=UTF-8" />'+
    '</head>'+
    '<body>'+
    '<form action="/upload" enctype="multipart/form-data" method="post">'+
    '<input type="file" name="upload" multiple="multiple" >'+
    '<input type="submit" value="Upload file" />'+
    '</form>'+
    '</body>'+
    '</html>';


var sample = '<form action="/upload" enctype="multipart/form-data" '+
    'method="post">'+
    '<input type="text" name="title"><br>'+
    '<input type="file" name="upload" multiple="multiple"><br>'+
    '<input type="submit" value="Upload">'+
    '</form>';

var imgpath = "C:/Users/Wells.Chen/Desktop/javascript練習/images.jpg";

http.createServer(Request
).listen(8888);

function Request(request, response) {
    var postData = "";
    var pathname = url.parse(request.url).pathname;
//    console.log("Request pathname:" + pathname);
    console.log("Request request.url:" + request.url);
    
   switch(request.url.toLowerCase()) {
    case "/show":
       show(request, response);
        break;
    case "/upload":
        upload(request, response);
        break;   
    default:
        default_handler(request, response);
   }
}

function show(request, response) {
    console.log("+show");
    fs.readFile(imgpath, "binary", function(error, file) {
            if(error) {
              response.writeHead(500, {"Content-Type": "text/plain"});
              response.write(error + "\n");
              response.end();
            } else {
              response.writeHead(200, {"Content-Type": "image/png"});
              response.write(file, "binary");
              response.end();
            }
          });
}

function upload(request, response) {
    console.log("+upload");
    var form = new formidable.IncomingForm();

    form.parse(request, function(error, fields, files) {
        console.log("parsing done");
        fs.renameSync(files.upload.path, imgpath);
        response.writeHead(200, {"Content-Type": "text/html"});
        response.write("received image:<br/>");
        response.write("<img src='/show' />");
        response.end();
    });
}

function default_handler(request, response) {
    console.log("+default_handler");
    response.writeHead(200, {"Content-Type": "text/html"});
    response.write(body);
    response.end();  
    console.log("Request end");
}

