Xinha.getHTML=function(_1,_2,_3){
try{
return Xinha.getHTMLWrapper(_1,_2,_3);
}
catch(ex){
alert(Xinha._lc("Your Document is not well formed. Check JavaScript console for details."));
return _3._iframe.contentWindow.document.body.innerHTML;
}
};
Xinha.getHTMLWrapper=function(_4,_5,_6,_7){
var _8="";
if(!_7){
_7="";
}
switch(_4.nodeType){
case 10:
case 6:
case 12:
break;
case 2:
break;
case 4:
_8+=(Xinha.is_ie?("\n"+_7):"")+"<![CDATA["+_4.data+"]]>";
break;
case 5:
_8+="&"+_4.nodeValue+";";
break;
case 7:
_8+=(Xinha.is_ie?("\n"+_7):"")+"<?"+_4.target+" "+_4.data+" ?>";
break;
case 1:
case 11:
case 9:
var _9;
var i;
var _b=(_4.nodeType==1)?_4.tagName.toLowerCase():"";
if((_b=="script"||_b=="noscript")&&_6.config.stripScripts){
break;
}
if(_5){
_5=!(_6.config.htmlRemoveTags&&_6.config.htmlRemoveTags.test(_b));
}
if(Xinha.is_ie&&_b=="head"){
if(_5){
_8+=(Xinha.is_ie?("\n"+_7):"")+"<head>";
}
var _c=RegExp.multiline;
RegExp.multiline=true;
var _d=_4.innerHTML.replace(Xinha.RE_tagName,function(_e,p1,p2){
return p1+p2.toLowerCase();
});
RegExp.multiline=_c;
_8+=_d+"\n";
if(_5){
_8+=(Xinha.is_ie?("\n"+_7):"")+"</head>";
}
break;
}else{
if(_5){
_9=(!(_4.hasChildNodes()||Xinha.needsClosingTag(_4)));
_8+=(Xinha.is_ie&&Xinha.isBlockElement(_4)?("\n"+_7):"")+"<"+_4.tagName.toLowerCase();
var _11=_4.attributes;
for(i=0;i<_11.length;++i){
var a=_11.item(i);
if(!a.specified&&!(_4.tagName.toLowerCase().match(/input|option/)&&a.nodeName=="value")&&!(_4.tagName.toLowerCase().match(/area/)&&a.nodeName.match(/shape|coords/i))){
continue;
}
var _13=a.nodeName.toLowerCase();
if(/_moz_editor_bogus_node/.test(_13)){
_8="";
break;
}
if(/(_moz)|(contenteditable)|(_msh)/.test(_13)){
continue;
}
var _14;
if(_13!="style"){
if(typeof _4[a.nodeName]!="undefined"&&_13!="href"&&_13!="src"&&!(/^on/.test(_13))){
_14=_4[a.nodeName];
}else{
_14=a.nodeValue;
if(Xinha.is_ie&&(_13=="href"||_13=="src")){
_14=_6.stripBaseURL(_14);
}
if(_6.config.only7BitPrintablesInURLs&&(_13=="href"||_13=="src")){
_14=_14.replace(/([^!-~]+)/g,function(_15){
return escape(_15);
});
}
}
}else{
_14=_4.style.cssText;
}
if(/^(_moz)?$/.test(_14)){
continue;
}
_8+=" "+_13+"=\""+Xinha.htmlEncode(_14)+"\"";
}
if(_8!==""){
if(_9&&_b=="p"){
_8+=">&nbsp;</p>";
}else{
if(_9){
_8+=" />";
}else{
_8+=">";
}
}
}
}
}
var _16=false;
if(_b=="script"||_b=="noscript"){
if(!_6.config.stripScripts){
if(Xinha.is_ie){
var _17="\n"+_4.innerHTML.replace(/^[\n\r]*/,"").replace(/\s+$/,"")+"\n"+_7;
}else{
var _17=(_4.hasChildNodes())?_4.firstChild.nodeValue:"";
}
_8+=_17+"</"+_b+">"+((Xinha.is_ie)?"\n":"");
}
}else{
for(i=_4.firstChild;i;i=i.nextSibling){
if(!_16&&i.nodeType==1&&Xinha.isBlockElement(i)){
_16=true;
}
_8+=Xinha.getHTMLWrapper(i,true,_6,_7+"  ");
}
if(_5&&!_9){
_8+=(Xinha.is_ie&&Xinha.isBlockElement(_4)&&_16?("\n"+_7):"")+"</"+_4.tagName.toLowerCase()+">";
}
}
break;
case 3:
_8=/^script|noscript|style$/i.test(_4.parentNode.tagName)?_4.data:Xinha.htmlEncode(_4.data);
break;
case 8:
_8="<!--"+_4.data+"-->";
break;
}
return _8;
};

