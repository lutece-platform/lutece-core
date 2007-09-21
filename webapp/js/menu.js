
function hover(obj){
  if(document.all){
    UL = obj.getElementsByTagName('ul');
    if(UL.length > 0){
      sousMenu = UL[0].style;
      if(sousMenu.display == 'none' || sousMenu.display == ''){
        sousMenu.display = 'block';
      }else{
        sousMenu.display = 'none';
      }
    }
  }
}

function setHover( menu ){
  LI = document.getElementById( menu ).getElementsByTagName('li');
  nLI = LI.length;
  for(i=0; i < nLI; i++){
    LI[i].onmouseover = function(){
      hover(this);
    }
    LI[i].onmouseout = function(){
      hover(this);
    }
  }
}
