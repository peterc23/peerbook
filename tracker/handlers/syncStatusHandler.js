var allowSync = true;

function start(req, res){
    allowSync = true;
    res.send("done", 200);
}

function stop(req, res){
    allowSync = false;
    res.send("done", 200);
}

function status(req,res){
    if (allowSync == true ){
        res.send("ok", 200);
    } else {
        res.send("notok", 200);
    }
}

exports.start = start;
exports.stop = stop;
exports.status = status;