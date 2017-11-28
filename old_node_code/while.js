var a = [[1,2,3], [2,3,4]];
var b = [[1,1],[2,2],[3,3]];

var result_row = a.length + b.length;
var result_column = a[0].length + b[0].length;
var result = new Array(result_row);
for(var i = 0 ;i < result_row ; i++) {
    result[i] = new Array(result_column);
    for(var j = 0 ; j < result_column; j++) {
        if (i < a.length && j < a[0].length) {
            result[i][j] =  a[i][j];
            } else if(i >= a.length && j>= a[0].length) {
                result[i][j] =  b[i - a.length][j - a[0].length];
            } else {
                result[i][j] = 0;
            }         
    }
    
}

//console.log("myprint = " + myprint(a));

myprint(a);

function myprint() {
    for(i in arguments){
        console.log("arguments[%d] = %s", i, arguments[i]);
    }
}


