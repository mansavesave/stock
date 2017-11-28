var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');
var google = require('googleapis');
var sprintf = require('sprintf');

var mSuccessCallback;
var mFailCallback;

var global_values = [];
var global_range = 'aaa!A1:B';

function write_sheet(auth) {
    var body = {
        values: global_values
    };
    
    var sheets = google.sheets('v4');
    
    sheets.spreadsheets.values.update({
        auth: auth,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: global_range,
        resource: body,
        valueInputOption: "USER_ENTERED"
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
            console.log('%d cells updated.', result.updatedCells);
			console.log('result', result);

/*result { spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
    updatedRange: 'aaa!A2:B3',
    updatedRows: 2,
    updatedColumns: 2,
    updatedCells: 4 }*/
            
        }
    });
  
}

function set_param(local_range, local_values, success_callback, fail_callback){
    global_range = local_range;
    global_values = local_values;
    mSuccessCallback = success_callback;
    mFailCallback = fail_callback;
}

var self_module = {
    set_param:set_param,
    write_sheet:write_sheet
}

module.exports = self_module;

