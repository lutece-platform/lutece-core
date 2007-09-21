SimpleLinker._pluginInfo={name:"SimpleLinker",version:"1.0",developer:"James Sleeman",developer_url:"http://www.gogo.co.nz/",c_owner:"Gogo Internet Services",license:"htmlArea",sponsor:"Gogo Internet Services",sponsor_url:"http://www.gogo.co.nz/"};
HTMLArea.Config.prototype.SimpleLinker={};
function SimpleLinker(_1,_2){
this.editor=_1;
this.lConfig=_1.config.SimpleLinker;
var _3=this;
if(_1.config.btnList.createlink){
_1.config.btnList.createlink[3]=function(e,_5,_6){
_3._createLink(_3._getSelectedAnchor());
};
}else{
_1.config.registerButton("createlink","Insert/Modify Hyperlink",[_editor_url+"images/ed_buttons_main.gif",6,1],false,function(e,_8,_9){
_3._createLink(_3._getSelectedAnchor());
});
}
_1.config.addToolbarElement("createlink","createlink",0);
}
SimpleLinker.prototype._lc=function(_a){
return HTMLArea._lc(_a,"SimpleLinker");
};
SimpleLinker.prototype._createLink=function(a){
if(!a&&this.editor._selectionEmpty(this.editor._getSelection())){
alert(this._lc("You must select some text before making a new link."));
return false;
}
var _c="";
if(this.editor.config.anchorPrefix!=""){
_c=this.editor.config.anchorPrefix+window.top.document.forms[0].page_id.value;
}
var _d={type:"url",href:"",anchorprefix:_c,target:"",p_width:"",p_height:"",p_options:["menubar=no","toolbar=yes","location=no","status=no","scrollbars=yes","resizeable=yes"],to:"",subject:"",body:""};
if(a&&a.tagName.toLowerCase()=="a"){
var m=a.href.match(/^mailto:(.*@[^?&]*)(\?(.*))?$/);
var _f=a.href.match(/^#(.*)$/);
if(m){
_d.type="mailto";
_d.to=m[1];
if(m[3]){
var _10=m[3].split("&");
for(var x=0;x<_10.length;x++){
var j=_10[x].match(/(subject|body)=(.*)/);
if(j){
_d[j[1]]=decodeURIComponent(j[2]);
}
}
}
}else{
if(_f){
_d.type="anchor";
_d.anchor=m[1];
}else{
if(a.getAttribute("onclick")){
var m=a.getAttribute("onclick").match(/window\.open\(\s*this\.href\s*,\s*'([a-z0-9_]*)'\s*,\s*'([a-z0-9_=,]*)'\s*\)/i);
_d.href=a.href?a.href:"";
_d.target="popup";
_d.p_name=m[1];
_d.p_options=[];
var _10=m[2].split(",");
for(var x=0;x<_10.length;x++){
var i=_10[x].match(/(width|height)=([0-9]+)/);
if(i){
_d["p_"+i[1]]=parseInt(i[2]);
}else{
_d.p_options.push(_10[x]);
}
}
}else{
_d.href=a.href;
_d.target=a.target;
}
}
}
}
var _14=this;
this.a=a;
var _15=function(){
var a=_14.a;
var _17=_14._dialog.hide();
var atr={href:"",target:"",title:"",onclick:""};
if(_17.type=="url"){
if(_17.href){
atr.href=_17.href;
atr.target=_17.target;
if(_17.target=="popup"){
if(_17.p_width){
_17.p_options.push("width="+_17.p_width);
}
if(_17.p_height){
_17.p_options.push("height="+_17.p_height);
}
atr.onclick="try{if(document.designMode && document.designMode == 'on') return false;}catch(e){} window.open(this.href, '"+(_17.p_name.replace(/[^a-z0-9_]/i,"_"))+"', '"+_17.p_options.join(",")+"');return false;";
}
}
}else{
if(_17.type=="anchor"){
if(_17.anchor){
atr.href=_17.anchor.value;
}
}else{
if(_17.to){
atr.href="mailto:"+_17.to;
if(_17.subject){
atr.href+="?subject="+encodeURIComponent(_17.subject);
}
if(_17.body){
atr.href+=(_17.subject?"&":"?")+"body="+encodeURIComponent(_17.body);
}
}
}
}
if(a&&a.tagName.toLowerCase()=="a"){
if(!atr.href){
if(confirm(_14._dialog._lc("Are you sure you wish to remove this link?"))){
var p=a.parentNode;
while(a.hasChildNodes()){
p.insertBefore(a.removeChild(a.childNodes[0]),a);
}
p.removeChild(a);
}
}else{
for(var i in atr){
a.setAttribute(i,atr[i]);
}
if(HTMLArea.is_ie){
if(/mailto:([^?<>]*)(\?[^<]*)?$/i.test(a.innerHTML)){
a.innerHTML=RegExp.$1;
}
}
}
}else{
if(!atr.href){
return true;
}
var tmp=HTMLArea.uniq("http://www.example.com/Link");
_14.editor._doc.execCommand("createlink",false,tmp);
var _1c=_14.editor._doc.getElementsByTagName("a");
for(var i=0;i<_1c.length;i++){
var a=_1c[i];
if(a.href==tmp){
for(var i in atr){
a.setAttribute(i,atr[i]);
}
}
}
}
};
this._dialog.show(_d,_15);
};
SimpleLinker.prototype._getSelectedAnchor=function(){
var sel=this.editor._getSelection();
var rng=this.editor._createRange(sel);
var a=this.editor._activeElement(sel);
if(a!=null&&a.tagName.toLowerCase()=="a"){
return a;
}else{
a=this.editor._getFirstAncestor(sel,"a");
if(a!=null){
return a;
}
}
return null;
};
SimpleLinker.prototype.onGenerate=function(){
this._dialog=new SimpleLinker.Dialog(this);
};
SimpleLinker.Dialog=function(_20){
var _21=this;
this.Dialog_nxtid=0;
this.simplelinker=_20;
this.id={};
this.ready=false;
this.files=false;
this.html=false;
this.dialog=false;
this._prepareDialog();
};
SimpleLinker.Dialog.prototype._prepareDialog=function(){
var _22=this;
var _23=this.simplelinker;
if(this.html==false){
HTMLArea._getback(_editor_url+"plugins/SimpleLinker/dialog.html",function(txt){
_22.html=txt;
_22._prepareDialog();
});
return;
}
var _25=this.html;
var _26=this.dialog=new HTMLArea.Dialog(_23.editor,this.html,"SimpleLinker");
var _27=this.dialog.getElementById("options");
_27.style.position="relative";
_27.style.top=0+"px";
_27.style.right=0+"px";
_27.style.overflow="auto";
this.dialog.onresize=function(){
};
this.ready=true;
};
SimpleLinker.Dialog.prototype._lc=SimpleLinker.prototype._lc;
SimpleLinker.Dialog.prototype.show=function(_28,ok,_2a){
if(!this.ready){
var _2b=this;
window.setTimeout(function(){
_2b.show(_28,ok,_2a);
},100);
return;
}
if(_28.type=="url"){
this.dialog.getElementById("urltable").style.display="";
this.dialog.getElementById("mailtable").style.display="none";
this.dialog.getElementById("anchortable").style.display="none";
}else{
if(_28.type=="anchor"){
this.dialog.getElementById("urltable").style.display="none";
this.dialog.getElementById("mailtable").style.display="none";
this.dialog.getElementById("anchortable").style.display="";
}else{
this.dialog.getElementById("urltable").style.display="none";
this.dialog.getElementById("mailtable").style.display="";
this.dialog.getElementById("anchortable").style.display="none";
}
}
if(_28.target=="popup"){
this.dialog.getElementById("popuptable").style.display="";
}else{
this.dialog.getElementById("popuptable").style.display="none";
}
var _2c=this.dialog.getElementById("anchor");
for(var i=0;i<_2c.childNodes.length;i++){
_2c.removeChild(_2c.childNodes[i]);
}
var _2e=this.simplelinker.editor.getHTML();
var _2f=new Array();
var m=_2e.match(/<a[^>]+name="([^"]+)"/gi);
if(m){
for(i=0;i<m.length;i++){
var n=m[i].match(/name="([^"]+)"/i);
if(!_2f.contains(n[1])){
_2f.push(n[1]);
}
}
}
m=_2e.match(/id="([^"]+)"/gi);
if(m){
for(i=0;i<m.length;i++){
n=m[i].match(/id="([^"]+)"/i);
if(!_2f.contains(n[1])){
_2f.push(n[1]);
}
}
}
for(i=0;i<_2f.length;i++){
var opt=document.createElement("option");
opt.value=_28.anchorprefix+"#"+_2f[i];
opt.innerHTML=_2f[i];
_2c.appendChild(opt);
}
if(_2c.childNodes.length==0){
this.dialog.getElementById("anchorfieldset").style.display="none";
}
var _33=this.dialog;
var _2b=this;
if(ok){
this.dialog.getElementById("ok").onclick=ok;
}else{
this.dialog.getElementById("ok").onclick=function(){
_2b.hide();
};
}
if(_2a){
this.dialog.getElementById("cancel").onclick=_2a;
}else{
this.dialog.getElementById("cancel").onclick=function(){
_2b.hide();
};
}
this.simplelinker.editor.disableToolbar(["fullscreen","simplelinker"]);
this.dialog.show(_28);
this.dialog.onresize();
};
SimpleLinker.Dialog.prototype.hide=function(){
this.simplelinker.editor.enableToolbar();
return this.dialog.hide();
};

