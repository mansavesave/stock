var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');
var google = require('googleapis');
var sprintf = require('sprintf');

var mSuccessCallback;
var mFailCallback;

var global_range = 'aaa!A1:B';

function read_sheet(auth) {    
    var sheets = google.sheets('v4');
    
    sheets.spreadsheets.values.get({
        auth: auth,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: global_range,
    }, function(err, result) {
        if(err) {
            // Handle error
            console.log(err);
             if(mFailCallback != undefined){
                mFailCallback(err);
            }
        } else {
             if(mSuccessCallback != undefined){
                mSuccessCallback(result);
            }
			console.log('result', result);

/*result { range: 'history!A1:B1000',
  majorDimension: 'ROWS',
  values:
   [ [ '日期', '成交股數' ],
     [ ' 81/01', '207881768' ],
     [ ' 81/02', '61018868' ],
     [ ' 81/03', '53494367' ],
     [ ' 81/12', '99207945' ],
     [ ' 82/01', '65335567' ] ] }*/            
        }
    });
  
}

function set_param(local_range, success_callback, fail_callback){
    global_range = local_range;
    mSuccessCallback = success_callback;
    mFailCallback = fail_callback;
}

var self_module = {
    set_param:set_param,
    read_sheet:read_sheet
}

module.exports = self_module;

