var request = require('request');
var google = require('googleapis');
var sprintf = require('sprintf');
var mSheetId = -1;
var mTitle = "title";
var mSuccessCallback;
var mFailCallback;

function add_sheet(auth) {
    var body = {
        "requests": [
            {
              "addSheet": {
                 "properties": {
                  "sheetId": mSheetId,
                  "title": mTitle,
                  "index": 0
                }
              }
            }
        ],
    };
    var request = {
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',  // TODO: Update placeholder value.
        auth: auth,
        resource: body        
  };
    
    var sheets = google.sheets('v4');

    sheets.spreadsheets.batchUpdate(request, function(err, response) {
        if(err) {
            // Handle error
            console.log(err);
            if(mFailCallback != undefined){
                mFailCallback();
            }
        } else {
            console.log("response:" + response);
            console.log(JSON.stringify(response, null, 2));
            if(mSuccessCallback != undefined){
                mSuccessCallback();
            }
        }
    });

}

function set_param(sheetid, title, success_callback, fail_callback) {
    mSheetId = sheetid;
    mTitle = title;
    mSuccessCallback = success_callback;
    mFailCallback = fail_callback;
}

var self_module = {
    set_param:set_param,
    add_sheet:add_sheet
}

module.exports = self_module;