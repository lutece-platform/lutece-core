DoubleClick._pluginInfo={name:"DoubleClick",version:"1.0",developer:"Marijn Kampf",developer_url:"http://www.marijn.org",c_owner:"Marijn Kampf",sponsor:"smiling-faces.com",sponsor_url:"http://www.smiling-faces.com",license:"htmlArea"};
function DoubleClick(_1){
this.editor=_1;
this.editor.dblClickList={u:[function(e){
e.execCommand("underline");
}],strike:[function(e){
e.execCommand("strikethrough");
}],sub:[function(e){
e.execCommand("subscript");
}],sup:[function(e){
e.execCommand("superscript");
}],a:[function(e){
e.execCommand("createlink");
}],img:[function(e){
e.execCommand("insertimage");
}],td:[function(e){
e.execCommand("inserttable");
}]};
}
DoubleClick.prototype.onGenerate=function(){
var _9=this;
var _a=this.editordoc=this.editor._iframe.contentWindow.document;
HTMLArea._addEvents(_a,["dblclick"],function(_b){
return _9.onDoubleClick(HTMLArea.is_ie?_9.editor._iframe.contentWindow.event:_b);
});
this.currentClick=null;
};
DoubleClick.prototype.onDoubleClick=function(ev){
var _d=HTMLArea.is_ie?ev.srcElement:ev.target;
var _e=_d.tagName.toLowerCase();
if(this.editor.dblClickList[_e]!=undefined){
this.editor.dblClickList[_e][0](this.editor,_d);
}
};

