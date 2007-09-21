function InsertAnchor(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"insert-anchor",tooltip:this._lc("Insert Anchor"),image:_1.imgURL("insert-anchor.gif","InsertAnchor"),textMode:false,action:function(_4){
_3.buttonPress(_4);
}});
_2.addToolbarElement("insert-anchor","createlink",1);
}
InsertAnchor._pluginInfo={name:"InsertAnchor",origin:"version: 1.0, by Andre Rabold, MR Printware GmbH, http://www.mr-printware.de",version:"2.0",developer:"Udo Schmal",developer_url:"http://www.schaffrath-neuemedien.de",c_owner:"Udo Schmal",sponsor:"L.N.Schaffrath NeueMedien",sponsor_url:"http://www.schaffrath-neuemedien.de",license:"htmlArea"};
InsertAnchor.prototype._lc=function(_5){
return Xinha._lc(_5,"InsertAnchor");
};
InsertAnchor.prototype.onGenerate=function(){
var _6="IA-style";
var _7=this.editor._doc.getElementById(_6);
if(_7==null){
_7=this.editor._doc.createElement("link");
_7.id=_6;
_7.rel="stylesheet";
_7.href=_editor_url+"plugins/InsertAnchor/insert-anchor.css";
this.editor._doc.getElementsByTagName("HEAD")[0].appendChild(_7);
}
};
InsertAnchor.prototype.buttonPress=function(_8){
var _9=null;
var _a=_8.getSelectedHTML();
var _b=_8._getSelection();
var _c=_8._createRange(_b);
var a=_8._activeElement(_b);
if(!(a!=null&&a.tagName.toLowerCase()=="a")){
a=_8._getFirstAncestor(_b,"a");
}
if(a!=null&&a.tagName.toLowerCase()=="a"){
_9={name:a.id};
}else{
_9={name:""};
}
_8._popupDialog("plugin://InsertAnchor/insert_anchor",function(_e){
if(_e){
var _f=_e["name"];
if(_f==""||_f==null){
if(a){
var _10=a.innerHTML;
a.parentNode.removeChild(a);
_8.insertHTML(_10);
}
return;
}
try{
var doc=_8._doc;
if(!a){
a=doc.createElement("a");
a.id=_f;
a.name=_f;
a.title=_f;
a.className="anchor";
a.innerHTML=_a;
if(Xinha.is_ie){
_c.pasteHTML(a.outerHTML);
}else{
_8.insertNodeAtSelection(a);
}
}else{
a.id=_f;
a.name=_f;
a.title=_f;
a.className="anchor";
}
}
catch(e){
}
}
},_9);
};

