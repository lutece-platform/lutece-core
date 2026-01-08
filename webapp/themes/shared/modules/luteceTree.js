/**
A class for making tree.
*/
export default class LuteceTree {
	/**
	 * Creates a new instance of LuteceSearchList.
	 * @param {HTMLElement} treeRoot - The root element.
	 * @param {Object} [options] - Optional parameters for customizing search behavior.
	 * @param {boolean} [options.useLocalStorage=false] - Whether to save selected node to local storage.
	 */
		  constructor( treeRoot, options ) {
			this.treeRoot = treeRoot;
			this.options = Object.assign({
				treeWrapper : 'lutece-tree',
				treeNode    : 'lutece-tree-node',
				treeOpenNode: 'lutece-tree-node-open',
				treeNodeItem: 'lutece-tree-item',
				debounceTime: 100,
				useLocalStorage: false,
			}, options );
			this.localStorageKey = `treeNode_${this.options.treeWrapper}`;
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
						event.currentTarget.dataset.treeIcon ==='folder' ? event.currentTarget.dataset.treeIcon = 'folder-open' : '';
						// Save to local storage if enabled
						if( this.options.useLocalStorage ){
							this.setLocalStorageValue( event.currentTarget.id );
						}
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

			// Restore selected node from local storage if enabled
			if( this.options.useLocalStorage ){
				const storedNode = this.getLocalStorageValue();
				if(  storedNode && storedNode != 'node-0' ){
					this.selectTreeNode( storedNode );
				}
			}
		}
	
		  selectTreeNode( element ) {
			const currentNode = this.treeRoot.querySelector( element );
			if( currentNode != null && currentNode.id != 'node-0' ){ 
				currentNode.classList.add('active') 
				let el = currentNode.closest( `.${this.options.treeNode}` );
				if( currentNode.dataset.treeIcon.startsWith('folder') ){
					 currentNode.children[0].classList.remove('ti-folder');
					 currentNode.children[0].classList.add('ti-folder-open');
				}
				
				if( el === null ){
					return 
				}
				do {
					el.classList.toggle( this.options.treeOpenNode );
					el = el.parentNode.closest( `.${this.options.treeNode}` );
					if( el === null ){
						break 
					}
				} while ( !el.parentNode.classList.contains( this.options.treeWrapper ) );

				// Save to local storage if enabled
				if( this.options.useLocalStorage ){
					this.setLocalStorageValue( element );
				}
			}
		}

		/**
		 * Saves the selected node to local storage.
		 * @param {string} value - The selector of the selected node.
		 */
		setLocalStorageValue( value ) {
			try {
				localStorage.setItem( this.localStorageKey, value );
			} catch ( e ) {
				console.warn( 'LuteceTree: Unable to save to local storage', e );
			}
		}

		/**
		 * Retrieves the selected node value from local storage.
		 * @returns {string|null} The stored node selector or null if not found.
		 */
		getLocalStorageValue() {
			try {
				return localStorage.getItem( this.localStorageKey );
			} catch ( e ) {
				console.warn( 'LuteceTree: Unable to read from local storage', e );
				return null;
			}
		}

		/**
		 * Retrieves a value from local storage with the treeNode prefix.
		 * @param {string} key - The key suffix to retrieve (will be prefixed with 'treeNode_').
		 * @returns {string|null} The stored value or null if not found.
		 */
		static getTreeNodeStorageValue( key ) {
			try {
				return localStorage.getItem( `treeNode_${key}` );
			} catch ( e ) {
				console.warn( 'LuteceTree: Unable to read from local storage', e );
				return null;
			}
		}

		/**
		 * Clears the selected node from local storage.
		 */
		clearLocalStorageValue() {
			try {
				localStorage.removeItem( this.localStorageKey );
			} catch ( e ) {
				console.warn( 'LuteceTree: Unable to clear local storage', e );
			}
		}
	
	}