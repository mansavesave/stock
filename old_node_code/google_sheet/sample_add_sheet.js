var google = require('googleapis');

var utility_google_login = require('./utility_google_login');

var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');


function add_new_sheet_task(auth) {
    global_auto = auth;
    request({url,  encoding: null}, request_function);
    
}


function google_write_all_stock_list(local_values) {
    var body = {
        values: local_values
    };
    
    var sheets = google.sheets('v4');
    
    sheets.spreadsheets.values.update({
        auth: global_auto,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: 'all_stocks!A1:B',
        resource: body,
        valueInputOption: "USER_ENTERED"
    }, function(err, result) {
        if(err) {
            // Handle error
            console.log(err);
        } else {
            console.log('%d cells updated.', result.updatedCells);
        }
    });
  
}


var param = [add_new_sheet_task];
utility_google_login.login(param);