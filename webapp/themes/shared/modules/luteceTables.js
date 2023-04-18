/**
A class for searching through a list of elements and highlighting search matches.
*/
export default class LuteceTables {
  /**
   * Creates a new instance of LuteceSearchList.
   * @param {HTMLElement} tableElement - The table to add logiv .
   * @param {NodeList} tableElementList - The list of elements to search through.
   */
  constructor( table, tableElementList ) {
    this.table = '#lutece-table';
    this.tableElementList = '[data-url]';
  }
  
  /**
  * Configures the draggable elements to make them draggable.
  */
  setClickableTableRow() {
    this.tableElementList.forEach((tableElement) => {
        tableElement.addEventListener("click", function () {
            document.location.replace( tableElement.dataset.url );
        });
    });
  }

}