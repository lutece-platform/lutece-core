
function displayId(baliseId)
  {
  if (document.getElementById && document.getElementById(baliseId) != null)
    {
    	document.getElementById(baliseId).style.visibility='visible';
    	document.getElementById(baliseId).style.display='block';
    }
  }

function hideId(baliseId)
  {
  if (document.getElementById && document.getElementById(baliseId) != null)
    {
		document.getElementById(baliseId).style.visibility='hidden';
    	document.getElementById(baliseId).style.display='none';
    }
  }
/*
function adminToggle() {
if ( document.getElementById('content-admin-site-page').className == 'admin-site-page' ) 
    {
      document.getElementById('content-admin-site-page').className = 'admin-site-page-hidden';
      document.getElementById('admin-site-preview').className = 'preview-ext';
      document.getElementById('toggle_admin').className = 'toggle_admin_on';
    } else {
      document.getElementById('content-admin-site-page').className = 'admin-site-page';
      document.getElementById('admin-site-preview').className = 'preview';
      document.getElementById('toggle_admin').className = 'toggle_admin_off';
    }
}*/