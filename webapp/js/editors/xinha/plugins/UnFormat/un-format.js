function UnFormat(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"unformat",tooltip:this._lc("Page Cleaner"),image:_1.imgURL("unformat.gif","UnFormat"),textMode:false,action:function(_4){
_3.buttonPress(_4);
}});
_2.addToolbarElement("unformat","killword",1);
}
UnFormat._pluginInfo={name:"UnFormat",version:"1.0",license:"htmlArea"};
UnFormat.prototype._lc=function(_5){
return Xinha._lc(_5,"UnFormat");
};
UnFormat.prototype.buttonPress=function(_6){
_6._popupDialog("plugin://UnFormat/unformat",function(_7){
if(_7){
if(_7["cleaning_area"]=="all"){
var _8=_6._doc.body.innerHTML;
}else{
var _8=_6.getSelectedHTML();
}
if(_7["html_all"]==true){
_8=_8.replace(/<[\!]*?[^<>]*?>/g,"");
}
if(_7["formatting"]==true){
_8=_8.replace(/style="[^"]*"/gi,"");
_8=_8.replace(/<\/?font[^>]*>/gi,"");
_8=_8.replace(/<\/?b>/gi,"");
_8=_8.replace(/<\/?strong[^>]*>/gi,"");
_8=_8.replace(/<\/?i>/gi,"");
_8=_8.replace(/<\/?em[^>]*>/gi,"");
_8=_8.replace(/<\/?u[^>]*>/gi,"");
_8=_8.replace(/<\/?strike[^>]*>/gi,"");
_8=_8.replace(/ align=[^\s|>]*/gi,"");
_8=_8.replace(/ class=[^\s|>]*/gi,"");
}
if(_7["cleaning_area"]=="all"){
_6._doc.body.innerHTML=_8;
}else{
_6.insertHTML(_8);
}
}else{
return false;
}
},null);
};

