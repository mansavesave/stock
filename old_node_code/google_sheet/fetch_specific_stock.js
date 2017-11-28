var request = require('request');
var cheerio = require('cheerio');
var iconv = require('iconv-lite');
var google = require('googleapis');
var sprintf = require('sprintf');
var utility_google_login = require('./utility_google_login');

var global_auto = null;

//var total_data = [];
var is_addField = false;

var first_date = "19920101";
var current_date = "20170921";
//var current_date = "19930102"
var query_stock_num = 2002;

var total_query_date = [];

var unit = "day";//day, month

//http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=19920101&stockNo=2002
function getQueryURl(date_string, local_stock_number) {
    var url = "http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=" + date_string + "&stockNo=" + local_stock_number;
    return url;    
}

var start_row_value = 1;

//["日期","成交股數","成交金額","開盤價","最高價","最低價","收盤價","漲跌價差","成交筆數"]
//[" 81/01/04","5,280,000","137,435,600","26.10","26.30","25.60","25.70","0.00","1,349"]
function request_function(err, res, body){
    console.log("request_function");
    var utf8_body = iconv.decode(body, "utf8");
    var json_obj = JSON.parse(utf8_body);
    var fields = json_obj.fields;
    var data = json_obj.data;
    let totla_data_length = data.length;
    
    if (unit == "month"){
        total_month = [];
        for(var i = 0; i < data.length; i++) {
            let day_item = data[i];
            for(var j =0; j < day_item.length ; j++) {
                if (j == 0) { //day
                    if (total_month[0] == undefined) {
                        total_month[0] = day_item[0].substring(0, day_item[0].lastIndexOf("/"));
//                        console.log("index 0:" + total_month[0]);
                    }
                } else if (j == 1) {//成交股數
                    if (total_month[1] == undefined) {
                        total_month[1] = parseInt(day_item[1].replace(/,/g, ''), 10);
                    } else {
                        total_month[1] = total_month[1] + parseInt(day_item[1].replace(/,/g, ''), 10);
                    }
                } else if (j == 2) {//成交金額
                    if (total_month[2] == undefined) {
                        total_month[2] = parseInt(day_item[2].replace(/,/g, ''));
                    } else {
                        total_month[2] = total_month[2] + parseInt(day_item[2].replace(/,/g, ''));
                    }
                } else if (j == 3) {//開盤價
                    if (total_month[3] == undefined) {
                        total_month[3] = day_item[3];
                    }
                } else if (j == 4) {//最高價
                    if (total_month[4] == undefined) {
                        total_month[4] = day_item[4];
                    } else {
                        total_month[4] = day_item[4] >= total_month[4] ? day_item[4] : total_month[4];
                    }
                } else if (j == 5) {//最低價
                    if (total_month[5] == undefined) {
                        total_month[5] = day_item[5];
                    } else {
                        total_month[5] = day_item[5] >= total_month[5] ? total_month[5] : day_item[5];
                    }
                } else if (j == 6) {//收盤價
                    total_month[6] = day_item[6];
                } else if (j == 7) {//漲跌價差

                } else if (j == 8) {//成交筆數
                    if (total_month[8] == undefined) {
                        total_month[8] = parseInt(day_item[8].replace(/,/g, ''));
                    } else {
                        total_month[8] = total_month[8] + parseInt(day_item[8].replace(/,/g, ''));
                    }
                }

            }
            
        }
        total_month[7] = total_month[3] - total_month[6];
        total_month[7] = total_month[7].toFixed(2);
        console.log("total_month:" + total_month.length);
        for(var i = 0; i < total_month.length; i++) {
            console.log("total_month i:" + i + "result:"+ total_month[i]);
        }
        data = [];
        data.push(total_month);
    } else {//day       
    }
    
    if (is_addField == false) {
            data.unshift(fields);
            is_addField = true;
    }
    
   
    
    google_write_sheet(data);
}

function google_write_sheet(local_values) {
    var body = {
        values: local_values
    };
    
    var sheets = google.sheets('v4');
    
    sheets.spreadsheets.values.update({
        auth: global_auto,
        spreadsheetId: '1U_MzspnOPJtOdVJW-uwLzqhMfjbM2T3HHpajrApwGQg',
        range: 'history!A'+start_row_value+':I',
        resource: body,
        valueInputOption: "USER_ENTERED"
    }, function(err, result) {
        if(err) {
            // Handle error
            console.log(err);
        } else {
            start_row_value = start_row_value + local_values.length;
            console.log('%d cells updated.', result.updatedCells);
            
            query_date = total_query_date.shift();
//            console.log("query_date:" + query_date);
            if (query_date != undefined) {
                request({url:getQueryURl(query_date, query_stock_num), encoding: null}, request_function);
            }
        }
    });
  
}

function write_stock_history(auth) {
    global_auto = auth;
    var query_date = first_date;
    while(true) {
        total_query_date.push(query_date);
        var year = query_date.substring(0, 4);
        var month = query_date.substring(4, 6);
        var int_year = parseInt(year, 10);
        var int_month = parseInt(month, 10);
        int_month = int_month + 1;        
        if (int_month > 12) {
            int_month = 1;
            int_year = int_year + 1;
        }

        year = sprintf("%02d", int_year);
        month = sprintf("%02d", int_month);

        query_date = year + month + "01";
        if (parseInt(query_date, 10) > parseInt(current_date, 10)) {
            break;
        }
    }

    query_date = total_query_date.shift();
    if (query_date != undefined) {
        request({url:getQueryURl(query_date, query_stock_num), encoding: null}, request_function);
    }
    


//    for(var i = 0 ; i <total_query_date.length ; i++ ){
//        console.log(total_query_date[i]);
//    }
    
//    console.log(query_date);
//    request({url:getQueryURl(query_date, query_stock_num), encoding: null}, request_function);
    
    
   
}

function set_param(local_first_date, local_current_date, local_query_stock_num, local_unit){
    is_addField = false;
    first_date = local_first_date;
    current_date = local_current_date;
    query_stock_num = local_query_stock_num;
    unit = local_unit;
}

function run(){
    var param = [write_stock_history];
    utility_google_login.login(param);
}

var self_module = {
    set_param:set_param,
    run:run
}

module.exports = self_module;

