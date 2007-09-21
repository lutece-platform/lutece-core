var is_ie=((navigator.userAgent.toLowerCase().indexOf("msie")!=-1)&&(navigator.userAgent.toLowerCase().indexOf("opera")==-1));
var is_compat=(document.compatMode=="BackCompat");
function PopupDiv(_1,_2,_3,_4){
var _5=this;
this.editor=_1;
this.doc=_1._mdoc;
this.handler=_3;
var el=this.doc.createElement("div");
el.className="content";
var _7=this.doc.createElement("div");
_7.className="dialog popupdiv";
this.element=_7;
var s=_7.style;
s.position="absolute";
s.left="0px";
s.top="0px";
var _9=this.doc.createElement("div");
_9.className="title";
this.title=_9;
_7.appendChild(_9);
HTMLArea._addEvent(_9,"mousedown",function(ev){
_5._dragStart(is_ie?window.event:ev);
});
var _b=this.doc.createElement("div");
_b.className="button";
_9.appendChild(_b);
_b.innerHTML="&#x00d7;";
_9.appendChild(this.doc.createTextNode(_2));
this.titleText=_2;
_b.onmouseover=function(){
this.className+=" button-hilite";
};
_b.onmouseout=function(){
this.className=this.className.replace(/\s*button-hilite\s*/g," ");
};
_b.onclick=function(){
this.className=this.className.replace(/\s*button-hilite\s*/g," ");
_5.close();
};
_7.appendChild(el);
this.content=el;
this.doc.body.appendChild(_7);
this.dragging=false;
this.onShow=null;
this.onClose=null;
this.modal=false;
_4(this);
}
PopupDiv.currentPopup=null;
PopupDiv.prototype.showAtElement=function(el,_d){
this.defaultSize();
var _e,ew,eh;
var _f=this.element;
_f.style.display="block";
var w=_f.offsetWidth;
var h=_f.offsetHeight;
_f.style.display="none";
if(el!=window){
_e=PopupDiv.getAbsolutePos(el);
ew=el.offsetWidth;
eh=el.offsetHeight;
}else{
_e={x:0,y:0};
var _12=PopupDiv.getWindowSize();
ew=_12.x;
eh=_12.y;
}
var FX=false,FY=false;
if(_d.indexOf("l")!=-1){
_e.x-=w;
FX=true;
}
if(_d.indexOf("r")!=-1){
_e.x+=ew;
FX=true;
}
if(_d.indexOf("t")!=-1){
_e.y-=h;
FY=true;
}
if(_d.indexOf("b")!=-1){
_e.y+=eh;
FY=true;
}
if(_d.indexOf("c")!=-1){
FX||(_e.x+=Math.round((ew-w)/2));
FY||(_e.y+=Math.round((eh-h)/2));
}
this.showAt(_e.x,_e.y);
};
PopupDiv.prototype.defaultSize=function(){
var s=this.element.style;
var cs=this.element.currentStyle;
var _16=(is_ie&&is_compat)?(parseInt(cs.borderLeftWidth)+parseInt(cs.borderRightWidth)+parseInt(cs.paddingLeft)+parseInt(cs.paddingRight)):0;
var _17=(is_ie&&is_compat)?(parseInt(cs.borderTopWidth)+parseInt(cs.borderBottomWidth)+parseInt(cs.paddingTop)+parseInt(cs.paddingBottom)):0;
s.display="block";
s.width=(this.content.offsetWidth+_16)+"px";
s.height=(this.content.offsetHeight+this.title.offsetHeight)+"px";
s.display="none";
};
PopupDiv.prototype.showAt=function(x,y){
this.defaultSize();
var s=this.element.style;
s.display="block";
s.left=x+"px";
s.top=y+"px";
this.hideShowCovered();
PopupDiv.currentPopup=this;
HTMLArea._addEvents(this.doc.body,["mousedown","click"],PopupDiv.checkPopup);
HTMLArea._addEvents(this.editor._doc.body,["mousedown","click"],PopupDiv.checkPopup);
if(is_ie&&this.modal){
this.doc.body.setCapture(false);
this.doc.body.onlosecapture=function(){
(PopupDiv.currentPopup)&&(this.doc.body.setCapture(false));
};
}
window.event&&HTMLArea._stopEvent(window.event);
if(typeof this.onShow=="function"){
this.onShow();
}else{
if(typeof this.onShow=="string"){
eval(this.onShow);
}
}
var _1b=this.element.getElementsByTagName("input")[0];
if(!_1b){
_1b=this.element.getElementsByTagName("select")[0];
}
if(!_1b){
_1b=this.element.getElementsByTagName("textarea")[0];
}
if(_1b){
_1b.focus();
}
};
PopupDiv.prototype.close=function(){
this.element.style.display="none";
PopupDiv.currentPopup=null;
this.hideShowCovered();
HTMLArea._removeEvents(this.doc.body,["mousedown","click"],PopupDiv.checkPopup);
HTMLArea._removeEvents(this.editor._doc.body,["mousedown","click"],PopupDiv.checkPopup);
is_ie&&this.modal&&this.doc.body.releaseCapture();
if(typeof this.onClose=="function"){
this.onClose();
}else{
if(typeof this.onClose=="string"){
eval(this.onClose);
}
}
this.element.parentNode.removeChild(this.element);
};
PopupDiv.prototype.getForm=function(){
var _1c=this.content.getElementsByTagName("form");
return (_1c.length>0)?_1c[0]:null;
};
PopupDiv.prototype.callHandler=function(){
var _1d=["input","textarea","select"];
var _1e=new Object();
for(var ti=_1d.length;--ti>=0;){
var tag=_1d[ti];
var els=this.content.getElementsByTagName(tag);
for(var j=0;j<els.length;++j){
var el=els[j];
_1e[el.name]=el.value;
}
}
this.handler(this,_1e);
return false;
};
PopupDiv.getAbsolutePos=function(el){
var r={x:el.offsetLeft,y:el.offsetTop};
if(el.offsetParent){
var tmp=PopupDiv.getAbsolutePos(el.offsetParent);
r.x+=tmp.x;
r.y+=tmp.y;
}
return r;
};
PopupDiv.getWindowSize=function(){
if(window.innerHeight){
return {y:window.innerHeight,x:window.innerWidth};
}
if(this.doc.body.clientHeight){
return {y:this.doc.body.clientHeight,x:this.doc.body.clientWidth};
}
return {y:this.doc.documentElement.clientHeight,x:this.doc.documentElement.clientWidth};
};
PopupDiv.prototype.hideShowCovered=function(){
var _27=this;
function isContained(el){
while(el){
if(el==_27.element){
return true;
}
el=el.parentNode;
}
return false;
}
var _29=new Array("applet","select");
var el=this.element;
var p=PopupDiv.getAbsolutePos(el);
var EX1=p.x;
var EX2=el.offsetWidth+EX1;
var EY1=p.y;
var EY2=el.offsetHeight+EY1;
if(el.style.display=="none"){
EX1=EX2=EY1=EY2=0;
}
for(var k=_29.length;k>0;){
var ar=this.doc.getElementsByTagName(_29[--k]);
var cc=null;
for(var i=ar.length;i>0;){
cc=ar[--i];
if(isContained(cc)){
cc.style.visibility="visible";
continue;
}
p=PopupDiv.getAbsolutePos(cc);
var CX1=p.x;
var CX2=cc.offsetWidth+CX1;
var CY1=p.y;
var CY2=cc.offsetHeight+CY1;
if((CX1>EX2)||(CX2<EX1)||(CY1>EY2)||(CY2<EY1)){
cc.style.visibility="visible";
}else{
cc.style.visibility="hidden";
}
}
}
};
PopupDiv.prototype._dragStart=function(ev){
if(this.dragging){
return false;
}
this.dragging=true;
PopupDiv.currentPopup=this;
var _39=ev.clientX;
var _3a=ev.clientY;
if(is_ie){
_3a+=this.doc.body.scrollTop;
_39+=this.doc.body.scrollLeft;
}else{
_3a+=window.scrollY;
_39+=window.scrollX;
}
var st=this.element.style;
this.xOffs=_39-parseInt(st.left);
this.yOffs=_3a-parseInt(st.top);
HTMLArea._addEvent(this.doc,"mousemove",PopupDiv.dragIt);
HTMLArea._addEvent(this.doc,"mouseover",HTMLArea._stopEvent);
HTMLArea._addEvent(this.doc,"mouseup",PopupDiv.dragEnd);
HTMLArea._stopEvent(ev);
};
PopupDiv.dragIt=function(ev){
var _3d=PopupDiv.currentPopup;
if(!(_3d&&_3d.dragging)){
return false;
}
is_ie&&(ev=window.event);
var _3e=ev.clientX;
var _3f=ev.clientY;
if(is_ie){
_3f+=this.doc.body.scrollTop;
_3e+=this.doc.body.scrollLeft;
}else{
_3f+=window.scrollY;
_3e+=window.scrollX;
}
_3d.hideShowCovered();
var st=_3d.element.style;
st.left=(_3e-_3d.xOffs)+"px";
st.top=(_3f-_3d.yOffs)+"px";
HTMLArea._stopEvent(ev);
};
PopupDiv.dragEnd=function(){
var _41=PopupDiv.currentPopup;
if(!_41){
return false;
}
_41.dragging=false;
HTMLArea._removeEvent(_41.doc,"mouseup",PopupDiv.dragEnd);
HTMLArea._removeEvent(_41.doc,"mouseover",HTMLArea._stopEvent);
HTMLArea._removeEvent(_41.doc,"mousemove",PopupDiv.dragIt);
_41.hideShowCovered();
};
PopupDiv.checkPopup=function(ev){
is_ie&&(ev=window.event);
var el=is_ie?ev.srcElement:ev.target;
var cp=PopupDiv.currentPopup;
for(;(el!=null)&&(el!=cp.element);el=el.parentNode){
}
if(el==null){
cp.modal||ev.type=="mouseover"||cp.close();
HTMLArea._stopEvent(ev);
}
};
PopupDiv.prototype.addButtons=function(){
var _45=this;
var div=this.doc.createElement("div");
this.content.appendChild(div);
div.className="buttons";
for(var i=0;i<arguments.length;++i){
var btn=arguments[i];
var _49=this.doc.createElement("button");
div.appendChild(_49);
_49.innerHTML=HTMLArea._lc(buttons[btn],"HTMLArea");
switch(btn){
case "ok":
_49.onclick=function(){
_45.callHandler();
_45.close();
};
break;
case "cancel":
_49.onclick=function(){
_45.close();
};
break;
}
}
};

