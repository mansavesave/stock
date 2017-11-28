//get all sheets message, ex. sheets name ;utility_google_get
//add sheet ;utility_google_add_sheet
//delete sheet ;utility_google_delete_sheet
//remove all content in a sheet
//write value  ;utility_google_value_update
//read value  ;utility_google_value_get

var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');
var google = require('googleapis');
var sprintf = require('sprintf');
var utility_google_login = require('./utility_google_login');
var fetch_specific_stock = require('./fetch_specific_stock');
var utility_google_delete_sheet = require('./utility_google_delete_sheet');
var utility_google_add_sheet = require('./utility_google_add_sheet');
var utility_google_get = require('./utility_google_get');
var utility_google_value_update = require('./utility_google_value_update');
var utility_google_value_get = require('./utility_google_value_get');


//utility_google_add_sheet.set_param(1000)
//var param = [utility_google_add_sheet.add_sheet];
//utility_google_login.login(param);



//utility_google_get.set_param(function(json_obj){
//    var sheets = json_obj.sheets;
//    for(var i = 0; i < json_obj.sheets.length ; i++) {
//        console.log("each:" + sheets[i].properties.title + ", " + sheets[i].properties.sheetId);
//    }
//});
//var param = [utility_google_get.get_sheet];
//utility_google_login.login(param);



//var range = "aaa!A2:B";
//var value = [[1,1],[3,4]]
//utility_google_value_update.set_param(range, value);
//var param = [utility_google_value_update.write_sheet];
//utility_google_login.login(param);



var range = "history!A1:B";
utility_google_value_get.set_param(range);
var param = [utility_google_value_get.read_sheet];
utility_google_login.login(param);

