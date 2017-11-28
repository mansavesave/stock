var request = require('request');
var google = require('googleapis');
var sprintf = require('sprintf');
var mSuccessCallback;
var mFailCallback;

function get_sheet(auth) {
    var request = {
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',  // TODO: Update placeholder value.
        auth: auth      
    };

    var sheets = google.sheets('v4');

    sheets.spreadsheets.get(request, function(err, response) {
        if(err) {
            // Handle error
            console.log(err);
            if(mFailCallback != undefined){
                mFailCallback();
            }
        } else {
            console.log("get_sheet response:" + response);
            if(mSuccessCallback != undefined){
                mSuccessCallback(response);
            }
        }
    });

}

function set_param(success_callback, fail_callback) {
    mSuccessCallback = success_callback;
    mFailCallback = fail_callback;
}

var self_module = {
    set_param:set_param,
    get_sheet:get_sheet
}

module.exports = self_module;

/*{
  "spreadsheetId": "1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg",
  "properties": {
    "title": "test_sheet",
    "locale": "zh_TW",
    "autoRecalc": "ON_CHANGE",
    "timeZone": "Asia/Shanghai",
    "defaultFormat": {
      "backgroundColor": {
        "red": 1,
        "green": 1,
        "blue": 1
      },
      "padding": {
        "top": 2,
        "right": 3,
        "bottom": 2,
        "left": 3
      },
      "verticalAlignment": "BOTTOM",
      "wrapStrategy": "OVERFLOW_CELL",
      "textFormat": {
        "foregroundColor": {},
        "fontFamily": "arial,sans,sans-serif",
        "fontSize": 10,
        "bold": false,
        "italic": false,
        "strikethrough": false,
        "underline": false
      }
    }
  },
  "sheets": [
    {
      "properties": {
        "sheetId": 0,
        "title": "工作表1",
        "index": 0,
        "sheetType": "GRID",
        "gridProperties": {
          "rowCount": 1000,
          "columnCount": 26
        }
      }
    },
    {
      "properties": {
        "sheetId": 134224826,
        "title": "history2",
        "index": 1,
        "sheetType": "GRID",
        "gridProperties": {
          "rowCount": 1000,
          "columnCount": 26
        }
      }
    },
    {
      "properties": {
        "sheetId": 1593002825,
        "title": "history",
        "index": 2,
        "sheetType": "GRID",
        "gridProperties": {
          "rowCount": 1000,
          "columnCount": 26
        }
      }
    },
    {
      "properties": {
        "sheetId": 1074248588,
        "title": "all_stocks",
        "index": 3,
        "sheetType": "GRID",
        "gridProperties": {
          "rowCount": 12852,
          "columnCount": 26
        }
      }
    }
  ],
  "spreadsheetUrl": "https://docs.google.com/spreadsheets/d/1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg/edit"
}*/