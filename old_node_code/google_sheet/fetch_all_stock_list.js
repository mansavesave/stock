
var google = require('googleapis');

var utility_google_login = require('./utility_google_login');

var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');

//he.decode(str);
var url = 'http://isin.twse.com.tw/isin/C_public.jsp?strMode=2';

var allStock = [];
var global_auto = null;


function request_function(err, res, body){

    var big5_body = iconv.decode(body, "big5");
    var $ = cheerio.load(big5_body);
    // 把要到的資料放進 cheerio

    var stockArray = []
    $('.h4 tbody tr td:first-child').each(function(i, elem){
        var eachblock = $(this).text();                
        stockArray.push(eachblock);
    });


    for(i = 0 ; i < stockArray.length ; i++) {
        split_number_name(stockArray[i]);
    }

     console.log(allStock);
     google_write_all_stock_list(allStock);

}

function split_number_name(str){
    var res = str.split("　");
    if (typeof res[1] != 'undefined') {
        allStock.push(res);
    }
    
}


function write_all_stock_process(auth) {
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


var param = [write_all_stock_process];
utility_google_login.login(param);
