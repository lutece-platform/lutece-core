<?php
  /**
    = MootoolsFileManager Configuration File =
        
    Configure either by directly editing the config.php file (not recommended) or
    as follows...

    1. You need to be able to put PHP in your XinhaConfig.js, so
      you may want to call it XinhaConfig.php instead, or whatever other
      method you choose (eg put the config as an inline script in your 
      main php page).
      
    2. In step 3 of your XinhaConfig write something like...
      {{{
        with (xinha_config.MootoolsFileManager)
        { 
          <?php 
            require_once('/path/to/xinha/contrib/php-xinha.php');
            xinha_pass_to_php_backend
            (       
              array
              (
                'images_dir' => '/home/your/directory',
                'images_url' => '/directory',
                'allow_images_upload' => true,
              )
            )
          ?>
        }
      }}}

    This will work provided you are using normal file-based PHP sessions
    (most likely), if not, you may need to modify the php-xinha.php
    file to suit your setup.
  
  * @author $Author$
  * @version $Id$
  * @package MootoolsFileManager
  *
  */


  /**    
    == File Paths REQUIRED ==
    
    This plugin operates (optionally) in two modes.
    
    1. As a File Manager where people are inserting a link to a file 
      (eg a doc or pdf commonly), we call this "files" mode.
    2. As an Image Manager where people are inserting an inline image,
      we call this "images" mode.
    
    You may provide one of, or both of, files_dir and images_dir.  If you do not 
    provide one, that mode of MootoolsFileManager will be disabled.
    
    # `files_dir` -- Directory path to the location where ordinary files are stored
                      (eg /home/you/public_html/downloads/ )                   
    # `files_url` -- The URL path to the files_dir
                      (eg /downloads/)  
    # `images_dir` -- Directory path to the location where inline images are stored
                      (eg /home/you/public_html/images/)
    # `images_url` -- The URL path to the images_dir

    === Security Caution ===

    You should ensure that the paths you specify are protected against having PHP and
    other scripts from being executed.  The following .htaccess file is a good idea

    {{{
      <IfModule mod_php.c>
        php_flag engine off
      </IfModule>
      AddType text/html .html .htm .shtml .php .php3 .php4 .php5 .php6 .php7 .php8 .phtml .phtm .pl .py .cgi
      RemoveHandler .php
      RemoveHandler .php8
      RemoveHandler .php7
      RemoveHandler .php6
      RemoveHandler .php5
      RemoveHandler .php4
      RemoveHandler .php3
    }}}
    
  */

  $IMConfig['files_dir']  = FALSE; // No trailing slash 
  $IMConfig['files_url']  = FALSE; // No trailing slash

  $IMConfig['images_dir'] = FALSE; // No trailing slash
  $IMConfig['images_url'] = FALSE; // No trailing slash

  /**
    == Turning On Uploads ==
    We have two sets of settings for turning on uploads, one controls the files mode
    of the plugin, the other is for images mode.

    Note that allowing upload also permits the user to create subdirectories to better
    organise the files.
    
    === Maximum File Sizes ===

    Each mode can have a different maximum file size that can be uploaded, this
    size is a number followed by one of M, KB or B.

    === Suggested Image Dimensions ===

    Each mode can have a different "suggested maximum image dimension", when the
    user uses the Mootools File Manager to upload a file, they are able to choose
    to "resize large images" on upload.  This defines what "large" means.
  
  */

  $IMConfig['allow_files_upload']     = false;
  $IMConfig['allow_files_delete']     = false;
  $IMConfig['max_files_upload_size']  = '3M';
  $IMConfig['suggested_files_image_dimension']  = array('width' => 2048, 'height' => 1536);

  $IMConfig['allow_images_upload']     = false;
  $IMConfig['allow_images_delete']     = false;
  $IMConfig['max_images_upload_size']  = '3M';
  $IMConfig['suggested_images_image_dimension']  = array('width' => 1024, 'height' => 768);


// -------------------------------------------------------------------------
//                OPTIONAL SETTINGS 
// -------------------------------------------------------------------------

/**

== Plugin Path ==
 
 For most people the defaults will work fine, but if you have trouble, you can set
 `base_dir` to be the directory path to xinha/plugins/MootoolsFileManager
 `base_url` to be the url path to xinha/plugins/MootoolsFileManager
*/

$IMConfig['base_dir'] = getcwd();
$IMConfig['base_url'] = preg_replace('/\/backend\.php.*/', '', $_SERVER['REQUEST_URI']);


 /**
  // Future use, not yet required.
  
  == ImageMagick Path ==
  
  Certain operations require that ImageMagick is available on your server,
  mogrify, convert and identify executables are required.
    
  If these executables are not in your executable path, you'll want to 
  set the path to them here, for Linux/Unix etc this will be something like
  
  {{{ 
    /usr/bin
  }}}
  
  for Windows servers, something like
  
  {{{
    C:/"Program Files"/ImageMagick-5.5.7-Q16/
  }}}
    
  

  $IMConfig['IMAGE_TRANSFORM_LIB_PATH'] = '';
*/

////////////////////////////////////////////////////////////////////////////////
//       ================== END OF CONFIGURATION =======================      //
////////////////////////////////////////////////////////////////////////////////


// Standard PHP Backend Data Passing
//  if data was passed using xinha_pass_to_php_backend() we merge the items
//  provided into the Config
require_once(realpath(dirname(__FILE__) . '/../../contrib/php-xinha.php'));

if($passed_data = xinha_read_passed_data())
{
  $IMConfig = array_merge($IMConfig, $passed_data);
}
@session_write_close(); // Close session now so we don't lock.

?>
