tinyMCEPopup.requireLangPack();

var templates = {
  xhtml11strict : '<?xml version="1.0" encoding="utf-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"><head><title>Generated ' + new Date() + '</title><meta http-equiv="content-type" content="text/html;charset=utf-8" /></head><body>',
  
  xhtml10strict : '<?xml version="1.0" encoding="utf-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head><title>Generated ' + new Date() + '</title><meta http-equiv="content-type" content="text/html;charset=utf-8" /></head><body>',
  xhtml10transitional : '<?xml version="1.0" encoding="utf-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"><head><title>Generated ' + new Date() + '</title><meta http-equiv="content-type" content="text/html;charset=utf-8" /></head><body>',
  
  html401strict : '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"><html><head><title>Generated ' + new Date() + '</title><meta http-equiv="content-type" content="text/html;charset=utf-8"></head><body>',
  html401transitional : '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><title>Generated ' + new Date() + '</title><meta http-equiv="content-type" content="text/html;charset=utf-8"></head><body>',

  head : '',
  end : '</body></html>'
}


W3C = {
  init : function() {
    var form = document.forms[0];
    form.w3caddress.value = 'http://validator.w3.org/check';    
  },
  
  go : function() {
    var form = document.forms[0];
    var dtd = form.w3cdoctypesel.value;
    dtd = eval('templates.' + dtd);

    var content = tinyMCEPopup.editor.getContent();
    form.fragment.value = dtd + templates.head + content + templates.end;
    form.action = form.w3caddress.value;
    form.target = '_blank';
    form.submit();
  },
  
  toggle : function(el, tel) {
    var e = document.getElementById(tel)
    if (!el.checked)
      e.style.display = 'block';
    else
      e.style.display = 'none';
  }
}

tinyMCEPopup.onInit.add(W3C.init, W3C);