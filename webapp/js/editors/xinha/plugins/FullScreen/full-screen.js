function FullScreen(_1,_2){
this.editor=_1;
_1._superclean_on=false;
cfg=_1.config;
cfg.registerButton("fullscreen",this._lc("Maximize/Minimize Editor"),[_editor_url+cfg.imgURL+"ed_buttons_main.gif",8,0],true,function(e,_4,_5){
e._fullScreen();
if(e._isFullScreen){
_5.swapImage([_editor_url+cfg.imgURL+"ed_buttons_main.gif",9,0]);
}else{
_5.swapImage([_editor_url+cfg.imgURL+"ed_buttons_main.gif",8,0]);
}
});
cfg.addToolbarElement("fullscreen","popupeditor",0);
}
FullScreen._pluginInfo={name:"FullScreen",version:"1.0",developer:"James Sleeman",developer_url:"http://www.gogo.co.nz/",c_owner:"Gogo Internet Services",license:"htmlArea",sponsor:"Gogo Internet Services",sponsor_url:"http://www.gogo.co.nz/"};
FullScreen.prototype._lc=function(_6){
return HTMLArea._lc(_6,"FullScreen");
};
HTMLArea.prototype._fullScreen=function(){
var e=this;
function sizeItUp(){
if(!e._isFullScreen||e._sizing){
return false;
}
e._sizing=true;
var _8=HTMLArea.viewportSize();
e.sizeEditor(_8.x+"px",_8.y+"px",true,true);
e._sizing=false;
}
function sizeItDown(){
if(e._isFullScreen||e._sizing){
return false;
}
e._sizing=true;
e.initSize();
e._sizing=false;
}
function resetScroll(){
if(e._isFullScreen){
window.scroll(0,0);
window.setTimeout(resetScroll,150);
}
}
if(typeof this._isFullScreen=="undefined"){
this._isFullScreen=false;
if(e.target!=e._iframe){
HTMLArea._addEvent(window,"resize",sizeItUp);
}
}
if(HTMLArea.is_gecko){
this.deactivateEditor();
}
if(this._isFullScreen){
this._htmlArea.style.position="";
try{
if(HTMLArea.is_ie){
var _9=document.getElementsByTagName("html");
}else{
var _9=document.getElementsByTagName("body");
}
_9[0].style.overflow="";
}
catch(e){
}
this._isFullScreen=false;
sizeItDown();
var _a=this._htmlArea;
while((_a=_a.parentNode)&&_a.style){
_a.style.position=_a._xinha_fullScreenOldPosition;
_a._xinha_fullScreenOldPosition=null;
}
window.scroll(this._unScroll.x,this._unScroll.y);
}else{
this._unScroll={x:(window.pageXOffset)?(window.pageXOffset):(document.documentElement)?document.documentElement.scrollLeft:document.body.scrollLeft,y:(window.pageYOffset)?(window.pageYOffset):(document.documentElement)?document.documentElement.scrollTop:document.body.scrollTop};
var _a=this._htmlArea;
while((_a=_a.parentNode)&&_a.style){
_a._xinha_fullScreenOldPosition=_a.style.position;
_a.style.position="static";
}
window.scroll(0,0);
this._htmlArea.style.position="absolute";
this._htmlArea.style.zIndex=999;
this._htmlArea.style.left=0;
this._htmlArea.style.top=0;
this._isFullScreen=true;
resetScroll();
try{
if(HTMLArea.is_ie){
var _9=document.getElementsByTagName("html");
}else{
var _9=document.getElementsByTagName("body");
}
_9[0].style.overflow="hidden";
}
catch(e){
}
sizeItUp();
}
if(HTMLArea.is_gecko){
this.activateEditor();
}
this.focusEditor();
};

