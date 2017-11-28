var google = require('googleapis');

var utility_google_login = require('./utility_google_login');
var param = [read_sample];

utility_google_login.login(param);




function listMajors(auth) {
  var sheets = google.sheets('v4');
  sheets.spreadsheets.values.get({
    auth: auth,
    spreadsheetId: '1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms',
    range: 'Class Data!A2:E',
  }, function(err, response) {
    if (err) {
      console.log('The API returned an error: ' + err);
      return;
    }
    var rows = response.values;
    if (rows.length == 0) {
      console.log('No data found.');
    } else {
      console.log('Name, Major:');
      for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        // Print columns A and E, which correspond to indices 0 and 4.
        console.log('%s, %s', row[0], row[4]);
      }
    }
  });
}

function write_sample(auth) {
    var values = [
    [
    "1","2","3","4","5"
    ],
    [
    "11111","222","33","44","55"
    ]
    ];
    
    var body = {
        values: values
    };
    
    var sheets = google.sheets('v4');
    
    sheets.spreadsheets.values.update({
        auth: auth,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: '工作表1!11:12',
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

function read_sample(auth) {
    var sheets = google.sheets('v4');
    sheets.spreadsheets.values.get({
        auth: auth,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: 'sheet2!A4:B',
    }, function(err, response) {
        if (err) {
          console.log('The API returned an error: ' + err);
          return;
        }
        var rows = response.values;
        if (rows.length == 0) {
          console.log('No data found.');
        } else {
          console.log('Name, Major:');
          for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            // Print columns A and E, which correspond to indices 0 and 4.
            console.log('%s, %s', typeof row[0] != 'undefined' ? row[0].length: -1, typeof row[1] != 'undefined' ? row[1].length:-1);
          }
        }
  });
  
}