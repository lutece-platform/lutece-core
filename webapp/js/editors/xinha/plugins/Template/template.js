function Template(_1){
this.editor=_1;
var _2=_1.config;
var _3=this;
_2.registerButton({id:"template",tooltip:Xinha._lc("Insert template","Template"),image:_1.imgURL("ed_template.gif","Template"),textMode:false,action:function(_4){
_3.buttonPress(_4);
}});
_2.addToolbarElement("template","inserthorizontalrule",1);
}
Template._pluginInfo={name:"Template",version:"1.0",developer:"Udo Schmal",developer_url:"http://www.schaffrath-neuemedien.de/",c_owner:"Udo Schmal & Schaffrath NeueMedien",license:"htmlArea"};
Template.prototype.onGenerate=function(){
var _5="Template-style";
var _6=this.editor._doc.getElementById(_5);
if(_6==null){
_6=this.editor._doc.createElement("link");
_6.id=_5;
_6.rel="stylesheet";
_6.href=_editor_url+"plugins/Template/template.css";
this.editor._doc.getElementsByTagName("HEAD")[0].appendChild(_6);
}
};
Template.prototype.buttonPress=function(_7){
_7._popupDialog("plugin://Template/template",function(_8){
if(!_8){
return false;
}
var _9=_7._doc.getElementsByTagName("body");
var _a=_9[0];
function getElement(x){
var _c=_7._doc.getElementById(x);
if(!_c){
_c=_7._doc.createElement("div");
_c.id=x;
_c.innerHTML=x;
_a.appendChild(_c);
}
if(_c.style){
_c.removeAttribute("style");
}
return _c;
}
var _d=getElement("content");
var _e=getElement("menu1");
var _f=getElement("menu2");
var _10=getElement("menu3");
switch(_8["templ"]){
case "1":
_e.style.position="absolute";
_e.style.right="0px";
_e.style.width="28%";
_e.style.backgroundColor="#e1ddd9";
_e.style.padding="2px 20px";
_d.style.position="absolute";
_d.style.left="0px";
_d.style.width="70%";
_d.style.backgroundColor="#fff";
_f.style.visibility="hidden";
_10.style.visibility="hidden";
break;
case "2":
_e.style.position="absolute";
_e.style.left="0px";
_e.style.width="28%";
_e.style.height="100%";
_e.style.backgroundColor="#e1ddd9";
_d.style.position="absolute";
_d.style.right="0px";
_d.style.width="70%";
_d.style.backgroundColor="#fff";
_f.style.visibility="hidden";
_10.style.visibility="hidden";
break;
case "3":
_e.style.position="absolute";
_e.style.left="0px";
_e.style.width="28%";
_e.style.backgroundColor="#e1ddd9";
_f.style.position="absolute";
_f.style.right="0px";
_f.style.width="28%";
_f.style.backgroundColor="#e1ddd9";
_d.style.position="absolute";
_d.style.right="30%";
_d.style.width="60%";
_d.style.backgroundColor="#fff";
_10.style.visibility="hidden";
break;
}
},null);
};

