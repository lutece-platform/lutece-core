/*
 * Entry of the Milkdown bundle of the back office: the Crepe builder, the features used by Lutece (no CodeMirror, LaTeX nor AI: nothing
 * fetched from a CDN, no KaTeX fonts), and the ProseMirror HTML parser and serializer, exposed as window.LuteceMilkdownKit.
 * Build: see README.md in this folder.
 */
import { CrepeBuilder } from '@milkdown/crepe/builder';
import { blockEdit } from '@milkdown/crepe/feature/block-edit';
import { toolbar } from '@milkdown/crepe/feature/toolbar';
import { topBar } from '@milkdown/crepe/feature/top-bar';
import { linkTooltip } from '@milkdown/crepe/feature/link-tooltip';
import { imageBlock } from '@milkdown/crepe/feature/image-block';
import { listItem } from '@milkdown/crepe/feature/list-item';
import { placeholder } from '@milkdown/crepe/feature/placeholder';
import { table } from '@milkdown/crepe/feature/table';
import { cursor } from '@milkdown/crepe/feature/cursor';
import { editorViewCtx, schemaCtx } from '@milkdown/kit/core';
import { DOMParser, DOMSerializer } from '@milkdown/kit/prose/model';

import '@milkdown/crepe/theme/common/prosemirror.css';
import '@milkdown/crepe/theme/common/reset.css';
import '@milkdown/crepe/theme/common/block-edit.css';
import '@milkdown/crepe/theme/common/cursor.css';
import '@milkdown/crepe/theme/common/image-block.css';
import '@milkdown/crepe/theme/common/link-tooltip.css';
import '@milkdown/crepe/theme/common/list-item.css';
import '@milkdown/crepe/theme/common/placeholder.css';
import '@milkdown/crepe/theme/common/toolbar.css';
import '@milkdown/crepe/theme/common/table.css';
import '@milkdown/crepe/theme/common/top-bar.css';
import '@milkdown/crepe/theme/frame.css';
import './lutece.css';

window.LuteceMilkdownKit = {
	CrepeBuilder,
	features: { blockEdit, toolbar, topBar, linkTooltip, imageBlock, listItem, placeholder, table, cursor },
	editorViewCtx,
	schemaCtx,
	DOMParser,
	DOMSerializer
};
