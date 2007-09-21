function InsertSnippet(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"insertsnippet",tooltip:this._lc("Insert Snippet"),image:_1.imgURL("ed_snippet.gif","InsertSnippet"),textMode:false,action:function(_4){
_3.buttonPress(_4);
}});
_2.addToolbarElement("insertsnippet","insertimage",-1);
this.snippets=null;
var _5=_2.InsertSnippet.snippets+"?";
if(_2.InsertSnippet.backend_data!=null){
for(var i in _2.InsertSnippet.backend_data){
_5+="&"+i+"="+encodeURIComponent(_2.InsertSnippet.backend_data[i]);
}
}
Xinha._getback(_5,function(_7){
eval(_7);
_3.snippets=snippets;
});
}
InsertSnippet.prototype.onUpdateToolbar=function(){
if(!this.snippets){
this.editor._toolbarObjects.insertsnippet.state("enabled",false);
}else{
InsertSnippet.prototype.onUpdateToolbar=null;
}
};
InsertSnippet._pluginInfo={name:"InsertSnippet",version:"1.2",developer:"Raimund Meyer",developer_url:"http://rheinauf.de",c_owner:"Raimund Meyer",sponsor:"",sponsor_url:"",license:"htmlArea"};
InsertSnippet.prototype._lc=function(_8){
return Xinha._lc(_8,"InsertSnippet");
};
InsertSnippet.prototype.onGenerate=function(){
var _9="IS-style";
var _a=this.editor._doc.getElementById(_9);
if(_a==null){
_a=this.editor._doc.createElement("link");
_a.id=_9;
_a.rel="stylesheet";
_a.href=_editor_url+"plugins/InsertSnippet/InsertSnippet.css";
this.editor._doc.getElementsByTagName("HEAD")[0].appendChild(_a);
}
};
Xinha.Config.prototype.InsertSnippet={"snippets":_editor_url+"plugins/InsertSnippet/demosnippets.js","css":["../InsertSnippet.css"],"showInsertVariable":false,"backend_data":null};
InsertSnippet.prototype.buttonPress=function(_b){
var _c=_b.config;
_c.snippets=this.snippets;
var _d=this;
_b._popupDialog("plugin://InsertSnippet/insertsnippet",function(_e){
if(!_e){
return false;
}
_b.focusEditor();
if(_e["how"]=="variable"){
_b.insertHTML("{"+_d.snippets[_e["snippetnum"]].id+"}");
}else{
_b.insertHTML(_d.snippets[_e["snippetnum"]].HTML);
}
},_c);
};

