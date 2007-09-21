function Abbreviation(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"abbreviation",tooltip:this._lc("Abbreviation"),image:_1.imgURL("ed_abbreviation.gif","Abbreviation"),textMode:false,action:function(_4){
_3.buttonPress(_4);
}});
_2.addToolbarElement("abbreviation","inserthorizontalrule",1);
}
Abbreviation._pluginInfo={name:"Abbreviation",version:"1.0",developer:"Udo Schmal",developer_url:"",sponsor:"L.N.Schaffrath NeueMedien",sponsor_url:"http://www.schaffrath-neuemedien.de/",c_owner:"Udo Schmal & Schaffrath-NeueMedien",license:"htmlArea"};
Abbreviation.prototype._lc=function(_5){
return Xinha._lc(_5,"Abbreviation");
};
Abbreviation.prototype.onGenerate=function(){
var _6="Abbr-style";
var _7=this.editor._doc.getElementById(_6);
if(_7==null){
_7=this.editor._doc.createElement("link");
_7.id=_6;
_7.rel="stylesheet";
_7.href=_editor_url+"plugins/Abbreviation/abbreviation.css";
this.editor._doc.getElementsByTagName("HEAD")[0].appendChild(_7);
}
};
Abbreviation.prototype.buttonPress=function(_8,_9,_a){
var _b=null;
var _c=_8.getSelectedHTML();
var _d=_8._getSelection();
var _e=_8._createRange(_d);
var _f=_8._activeElement(_d);
if(!(_f!=null&&_f.tagName.toLowerCase()=="abbr")){
_f=_8._getFirstAncestor(_d,"abbr");
}
if(_f!=null&&_f.tagName.toLowerCase()=="abbr"){
_b={title:_f.title,text:_f.innerHTML};
}else{
_b={title:"",text:_c};
}
_8._popupDialog("plugin://Abbreviation/abbreviation",function(_10){
if(_10){
var _11=_10["title"];
if(_11==""||_11==null){
if(_f){
var _12=_f.innerHTML;
_f.parentNode.removeChild(_f);
_8.insertHTML(_12);
}
return;
}
try{
var doc=_8._doc;
if(!_f){
_f=doc.createElement("abbr");
_f.title=_11;
_f.innerHTML=_c;
if(Xinha.is_ie){
_e.pasteHTML(_f.outerHTML);
}else{
_8.insertNodeAtSelection(_f);
}
}else{
_f.title=_11;
}
}
catch(e){
}
}
},_b);
};

