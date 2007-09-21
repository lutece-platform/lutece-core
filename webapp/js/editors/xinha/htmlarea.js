if(typeof _editor_url=="string"){
_editor_url=_editor_url.replace(/\x2f*$/,"/");
}else{
alert("WARNING: _editor_url is not set!  You should set this variable to the editor files path; it should preferably be an absolute path, like in '/htmlarea/', but it can be relative if you prefer.  Further we will try to load the editor files correctly but we'll probably fail.");
_editor_url="";
}
document.write("<script type=\"text/javascript\" src=\""+_editor_url+"XinhaCore.js\"></script>");

