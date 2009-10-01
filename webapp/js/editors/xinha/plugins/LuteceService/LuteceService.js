LuteceService._pluginInfo={name:"LuteceService",version:"2.0",developer:"Lutece",developer_url:"mailto:lutece@paris.fr",c_owner:"Lutece",sponsor:"Lutece",sponsor_url:"http://lutece.paris.fr",license:"htmlArea"};
LuteceService.prototype._lc=function(_1){
return HTMLArea._lc(_1,"LuteceService");
};
function LuteceService(_2){
this.editor=_2;
var _3=_2.config;
var _4=this;
_3.registerButton("LS-service",this._lc("Add Lutece internal links"),_2.imgURL("ed_linkservice.png","LuteceService"),false,function(_5){
_4.buttonPress(_5);
});
_3.addToolbarElement(["LS-service"],["createlink"],+1);
}
LuteceService.prototype.buttonPress=function(_6){
var _7="\""+location+"\"";
var _8=_7.indexOf("/plugins/");
if(_8>0){
if(HTMLArea.is_ie){
path="../../";
}else{
path="jsp/admin/";
}
}else{
path="";
}
var _9=path+"insert/GetAvailableInsertServices.jsp?input="+_6._textArea.name+"&selected_text="+_6.getSelectedHTML();
fen=window.open(_9,"","toolbar=no, scrollbars=yes, status=yes, location=no, directories=no, menubar=no, width=550, height=450");
fen.focus();
};

