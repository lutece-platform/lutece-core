Gecko._pluginInfo={name:"Gecko",origin:"Xinha Core",version:"$LastChangedRevision: 682 $".replace(/^[^:]*: (.*) \$$/,"$1"),developer:"The Xinha Core Developer Team",developer_url:"$HeadURL: http://svn.xinha.python-hosting.com/trunk/functionsMozilla.js $".replace(/^[^:]*: (.*) \$$/,"$1"),license:"htmlArea"};
function Gecko(_1){
this.editor=_1;
_1.Gecko=this;
}
Gecko.prototype.onKeyPress=function(ev){
var _3=this.editor;
var s=_3.getSelection();
if(_3.isShortCut(ev)){
switch(_3.getKey(ev).toLowerCase()){
case "z":
if(_3._unLink&&_3._unlinkOnUndo){
Xinha._stopEvent(ev);
_3._unLink();
_3.updateToolbar();
return true;
}
break;
case "a":
sel=_3.getSelection();
sel.removeAllRanges();
range=_3.createRange();
range.selectNodeContents(_3._doc.body);
sel.addRange(range);
Xinha._stopEvent(ev);
return true;
break;
case "v":
if(!_3.config.htmlareaPaste){
return true;
}
break;
}
}
switch(_3.getKey(ev)){
case " ":
var _5=function(_6,_7){
var _8=_6.nextSibling;
if(typeof _7=="string"){
_7=_3._doc.createElement(_7);
}
var a=_6.parentNode.insertBefore(_7,_8);
Xinha.removeFromParent(_6);
a.appendChild(_6);
_8.data=" "+_8.data;
s.collapse(_8,1);
_3._unLink=function(){
var t=a.firstChild;
a.removeChild(t);
a.parentNode.insertBefore(t,a);
Xinha.removeFromParent(a);
_3._unLink=null;
_3._unlinkOnUndo=false;
};
_3._unlinkOnUndo=true;
return a;
};
if(_3.config.convertUrlsToLinks&&s&&s.isCollapsed&&s.anchorNode.nodeType==3&&s.anchorNode.data.length>3&&s.anchorNode.data.indexOf(".")>=0){
var _b=s.anchorNode.data.substring(0,s.anchorOffset).search(/\S{4,}$/);
if(_b==-1){
break;
}
if(_3._getFirstAncestor(s,"a")){
break;
}
var _c=s.anchorNode.data.substring(0,s.anchorOffset).replace(/^.*?(\S*)$/,"$1");
var _d=_c.match(Xinha.RE_email);
if(_d){
var _e=s.anchorNode;
var _f=_e.splitText(s.anchorOffset);
var _10=_e.splitText(_b);
_5(_10,"a").href="mailto:"+_d[0];
break;
}
RE_date=/([0-9]+\.)+/;
RE_ip=/(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/;
var _11=_c.match(Xinha.RE_url);
if(_11){
if(RE_date.test(_c)){
if(!RE_ip.test(_c)){
break;
}
}
var _12=s.anchorNode;
var _13=_12.splitText(s.anchorOffset);
var _14=_12.splitText(_b);
_5(_14,"a").href=(_11[1]?_11[1]:"http://")+_11[2];
break;
}
}
break;
}
switch(ev.keyCode){
case 13:
if(!ev.shiftKey&&_3.config.mozParaHandler=="dirty"){
this.dom_checkInsertP();
Xinha._stopEvent(ev);
}
break;
case 27:
if(_3._unLink){
_3._unLink();
Xinha._stopEvent(ev);
}
break;
break;
case 8:
case 46:
if(!ev.shiftKey&&this.handleBackspace()){
Xinha._stopEvent(ev);
}
default:
_3._unlinkOnUndo=false;
if(s.anchorNode&&s.anchorNode.nodeType==3){
var a=_3._getFirstAncestor(s,"a");
if(!a){
break;
}
if(!a._updateAnchTimeout){
if(s.anchorNode.data.match(Xinha.RE_email)&&a.href.match("mailto:"+s.anchorNode.data.trim())){
var _16=s.anchorNode;
var _17=function(){
a.href="mailto:"+_16.data.trim();
a._updateAnchTimeout=setTimeout(_17,250);
};
a._updateAnchTimeout=setTimeout(_17,1000);
break;
}
var m=s.anchorNode.data.match(Xinha.RE_url);
if(m&&a.href.match(s.anchorNode.data.trim())){
var _19=s.anchorNode;
var _1a=function(){
m=_19.data.match(Xinha.RE_url);
if(m){
a.href=(m[1]?m[1]:"http://")+m[2];
}
a._updateAnchTimeout=setTimeout(_1a,250);
};
a._updateAnchTimeout=setTimeout(_1a,1000);
}
}
}
break;
}
return false;
};
Gecko.prototype.handleBackspace=function(){
var _1b=this.editor;
setTimeout(function(){
var sel=_1b.getSelection();
var _1d=_1b.createRange(sel);
var SC=_1d.startContainer;
var SO=_1d.startOffset;
var EC=_1d.endContainer;
var EO=_1d.endOffset;
var _22=SC.nextSibling;
if(SC.nodeType==3){
SC=SC.parentNode;
}
if(!(/\S/.test(SC.tagName))){
var p=document.createElement("p");
while(SC.firstChild){
p.appendChild(SC.firstChild);
}
SC.parentNode.insertBefore(p,SC);
Xinha.removeFromParent(SC);
var r=_1d.cloneRange();
r.setStartBefore(_22);
r.setEndAfter(_22);
r.extractContents();
sel.removeAllRanges();
sel.addRange(r);
}
},10);
};
Gecko.prototype.dom_checkInsertP=function(){
var _25=this.editor;
var p,body;
var sel=_25.getSelection();
var _28=_25.createRange(sel);
if(!_28.collapsed){
_28.deleteContents();
}
_25.deactivateEditor();
var SC=_28.startContainer;
var SO=_28.startOffset;
var EC=_28.endContainer;
var EO=_28.endOffset;
if(SC==EC&&SC==body&&!SO&&!EO){
p=_25._doc.createTextNode(" ");
body.insertBefore(p,body.firstChild);
_28.selectNodeContents(p);
SC=_28.startContainer;
SO=_28.startOffset;
EC=_28.endContainer;
EO=_28.endOffset;
}
p=_25.getAllAncestors();
var _2d=null;
body=_25._doc.body;
for(var i=0;i<p.length;++i){
if(Xinha.isParaContainer(p[i])){
break;
}else{
if(Xinha.isBlockElement(p[i])&&!(/body|html/i.test(p[i].tagName))){
_2d=p[i];
break;
}
}
}
if(!_2d){
var _2f=_28.startContainer;
while(_2f.parentNode&&!Xinha.isParaContainer(_2f.parentNode)){
_2f=_2f.parentNode;
}
var _30=_2f;
var end=_2f;
while(_30.previousSibling){
if(_30.previousSibling.tagName){
if(!Xinha.isBlockElement(_30.previousSibling)){
_30=_30.previousSibling;
}else{
break;
}
}else{
_30=_30.previousSibling;
}
}
while(end.nextSibling){
if(end.nextSibling.tagName){
if(!Xinha.isBlockElement(end.nextSibling)){
end=end.nextSibling;
}else{
break;
}
}else{
end=end.nextSibling;
}
}
_28.setStartBefore(_30);
_28.setEndAfter(end);
_28.surroundContents(_25._doc.createElement("p"));
_2d=_28.startContainer.firstChild;
_28.setStart(SC,SO);
}
_28.setEndAfter(_2d);
var r2=_28.cloneRange();
sel.removeRange(_28);
var df=r2.extractContents();
if(df.childNodes.length===0){
df.appendChild(_25._doc.createElement("p"));
df.firstChild.appendChild(_25._doc.createElement("br"));
}
if(df.childNodes.length>1){
var nb=_25._doc.createElement("p");
while(df.firstChild){
var s=df.firstChild;
df.removeChild(s);
nb.appendChild(s);
}
df.appendChild(nb);
}
if(!(/\S/.test(_2d.innerHTML))){
_2d.innerHTML="&nbsp;";
}
p=df.firstChild;
if(!(/\S/.test(p.innerHTML))){
p.innerHTML="<br />";
}
if((/^\s*<br\s*\/?>\s*$/.test(p.innerHTML))&&(/^h[1-6]$/i.test(p.tagName))){
df.appendChild(_25.convertNode(p,"p"));
df.removeChild(p);
}
var _36=_2d.parentNode.insertBefore(df.firstChild,_2d.nextSibling);
_25.activateEditor();
sel=_25.getSelection();
sel.removeAllRanges();
sel.collapse(_36,0);
_25.scrollToElement(_36);
};
Gecko.prototype.inwardHtml=function(_37){
_37=_37.replace(/<(\/?)strong(\s|>|\/)/ig,"<$1b$2");
_37=_37.replace(/<(\/?)em(\s|>|\/)/ig,"<$1i$2");
_37=_37.replace(/<(\/?)del(\s|>|\/)/ig,"<$1strike$2");
return _37;
};
Gecko.prototype.outwardHtml=function(_38){
_38=_38.replace(/<script[\s]*src[\s]*=[\s]*['"]chrome:\/\/.*?["']>[\s]*<\/script>/ig,"");
return _38;
};
Gecko.prototype.onExecCommand=function(_39,UI,_3b){
try{
this.editor._doc.execCommand("useCSS",false,true);
this.editor._doc.execCommand("styleWithCSS",false,false);
}
catch(ex){
}
switch(_39){
case "paste":
alert(Xinha._lc("The Paste button does not work in Mozilla based web browsers (technical security reasons). Press CTRL-V on your keyboard to paste directly."));
return true;
}
return false;
};
Xinha.prototype.insertNodeAtSelection=function(_3c){
var sel=this.getSelection();
var _3e=this.createRange(sel);
sel.removeAllRanges();
_3e.deleteContents();
var _3f=_3e.startContainer;
var pos=_3e.startOffset;
var _41=_3c;
switch(_3f.nodeType){
case 3:
if(_3c.nodeType==3){
_3f.insertData(pos,_3c.data);
_3e=this.createRange();
_3e.setEnd(_3f,pos+_3c.length);
_3e.setStart(_3f,pos+_3c.length);
sel.addRange(_3e);
}else{
_3f=_3f.splitText(pos);
if(_3c.nodeType==11){
_41=_41.firstChild;
}
_3f.parentNode.insertBefore(_3c,_3f);
this.selectNodeContents(_41);
this.updateToolbar();
}
break;
case 1:
if(_3c.nodeType==11){
_41=_41.firstChild;
}
_3f.insertBefore(_3c,_3f.childNodes[pos]);
this.selectNodeContents(_41);
this.updateToolbar();
break;
}
};
Xinha.prototype.getParentElement=function(sel){
if(typeof sel=="undefined"){
sel=this.getSelection();
}
var _43=this.createRange(sel);
try{
var p=_43.commonAncestorContainer;
if(!_43.collapsed&&_43.startContainer==_43.endContainer&&_43.startOffset-_43.endOffset<=1&&_43.startContainer.hasChildNodes()){
p=_43.startContainer.childNodes[_43.startOffset];
}
while(p.nodeType==3){
p=p.parentNode;
}
return p;
}
catch(ex){
return null;
}
};
Xinha.prototype.activeElement=function(sel){
if((sel===null)||this.selectionEmpty(sel)){
return null;
}
if(!sel.isCollapsed){
if(sel.anchorNode.childNodes.length>sel.anchorOffset&&sel.anchorNode.childNodes[sel.anchorOffset].nodeType==1){
return sel.anchorNode.childNodes[sel.anchorOffset];
}else{
if(sel.anchorNode.nodeType==1){
return sel.anchorNode;
}else{
return null;
}
}
}
return null;
};
Xinha.prototype.selectionEmpty=function(sel){
if(!sel){
return true;
}
if(typeof sel.isCollapsed!="undefined"){
return sel.isCollapsed;
}
return true;
};
Xinha.prototype.selectNodeContents=function(_47,pos){
this.focusEditor();
this.forceRedraw();
var _49;
var _4a=typeof pos=="undefined"?true:false;
var sel=this.getSelection();
_49=this._doc.createRange();
if(_4a&&_47.tagName&&_47.tagName.toLowerCase().match(/table|img|input|textarea|select/)){
_49.selectNode(_47);
}else{
_49.selectNodeContents(_47);
}
sel.removeAllRanges();
sel.addRange(_49);
};
Xinha.prototype.insertHTML=function(_4c){
var sel=this.getSelection();
var _4e=this.createRange(sel);
this.focusEditor();
var _4f=this._doc.createDocumentFragment();
var div=this._doc.createElement("div");
div.innerHTML=_4c;
while(div.firstChild){
_4f.appendChild(div.firstChild);
}
var _51=this.insertNodeAtSelection(_4f);
};
Xinha.prototype.getSelectedHTML=function(){
var sel=this.getSelection();
var _53=this.createRange(sel);
return Xinha.getHTML(_53.cloneContents(),false,this);
};
Xinha.prototype.getSelection=function(){
return this._iframe.contentWindow.getSelection();
};
Xinha.prototype.createRange=function(sel){
this.activateEditor();
if(typeof sel!="undefined"){
try{
return sel.getRangeAt(0);
}
catch(ex){
return this._doc.createRange();
}
}else{
return this._doc.createRange();
}
};
Xinha.prototype.isKeyEvent=function(_55){
return _55.type=="keypress";
};
Xinha.prototype.getKey=function(_56){
return String.fromCharCode(_56.charCode);
};
Xinha.getOuterHTML=function(_57){
return (new XMLSerializer()).serializeToString(_57);
};
Xinha.prototype._standardToggleBorders=Xinha.prototype._toggleBorders;
Xinha.prototype._toggleBorders=function(){
var _58=Xinha.prototype._standardToggleBorders();
var _59=this._doc.getElementByTagName("TABLE");
for(var i=0;i<_59.length;i++){
_59[i].style.display="none";
_59[i].style.display="table";
}
return _58;
};

