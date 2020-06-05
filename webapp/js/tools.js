
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
