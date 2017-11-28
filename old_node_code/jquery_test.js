//require("jsdom").env("", function(err, window) {
//    if (err) {
//        console.error(err);
//        return;
//    }
//
//    var $ = require("jquery")(window);
//});


var parameter = {
  url: 'https://docs.google.com/spreadsheets/d/1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg/edit?usp=sharing',
  name: '工作表1',
  startRow: 1,
  startColumn: 1
};

var request = require('request');
var iconv = require('iconv-lite');
//var script_url = "https://script.google.com/d/1wz184zzpCdDcn-nK16Ne_I7gBp0uOvJrGjX4NJK-OI5NvrGGXOHPyIfC/edit?usp=sharing";
var script_url = "https://script.google.com/macros/s/AKfycbwbPK2q0EJBv67N4cXON9r8wkvHWdhE9qrJH9d3UdwgzoPmdLM/exec";

function request_function(err, res, body){

    //var big5_body = iconv.decode(body, "big5");
    var big5_body = iconv.decode(body, "utf8");
    console.log(big5_body);   
}

request({
    url: script_url,
    method: "GET",
    qs: parameter,
    encoding: null
  }, request_function);

