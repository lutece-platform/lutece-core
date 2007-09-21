function SmartReplace(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"smartreplace",tooltip:this._lc("SmartReplace"),image:_editor_url+"plugins/SmartReplace/img/smartquotes.gif",textMode:false,action:function(e,_5,_6){
_3.buttonPress(null,_6);
}});
_2.addToolbarElement("smartreplace","htmlmode",1);
}
SmartReplace._pluginInfo={name:"SmartReplace",version:"1.0",developer:"Raimund Meyer",developer_url:"http://rheinauf.de",c_owner:"Raimund Meyer",sponsor:"",sponsor_url:"",license:"htmlArea"};
SmartReplace.prototype._lc=function(_7){
return Xinha._lc(_7,"SmartReplace");
};
Xinha.Config.prototype.SmartReplace={"defaultActive":true,"quotes":null};
SmartReplace.prototype.toggleActivity=function(_8){
if(typeof _8!="undefined"){
this.active=_8;
}else{
this.active=this.active?false:true;
}
this.editor._toolbarObjects.smartreplace.state("active",this.active);
};
SmartReplace.prototype.onUpdateToolbar=function(){
this.editor._toolbarObjects.smartreplace.state("active",this.active);
};
SmartReplace.prototype.onGenerate=function(){
this.active=this.editor.config.SmartReplace.defaultActive;
this.editor._toolbarObjects.smartreplace.state("active",this.active);
var _9=this;
Xinha._addEvents(_9.editor._doc,["keypress"],function(_a){
return _9.keyEvent(Xinha.is_ie?_9.editor._iframe.contentWindow.event:_a);
});
var _b=this.editor.config.SmartReplace.quotes;
if(_b&&typeof _b=="object"){
this.openingQuotes=_b[0];
this.closingQuotes=_b[1];
this.openingQuote=_b[2];
this.closingQuote=_b[3];
}else{
this.openingQuotes=this._lc("OpeningDoubleQuotes");
this.closingQuote=this._lc("ClosingSingleQuote");
this.closingQuotes=this._lc("ClosingDoubleQuotes");
this.openingQuote=this._lc("OpeningSingleQuote");
}
if(this.openingQuotes=="OpeningDoubleQuotes"){
this.openingQuotes=String.fromCharCode(8220);
this.closingQuotes=String.fromCharCode(8221);
this.openingQuote=String.fromCharCode(8216);
this.closingQuote=String.fromCharCode(8217);
}
};
SmartReplace.prototype.keyEvent=function(ev){
if(!this.active){
return true;
}
var _d=this.editor;
var _e=Xinha.is_ie?ev.keyCode:ev.charCode;
var _f=String.fromCharCode(_e);
if(_e==32){
return this.smartDash();
}
if(_f=="\""||_f=="'"){
Xinha._stopEvent(ev);
return this.smartQuotes(_f);
}
return true;
};
SmartReplace.prototype.smartQuotes=function(_10){
if(_10=="'"){
var _11=this.openingQuote;
var _12=this.closingQuote;
}else{
var _11=this.openingQuotes;
var _12=this.closingQuotes;
}
var _13=this.editor;
var sel=_13.getSelection();
if(Xinha.is_ie){
var r=_13.createRange(sel);
if(r.text!==""){
r.text="";
}
r.moveStart("character",-1);
if(r.text.match(/\S/)){
r.moveStart("character",+1);
r.text=_12;
}else{
r.moveStart("character",+1);
r.text=_11;
}
}else{
if(!sel.isCollapsed){
_13.insertNodeAtSelection(document.createTextNode(""));
}
if(sel.anchorOffset>0){
sel.extend(sel.anchorNode,sel.anchorOffset-1);
}
if(sel.toString().match(/\S/)){
sel.collapse(sel.anchorNode,sel.anchorOffset);
_13.insertNodeAtSelection(document.createTextNode(_12));
}else{
sel.collapse(sel.anchorNode,sel.anchorOffset);
_13.insertNodeAtSelection(document.createTextNode(_11));
}
}
};
SmartReplace.prototype.smartDash=function(){
var _16=this.editor;
var sel=this.editor.getSelection();
if(Xinha.is_ie){
var r=this.editor.createRange(sel);
r.moveStart("character",-2);
if(r.text.match(/\s-/)){
r.text=" "+String.fromCharCode(8211);
}
}else{
sel.extend(sel.anchorNode,sel.anchorOffset-2);
if(sel.toString().match(/^-/)){
this.editor.insertNodeAtSelection(document.createTextNode(" "+String.fromCharCode(8211)));
}
sel.collapse(sel.anchorNode,sel.anchorOffset);
}
};
SmartReplace.prototype.replaceAll=function(){
var _19=["&quot;",String.fromCharCode(8220),String.fromCharCode(8221),String.fromCharCode(8222),String.fromCharCode(187),String.fromCharCode(171)];
var _1a=["'",String.fromCharCode(8216),String.fromCharCode(8217),String.fromCharCode(8218),String.fromCharCode(8250),String.fromCharCode(8249)];
var _1b=this.editor.getHTML();
var _1c=new RegExp("(\\s|^|>)("+_19.join("|")+")(\\S)","g");
_1b=_1b.replace(_1c,"$1"+this.openingQuotes+"$3");
var _1d=new RegExp("(\\s|^|>)("+_1a.join("|")+")(\\S)","g");
_1b=_1b.replace(_1d,"$1"+this.openingQuote+"$3");
var _1e=new RegExp("(\\S)("+_19.join("|")+")","g");
_1b=_1b.replace(_1e,"$1"+this.closingQuotes);
var _1f=new RegExp("(\\S)("+_1a.join("|")+")","g");
_1b=_1b.replace(_1f,"$1"+this.closingQuote);
var _20=new RegExp("( |&nbsp;)(-)( |&nbsp;)","g");
_1b=_1b.replace(_20," "+String.fromCharCode(8211)+" ");
this.editor.setHTML(_1b);
};
SmartReplace.prototype.dialog=function(){
var _21=this;
var _22=function(_23){
_21.toggleActivity(_23.enable);
if(_23.convert){
_21.replaceAll();
}
};
var _24=this;
Dialog(_editor_url+"plugins/SmartReplace/popups/dialog.html",_22,_24);
};
SmartReplace.prototype.buttonPress=function(_25,obj){
var _27=this;
if(this._dialog.dialog.rootElem.style.display!="none"){
return this._dialog.hide();
}
var _28=function(){
var _29=_27._dialog.hide();
_27.toggleActivity((_29.enable)?true:false);
if(_29.convert){
_27.replaceAll();
_27._dialog.dialog.getElementById("convert").checked=false;
}
};
var _2a={enable:_27.active?"on":"",convert:""};
this._dialog.show(_2a,_28);
};
SmartReplace.prototype.onGenerateOnce=function(){
if(!this._dialog){
this._dialog=new SmartReplace.Dialog(this);
}
};
SmartReplace.Dialog=function(_2b){
this.Dialog_nxtid=0;
this.mainPluginObject=_2b;
this.id={};
this.ready=false;
this.files=false;
this.html=false;
this.dialog=false;
this._prepareDialog();
};
SmartReplace.Dialog.prototype._prepareDialog=function(){
var _2c=this;
var _2d=this.mainPluginObject.editor;
if(this.html==false){
Xinha._getback(_editor_url+"plugins/SmartReplace/dialog.html",function(_2e){
_2c.html=_2e;
_2c._prepareDialog();
});
return;
}
this.dialog=new Xinha.Dialog(_2d,this.html,"SmartReplace");
this.ready=true;
};
SmartReplace.Dialog.prototype._lc=SmartReplace.prototype._lc;
SmartReplace.Dialog.prototype.show=function(_2f,ok,_31){
if(!this.ready){
var _32=this;
window.setTimeout(function(){
_32.show(_2f,ok,_31);
},100);
return;
}
var _33=this.dialog;
var _32=this;
if(ok){
this.dialog.getElementById("ok").onclick=ok;
}else{
this.dialog.getElementById("ok").onclick=function(){
_32.hide();
};
}
if(_31){
this.dialog.getElementById("cancel").onclick=_31;
}else{
this.dialog.getElementById("cancel").onclick=function(){
_32.hide();
};
}
this.mainPluginObject.editor.disableToolbar(["fullscreen","smartreplace"]);
this.dialog.show(_2f);
this.dialog.onresize();
};
SmartReplace.Dialog.prototype.hide=function(){
this.mainPluginObject.editor.enableToolbar();
return this.dialog.hide();
};

