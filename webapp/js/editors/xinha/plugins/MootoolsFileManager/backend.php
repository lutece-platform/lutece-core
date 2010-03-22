<?php 
/**
* Unified backend for ImageManager 
*
* Image Manager was originally developed by:
*   Xiang Wei Zhuo, email: xiangweizhuo(at)hotmail.com Wei Shou.
*
* Unified backend sponsored by DTLink Software, http://www.dtlink.com
* Implementation by Yermo Lamers, http://www.formvista.com
*
* (c) DTLink, LLC 2005.
* Distributed under the same terms as HTMLArea itself.
* This notice MUST stay intact for use (see license.txt).
*
* DESCRIPTION:
*
* Instead of using separate URL's for each function, ImageManager now
* routes all requests to the server through this single, replaceable,
* entry point. backend.php expects at least two URL variable parameters: 
*
* __plugin=ImageManager   for future expansion; identify the plugin being requested.
* __function=thumbs|images|editorFrame|editor|manager  function being called.
*
* Having a single entry point that strictly adheres to a defined interface will 
* make the backend code much easier to maintain and expand. It will make it easier
* on integrators, not to mention it'll make it easier to have separate 
* implementations of the backend in different languages (Perl, Python, ASP, etc.) 
*
* @see config.inc.php
*/

// Strip slashes if MQGPC is on
set_magic_quotes_runtime(0);
if(get_magic_quotes_gpc())
{
  $to_clean = array(&$_GET, &$_POST, &$_REQUEST, &$_COOKIE);
  while(count($to_clean))
  {
    $cleaning =& $to_clean[array_pop($junk = array_keys($to_clean))];
    unset($to_clean[array_pop($junk = array_keys($to_clean))]);
    foreach(array_keys($cleaning) as $k)
    {
      if(is_array($cleaning[$k]))
      {
        $to_clean[] =& $cleaning[$k];
      }
      else
      {
        $cleaning[$k] = stripslashes($cleaning[$k]);
      }
    }
  }
}

function size_to_bytes($s)
{
  if(preg_match('/([0-9\.])+([a-zA-Z]+)/', $s, $M))
  {
    switch(strtolower($M))
    {      
      case 'm':
        return floor(floatval($M[1]) * 1024 * 1024);
        
      case 'b':
        return intval($M[1]);
        
      case 'kb':
        return floor(floatval($M[1]) * 1024);      
    }
  }
  
  if(floatval($s) < 10)   return floor(floatval($s) * 1024 * 1024); 
  if(floatval($s) < 1024) return floor(floatval($s) * 1024); // Kilobytes
  return intval($s); // Bytes
}

require_once('config.php');

switch ( @$_REQUEST[ "__function" ] )
{
  case 'read-config':        
    // This is used so that the javascript can read the config 
    // so we don't have to have a js config and a php config duplicating
    // settings
    echo xinha_to_js($IMConfig);
    break;

  case 'image-manager':
    include('mootools-filemanager/Backend/FileManager.php');

    $browser = new FileManager(array(
      'directory'     => $IMConfig['images_dir'],
      'baseURL'       => $IMConfig['images_url'],
      
      'assetBasePath' => $IMConfig['base_url'] .'/mootools-filemanager/Assets',
      
      'upload'        => $IMConfig['allow_images_upload'],
      'maxUploadSize' => size_to_bytes($IMConfig['max_images_upload_size']),
      
      'suggestedMaxImageDimension' => $IMConfig['suggested_images_image_dimension'],
            
      'destroy'       => $IMConfig['allow_images_delete'],
      'filter'        => 'image/',
            
    ));

    $browser->fireEvent(!empty($_REQUEST['event']) ? $_REQUEST['event'] : null);
    break;
  
  case 'file-manager':
    include('mootools-filemanager/Backend/FileManager.php');

    $browser = new FileManager(array(
      'directory'     => $IMConfig['files_dir'],
      'baseURL'       => $IMConfig['files_url'],
      
      'assetBasePath' => $IMConfig['base_url'] .'/mootools-filemanager/Assets',
      
      'upload'        => $IMConfig['allow_files_upload'],
      'maxUploadSize' => size_to_bytes($IMConfig['max_files_upload_size']),
      
      'suggestedMaxImageDimension' => $IMConfig['suggested_files_image_dimension'],
            
      'destroy'       => $IMConfig['allow_files_delete'],
     // 'filter'        => $IMConfig['files_filter'],
            
    ));

    $browser->fireEvent(!empty($_REQUEST['event']) ? $_REQUEST['event'] : null);
    break;
}

?>
