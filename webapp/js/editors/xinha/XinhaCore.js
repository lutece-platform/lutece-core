Xinha.version={"Release":"Trunk","Head":"$HeadURL: http://svn.xinha.python-hosting.com/trunk/XinhaCore.js $".replace(/^[^:]*: (.*) \$$/,"$1"),"Date":"$LastChangedDate: 2007-06-26 07:34:27 +1200 (Tue, 26 Jun 2007) $".replace(/^[^:]*: ([0-9-]*) ([0-9:]*) ([+0-9]*) \((.*)\) \$/,"$4 $2 $3"),"Revision":"$LastChangedRevision: 860 $".replace(/^[^:]*: (.*) \$$/,"$1"),"RevisionBy":"$LastChangedBy: ray $".replace(/^[^:]*: (.*) \$$/,"$1")};
Xinha._resolveRelativeUrl=function(_1,_2){
if(_2.match(/^([^:]+\:)?\//)){
return _2;
}else{
var b=_1.split("/");
if(b[b.length-1]==""){
b.pop();
}
var p=_2.split("/");
if(p[0]=="."){
p.shift();
}
while(p[0]==".."){
b.pop();
p.shift();
}
return b.join("/")+"/"+p.join("/");
}
};
if(typeof _editor_url=="string"){
_editor_url=_editor_url.replace(/\x2f*$/,"/");
if(!_editor_url.match(/^([^:]+\:)?\//)){
var path=window.location.toString().split("/");
path.pop();
_editor_url=Xinha._resolveRelativeUrl(path.join("/"),_editor_url);
}
}else{
alert("WARNING: _editor_url is not set!  You should set this variable to the editor files path; it should preferably be an absolute path, like in '/htmlarea/', but it can be relative if you prefer.  Further we will try to load the editor files correctly but we'll probably fail.");
_editor_url="";
}
if(typeof _editor_lang=="string"){
_editor_lang=_editor_lang.toLowerCase();
}else{
_editor_lang="en";
}
if(typeof _editor_skin!=="string"){
_editor_skin="";
}
var __xinhas=[];
Xinha.agt=navigator.userAgent.toLowerCase();
Xinha.is_ie=((Xinha.agt.indexOf("msie")!=-1)&&(Xinha.agt.indexOf("opera")==-1));
Xinha.ie_version=parseFloat(Xinha.agt.substring(Xinha.agt.indexOf("msie")+5));
Xinha.is_opera=(Xinha.agt.indexOf("opera")!=-1);
Xinha.opera_version=navigator.appVersion.substring(0,navigator.appVersion.indexOf(" "))*1;
Xinha.is_khtml=(Xinha.agt.indexOf("khtml")!=-1);
Xinha.is_safari=(Xinha.agt.indexOf("safari")!=-1);
Xinha.is_mac=(Xinha.agt.indexOf("mac")!=-1);
Xinha.is_mac_ie=(Xinha.is_ie&&Xinha.is_mac);
Xinha.is_win_ie=(Xinha.is_ie&&!Xinha.is_mac);
Xinha.is_gecko=(navigator.product=="Gecko"&&!Xinha.is_safari);
Xinha.isRunLocally=document.URL.toLowerCase().search(/^file:/)!=-1;
Xinha.is_designMode=(typeof document.designMode!="undefined"&&!Xinha.is_ie);
Xinha.checkSupportedBrowser=function(){
if(Xinha.is_gecko){
if(navigator.productSub<20021201){
alert("You need at least Mozilla-1.3 Alpha.\nSorry, your Gecko is not supported.");
return false;
}
if(navigator.productSub<20030210){
alert("Mozilla < 1.3 Beta is not supported!\nI'll try, though, but it might not work.");
}
}
if(Xinha.is_opera){
alert("Sorry, Opera is not yet supported by Xinha.");
}
return Xinha.is_gecko||(Xinha.is_opera&&Xinha.opera_version>=9.1)||Xinha.ie_version>=5.5;
};
Xinha.isSupportedBrowser=Xinha.checkSupportedBrowser();
if(Xinha.isRunLocally&&Xinha.isSupportedBrowser){
alert("Xinha *must* be installed on a web server. Locally opened files (those that use the \"file://\" protocol) cannot properly function. Xinha will try to initialize but may not be correctly loaded.");
}
function Xinha(_5,_6){
if(!Xinha.isSupportedBrowser){
return;
}
if(!_5){
throw ("Tried to create Xinha without textarea specified.");
}
if(typeof _6=="undefined"){
this.config=new Xinha.Config();
}else{
this.config=_6;
}
if(typeof _5!="object"){
_5=Xinha.getElementById("textarea",_5);
}
this._textArea=_5;
this._textArea.spellcheck=false;
Xinha.freeLater(this,"_textArea");
this._initial_ta_size={w:_5.style.width?_5.style.width:(_5.offsetWidth?(_5.offsetWidth+"px"):(_5.cols+"em")),h:_5.style.height?_5.style.height:(_5.offsetHeight?(_5.offsetHeight+"px"):(_5.rows+"em"))};
if(document.getElementById("loading_"+_5.id)||this.config.showLoading){
if(!document.getElementById("loading_"+_5.id)){
Xinha.createLoadingMessage(_5);
}
this.setLoadingMessage(Xinha._lc("Constructing object"));
}
this._editMode="wysiwyg";
this.plugins={};
this._timerToolbar=null;
this._timerUndo=null;
this._undoQueue=[this.config.undoSteps];
this._undoPos=-1;
this._customUndo=true;
this._mdoc=document;
this.doctype="";
this.__htmlarea_id_num=__xinhas.length;
__xinhas[this.__htmlarea_id_num]=this;
this._notifyListeners={};
var _7={right:{on:true,container:document.createElement("td"),panels:[]},left:{on:true,container:document.createElement("td"),panels:[]},top:{on:true,container:document.createElement("td"),panels:[]},bottom:{on:true,container:document.createElement("td"),panels:[]}};
for(var i in _7){
if(!_7[i].container){
continue;
}
_7[i].div=_7[i].container;
_7[i].container.className="panels "+i;
Xinha.freeLater(_7[i],"container");
Xinha.freeLater(_7[i],"div");
}
this._panels=_7;
this._statusBar=null;
this._statusBarTree=null;
this._statusBarTextMode=null;
this._statusBarItems=[];
this._framework={};
this._htmlArea=null;
this._iframe=null;
this._doc=null;
this._toolBar=this._toolbar=null;
this._toolbarObjects={};
}
Xinha.onload=function(){
};
Xinha.init=function(){
Xinha.onload();
};
Xinha.RE_tagName=/(<\/|<)\s*([^ \t\n>]+)/ig;
Xinha.RE_doctype=/(<!doctype((.|\n)*?)>)\n?/i;
Xinha.RE_head=/<head>((.|\n)*?)<\/head>/i;
Xinha.RE_body=/<body[^>]*>((.|\n|\r|\t)*?)<\/body>/i;
Xinha.RE_Specials=/([\/\^$*+?.()|{}[\]])/g;
Xinha.escapeStringForRegExp=function(_9){
return _9.replace(Xinha.RE_Specials,"\\$1");
};
Xinha.RE_email=/[_a-z\d\-\.]{3,}@[_a-z\d\-]{2,}(\.[_a-z\d\-]{2,})+/i;
Xinha.RE_url=/(https?:\/\/)?(([a-z0-9_]+:[a-z0-9_]+@)?[a-z0-9_-]{2,}(\.[a-z0-9_-]{2,}){2,}(:[0-9]+)?(\/\S+)*)/i;
Xinha.Config=function(){
var _a=this;
this.version=Xinha.version.Revision;
this.width="auto";
this.height="auto";
this.sizeIncludesBars=true;
this.sizeIncludesPanels=true;
this.panel_dimensions={left:"200px",right:"200px",top:"100px",bottom:"100px"};
this.iframeWidth=null;
this.statusBar=true;
this.htmlareaPaste=false;
this.mozParaHandler="best";
this.getHtmlMethod="DOMwalk";
this.undoSteps=20;
this.undoTimeout=500;
this.changeJustifyWithDirection=false;
this.fullPage=false;
this.pageStyle="";
this.pageStyleSheets=[];
this.baseHref=null;
this.expandRelativeUrl=true;
this.stripBaseHref=true;
this.stripSelfNamedAnchors=true;
this.only7BitPrintablesInURLs=true;
this.sevenBitClean=false;
this.specialReplacements={};
this.killWordOnPaste=true;
this.makeLinkShowsTarget=true;
this.charSet=(typeof document.characterSet!="undefined")?document.characterSet:document.charset;
this.browserQuirksMode=null;
this.imgURL="images/";
this.popupURL="popups/";
this.htmlRemoveTags=null;
this.flowToolbars=true;
this.toolbarAlign="left";
this.showLoading=false;
this.stripScripts=true;
this.convertUrlsToLinks=true;
this.colorPickerCellSize="6px";
this.colorPickerGranularity=18;
this.colorPickerPosition="bottom,right";
this.colorPickerWebSafe=false;
this.colorPickerSaveColors=20;
this.fullScreen=false;
this.fullScreenMargins=[0,0,0,0];
this.toolbar=[["popupeditor"],["separator","formatblock","fontname","fontsize","bold","italic","underline","strikethrough"],["separator","forecolor","hilitecolor","textindicator"],["separator","subscript","superscript"],["linebreak","separator","justifyleft","justifycenter","justifyright","justifyfull"],["separator","insertorderedlist","insertunorderedlist","outdent","indent"],["separator","inserthorizontalrule","createlink","insertimage","inserttable"],["linebreak","separator","undo","redo","selectall","print"],(Xinha.is_gecko?[]:["cut","copy","paste","overwrite","saveas"]),["separator","killword","clearfonts","removeformat","toggleborders","splitblock","lefttoright","righttoleft"],["separator","htmlmode","showhelp","about"]];
this.fontname={"&mdash; font &mdash;":"","Arial":"arial,helvetica,sans-serif","Courier New":"courier new,courier,monospace","Georgia":"georgia,times new roman,times,serif","Tahoma":"tahoma,arial,helvetica,sans-serif","Times New Roman":"times new roman,times,serif","Verdana":"verdana,arial,helvetica,sans-serif","impact":"impact","WingDings":"wingdings"};
this.fontsize={"&mdash; size &mdash;":"","1 (8 pt)":"1","2 (10 pt)":"2","3 (12 pt)":"3","4 (14 pt)":"4","5 (18 pt)":"5","6 (24 pt)":"6","7 (36 pt)":"7"};
this.formatblock={"&mdash; format &mdash;":"","Heading 1":"h1","Heading 2":"h2","Heading 3":"h3","Heading 4":"h4","Heading 5":"h5","Heading 6":"h6","Normal":"p","Address":"address","Formatted":"pre"};
this.customSelects={};
this.debug=true;
this.URIs={"blank":"popups/blank.html","link":_editor_url+"modules/CreateLink/link.html","insert_image":_editor_url+"modules/InsertImage/insert_image.html","insert_table":_editor_url+"modules/InsertTable/insert_table.html","select_color":"select_color.html","about":"about.html","help":"editor_help.html"};
this.btnList={bold:["Bold",Xinha._lc({key:"button_bold",string:["ed_buttons_main.gif",3,2]},"Xinha"),false,function(e){
e.execCommand("bold");
}],italic:["Italic",Xinha._lc({key:"button_italic",string:["ed_buttons_main.gif",2,2]},"Xinha"),false,function(e){
e.execCommand("italic");
}],underline:["Underline",Xinha._lc({key:"button_underline",string:["ed_buttons_main.gif",2,0]},"Xinha"),false,function(e){
e.execCommand("underline");
}],strikethrough:["Strikethrough",Xinha._lc({key:"button_strikethrough",string:["ed_buttons_main.gif",3,0]},"Xinha"),false,function(e){
e.execCommand("strikethrough");
}],subscript:["Subscript",Xinha._lc({key:"button_subscript",string:["ed_buttons_main.gif",3,1]},"Xinha"),false,function(e){
e.execCommand("subscript");
}],superscript:["Superscript",Xinha._lc({key:"button_superscript",string:["ed_buttons_main.gif",2,1]},"Xinha"),false,function(e){
e.execCommand("superscript");
}],justifyleft:["Justify Left",["ed_buttons_main.gif",0,0],false,function(e){
e.execCommand("justifyleft");
}],justifycenter:["Justify Center",["ed_buttons_main.gif",1,1],false,function(e){
e.execCommand("justifycenter");
}],justifyright:["Justify Right",["ed_buttons_main.gif",1,0],false,function(e){
e.execCommand("justifyright");
}],justifyfull:["Justify Full",["ed_buttons_main.gif",0,1],false,function(e){
e.execCommand("justifyfull");
}],orderedlist:["Ordered List",["ed_buttons_main.gif",0,3],false,function(e){
e.execCommand("insertorderedlist");
}],unorderedlist:["Bulleted List",["ed_buttons_main.gif",1,3],false,function(e){
e.execCommand("insertunorderedlist");
}],insertorderedlist:["Ordered List",["ed_buttons_main.gif",0,3],false,function(e){
e.execCommand("insertorderedlist");
}],insertunorderedlist:["Bulleted List",["ed_buttons_main.gif",1,3],false,function(e){
e.execCommand("insertunorderedlist");
}],outdent:["Decrease Indent",["ed_buttons_main.gif",1,2],false,function(e){
e.execCommand("outdent");
}],indent:["Increase Indent",["ed_buttons_main.gif",0,2],false,function(e){
e.execCommand("indent");
}],forecolor:["Font Color",["ed_buttons_main.gif",3,3],false,function(e){
e.execCommand("forecolor");
}],hilitecolor:["Background Color",["ed_buttons_main.gif",2,3],false,function(e){
e.execCommand("hilitecolor");
}],undo:["Undoes your last action",["ed_buttons_main.gif",4,2],false,function(e){
e.execCommand("undo");
}],redo:["Redoes your last action",["ed_buttons_main.gif",5,2],false,function(e){
e.execCommand("redo");
}],cut:["Cut selection",["ed_buttons_main.gif",5,0],false,function(e,cmd){
e.execCommand(cmd);
}],copy:["Copy selection",["ed_buttons_main.gif",4,0],false,function(e,cmd){
e.execCommand(cmd);
}],paste:["Paste from clipboard",["ed_buttons_main.gif",4,1],false,function(e,cmd){
e.execCommand(cmd);
}],selectall:["Select all","ed_selectall.gif",false,function(e){
e.execCommand("selectall");
}],inserthorizontalrule:["Horizontal Rule",["ed_buttons_main.gif",6,0],false,function(e){
e.execCommand("inserthorizontalrule");
}],createlink:["Insert Web Link",["ed_buttons_main.gif",6,1],false,function(e){
e._createLink();
}],insertimage:["Insert/Modify Image",["ed_buttons_main.gif",6,3],false,function(e){
e.execCommand("insertimage");
}],inserttable:["Insert Table",["ed_buttons_main.gif",6,2],false,function(e){
e.execCommand("inserttable");
}],htmlmode:["Toggle HTML Source",["ed_buttons_main.gif",7,0],true,function(e){
e.execCommand("htmlmode");
}],toggleborders:["Toggle Borders",["ed_buttons_main.gif",7,2],false,function(e){
e._toggleBorders();
}],print:["Print document",["ed_buttons_main.gif",8,1],false,function(e){
if(Xinha.is_gecko){
e._iframe.contentWindow.print();
}else{
e.focusEditor();
print();
}
}],saveas:["Save as","ed_saveas.gif",false,function(e){
e.execCommand("saveas",false,"noname.htm");
}],about:["About this editor",["ed_buttons_main.gif",8,2],true,function(e){
e.execCommand("about");
}],showhelp:["Help using editor",["ed_buttons_main.gif",9,2],true,function(e){
e.execCommand("showhelp");
}],splitblock:["Split Block","ed_splitblock.gif",false,function(e){
e._splitBlock();
}],lefttoright:["Direction left to right",["ed_buttons_main.gif",0,4],false,function(e){
e.execCommand("lefttoright");
}],righttoleft:["Direction right to left",["ed_buttons_main.gif",1,4],false,function(e){
e.execCommand("righttoleft");
}],overwrite:["Insert/Overwrite","ed_overwrite.gif",false,function(e){
e.execCommand("overwrite");
}],wordclean:["MS Word Cleaner",["ed_buttons_main.gif",5,3],false,function(e){
e._wordClean();
}],clearfonts:["Clear Inline Font Specifications",["ed_buttons_main.gif",5,4],true,function(e){
e._clearFonts();
}],removeformat:["Remove formatting",["ed_buttons_main.gif",4,4],false,function(e){
e.execCommand("removeformat");
}],killword:["Clear MSOffice tags",["ed_buttons_main.gif",4,3],false,function(e){
e.execCommand("killword");
}]};
for(var i in this.btnList){
var btn=this.btnList[i];
if(typeof btn!="object"){
continue;
}
if(typeof btn[1]!="string"){
btn[1][0]=_editor_url+this.imgURL+btn[1][0];
}else{
btn[1]=_editor_url+this.imgURL+btn[1];
}
btn[0]=Xinha._lc(btn[0]);
}
};
Xinha.Config.prototype.registerButton=function(id,_3b,_3c,_3d,_3e,_3f){
var _40;
if(typeof id=="string"){
_40=id;
}else{
if(typeof id=="object"){
_40=id.id;
}else{
alert("ERROR [Xinha.Config::registerButton]:\ninvalid arguments");
return false;
}
}
switch(typeof id){
case "string":
this.btnList[id]=[_3b,_3c,_3d,_3e,_3f];
break;
case "object":
this.btnList[id.id]=[id.tooltip,id.image,id.textMode,id.action,id.context];
break;
}
};
Xinha.prototype.registerPanel=function(_41,_42){
if(!_41){
_41="right";
}
this.setLoadingMessage("Register "+_41+" panel ");
var _43=this.addPanel(_41);
if(_42){
_42.drawPanelIn(_43);
}
};
Xinha.Config.prototype.registerDropdown=function(_44){
this.customSelects[_44.id]=_44;
};
Xinha.Config.prototype.hideSomeButtons=function(_45){
var _46=this.toolbar;
for(var i=_46.length;--i>=0;){
var _48=_46[i];
for(var j=_48.length;--j>=0;){
if(_45.indexOf(" "+_48[j]+" ")>=0){
var len=1;
if(/separator|space/.test(_48[j+1])){
len=2;
}
_48.splice(j,len);
}
}
}
};
Xinha.Config.prototype.addToolbarElement=function(id,_4c,_4d){
var _4e=this.toolbar;
var a,i,j,o,sid;
var _50=false;
var _51=false;
var _52=0;
var _53=0;
var _54=0;
var _55=false;
var _56=false;
if((id&&typeof id=="object")&&(id.constructor==Array)){
_50=true;
}
if((_4c&&typeof _4c=="object")&&(_4c.constructor==Array)){
_51=true;
_52=_4c.length;
}
if(_50){
for(i=0;i<id.length;++i){
if((id[i]!="separator")&&(id[i].indexOf("T[")!==0)){
sid=id[i];
}
}
}else{
sid=id;
}
for(i=0;i<_4e.length;++i){
a=_4e[i];
for(j=0;j<a.length;++j){
if(a[j]==sid){
return;
}
}
}
for(i=0;!_56&&i<_4e.length;++i){
a=_4e[i];
for(j=0;!_56&&j<a.length;++j){
if(_51){
for(o=0;o<_52;++o){
if(a[j]==_4c[o]){
if(o===0){
_56=true;
j--;
break;
}else{
_54=i;
_53=j;
_52=o;
}
}
}
}else{
if(a[j]==_4c){
_56=true;
break;
}
}
}
}
if(!_56&&_51){
if(_4c.length!=_52){
j=_53;
a=_4e[_54];
_56=true;
}
}
if(_56){
if(_4d===0){
if(_50){
a[j]=id[id.length-1];
for(i=id.length-1;--i>=0;){
a.splice(j,0,id[i]);
}
}else{
a[j]=id;
}
}else{
if(_4d<0){
j=j+_4d+1;
}else{
if(_4d>0){
j=j+_4d;
}
}
if(_50){
for(i=id.length;--i>=0;){
a.splice(j,0,id[i]);
}
}else{
a.splice(j,0,id);
}
}
}else{
_4e[0].splice(0,0,"separator");
if(_50){
for(i=id.length;--i>=0;){
_4e[0].splice(0,0,id[i]);
}
}else{
_4e[0].splice(0,0,id);
}
}
};
Xinha.Config.prototype.removeToolbarElement=Xinha.Config.prototype.hideSomeButtons;
Xinha.replaceAll=function(_57){
var tas=document.getElementsByTagName("textarea");
for(var i=tas.length;i>0;(new Xinha(tas[--i],_57)).generate()){
}
};
Xinha.replace=function(id,_5b){
var ta=Xinha.getElementById("textarea",id);
return ta?(new Xinha(ta,_5b)).generate():null;
};
Xinha.prototype._createToolbar=function(){
this.setLoadingMessage(Xinha._lc("Create Toolbar"));
var _5d=this;
var _5e=document.createElement("div");
this._toolBar=this._toolbar=_5e;
_5e.className="toolbar";
_5e.unselectable="1";
Xinha.freeLater(this,"_toolBar");
Xinha.freeLater(this,"_toolbar");
var _5f=null;
var _60={};
this._toolbarObjects=_60;
this._createToolbar1(_5d,_5e,_60);
this._htmlArea.appendChild(_5e);
return _5e;
};
Xinha.prototype._setConfig=function(_61){
this.config=_61;
};
Xinha.prototype._addToolbar=function(){
this._createToolbar1(this,this._toolbar,this._toolbarObjects);
};
Xinha._createToolbarBreakingElement=function(){
var brk=document.createElement("div");
brk.style.height="1px";
brk.style.width="1px";
brk.style.lineHeight="1px";
brk.style.fontSize="1px";
brk.style.clear="both";
return brk;
};
Xinha.prototype._createToolbar1=function(_63,_64,_65){
var _66;
if(_63.config.flowToolbars){
_64.appendChild(Xinha._createToolbarBreakingElement());
}
function newLine(){
if(typeof _66!="undefined"&&_66.childNodes.length===0){
return;
}
var _67=document.createElement("table");
_67.border="0px";
_67.cellSpacing="0px";
_67.cellPadding="0px";
if(_63.config.flowToolbars){
if(Xinha.is_ie){
_67.style.styleFloat="left";
}else{
_67.style.cssFloat="left";
}
}
_64.appendChild(_67);
var _68=document.createElement("tbody");
_67.appendChild(_68);
_66=document.createElement("tr");
_68.appendChild(_66);
_67.className="toolbarRow";
}
newLine();
function setButtonStatus(id,_6a){
var _6b=this[id];
var el=this.element;
if(_6b!=_6a){
switch(id){
case "enabled":
if(_6a){
Xinha._removeClass(el,"buttonDisabled");
el.disabled=false;
}else{
Xinha._addClass(el,"buttonDisabled");
el.disabled=true;
}
break;
case "active":
if(_6a){
Xinha._addClass(el,"buttonPressed");
}else{
Xinha._removeClass(el,"buttonPressed");
}
break;
}
this[id]=_6a;
}
}
function createSelect(txt){
var _6e=null;
var el=null;
var cmd=null;
var _71=_63.config.customSelects;
var _72=null;
var _73="";
switch(txt){
case "fontsize":
case "fontname":
case "formatblock":
_6e=_63.config[txt];
cmd=txt;
break;
default:
cmd=txt;
var _74=_71[cmd];
if(typeof _74!="undefined"){
_6e=_74.options;
_72=_74.context;
if(typeof _74.tooltip!="undefined"){
_73=_74.tooltip;
}
}else{
alert("ERROR [createSelect]:\nCan't find the requested dropdown definition");
}
break;
}
if(_6e){
el=document.createElement("select");
el.title=_73;
var obj={name:txt,element:el,enabled:true,text:false,cmd:cmd,state:setButtonStatus,context:_72};
Xinha.freeLater(obj);
_65[txt]=obj;
for(var i in _6e){
if(typeof (_6e[i])!="string"){
continue;
}
var op=document.createElement("option");
op.innerHTML=Xinha._lc(i);
op.value=_6e[i];
el.appendChild(op);
}
Xinha._addEvent(el,"change",function(){
_63._comboSelected(el,txt);
});
}
return el;
}
function createButton(txt){
var el,btn,obj=null;
switch(txt){
case "separator":
if(_63.config.flowToolbars){
newLine();
}
el=document.createElement("div");
el.className="separator";
break;
case "space":
el=document.createElement("div");
el.className="space";
break;
case "linebreak":
newLine();
return false;
case "textindicator":
el=document.createElement("div");
el.appendChild(document.createTextNode("A"));
el.className="indicator";
el.title=Xinha._lc("Current style");
obj={name:txt,element:el,enabled:true,active:false,text:false,cmd:"textindicator",state:setButtonStatus};
Xinha.freeLater(obj);
_65[txt]=obj;
break;
default:
btn=_63.config.btnList[txt];
}
if(!el&&btn){
el=document.createElement("a");
el.style.display="block";
el.href="javascript:void(0)";
el.style.textDecoration="none";
el.title=btn[0];
el.className="button";
el.style.direction="ltr";
obj={name:txt,element:el,enabled:true,active:false,text:btn[2],cmd:btn[3],state:setButtonStatus,context:btn[4]||null};
Xinha.freeLater(el);
Xinha.freeLater(obj);
_65[txt]=obj;
el.ondrag=function(){
return false;
};
Xinha._addEvent(el,"mouseout",function(ev){
if(obj.enabled){
Xinha._removeClass(el,"buttonActive");
if(obj.active){
Xinha._addClass(el,"buttonPressed");
}
}
});
Xinha._addEvent(el,"mousedown",function(ev){
if(obj.enabled){
Xinha._addClass(el,"buttonActive");
Xinha._removeClass(el,"buttonPressed");
Xinha._stopEvent(Xinha.is_ie?window.event:ev);
}
});
Xinha._addEvent(el,"click",function(ev){
ev=Xinha.is_ie?window.event:ev;
_63.btnClickEvent=ev;
if(obj.enabled){
Xinha._removeClass(el,"buttonActive");
if(Xinha.is_gecko){
_63.activateEditor();
}
obj.cmd(_63,obj.name,obj);
Xinha._stopEvent(ev);
}
});
var _7d=Xinha.makeBtnImg(btn[1]);
var img=_7d.firstChild;
Xinha.freeLater(_7d);
Xinha.freeLater(img);
el.appendChild(_7d);
obj.imgel=img;
obj.swapImage=function(_7f){
if(typeof _7f!="string"){
img.src=_7f[0];
img.style.position="relative";
img.style.top=_7f[2]?("-"+(18*(_7f[2]+1))+"px"):"-18px";
img.style.left=_7f[1]?("-"+(18*(_7f[1]+1))+"px"):"-18px";
}else{
obj.imgel.src=_7f;
img.style.top="0px";
img.style.left="0px";
}
};
}else{
if(!el){
el=createSelect(txt);
}
}
return el;
}
var _80=true;
for(var i=0;i<this.config.toolbar.length;++i){
if(!_80){
}else{
_80=false;
}
if(this.config.toolbar[i]===null){
this.config.toolbar[i]=["separator"];
}
var _82=this.config.toolbar[i];
for(var j=0;j<_82.length;++j){
var _84=_82[j];
var _85;
if(/^([IT])\[(.*?)\]/.test(_84)){
var _86=RegExp.$1=="I";
var _87=RegExp.$2;
if(_86){
_87=Xinha._lc(_87);
}
_85=document.createElement("td");
_66.appendChild(_85);
_85.className="label";
_85.innerHTML=_87;
}else{
if(typeof _84!="function"){
var _88=createButton(_84);
if(_88){
_85=document.createElement("td");
_85.className="toolbarElement";
_66.appendChild(_85);
_85.appendChild(_88);
}else{
if(_88===null){
alert("FIXME: Unknown toolbar item: "+_84);
}
}
}
}
}
}
if(_63.config.flowToolbars){
_64.appendChild(Xinha._createToolbarBreakingElement());
}
return _64;
};
var use_clone_img=false;
Xinha.makeBtnImg=function(_89,doc){
if(!doc){
doc=document;
}
if(!doc._xinhaImgCache){
doc._xinhaImgCache={};
Xinha.freeLater(doc._xinhaImgCache);
}
var _8b=null;
if(Xinha.is_ie&&((!doc.compatMode)||(doc.compatMode&&doc.compatMode=="BackCompat"))){
_8b=doc.createElement("span");
}else{
_8b=doc.createElement("div");
_8b.style.position="relative";
}
_8b.style.overflow="hidden";
_8b.style.width="18px";
_8b.style.height="18px";
_8b.className="buttonImageContainer";
var img=null;
if(typeof _89=="string"){
if(doc._xinhaImgCache[_89]){
img=doc._xinhaImgCache[_89].cloneNode();
}else{
img=doc.createElement("img");
img.src=_89;
img.style.width="18px";
img.style.height="18px";
if(use_clone_img){
doc._xinhaImgCache[_89]=img.cloneNode();
}
}
}else{
if(doc._xinhaImgCache[_89[0]]){
img=doc._xinhaImgCache[_89[0]].cloneNode();
}else{
img=doc.createElement("img");
img.src=_89[0];
img.style.position="relative";
if(use_clone_img){
doc._xinhaImgCache[_89[0]]=img.cloneNode();
}
}
img.style.top=_89[2]?("-"+(18*(_89[2]+1))+"px"):"-18px";
img.style.left=_89[1]?("-"+(18*(_89[1]+1))+"px"):"-18px";
}
_8b.appendChild(img);
return _8b;
};
Xinha.prototype._createStatusBar=function(){
this.setLoadingMessage(Xinha._lc("Create Statusbar"));
var _8d=document.createElement("div");
_8d.className="statusBar";
this._statusBar=_8d;
Xinha.freeLater(this,"_statusBar");
var div=document.createElement("span");
div.className="statusBarTree";
div.innerHTML=Xinha._lc("Path")+": ";
this._statusBarTree=div;
Xinha.freeLater(this,"_statusBarTree");
this._statusBar.appendChild(div);
div=document.createElement("span");
div.innerHTML=Xinha._lc("You are in TEXT MODE.  Use the [<>] button to switch back to WYSIWYG.");
div.style.display="none";
this._statusBarTextMode=div;
Xinha.freeLater(this,"_statusBarTextMode");
this._statusBar.appendChild(div);
if(!this.config.statusBar){
_8d.style.display="none";
}
return _8d;
};
Xinha.prototype.generate=function(){
if(!Xinha.isSupportedBrowser){
return;
}
var i;
var _90=this;
var url;
var _92=false;
var _93=document.getElementsByTagName("link");
if(!document.getElementById("XinhaCoreDesign")){
_editor_css=(typeof _editor_css=="string")?_editor_css:"Xinha.css";
for(i=0;i<_93.length;i++){
if((_93[i].rel=="stylesheet")&&(_93[i].href==_editor_url+_editor_css)){
_92=true;
}
}
if(!_92){
Xinha.loadStyle(_editor_css,null,"XinhaCoreDesign");
}
}
if(_editor_skin!==""&&!document.getElementById("XinhaSkin")){
_92=false;
for(i=0;i<_93.length;i++){
if((_93[i].rel=="stylesheet")&&(_93[i].href==_editor_url+"skins/"+_editor_skin+"/skin.css")){
_92=true;
}
}
if(!_92){
Xinha.loadStyle("skins/"+_editor_skin+"/skin.css",null,"XinhaSkin");
}
}
if(Xinha.is_ie){
url=_editor_url+"modules/InternetExplorer/InternetExplorer.js";
if(typeof InternetExplorer=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("InternetExplorer",function(){
_90.generate();
},url);
return false;
}
_90._browserSpecificPlugin=_90.registerPlugin("InternetExplorer");
}else{
url=_editor_url+"modules/Gecko/Gecko.js";
if(typeof Gecko=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("Gecko",function(){
_90.generate();
},url);
return false;
}
_90._browserSpecificPlugin=_90.registerPlugin("Gecko");
}
if(typeof Dialog=="undefined"&&!Xinha._loadback(_editor_url+"modules/Dialogs/dialog.js",this.generate,this)){
return false;
}
if(typeof Xinha.Dialog=="undefined"&&!Xinha._loadback(_editor_url+"modules/Dialogs/inline-dialog.js",this.generate,this)){
return false;
}
url=_editor_url+"modules/FullScreen/full-screen.js";
if(typeof FullScreen=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("FullScreen",function(){
_90.generate();
},url);
return false;
}
url=_editor_url+"modules/ColorPicker/ColorPicker.js";
if(typeof ColorPicker=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("ColorPicker",function(){
_90.generate();
},url);
return false;
}else{
if(typeof ColorPicker!="undefined"){
_90.registerPlugin("ColorPicker");
}
}
var _94=_90.config.toolbar;
for(i=_94.length;--i>=0;){
for(var j=_94[i].length;--j>=0;){
switch(_94[i][j]){
case "popupeditor":
_90.registerPlugin("FullScreen");
break;
case "insertimage":
url=_editor_url+"modules/InsertImage/insert_image.js";
if(typeof InsertImage=="undefined"&&typeof Xinha.prototype._insertImage=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("InsertImage",function(){
_90.generate();
},url);
return false;
}else{
if(typeof InsertImage!="undefined"){
_90.registerPlugin("InsertImage");
}
}
break;
case "createlink":
url=_editor_url+"modules/CreateLink/link.js";
if(typeof CreateLink=="undefined"&&typeof Xinha.prototype._createLink=="undefined"&&typeof Linker=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("CreateLink",function(){
_90.generate();
},url);
return false;
}else{
if(typeof CreateLink!="undefined"){
_90.registerPlugin("CreateLink");
}
}
break;
case "inserttable":
url=_editor_url+"modules/InsertTable/insert_table.js";
if(typeof InsertTable=="undefined"&&typeof Xinha.prototype._insertTable=="undefined"&&!document.getElementById(url)){
Xinha.loadPlugin("InsertTable",function(){
_90.generate();
},url);
return false;
}else{
if(typeof InsertTable!="undefined"){
_90.registerPlugin("InsertTable");
}
}
break;
}
}
}
if(Xinha.is_gecko&&(_90.config.mozParaHandler=="best"||_90.config.mozParaHandler=="dirty")){
switch(this.config.mozParaHandler){
case "dirty":
var _96=_editor_url+"modules/Gecko/paraHandlerDirty.js";
break;
default:
var _96=_editor_url+"modules/Gecko/paraHandlerBest.js";
break;
}
if(typeof EnterParagraphs=="undefined"&&!document.getElementById(_96)){
Xinha.loadPlugin("EnterParagraphs",function(){
_90.generate();
},_96);
return false;
}
_90.registerPlugin("EnterParagraphs");
}
switch(this.config.getHtmlMethod){
case "TransformInnerHTML":
var _97=_editor_url+"modules/GetHtml/TransformInnerHTML.js";
break;
default:
var _97=_editor_url+"modules/GetHtml/DOMwalk.js";
break;
}
if(typeof GetHtmlImplementation=="undefined"&&!document.getElementById(_97)){
Xinha.loadPlugin("GetHtmlImplementation",function(){
_90.generate();
},_97);
return false;
}else{
_90.registerPlugin("GetHtmlImplementation");
}
this.setLoadingMessage(Xinha._lc("Generate Xinha framework"));
this._framework={"table":document.createElement("table"),"tbody":document.createElement("tbody"),"tb_row":document.createElement("tr"),"tb_cell":document.createElement("td"),"tp_row":document.createElement("tr"),"tp_cell":this._panels.top.container,"ler_row":document.createElement("tr"),"lp_cell":this._panels.left.container,"ed_cell":document.createElement("td"),"rp_cell":this._panels.right.container,"bp_row":document.createElement("tr"),"bp_cell":this._panels.bottom.container,"sb_row":document.createElement("tr"),"sb_cell":document.createElement("td")};
Xinha.freeLater(this._framework);
var fw=this._framework;
fw.table.border="0";
fw.table.cellPadding="0";
fw.table.cellSpacing="0";
fw.tb_row.style.verticalAlign="top";
fw.tp_row.style.verticalAlign="top";
fw.ler_row.style.verticalAlign="top";
fw.bp_row.style.verticalAlign="top";
fw.sb_row.style.verticalAlign="top";
fw.ed_cell.style.position="relative";
fw.tb_row.appendChild(fw.tb_cell);
fw.tb_cell.colSpan=3;
fw.tp_row.appendChild(fw.tp_cell);
fw.tp_cell.colSpan=3;
fw.ler_row.appendChild(fw.lp_cell);
fw.ler_row.appendChild(fw.ed_cell);
fw.ler_row.appendChild(fw.rp_cell);
fw.bp_row.appendChild(fw.bp_cell);
fw.bp_cell.colSpan=3;
fw.sb_row.appendChild(fw.sb_cell);
fw.sb_cell.colSpan=3;
fw.tbody.appendChild(fw.tb_row);
fw.tbody.appendChild(fw.tp_row);
fw.tbody.appendChild(fw.ler_row);
fw.tbody.appendChild(fw.bp_row);
fw.tbody.appendChild(fw.sb_row);
fw.table.appendChild(fw.tbody);
var _99=this._framework.table;
this._htmlArea=_99;
Xinha.freeLater(this,"_htmlArea");
_99.className="htmlarea";
this._framework.tb_cell.appendChild(this._createToolbar());
var _9a=document.createElement("iframe");
_9a.src=_editor_url+_90.config.URIs.blank;
_9a.id="XinhaIFrame_"+this._textArea.id;
this._framework.ed_cell.appendChild(_9a);
this._iframe=_9a;
this._iframe.className="xinha_iframe";
Xinha.freeLater(this,"_iframe");
var _9b=this._createStatusBar();
this._framework.sb_cell.appendChild(_9b);
var _9c=this._textArea;
_9c.parentNode.insertBefore(_99,_9c);
_9c.className="xinha_textarea";
Xinha.removeFromParent(_9c);
this._framework.ed_cell.appendChild(_9c);
Xinha.addDom0Event(this._textArea,"click",function(){
if(Xinha._currentlyActiveEditor!=this){
_90.updateToolbar();
}
return true;
});
if(_9c.form){
Xinha.prependDom0Event(this._textArea.form,"submit",function(){
_90._textArea.value=_90.outwardHtml(_90.getHTML());
return true;
});
var _9d=_9c.value;
Xinha.prependDom0Event(this._textArea.form,"reset",function(){
_90.setHTML(_90.inwardHtml(_9d));
_90.updateToolbar();
return true;
});
if(!_9c.form.xinha_submit){
try{
_9c.form.xinha_submit=_9c.form.submit;
_9c.form.submit=function(){
this.onsubmit();
this.xinha_submit();
};
}
catch(ex){
}
}
}
Xinha.prependDom0Event(window,"unload",function(){
_9c.value=_90.outwardHtml(_90.getHTML());
if(!Xinha.is_ie){
_99.parentNode.replaceChild(_9c,_99);
}
return true;
});
_9c.style.display="none";
_90.initSize();
this.setLoadingMessage(Xinha._lc("Finishing"));
_90._iframeLoadDone=false;
if(Xinha.is_opera){
Xinha._addEvent(this._iframe.contentWindow,"load",function(e){
if(!_90._iframeLoadDone){
_90._iframeLoadDone=true;
_90.initIframe();
}
return true;
});
}else{
Xinha._addEvent(this._iframe,"load",function(e){
if(!_90._iframeLoadDone){
_90._iframeLoadDone=true;
_90.initIframe();
}
return true;
});
}
};
Xinha.prototype.initSize=function(){
this.setLoadingMessage(Xinha._lc("Init editor size"));
var _a0=this;
var _a1=null;
var _a2=null;
switch(this.config.width){
case "auto":
_a1=this._initial_ta_size.w;
break;
case "toolbar":
_a1=this._toolBar.offsetWidth+"px";
break;
default:
_a1=/[^0-9]/.test(this.config.width)?this.config.width:this.config.width+"px";
break;
}
switch(this.config.height){
case "auto":
_a2=this._initial_ta_size.h;
break;
default:
_a2=/[^0-9]/.test(this.config.height)?this.config.height:this.config.height+"px";
break;
}
this.sizeEditor(_a1,_a2,this.config.sizeIncludesBars,this.config.sizeIncludesPanels);
this.notifyOn("panel_change",function(){
_a0.sizeEditor();
});
};
Xinha.prototype.sizeEditor=function(_a3,_a4,_a5,_a6){
if(this._risizing){
return;
}
this._risizing=true;
this.notifyOf("before_resize",{width:_a3,height:_a4});
this._iframe.style.height="100%";
this._textArea.style.height="100%";
this._iframe.style.width="";
this._textArea.style.width="";
if(_a5!==null){
this._htmlArea.sizeIncludesToolbars=_a5;
}
if(_a6!==null){
this._htmlArea.sizeIncludesPanels=_a6;
}
if(_a3){
this._htmlArea.style.width=_a3;
if(!this._htmlArea.sizeIncludesPanels){
var _a7=this._panels.right;
if(_a7.on&&_a7.panels.length&&Xinha.hasDisplayedChildren(_a7.div)){
this._htmlArea.style.width=(this._htmlArea.offsetWidth+parseInt(this.config.panel_dimensions.right,10))+"px";
}
var _a8=this._panels.left;
if(_a8.on&&_a8.panels.length&&Xinha.hasDisplayedChildren(_a8.div)){
this._htmlArea.style.width=(this._htmlArea.offsetWidth+parseInt(this.config.panel_dimensions.left,10))+"px";
}
}
}
if(_a4){
this._htmlArea.style.height=_a4;
if(!this._htmlArea.sizeIncludesToolbars){
this._htmlArea.style.height=(this._htmlArea.offsetHeight+this._toolbar.offsetHeight+this._statusBar.offsetHeight)+"px";
}
if(!this._htmlArea.sizeIncludesPanels){
var _a9=this._panels.top;
if(_a9.on&&_a9.panels.length&&Xinha.hasDisplayedChildren(_a9.div)){
this._htmlArea.style.height=(this._htmlArea.offsetHeight+parseInt(this.config.panel_dimensions.top,10))+"px";
}
var _aa=this._panels.bottom;
if(_aa.on&&_aa.panels.length&&Xinha.hasDisplayedChildren(_aa.div)){
this._htmlArea.style.height=(this._htmlArea.offsetHeight+parseInt(this.config.panel_dimensions.bottom,10))+"px";
}
}
}
_a3=this._htmlArea.offsetWidth;
_a4=this._htmlArea.offsetHeight;
var _ab=this._panels;
var _ac=this;
var _ad=1;
function panel_is_alive(pan){
if(_ab[pan].on&&_ab[pan].panels.length&&Xinha.hasDisplayedChildren(_ab[pan].container)){
_ab[pan].container.style.display="";
return true;
}else{
_ab[pan].container.style.display="none";
return false;
}
}
if(panel_is_alive("left")){
_ad+=1;
}
if(panel_is_alive("right")){
_ad+=1;
}
this._framework.tb_cell.colSpan=_ad;
this._framework.tp_cell.colSpan=_ad;
this._framework.bp_cell.colSpan=_ad;
this._framework.sb_cell.colSpan=_ad;
if(!this._framework.tp_row.childNodes.length){
Xinha.removeFromParent(this._framework.tp_row);
}else{
if(!Xinha.hasParentNode(this._framework.tp_row)){
this._framework.tbody.insertBefore(this._framework.tp_row,this._framework.ler_row);
}
}
if(!this._framework.bp_row.childNodes.length){
Xinha.removeFromParent(this._framework.bp_row);
}else{
if(!Xinha.hasParentNode(this._framework.bp_row)){
this._framework.tbody.insertBefore(this._framework.bp_row,this._framework.ler_row.nextSibling);
}
}
if(!this.config.statusBar){
Xinha.removeFromParent(this._framework.sb_row);
}else{
if(!Xinha.hasParentNode(this._framework.sb_row)){
this._framework.table.appendChild(this._framework.sb_row);
}
}
this._framework.lp_cell.style.width=this.config.panel_dimensions.left;
this._framework.rp_cell.style.width=this.config.panel_dimensions.right;
this._framework.tp_cell.style.height=this.config.panel_dimensions.top;
this._framework.bp_cell.style.height=this.config.panel_dimensions.bottom;
this._framework.tb_cell.style.height=this._toolBar.offsetHeight+"px";
this._framework.sb_cell.style.height=this._statusBar.offsetHeight+"px";
var _af=_a4-this._toolBar.offsetHeight-this._statusBar.offsetHeight;
if(panel_is_alive("top")){
_af-=parseInt(this.config.panel_dimensions.top,10);
}
if(panel_is_alive("bottom")){
_af-=parseInt(this.config.panel_dimensions.bottom,10);
}
this._iframe.style.height=_af+"px";
var _b0=_a3;
if(panel_is_alive("left")){
_b0-=parseInt(this.config.panel_dimensions.left,10);
}
if(panel_is_alive("right")){
_b0-=parseInt(this.config.panel_dimensions.right,10);
}
this._iframe.style.width=_b0+"px";
this._textArea.style.height=this._iframe.style.height;
this._textArea.style.width=this._iframe.style.width;
this.notifyOf("resize",{width:this._htmlArea.offsetWidth,height:this._htmlArea.offsetHeight});
this._risizing=false;
};
Xinha.prototype.registerPanel=function(_b1,_b2){
if(!_b1){
_b1="right";
}
this.setLoadingMessage("Register "+_b1+" panel ");
var _b3=this.addPanel(_b1);
if(_b2){
_b2.drawPanelIn(_b3);
}
};
Xinha.prototype.addPanel=function(_b4){
var div=document.createElement("div");
div.side=_b4;
if(_b4=="left"||_b4=="right"){
div.style.width=this.config.panel_dimensions[_b4];
if(this._iframe){
div.style.height=this._iframe.style.height;
}
}
Xinha.addClasses(div,"panel");
this._panels[_b4].panels.push(div);
this._panels[_b4].div.appendChild(div);
this.notifyOf("panel_change",{"action":"add","panel":div});
return div;
};
Xinha.prototype.removePanel=function(_b6){
this._panels[_b6.side].div.removeChild(_b6);
var _b7=[];
for(var i=0;i<this._panels[_b6.side].panels.length;i++){
if(this._panels[_b6.side].panels[i]!=_b6){
_b7.push(this._panels[_b6.side].panels[i]);
}
}
this._panels[_b6.side].panels=_b7;
this.notifyOf("panel_change",{"action":"remove","panel":_b6});
};
Xinha.prototype.hidePanel=function(_b9){
if(_b9&&_b9.style.display!="none"){
try{
var pos=this.scrollPos(this._iframe.contentWindow);
}
catch(e){
}
_b9.style.display="none";
this.notifyOf("panel_change",{"action":"hide","panel":_b9});
try{
this._iframe.contentWindow.scrollTo(pos.x,pos.y);
}
catch(e){
}
}
};
Xinha.prototype.showPanel=function(_bb){
if(_bb&&_bb.style.display=="none"){
try{
var pos=this.scrollPos(this._iframe.contentWindow);
}
catch(e){
}
_bb.style.display="";
this.notifyOf("panel_change",{"action":"show","panel":_bb});
try{
this._iframe.contentWindow.scrollTo(pos.x,pos.y);
}
catch(e){
}
}
};
Xinha.prototype.hidePanels=function(_bd){
if(typeof _bd=="undefined"){
_bd=["left","right","top","bottom"];
}
var _be=[];
for(var i=0;i<_bd.length;i++){
if(this._panels[_bd[i]].on){
_be.push(_bd[i]);
this._panels[_bd[i]].on=false;
}
}
this.notifyOf("panel_change",{"action":"multi_hide","sides":_bd});
};
Xinha.prototype.showPanels=function(_c0){
if(typeof _c0=="undefined"){
_c0=["left","right","top","bottom"];
}
var _c1=[];
for(var i=0;i<_c0.length;i++){
if(!this._panels[_c0[i]].on){
_c1.push(_c0[i]);
this._panels[_c0[i]].on=true;
}
}
this.notifyOf("panel_change",{"action":"multi_show","sides":_c0});
};
Xinha.objectProperties=function(obj){
var _c4=[];
for(var x in obj){
_c4[_c4.length]=x;
}
return _c4;
};
Xinha.prototype.editorIsActivated=function(){
try{
return Xinha.is_designMode?this._doc.designMode=="on":this._doc.body.contentEditable;
}
catch(ex){
return false;
}
};
Xinha._someEditorHasBeenActivated=false;
Xinha._currentlyActiveEditor=null;
Xinha.prototype.activateEditor=function(){
if(Xinha._currentlyActiveEditor){
if(Xinha._currentlyActiveEditor==this){
return true;
}
Xinha._currentlyActiveEditor.deactivateEditor();
}
if(Xinha.is_designMode&&this._doc.designMode!="on"){
try{
if(this._iframe.style.display=="none"){
this._iframe.style.display="";
this._doc.designMode="on";
this._iframe.style.display="none";
}else{
this._doc.designMode="on";
}
}
catch(ex){
}
}else{
if(Xinha.is_ie&&this._doc.body.contentEditable!==true){
this._doc.body.contentEditable=true;
if(this._iframe.contentWindow.event.srcElement.tagName.toLowerCase()=="html"){
var r=this._doc.body.createTextRange();
setTimeout(function(){
r.collapse();
r.select();
},100);
}
}
}
Xinha._someEditorHasBeenActivated=true;
Xinha._currentlyActiveEditor=this;
var _c7=this;
this.enableToolbar();
};
Xinha.prototype.deactivateEditor=function(){
this.disableToolbar();
if(Xinha.is_designMode&&this._doc.designMode!="off"){
try{
this._doc.designMode="off";
}
catch(ex){
}
}else{
if(!Xinha.is_gecko&&this._doc.body.contentEditable!==false){
this._doc.body.contentEditable=false;
}
}
if(Xinha._currentlyActiveEditor!=this){
return;
}
Xinha._currentlyActiveEditor=false;
};
Xinha.prototype.initIframe=function(){
this.disableToolbar();
var doc=null;
var _c9=this;
try{
if(_c9._iframe.contentDocument){
this._doc=_c9._iframe.contentDocument;
}else{
this._doc=_c9._iframe.contentWindow.document;
}
doc=this._doc;
if(!doc){
if(Xinha.is_gecko){
setTimeout(function(){
_c9.initIframe();
},50);
return false;
}else{
alert("ERROR: IFRAME can't be initialized.");
}
}
}
catch(ex){
setTimeout(function(){
_c9.initIframe();
},50);
}
Xinha.freeLater(this,"_doc");
doc.open("text/html","replace");
var _ca="";
if(_c9.config.browserQuirksMode===false){
var _cb="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
}else{
if(_c9.config.browserQuirksMode===true){
var _cb="";
}else{
var _cb=Xinha.getDoctype(document);
}
}
if(!_c9.config.fullPage){
_ca+=_cb+"\n";
_ca+="<html>\n";
_ca+="<head>\n";
_ca+="<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+_c9.config.charSet+"\">\n";
if(typeof _c9.config.baseHref!="undefined"&&_c9.config.baseHref!==null){
_ca+="<base href=\""+_c9.config.baseHref+"\"/>\n";
}
_ca+=Xinha.addCoreCSS();
if(_c9.config.pageStyle){
_ca+="<style type=\"text/css\">\n"+_c9.config.pageStyle+"\n</style>";
}
if(typeof _c9.config.pageStyleSheets!=="undefined"){
for(var i=0;i<_c9.config.pageStyleSheets.length;i++){
if(_c9.config.pageStyleSheets[i].length>0){
_ca+="<link rel=\"stylesheet\" type=\"text/css\" href=\""+_c9.config.pageStyleSheets[i]+"\">";
}
}
}
_ca+="</head>\n";
_ca+="<body>\n";
_ca+=_c9.inwardHtml(_c9._textArea.value);
_ca+="</body>\n";
_ca+="</html>";
}else{
_ca=_c9.inwardHtml(_c9._textArea.value);
if(_ca.match(Xinha.RE_doctype)){
_c9.setDoctype(RegExp.$1);
}
var _cd=_ca.match(/<link\s+[\s\S]*?["']\s*\/?>/gi);
_ca=_ca.replace(/<link\s+[\s\S]*?["']\s*\/?>\s*/gi,"");
_cd?_ca=_ca.replace(/<\/head>/i,_cd.join("\n")+"\n</head>"):null;
}
doc.write(_ca);
doc.close();
if(this.config.fullScreen){
this._fullScreen();
}
this.setEditorEvents();
};
Xinha.prototype.whenDocReady=function(f){
var e=this;
if(this._doc&&this._doc.body){
f();
}else{
setTimeout(function(){
e.whenDocReady(f);
},50);
}
};
Xinha.prototype.setMode=function(_d0){
var _d1;
if(typeof _d0=="undefined"){
_d0=this._editMode=="textmode"?"wysiwyg":"textmode";
}
switch(_d0){
case "textmode":
this.setCC("iframe");
_d1=this.outwardHtml(this.getHTML());
this.setHTML(_d1);
this.deactivateEditor();
this._iframe.style.display="none";
this._textArea.style.display="";
if(this.config.statusBar){
this._statusBarTree.style.display="none";
this._statusBarTextMode.style.display="";
}
this.notifyOf("modechange",{"mode":"text"});
this.findCC("textarea");
break;
case "wysiwyg":
this.setCC("textarea");
_d1=this.inwardHtml(this.getHTML());
this.deactivateEditor();
this.setHTML(_d1);
this._iframe.style.display="";
this._textArea.style.display="none";
this.activateEditor();
if(this.config.statusBar){
this._statusBarTree.style.display="";
this._statusBarTextMode.style.display="none";
}
this.notifyOf("modechange",{"mode":"wysiwyg"});
this.findCC("iframe");
break;
default:
alert("Mode <"+_d0+"> not defined!");
return false;
}
this._editMode=_d0;
for(var i in this.plugins){
var _d3=this.plugins[i].instance;
if(_d3&&typeof _d3.onMode=="function"){
_d3.onMode(_d0);
}
}
};
Xinha.prototype.setFullHTML=function(_d4){
var _d5=RegExp.multiline;
RegExp.multiline=true;
if(_d4.match(Xinha.RE_doctype)){
this.setDoctype(RegExp.$1);
}
RegExp.multiline=_d5;
if(0){
if(_d4.match(Xinha.RE_head)){
this._doc.getElementsByTagName("head")[0].innerHTML=RegExp.$1;
}
if(_d4.match(Xinha.RE_body)){
this._doc.getElementsByTagName("body")[0].innerHTML=RegExp.$1;
}
}else{
var _d6=this.editorIsActivated();
if(_d6){
this.deactivateEditor();
}
var _d7=/<html>((.|\n)*?)<\/html>/i;
_d4=_d4.replace(_d7,"$1");
this._doc.open("text/html","replace");
this._doc.write(_d4);
this._doc.close();
if(_d6){
this.activateEditor();
}
this.setEditorEvents();
return true;
}
};
Xinha.prototype.setEditorEvents=function(){
var _d8=this;
var doc=(Xinha.is_ie)?this._doc.getElementsByTagName("html")[0]:this._doc;
_d8.whenDocReady(function(){
Xinha._addEvents(doc,["mousedown"],function(){
_d8.activateEditor();
return true;
});
Xinha._addEvents(doc,["keydown","keypress","mousedown","mouseup","drag"],function(_da){
return _d8._editorEvent(Xinha.is_ie?_d8._iframe.contentWindow.event:_da);
});
for(var i in _d8.plugins){
var _dc=_d8.plugins[i].instance;
Xinha.refreshPlugin(_dc);
}
if(typeof _d8._onGenerate=="function"){
_d8._onGenerate();
}
Xinha.addDom0Event(window,"resize",function(e){
_d8.sizeEditor();
});
_d8.removeLoadingMessage();
});
};
Xinha.prototype.registerPlugin=function(){
if(!Xinha.isSupportedBrowser){
return;
}
var _de=arguments[0];
if(_de===null||typeof _de=="undefined"||(typeof _de=="string"&&eval("typeof "+_de)=="undefined")){
return false;
}
var _df=[];
for(var i=1;i<arguments.length;++i){
_df.push(arguments[i]);
}
return this.registerPlugin2(_de,_df);
};
Xinha.prototype.registerPlugin2=function(_e1,_e2){
if(typeof _e1=="string"){
_e1=eval(_e1);
}
if(typeof _e1=="undefined"){
return false;
}
var obj=new _e1(this,_e2);
if(obj){
var _e4={};
var _e5=_e1._pluginInfo;
for(var i in _e5){
_e4[i]=_e5[i];
}
_e4.instance=obj;
_e4.args=_e2;
this.plugins[_e1._pluginInfo.name]=_e4;
return obj;
}else{
alert("Can't register plugin "+_e1.toString()+".");
}
};
Xinha.getPluginDir=function(_e7){
return _editor_url+"plugins/"+_e7;
};
Xinha.loadPlugin=function(_e8,_e9,_ea){
if(!Xinha.isSupportedBrowser){
return;
}
Xinha.setLoadingMessage(Xinha._lc("Loading plugin $plugin="+_e8+"$"));
if(typeof window["pluginName"]!="undefined"){
if(_e9){
_e9(_e8);
}
return true;
}
if(!_ea){
var dir=this.getPluginDir(_e8);
var _ec=_e8.replace(/([a-z])([A-Z])([a-z])/g,function(str,l1,l2,l3){
return l1+"-"+l2.toLowerCase()+l3;
}).toLowerCase()+".js";
_ea=dir+"/"+_ec;
}
Xinha._loadback(_ea,_e9?function(){
_e9(_e8);
}:null);
return false;
};
Xinha._pluginLoadStatus={};
Xinha.loadPlugins=function(_f1,_f2){
if(!Xinha.isSupportedBrowser){
return;
}
var _f3=true;
var _f4=Xinha.cloneObject(_f1);
while(_f4.length){
var p=_f4.pop();
if(typeof Xinha._pluginLoadStatus[p]=="undefined"){
Xinha._pluginLoadStatus[p]="loading";
Xinha.loadPlugin(p,function(_f6){
if(eval("typeof "+_f6)!="undefined"){
Xinha._pluginLoadStatus[_f6]="ready";
}else{
Xinha._pluginLoadStatus[_f6]="failed";
}
});
_f3=false;
}else{
switch(Xinha._pluginLoadStatus[p]){
case "failed":
case "ready":
break;
default:
_f3=false;
break;
}
}
}
if(_f3){
return true;
}
if(_f2){
setTimeout(function(){
if(Xinha.loadPlugins(_f1,_f2)){
_f2();
}
},150);
}
return _f3;
};
Xinha.refreshPlugin=function(_f7){
if(_f7&&typeof _f7.onGenerate=="function"){
_f7.onGenerate();
}
if(_f7&&typeof _f7.onGenerateOnce=="function"){
_f7.onGenerateOnce();
_f7.onGenerateOnce=null;
}
};
Xinha.prototype.firePluginEvent=function(_f8){
var _f9=[];
for(var i=1;i<arguments.length;i++){
_f9[i-1]=arguments[i];
}
for(var i in this.plugins){
var _fb=this.plugins[i].instance;
if(_fb==this._browserSpecificPlugin){
continue;
}
if(_fb&&typeof _fb[_f8]=="function"){
if(_fb[_f8].apply(_fb,_f9)){
return true;
}
}
}
var _fb=this._browserSpecificPlugin;
if(_fb&&typeof _fb[_f8]=="function"){
if(_fb[_f8].apply(_fb,_f9)){
return true;
}
}
return false;
};
Xinha.loadStyle=function(_fc,_fd,id){
var url=_editor_url||"";
if(_fd){
url=Xinha.getPluginDir(_fd)+"/";
}
url+=_fc;
if(/^\//.test(_fc)){
url=_fc;
}
var head=document.getElementsByTagName("head")[0];
var link=document.createElement("link");
link.rel="stylesheet";
link.href=url;
link.type="text/css";
if(id){
link.id=id;
}
head.appendChild(link);
};
Xinha.prototype.debugTree=function(){
var ta=document.createElement("textarea");
ta.style.width="100%";
ta.style.height="20em";
ta.value="";
function debug(_103,str){
for(;--_103>=0;){
ta.value+=" ";
}
ta.value+=str+"\n";
}
function _dt(root,_106){
var tag=root.tagName.toLowerCase(),i;
var ns=Xinha.is_ie?root.scopeName:root.prefix;
debug(_106,"- "+tag+" ["+ns+"]");
for(i=root.firstChild;i;i=i.nextSibling){
if(i.nodeType==1){
_dt(i,_106+2);
}
}
}
_dt(this._doc.body,0);
document.body.appendChild(ta);
};
Xinha.getInnerText=function(el){
var txt="",i;
for(i=el.firstChild;i;i=i.nextSibling){
if(i.nodeType==3){
txt+=i.data;
}else{
if(i.nodeType==1){
txt+=Xinha.getInnerText(i);
}
}
}
return txt;
};
Xinha.prototype._wordClean=function(){
var _10b=this;
var _10c={empty_tags:0,mso_class:0,mso_style:0,mso_xmlel:0,orig_len:this._doc.body.innerHTML.length,T:(new Date()).getTime()};
var _10d={empty_tags:"Empty tags removed: ",mso_class:"MSO class names removed: ",mso_style:"MSO inline style removed: ",mso_xmlel:"MSO XML elements stripped: "};
function showStats(){
var txt="Xinha word cleaner stats: \n\n";
for(var i in _10c){
if(_10d[i]){
txt+=_10d[i]+_10c[i]+"\n";
}
}
txt+="\nInitial document length: "+_10c.orig_len+"\n";
txt+="Final document length: "+_10b._doc.body.innerHTML.length+"\n";
txt+="Clean-up took "+(((new Date()).getTime()-_10c.T)/1000)+" seconds";
alert(txt);
}
function clearClass(node){
var newc=node.className.replace(/(^|\s)mso.*?(\s|$)/ig," ");
if(newc!=node.className){
node.className=newc;
if(!(/\S/.test(node.className))){
node.removeAttribute("className");
++_10c.mso_class;
}
}
}
function clearStyle(node){
var _113=node.style.cssText.split(/\s*;\s*/);
for(var i=_113.length;--i>=0;){
if((/^mso|^tab-stops/i.test(_113[i]))||(/^margin\s*:\s*0..\s+0..\s+0../i.test(_113[i]))){
++_10c.mso_style;
_113.splice(i,1);
}
}
node.style.cssText=_113.join("; ");
}
var _115=null;
if(Xinha.is_ie){
_115=function(el){
el.outerHTML=Xinha.htmlEncode(el.innerText);
++_10c.mso_xmlel;
};
}else{
_115=function(el){
var txt=document.createTextNode(Xinha.getInnerText(el));
el.parentNode.insertBefore(txt,el);
Xinha.removeFromParent(el);
++_10c.mso_xmlel;
};
}
function checkEmpty(el){
if(/^(span|b|strong|i|em|font|div|p)$/i.test(el.tagName)&&!el.firstChild){
Xinha.removeFromParent(el);
++_10c.empty_tags;
}
}
function parseTree(root){
var tag=root.tagName.toLowerCase(),i,next;
if((Xinha.is_ie&&root.scopeName!="HTML")||(!Xinha.is_ie&&(/:/.test(tag)))){
_115(root);
return false;
}else{
clearClass(root);
clearStyle(root);
for(i=root.firstChild;i;i=next){
next=i.nextSibling;
if(i.nodeType==1&&parseTree(i)){
checkEmpty(i);
}
}
}
return true;
}
parseTree(this._doc.body);
this.updateToolbar();
};
Xinha.prototype._clearFonts=function(){
var D=this.getInnerHTML();
if(confirm(Xinha._lc("Would you like to clear font typefaces?"))){
D=D.replace(/face="[^"]*"/gi,"");
D=D.replace(/font-family:[^;}"']+;?/gi,"");
}
if(confirm(Xinha._lc("Would you like to clear font sizes?"))){
D=D.replace(/size="[^"]*"/gi,"");
D=D.replace(/font-size:[^;}"']+;?/gi,"");
}
if(confirm(Xinha._lc("Would you like to clear font colours?"))){
D=D.replace(/color="[^"]*"/gi,"");
D=D.replace(/([^-])color:[^;}"']+;?/gi,"$1");
}
D=D.replace(/(style|class)="\s*"/gi,"");
D=D.replace(/<(font|span)\s*>/gi,"");
this.setHTML(D);
this.updateToolbar();
};
Xinha.prototype._splitBlock=function(){
this._doc.execCommand("formatblock",false,"div");
};
Xinha.prototype.forceRedraw=function(){
this._doc.body.style.visibility="hidden";
this._doc.body.style.visibility="";
};
Xinha.prototype.focusEditor=function(){
switch(this._editMode){
case "wysiwyg":
try{
if(Xinha._someEditorHasBeenActivated){
this.activateEditor();
this._iframe.contentWindow.focus();
}
}
catch(ex){
}
break;
case "textmode":
try{
this._textArea.focus();
}
catch(e){
}
break;
default:
alert("ERROR: mode "+this._editMode+" is not defined");
}
return this._doc;
};
Xinha.prototype._undoTakeSnapshot=function(){
++this._undoPos;
if(this._undoPos>=this.config.undoSteps){
this._undoQueue.shift();
--this._undoPos;
}
var take=true;
var txt=this.getInnerHTML();
if(this._undoPos>0){
take=(this._undoQueue[this._undoPos-1]!=txt);
}
if(take){
this._undoQueue[this._undoPos]=txt;
}else{
this._undoPos--;
}
};
Xinha.prototype.undo=function(){
if(this._undoPos>0){
var txt=this._undoQueue[--this._undoPos];
if(txt){
this.setHTML(txt);
}else{
++this._undoPos;
}
}
};
Xinha.prototype.redo=function(){
if(this._undoPos<this._undoQueue.length-1){
var txt=this._undoQueue[++this._undoPos];
if(txt){
this.setHTML(txt);
}else{
--this._undoPos;
}
}
};
Xinha.prototype.disableToolbar=function(_121){
if(this._timerToolbar){
clearTimeout(this._timerToolbar);
}
if(typeof _121=="undefined"){
_121=[];
}else{
if(typeof _121!="object"){
_121=[_121];
}
}
for(var i in this._toolbarObjects){
var btn=this._toolbarObjects[i];
if(_121.contains(i)){
continue;
}
if(typeof (btn.state)!="function"){
continue;
}
btn.state("enabled",false);
}
};
Xinha.prototype.enableToolbar=function(){
this.updateToolbar();
};
Xinha.prototype.updateToolbar=function(_124){
var doc=this._doc;
var text=(this._editMode=="textmode");
var _127=null;
if(!text){
_127=this.getAllAncestors();
if(this.config.statusBar&&!_124){
while(this._statusBarItems.length){
var item=this._statusBarItems.pop();
item.el=null;
item.editor=null;
item.onclick=null;
item.oncontextmenu=null;
item._xinha_dom0Events["click"]=null;
item._xinha_dom0Events["contextmenu"]=null;
item=null;
}
this._statusBarTree.innerHTML=Xinha._lc("Path")+": ";
for(var i=_127.length;--i>=0;){
var el=_127[i];
if(!el){
continue;
}
var a=document.createElement("a");
a.href="javascript:void(0)";
a.el=el;
a.editor=this;
this._statusBarItems.push(a);
Xinha.addDom0Event(a,"click",function(){
this.blur();
this.editor.selectNodeContents(this.el);
this.editor.updateToolbar(true);
return false;
});
Xinha.addDom0Event(a,"contextmenu",function(){
this.blur();
var info="Inline style:\n\n";
info+=this.el.style.cssText.split(/;\s*/).join(";\n");
alert(info);
return false;
});
var txt=el.tagName.toLowerCase();
if(typeof el.style!="undefined"){
a.title=el.style.cssText;
}
if(el.id){
txt+="#"+el.id;
}
if(el.className){
txt+="."+el.className;
}
a.appendChild(document.createTextNode(txt));
this._statusBarTree.appendChild(a);
if(i!==0){
this._statusBarTree.appendChild(document.createTextNode(String.fromCharCode(187)));
}
Xinha.freeLater(a);
}
}
}
for(var cmd in this._toolbarObjects){
var btn=this._toolbarObjects[cmd];
var _130=true;
if(typeof (btn.state)!="function"){
continue;
}
if(btn.context&&!text){
_130=false;
var _131=btn.context;
var _132=[];
if(/(.*)\[(.*?)\]/.test(_131)){
_131=RegExp.$1;
_132=RegExp.$2.split(",");
}
_131=_131.toLowerCase();
var _133=(_131=="*");
for(var k=0;k<_127.length;++k){
if(!_127[k]){
continue;
}
if(_133||(_127[k].tagName.toLowerCase()==_131)){
_130=true;
var _135=null;
var att=null;
var comp=null;
var _138=null;
for(var ka=0;ka<_132.length;++ka){
_135=_132[ka].match(/(.*)(==|!=|===|!==|>|>=|<|<=)(.*)/);
att=_135[1];
comp=_135[2];
_138=_135[3];
if(!eval(_127[k][att]+comp+_138)){
_130=false;
break;
}
}
if(_130){
break;
}
}
}
}
btn.state("enabled",(!text||btn.text)&&_130);
if(typeof cmd=="function"){
continue;
}
var _13a=this.config.customSelects[cmd];
if((!text||btn.text)&&(typeof _13a!="undefined")){
_13a.refresh(this);
continue;
}
switch(cmd){
case "fontname":
case "fontsize":
if(!text){
try{
var _13b=(""+doc.queryCommandValue(cmd)).toLowerCase();
if(!_13b){
btn.element.selectedIndex=0;
break;
}
var _13c=this.config[cmd];
var _13d=0;
for(var j in _13c){
if((j.toLowerCase()==_13b)||(_13c[j].substr(0,_13b.length).toLowerCase()==_13b)){
btn.element.selectedIndex=_13d;
throw "ok";
}
++_13d;
}
btn.element.selectedIndex=0;
}
catch(ex){
}
}
break;
case "formatblock":
var _13f=[];
for(var _140 in this.config.formatblock){
if(typeof this.config.formatblock[_140]=="string"){
_13f[_13f.length]=this.config.formatblock[_140];
}
}
var _141=this._getFirstAncestor(this.getSelection(),_13f);
if(_141){
for(var x=0;x<_13f.length;x++){
if(_13f[x].toLowerCase()==_141.tagName.toLowerCase()){
btn.element.selectedIndex=x;
}
}
}else{
btn.element.selectedIndex=0;
}
break;
case "textindicator":
if(!text){
try{
var _143=btn.element.style;
_143.backgroundColor=Xinha._makeColor(doc.queryCommandValue(Xinha.is_ie?"backcolor":"hilitecolor"));
if(/transparent/i.test(_143.backgroundColor)){
_143.backgroundColor=Xinha._makeColor(doc.queryCommandValue("backcolor"));
}
_143.color=Xinha._makeColor(doc.queryCommandValue("forecolor"));
_143.fontFamily=doc.queryCommandValue("fontname");
_143.fontWeight=doc.queryCommandState("bold")?"bold":"normal";
_143.fontStyle=doc.queryCommandState("italic")?"italic":"normal";
}
catch(ex){
}
}
break;
case "htmlmode":
btn.state("active",text);
break;
case "lefttoright":
case "righttoleft":
var _144=this.getParentElement();
while(_144&&!Xinha.isBlockElement(_144)){
_144=_144.parentNode;
}
if(_144){
btn.state("active",(_144.style.direction==((cmd=="righttoleft")?"rtl":"ltr")));
}
break;
default:
cmd=cmd.replace(/(un)?orderedlist/i,"insert$1orderedlist");
try{
btn.state("active",(!text&&doc.queryCommandState(cmd)));
}
catch(ex){
}
break;
}
}
if(this._customUndo&&!this._timerUndo){
this._undoTakeSnapshot();
var _145=this;
this._timerUndo=setTimeout(function(){
_145._timerUndo=null;
},this.config.undoTimeout);
}
if(0&&Xinha.is_gecko){
var s=this.getSelection();
if(s&&s.isCollapsed&&s.anchorNode&&s.anchorNode.parentNode.tagName.toLowerCase()!="body"&&s.anchorNode.nodeType==3&&s.anchorOffset==s.anchorNode.length&&!(s.anchorNode.parentNode.nextSibling&&s.anchorNode.parentNode.nextSibling.nodeType==3)&&!Xinha.isBlockElement(s.anchorNode.parentNode)){
try{
s.anchorNode.parentNode.parentNode.insertBefore(this._doc.createTextNode("\t"),s.anchorNode.parentNode.nextSibling);
}
catch(ex){
}
}
}
for(var _147 in this.plugins){
var _148=this.plugins[_147].instance;
if(_148&&typeof _148.onUpdateToolbar=="function"){
_148.onUpdateToolbar();
}
}
};
Xinha.prototype.getAllAncestors=function(){
var p=this.getParentElement();
var a=[];
while(p&&(p.nodeType==1)&&(p.tagName.toLowerCase()!="body")){
a.push(p);
p=p.parentNode;
}
a.push(this._doc.body);
return a;
};
Xinha.prototype._getFirstAncestor=function(sel,_14c){
var prnt=this.activeElement(sel);
if(prnt===null){
try{
prnt=(Xinha.is_ie?this.createRange(sel).parentElement():this.createRange(sel).commonAncestorContainer);
}
catch(ex){
return null;
}
}
if(typeof _14c=="string"){
_14c=[_14c];
}
while(prnt){
if(prnt.nodeType==1){
if(_14c===null){
return prnt;
}
if(_14c.contains(prnt.tagName.toLowerCase())){
return prnt;
}
if(prnt.tagName.toLowerCase()=="body"){
break;
}
if(prnt.tagName.toLowerCase()=="table"){
break;
}
}
prnt=prnt.parentNode;
}
return null;
};
Xinha.prototype._getAncestorBlock=function(sel){
var prnt=(Xinha.is_ie?this.createRange(sel).parentElement:this.createRange(sel).commonAncestorContainer);
while(prnt&&(prnt.nodeType==1)){
switch(prnt.tagName.toLowerCase()){
case "div":
case "p":
case "address":
case "blockquote":
case "center":
case "del":
case "ins":
case "pre":
case "h1":
case "h2":
case "h3":
case "h4":
case "h5":
case "h6":
case "h7":
return prnt;
case "body":
case "noframes":
case "dd":
case "li":
case "th":
case "td":
case "noscript":
return null;
default:
break;
}
}
return null;
};
Xinha.prototype._createImplicitBlock=function(type){
var sel=this.getSelection();
if(Xinha.is_ie){
sel.empty();
}else{
sel.collapseToStart();
}
var rng=this.createRange(sel);
};
Xinha.prototype.surroundHTML=function(_153,_154){
var html=this.getSelectedHTML();
this.insertHTML(_153+html+_154);
};
Xinha.prototype.hasSelectedText=function(){
return this.getSelectedHTML()!=="";
};
Xinha.prototype._comboSelected=function(el,txt){
this.focusEditor();
var _158=el.options[el.selectedIndex].value;
switch(txt){
case "fontname":
case "fontsize":
this.execCommand(txt,false,_158);
break;
case "formatblock":
if(!_158){
this.updateToolbar();
break;
}
if(!Xinha.is_gecko||_158!=="blockquote"){
_158="<"+_158+">";
}
this.execCommand(txt,false,_158);
break;
default:
var _159=this.config.customSelects[txt];
if(typeof _159!="undefined"){
_159.action(this);
}else{
alert("FIXME: combo box "+txt+" not implemented");
}
break;
}
};
Xinha.prototype._colorSelector=function(_15a){
var _15b=this;
if(Xinha.is_gecko){
try{
_15b._doc.execCommand("useCSS",false,false);
_15b._doc.execCommand("styleWithCSS",false,true);
}
catch(ex){
}
}
var btn=_15b._toolbarObjects[_15a].element;
var _15d;
if(_15a=="hilitecolor"){
if(Xinha.is_ie){
_15a="backcolor";
_15d=Xinha._colorToRgb(_15b._doc.queryCommandValue("backcolor"));
}else{
_15d=Xinha._colorToRgb(_15b._doc.queryCommandValue("hilitecolor"));
}
}else{
_15d=Xinha._colorToRgb(_15b._doc.queryCommandValue("forecolor"));
}
var _15e=function(_15f){
_15b._doc.execCommand(_15a,false,_15f);
};
if(Xinha.is_ie){
var _160=_15b.createRange(_15b.getSelection());
_15e=function(_161){
_160.select();
_15b._doc.execCommand(_15a,false,_161);
};
}
var _162=new Xinha.colorPicker({cellsize:_15b.config.colorPickerCellSize,callback:_15e,granularity:_15b.config.colorPickerGranularity,websafe:_15b.config.colorPickerWebSafe,savecolors:_15b.config.colorPickerSaveColors});
_162.open(_15b.config.colorPickerPosition,btn,_15d);
};
Xinha.prototype.execCommand=function(_163,UI,_165){
var _166=this;
this.focusEditor();
_163=_163.toLowerCase();
if(this.firePluginEvent("onExecCommand",_163,UI,_165)){
this.updateToolbar();
return false;
}
switch(_163){
case "htmlmode":
this.setMode();
break;
case "hilitecolor":
case "forecolor":
this._colorSelector(_163);
break;
case "createlink":
this._createLink();
break;
case "undo":
case "redo":
if(this._customUndo){
this[_163]();
}else{
this._doc.execCommand(_163,UI,_165);
}
break;
case "inserttable":
this._insertTable();
break;
case "insertimage":
this._insertImage();
break;
case "about":
this._popupDialog(_166.config.URIs.about,null,this);
break;
case "showhelp":
this._popupDialog(_166.config.URIs.help,null,this);
break;
case "killword":
this._wordClean();
break;
case "cut":
case "copy":
case "paste":
this._doc.execCommand(_163,UI,_165);
if(this.config.killWordOnPaste){
this._wordClean();
}
break;
case "lefttoright":
case "righttoleft":
if(this.config.changeJustifyWithDirection){
this._doc.execCommand((_163=="righttoleft")?"justifyright":"justifyleft",UI,_165);
}
var dir=(_163=="righttoleft")?"rtl":"ltr";
var el=this.getParentElement();
while(el&&!Xinha.isBlockElement(el)){
el=el.parentNode;
}
if(el){
if(el.style.direction==dir){
el.style.direction="";
}else{
el.style.direction=dir;
}
}
break;
case "justifyleft":
case "justifyright":
_163.match(/^justify(.*)$/);
var ae=this.activeElement(this.getSelection());
if(ae&&ae.tagName.toLowerCase()=="img"){
ae.align=ae.align==RegExp.$1?"":RegExp.$1;
}else{
this._doc.execCommand(_163,UI,_165);
}
break;
default:
try{
this._doc.execCommand(_163,UI,_165);
}
catch(ex){
if(this.config.debug){
alert(ex+"\n\nby execCommand("+_163+");");
}
}
break;
}
this.updateToolbar();
return false;
};
Xinha.prototype._editorEvent=function(ev){
var _16b=this;
if(typeof _16b._textArea["on"+ev.type]=="function"){
_16b._textArea["on"+ev.type]();
}
if(this.isKeyEvent(ev)){
if(_16b.firePluginEvent("onKeyPress",ev)){
return false;
}
if(this.isShortCut(ev)){
this._shortCuts(ev);
}
}
if(ev.type=="mousedown"){
if(_16b.firePluginEvent("onMouseDown",ev)){
return false;
}
}
if(_16b._timerToolbar){
clearTimeout(_16b._timerToolbar);
}
_16b._timerToolbar=setTimeout(function(){
_16b.updateToolbar();
_16b._timerToolbar=null;
},250);
};
Xinha.prototype._shortCuts=function(ev){
var key=this.getKey(ev).toLowerCase();
var cmd=null;
var _16f=null;
switch(key){
case "b":
cmd="bold";
break;
case "i":
cmd="italic";
break;
case "u":
cmd="underline";
break;
case "s":
cmd="strikethrough";
break;
case "l":
cmd="justifyleft";
break;
case "e":
cmd="justifycenter";
break;
case "r":
cmd="justifyright";
break;
case "j":
cmd="justifyfull";
break;
case "z":
cmd="undo";
break;
case "y":
cmd="redo";
break;
case "v":
cmd="paste";
break;
case "n":
cmd="formatblock";
_16f="p";
break;
case "0":
cmd="killword";
break;
case "1":
case "2":
case "3":
case "4":
case "5":
case "6":
cmd="formatblock";
_16f="h"+key;
break;
}
if(cmd){
this.execCommand(cmd,false,_16f);
Xinha._stopEvent(ev);
}
};
Xinha.prototype.convertNode=function(el,_171){
var _172=this._doc.createElement(_171);
while(el.firstChild){
_172.appendChild(el.firstChild);
}
return _172;
};
Xinha.prototype.scrollToElement=function(e){
if(!e){
e=this.getParentElement();
if(!e){
return;
}
}
var _174=Xinha.getElementTopLeft(e);
this._iframe.contentWindow.scrollTo(_174.left,_174.top);
};
Xinha.prototype.getEditorContent=function(){
return this.outwardHtml(this.getHTML());
};
Xinha.prototype.setEditorContent=function(html){
this.setHTML(this.inwardHtml(html));
};
Xinha.prototype.getHTML=function(){
var html="";
switch(this._editMode){
case "wysiwyg":
if(!this.config.fullPage){
html=Xinha.getHTML(this._doc.body,false,this).trim();
}else{
html=this.doctype+"\n"+Xinha.getHTML(this._doc.documentElement,true,this);
}
break;
case "textmode":
html=this._textArea.value;
break;
default:
alert("Mode <"+this._editMode+"> not defined!");
return false;
}
return html;
};
Xinha.prototype.outwardHtml=function(html){
for(var i in this.plugins){
var _179=this.plugins[i].instance;
if(_179&&typeof _179.outwardHtml=="function"){
html=_179.outwardHtml(html);
}
}
html=html.replace(/<(\/?)b(\s|>|\/)/ig,"<$1strong$2");
html=html.replace(/<(\/?)i(\s|>|\/)/ig,"<$1em$2");
html=html.replace(/<(\/?)strike(\s|>|\/)/ig,"<$1del$2");
html=html.replace(/(<[^>]*onclick=['"])if\(window\.top &amp;&amp; window\.top\.Xinha\)\{return false\}/gi,"$1");
html=html.replace(/(<[^>]*onmouseover=['"])if\(window\.top &amp;&amp; window\.top\.Xinha\)\{return false\}/gi,"$1");
html=html.replace(/(<[^>]*onmouseout=['"])if\(window\.top &amp;&amp; window\.top\.Xinha\)\{return false\}/gi,"$1");
html=html.replace(/(<[^>]*onmousedown=['"])if\(window\.top &amp;&amp; window\.top\.Xinha\)\{return false\}/gi,"$1");
html=html.replace(/(<[^>]*onmouseup=['"])if\(window\.top &amp;&amp; window\.top\.Xinha\)\{return false\}/gi,"$1");
var _17a=location.href.replace(/(https?:\/\/[^\/]*)\/.*/,"$1")+"/";
html=html.replace(/https?:\/\/null\//g,_17a);
html=html.replace(/((href|src|background)=[\'\"])\/+/ig,"$1"+_17a);
html=this.outwardSpecialReplacements(html);
html=this.fixRelativeLinks(html);
if(this.config.sevenBitClean){
html=html.replace(/[^ -~\r\n\t]/g,function(c){
return "&#"+c.charCodeAt(0)+";";
});
}
html=html.replace(/(<script[^>]*)(freezescript)/gi,"$1javascript");
if(this.config.fullPage){
html=Xinha.stripCoreCSS(html);
}
return html;
};
Xinha.prototype.inwardHtml=function(html){
for(var i in this.plugins){
var _17e=this.plugins[i].instance;
if(_17e&&typeof _17e.inwardHtml=="function"){
html=_17e.inwardHtml(html);
}
}
html=html.replace(/<(\/?)del(\s|>|\/)/ig,"<$1strike$2");
html=html.replace(/(<[^>]*onclick=["'])/gi,"$1if(window.top &amp;&amp; window.top.Xinha){return false}");
html=html.replace(/(<[^>]*onmouseover=["'])/gi,"$1if(window.top &amp;&amp; window.top.Xinha){return false}");
html=html.replace(/(<[^>]*onmouseout=["'])/gi,"$1if(window.top &amp;&amp; window.top.Xinha){return false}");
html=html.replace(/(<[^>]*onmouseodown=["'])/gi,"$1if(window.top &amp;&amp; window.top.Xinha){return false}");
html=html.replace(/(<[^>]*onmouseup=["'])/gi,"$1if(window.top &amp;&amp; window.top.Xinha){return false}");
html=this.inwardSpecialReplacements(html);
html=html.replace(/(<script[^>]*)(javascript)/gi,"$1freezescript");
var _17f=new RegExp("((href|src|background)=['\"])/+","gi");
html=html.replace(_17f,"$1"+location.href.replace(/(https?:\/\/[^\/]*)\/.*/,"$1")+"/");
html=this.fixRelativeLinks(html);
if(this.config.fullPage){
html=Xinha.addCoreCSS(html);
}
return html;
};
Xinha.prototype.outwardSpecialReplacements=function(html){
for(var i in this.config.specialReplacements){
var from=this.config.specialReplacements[i];
var to=i;
if(typeof from.replace!="function"||typeof to.replace!="function"){
continue;
}
var reg=new RegExp(Xinha.escapeStringForRegExp(from),"g");
html=html.replace(reg,to.replace(/\$/g,"$$$$"));
}
return html;
};
Xinha.prototype.inwardSpecialReplacements=function(html){
for(var i in this.config.specialReplacements){
var from=i;
var to=this.config.specialReplacements[i];
if(typeof from.replace!="function"||typeof to.replace!="function"){
continue;
}
var reg=new RegExp(Xinha.escapeStringForRegExp(from),"g");
html=html.replace(reg,to.replace(/\$/g,"$$$$"));
}
return html;
};
Xinha.prototype.fixRelativeLinks=function(html){
if(typeof this.config.expandRelativeUrl!="undefined"&&this.config.expandRelativeUrl){
var src=html.match(/(src|href)="([^"]*)"/gi);
}
var b=document.location.href;
if(src){
var url,url_m,relPath,base_m,absPath;
for(var i=0;i<src.length;++i){
url=src[i].match(/(src|href)="([^"]*)"/i);
url_m=url[2].match(/\.\.\//g);
if(url_m){
relPath=new RegExp("(.*?)(([^/]*/){"+url_m.length+"})[^/]*$");
base_m=b.match(relPath);
absPath=url[2].replace(/(\.\.\/)*/,base_m[1]);
html=html.replace(new RegExp(Xinha.escapeStringForRegExp(url[2])),absPath);
}
}
}
if(typeof this.config.stripSelfNamedAnchors!="undefined"&&this.config.stripSelfNamedAnchors){
var _18f=new RegExp(Xinha.escapeStringForRegExp(document.location.href.replace(/&/g,"&amp;"))+"(#[^'\" ]*)","g");
html=html.replace(_18f,"$1");
}
if(typeof this.config.stripBaseHref!="undefined"&&this.config.stripBaseHref){
var _190=null;
if(typeof this.config.baseHref!="undefined"&&this.config.baseHref!==null){
_190=new RegExp("((href|src|background)=\")("+Xinha.escapeStringForRegExp(this.config.baseHref)+")","g");
}else{
_190=new RegExp("((href|src|background)=\")("+Xinha.escapeStringForRegExp(document.location.href.replace(/^(https?:\/\/[^\/]*)(.*)/,"$1"))+")","g");
}
html=html.replace(_190,"$1");
}
return html;
};
Xinha.prototype.getInnerHTML=function(){
if(!this._doc.body){
return "";
}
var html="";
switch(this._editMode){
case "wysiwyg":
if(!this.config.fullPage){
html=this._doc.body.innerHTML;
}else{
html=this.doctype+"\n"+this._doc.documentElement.innerHTML;
}
break;
case "textmode":
html=this._textArea.value;
break;
default:
alert("Mode <"+this._editMode+"> not defined!");
return false;
}
return html;
};
Xinha.prototype.setHTML=function(html){
if(!this.config.fullPage){
this._doc.body.innerHTML=html;
}else{
this.setFullHTML(html);
}
this._textArea.value=html;
};
Xinha.prototype.setDoctype=function(_193){
this.doctype=_193;
};
Xinha._object=null;
Xinha.cloneObject=function(obj){
if(!obj){
return null;
}
var _195={};
if(obj.constructor.toString().match(/\s*function Array\(/)){
_195=obj.constructor();
}
if(obj.constructor.toString().match(/\s*function Function\(/)){
_195=obj;
}else{
for(var n in obj){
var node=obj[n];
if(typeof node=="object"){
_195[n]=Xinha.cloneObject(node);
}else{
_195[n]=node;
}
}
}
return _195;
};
Xinha.flushEvents=function(){
var x=0;
var e=Xinha._eventFlushers.pop();
while(e){
try{
if(e.length==3){
Xinha._removeEvent(e[0],e[1],e[2]);
x++;
}else{
if(e.length==2){
e[0]["on"+e[1]]=null;
e[0]._xinha_dom0Events[e[1]]=null;
x++;
}
}
}
catch(ex){
}
e=Xinha._eventFlushers.pop();
}
};
Xinha._eventFlushers=[];
if(document.addEventListener){
Xinha._addEvent=function(el,_19b,func){
el.addEventListener(_19b,func,true);
Xinha._eventFlushers.push([el,_19b,func]);
};
Xinha._removeEvent=function(el,_19e,func){
el.removeEventListener(_19e,func,true);
};
Xinha._stopEvent=function(ev){
ev.preventDefault();
ev.stopPropagation();
};
}else{
if(document.attachEvent){
Xinha._addEvent=function(el,_1a2,func){
el.attachEvent("on"+_1a2,func);
Xinha._eventFlushers.push([el,_1a2,func]);
};
Xinha._removeEvent=function(el,_1a5,func){
el.detachEvent("on"+_1a5,func);
};
Xinha._stopEvent=function(ev){
try{
ev.cancelBubble=true;
ev.returnValue=false;
}
catch(ex){
}
};
}else{
Xinha._addEvent=function(el,_1a9,func){
alert("_addEvent is not supported");
};
Xinha._removeEvent=function(el,_1ac,func){
alert("_removeEvent is not supported");
};
Xinha._stopEvent=function(ev){
alert("_stopEvent is not supported");
};
}
}
Xinha._addEvents=function(el,evs,func){
for(var i=evs.length;--i>=0;){
Xinha._addEvent(el,evs[i],func);
}
};
Xinha._removeEvents=function(el,evs,func){
for(var i=evs.length;--i>=0;){
Xinha._removeEvent(el,evs[i],func);
}
};
Xinha.addDom0Event=function(el,ev,fn){
Xinha._prepareForDom0Events(el,ev);
el._xinha_dom0Events[ev].unshift(fn);
};
Xinha.prependDom0Event=function(el,ev,fn){
Xinha._prepareForDom0Events(el,ev);
el._xinha_dom0Events[ev].push(fn);
};
Xinha._prepareForDom0Events=function(el,ev){
if(typeof el._xinha_dom0Events=="undefined"){
el._xinha_dom0Events={};
Xinha.freeLater(el,"_xinha_dom0Events");
}
if(typeof el._xinha_dom0Events[ev]=="undefined"){
el._xinha_dom0Events[ev]=[];
if(typeof el["on"+ev]=="function"){
el._xinha_dom0Events[ev].push(el["on"+ev]);
}
el["on"+ev]=function(_1bf){
var a=el._xinha_dom0Events[ev];
var _1c1=true;
for(var i=a.length;--i>=0;){
el._xinha_tempEventHandler=a[i];
if(el._xinha_tempEventHandler(_1bf)===false){
el._xinha_tempEventHandler=null;
_1c1=false;
break;
}
el._xinha_tempEventHandler=null;
}
return _1c1;
};
Xinha._eventFlushers.push([el,ev]);
}
};
Xinha.prototype.notifyOn=function(ev,fn){
if(typeof this._notifyListeners[ev]=="undefined"){
this._notifyListeners[ev]=[];
Xinha.freeLater(this,"_notifyListeners");
}
this._notifyListeners[ev].push(fn);
};
Xinha.prototype.notifyOf=function(ev,args){
if(this._notifyListeners[ev]){
for(var i=0;i<this._notifyListeners[ev].length;i++){
this._notifyListeners[ev][i](ev,args);
}
}
};
Xinha._blockTags=" body form textarea fieldset ul ol dl li div "+"p h1 h2 h3 h4 h5 h6 quote pre table thead "+"tbody tfoot tr td th iframe address blockquote ";
Xinha.isBlockElement=function(el){
return el&&el.nodeType==1&&(Xinha._blockTags.indexOf(" "+el.tagName.toLowerCase()+" ")!=-1);
};
Xinha._paraContainerTags=" body td th caption fieldset div";
Xinha.isParaContainer=function(el){
return el&&el.nodeType==1&&(Xinha._paraContainerTags.indexOf(" "+el.tagName.toLowerCase()+" ")!=-1);
};
Xinha._closingTags=" a abbr acronym address applet b bdo big blockquote button caption center cite code del dfn dir div dl em fieldset font form frameset h1 h2 h3 h4 h5 h6 i iframe ins kbd label legend map menu noframes noscript object ol optgroup pre q s samp script select small span strike strong style sub sup table textarea title tt u ul var ";
Xinha.needsClosingTag=function(el){
return el&&el.nodeType==1&&(Xinha._closingTags.indexOf(" "+el.tagName.toLowerCase()+" ")!=-1);
};
Xinha.htmlEncode=function(str){
if(typeof str.replace=="undefined"){
str=str.toString();
}
str=str.replace(/&/ig,"&amp;");
str=str.replace(/</ig,"&lt;");
str=str.replace(/>/ig,"&gt;");
str=str.replace(/\xA0/g,"&nbsp;");
str=str.replace(/\x22/g,"&quot;");
return str;
};
Xinha.prototype.stripBaseURL=function(_1cc){
if(this.config.baseHref===null||!this.config.stripBaseHref){
return _1cc;
}
var _1cd=this.config.baseHref.replace(/^(https?:\/\/[^\/]+)(.*)$/,"$1");
var _1ce=new RegExp(_1cd);
return _1cc.replace(_1ce,"");
};
String.prototype.trim=function(){
return this.replace(/^\s+/,"").replace(/\s+$/,"");
};
Xinha._makeColor=function(v){
if(typeof v!="number"){
return v;
}
var r=v&255;
var g=(v>>8)&255;
var b=(v>>16)&255;
return "rgb("+r+","+g+","+b+")";
};
Xinha._colorToRgb=function(v){
if(!v){
return "";
}
var r,g,b;
function hex(d){
return (d<16)?("0"+d.toString(16)):d.toString(16);
}
if(typeof v=="number"){
r=v&255;
g=(v>>8)&255;
b=(v>>16)&255;
return "#"+hex(r)+hex(g)+hex(b);
}
if(v.substr(0,3)=="rgb"){
var re=/rgb\s*\(\s*([0-9]+)\s*,\s*([0-9]+)\s*,\s*([0-9]+)\s*\)/;
if(v.match(re)){
r=parseInt(RegExp.$1,10);
g=parseInt(RegExp.$2,10);
b=parseInt(RegExp.$3,10);
return "#"+hex(r)+hex(g)+hex(b);
}
return null;
}
if(v.substr(0,1)=="#"){
return v;
}
return null;
};
Xinha.prototype._popupDialog=function(url,_1d8,init){
Dialog(this.popupURL(url),_1d8,init);
};
Xinha.prototype.imgURL=function(file,_1db){
if(typeof _1db=="undefined"){
return _editor_url+file;
}else{
return _editor_url+"plugins/"+_1db+"/img/"+file;
}
};
Xinha.prototype.popupURL=function(file){
var url="";
if(file.match(/^plugin:\/\/(.*?)\/(.*)/)){
var _1de=RegExp.$1;
var _1df=RegExp.$2;
if(!(/\.html$/.test(_1df))){
_1df+=".html";
}
url=_editor_url+"plugins/"+_1de+"/popups/"+_1df;
}else{
if(file.match(/^\/.*?/)){
url=file;
}else{
url=_editor_url+this.config.popupURL+file;
}
}
return url;
};
Xinha.getElementById=function(tag,id){
var el,i,objs=document.getElementsByTagName(tag);
for(i=objs.length;--i>=0&&(el=objs[i]);){
if(el.id==id){
return el;
}
}
return null;
};
Xinha.prototype._toggleBorders=function(){
var _1e3=this._doc.getElementsByTagName("TABLE");
if(_1e3.length!==0){
if(!this.borders){
this.borders=true;
}else{
this.borders=false;
}
for(var i=0;i<_1e3.length;i++){
if(this.borders){
Xinha._addClass(_1e3[i],"htmtableborders");
}else{
Xinha._removeClass(_1e3[i],"htmtableborders");
}
}
}
return true;
};
Xinha.addCoreCSS=function(html){
var _1e6="<style title=\"Xinha Internal CSS\" type=\"text/css\">"+".htmtableborders, .htmtableborders td, .htmtableborders th {border : 1px dashed lightgrey ! important;}\n"+"html, body { border: 0px; } \n"+"body { background-color: #ffffff; } \n"+"</style>\n";
if(html&&/<head>/i.test(html)){
return html.replace(/<head>/i,"<head>"+_1e6);
}else{
if(html){
return _1e6+html;
}else{
return _1e6;
}
}
};
Xinha.stripCoreCSS=function(html){
return html.replace(/<style[^>]+title="Xinha Internal CSS"(.|\n)*?<\/style>/i,"");
};
Xinha._removeClass=function(el,_1e9){
if(!(el&&el.className)){
return;
}
var cls=el.className.split(" ");
var ar=[];
for(var i=cls.length;i>0;){
if(cls[--i]!=_1e9){
ar[ar.length]=cls[i];
}
}
el.className=ar.join(" ");
};
Xinha._addClass=function(el,_1ee){
Xinha._removeClass(el,_1ee);
el.className+=" "+_1ee;
};
Xinha.addClasses=function(el,_1f0){
if(el!==null){
var _1f1=el.className.trim().split(" ");
var ours=_1f0.split(" ");
for(var x=0;x<ours.length;x++){
var _1f4=false;
for(var i=0;_1f4===false&&i<_1f1.length;i++){
if(_1f1[i]==ours[x]){
_1f4=true;
}
}
if(_1f4===false){
_1f1[_1f1.length]=ours[x];
}
}
el.className=_1f1.join(" ").trim();
}
};
Xinha.removeClasses=function(el,_1f7){
var _1f8=el.className.trim().split();
var _1f9=[];
var _1fa=_1f7.trim().split();
for(var i=0;i<_1f8.length;i++){
var _1fc=false;
for(var x=0;x<_1fa.length&&!_1fc;x++){
if(_1f8[i]==_1fa[x]){
_1fc=true;
}
}
if(!_1fc){
_1f9[_1f9.length]=_1f8[i];
}
}
return _1f9.join(" ");
};
Xinha.addClass=Xinha._addClass;
Xinha.removeClass=Xinha._removeClass;
Xinha._addClasses=Xinha.addClasses;
Xinha._removeClasses=Xinha.removeClasses;
Xinha._hasClass=function(el,_1ff){
if(!(el&&el.className)){
return false;
}
var cls=el.className.split(" ");
for(var i=cls.length;i>0;){
if(cls[--i]==_1ff){
return true;
}
}
return false;
};
Xinha._postback=function(url,data,_204){
var req=null;
req=Xinha.getXMLHTTPRequestObject();
var _206="";
if(typeof data=="string"){
_206=data;
}else{
if(typeof data=="object"){
for(var i in data){
_206+=(_206.length?"&":"")+i+"="+encodeURIComponent(data[i]);
}
}
}
function callBack(){
if(req.readyState==4){
if(req.status==200||Xinha.isRunLocally&&req.status==0){
if(typeof _204=="function"){
_204(req.responseText,req);
}
}else{
alert("An error has occurred: "+req.statusText+"\nURL: "+url);
}
}
}
req.onreadystatechange=callBack;
req.open("POST",url,true);
req.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
req.send(_206);
};
Xinha._getback=function(url,_209){
var req=null;
req=Xinha.getXMLHTTPRequestObject();
function callBack(){
if(req.readyState==4){
if(req.status==200||Xinha.isRunLocally&&req.status==0){
_209(req.responseText,req);
}else{
alert("An error has occurred: "+req.statusText+"\nURL: "+url);
}
}
}
req.onreadystatechange=callBack;
req.open("GET",url,true);
req.send(null);
};
Xinha._geturlcontent=function(url){
var req=null;
req=Xinha.getXMLHTTPRequestObject();
req.open("GET",url,false);
req.send(null);
if(req.status==200||Xinha.isRunLocally&&req.status==0){
return req.responseText;
}else{
return "";
}
};
if(typeof dump=="undefined"){
function dump(o){
var s="";
for(var prop in o){
s+=prop+" = "+o[prop]+"\n";
}
var x=window.open("","debugger");
x.document.write("<pre>"+s+"</pre>");
}
}
if(!Array.prototype.contains){
Array.prototype.contains=function(_211){
var _212=this;
for(var i=0;i<_212.length;i++){
if(_211==_212[i]){
return true;
}
}
return false;
};
}
if(!Array.prototype.indexOf){
Array.prototype.indexOf=function(_214){
var _215=this;
for(var i=0;i<_215.length;i++){
if(_214==_215[i]){
return i;
}
}
return null;
};
}
if(!Array.prototype.append){
Array.prototype.append=function(a){
for(var i=0;i<a.length;i++){
this.push(a[i]);
}
return this;
};
}
Xinha.arrayContainsArray=function(a1,a2){
var _21b=true;
for(var x=0;x<a2.length;x++){
var _21d=false;
for(var i=0;i<a1.length;i++){
if(a1[i]==a2[x]){
_21d=true;
break;
}
}
if(!_21d){
_21b=false;
break;
}
}
return _21b;
};
Xinha.arrayFilter=function(a1,_220){
var _221=[];
for(var x=0;x<a1.length;x++){
if(_220(a1[x])){
_221[_221.length]=a1[x];
}
}
return _221;
};
Xinha.collectionToArray=function(_223){
var _224=[];
for(var i=0;i<_223.length;i++){
_224.push(_223.item(i));
}
return _224;
};
Xinha.uniq_count=0;
Xinha.uniq=function(_226){
return _226+Xinha.uniq_count++;
};
Xinha._loadlang=function(_227,url){
var lang;
if(typeof _editor_lcbackend=="string"){
url=_editor_lcbackend;
url=url.replace(/%lang%/,_editor_lang);
url=url.replace(/%context%/,_227);
}else{
if(!url){
if(_227!="Xinha"){
url=_editor_url+"plugins/"+_227+"/lang/"+_editor_lang+".js";
}else{
Xinha.setLoadingMessage("Loading language");
url=_editor_url+"lang/"+_editor_lang+".js";
}
}
}
var _22a=Xinha._geturlcontent(url);
if(_22a!==""){
try{
eval("lang = "+_22a);
}
catch(ex){
alert("Error reading Language-File ("+url+"):\n"+Error.toString());
lang={};
}
}else{
lang={};
}
return lang;
};
Xinha._lc=function(_22b,_22c,_22d){
var url,ret;
if(typeof _22c=="object"&&_22c.url&&_22c.context){
url=_22c.url+_editor_lang+".js";
_22c=_22c.context;
}
var m=null;
if(typeof _22b=="string"){
m=_22b.match(/\$(.*?)=(.*?)\$/g);
}
if(m){
if(!_22d){
_22d={};
}
for(var i=0;i<m.length;i++){
var n=m[i].match(/\$(.*?)=(.*?)\$/);
_22d[n[1]]=n[2];
_22b=_22b.replace(n[0],"$"+n[1]);
}
}
if(_editor_lang=="en"){
if(typeof _22b=="object"&&_22b.string){
ret=_22b.string;
}else{
ret=_22b;
}
}else{
if(typeof Xinha._lc_catalog=="undefined"){
Xinha._lc_catalog=[];
}
if(typeof _22c=="undefined"){
_22c="Xinha";
}
if(typeof Xinha._lc_catalog[_22c]=="undefined"){
Xinha._lc_catalog[_22c]=Xinha._loadlang(_22c,url);
}
var key;
if(typeof _22b=="object"&&_22b.key){
key=_22b.key;
}else{
if(typeof _22b=="object"&&_22b.string){
key=_22b.string;
}else{
key=_22b;
}
}
if(typeof Xinha._lc_catalog[_22c][key]=="undefined"){
if(_22c=="Xinha"){
if(typeof _22b=="object"&&_22b.string){
ret=_22b.string;
}else{
ret=_22b;
}
}else{
return Xinha._lc(_22b,"Xinha",_22d);
}
}else{
ret=Xinha._lc_catalog[_22c][key];
}
}
if(typeof _22b=="object"&&_22b.replace){
_22d=_22b.replace;
}
if(typeof _22d!="undefined"){
for(var i in _22d){
ret=ret.replace("$"+i,_22d[i]);
}
}
return ret;
};
Xinha.hasDisplayedChildren=function(el){
var _234=el.childNodes;
for(var i=0;i<_234.length;i++){
if(_234[i].tagName){
if(_234[i].style.display!="none"){
return true;
}
}
}
return false;
};
Xinha._loadback=function(url,_237,_238,_239){
if(document.getElementById(url)){
return true;
}
var t=!Xinha.is_ie?"onload":"onreadystatechange";
var s=document.createElement("script");
s.type="text/javascript";
s.src=url;
s.id=url;
if(_237){
s[t]=function(){
if(Xinha.is_ie&&(!(/loaded|complete/.test(window.event.srcElement.readyState)))){
return;
}
_237.call(_238?_238:this,_239);
s[t]=null;
};
}
document.getElementsByTagName("head")[0].appendChild(s);
return false;
};
Xinha.makeEditors=function(_23c,_23d,_23e){
if(!Xinha.isSupportedBrowser){
return;
}
if(typeof _23d=="function"){
_23d=_23d();
}
var _23f={};
for(var x=0;x<_23c.length;x++){
if(typeof _23c[x]!="object"){
var _241=Xinha.getElementById("textarea",_23c[x]);
if(!_241){
continue;
}
}
var _242=new Xinha(_241,Xinha.cloneObject(_23d));
_242.registerPlugins(_23e);
_23f[_23c[x]]=_242;
}
return _23f;
};
Xinha.startEditors=function(_243){
if(!Xinha.isSupportedBrowser){
return;
}
for(var i in _243){
if(_243[i].generate){
_243[i].generate();
}
}
};
Xinha.prototype.registerPlugins=function(_245){
if(!Xinha.isSupportedBrowser){
return;
}
if(_245){
for(var i=0;i<_245.length;i++){
this.setLoadingMessage(Xinha._lc("Register plugin $plugin","Xinha",{"plugin":_245[i]}));
this.registerPlugin(_245[i]);
}
}
};
Xinha.base64_encode=function(_247){
var _248="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
var _249="";
var chr1,chr2,chr3;
var enc1,enc2,enc3,enc4;
var i=0;
do{
chr1=_247.charCodeAt(i++);
chr2=_247.charCodeAt(i++);
chr3=_247.charCodeAt(i++);
enc1=chr1>>2;
enc2=((chr1&3)<<4)|(chr2>>4);
enc3=((chr2&15)<<2)|(chr3>>6);
enc4=chr3&63;
if(isNaN(chr2)){
enc3=enc4=64;
}else{
if(isNaN(chr3)){
enc4=64;
}
}
_249=_249+_248.charAt(enc1)+_248.charAt(enc2)+_248.charAt(enc3)+_248.charAt(enc4);
}while(i<_247.length);
return _249;
};
Xinha.base64_decode=function(_24d){
var _24e="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
var _24f="";
var chr1,chr2,chr3;
var enc1,enc2,enc3,enc4;
var i=0;
_24d=_24d.replace(/[^A-Za-z0-9\+\/\=]/g,"");
do{
enc1=_24e.indexOf(_24d.charAt(i++));
enc2=_24e.indexOf(_24d.charAt(i++));
enc3=_24e.indexOf(_24d.charAt(i++));
enc4=_24e.indexOf(_24d.charAt(i++));
chr1=(enc1<<2)|(enc2>>4);
chr2=((enc2&15)<<4)|(enc3>>2);
chr3=((enc3&3)<<6)|enc4;
_24f=_24f+String.fromCharCode(chr1);
if(enc3!=64){
_24f=_24f+String.fromCharCode(chr2);
}
if(enc4!=64){
_24f=_24f+String.fromCharCode(chr3);
}
}while(i<_24d.length);
return _24f;
};
Xinha.removeFromParent=function(el){
if(!el.parentNode){
return;
}
var pN=el.parentNode;
pN.removeChild(el);
return el;
};
Xinha.hasParentNode=function(el){
if(el.parentNode){
if(el.parentNode.nodeType==11){
return false;
}
return true;
}
return false;
};
Xinha.viewportSize=function(_256){
_256=(_256)?_256:window;
var x,y;
if(_256.innerHeight){
x=_256.innerWidth;
y=_256.innerHeight;
}else{
if(_256.document.documentElement&&_256.document.documentElement.clientHeight){
x=_256.document.documentElement.clientWidth;
y=_256.document.documentElement.clientHeight;
}else{
if(_256.document.body){
x=_256.document.body.clientWidth;
y=_256.document.body.clientHeight;
}
}
}
return {"x":x,"y":y};
};
Xinha.pageSize=function(_258){
_258=(_258)?_258:window;
var x,y;
var _25a=_258.document.body.scrollHeight;
var _25b=_258.document.documentElement.scrollHeight;
if(_25a>_25b){
x=_258.document.body.scrollWidth;
y=_258.document.body.scrollHeight;
}else{
x=_258.document.documentElement.scrollWidth;
y=_258.document.documentElement.scrollHeight;
}
return {"x":x,"y":y};
};
Xinha.prototype.scrollPos=function(_25c){
_25c=(_25c)?_25c:window;
var x,y;
if(_25c.pageYOffset){
x=_25c.pageXOffset;
y=_25c.pageYOffset;
}else{
if(_25c.document.documentElement&&document.documentElement.scrollTop){
x=_25c.document.documentElement.scrollLeft;
y=_25c.document.documentElement.scrollTop;
}else{
if(_25c.document.body){
x=_25c.document.body.scrollLeft;
y=_25c.document.body.scrollTop;
}
}
}
return {"x":x,"y":y};
};
Xinha.getElementTopLeft=function(_25e){
var _25f=curtop=0;
if(_25e.offsetParent){
_25f=_25e.offsetLeft;
curtop=_25e.offsetTop;
while(_25e=_25e.offsetParent){
_25f+=_25e.offsetLeft;
curtop+=_25e.offsetTop;
}
}
return {top:curtop,left:_25f};
};
Xinha.findPosX=function(obj){
var _261=0;
if(obj.offsetParent){
return Xinha.getElementTopLeft(obj).left;
}else{
if(obj.x){
_261+=obj.x;
}
}
return _261;
};
Xinha.findPosY=function(obj){
var _263=0;
if(obj.offsetParent){
return Xinha.getElementTopLeft(obj).top;
}else{
if(obj.y){
_263+=obj.y;
}
}
return _263;
};
Xinha.createLoadingMessages=function(_264){
if(Xinha.loadingMessages||!Xinha.isSupportedBrowser){
return;
}
Xinha.loadingMessages=[];
for(var i=0;i<_264.length;i++){
Xinha.loadingMessages.push(Xinha.createLoadingMessage(Xinha.getElementById("textarea",_264[i])));
}
};
Xinha.createLoadingMessage=function(_266,text){
if(document.getElementById("loading_"+_266.id)||!Xinha.isSupportedBrowser){
return;
}
var _268=document.createElement("div");
_268.id="loading_"+_266.id;
_268.className="loading";
_268.style.left=Xinha.findPosX(_266)+"px";
_268.style.top=(Xinha.findPosY(_266)+_266.offsetHeight/2)-50+"px";
_268.style.width=_266.offsetWidth+"px";
var _269=document.createElement("div");
_269.className="loading_main";
_269.id="loading_main_"+_266.id;
_269.appendChild(document.createTextNode(Xinha._lc("Loading in progress. Please wait!")));
var _26a=document.createElement("div");
_26a.className="loading_sub";
_26a.id="loading_sub_"+_266.id;
text=text?text:Xinha._lc("Constructing object");
_26a.appendChild(document.createTextNode(text));
_268.appendChild(_269);
_268.appendChild(_26a);
document.body.appendChild(_268);
Xinha.freeLater(_268);
Xinha.freeLater(_269);
Xinha.freeLater(_26a);
return _26a;
};
Xinha.prototype.setLoadingMessage=function(_26b,_26c){
if(!document.getElementById("loading_sub_"+this._textArea.id)){
return;
}
document.getElementById("loading_main_"+this._textArea.id).innerHTML=_26c?_26c:Xinha._lc("Loading in progress. Please wait!");
document.getElementById("loading_sub_"+this._textArea.id).innerHTML=_26b;
};
Xinha.setLoadingMessage=function(_26d){
if(!Xinha.loadingMessages){
return;
}
for(var i=0;i<Xinha.loadingMessages.length;i++){
Xinha.loadingMessages[i].innerHTML=_26d;
}
};
Xinha.prototype.removeLoadingMessage=function(){
if(document.getElementById("loading_"+this._textArea.id)){
document.body.removeChild(document.getElementById("loading_"+this._textArea.id));
}
};
Xinha.removeLoadingMessages=function(_26f){
for(var i=0;i<_26f.length;i++){
var main=document.getElementById("loading_"+document.getElementById(_26f[i]).id);
main.parentNode.removeChild(main);
}
Xinha.loadingMessages=null;
};
Xinha.toFree=[];
Xinha.freeLater=function(obj,prop){
Xinha.toFree.push({o:obj,p:prop});
};
Xinha.free=function(obj,prop){
if(obj&&!prop){
for(var p in obj){
Xinha.free(obj,p);
}
}else{
if(obj){
try{
obj[prop]=null;
}
catch(x){
}
}
}
};
Xinha.collectGarbageForIE=function(){
Xinha.flushEvents();
for(var x=0;x<Xinha.toFree.length;x++){
Xinha.free(Xinha.toFree[x].o,Xinha.toFree[x].p);
Xinha.toFree[x].o=null;
}
};
Xinha.prototype.insertNodeAtSelection=function(_278){
Xinha.notImplemented("insertNodeAtSelection");
};
Xinha.prototype.getParentElement=function(sel){
Xinha.notImplemented("getParentElement");
};
Xinha.prototype.activeElement=function(sel){
Xinha.notImplemented("activeElement");
};
Xinha.prototype.selectionEmpty=function(sel){
Xinha.notImplemented("selectionEmpty");
};
Xinha.prototype.saveSelection=function(){
Xinha.notImplemented("saveSelection");
};
Xinha.prototype.restoreSelection=function(_27c){
Xinha.notImplemented("restoreSelection");
};
Xinha.prototype.selectNodeContents=function(node,pos){
Xinha.notImplemented("selectNodeContents");
};
Xinha.prototype.insertHTML=function(html){
Xinha.notImplemented("insertHTML");
};
Xinha.prototype.getSelectedHTML=function(){
Xinha.notImplemented("getSelectedHTML");
};
Xinha.prototype.getSelection=function(){
Xinha.notImplemented("getSelection");
};
Xinha.prototype.createRange=function(sel){
Xinha.notImplemented("createRange");
};
Xinha.prototype.isKeyEvent=function(_281){
Xinha.notImplemented("isKeyEvent");
};
Xinha.prototype.isShortCut=function(_282){
if(_282.ctrlKey&&!_282.altKey){
return true;
}
return false;
};
Xinha.prototype.getKey=function(_283){
Xinha.notImplemented("getKey");
};
Xinha.getOuterHTML=function(_284){
Xinha.notImplemented("getOuterHTML");
};
Xinha.getXMLHTTPRequestObject=function(){
try{
if(typeof XMLHttpRequest=="function"){
return new XMLHttpRequest();
}else{
if(typeof ActiveXObject=="function"){
return new ActiveXObject("Microsoft.XMLHTTP");
}
}
}
catch(e){
Xinha.notImplemented("getXMLHTTPRequestObject");
}
};
Xinha.prototype._activeElement=function(sel){
return this.activeElement(sel);
};
Xinha.prototype._selectionEmpty=function(sel){
return this.selectionEmpty(sel);
};
Xinha.prototype._getSelection=function(){
return this.getSelection();
};
Xinha.prototype._createRange=function(sel){
return this.createRange(sel);
};
HTMLArea=Xinha;
Xinha.init();
if(Xinha.is_ie){
Xinha.addDom0Event(window,"unload",Xinha.collectGarbageForIE);
}
Xinha.notImplemented=function(_288){
throw new Error("Method Not Implemented","Part of Xinha has tried to call the "+_288+" method which has not been implemented.");
};

