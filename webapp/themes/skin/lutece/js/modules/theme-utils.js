/**
 * BackportTemplateStyle class
 */
export class themeUtils {
  
    removeResourceLinksFromArray(resourcesToRemove) {
        resourcesToRemove.forEach( link => {
            const elements = document.querySelectorAll(`link[href^="${link.pathtoremove}${link.name}"], script[src^="${link.pathtoremove}${link.name}"]`);
            elements.forEach(element => {
                element.parentNode.removeChild(element);
            });
        });
    }
    
    addResourceLinksFromArray( resourcesToAdd ) {
       // Ajouter les nouvelles ressources dans le <head> ou <body> selon leur type
       resourcesToAdd.forEach( resource => {
            let newElement;
            if (resource.name.endsWith('css') ) {
                if( !this.isFileDeclared(resource.name, 'css') ) {
                    newElement = document.createElement('link');
                    newElement.rel = 'stylesheet';
                    newElement.href = resource.pathtoadd + resource.name;
                    document.head.appendChild(newElement);
                }
            } else if (resource.name.endsWith('js') ) {
                if( !this.isFileDeclared(resource.name, 'js') ) {
                    newElement = document.createElement('script');
                    newElement.src = `${resource.pathtoadd}${resource.name}?lutece_h=${Math.random().toString(36).slice(2, 11)}`;
                    document.body.appendChild(newElement);
                }
            }
        });
    }

    replaceSourceLinksFromArray( resourceData ) {
        this.removeResourceLinksFromArray( resourceData )
        this.addResourceLinksFromArray( resourceData )
    }

    isFileDeclared( fileName, fileType) {
        let files = [];
        if (fileType === 'js') { // Check for JS files
            files = document.body.getElementsByTagName('script');
            for ( let i = 0; i < files.length; i++) {
                if (files[i].src.indexOf(fileName) !== -1) {
                    return true;
                }
            }
        } else if (fileType === 'css') { // Check for CSS files
            files = document.head.getElementsByTagName('link');
            for ( let i = 0; i < files.length; i++) {
                if (files[i].href.indexOf(fileName) !== -1) {
                    return true;
                }
            }
        }
        return false;
    }

    
}