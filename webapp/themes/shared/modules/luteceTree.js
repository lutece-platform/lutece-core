/**
A class for making tree.
*/
export default class LuteceTree {
	/**
	 * Creates a new instance of LuteceSearchList.
	 * @param {HTMLElement} treeRoot - The root element.
	 * @param {Object} [options] - Optional parameters for customizing search behavior.
	 */
		  constructor( treeRoot, options ) {
			this.treeRoot = treeRoot;
			this.options = Object.assign({
				treeWrapper : 'lutece-tree',
				treeNode    : 'lutece-tree-node',
				treeOpenNode: 'lutece-tree-node-open',
				treeNodeItem: 'lutece-tree-item',
				debounceTime: 100,
			}, options );
			this.init();
		  }
	
		  init() {
			// Node Management
			const luteceTreeToggler = this.treeRoot.querySelectorAll( `.${this.options.treeNode}` );
			if( luteceTreeToggler != null){ 
				luteceTreeToggler.forEach( (toggler) => {
					if( toggler.dataset.treeIcon !='' ){
						let span = document.createElement("span");
						span.classList.add('me-2')
						span.classList.add('ti')
						span.classList.add(`ti-${toggler.dataset.treeIcon}`);
						toggler.prepend( span )
					}
					toggler.addEventListener( 'click', ( event ) => {
						event.currentTarget.classList.toggle( this.options.treeOpenNode );
						event.stopPropagation();
					});
				});
			}
			
			// Leaf Management
			const luteceTreeLeaf = this.treeRoot.querySelectorAll( `.${this.options.treeNodeItem}` );
			if( luteceTreeLeaf != null){ 
				luteceTreeLeaf.forEach( (leaf) => {
					if( leaf.dataset.treeIcon !='' ){
						const iconValue = leaf.dataset.treeIcon;
						const imageRegex = /\.(png|jpg|svg)$/i;
						if( iconValue && iconValue !== '' ){
							let span = document.createElement('span');
							if( imageRegex.test(iconValue) ){
								let img = document.createElement('img');
								img.src = iconValue;
								img.classList.add('me-2');
								leaf.prepend( img );
							} else {
								span.classList.add('me-2');
								span.classList.add('ti');
								span.classList.add(`ti-${iconValue}`);
								leaf.prepend( span );
							}
						
						}
					}
					leaf.addEventListener( "click", ( event ) => {
						event.stopPropagation();
					});
				});
			}
		}
	
		  selectTreeNode( element ) {
			const currentNode = this.treeRoot.querySelector( element );
			if( currentNode != null){ 
				currentNode.classList.add('active') 
				let el = currentNode.closest( `.${this.options.treeNode}` );
				do {
					el.classList.toggle( this.options.treeOpenNode );
					el = el.parentNode.closest( `.${this.options.treeNode}` );
					if( el === null ){
						break 
					}
				} while ( !el.parentNode.classList.contains( this.options.treeWrapper ) );
			}
		}
	
	}