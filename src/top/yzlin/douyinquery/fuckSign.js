//参考自ladyrick的github项目
//https://github.com/ladyrick/DouyinBackup/blob/master/phantomjs/sign.js
var args = require('system').args;

if (args.length == 1) {
    console.log("user_id is required.");
    phantom.exit(-1);
}

// use user_id to get the signature.

var page = require("webpage").create();
var user_id = args[1];
var url = "https://www.amemv.com/share/user/" + user_id;
var statuscode = 0; // store the status code phantom will return.
page.open(url, function (status) {
    if (status == "success") {
        var info = page.evaluate(function (user_id) {
            var module = "douyin_falcon:node_modules/byted-acrawler/dist/runtime";
            if (window.__M) {
                signature = __M.require(module).sign(user_id);
                return {status: 0, msg: signature};
            } else {
                return {status: -1, msg: "error occured. maybe invalid user_id."};
            }
        }, user_id);
        statuscode = info.status;
        console.log(info.msg);
    } else {
        statuscode = -1;
        console.log("status: " + status + ". maybe check your network?");
    }
    phantom.exit(statuscode);
});