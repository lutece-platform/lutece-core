/*!
* Tabler v1.6.1 (https://tabler.io)
* Copyright 2018-2026 The Tabler Authors
* Copyright 2018-2026 codecalm.net Paweł Kuna
* Licensed under MIT (https://github.com/tabler/tabler/blob/master/LICENSE)
*/
//#region \0rolldown/runtime.js
var __defProp = Object.defineProperty;
var __exportAll = (all, no_symbols) => {
	let target = {};
	for (var name in all) __defProp(target, name, {
		get: all[name],
		enumerable: true
	});
	if (!no_symbols) __defProp(target, Symbol.toStringTag, { value: "Module" });
	return target;
};
//#endregion
//#region js/src/bootstrap/dom/data.ts
/**
* --------------------------------------------------------------------------
* Bootstrap dom/data.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var elementMap = /* @__PURE__ */ new Map();
var Data = {
	set(element, key, instance) {
		if (!elementMap.has(element)) elementMap.set(element, /* @__PURE__ */ new Map());
		const instanceMap = elementMap.get(element);
		if (!instanceMap.has(key) && instanceMap.size !== 0) {
			console.error(`Bootstrap doesn't allow more than one instance per element. Bound instance: ${Array.from(instanceMap.keys())[0]}.`);
			return;
		}
		instanceMap.set(key, instance);
	},
	get(element, key) {
		if (elementMap.has(element)) return elementMap.get(element).get(key) || null;
		return null;
	},
	remove(element, key) {
		if (!elementMap.has(element)) return;
		const instanceMap = elementMap.get(element);
		instanceMap.delete(key);
		if (instanceMap.size === 0) elementMap.delete(element);
	}
};
//#endregion
//#region js/src/bootstrap/util/index.ts
var MAX_UID = 1e6;
var MILLISECONDS_MULTIPLIER = 1e3;
var TRANSITION_END = "transitionend";
var parseSelector = (selector) => {
	if (selector && typeof CSS !== "undefined" && typeof CSS.escape === "function") selector = selector.replace(/#([^\s"#']+)/g, (match, id) => `#${CSS.escape(id)}`);
	return selector;
};
var toType = (object) => {
	if (object === null || object === void 0) return `${object}`;
	return Object.prototype.toString.call(object).match(/\s([a-z]+)/i)[1].toLowerCase();
};
var getUID = (prefix) => {
	do
		prefix += Math.floor(Math.random() * MAX_UID);
	while (document.getElementById(prefix));
	return prefix;
};
var getTransitionDurationFromElement = (element) => {
	if (!element) return 0;
	let { transitionDuration, transitionDelay } = window.getComputedStyle(element);
	if (!Number.parseFloat(transitionDuration) && !Number.parseFloat(transitionDelay)) return 0;
	transitionDuration = transitionDuration.split(",")[0];
	transitionDelay = transitionDelay.split(",")[0];
	return (Number.parseFloat(transitionDuration) + Number.parseFloat(transitionDelay)) * MILLISECONDS_MULTIPLIER;
};
var triggerTransitionEnd = (element) => {
	element.dispatchEvent(new Event(TRANSITION_END));
};
var unwrapJQuery = (object) => {
	if (object && typeof object === "object" && typeof object.jquery !== "undefined") return object[0];
	return object;
};
var isElement$1 = (object) => {
	object = unwrapJQuery(object);
	if (!object || typeof object !== "object") return false;
	return typeof object.nodeType !== "undefined";
};
var getElement = (object) => {
	object = unwrapJQuery(object);
	if (isElement$1(object)) return object;
	if (typeof object === "string" && object.length > 0) return document.querySelector(parseSelector(object));
	return null;
};
var isVisible = (element) => {
	if (!isElement$1(element) || element.getClientRects().length === 0) return false;
	const elementIsVisible = getComputedStyle(element).getPropertyValue("visibility") === "visible";
	const closedDetails = element.closest("details:not([open])");
	if (!closedDetails) return elementIsVisible;
	if (closedDetails !== element) {
		const summary = element.closest("summary");
		if (summary && summary.parentNode !== closedDetails) return false;
		if (summary === null) return false;
	}
	return elementIsVisible;
};
var isDisabled = (element) => {
	if (!element || element.nodeType !== Node.ELEMENT_NODE) return true;
	if (element.classList.contains("disabled")) return true;
	if ("disabled" in element && typeof element.disabled !== "undefined") return Boolean(element.disabled);
	return element.hasAttribute("disabled") && element.getAttribute("disabled") !== "false";
};
var findShadowRoot = (element) => {
	if (!document.documentElement.attachShadow) return null;
	if (typeof element.getRootNode === "function") {
		const root = element.getRootNode();
		return root instanceof ShadowRoot ? root : null;
	}
	if (element instanceof ShadowRoot) return element;
	if (!element.parentNode) return null;
	return findShadowRoot(element.parentNode);
};
var noop = () => {};
/**
* Trick to restart an element's animation
*
* @see https://www.harrytheo.com/blog/2021/02/restart-a-css-animation-with-javascript/#restarting-a-css-animation
*/
var reflow = (element) => {
	element.offsetHeight;
};
var isRTL = () => document.documentElement.dir === "rtl";
var getjQuery = () => {
	if (window.jQuery && !document.body.hasAttribute("data-bs-no-jquery") && !document.body.hasAttribute("data-tblr-no-jquery")) return window.jQuery;
	return null;
};
var DOMContentLoadedCallbacks = [];
var onDOMContentLoaded = (callback) => {
	if (document.readyState === "loading") {
		if (!DOMContentLoadedCallbacks.length) document.addEventListener("DOMContentLoaded", () => {
			for (const domCallback of DOMContentLoadedCallbacks) domCallback();
		});
		DOMContentLoadedCallbacks.push(callback);
	} else callback();
};
var defineJQueryPlugin = (plugin) => {
	onDOMContentLoaded(() => {
		const $ = getjQuery();
		if ($) {
			const name = plugin.NAME;
			const JQUERY_NO_CONFLICT = $.fn[name];
			$.fn[name] = plugin.jQueryInterface;
			$.fn[name].Constructor = plugin;
			$.fn[name].noConflict = () => {
				$.fn[name] = JQUERY_NO_CONFLICT;
				return plugin.jQueryInterface;
			};
		}
	});
};
var execute = (possibleCallback, args = [], defaultValue = possibleCallback) => {
	return typeof possibleCallback === "function" ? possibleCallback.call(args[0], ...args.slice(1)) : defaultValue;
};
var executeAfterTransition = (callback, transitionElement, waitForTransition = true) => {
	if (!waitForTransition) {
		execute(callback);
		return;
	}
	const emulatedDuration = getTransitionDurationFromElement(transitionElement) + 5;
	let called = false;
	const handler = ({ target }) => {
		if (target !== transitionElement) return;
		called = true;
		transitionElement.removeEventListener(TRANSITION_END, handler);
		execute(callback);
	};
	transitionElement.addEventListener(TRANSITION_END, handler);
	setTimeout(() => {
		if (!called) triggerTransitionEnd(transitionElement);
	}, emulatedDuration);
};
var getNextActiveElement = (list, activeElement, shouldGetNext, isCycleAllowed) => {
	const listLength = list.length;
	let index = list.indexOf(activeElement);
	if (index === -1) return !shouldGetNext && isCycleAllowed ? list[listLength - 1] : list[0];
	index += shouldGetNext ? 1 : -1;
	if (isCycleAllowed) index = (index + listLength) % listLength;
	return list[Math.max(0, Math.min(index, listLength - 1))];
};
//#endregion
//#region js/src/bootstrap/dom/event-handler.ts
/**
* --------------------------------------------------------------------------
* Bootstrap dom/event-handler.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var namespaceRegex = /[^.]*(?=\..*)\.|.*/;
var stripNameRegex = /\..*/;
var stripUidRegex = /::\d+$/;
var eventRegistry = {};
var uidEvent = 1;
var customEvents = {
	mouseenter: "mouseover",
	mouseleave: "mouseout"
};
var nativeEvents = /* @__PURE__ */ new Set([
	"click",
	"dblclick",
	"mouseup",
	"mousedown",
	"contextmenu",
	"mousewheel",
	"DOMMouseScroll",
	"mouseover",
	"mouseout",
	"mousemove",
	"selectstart",
	"selectend",
	"keydown",
	"keypress",
	"keyup",
	"orientationchange",
	"touchstart",
	"touchmove",
	"touchend",
	"touchcancel",
	"pointerdown",
	"pointermove",
	"pointerup",
	"pointerleave",
	"pointercancel",
	"gesturestart",
	"gesturechange",
	"gestureend",
	"focus",
	"blur",
	"change",
	"reset",
	"select",
	"submit",
	"focusin",
	"focusout",
	"load",
	"unload",
	"beforeunload",
	"resize",
	"move",
	"DOMContentLoaded",
	"readystatechange",
	"error",
	"abort",
	"scroll"
]);
function makeEventUid(element, uid) {
	return uid && `${uid}::${uidEvent++}` || element.uidEvent || uidEvent++;
}
function getElementEvents(element) {
	const uid = makeEventUid(element);
	element.uidEvent = uid;
	eventRegistry[uid] = eventRegistry[uid] || {};
	return eventRegistry[uid];
}
function bootstrapHandler(element, fn) {
	return function handler(event) {
		hydrateObj(event, { delegateTarget: element });
		if (handler.oneOff) EventHandler.off(element, event.type, fn);
		return fn.apply(element, [event]);
	};
}
function bootstrapDelegationHandler(element, selector, fn) {
	return function handler(event) {
		const domElements = element.querySelectorAll(selector);
		for (let { target } = event; target && target !== this; target = target.parentNode) for (const domElement of domElements) {
			if (domElement !== target) continue;
			hydrateObj(event, { delegateTarget: target });
			if (handler.oneOff) EventHandler.off(element, event.type, selector, fn);
			return fn.apply(target, [event]);
		}
	};
}
function findHandler(events, callable, delegationSelector = null) {
	return Object.values(events).find((event) => event.callable === callable && event.delegationSelector === delegationSelector);
}
function normalizeParameters(originalTypeEvent, handler, delegationFunction) {
	const isDelegated = typeof handler === "string";
	const callable = isDelegated ? delegationFunction : handler || delegationFunction;
	let typeEvent = getTypeEvent(originalTypeEvent);
	if (!nativeEvents.has(typeEvent)) typeEvent = originalTypeEvent;
	return [
		isDelegated,
		callable,
		typeEvent
	];
}
function addHandler(element, originalTypeEvent, handler, delegationFunction, oneOff) {
	if (typeof originalTypeEvent !== "string" || !element) return;
	let [isDelegated, callable, typeEvent] = normalizeParameters(originalTypeEvent, handler, delegationFunction);
	if (originalTypeEvent in customEvents) {
		const wrapFunction = (fn) => {
			return function(event) {
				const evt = event;
				if (!evt.relatedTarget || evt.relatedTarget !== evt.delegateTarget && !evt.delegateTarget.contains(evt.relatedTarget)) return fn.call(this, event);
			};
		};
		callable = wrapFunction(callable);
	}
	const events = getElementEvents(element);
	const handlers = events[typeEvent] || (events[typeEvent] = {});
	const previousFunction = findHandler(handlers, callable, isDelegated ? handler : null);
	if (previousFunction) {
		previousFunction.oneOff = previousFunction.oneOff && oneOff;
		return;
	}
	const uid = makeEventUid(callable, originalTypeEvent.replace(namespaceRegex, ""));
	const fn = isDelegated ? bootstrapDelegationHandler(element, handler, callable) : bootstrapHandler(element, callable);
	fn.delegationSelector = isDelegated ? handler : null;
	fn.callable = callable;
	fn.oneOff = oneOff;
	fn.uidEvent = uid;
	handlers[uid] = fn;
	element.addEventListener(typeEvent, fn, isDelegated);
}
function removeHandler(element, events, typeEvent, handler, delegationSelector) {
	const fn = findHandler(events[typeEvent], handler, delegationSelector !== null && delegationSelector !== void 0 ? delegationSelector : null);
	if (!fn) return;
	element.removeEventListener(typeEvent, fn, Boolean(delegationSelector));
	delete events[typeEvent][fn.uidEvent];
}
function removeNamespacedHandlers(element, events, typeEvent, namespace) {
	const storeElementEvent = events[typeEvent] || {};
	for (const [handlerKey, event] of Object.entries(storeElementEvent)) if (handlerKey.includes(namespace)) removeHandler(element, events, typeEvent, event.callable, event.delegationSelector);
}
function getTypeEvent(event) {
	event = event.replace(stripNameRegex, "");
	return customEvents[event] || event;
}
var EventHandler = {
	on(element, event, handler, delegationFunction) {
		addHandler(element, event, handler, delegationFunction, false);
	},
	one(element, event, handler, delegationFunction) {
		addHandler(element, event, handler, delegationFunction, true);
	},
	off(element, originalTypeEvent, handler, delegationFunction) {
		if (typeof originalTypeEvent !== "string" || !element) return;
		const [isDelegated, callable, typeEvent] = normalizeParameters(originalTypeEvent, handler, delegationFunction);
		const inNamespace = typeEvent !== originalTypeEvent;
		const events = getElementEvents(element);
		const storeElementEvent = events[typeEvent] || {};
		const isNamespace = originalTypeEvent.startsWith(".");
		if (typeof callable !== "undefined") {
			if (!Object.keys(storeElementEvent).length) return;
			removeHandler(element, events, typeEvent, callable, isDelegated ? handler : null);
			return;
		}
		if (isNamespace) for (const elementEvent of Object.keys(events)) removeNamespacedHandlers(element, events, elementEvent, originalTypeEvent.slice(1));
		for (const [keyHandlers, event] of Object.entries(storeElementEvent)) {
			const handlerKey = keyHandlers.replace(stripUidRegex, "");
			if (!inNamespace || originalTypeEvent.includes(handlerKey)) removeHandler(element, events, typeEvent, event.callable, event.delegationSelector);
		}
	},
	trigger(element, event, args) {
		if (typeof event !== "string" || !element) return null;
		const $ = getjQuery();
		const inNamespace = event !== getTypeEvent(event);
		let jQueryEvent = null;
		let bubbles = true;
		let nativeDispatch = true;
		let defaultPrevented = false;
		if (inNamespace && $) {
			jQueryEvent = $.Event(event, args);
			$(element).trigger(jQueryEvent);
			bubbles = !jQueryEvent.isPropagationStopped();
			nativeDispatch = !jQueryEvent.isImmediatePropagationStopped();
			defaultPrevented = jQueryEvent.isDefaultPrevented();
		}
		const evt = hydrateObj(new Event(event, {
			bubbles,
			cancelable: true
		}), args);
		if (defaultPrevented) evt.preventDefault();
		if (nativeDispatch) element.dispatchEvent(evt);
		if (evt.defaultPrevented && jQueryEvent) jQueryEvent.preventDefault();
		return evt;
	}
};
function hydrateObj(obj, meta = {}) {
	for (const [key, value] of Object.entries(meta)) try {
		obj[key] = value;
	} catch (_unused) {
		Object.defineProperty(obj, key, {
			configurable: true,
			get() {
				return value;
			}
		});
	}
	return obj;
}
//#endregion
//#region js/src/bootstrap/dom/manipulator.ts
function normalizeData(value) {
	if (value === "true") return true;
	if (value === "false") return false;
	if (value === Number(value).toString()) return Number(value);
	if (value === "" || value === "null") return null;
	if (typeof value !== "string") return value;
	try {
		return JSON.parse(decodeURIComponent(value));
	} catch (_unused) {
		return value;
	}
}
function normalizeDataKey(key) {
	return key.replace(/[A-Z]/g, (chr) => `-${chr.toLowerCase()}`);
}
var PREFIXES = ["tblr", "bs"];
var Manipulator = {
	setDataAttribute(element, key, value) {
		element.setAttribute(`data-bs-${normalizeDataKey(key)}`, value);
	},
	removeDataAttribute(element, key) {
		for (const prefix of PREFIXES) element.removeAttribute(`data-${prefix}-${normalizeDataKey(key)}`);
	},
	getDataAttributes(element) {
		if (!element) return {};
		const attributes = {};
		for (const prefix of PREFIXES) {
			const keys = Object.keys(element.dataset).filter((key) => key.startsWith(prefix) && !key.startsWith(`${prefix}Config`));
			for (const key of keys) {
				let pureKey = key.replace(new RegExp(`^${prefix}`), "");
				pureKey = pureKey.charAt(0).toLowerCase() + pureKey.slice(1);
				if (!(pureKey in attributes)) attributes[pureKey] = normalizeData(element.dataset[key]);
			}
		}
		return attributes;
	},
	getDataAttribute(element, key) {
		for (const prefix of PREFIXES) {
			const value = element.getAttribute(`data-${prefix}-${normalizeDataKey(key)}`);
			if (value !== null) return normalizeData(value);
		}
		return null;
	}
};
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/typeof.js
function _typeof(o) {
	"@babel/helpers - typeof";
	return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function(o) {
		return typeof o;
	} : function(o) {
		return o && "function" == typeof Symbol && o.constructor === Symbol && o !== Symbol.prototype ? "symbol" : typeof o;
	}, _typeof(o);
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/toPrimitive.js
function toPrimitive(t, r) {
	if ("object" != _typeof(t) || !t) return t;
	var e = t[Symbol.toPrimitive];
	if (void 0 !== e) {
		var i = e.call(t, r || "default");
		if ("object" != _typeof(i)) return i;
		throw new TypeError("@@toPrimitive must return a primitive value.");
	}
	return ("string" === r ? String : Number)(t);
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/toPropertyKey.js
function toPropertyKey(t) {
	var i = toPrimitive(t, "string");
	return "symbol" == _typeof(i) ? i : i + "";
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/defineProperty.js
function _defineProperty(e, r, t) {
	return (r = toPropertyKey(r)) in e ? Object.defineProperty(e, r, {
		value: t,
		enumerable: !0,
		configurable: !0,
		writable: !0
	}) : e[r] = t, e;
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/objectSpread2.js
function ownKeys(e, r) {
	var t = Object.keys(e);
	if (Object.getOwnPropertySymbols) {
		var o = Object.getOwnPropertySymbols(e);
		r && (o = o.filter(function(r) {
			return Object.getOwnPropertyDescriptor(e, r).enumerable;
		})), t.push.apply(t, o);
	}
	return t;
}
function _objectSpread2(e) {
	for (var r = 1; r < arguments.length; r++) {
		var t = null != arguments[r] ? arguments[r] : {};
		r % 2 ? ownKeys(Object(t), !0).forEach(function(r) {
			_defineProperty(e, r, t[r]);
		}) : Object.getOwnPropertyDescriptors ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(t)) : ownKeys(Object(t)).forEach(function(r) {
			Object.defineProperty(e, r, Object.getOwnPropertyDescriptor(t, r));
		});
	}
	return e;
}
//#endregion
//#region js/src/bootstrap/util/config.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/config.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var Config = class {
	static get Default() {
		return {};
	}
	static get DefaultType() {
		return {};
	}
	static get NAME() {
		throw new Error("You have to implement the static method \"NAME\", for each component!");
	}
	_getConfig(config) {
		config = this._mergeConfigObj(config);
		config = this._configAfterMerge(config);
		this._typeCheckConfig(config);
		return config;
	}
	_configAfterMerge(config) {
		return config;
	}
	_mergeConfigObj(config, element) {
		const jsonConfig = isElement$1(element) ? Manipulator.getDataAttribute(element, "config") : {};
		const ctor = this.constructor;
		return _objectSpread2(_objectSpread2(_objectSpread2(_objectSpread2({}, ctor.Default), typeof jsonConfig === "object" ? jsonConfig : {}), isElement$1(element) ? Manipulator.getDataAttributes(element) : {}), typeof config === "object" ? config : {});
	}
	_typeCheckConfig(config, configTypes) {
		const ctor = this.constructor;
		const types = configTypes || ctor.DefaultType;
		for (const [property, expectedTypes] of Object.entries(types)) {
			const value = config[property];
			const valueType = isElement$1(value) ? "element" : toType(value);
			if (!new RegExp(expectedTypes).test(valueType)) throw new TypeError(`${ctor.NAME.toUpperCase()}: Option "${property}" provided type "${valueType}" but expected type "${expectedTypes}".`);
		}
	}
};
//#endregion
//#region js/src/bootstrap/base-component.ts
/**
* --------------------------------------------------------------------------
* Bootstrap base-component.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var VERSION = "5.3.8";
var BaseComponent = class extends Config {
	constructor(element, config) {
		var _Data$get;
		super();
		const resolved = getElement(element);
		if (!resolved) return;
		const ctor = this.constructor;
		(_Data$get = Data.get(resolved, ctor.DATA_KEY)) === null || _Data$get === void 0 || _Data$get.dispose();
		this._element = resolved;
		this._config = this._getConfig(config);
		Data.set(this._element, ctor.DATA_KEY, this);
	}
	dispose() {
		const ctor = this.constructor;
		Data.remove(this._element, ctor.DATA_KEY);
		EventHandler.off(this._element, ctor.EVENT_KEY);
		for (const propertyName of Object.getOwnPropertyNames(this)) this[propertyName] = null;
	}
	_queueCallback(callback, element, isAnimated = true) {
		executeAfterTransition(() => {
			if (this._element) callback();
		}, element, isAnimated);
	}
	_getConfig(config) {
		config = this._mergeConfigObj(config, this._element);
		config = this._configAfterMerge(config);
		this._typeCheckConfig(config);
		return config;
	}
	static getInstance(element) {
		return Data.get(getElement(element), this.DATA_KEY);
	}
	static getOrCreateInstance(element, config = {}) {
		return this.getInstance(element) || new this(element, typeof config === "object" ? config : void 0);
	}
	static get VERSION() {
		return VERSION;
	}
	static get DATA_KEY() {
		return `bs.${this.NAME}`;
	}
	static get EVENT_KEY() {
		return `.${this.DATA_KEY}`;
	}
	static eventName(name) {
		return `${name}${this.EVENT_KEY}`;
	}
};
//#endregion
//#region js/src/bootstrap/dom/selector-engine.ts
/**
* --------------------------------------------------------------------------
* Bootstrap dom/selector-engine.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var getSelector = (element) => {
	let selector = element.getAttribute("data-tblr-target") || element.getAttribute("data-bs-target");
	if (!selector || selector === "#") {
		let hrefAttribute = element.getAttribute("href");
		if (!hrefAttribute || !hrefAttribute.includes("#") && !hrefAttribute.startsWith(".")) return null;
		if (hrefAttribute.includes("#") && !hrefAttribute.startsWith("#")) hrefAttribute = `#${hrefAttribute.split("#")[1]}`;
		selector = hrefAttribute && hrefAttribute !== "#" ? hrefAttribute.trim() : null;
	}
	return selector ? selector.split(",").map((sel) => parseSelector(sel)).join(",") : null;
};
var SelectorEngine = {
	find(selector, element = document.documentElement) {
		return Array.from(element.querySelectorAll(selector));
	},
	findOne(selector, element = document.documentElement) {
		return element.querySelector(selector);
	},
	children(element, selector) {
		return Array.from(element.children).filter((child) => child.matches(selector));
	},
	parents(element, selector) {
		const parents = [];
		let ancestor = element.parentNode && element.parentNode.closest(selector);
		while (ancestor) {
			parents.push(ancestor);
			ancestor = ancestor.parentNode && ancestor.parentNode.closest(selector);
		}
		return parents;
	},
	prev(element, selector) {
		let previous = element.previousElementSibling;
		while (previous) {
			if (previous.matches(selector)) return [previous];
			previous = previous.previousElementSibling;
		}
		return [];
	},
	next(element, selector) {
		let next = element.nextElementSibling;
		while (next) {
			if (next.matches(selector)) return [next];
			next = next.nextElementSibling;
		}
		return [];
	},
	focusableChildren(element) {
		const focusables = [
			"a",
			"button",
			"input",
			"textarea",
			"select",
			"details",
			"[tabindex]",
			"[contenteditable=\"true\"]"
		].map((selector) => `${selector}:not([tabindex^="-"])`).join(",");
		return this.find(focusables, element).filter((el) => !isDisabled(el) && isVisible(el));
	},
	getSelectorFromElement(element) {
		const selector = getSelector(element);
		if (selector) return SelectorEngine.findOne(selector) ? selector : null;
		return null;
	},
	getElementFromSelector(element) {
		const selector = getSelector(element);
		return selector ? SelectorEngine.findOne(selector) : null;
	},
	getMultipleElementsFromSelector(element) {
		const selector = getSelector(element);
		return selector ? SelectorEngine.find(selector) : [];
	}
};
//#endregion
//#region js/src/bootstrap/util/component-functions.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/component-functions.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var callMethod = (instance, method) => {
	const callable = instance[method];
	if (typeof callable === "function") callable.call(instance);
};
var enableDismissTrigger = (component, method = "hide") => {
	const clickEvent = `click.dismiss${component.EVENT_KEY}`;
	const name = component.NAME;
	EventHandler.on(document, clickEvent, `[data-bs-dismiss="${name}"], [data-tblr-dismiss="${name}"]`, function(event) {
		if (["A", "AREA"].includes(this.tagName)) event.preventDefault();
		if (isDisabled(this)) return;
		const target = SelectorEngine.getElementFromSelector(this) || this.closest(`.${name}`);
		callMethod(component.getOrCreateInstance(target), method);
	});
};
/**
* Creates the component for every element that matches on page load. One
* element with a broken config must not stop the rest of the bundle, so the
* error is logged and the loop goes on.
*/
var initAll = (selector, Plugin, filter = null) => {
	for (const element of SelectorEngine.find(selector)) {
		if (filter && !filter(element)) continue;
		try {
			Plugin.getOrCreateInstance(element);
		} catch (error) {
			console.error(error);
		}
	}
};
//#endregion
//#region js/src/autosize.ts
/**
* --------------------------------------------------------------------------
* Tabler autosize.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$26 = "autosize";
var EVENT_RESIZED = `resized${`.${`bs.${NAME$26}`}`}`;
var SELECTOR_DATA_TOGGLE$12 = `[data-bs-toggle="${NAME$26}"], [data-tblr-toggle="${NAME$26}"]`;
var Default$23 = {};
var DefaultType$23 = {};
/**
* Class definition
*
* Grows a textarea with its content and shrinks it back when text is removed.
* The height follows `scrollHeight`, so `rows` sets the starting height and
* `max-height` caps the growth, after which the field scrolls again.
*/
var Autosize = class extends BaseComponent {
	constructor(element, config) {
		var _this$_element$getAtt;
		super(element, config);
		this._observer = null;
		this._inlineStyle = "";
		this._width = 0;
		this._onInput = () => this.update();
		if (!this._element) return;
		this._inlineStyle = (_this$_element$getAtt = this._element.getAttribute("style")) !== null && _this$_element$getAtt !== void 0 ? _this$_element$getAtt : "";
		this._element.style.resize = "none";
		this._element.style.overflowY = "hidden";
		this._element.addEventListener("input", this._onInput);
		if (typeof ResizeObserver !== "undefined") {
			this._observer = new ResizeObserver(() => {
				if (this._element.offsetWidth !== this._width) {
					this._width = this._element.offsetWidth;
					this.update();
				}
			});
			this._observer.observe(this._element);
		}
		this.update();
	}
	static get Default() {
		return Default$23;
	}
	static get DefaultType() {
		return DefaultType$23;
	}
	static get NAME() {
		return NAME$26;
	}
	update() {
		const element = this._element;
		if (!element.isConnected || element.getClientRects().length === 0) return;
		const { scrollTop } = document.documentElement;
		const style = getComputedStyle(element);
		const extra = style.boxSizing === "border-box" ? Number.parseFloat(style.borderTopWidth) + Number.parseFloat(style.borderBottomWidth) : -(Number.parseFloat(style.paddingTop) + Number.parseFloat(style.paddingBottom));
		const before = element.style.height;
		element.style.overflowY = "hidden";
		element.style.height = "auto";
		const height = `${element.scrollHeight + extra}px`;
		element.style.height = height;
		element.style.overflowY = element.scrollHeight > element.clientHeight ? "auto" : "hidden";
		document.documentElement.scrollTop = scrollTop;
		if (height !== before) EventHandler.trigger(element, EVENT_RESIZED);
	}
	dispose() {
		var _this$_observer;
		this._element.removeEventListener("input", this._onInput);
		(_this$_observer = this._observer) === null || _this$_observer === void 0 || _this$_observer.disconnect();
		if (this._inlineStyle) this._element.setAttribute("style", this._inlineStyle);
		else this._element.removeAttribute("style");
		super.dispose();
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_TOGGLE$12, Autosize);
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/asyncToGenerator.js
function asyncGeneratorStep(n, t, e, r, o, a, c) {
	try {
		var i = n[a](c), u = i.value;
	} catch (n) {
		e(n);
		return;
	}
	i.done ? t(u) : Promise.resolve(u).then(r, o);
}
function _asyncToGenerator(n) {
	return function() {
		var t = this, e = arguments;
		return new Promise(function(r, o) {
			var a = n.apply(t, e);
			function _next(n) {
				asyncGeneratorStep(a, r, o, _next, _throw, "next", n);
			}
			function _throw(n) {
				asyncGeneratorStep(a, r, o, _next, _throw, "throw", n);
			}
			_next(void 0);
		});
	};
}
//#endregion
//#region js/src/clipboard.ts
/**
* --------------------------------------------------------------------------
* Tabler clipboard.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$25 = "clipboard";
var EVENT_KEY$18 = `.${`bs.${NAME$25}`}`;
var EVENT_COPIED = `copied${EVENT_KEY$18}`;
var EVENT_ERROR = `error${EVENT_KEY$18}`;
var CLASS_NAME_COPIED = "copied";
var SELECTOR_DATA_TOGGLE$11 = `[data-bs-toggle="${NAME$25}"], [data-tblr-toggle="${NAME$25}"]`;
var SELECTOR_LABEL = `.${NAME$25}-label`;
var SELECTOR_FEEDBACK = `.${NAME$25}-feedback`;
var ATTRIBUTE_TEXT = "data-bs-text";
var ATTRIBUTE_TEXT_ALIAS = "data-tblr-text";
var Default$22 = {
	target: null,
	text: null,
	delay: 2e3
};
var DefaultType$22 = {
	target: "(string|null)",
	text: "(string|number|null)",
	delay: "number"
};
/**
* Helpers
*/
var toggleHidden = (element, hidden) => {
	if (hidden) element.setAttribute("hidden", "");
	else element.removeAttribute("hidden");
};
/**
* Class definition
*
* Copies text to the clipboard when the trigger is clicked, then shows the
* copied state for a moment. Every word lives in the markup: the trigger holds
* a `clipboard-label` and a `clipboard-feedback`, and the component only swaps
* which of them is hidden.
*
* The browser gives `navigator.clipboard` to secure contexts only, so on plain
* http a copy fails and `error.bs.clipboard` fires.
*/
var Clipboard = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._labels = [];
		this._feedbacks = [];
		this._timeout = 0;
		if (!this._element) return;
		this._labels = SelectorEngine.find(SELECTOR_LABEL, this._element);
		this._feedbacks = SelectorEngine.find(SELECTOR_FEEDBACK, this._element);
		for (const feedback of this._feedbacks) {
			toggleHidden(feedback, true);
			if (!feedback.hasAttribute("role")) feedback.setAttribute("role", "status");
		}
		EventHandler.on(this._element, `click${EVENT_KEY$18}`, (event) => {
			event.preventDefault();
			this.copy();
		});
	}
	static get Default() {
		return Default$22;
	}
	static get DefaultType() {
		return DefaultType$22;
	}
	static get NAME() {
		return NAME$25;
	}
	get text() {
		var _source$textContent;
		const { text, target } = this._config;
		if (text !== null && text !== "") return String(text);
		const source = target ? getElement(target) : null;
		if (!source) return "";
		if (source instanceof HTMLInputElement || source instanceof HTMLTextAreaElement) return source.value;
		return (source instanceof HTMLElement ? source.innerText : (_source$textContent = source.textContent) !== null && _source$textContent !== void 0 ? _source$textContent : "").trim();
	}
	copy() {
		var _this = this;
		return _asyncToGenerator(function* () {
			const { text } = _this;
			if (!text || !navigator.clipboard) {
				EventHandler.trigger(_this._element, EVENT_ERROR);
				return;
			}
			try {
				yield navigator.clipboard.writeText(text);
			} catch (_unused) {
				EventHandler.trigger(_this._element, EVENT_ERROR);
				return;
			}
			_this._showCopied();
			EventHandler.trigger(_this._element, EVENT_COPIED);
		})();
	}
	/** Ends the copied state, for a `delay` of 0 or an early reset. */
	reset() {
		window.clearTimeout(this._timeout);
		this._toggleCopied(false);
	}
	dispose() {
		window.clearTimeout(this._timeout);
		super.dispose();
	}
	_mergeConfigObj(config, element) {
		var _element$getAttribute;
		const raw = (_element$getAttribute = element === null || element === void 0 ? void 0 : element.getAttribute(ATTRIBUTE_TEXT)) !== null && _element$getAttribute !== void 0 ? _element$getAttribute : element === null || element === void 0 ? void 0 : element.getAttribute(ATTRIBUTE_TEXT_ALIAS);
		const dataOptions = raw === null || raw === void 0 ? {} : { text: raw };
		return super._mergeConfigObj(_objectSpread2(_objectSpread2({}, dataOptions), config), element);
	}
	_showCopied() {
		window.clearTimeout(this._timeout);
		this._toggleCopied(true);
		if (this._config.delay > 0) this._timeout = window.setTimeout(() => this._toggleCopied(false), this._config.delay);
	}
	_toggleCopied(copied) {
		this._element.classList.toggle(CLASS_NAME_COPIED, copied);
		for (const label of this._labels) toggleHidden(label, copied);
		for (const feedback of this._feedbacks) toggleHidden(feedback, !copied);
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_TOGGLE$11, Clipboard);
//#endregion
//#region js/src/confetti.ts
/**
* --------------------------------------------------------------------------
* Tabler confetti.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$24 = "confetti";
var EVENT_KEY$17 = `.${`bs.${NAME$24}`}`;
var EVENT_START$1 = `start${EVENT_KEY$17}`;
var EVENT_END = `end${EVENT_KEY$17}`;
var EVENT_CLICK_DATA_API$8 = `click${EVENT_KEY$17}.data-api`;
var CLASS_NAME_CANVAS = `${NAME$24}-canvas`;
var SELECTOR_DATA_TOGGLE$10 = `[data-bs-toggle="${NAME$24}"], [data-tblr-toggle="${NAME$24}"]`;
var PALETTE = [
	["blue", "#066fd1"],
	["azure", "#4299e1"],
	["indigo", "#4263eb"],
	["purple", "#ae3ec9"],
	["pink", "#d6336c"],
	["red", "#d63939"],
	["orange", "#f76707"],
	["yellow", "#f59f00"],
	["lime", "#74b816"],
	["green", "#2fb344"],
	["teal", "#0ca678"],
	["cyan", "#17a2b8"]
];
var REFERENCE_HEIGHT = 800;
var FRAME_MS = 1e3 / 60;
var MAX_DPR = 2;
var Default$21 = {
	count: 220,
	duration: 3500,
	decay: 3.5,
	fade: .15,
	speed: 1,
	colors: null
};
var DefaultType$21 = {
	count: "number",
	duration: "number",
	decay: "number",
	fade: "number",
	speed: "number",
	colors: "(array|string|null)"
};
/**
* Stage
*
* One fixed canvas shared by every burst on the page. It is created on the
* first burst and removed once the last piece has landed, so an idle page
* carries no extra element and no running animation frame.
*/
var stage = {
	canvas: null,
	context: null,
	particles: [],
	emitters: [],
	frame: 0,
	lastTick: 0,
	add(emitter) {
		this._mount();
		if (!this.context) return false;
		this.emitters.push(emitter);
		if (!this.frame) {
			this.lastTick = performance.now();
			this.frame = requestAnimationFrame((now) => this._tick(now));
		}
		return true;
	},
	_mount() {
		if (this.canvas) return;
		const canvas = document.createElement("canvas");
		canvas.className = CLASS_NAME_CANVAS;
		canvas.setAttribute("aria-hidden", "true");
		Object.assign(canvas.style, {
			position: "fixed",
			inset: "0",
			width: "100%",
			height: "100%",
			zIndex: "9999",
			pointerEvents: "none",
			display: "block"
		});
		const context = canvas.getContext("2d");
		if (!context) return;
		document.body.append(canvas);
		this.canvas = canvas;
		this.context = context;
		this._resize();
		window.addEventListener("resize", this._onResize);
	},
	_unmount() {
		var _this$canvas;
		cancelAnimationFrame(this.frame);
		this.frame = 0;
		window.removeEventListener("resize", this._onResize);
		(_this$canvas = this.canvas) === null || _this$canvas === void 0 || _this$canvas.remove();
		this.canvas = null;
		this.context = null;
		this.particles = [];
		this.emitters = [];
	},
	_onResize: () => {
		stage._resize();
	},
	_resize() {
		if (!this.canvas || !this.context) return;
		const dpr = Math.min(window.devicePixelRatio || 1, MAX_DPR);
		this.canvas.width = document.documentElement.clientWidth * dpr;
		this.canvas.height = window.innerHeight * dpr;
		this.context.setTransform(dpr, 0, 0, dpr, 0, 0);
	},
	_spawn(emitter) {
		var _colors$Math$floor;
		const { colors, config } = emitter;
		const speed = window.innerHeight / REFERENCE_HEIGHT * config.speed;
		return {
			emitter,
			x: Math.random() * window.innerWidth,
			y: -20 - Math.random() * 30,
			w: 6 + Math.random() * 3.6,
			h: 10 + Math.random() * 4.4,
			color: (_colors$Math$floor = colors[Math.floor(Math.random() * colors.length)]) !== null && _colors$Math$floor !== void 0 ? _colors$Math$floor : "#000",
			vx: (Math.random() - .5) * 1.2,
			vy: (1.5 + Math.random() * 2.1) * speed,
			speed,
			angle: Math.random() * Math.PI * 2,
			spin: (Math.random() - .5) * .25,
			flip: Math.random() * Math.PI * 2,
			flipSpeed: .12 + Math.random() * .15,
			sway: Math.random() * Math.PI * 2,
			alpha: 1
		};
	},
	_pour(emitter, now) {
		const { count, duration, decay } = emitter.config;
		const elapsed = now - emitter.startedAt;
		if (emitter.stopped || elapsed >= duration) return;
		const t = elapsed / duration;
		const target = Math.round(count * (1 - Math.exp(-decay * t)) / (1 - Math.exp(-decay)));
		while (emitter.emitted < target) {
			this.particles.push(this._spawn(emitter));
			emitter.emitted++;
		}
	},
	_tick(now) {
		const context = this.context;
		if (!context) return;
		const step = Math.min((now - this.lastTick) / FRAME_MS, 3);
		this.lastTick = now;
		const width = window.innerWidth;
		const height = window.innerHeight;
		context.clearRect(0, 0, width, height);
		for (const emitter of this.emitters) this._pour(emitter, now);
		for (let i = this.particles.length - 1; i >= 0; i--) {
			const p = this.particles[i];
			const fadeFrom = height * (1 - p.emitter.config.fade);
			p.sway += .05 * step;
			p.vy = Math.min(p.vy + .025 * p.speed * step, 4.2 * p.speed);
			p.x += (p.vx + Math.sin(p.sway) * .7) * step;
			p.y += p.vy * step;
			p.angle += p.spin * step;
			p.flip += p.flipSpeed * step;
			if (p.y > fadeFrom) p.alpha = Math.max(0, 1 - (p.y - fadeFrom) / (height - fadeFrom));
			if (p.alpha === 0) {
				this.particles.splice(i, 1);
				continue;
			}
			context.save();
			context.globalAlpha = p.alpha;
			context.translate(p.x, p.y);
			context.rotate(p.angle);
			context.scale(1, Math.cos(p.flip));
			context.fillStyle = p.color;
			context.fillRect(-p.w / 2, -p.h / 2, p.w, p.h);
			context.restore();
		}
		this._settle(now);
		if (this.emitters.length === 0) {
			this._unmount();
			return;
		}
		this.frame = requestAnimationFrame((next) => this._tick(next));
	},
	_settle(now) {
		for (let i = this.emitters.length - 1; i >= 0; i--) {
			const emitter = this.emitters[i];
			const pouring = !emitter.stopped && now - emitter.startedAt < emitter.config.duration;
			const airborne = this.particles.some((p) => p.emitter === emitter);
			if (pouring || airborne) continue;
			this.emitters.splice(i, 1);
			emitter.done = true;
			if (!emitter.detached) EventHandler.trigger(emitter.element, EVENT_END);
		}
	}
};
/**
* Class definition
*
* Pours a short shower of confetti over the page, like a bucket emptied from
* the top edge: dense at first, then thinning out. Nothing is drawn when the
* user prefers reduced motion, but the events still fire.
*/
var Confetti = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._emitter = null;
	}
	static get Default() {
		return Default$21;
	}
	static get DefaultType() {
		return DefaultType$21;
	}
	static get NAME() {
		return NAME$24;
	}
	burst() {
		const startEvent = EventHandler.trigger(this._element, EVENT_START$1);
		if (startEvent === null || startEvent === void 0 ? void 0 : startEvent.defaultPrevented) return;
		this.stop();
		if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
			EventHandler.trigger(this._element, EVENT_END);
			return;
		}
		this._emitter = {
			element: this._element,
			config: this._config,
			colors: this._colors(),
			startedAt: performance.now(),
			emitted: 0,
			stopped: false,
			done: false,
			detached: false
		};
		if (!stage.add(this._emitter)) {
			this._emitter = null;
			EventHandler.trigger(this._element, EVENT_END);
		}
	}
	stop() {
		if (this._emitter && !this._emitter.done) this._emitter.stopped = true;
		this._emitter = null;
	}
	dispose() {
		if (this._emitter) this._emitter.detached = true;
		this.stop();
		super.dispose();
	}
	_configAfterMerge(config) {
		if (typeof config.colors === "string") config.colors = config.colors.split(",").map((color) => color.trim()).filter(Boolean);
		return config;
	}
	_colors() {
		const { colors } = this._config;
		if (colors && colors.length > 0) return colors;
		const style = getComputedStyle(document.documentElement);
		return PALETTE.map(([name, fallback]) => style.getPropertyValue(`--tblr-${name}`).trim() || fallback);
	}
};
/**
* Data API implementation
*
* A trigger bursts on its `data-bs-target` when it has one, otherwise on
* itself, so the events fire where the page can listen for them.
*/
EventHandler.on(document, EVENT_CLICK_DATA_API$8, SELECTOR_DATA_TOGGLE$10, function(event) {
	if (["A", "AREA"].includes(this.tagName)) event.preventDefault();
	if (isDisabled(this)) return;
	const target = SelectorEngine.getElementFromSelector(this) || this;
	const config = Manipulator.getDataAttributes(this);
	const instance = Confetti.getOrCreateInstance(target, config);
	if (target !== this) instance._config = instance._getConfig(config);
	instance.burst();
});
//#endregion
//#region js/src/countup.ts
/**
* --------------------------------------------------------------------------
* Tabler countup.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$23 = "countup";
var EVENT_KEY$16 = `.${`bs.${NAME$23}`}`;
var DATA_ATTRIBUTE$1 = `data-${NAME$23}`;
var EVENT_START = `start${EVENT_KEY$16}`;
var EVENT_COMPLETE$1 = `complete${EVENT_KEY$16}`;
var DATA_ATTRIBUTE_ALIAS = `data-tblr-${NAME$23}`;
var SELECTOR_DATA_COUNTUP = `[${DATA_ATTRIBUTE$1}], [${DATA_ATTRIBUTE_ALIAS}]`;
var Default$20 = {
	autoAnimate: true,
	startVal: 0,
	duration: 2,
	decimalPlaces: 0,
	useEasing: true,
	useGrouping: true,
	separator: ",",
	decimal: ".",
	prefix: "",
	suffix: "",
	format: "number",
	formatter: null
};
var DefaultType$20 = {
	autoAnimate: "boolean",
	startVal: "number",
	duration: "number",
	decimalPlaces: "number",
	useEasing: "boolean",
	useGrouping: "boolean",
	separator: "string",
	decimal: "string",
	prefix: "string",
	suffix: "string",
	format: "string",
	formatter: "(function|null)"
};
/**
* Helpers
*/
var coerceOptions = (options) => {
	const result = _objectSpread2({}, options);
	for (const [key, value] of Object.entries(options)) {
		const expected = DefaultType$20[key];
		if (typeof value === "string" && expected === "number" && value.trim() !== "" && !Number.isNaN(Number(value))) result[key] = Number(value);
		else if (typeof value === "string" && expected === "boolean" && (value === "true" || value === "false")) result[key] = value === "true";
		else if (typeof value === "number" && expected === "string") result[key] = String(value);
	}
	return result;
};
var parseValue = (input, format) => {
	if (typeof input === "number") return input;
	if (format === "time") {
		const match = /^\D{0,32}(\d{1,4}):(\d{1,2})/.exec(input);
		return match ? Number(match[1]) * 60 + Number(match[2]) : NaN;
	}
	return Number.parseFloat(input.replace(/[^0-9.-]/g, ""));
};
var easeOut$1 = (t) => t >= 1 ? 1 : (1 - Math.pow(2, -10 * t)) * 1024 / 1023;
var reducedMotion = () => window.matchMedia("(prefers-reduced-motion: reduce)").matches;
/**
* Class definition
*
* Animates the number written inside the element from `startVal` to it,
* formatted with grouping, decimals, a prefix and a suffix. Options come from
* the `data-countup` attribute as JSON, or from the config object.
*/
var CountUp = class extends BaseComponent {
	constructor(element, config) {
		var _this$_element$textCo;
		super(element, config);
		this._endVal = 0;
		this._value = 0;
		this._frame = 0;
		this._observer = null;
		this._from = 0;
		this._elapsed = 0;
		this._running = false;
		this._paused = false;
		this._started = false;
		if (!this._element) return;
		const value = parseValue((_this$_element$textCo = this._element.textContent) !== null && _this$_element$textCo !== void 0 ? _this$_element$textCo : "", this._config.format);
		if (Number.isNaN(value)) return;
		this._endVal = value;
		this._value = this._config.startVal;
		this._print(this._value);
		if (!this._config.autoAnimate) {
			this.start();
			return;
		}
		if (typeof IntersectionObserver === "undefined") {
			this.start();
			return;
		}
		this._observer = new IntersectionObserver((entries) => this._observerCallback(entries));
		this._observer.observe(this._element);
	}
	static get Default() {
		return Default$20;
	}
	static get DefaultType() {
		return DefaultType$20;
	}
	static get NAME() {
		return NAME$23;
	}
	start() {
		this._animate(this._value, this._endVal);
	}
	reset() {
		this._stop();
		this._started = false;
		this._value = this._config.startVal;
		this._print(this._value);
	}
	update(value) {
		const parsed = parseValue(value, this._config.format);
		if (Number.isNaN(parsed)) return;
		this._endVal = parsed;
		this._animate(this._value, this._endVal);
	}
	pauseResume() {
		if (this._paused) {
			this._paused = false;
			this._run();
		} else if (this._running) {
			this._paused = true;
			cancelAnimationFrame(this._frame);
		}
	}
	dispose() {
		var _this$_observer;
		this._stop();
		(_this$_observer = this._observer) === null || _this$_observer === void 0 || _this$_observer.disconnect();
		super.dispose();
	}
	_mergeConfigObj(config, element) {
		var _element$getAttribute;
		let dataOptions = {};
		const raw = (_element$getAttribute = element === null || element === void 0 ? void 0 : element.getAttribute(DATA_ATTRIBUTE$1)) !== null && _element$getAttribute !== void 0 ? _element$getAttribute : element === null || element === void 0 ? void 0 : element.getAttribute(DATA_ATTRIBUTE_ALIAS);
		if (raw) try {
			const parsed = JSON.parse(raw);
			if (parsed && typeof parsed === "object" && !Array.isArray(parsed)) dataOptions = parsed;
		} catch (_unused) {}
		return super._mergeConfigObj(_objectSpread2(_objectSpread2({}, coerceOptions(dataOptions)), config), element);
	}
	_observerCallback(entries) {
		if (entries.some((entry) => entry.isIntersecting)) {
			var _this$_observer2;
			(_this$_observer2 = this._observer) === null || _this$_observer2 === void 0 || _this$_observer2.disconnect();
			this._observer = null;
			if (!this._started) this.start();
		}
	}
	_animate(from, to) {
		this._stop();
		this._started = true;
		if (from === to || this._config.duration <= 0 || reducedMotion()) {
			this._finish(to);
			return;
		}
		this._from = from;
		this._elapsed = 0;
		this._running = true;
		EventHandler.trigger(this._element, EVENT_START);
		this._run();
	}
	_run() {
		const duration = this._config.duration * 1e3;
		let last = performance.now();
		const step = (now) => {
			this._elapsed += now - last;
			last = now;
			const t = Math.min(1, this._elapsed / duration);
			const progress = this._config.useEasing ? easeOut$1(t) : t;
			this._value = this._from + (this._endVal - this._from) * progress;
			this._print(this._value);
			if (t < 1) this._frame = requestAnimationFrame(step);
			else this._finish(this._endVal);
		};
		this._frame = requestAnimationFrame(step);
	}
	_finish(value) {
		this._running = false;
		this._paused = false;
		this._value = value;
		this._print(value);
		EventHandler.trigger(this._element, EVENT_COMPLETE$1);
	}
	_stop() {
		cancelAnimationFrame(this._frame);
		this._running = false;
		this._paused = false;
	}
	_print(value) {
		this._element.textContent = this._format(value);
	}
	_format(value) {
		const { decimalPlaces, useGrouping, separator, decimal, prefix, suffix, format, formatter } = this._config;
		if (formatter) return formatter(value);
		if (format === "time") {
			const rounded = Math.sign(value) * Math.round(Math.abs(value));
			const minutes = Math.abs(rounded);
			return `${rounded < 0 ? "-" : ""}${prefix}${Math.floor(minutes / 60)}:${String(minutes % 60).padStart(2, "0")}${suffix}`;
		}
		const fixed = value.toFixed(decimalPlaces);
		const [integer, fraction] = fixed.replace("-", "").split(".");
		const grouped = useGrouping ? integer.replace(/\B(?=(\d{3})+(?!\d))/g, separator) : integer;
		return `${Number(fixed) < 0 ? "-" : ""}${prefix}${grouped}${fraction ? decimal + fraction : ""}${suffix}`;
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_COUNTUP, CountUp);
//#endregion
//#region js/src/datepicker.ts
/**
* Constants
*/
var NAME$22 = "datepicker";
var EVENT_KEY$15 = `.${`bs.${NAME$22}`}`;
var DATA_API_KEY$7 = ".data-api";
var EVENT_CHANGE$2 = `change${EVENT_KEY$15}`;
var EVENT_SHOW$7 = `show${EVENT_KEY$15}`;
var EVENT_SHOWN$7 = `shown${EVENT_KEY$15}`;
var EVENT_HIDE$7 = `hide${EVENT_KEY$15}`;
var EVENT_HIDDEN$7 = `hidden${EVENT_KEY$15}`;
var EVENT_FOCUSIN$3 = `focusin${EVENT_KEY$15}`;
var EVENT_CLICK_DATA_API$7 = `click${EVENT_KEY$15}${DATA_API_KEY$7}`;
var EVENT_FOCUSIN_DATA_API = `focusin${EVENT_KEY$15}${DATA_API_KEY$7}`;
var SELECTOR_DATA_TOGGLE$9 = "[data-bs-toggle=\"datepicker\"], [data-tblr-toggle=\"datepicker\"]";
var SELECTOR_DISPLAY = "[data-bs-datepicker-display], [data-tblr-datepicker-display]";
var SELECTOR_INPUT_WRAPPER = ".input-icon, .input-group";
var SELECTOR_BOUND_INPUT = "input[type=\"hidden\"], input[name]";
var HIDE_DELAY = 100;
var DATE_PATTERN = /^(\d{4})-(\d{2})-(\d{2})$/;
var EXTENSION_NAMES = [
	"annotations",
	"months",
	"motion",
	"time",
	"weeks"
];
var writtenSelections = /* @__PURE__ */ new WeakMap();
var Default$19 = {
	datepickerTheme: null,
	dateMin: null,
	dateMax: null,
	dateFormat: null,
	displayElement: null,
	displayMonthsCount: 1,
	firstWeekday: 1,
	inline: false,
	locale: "default",
	positionElement: null,
	selectedDates: [],
	selectionMode: "single",
	placement: "left",
	vcpOptions: {}
};
var DefaultType$19 = {
	datepickerTheme: "(null|string)",
	dateMin: "(null|string|number|object|date)",
	dateMax: "(null|string|number|object|date)",
	dateFormat: "(null|object|function)",
	displayElement: "(null|string|element|boolean)",
	displayMonthsCount: "number",
	firstWeekday: "number",
	inline: "boolean",
	locale: "string",
	positionElement: "(null|string|element)",
	selectedDates: "array",
	selectionMode: "string",
	placement: "string",
	vcpOptions: "object"
};
var afterPluginTimers = (callback) => {
	setTimeout(() => setTimeout(callback));
};
/**
* Class definition
*
* Wraps Vanilla Calendar Pro (https://vanilla-calendar.pro), loaded separately
* as `window.VanillaCalendarPro`. Without the plugin the component is inert.
* Same options, methods and events as Bootstrap 6's Datepicker, so markup
* written for one works with the other.
*/
var Datepicker = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._calendar = null;
		this._isShown = false;
		this._isShowing = false;
		this._isHiding = false;
		this._isSilent = false;
		this._skipPluginShow = false;
		this._resolveShown = null;
		this._hideTimeout = 0;
		this._isInput = false;
		this._isInline = false;
		this._boundInput = null;
		this._positionElement = null;
		this._displayElement = null;
		this._themeObserver = null;
		this._onFocusIn = null;
		this._onResize = () => this._alignToPositionElement();
		this._selectedDates = [];
		if (!this._element || !window.VanillaCalendarPro) return;
		this._initCalendar();
	}
	static get Default() {
		return Default$19;
	}
	static get DefaultType() {
		return DefaultType$19;
	}
	static get NAME() {
		return NAME$22;
	}
	/** The Vanilla Calendar Pro instance, for options the component does not expose. */
	get calendar() {
		return this._calendar;
	}
	toggle() {
		if (this._config.inline) return Promise.resolve();
		return this._isShown ? this.hide() : this.show();
	}
	show() {
		var _this = this;
		return _asyncToGenerator(function* () {
			if (_this._config.inline) return;
			if (!_this._calendar || isDisabled(_this._element) || _this._isShown || _this._isShowing) return;
			const showEvent = EventHandler.trigger(_this._element, EVENT_SHOW$7);
			if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
			_this._isShowing = true;
			_this._skipPluginShow = false;
			_this._calendar.show();
			if (!_this._isShown) yield new Promise((resolve) => {
				_this._resolveShown = resolve;
				afterPluginTimers(resolve);
			});
			_this._resolveShown = null;
			_this._isShowing = false;
		})();
	}
	hide() {
		var _this2 = this;
		return _asyncToGenerator(function* () {
			if (_this2._config.inline) return;
			if (!_this2._calendar || !_this2._isShown) return;
			const hideEvent = EventHandler.trigger(_this2._element, EVENT_HIDE$7);
			if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
			_this2._skipPluginShow = true;
			afterPluginTimers(() => {
				_this2._skipPluginShow = false;
			});
			_this2._isHiding = true;
			_this2._calendar.hide();
			_this2._isHiding = false;
		})();
	}
	dispose() {
		var _this$_themeObserver;
		window.clearTimeout(this._hideTimeout);
		(_this$_themeObserver = this._themeObserver) === null || _this$_themeObserver === void 0 || _this$_themeObserver.disconnect();
		window.removeEventListener("resize", this._onResize);
		if (this._onFocusIn) EventHandler.off(document, EVENT_FOCUSIN$3, this._onFocusIn);
		if (this._calendar) {
			this._calendar.destroy();
			const clone = this._calendar.context.mainElement;
			if (clone && clone !== this._element && clone.isConnected) {
				if (this._isInput) clone.replaceWith(this._element);
				else if (this._boundInput) {
					const copy = SelectorEngine.findOne(SELECTOR_BOUND_INPUT, clone);
					if (copy) copy.replaceWith(this._boundInput);
					else clone.append(this._boundInput);
				}
			}
		}
		super.dispose();
	}
	getSelectedDates() {
		var _this$_calendar;
		const dates = (_this$_calendar = this._calendar) === null || _this$_calendar === void 0 || (_this$_calendar = _this$_calendar.context) === null || _this$_calendar === void 0 ? void 0 : _this$_calendar.selectedDates;
		return dates && dates.length > 0 ? [...dates] : [...this._selectedDates];
	}
	setSelectedDates(dates) {
		var _this$_calendar2;
		this._selectedDates = dates.map((date) => this._toIsoDate(date));
		(_this$_calendar2 = this._calendar) === null || _this$_calendar2 === void 0 || _this$_calendar2.set(_objectSpread2({ selectedDates: this._selectedDates }, this._monthOf(this._selectedDates[0])));
		this._writeSelection(this._selectedDates);
	}
	_initCalendar() {
		this._isInput = this._element.tagName === "INPUT";
		this._isInline = this._config.inline;
		if (this._isInline && !this._isInput) this._boundInput = SelectorEngine.findOne(SELECTOR_BOUND_INPUT, this._element);
		this._positionElement = this._resolvePositionElement();
		this._displayElement = this._resolveDisplayElement();
		this._selectedDates = [...this._config.selectedDates];
		this._calendar = new window.VanillaCalendarPro.Calendar(this._element, this._buildCalendarOptions());
		this._calendar.init();
		if (this._boundInput && !this._boundInput.isConnected) this._element.after(this._boundInput);
		this._setupThemeObserver();
		this._setupDismissOnFocus();
		if (this._isInput && this._element.value) this._parseInputValue();
		this._updateDisplayWithSelectedDates();
	}
	_updateDisplayWithSelectedDates() {
		if (this._selectedDates.length === 0) return;
		this._writeSelection(this._selectedDates);
	}
	_writeSelection(selectedDates) {
		const formattedDate = this._formatDateForInput(selectedDates);
		if (this._isInput) {
			this._element.value = formattedDate;
			writtenSelections.set(this._element, {
				text: formattedDate,
				dates: [...selectedDates]
			});
		}
		if (this._boundInput) this._boundInput.value = selectedDates.join(",");
		if (this._displayElement) this._displayElement.textContent = formattedDate;
	}
	_resolvePositionElement() {
		let { positionElement } = this._config;
		if (typeof positionElement === "string") positionElement = getElement(positionElement);
		if (!positionElement && this._isInput && !this._isInline) positionElement = this._element.closest(SELECTOR_INPUT_WRAPPER);
		return positionElement || this._element;
	}
	_resolveDisplayElement() {
		const { displayElement } = this._config;
		if (typeof displayElement === "string") return getElement(displayElement);
		if (displayElement === true || displayElement === null && !this._isInput && !this._isInline) return SelectorEngine.findOne(SELECTOR_DISPLAY, this._element) || this._element;
		return displayElement;
	}
	_alignToPositionElement() {
		var _this$_calendar3;
		const mainElement = (_this$_calendar3 = this._calendar) === null || _this$_calendar3 === void 0 || (_this$_calendar3 = _this$_calendar3.context) === null || _this$_calendar3 === void 0 ? void 0 : _this$_calendar3.mainElement;
		if (!mainElement || this._isInline || !this._positionElement || this._positionElement === this._element) return;
		const anchor = this._positionElement.getBoundingClientRect();
		const popup = mainElement.getBoundingClientRect();
		let { placement } = this._config;
		if (placement === "auto") placement = anchor.left + popup.width > document.documentElement.clientWidth ? "right" : "left";
		const left = placement === "right" ? anchor.right - popup.width : placement === "center" ? anchor.left + (anchor.width - popup.width) / 2 : anchor.left;
		mainElement.style.left = `${left + window.scrollX}px`;
	}
	_getThemeAncestor() {
		return this._element.closest("[data-bs-theme]");
	}
	_getEffectiveTheme() {
		var _this$_getThemeAncest;
		const { datepickerTheme } = this._config;
		if (datepickerTheme) return datepickerTheme;
		return ((_this$_getThemeAncest = this._getThemeAncestor()) === null || _this$_getThemeAncest === void 0 ? void 0 : _this$_getThemeAncest.getAttribute("data-bs-theme")) || null;
	}
	_syncThemeAttribute(element) {
		if (!element) return;
		const theme = this._getEffectiveTheme();
		if (theme) element.setAttribute("data-bs-theme", theme);
		else element.removeAttribute("data-bs-theme");
	}
	_setupThemeObserver() {
		const ancestor = this._getThemeAncestor();
		if (!ancestor || this._config.datepickerTheme) return;
		this._themeObserver = new MutationObserver(() => {
			var _this$_calendar4;
			this._syncThemeAttribute((_this$_calendar4 = this._calendar) === null || _this$_calendar4 === void 0 || (_this$_calendar4 = _this$_calendar4.context) === null || _this$_calendar4 === void 0 ? void 0 : _this$_calendar4.mainElement);
		});
		this._themeObserver.observe(ancestor, {
			attributes: true,
			attributeFilter: ["data-bs-theme"]
		});
	}
	_setupDismissOnFocus() {
		if (this._isInline) return;
		this._onFocusIn = (event) => {
			var _this$_calendar5;
			if (!this._isShown) return;
			const { target } = event;
			const mainElement = (_this$_calendar5 = this._calendar) === null || _this$_calendar5 === void 0 || (_this$_calendar5 = _this$_calendar5.context) === null || _this$_calendar5 === void 0 ? void 0 : _this$_calendar5.mainElement;
			if (target instanceof Node && (this._element.contains(target) || (mainElement === null || mainElement === void 0 ? void 0 : mainElement.contains(target)))) return;
			this.hide();
		};
		EventHandler.on(document, EVENT_FOCUSIN$3, this._onFocusIn);
	}
	_silently(callback) {
		this._isSilent = true;
		callback();
		this._isSilent = false;
	}
	_handlePluginShow() {
		var _this$_resolveShown;
		const calendar = this._calendar;
		if (!calendar) return;
		if (!this._isSilent && !this._isShown) {
			var _EventHandler$trigger;
			if (this._skipPluginShow || !this._isShowing && ((_EventHandler$trigger = EventHandler.trigger(this._element, EVENT_SHOW$7)) === null || _EventHandler$trigger === void 0 ? void 0 : _EventHandler$trigger.defaultPrevented)) {
				this._silently(() => calendar.hide());
				return;
			}
		}
		const wasShown = this._isShown;
		this._isShown = true;
		this._syncThemeAttribute(calendar.context.mainElement);
		this._alignToPositionElement();
		window.removeEventListener("resize", this._onResize);
		window.addEventListener("resize", this._onResize);
		if (!wasShown) EventHandler.trigger(this._element, EVENT_SHOWN$7);
		(_this$_resolveShown = this._resolveShown) === null || _this$_resolveShown === void 0 || _this$_resolveShown.call(this);
	}
	_handlePluginHide() {
		var _EventHandler$trigger2;
		const calendar = this._calendar;
		if (!calendar || this._isSilent || !this._isShown) return;
		if (!this._isHiding && ((_EventHandler$trigger2 = EventHandler.trigger(this._element, EVENT_HIDE$7)) === null || _EventHandler$trigger2 === void 0 ? void 0 : _EventHandler$trigger2.defaultPrevented)) {
			this._silently(() => calendar.show());
			return;
		}
		this._isShown = false;
		window.removeEventListener("resize", this._onResize);
		EventHandler.trigger(this._element, EVENT_HIDDEN$7);
	}
	/** @internal */
	_calendarExtensions(own = []) {
		const library = window.VanillaCalendarPro;
		const extensions = [...own];
		for (const name of EXTENSION_NAMES) {
			const extension = library === null || library === void 0 ? void 0 : library[name];
			if (extension && typeof extension === "object" && !extensions.includes(extension)) extensions.push(extension);
		}
		return extensions;
	}
	/** @internal */
	_buildCalendarOptions() {
		const theme = this._getEffectiveTheme();
		const vcpTheme = !theme || theme === "auto" ? "system" : theme;
		const vcpOptions = this._config.vcpOptions;
		const extensions = this._calendarExtensions(vcpOptions.extensions);
		const calendarOptions = _objectSpread2(_objectSpread2(_objectSpread2({}, vcpOptions), extensions.length > 0 ? { extensions } : {}), {}, {
			inputMode: !this._isInline,
			positionToInput: this._config.placement,
			firstWeekday: this._config.firstWeekday,
			locale: this._config.locale,
			selectionDatesMode: this._config.selectionMode,
			selectedDates: this._config.selectedDates,
			displayMonthsCount: this._config.displayMonthsCount,
			type: this._config.displayMonthsCount > 1 ? "multiple" : "default",
			selectedTheme: vcpTheme,
			themeAttrDetect: "[data-bs-theme]",
			onClickDate: (self, event) => this._handleDateClick(self, event),
			onInit: (self) => {
				this._syncThemeAttribute(self.context.mainElement);
			},
			onShow: () => this._handlePluginShow(),
			onHide: () => this._handlePluginHide()
		});
		const [firstSelected] = this._config.selectedDates;
		if (firstSelected) {
			const firstDate = this._parseDate(firstSelected);
			calendarOptions.selectedMonth = firstDate.getMonth();
			calendarOptions.selectedYear = firstDate.getFullYear();
		}
		if (this._config.dateMin) calendarOptions.dateMin = this._config.dateMin;
		if (this._config.dateMax) calendarOptions.dateMax = this._config.dateMax;
		return calendarOptions;
	}
	/** @internal */
	_handleDateClick(self, event) {
		const selectedDates = [...self.context.selectedDates];
		this._selectedDates = selectedDates;
		this._writeSelection(selectedDates);
		const args = {
			dates: selectedDates,
			event
		};
		EventHandler.trigger(this._element, EVENT_CHANGE$2, args);
		this._maybeHideAfterSelection(selectedDates);
	}
	_maybeHideAfterSelection(selectedDates) {
		if (this._isInline) return;
		if (this._config.selectionMode === "single" && selectedDates.length > 0 || this._config.selectionMode === "multiple-ranged" && selectedDates.length >= 2) {
			window.clearTimeout(this._hideTimeout);
			this._hideTimeout = window.setTimeout(() => this.hide(), HIDE_DELAY);
		}
	}
	_parseDate(dateStr) {
		const [year = 0, month = 1, day = 1] = dateStr.split("-").map(Number);
		return new Date(year, month - 1, day);
	}
	_formatDate(dateStr) {
		const date = this._parseDate(dateStr);
		const locale = this._config.locale === "default" ? void 0 : this._config.locale;
		const { dateFormat } = this._config;
		if (typeof dateFormat === "function") return dateFormat(date, locale);
		if (dateFormat && typeof dateFormat === "object") return new Intl.DateTimeFormat(locale, dateFormat).format(date);
		return date.toLocaleDateString(locale);
	}
	_formatDateForInput(dates) {
		if (dates.length === 0) return "";
		if (dates.length === 1) return this._formatDate(dates[0]);
		const separator = this._config.selectionMode === "multiple-ranged" ? " – " : ", ";
		return dates.map((date) => this._formatDate(date)).join(separator);
	}
	_parseInputValue() {
		var _this$_calendar7;
		const value = this._element.value.trim();
		if (!value) return;
		const written = writtenSelections.get(this._element);
		if (written && written.text.trim() === value) {
			var _this$_calendar6;
			this._selectedDates = [...written.dates];
			(_this$_calendar6 = this._calendar) === null || _this$_calendar6 === void 0 || _this$_calendar6.set(_objectSpread2({ selectedDates: this._selectedDates }, this._monthOf(this._selectedDates[0])));
			return;
		}
		const date = DATE_PATTERN.test(value) ? this._parseDate(value) : new Date(value);
		if (Number.isNaN(date.getTime())) return;
		this._selectedDates = [this._toIsoDate(date)];
		(_this$_calendar7 = this._calendar) === null || _this$_calendar7 === void 0 || _this$_calendar7.set(_objectSpread2({ selectedDates: this._selectedDates }, this._monthOf(this._selectedDates[0])));
	}
	_toIsoDate(value) {
		if (typeof value === "string" && DATE_PATTERN.test(value)) return value;
		const date = value instanceof Date ? value : typeof value === "string" && DATE_PATTERN.test(value) ? this._parseDate(value) : new Date(value);
		if (Number.isNaN(date.getTime())) return String(value);
		return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
	}
	/** @internal */
	_monthOf(value) {
		if (!value || !DATE_PATTERN.test(value)) return {};
		const date = this._parseDate(value);
		return {
			selectedMonth: date.getMonth(),
			selectedYear: date.getFullYear()
		};
	}
};
/**
* Data API implementation
*/
EventHandler.on(document, EVENT_CLICK_DATA_API$7, SELECTOR_DATA_TOGGLE$9, function(event) {
	if (this.tagName === "INPUT" || this.dataset.bsInline === "true" || this.dataset.tblrInline === "true") return;
	event.preventDefault();
	Datepicker.getOrCreateInstance(this).toggle();
});
EventHandler.on(document, EVENT_FOCUSIN_DATA_API, SELECTOR_DATA_TOGGLE$9, function() {
	if (this.tagName !== "INPUT") return;
	Datepicker.getOrCreateInstance(this).show();
});
initAll(SELECTOR_DATA_TOGGLE$9, Datepicker, (element) => {
	const { dataset } = element;
	return dataset.bsInline === "true" || dataset.tblrInline === "true" || "bsSelectedDates" in dataset || "tblrSelectedDates" in dataset || Boolean(element.value);
});
//#endregion
//#region js/src/input-mask.ts
/**
* --------------------------------------------------------------------------
* Tabler input-mask.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$21 = "input-mask";
var ATTRIBUTE_MASK = "data-mask";
var ATTRIBUTE_VISIBLE = "data-mask-visible";
var SELECTOR_DATA_MASK = `[${ATTRIBUTE_MASK}]`;
var Default$18 = {
	mask: "",
	lazy: true
};
var DefaultType$18 = {
	mask: "(string|function|regexp|object|array)",
	lazy: "boolean"
};
/**
* Class definition
*
* Wraps the IMask plugin (https://imask.js.org), loaded separately as
* `window.IMask`. Without the plugin the component is inert.
*/
var InputMask = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._mask = null;
		if (!this._element || !this._config.mask || !window.IMask) return;
		this._mask = new window.IMask(this._element, _objectSpread2({}, this._config));
	}
	static get Default() {
		return Default$18;
	}
	static get DefaultType() {
		return DefaultType$18;
	}
	static get NAME() {
		return NAME$21;
	}
	/** The IMask instance, for options the component does not expose. */
	get mask() {
		return this._mask;
	}
	update() {
		var _this$_mask;
		(_this$_mask = this._mask) === null || _this$_mask === void 0 || _this$_mask.updateValue();
	}
	dispose() {
		var _this$_mask2;
		(_this$_mask2 = this._mask) === null || _this$_mask2 === void 0 || _this$_mask2.destroy();
		super.dispose();
	}
	_mergeConfigObj(config, element) {
		const dataOptions = {};
		const mask = element === null || element === void 0 ? void 0 : element.getAttribute(ATTRIBUTE_MASK);
		if (mask) dataOptions.mask = mask;
		if (element === null || element === void 0 ? void 0 : element.hasAttribute(ATTRIBUTE_VISIBLE)) dataOptions.lazy = element.getAttribute(ATTRIBUTE_VISIBLE) === "false";
		return super._mergeConfigObj(_objectSpread2(_objectSpread2({}, dataOptions), config), element);
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_MASK, InputMask);
var bottom = "bottom";
var right = "right";
var left = "left";
var auto = "auto";
var basePlacements = [
	"top",
	bottom,
	right,
	left
];
var start = "start";
var clippingParents = "clippingParents";
var viewport = "viewport";
var popper = "popper";
var reference = "reference";
var variationPlacements = /*#__PURE__*/ basePlacements.reduce(function(acc, placement) {
	return acc.concat([placement + "-" + start, placement + "-end"]);
}, []);
var placements = /*#__PURE__*/ [].concat(basePlacements, [auto]).reduce(function(acc, placement) {
	return acc.concat([
		placement,
		placement + "-" + start,
		placement + "-end"
	]);
}, []);
var beforeRead = "beforeRead";
var read = "read";
var afterRead = "afterRead";
var beforeMain = "beforeMain";
var main = "main";
var afterMain = "afterMain";
var beforeWrite = "beforeWrite";
var write = "write";
var afterWrite = "afterWrite";
var modifierPhases = [
	beforeRead,
	read,
	afterRead,
	beforeMain,
	main,
	afterMain,
	beforeWrite,
	write,
	afterWrite
];
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getNodeName.js
function getNodeName(element) {
	return element ? (element.nodeName || "").toLowerCase() : null;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getWindow.js
function getWindow(node) {
	if (node == null) return window;
	if (node.toString() !== "[object Window]") {
		var ownerDocument = node.ownerDocument;
		return ownerDocument ? ownerDocument.defaultView || window : window;
	}
	return node;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/instanceOf.js
function isElement(node) {
	return node instanceof getWindow(node).Element || node instanceof Element;
}
function isHTMLElement(node) {
	return node instanceof getWindow(node).HTMLElement || node instanceof HTMLElement;
}
function isShadowRoot(node) {
	if (typeof ShadowRoot === "undefined") return false;
	return node instanceof getWindow(node).ShadowRoot || node instanceof ShadowRoot;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/applyStyles.js
function applyStyles(_ref) {
	var state = _ref.state;
	Object.keys(state.elements).forEach(function(name) {
		var style = state.styles[name] || {};
		var attributes = state.attributes[name] || {};
		var element = state.elements[name];
		if (!isHTMLElement(element) || !getNodeName(element)) return;
		Object.assign(element.style, style);
		Object.keys(attributes).forEach(function(name) {
			var value = attributes[name];
			if (value === false) element.removeAttribute(name);
			else element.setAttribute(name, value === true ? "" : value);
		});
	});
}
function effect$2(_ref2) {
	var state = _ref2.state;
	var initialStyles = {
		popper: {
			position: state.options.strategy,
			left: "0",
			top: "0",
			margin: "0"
		},
		arrow: { position: "absolute" },
		reference: {}
	};
	Object.assign(state.elements.popper.style, initialStyles.popper);
	state.styles = initialStyles;
	if (state.elements.arrow) Object.assign(state.elements.arrow.style, initialStyles.arrow);
	return function() {
		Object.keys(state.elements).forEach(function(name) {
			var element = state.elements[name];
			var attributes = state.attributes[name] || {};
			var style = Object.keys(state.styles.hasOwnProperty(name) ? state.styles[name] : initialStyles[name]).reduce(function(style, property) {
				style[property] = "";
				return style;
			}, {});
			if (!isHTMLElement(element) || !getNodeName(element)) return;
			Object.assign(element.style, style);
			Object.keys(attributes).forEach(function(attribute) {
				element.removeAttribute(attribute);
			});
		});
	};
}
var applyStyles_default = {
	name: "applyStyles",
	enabled: true,
	phase: "write",
	fn: applyStyles,
	effect: effect$2,
	requires: ["computeStyles"]
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getBasePlacement.js
function getBasePlacement(placement) {
	return placement.split("-")[0];
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/math.js
var max = Math.max;
var min = Math.min;
var round = Math.round;
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/userAgent.js
function getUAString() {
	var uaData = navigator.userAgentData;
	if (uaData != null && uaData.brands && Array.isArray(uaData.brands)) return uaData.brands.map(function(item) {
		return item.brand + "/" + item.version;
	}).join(" ");
	return navigator.userAgent;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/isLayoutViewport.js
function isLayoutViewport() {
	return !/^((?!chrome|android).)*safari/i.test(getUAString());
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getBoundingClientRect.js
function getBoundingClientRect(element, includeScale, isFixedStrategy) {
	if (includeScale === void 0) includeScale = false;
	if (isFixedStrategy === void 0) isFixedStrategy = false;
	var clientRect = element.getBoundingClientRect();
	var scaleX = 1;
	var scaleY = 1;
	if (includeScale && isHTMLElement(element)) {
		scaleX = element.offsetWidth > 0 ? round(clientRect.width) / element.offsetWidth || 1 : 1;
		scaleY = element.offsetHeight > 0 ? round(clientRect.height) / element.offsetHeight || 1 : 1;
	}
	var visualViewport = (isElement(element) ? getWindow(element) : window).visualViewport;
	var addVisualOffsets = !isLayoutViewport() && isFixedStrategy;
	var x = (clientRect.left + (addVisualOffsets && visualViewport ? visualViewport.offsetLeft : 0)) / scaleX;
	var y = (clientRect.top + (addVisualOffsets && visualViewport ? visualViewport.offsetTop : 0)) / scaleY;
	var width = clientRect.width / scaleX;
	var height = clientRect.height / scaleY;
	return {
		width,
		height,
		top: y,
		right: x + width,
		bottom: y + height,
		left: x,
		x,
		y
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getLayoutRect.js
function getLayoutRect(element) {
	var clientRect = getBoundingClientRect(element);
	var width = element.offsetWidth;
	var height = element.offsetHeight;
	if (Math.abs(clientRect.width - width) <= 1) width = clientRect.width;
	if (Math.abs(clientRect.height - height) <= 1) height = clientRect.height;
	return {
		x: element.offsetLeft,
		y: element.offsetTop,
		width,
		height
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/contains.js
function contains(parent, child) {
	var rootNode = child.getRootNode && child.getRootNode();
	if (parent.contains(child)) return true;
	else if (rootNode && isShadowRoot(rootNode)) {
		var next = child;
		do {
			if (next && parent.isSameNode(next)) return true;
			next = next.parentNode || next.host;
		} while (next);
	}
	return false;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getComputedStyle.js
function getComputedStyle$1(element) {
	return getWindow(element).getComputedStyle(element);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/isTableElement.js
function isTableElement(element) {
	return [
		"table",
		"td",
		"th"
	].indexOf(getNodeName(element)) >= 0;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getDocumentElement.js
function getDocumentElement(element) {
	return ((isElement(element) ? element.ownerDocument : element.document) || window.document).documentElement;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getParentNode.js
function getParentNode(element) {
	if (getNodeName(element) === "html") return element;
	return element.assignedSlot || element.parentNode || (isShadowRoot(element) ? element.host : null) || getDocumentElement(element);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getOffsetParent.js
function getTrueOffsetParent(element) {
	if (!isHTMLElement(element) || getComputedStyle$1(element).position === "fixed") return null;
	return element.offsetParent;
}
function getContainingBlock(element) {
	var isFirefox = /firefox/i.test(getUAString());
	if (/Trident/i.test(getUAString()) && isHTMLElement(element)) {
		if (getComputedStyle$1(element).position === "fixed") return null;
	}
	var currentNode = getParentNode(element);
	if (isShadowRoot(currentNode)) currentNode = currentNode.host;
	while (isHTMLElement(currentNode) && ["html", "body"].indexOf(getNodeName(currentNode)) < 0) {
		var css = getComputedStyle$1(currentNode);
		if (css.transform !== "none" || css.perspective !== "none" || css.contain === "paint" || ["transform", "perspective"].indexOf(css.willChange) !== -1 || isFirefox && css.willChange === "filter" || isFirefox && css.filter && css.filter !== "none") return currentNode;
		else currentNode = currentNode.parentNode;
	}
	return null;
}
function getOffsetParent(element) {
	var window = getWindow(element);
	var offsetParent = getTrueOffsetParent(element);
	while (offsetParent && isTableElement(offsetParent) && getComputedStyle$1(offsetParent).position === "static") offsetParent = getTrueOffsetParent(offsetParent);
	if (offsetParent && (getNodeName(offsetParent) === "html" || getNodeName(offsetParent) === "body" && getComputedStyle$1(offsetParent).position === "static")) return window;
	return offsetParent || getContainingBlock(element) || window;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getMainAxisFromPlacement.js
function getMainAxisFromPlacement(placement) {
	return ["top", "bottom"].indexOf(placement) >= 0 ? "x" : "y";
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/within.js
function within(min$2, value, max$2) {
	return max(min$2, min(value, max$2));
}
function withinMaxClamp(min, value, max) {
	var v = within(min, value, max);
	return v > max ? max : v;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getFreshSideObject.js
function getFreshSideObject() {
	return {
		top: 0,
		right: 0,
		bottom: 0,
		left: 0
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/mergePaddingObject.js
function mergePaddingObject(paddingObject) {
	return Object.assign({}, getFreshSideObject(), paddingObject);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/expandToHashMap.js
function expandToHashMap(value, keys) {
	return keys.reduce(function(hashMap, key) {
		hashMap[key] = value;
		return hashMap;
	}, {});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/arrow.js
var toPaddingObject = function toPaddingObject(padding, state) {
	padding = typeof padding === "function" ? padding(Object.assign({}, state.rects, { placement: state.placement })) : padding;
	return mergePaddingObject(typeof padding !== "number" ? padding : expandToHashMap(padding, basePlacements));
};
function arrow(_ref) {
	var _state$modifiersData$;
	var state = _ref.state, name = _ref.name, options = _ref.options;
	var arrowElement = state.elements.arrow;
	var popperOffsets = state.modifiersData.popperOffsets;
	var basePlacement = getBasePlacement(state.placement);
	var axis = getMainAxisFromPlacement(basePlacement);
	var len = ["left", "right"].indexOf(basePlacement) >= 0 ? "height" : "width";
	if (!arrowElement || !popperOffsets) return;
	var paddingObject = toPaddingObject(options.padding, state);
	var arrowRect = getLayoutRect(arrowElement);
	var minProp = axis === "y" ? "top" : left;
	var maxProp = axis === "y" ? bottom : right;
	var endDiff = state.rects.reference[len] + state.rects.reference[axis] - popperOffsets[axis] - state.rects.popper[len];
	var startDiff = popperOffsets[axis] - state.rects.reference[axis];
	var arrowOffsetParent = getOffsetParent(arrowElement);
	var clientSize = arrowOffsetParent ? axis === "y" ? arrowOffsetParent.clientHeight || 0 : arrowOffsetParent.clientWidth || 0 : 0;
	var centerToReference = endDiff / 2 - startDiff / 2;
	var min = paddingObject[minProp];
	var max = clientSize - arrowRect[len] - paddingObject[maxProp];
	var center = clientSize / 2 - arrowRect[len] / 2 + centerToReference;
	var offset = within(min, center, max);
	var axisProp = axis;
	state.modifiersData[name] = (_state$modifiersData$ = {}, _state$modifiersData$[axisProp] = offset, _state$modifiersData$.centerOffset = offset - center, _state$modifiersData$);
}
function effect$1(_ref2) {
	var state = _ref2.state;
	var _options$element = _ref2.options.element, arrowElement = _options$element === void 0 ? "[data-popper-arrow]" : _options$element;
	if (arrowElement == null) return;
	if (typeof arrowElement === "string") {
		arrowElement = state.elements.popper.querySelector(arrowElement);
		if (!arrowElement) return;
	}
	if (!contains(state.elements.popper, arrowElement)) return;
	state.elements.arrow = arrowElement;
}
var arrow_default = {
	name: "arrow",
	enabled: true,
	phase: "main",
	fn: arrow,
	effect: effect$1,
	requires: ["popperOffsets"],
	requiresIfExists: ["preventOverflow"]
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getVariation.js
function getVariation(placement) {
	return placement.split("-")[1];
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/computeStyles.js
var unsetSides = {
	top: "auto",
	right: "auto",
	bottom: "auto",
	left: "auto"
};
function roundOffsetsByDPR(_ref, win) {
	var x = _ref.x, y = _ref.y;
	var dpr = win.devicePixelRatio || 1;
	return {
		x: round(x * dpr) / dpr || 0,
		y: round(y * dpr) / dpr || 0
	};
}
function mapToStyles(_ref2) {
	var _Object$assign2;
	var popper = _ref2.popper, popperRect = _ref2.popperRect, placement = _ref2.placement, variation = _ref2.variation, offsets = _ref2.offsets, position = _ref2.position, gpuAcceleration = _ref2.gpuAcceleration, adaptive = _ref2.adaptive, roundOffsets = _ref2.roundOffsets, isFixed = _ref2.isFixed;
	var _offsets$x = offsets.x, x = _offsets$x === void 0 ? 0 : _offsets$x, _offsets$y = offsets.y, y = _offsets$y === void 0 ? 0 : _offsets$y;
	var _ref3 = typeof roundOffsets === "function" ? roundOffsets({
		x,
		y
	}) : {
		x,
		y
	};
	x = _ref3.x;
	y = _ref3.y;
	var hasX = offsets.hasOwnProperty("x");
	var hasY = offsets.hasOwnProperty("y");
	var sideX = left;
	var sideY = "top";
	var win = window;
	if (adaptive) {
		var offsetParent = getOffsetParent(popper);
		var heightProp = "clientHeight";
		var widthProp = "clientWidth";
		if (offsetParent === getWindow(popper)) {
			offsetParent = getDocumentElement(popper);
			if (getComputedStyle$1(offsetParent).position !== "static" && position === "absolute") {
				heightProp = "scrollHeight";
				widthProp = "scrollWidth";
			}
		}
		offsetParent = offsetParent;
		if (placement === "top" || (placement === "left" || placement === "right") && variation === "end") {
			sideY = bottom;
			var offsetY = isFixed && offsetParent === win && win.visualViewport ? win.visualViewport.height : offsetParent[heightProp];
			y -= offsetY - popperRect.height;
			y *= gpuAcceleration ? 1 : -1;
		}
		if (placement === "left" || (placement === "top" || placement === "bottom") && variation === "end") {
			sideX = right;
			var offsetX = isFixed && offsetParent === win && win.visualViewport ? win.visualViewport.width : offsetParent[widthProp];
			x -= offsetX - popperRect.width;
			x *= gpuAcceleration ? 1 : -1;
		}
	}
	var commonStyles = Object.assign({ position }, adaptive && unsetSides);
	var _ref4 = roundOffsets === true ? roundOffsetsByDPR({
		x,
		y
	}, getWindow(popper)) : {
		x,
		y
	};
	x = _ref4.x;
	y = _ref4.y;
	if (gpuAcceleration) {
		var _Object$assign;
		return Object.assign({}, commonStyles, (_Object$assign = {}, _Object$assign[sideY] = hasY ? "0" : "", _Object$assign[sideX] = hasX ? "0" : "", _Object$assign.transform = (win.devicePixelRatio || 1) <= 1 ? "translate(" + x + "px, " + y + "px)" : "translate3d(" + x + "px, " + y + "px, 0)", _Object$assign));
	}
	return Object.assign({}, commonStyles, (_Object$assign2 = {}, _Object$assign2[sideY] = hasY ? y + "px" : "", _Object$assign2[sideX] = hasX ? x + "px" : "", _Object$assign2.transform = "", _Object$assign2));
}
function computeStyles(_ref5) {
	var state = _ref5.state, options = _ref5.options;
	var _options$gpuAccelerat = options.gpuAcceleration, gpuAcceleration = _options$gpuAccelerat === void 0 ? true : _options$gpuAccelerat, _options$adaptive = options.adaptive, adaptive = _options$adaptive === void 0 ? true : _options$adaptive, _options$roundOffsets = options.roundOffsets, roundOffsets = _options$roundOffsets === void 0 ? true : _options$roundOffsets;
	var commonStyles = {
		placement: getBasePlacement(state.placement),
		variation: getVariation(state.placement),
		popper: state.elements.popper,
		popperRect: state.rects.popper,
		gpuAcceleration,
		isFixed: state.options.strategy === "fixed"
	};
	if (state.modifiersData.popperOffsets != null) state.styles.popper = Object.assign({}, state.styles.popper, mapToStyles(Object.assign({}, commonStyles, {
		offsets: state.modifiersData.popperOffsets,
		position: state.options.strategy,
		adaptive,
		roundOffsets
	})));
	if (state.modifiersData.arrow != null) state.styles.arrow = Object.assign({}, state.styles.arrow, mapToStyles(Object.assign({}, commonStyles, {
		offsets: state.modifiersData.arrow,
		position: "absolute",
		adaptive: false,
		roundOffsets
	})));
	state.attributes.popper = Object.assign({}, state.attributes.popper, { "data-popper-placement": state.placement });
}
var computeStyles_default = {
	name: "computeStyles",
	enabled: true,
	phase: "beforeWrite",
	fn: computeStyles,
	data: {}
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/eventListeners.js
var passive = { passive: true };
function effect(_ref) {
	var state = _ref.state, instance = _ref.instance, options = _ref.options;
	var _options$scroll = options.scroll, scroll = _options$scroll === void 0 ? true : _options$scroll, _options$resize = options.resize, resize = _options$resize === void 0 ? true : _options$resize;
	var window = getWindow(state.elements.popper);
	var scrollParents = [].concat(state.scrollParents.reference, state.scrollParents.popper);
	if (scroll) scrollParents.forEach(function(scrollParent) {
		scrollParent.addEventListener("scroll", instance.update, passive);
	});
	if (resize) window.addEventListener("resize", instance.update, passive);
	return function() {
		if (scroll) scrollParents.forEach(function(scrollParent) {
			scrollParent.removeEventListener("scroll", instance.update, passive);
		});
		if (resize) window.removeEventListener("resize", instance.update, passive);
	};
}
var eventListeners_default = {
	name: "eventListeners",
	enabled: true,
	phase: "write",
	fn: function fn() {},
	effect,
	data: {}
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getOppositePlacement.js
var hash$1 = {
	left: "right",
	right: "left",
	bottom: "top",
	top: "bottom"
};
function getOppositePlacement(placement) {
	return placement.replace(/left|right|bottom|top/g, function(matched) {
		return hash$1[matched];
	});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getOppositeVariationPlacement.js
var hash = {
	start: "end",
	end: "start"
};
function getOppositeVariationPlacement(placement) {
	return placement.replace(/start|end/g, function(matched) {
		return hash[matched];
	});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getWindowScroll.js
function getWindowScroll(node) {
	var win = getWindow(node);
	return {
		scrollLeft: win.pageXOffset,
		scrollTop: win.pageYOffset
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getWindowScrollBarX.js
function getWindowScrollBarX(element) {
	return getBoundingClientRect(getDocumentElement(element)).left + getWindowScroll(element).scrollLeft;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getViewportRect.js
function getViewportRect(element, strategy) {
	var win = getWindow(element);
	var html = getDocumentElement(element);
	var visualViewport = win.visualViewport;
	var width = html.clientWidth;
	var height = html.clientHeight;
	var x = 0;
	var y = 0;
	if (visualViewport) {
		width = visualViewport.width;
		height = visualViewport.height;
		var layoutViewport = isLayoutViewport();
		if (layoutViewport || !layoutViewport && strategy === "fixed") {
			x = visualViewport.offsetLeft;
			y = visualViewport.offsetTop;
		}
	}
	return {
		width,
		height,
		x: x + getWindowScrollBarX(element),
		y
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getDocumentRect.js
function getDocumentRect(element) {
	var _element$ownerDocumen;
	var html = getDocumentElement(element);
	var winScroll = getWindowScroll(element);
	var body = (_element$ownerDocumen = element.ownerDocument) == null ? void 0 : _element$ownerDocumen.body;
	var width = max(html.scrollWidth, html.clientWidth, body ? body.scrollWidth : 0, body ? body.clientWidth : 0);
	var height = max(html.scrollHeight, html.clientHeight, body ? body.scrollHeight : 0, body ? body.clientHeight : 0);
	var x = -winScroll.scrollLeft + getWindowScrollBarX(element);
	var y = -winScroll.scrollTop;
	if (getComputedStyle$1(body || html).direction === "rtl") x += max(html.clientWidth, body ? body.clientWidth : 0) - width;
	return {
		width,
		height,
		x,
		y
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/isScrollParent.js
function isScrollParent(element) {
	var _getComputedStyle = getComputedStyle$1(element), overflow = _getComputedStyle.overflow, overflowX = _getComputedStyle.overflowX, overflowY = _getComputedStyle.overflowY;
	return /auto|scroll|overlay|hidden/.test(overflow + overflowY + overflowX);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getScrollParent.js
function getScrollParent(node) {
	if ([
		"html",
		"body",
		"#document"
	].indexOf(getNodeName(node)) >= 0) return node.ownerDocument.body;
	if (isHTMLElement(node) && isScrollParent(node)) return node;
	return getScrollParent(getParentNode(node));
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/listScrollParents.js
function listScrollParents(element, list) {
	var _element$ownerDocumen;
	if (list === void 0) list = [];
	var scrollParent = getScrollParent(element);
	var isBody = scrollParent === ((_element$ownerDocumen = element.ownerDocument) == null ? void 0 : _element$ownerDocumen.body);
	var win = getWindow(scrollParent);
	var target = isBody ? [win].concat(win.visualViewport || [], isScrollParent(scrollParent) ? scrollParent : []) : scrollParent;
	var updatedList = list.concat(target);
	return isBody ? updatedList : updatedList.concat(listScrollParents(getParentNode(target)));
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/rectToClientRect.js
function rectToClientRect(rect) {
	return Object.assign({}, rect, {
		left: rect.x,
		top: rect.y,
		right: rect.x + rect.width,
		bottom: rect.y + rect.height
	});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getClippingRect.js
function getInnerBoundingClientRect(element, strategy) {
	var rect = getBoundingClientRect(element, false, strategy === "fixed");
	rect.top = rect.top + element.clientTop;
	rect.left = rect.left + element.clientLeft;
	rect.bottom = rect.top + element.clientHeight;
	rect.right = rect.left + element.clientWidth;
	rect.width = element.clientWidth;
	rect.height = element.clientHeight;
	rect.x = rect.left;
	rect.y = rect.top;
	return rect;
}
function getClientRectFromMixedType(element, clippingParent, strategy) {
	return clippingParent === "viewport" ? rectToClientRect(getViewportRect(element, strategy)) : isElement(clippingParent) ? getInnerBoundingClientRect(clippingParent, strategy) : rectToClientRect(getDocumentRect(getDocumentElement(element)));
}
function getClippingParents(element) {
	var clippingParents = listScrollParents(getParentNode(element));
	var clipperElement = ["absolute", "fixed"].indexOf(getComputedStyle$1(element).position) >= 0 && isHTMLElement(element) ? getOffsetParent(element) : element;
	if (!isElement(clipperElement)) return [];
	return clippingParents.filter(function(clippingParent) {
		return isElement(clippingParent) && contains(clippingParent, clipperElement) && getNodeName(clippingParent) !== "body";
	});
}
function getClippingRect(element, boundary, rootBoundary, strategy) {
	var mainClippingParents = boundary === "clippingParents" ? getClippingParents(element) : [].concat(boundary);
	var clippingParents = [].concat(mainClippingParents, [rootBoundary]);
	var firstClippingParent = clippingParents[0];
	var clippingRect = clippingParents.reduce(function(accRect, clippingParent) {
		var rect = getClientRectFromMixedType(element, clippingParent, strategy);
		accRect.top = max(rect.top, accRect.top);
		accRect.right = min(rect.right, accRect.right);
		accRect.bottom = min(rect.bottom, accRect.bottom);
		accRect.left = max(rect.left, accRect.left);
		return accRect;
	}, getClientRectFromMixedType(element, firstClippingParent, strategy));
	clippingRect.width = clippingRect.right - clippingRect.left;
	clippingRect.height = clippingRect.bottom - clippingRect.top;
	clippingRect.x = clippingRect.left;
	clippingRect.y = clippingRect.top;
	return clippingRect;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/computeOffsets.js
function computeOffsets(_ref) {
	var reference = _ref.reference, element = _ref.element, placement = _ref.placement;
	var basePlacement = placement ? getBasePlacement(placement) : null;
	var variation = placement ? getVariation(placement) : null;
	var commonX = reference.x + reference.width / 2 - element.width / 2;
	var commonY = reference.y + reference.height / 2 - element.height / 2;
	var offsets;
	switch (basePlacement) {
		case "top":
			offsets = {
				x: commonX,
				y: reference.y - element.height
			};
			break;
		case bottom:
			offsets = {
				x: commonX,
				y: reference.y + reference.height
			};
			break;
		case right:
			offsets = {
				x: reference.x + reference.width,
				y: commonY
			};
			break;
		case left:
			offsets = {
				x: reference.x - element.width,
				y: commonY
			};
			break;
		default: offsets = {
			x: reference.x,
			y: reference.y
		};
	}
	var mainAxis = basePlacement ? getMainAxisFromPlacement(basePlacement) : null;
	if (mainAxis != null) {
		var len = mainAxis === "y" ? "height" : "width";
		switch (variation) {
			case start:
				offsets[mainAxis] = offsets[mainAxis] - (reference[len] / 2 - element[len] / 2);
				break;
			case "end": offsets[mainAxis] = offsets[mainAxis] + (reference[len] / 2 - element[len] / 2);
		}
	}
	return offsets;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/detectOverflow.js
function detectOverflow(state, options) {
	if (options === void 0) options = {};
	var _options = options, _options$placement = _options.placement, placement = _options$placement === void 0 ? state.placement : _options$placement, _options$strategy = _options.strategy, strategy = _options$strategy === void 0 ? state.strategy : _options$strategy, _options$boundary = _options.boundary, boundary = _options$boundary === void 0 ? clippingParents : _options$boundary, _options$rootBoundary = _options.rootBoundary, rootBoundary = _options$rootBoundary === void 0 ? viewport : _options$rootBoundary, _options$elementConte = _options.elementContext, elementContext = _options$elementConte === void 0 ? popper : _options$elementConte, _options$altBoundary = _options.altBoundary, altBoundary = _options$altBoundary === void 0 ? false : _options$altBoundary, _options$padding = _options.padding, padding = _options$padding === void 0 ? 0 : _options$padding;
	var paddingObject = mergePaddingObject(typeof padding !== "number" ? padding : expandToHashMap(padding, basePlacements));
	var altContext = elementContext === "popper" ? reference : popper;
	var popperRect = state.rects.popper;
	var element = state.elements[altBoundary ? altContext : elementContext];
	var clippingClientRect = getClippingRect(isElement(element) ? element : element.contextElement || getDocumentElement(state.elements.popper), boundary, rootBoundary, strategy);
	var referenceClientRect = getBoundingClientRect(state.elements.reference);
	var popperOffsets = computeOffsets({
		reference: referenceClientRect,
		element: popperRect,
		strategy: "absolute",
		placement
	});
	var popperClientRect = rectToClientRect(Object.assign({}, popperRect, popperOffsets));
	var elementClientRect = elementContext === "popper" ? popperClientRect : referenceClientRect;
	var overflowOffsets = {
		top: clippingClientRect.top - elementClientRect.top + paddingObject.top,
		bottom: elementClientRect.bottom - clippingClientRect.bottom + paddingObject.bottom,
		left: clippingClientRect.left - elementClientRect.left + paddingObject.left,
		right: elementClientRect.right - clippingClientRect.right + paddingObject.right
	};
	var offsetData = state.modifiersData.offset;
	if (elementContext === "popper" && offsetData) {
		var offset = offsetData[placement];
		Object.keys(overflowOffsets).forEach(function(key) {
			var multiply = ["right", "bottom"].indexOf(key) >= 0 ? 1 : -1;
			var axis = ["top", "bottom"].indexOf(key) >= 0 ? "y" : "x";
			overflowOffsets[key] += offset[axis] * multiply;
		});
	}
	return overflowOffsets;
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/computeAutoPlacement.js
function computeAutoPlacement(state, options) {
	if (options === void 0) options = {};
	var _options = options, placement = _options.placement, boundary = _options.boundary, rootBoundary = _options.rootBoundary, padding = _options.padding, flipVariations = _options.flipVariations, _options$allowedAutoP = _options.allowedAutoPlacements, allowedAutoPlacements = _options$allowedAutoP === void 0 ? placements : _options$allowedAutoP;
	var variation = getVariation(placement);
	var placements$1 = variation ? flipVariations ? variationPlacements : variationPlacements.filter(function(placement) {
		return getVariation(placement) === variation;
	}) : basePlacements;
	var allowedPlacements = placements$1.filter(function(placement) {
		return allowedAutoPlacements.indexOf(placement) >= 0;
	});
	if (allowedPlacements.length === 0) allowedPlacements = placements$1;
	var overflows = allowedPlacements.reduce(function(acc, placement) {
		acc[placement] = detectOverflow(state, {
			placement,
			boundary,
			rootBoundary,
			padding
		})[getBasePlacement(placement)];
		return acc;
	}, {});
	return Object.keys(overflows).sort(function(a, b) {
		return overflows[a] - overflows[b];
	});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/flip.js
function getExpandedFallbackPlacements(placement) {
	if (getBasePlacement(placement) === "auto") return [];
	var oppositePlacement = getOppositePlacement(placement);
	return [
		getOppositeVariationPlacement(placement),
		oppositePlacement,
		getOppositeVariationPlacement(oppositePlacement)
	];
}
function flip(_ref) {
	var state = _ref.state, options = _ref.options, name = _ref.name;
	if (state.modifiersData[name]._skip) return;
	var _options$mainAxis = options.mainAxis, checkMainAxis = _options$mainAxis === void 0 ? true : _options$mainAxis, _options$altAxis = options.altAxis, checkAltAxis = _options$altAxis === void 0 ? true : _options$altAxis, specifiedFallbackPlacements = options.fallbackPlacements, padding = options.padding, boundary = options.boundary, rootBoundary = options.rootBoundary, altBoundary = options.altBoundary, _options$flipVariatio = options.flipVariations, flipVariations = _options$flipVariatio === void 0 ? true : _options$flipVariatio, allowedAutoPlacements = options.allowedAutoPlacements;
	var preferredPlacement = state.options.placement;
	var isBasePlacement = getBasePlacement(preferredPlacement) === preferredPlacement;
	var fallbackPlacements = specifiedFallbackPlacements || (isBasePlacement || !flipVariations ? [getOppositePlacement(preferredPlacement)] : getExpandedFallbackPlacements(preferredPlacement));
	var placements = [preferredPlacement].concat(fallbackPlacements).reduce(function(acc, placement) {
		return acc.concat(getBasePlacement(placement) === "auto" ? computeAutoPlacement(state, {
			placement,
			boundary,
			rootBoundary,
			padding,
			flipVariations,
			allowedAutoPlacements
		}) : placement);
	}, []);
	var referenceRect = state.rects.reference;
	var popperRect = state.rects.popper;
	var checksMap = /* @__PURE__ */ new Map();
	var makeFallbackChecks = true;
	var firstFittingPlacement = placements[0];
	for (var i = 0; i < placements.length; i++) {
		var placement = placements[i];
		var _basePlacement = getBasePlacement(placement);
		var isStartVariation = getVariation(placement) === start;
		var isVertical = ["top", bottom].indexOf(_basePlacement) >= 0;
		var len = isVertical ? "width" : "height";
		var overflow = detectOverflow(state, {
			placement,
			boundary,
			rootBoundary,
			altBoundary,
			padding
		});
		var mainVariationSide = isVertical ? isStartVariation ? right : left : isStartVariation ? bottom : "top";
		if (referenceRect[len] > popperRect[len]) mainVariationSide = getOppositePlacement(mainVariationSide);
		var altVariationSide = getOppositePlacement(mainVariationSide);
		var checks = [];
		if (checkMainAxis) checks.push(overflow[_basePlacement] <= 0);
		if (checkAltAxis) checks.push(overflow[mainVariationSide] <= 0, overflow[altVariationSide] <= 0);
		if (checks.every(function(check) {
			return check;
		})) {
			firstFittingPlacement = placement;
			makeFallbackChecks = false;
			break;
		}
		checksMap.set(placement, checks);
	}
	if (makeFallbackChecks) {
		var numberOfChecks = flipVariations ? 3 : 1;
		var _loop = function _loop(_i) {
			var fittingPlacement = placements.find(function(placement) {
				var checks = checksMap.get(placement);
				if (checks) return checks.slice(0, _i).every(function(check) {
					return check;
				});
			});
			if (fittingPlacement) {
				firstFittingPlacement = fittingPlacement;
				return "break";
			}
		};
		for (var _i = numberOfChecks; _i > 0; _i--) if (_loop(_i) === "break") break;
	}
	if (state.placement !== firstFittingPlacement) {
		state.modifiersData[name]._skip = true;
		state.placement = firstFittingPlacement;
		state.reset = true;
	}
}
var flip_default = {
	name: "flip",
	enabled: true,
	phase: "main",
	fn: flip,
	requiresIfExists: ["offset"],
	data: { _skip: false }
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/hide.js
function getSideOffsets(overflow, rect, preventedOffsets) {
	if (preventedOffsets === void 0) preventedOffsets = {
		x: 0,
		y: 0
	};
	return {
		top: overflow.top - rect.height - preventedOffsets.y,
		right: overflow.right - rect.width + preventedOffsets.x,
		bottom: overflow.bottom - rect.height + preventedOffsets.y,
		left: overflow.left - rect.width - preventedOffsets.x
	};
}
function isAnySideFullyClipped(overflow) {
	return [
		"top",
		right,
		bottom,
		left
	].some(function(side) {
		return overflow[side] >= 0;
	});
}
function hide(_ref) {
	var state = _ref.state, name = _ref.name;
	var referenceRect = state.rects.reference;
	var popperRect = state.rects.popper;
	var preventedOffsets = state.modifiersData.preventOverflow;
	var referenceOverflow = detectOverflow(state, { elementContext: "reference" });
	var popperAltOverflow = detectOverflow(state, { altBoundary: true });
	var referenceClippingOffsets = getSideOffsets(referenceOverflow, referenceRect);
	var popperEscapeOffsets = getSideOffsets(popperAltOverflow, popperRect, preventedOffsets);
	var isReferenceHidden = isAnySideFullyClipped(referenceClippingOffsets);
	var hasPopperEscaped = isAnySideFullyClipped(popperEscapeOffsets);
	state.modifiersData[name] = {
		referenceClippingOffsets,
		popperEscapeOffsets,
		isReferenceHidden,
		hasPopperEscaped
	};
	state.attributes.popper = Object.assign({}, state.attributes.popper, {
		"data-popper-reference-hidden": isReferenceHidden,
		"data-popper-escaped": hasPopperEscaped
	});
}
var hide_default = {
	name: "hide",
	enabled: true,
	phase: "main",
	requiresIfExists: ["preventOverflow"],
	fn: hide
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/offset.js
function distanceAndSkiddingToXY(placement, rects, offset) {
	var basePlacement = getBasePlacement(placement);
	var invertDistance = ["left", "top"].indexOf(basePlacement) >= 0 ? -1 : 1;
	var _ref = typeof offset === "function" ? offset(Object.assign({}, rects, { placement })) : offset, skidding = _ref[0], distance = _ref[1];
	skidding = skidding || 0;
	distance = (distance || 0) * invertDistance;
	return ["left", "right"].indexOf(basePlacement) >= 0 ? {
		x: distance,
		y: skidding
	} : {
		x: skidding,
		y: distance
	};
}
function offset(_ref2) {
	var state = _ref2.state, options = _ref2.options, name = _ref2.name;
	var _options$offset = options.offset, offset = _options$offset === void 0 ? [0, 0] : _options$offset;
	var data = placements.reduce(function(acc, placement) {
		acc[placement] = distanceAndSkiddingToXY(placement, state.rects, offset);
		return acc;
	}, {});
	var _data$state$placement = data[state.placement], x = _data$state$placement.x, y = _data$state$placement.y;
	if (state.modifiersData.popperOffsets != null) {
		state.modifiersData.popperOffsets.x += x;
		state.modifiersData.popperOffsets.y += y;
	}
	state.modifiersData[name] = data;
}
var offset_default = {
	name: "offset",
	enabled: true,
	phase: "main",
	requires: ["popperOffsets"],
	fn: offset
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/popperOffsets.js
function popperOffsets(_ref) {
	var state = _ref.state, name = _ref.name;
	state.modifiersData[name] = computeOffsets({
		reference: state.rects.reference,
		element: state.rects.popper,
		strategy: "absolute",
		placement: state.placement
	});
}
var popperOffsets_default = {
	name: "popperOffsets",
	enabled: true,
	phase: "read",
	fn: popperOffsets,
	data: {}
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/getAltAxis.js
function getAltAxis(axis) {
	return axis === "x" ? "y" : "x";
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/modifiers/preventOverflow.js
function preventOverflow(_ref) {
	var state = _ref.state, options = _ref.options, name = _ref.name;
	var _options$mainAxis = options.mainAxis, checkMainAxis = _options$mainAxis === void 0 ? true : _options$mainAxis, _options$altAxis = options.altAxis, checkAltAxis = _options$altAxis === void 0 ? false : _options$altAxis, boundary = options.boundary, rootBoundary = options.rootBoundary, altBoundary = options.altBoundary, padding = options.padding, _options$tether = options.tether, tether = _options$tether === void 0 ? true : _options$tether, _options$tetherOffset = options.tetherOffset, tetherOffset = _options$tetherOffset === void 0 ? 0 : _options$tetherOffset;
	var overflow = detectOverflow(state, {
		boundary,
		rootBoundary,
		padding,
		altBoundary
	});
	var basePlacement = getBasePlacement(state.placement);
	var variation = getVariation(state.placement);
	var isBasePlacement = !variation;
	var mainAxis = getMainAxisFromPlacement(basePlacement);
	var altAxis = getAltAxis(mainAxis);
	var popperOffsets = state.modifiersData.popperOffsets;
	var referenceRect = state.rects.reference;
	var popperRect = state.rects.popper;
	var tetherOffsetValue = typeof tetherOffset === "function" ? tetherOffset(Object.assign({}, state.rects, { placement: state.placement })) : tetherOffset;
	var normalizedTetherOffsetValue = typeof tetherOffsetValue === "number" ? {
		mainAxis: tetherOffsetValue,
		altAxis: tetherOffsetValue
	} : Object.assign({
		mainAxis: 0,
		altAxis: 0
	}, tetherOffsetValue);
	var offsetModifierState = state.modifiersData.offset ? state.modifiersData.offset[state.placement] : null;
	var data = {
		x: 0,
		y: 0
	};
	if (!popperOffsets) return;
	if (checkMainAxis) {
		var _offsetModifierState$;
		var mainSide = mainAxis === "y" ? "top" : left;
		var altSide = mainAxis === "y" ? bottom : right;
		var len = mainAxis === "y" ? "height" : "width";
		var offset = popperOffsets[mainAxis];
		var min$1 = offset + overflow[mainSide];
		var max$1 = offset - overflow[altSide];
		var additive = tether ? -popperRect[len] / 2 : 0;
		var minLen = variation === "start" ? referenceRect[len] : popperRect[len];
		var maxLen = variation === "start" ? -popperRect[len] : -referenceRect[len];
		var arrowElement = state.elements.arrow;
		var arrowRect = tether && arrowElement ? getLayoutRect(arrowElement) : {
			width: 0,
			height: 0
		};
		var arrowPaddingObject = state.modifiersData["arrow#persistent"] ? state.modifiersData["arrow#persistent"].padding : getFreshSideObject();
		var arrowPaddingMin = arrowPaddingObject[mainSide];
		var arrowPaddingMax = arrowPaddingObject[altSide];
		var arrowLen = within(0, referenceRect[len], arrowRect[len]);
		var minOffset = isBasePlacement ? referenceRect[len] / 2 - additive - arrowLen - arrowPaddingMin - normalizedTetherOffsetValue.mainAxis : minLen - arrowLen - arrowPaddingMin - normalizedTetherOffsetValue.mainAxis;
		var maxOffset = isBasePlacement ? -referenceRect[len] / 2 + additive + arrowLen + arrowPaddingMax + normalizedTetherOffsetValue.mainAxis : maxLen + arrowLen + arrowPaddingMax + normalizedTetherOffsetValue.mainAxis;
		var arrowOffsetParent = state.elements.arrow && getOffsetParent(state.elements.arrow);
		var clientOffset = arrowOffsetParent ? mainAxis === "y" ? arrowOffsetParent.clientTop || 0 : arrowOffsetParent.clientLeft || 0 : 0;
		var offsetModifierValue = (_offsetModifierState$ = offsetModifierState == null ? void 0 : offsetModifierState[mainAxis]) != null ? _offsetModifierState$ : 0;
		var tetherMin = offset + minOffset - offsetModifierValue - clientOffset;
		var tetherMax = offset + maxOffset - offsetModifierValue;
		var preventedOffset = within(tether ? min(min$1, tetherMin) : min$1, offset, tether ? max(max$1, tetherMax) : max$1);
		popperOffsets[mainAxis] = preventedOffset;
		data[mainAxis] = preventedOffset - offset;
	}
	if (checkAltAxis) {
		var _offsetModifierState$2;
		var _mainSide = mainAxis === "x" ? "top" : left;
		var _altSide = mainAxis === "x" ? bottom : right;
		var _offset = popperOffsets[altAxis];
		var _len = altAxis === "y" ? "height" : "width";
		var _min = _offset + overflow[_mainSide];
		var _max = _offset - overflow[_altSide];
		var isOriginSide = ["top", left].indexOf(basePlacement) !== -1;
		var _offsetModifierValue = (_offsetModifierState$2 = offsetModifierState == null ? void 0 : offsetModifierState[altAxis]) != null ? _offsetModifierState$2 : 0;
		var _tetherMin = isOriginSide ? _min : _offset - referenceRect[_len] - popperRect[_len] - _offsetModifierValue + normalizedTetherOffsetValue.altAxis;
		var _tetherMax = isOriginSide ? _offset + referenceRect[_len] + popperRect[_len] - _offsetModifierValue - normalizedTetherOffsetValue.altAxis : _max;
		var _preventedOffset = tether && isOriginSide ? withinMaxClamp(_tetherMin, _offset, _tetherMax) : within(tether ? _tetherMin : _min, _offset, tether ? _tetherMax : _max);
		popperOffsets[altAxis] = _preventedOffset;
		data[altAxis] = _preventedOffset - _offset;
	}
	state.modifiersData[name] = data;
}
var preventOverflow_default = {
	name: "preventOverflow",
	enabled: true,
	phase: "main",
	fn: preventOverflow,
	requiresIfExists: ["offset"]
};
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getHTMLElementScroll.js
function getHTMLElementScroll(element) {
	return {
		scrollLeft: element.scrollLeft,
		scrollTop: element.scrollTop
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getNodeScroll.js
function getNodeScroll(node) {
	if (node === getWindow(node) || !isHTMLElement(node)) return getWindowScroll(node);
	else return getHTMLElementScroll(node);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/dom-utils/getCompositeRect.js
function isElementScaled(element) {
	var rect = element.getBoundingClientRect();
	var scaleX = round(rect.width) / element.offsetWidth || 1;
	var scaleY = round(rect.height) / element.offsetHeight || 1;
	return scaleX !== 1 || scaleY !== 1;
}
function getCompositeRect(elementOrVirtualElement, offsetParent, isFixed) {
	if (isFixed === void 0) isFixed = false;
	var isOffsetParentAnElement = isHTMLElement(offsetParent);
	var offsetParentIsScaled = isHTMLElement(offsetParent) && isElementScaled(offsetParent);
	var documentElement = getDocumentElement(offsetParent);
	var rect = getBoundingClientRect(elementOrVirtualElement, offsetParentIsScaled, isFixed);
	var scroll = {
		scrollLeft: 0,
		scrollTop: 0
	};
	var offsets = {
		x: 0,
		y: 0
	};
	if (isOffsetParentAnElement || !isOffsetParentAnElement && !isFixed) {
		if (getNodeName(offsetParent) !== "body" || isScrollParent(documentElement)) scroll = getNodeScroll(offsetParent);
		if (isHTMLElement(offsetParent)) {
			offsets = getBoundingClientRect(offsetParent, true);
			offsets.x += offsetParent.clientLeft;
			offsets.y += offsetParent.clientTop;
		} else if (documentElement) offsets.x = getWindowScrollBarX(documentElement);
	}
	return {
		x: rect.left + scroll.scrollLeft - offsets.x,
		y: rect.top + scroll.scrollTop - offsets.y,
		width: rect.width,
		height: rect.height
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/orderModifiers.js
function order(modifiers) {
	var map = /* @__PURE__ */ new Map();
	var visited = /* @__PURE__ */ new Set();
	var result = [];
	modifiers.forEach(function(modifier) {
		map.set(modifier.name, modifier);
	});
	function sort(modifier) {
		visited.add(modifier.name);
		[].concat(modifier.requires || [], modifier.requiresIfExists || []).forEach(function(dep) {
			if (!visited.has(dep)) {
				var depModifier = map.get(dep);
				if (depModifier) sort(depModifier);
			}
		});
		result.push(modifier);
	}
	modifiers.forEach(function(modifier) {
		if (!visited.has(modifier.name)) sort(modifier);
	});
	return result;
}
function orderModifiers(modifiers) {
	var orderedModifiers = order(modifiers);
	return modifierPhases.reduce(function(acc, phase) {
		return acc.concat(orderedModifiers.filter(function(modifier) {
			return modifier.phase === phase;
		}));
	}, []);
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/debounce.js
function debounce(fn) {
	var pending;
	return function() {
		if (!pending) pending = new Promise(function(resolve) {
			Promise.resolve().then(function() {
				pending = void 0;
				resolve(fn());
			});
		});
		return pending;
	};
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/utils/mergeByName.js
function mergeByName(modifiers) {
	var merged = modifiers.reduce(function(merged, current) {
		var existing = merged[current.name];
		merged[current.name] = existing ? Object.assign({}, existing, current, {
			options: Object.assign({}, existing.options, current.options),
			data: Object.assign({}, existing.data, current.data)
		}) : current;
		return merged;
	}, {});
	return Object.keys(merged).map(function(key) {
		return merged[key];
	});
}
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/createPopper.js
var DEFAULT_OPTIONS = {
	placement: "bottom",
	modifiers: [],
	strategy: "absolute"
};
function areValidElements() {
	for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) args[_key] = arguments[_key];
	return !args.some(function(element) {
		return !(element && typeof element.getBoundingClientRect === "function");
	});
}
function popperGenerator(generatorOptions) {
	if (generatorOptions === void 0) generatorOptions = {};
	var _generatorOptions = generatorOptions, _generatorOptions$def = _generatorOptions.defaultModifiers, defaultModifiers = _generatorOptions$def === void 0 ? [] : _generatorOptions$def, _generatorOptions$def2 = _generatorOptions.defaultOptions, defaultOptions = _generatorOptions$def2 === void 0 ? DEFAULT_OPTIONS : _generatorOptions$def2;
	return function createPopper(reference, popper, options) {
		if (options === void 0) options = defaultOptions;
		var state = {
			placement: "bottom",
			orderedModifiers: [],
			options: Object.assign({}, DEFAULT_OPTIONS, defaultOptions),
			modifiersData: {},
			elements: {
				reference,
				popper
			},
			attributes: {},
			styles: {}
		};
		var effectCleanupFns = [];
		var isDestroyed = false;
		var instance = {
			state,
			setOptions: function setOptions(setOptionsAction) {
				var options = typeof setOptionsAction === "function" ? setOptionsAction(state.options) : setOptionsAction;
				cleanupModifierEffects();
				state.options = Object.assign({}, defaultOptions, state.options, options);
				state.scrollParents = {
					reference: isElement(reference) ? listScrollParents(reference) : reference.contextElement ? listScrollParents(reference.contextElement) : [],
					popper: listScrollParents(popper)
				};
				var orderedModifiers = orderModifiers(mergeByName([].concat(defaultModifiers, state.options.modifiers)));
				state.orderedModifiers = orderedModifiers.filter(function(m) {
					return m.enabled;
				});
				runModifierEffects();
				return instance.update();
			},
			forceUpdate: function forceUpdate() {
				if (isDestroyed) return;
				var _state$elements = state.elements, reference = _state$elements.reference, popper = _state$elements.popper;
				if (!areValidElements(reference, popper)) return;
				state.rects = {
					reference: getCompositeRect(reference, getOffsetParent(popper), state.options.strategy === "fixed"),
					popper: getLayoutRect(popper)
				};
				state.reset = false;
				state.placement = state.options.placement;
				state.orderedModifiers.forEach(function(modifier) {
					return state.modifiersData[modifier.name] = Object.assign({}, modifier.data);
				});
				for (var index = 0; index < state.orderedModifiers.length; index++) {
					if (state.reset === true) {
						state.reset = false;
						index = -1;
						continue;
					}
					var _state$orderedModifie = state.orderedModifiers[index], fn = _state$orderedModifie.fn, _state$orderedModifie2 = _state$orderedModifie.options, _options = _state$orderedModifie2 === void 0 ? {} : _state$orderedModifie2, name = _state$orderedModifie.name;
					if (typeof fn === "function") state = fn({
						state,
						options: _options,
						name,
						instance
					}) || state;
				}
			},
			update: debounce(function() {
				return new Promise(function(resolve) {
					instance.forceUpdate();
					resolve(state);
				});
			}),
			destroy: function destroy() {
				cleanupModifierEffects();
				isDestroyed = true;
			}
		};
		if (!areValidElements(reference, popper)) return instance;
		instance.setOptions(options).then(function(state) {
			if (!isDestroyed && options.onFirstUpdate) options.onFirstUpdate(state);
		});
		function runModifierEffects() {
			state.orderedModifiers.forEach(function(_ref) {
				var name = _ref.name, _ref$options = _ref.options, options = _ref$options === void 0 ? {} : _ref$options, effect = _ref.effect;
				if (typeof effect === "function") {
					var cleanupFn = effect({
						state,
						name,
						instance,
						options
					});
					effectCleanupFns.push(cleanupFn || function noopFn() {});
				}
			});
		}
		function cleanupModifierEffects() {
			effectCleanupFns.forEach(function(fn) {
				return fn();
			});
			effectCleanupFns = [];
		}
		return instance;
	};
}
var createPopper$2 = /*#__PURE__*/ popperGenerator();
var createPopper$1 = /*#__PURE__*/ popperGenerator({ defaultModifiers: [
	eventListeners_default,
	popperOffsets_default,
	computeStyles_default,
	applyStyles_default
] });
var createPopper = /*#__PURE__*/ popperGenerator({ defaultModifiers: [
	eventListeners_default,
	popperOffsets_default,
	computeStyles_default,
	applyStyles_default,
	offset_default,
	flip_default,
	preventOverflow_default,
	arrow_default,
	hide_default
] });
//#endregion
//#region ../node_modules/.pnpm/@popperjs+core@2.11.8/node_modules/@popperjs/core/lib/index.js
var lib_exports = /* @__PURE__ */ __exportAll({
	afterMain: () => afterMain,
	afterRead: () => afterRead,
	afterWrite: () => afterWrite,
	applyStyles: () => applyStyles_default,
	arrow: () => arrow_default,
	auto: () => auto,
	basePlacements: () => basePlacements,
	beforeMain: () => beforeMain,
	beforeRead: () => beforeRead,
	beforeWrite: () => beforeWrite,
	bottom: () => bottom,
	clippingParents: () => clippingParents,
	computeStyles: () => computeStyles_default,
	createPopper: () => createPopper,
	createPopperBase: () => createPopper$2,
	createPopperLite: () => createPopper$1,
	detectOverflow: () => detectOverflow,
	end: () => "end",
	eventListeners: () => eventListeners_default,
	flip: () => flip_default,
	hide: () => hide_default,
	left: () => left,
	main: () => main,
	modifierPhases: () => modifierPhases,
	offset: () => offset_default,
	placements: () => placements,
	popper: () => popper,
	popperGenerator: () => popperGenerator,
	popperOffsets: () => popperOffsets_default,
	preventOverflow: () => preventOverflow_default,
	read: () => read,
	reference: () => reference,
	right: () => right,
	start: () => start,
	top: () => "top",
	variationPlacements: () => variationPlacements,
	viewport: () => viewport,
	write: () => write
});
//#endregion
//#region js/src/bootstrap/alert.ts
/**
* --------------------------------------------------------------------------
* Bootstrap alert.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$20 = "alert";
var EVENT_KEY$14 = `.bs.alert`;
var EVENT_CLOSE = `close${EVENT_KEY$14}`;
var EVENT_CLOSED = `closed${EVENT_KEY$14}`;
var CLASS_NAME_FADE$5 = "fade";
var CLASS_NAME_SHOW$8 = "show";
var Alert = class Alert extends BaseComponent {
	static get NAME() {
		return NAME$20;
	}
	close() {
		const closeEvent = EventHandler.trigger(this._element, EVENT_CLOSE);
		if (closeEvent === null || closeEvent === void 0 ? void 0 : closeEvent.defaultPrevented) return;
		this._element.classList.remove(CLASS_NAME_SHOW$8);
		const isAnimated = this._element.classList.contains(CLASS_NAME_FADE$5);
		this._queueCallback(() => this._destroyElement(), this._element, isAnimated);
	}
	_destroyElement() {
		this._element.remove();
		EventHandler.trigger(this._element, EVENT_CLOSED);
		this.dispose();
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Alert.getOrCreateInstance(this);
			if (typeof config !== "string") return;
			if (data[config] === void 0 || config.startsWith("_") || config === "constructor") throw new TypeError(`No method named "${config}"`);
			data[config](this);
		});
	}
};
enableDismissTrigger(Alert, "close");
defineJQueryPlugin(Alert);
//#endregion
//#region js/src/bootstrap/button.ts
/**
* --------------------------------------------------------------------------
* Bootstrap button.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$19 = "button";
var EVENT_KEY$13 = `.bs.button`;
var DATA_API_KEY$6 = ".data-api";
var CLASS_NAME_ACTIVE$5 = "active";
var SELECTOR_DATA_TOGGLE$8 = "[data-bs-toggle=\"button\"], [data-tblr-toggle=\"button\"]";
var EVENT_CLICK_DATA_API$6 = `click${EVENT_KEY$13}${DATA_API_KEY$6}`;
var Button = class Button extends BaseComponent {
	static get NAME() {
		return NAME$19;
	}
	toggle() {
		this._element.setAttribute("aria-pressed", String(this._element.classList.toggle(CLASS_NAME_ACTIVE$5)));
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Button.getOrCreateInstance(this);
			if (config === "toggle") data.toggle();
		});
	}
};
EventHandler.on(document, EVENT_CLICK_DATA_API$6, SELECTOR_DATA_TOGGLE$8, (event) => {
	var _event$target;
	event.preventDefault();
	const target = (_event$target = event.target) === null || _event$target === void 0 ? void 0 : _event$target.closest(SELECTOR_DATA_TOGGLE$8);
	if (!target) return;
	Button.getOrCreateInstance(target).toggle();
});
defineJQueryPlugin(Button);
//#endregion
//#region js/src/bootstrap/util/swipe.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/swipe.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$18 = "swipe";
var EVENT_KEY$12 = ".bs.swipe";
var EVENT_TOUCHSTART = `touchstart${EVENT_KEY$12}`;
var EVENT_TOUCHMOVE = `touchmove${EVENT_KEY$12}`;
var EVENT_TOUCHEND = `touchend${EVENT_KEY$12}`;
var EVENT_POINTERDOWN = `pointerdown${EVENT_KEY$12}`;
var EVENT_POINTERUP = `pointerup${EVENT_KEY$12}`;
var POINTER_TYPE_TOUCH = "touch";
var POINTER_TYPE_PEN = "pen";
var CLASS_NAME_POINTER_EVENT = "pointer-event";
var SWIPE_THRESHOLD = 40;
var Default$17 = {
	endCallback: null,
	leftCallback: null,
	rightCallback: null
};
var DefaultType$17 = {
	endCallback: "(function|null)",
	leftCallback: "(function|null)",
	rightCallback: "(function|null)"
};
var Swipe = class Swipe extends Config {
	constructor(element, config) {
		super();
		this._element = element;
		if (!element || !Swipe.isSupported()) return;
		this._config = this._getConfig(config);
		this._deltaX = 0;
		this._supportPointerEvents = Boolean(window.PointerEvent);
		this._initEvents();
	}
	static get Default() {
		return Default$17;
	}
	static get DefaultType() {
		return DefaultType$17;
	}
	static get NAME() {
		return NAME$18;
	}
	dispose() {
		EventHandler.off(this._element, EVENT_KEY$12);
	}
	_start(event) {
		if (!this._supportPointerEvents) {
			this._deltaX = event.touches[0].clientX;
			return;
		}
		if (this._eventIsPointerPenTouch(event)) this._deltaX = event.clientX;
	}
	_end(event) {
		if (this._eventIsPointerPenTouch(event)) this._deltaX = event.clientX - this._deltaX;
		this._handleSwipe();
		execute(this._config.endCallback);
	}
	_move(event) {
		this._deltaX = event.touches && event.touches.length > 1 ? 0 : event.touches[0].clientX - this._deltaX;
	}
	_handleSwipe() {
		const absDeltaX = Math.abs(this._deltaX);
		if (absDeltaX <= SWIPE_THRESHOLD) return;
		const direction = absDeltaX / this._deltaX;
		this._deltaX = 0;
		if (!direction) return;
		execute(direction > 0 ? this._config.rightCallback : this._config.leftCallback);
	}
	_initEvents() {
		if (this._supportPointerEvents) {
			EventHandler.on(this._element, EVENT_POINTERDOWN, (event) => this._start(event));
			EventHandler.on(this._element, EVENT_POINTERUP, (event) => this._end(event));
			this._element.classList.add(CLASS_NAME_POINTER_EVENT);
		} else {
			EventHandler.on(this._element, EVENT_TOUCHSTART, (event) => this._start(event));
			EventHandler.on(this._element, EVENT_TOUCHMOVE, (event) => this._move(event));
			EventHandler.on(this._element, EVENT_TOUCHEND, (event) => this._end(event));
		}
	}
	_eventIsPointerPenTouch(event) {
		return this._supportPointerEvents && (event.pointerType === POINTER_TYPE_PEN || event.pointerType === POINTER_TYPE_TOUCH);
	}
	static isSupported() {
		return "ontouchstart" in document.documentElement || navigator.maxTouchPoints > 0;
	}
};
//#endregion
//#region js/src/bootstrap/carousel.ts
/**
* --------------------------------------------------------------------------
* Bootstrap carousel.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$17 = "carousel";
var EVENT_KEY$11 = `.bs.carousel`;
var DATA_API_KEY$5 = ".data-api";
var ARROW_LEFT_KEY$1 = "ArrowLeft";
var ARROW_RIGHT_KEY$1 = "ArrowRight";
var TOUCHEVENT_COMPAT_WAIT = 500;
var ORDER_NEXT = "next";
var ORDER_PREV = "prev";
var DIRECTION_LEFT = "left";
var DIRECTION_RIGHT = "right";
var EVENT_SLIDE = `slide${EVENT_KEY$11}`;
var EVENT_SLID = `slid${EVENT_KEY$11}`;
var EVENT_KEYDOWN$1 = `keydown${EVENT_KEY$11}`;
var EVENT_MOUSEENTER$1 = `mouseenter${EVENT_KEY$11}`;
var EVENT_MOUSELEAVE$1 = `mouseleave${EVENT_KEY$11}`;
var EVENT_DRAG_START = `dragstart${EVENT_KEY$11}`;
var EVENT_LOAD_DATA_API$3 = `load${EVENT_KEY$11}${DATA_API_KEY$5}`;
var EVENT_CLICK_DATA_API$5 = `click${EVENT_KEY$11}${DATA_API_KEY$5}`;
var CLASS_NAME_CAROUSEL = "carousel";
var CLASS_NAME_ACTIVE$4 = "active";
var CLASS_NAME_SLIDE = "slide";
var CLASS_NAME_END = "carousel-item-end";
var CLASS_NAME_START = "carousel-item-start";
var CLASS_NAME_NEXT = "carousel-item-next";
var CLASS_NAME_PREV = "carousel-item-prev";
var SELECTOR_ACTIVE = ".active";
var SELECTOR_ITEM = ".carousel-item";
var SELECTOR_ACTIVE_ITEM = ".active.carousel-item";
var SELECTOR_ITEM_IMG = ".carousel-item img";
var SELECTOR_INDICATORS = ".carousel-indicators";
var SELECTOR_DATA_SLIDE = "[data-bs-slide], [data-bs-slide-to], [data-tblr-slide], [data-tblr-slide-to]";
var SELECTOR_DATA_RIDE = "[data-bs-ride=\"carousel\"], [data-tblr-ride=\"carousel\"]";
var KEY_TO_DIRECTION = {
	[ARROW_LEFT_KEY$1]: DIRECTION_RIGHT,
	[ARROW_RIGHT_KEY$1]: DIRECTION_LEFT
};
var Default$16 = {
	interval: 5e3,
	keyboard: true,
	pause: "hover",
	ride: false,
	touch: true,
	wrap: true
};
var DefaultType$16 = {
	interval: "(number|boolean)",
	keyboard: "boolean",
	pause: "(string|boolean)",
	ride: "(boolean|string)",
	touch: "boolean",
	wrap: "boolean"
};
var Carousel = class Carousel extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._interval = null;
		this._activeElement = null;
		this._isSliding = false;
		this.touchTimeout = null;
		this._swipeHelper = null;
		this._indicatorsElement = SelectorEngine.findOne(SELECTOR_INDICATORS, this._element);
		this._addEventListeners();
		if (this._config.ride === CLASS_NAME_CAROUSEL) this.cycle();
	}
	static get Default() {
		return Default$16;
	}
	static get DefaultType() {
		return DefaultType$16;
	}
	static get NAME() {
		return NAME$17;
	}
	next() {
		this._slide(ORDER_NEXT);
	}
	nextWhenVisible() {
		if (!document.hidden && isVisible(this._element)) this.next();
	}
	prev() {
		this._slide(ORDER_PREV);
	}
	pause() {
		if (this._isSliding) triggerTransitionEnd(this._element);
		this._clearInterval();
	}
	cycle() {
		this._clearInterval();
		this._updateInterval();
		this._interval = setInterval(() => this.nextWhenVisible(), this._config.interval);
	}
	_maybeEnableCycle() {
		if (!this._config.ride) return;
		if (this._isSliding) {
			EventHandler.one(this._element, EVENT_SLID, () => this.cycle());
			return;
		}
		this.cycle();
	}
	to(index) {
		const items = this._getItems();
		if (index > items.length - 1 || index < 0) return;
		if (this._isSliding) {
			EventHandler.one(this._element, EVENT_SLID, () => this.to(index));
			return;
		}
		const activeIndex = this._getItemIndex(this._getActive());
		if (activeIndex === index) return;
		const order = index > activeIndex ? ORDER_NEXT : ORDER_PREV;
		this._slide(order, items[index]);
	}
	dispose() {
		if (this._swipeHelper) this._swipeHelper.dispose();
		super.dispose();
	}
	_configAfterMerge(config) {
		config.defaultInterval = config.interval;
		return config;
	}
	_addEventListeners() {
		if (this._config.keyboard) EventHandler.on(this._element, EVENT_KEYDOWN$1, (event) => this._keydown(event));
		if (this._config.pause === "hover") {
			EventHandler.on(this._element, EVENT_MOUSEENTER$1, () => this.pause());
			EventHandler.on(this._element, EVENT_MOUSELEAVE$1, () => this._maybeEnableCycle());
		}
		if (this._config.touch && Swipe.isSupported()) this._addTouchEventListeners();
	}
	_addTouchEventListeners() {
		for (const img of SelectorEngine.find(SELECTOR_ITEM_IMG, this._element)) EventHandler.on(img, EVENT_DRAG_START, (event) => event.preventDefault());
		const endCallBack = () => {
			if (this._config.pause !== "hover") return;
			this.pause();
			if (this.touchTimeout) clearTimeout(this.touchTimeout);
			this.touchTimeout = setTimeout(() => this._maybeEnableCycle(), TOUCHEVENT_COMPAT_WAIT + this._config.interval);
		};
		const swipeConfig = {
			leftCallback: () => this._slide(this._directionToOrder(DIRECTION_LEFT)),
			rightCallback: () => this._slide(this._directionToOrder(DIRECTION_RIGHT)),
			endCallback: endCallBack
		};
		this._swipeHelper = new Swipe(this._element, swipeConfig);
	}
	_keydown(event) {
		if (/input|textarea/i.test(event.target.tagName)) return;
		const direction = KEY_TO_DIRECTION[event.key];
		if (direction) {
			event.preventDefault();
			this._slide(this._directionToOrder(direction));
		}
	}
	_getItemIndex(element) {
		return this._getItems().indexOf(element);
	}
	_setActiveIndicatorElement(index) {
		if (!this._indicatorsElement) return;
		const activeIndicator = SelectorEngine.findOne(SELECTOR_ACTIVE, this._indicatorsElement);
		activeIndicator.classList.remove(CLASS_NAME_ACTIVE$4);
		activeIndicator.removeAttribute("aria-current");
		const newActiveIndicator = SelectorEngine.findOne(`[data-bs-slide-to="${index}"], [data-tblr-slide-to="${index}"]`, this._indicatorsElement);
		if (newActiveIndicator) {
			newActiveIndicator.classList.add(CLASS_NAME_ACTIVE$4);
			newActiveIndicator.setAttribute("aria-current", "true");
		}
	}
	_updateInterval() {
		var _this$_config$default;
		const element = this._activeElement || this._getActive();
		if (!element) return;
		const elementInterval = Number.parseInt(element.getAttribute("data-bs-interval") || element.getAttribute("data-tblr-interval") || "", 10);
		this._config.interval = elementInterval || ((_this$_config$default = this._config.defaultInterval) !== null && _this$_config$default !== void 0 ? _this$_config$default : this._config.interval);
	}
	_slide(order, element = null) {
		if (this._isSliding) return;
		const activeElement = this._getActive();
		const isNext = order === ORDER_NEXT;
		const nextElement = element || getNextActiveElement(this._getItems(), activeElement, isNext, this._config.wrap);
		if (nextElement === activeElement) return;
		const nextElementIndex = this._getItemIndex(nextElement);
		const triggerEvent = (eventName) => {
			return EventHandler.trigger(this._element, eventName, {
				relatedTarget: nextElement,
				direction: this._orderToDirection(order),
				from: this._getItemIndex(activeElement),
				to: nextElementIndex
			});
		};
		const slideEvent = triggerEvent(EVENT_SLIDE);
		if (slideEvent === null || slideEvent === void 0 ? void 0 : slideEvent.defaultPrevented) return;
		if (!activeElement || !nextElement) return;
		const isCycling = Boolean(this._interval);
		this.pause();
		this._isSliding = true;
		this._setActiveIndicatorElement(nextElementIndex);
		this._activeElement = nextElement;
		const directionalClassName = isNext ? CLASS_NAME_START : CLASS_NAME_END;
		const orderClassName = isNext ? CLASS_NAME_NEXT : CLASS_NAME_PREV;
		nextElement.classList.add(orderClassName);
		reflow(nextElement);
		activeElement.classList.add(directionalClassName);
		nextElement.classList.add(directionalClassName);
		const completeCallBack = () => {
			nextElement.classList.remove(directionalClassName, orderClassName);
			nextElement.classList.add(CLASS_NAME_ACTIVE$4);
			activeElement.classList.remove(CLASS_NAME_ACTIVE$4, orderClassName, directionalClassName);
			this._isSliding = false;
			triggerEvent(EVENT_SLID);
		};
		this._queueCallback(completeCallBack, activeElement, this._isAnimated());
		if (isCycling) this.cycle();
	}
	_isAnimated() {
		return this._element.classList.contains(CLASS_NAME_SLIDE);
	}
	_getActive() {
		return SelectorEngine.findOne(SELECTOR_ACTIVE_ITEM, this._element);
	}
	_getItems() {
		return SelectorEngine.find(SELECTOR_ITEM, this._element);
	}
	_clearInterval() {
		if (this._interval) {
			clearInterval(this._interval);
			this._interval = null;
		}
	}
	_directionToOrder(direction) {
		if (isRTL()) return direction === DIRECTION_LEFT ? ORDER_PREV : ORDER_NEXT;
		return direction === DIRECTION_LEFT ? ORDER_NEXT : ORDER_PREV;
	}
	_orderToDirection(order) {
		if (isRTL()) return order === ORDER_PREV ? DIRECTION_LEFT : DIRECTION_RIGHT;
		return order === ORDER_PREV ? DIRECTION_RIGHT : DIRECTION_LEFT;
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Carousel.getOrCreateInstance(this, config);
			if (typeof config === "number") {
				data.to(config);
				return;
			}
			if (typeof config === "string") {
				if (data[config] === void 0 || config.startsWith("_") || config === "constructor") throw new TypeError(`No method named "${config}"`);
				data[config]();
			}
		});
	}
};
EventHandler.on(document, EVENT_CLICK_DATA_API$5, SELECTOR_DATA_SLIDE, function(event) {
	const target = SelectorEngine.getElementFromSelector(this);
	if (!target || !target.classList.contains(CLASS_NAME_CAROUSEL)) return;
	event.preventDefault();
	const carousel = Carousel.getOrCreateInstance(target);
	const slideIndex = this.getAttribute("data-bs-slide-to") || this.getAttribute("data-tblr-slide-to");
	if (slideIndex) {
		carousel.to(Number(slideIndex));
		carousel._maybeEnableCycle();
		return;
	}
	if (Manipulator.getDataAttribute(this, "slide") === "next") {
		carousel.next();
		carousel._maybeEnableCycle();
		return;
	}
	carousel.prev();
	carousel._maybeEnableCycle();
});
EventHandler.on(window, EVENT_LOAD_DATA_API$3, () => {
	const carousels = SelectorEngine.find(SELECTOR_DATA_RIDE);
	for (const carousel of carousels) Carousel.getOrCreateInstance(carousel);
});
defineJQueryPlugin(Carousel);
//#endregion
//#region js/src/bootstrap/collapse.ts
/**
* --------------------------------------------------------------------------
* Bootstrap collapse.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$16 = "collapse";
var EVENT_KEY$10 = `.bs.collapse`;
var DATA_API_KEY$4 = ".data-api";
var EVENT_SHOW$6 = `show${EVENT_KEY$10}`;
var EVENT_SHOWN$6 = `shown${EVENT_KEY$10}`;
var EVENT_HIDE$6 = `hide${EVENT_KEY$10}`;
var EVENT_HIDDEN$6 = `hidden${EVENT_KEY$10}`;
var EVENT_CLICK_DATA_API$4 = `click${EVENT_KEY$10}${DATA_API_KEY$4}`;
var CLASS_NAME_SHOW$7 = "show";
var CLASS_NAME_COLLAPSE = "collapse";
var CLASS_NAME_COLLAPSING = "collapsing";
var CLASS_NAME_COLLAPSED = "collapsed";
var CLASS_NAME_DEEPER_CHILDREN = `:scope .${CLASS_NAME_COLLAPSE} .${CLASS_NAME_COLLAPSE}`;
var CLASS_NAME_HORIZONTAL = "collapse-horizontal";
var WIDTH = "width";
var HEIGHT = "height";
var SELECTOR_ACTIVES = ".collapse.show, .collapse.collapsing";
var SELECTOR_DATA_TOGGLE$7 = "[data-bs-toggle=\"collapse\"], [data-tblr-toggle=\"collapse\"]";
var Default$15 = {
	parent: null,
	toggle: true
};
var DefaultType$15 = {
	parent: "(null|element)",
	toggle: "boolean"
};
var Collapse = class Collapse extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._isTransitioning = false;
		this._triggerArray = [];
		const toggleList = SelectorEngine.find(SELECTOR_DATA_TOGGLE$7);
		for (const elem of toggleList) {
			const selector = SelectorEngine.getSelectorFromElement(elem);
			const filterElement = SelectorEngine.find(selector).filter((foundElement) => foundElement === this._element);
			if (selector !== null && filterElement.length) this._triggerArray.push(elem);
		}
		this._initializeChildren();
		if (!this._config.parent) this._addAriaAndCollapsedClass(this._triggerArray, this._isShown());
		if (this._config.toggle) this.toggle();
	}
	static get Default() {
		return Default$15;
	}
	static get DefaultType() {
		return DefaultType$15;
	}
	static get NAME() {
		return NAME$16;
	}
	toggle() {
		if (this._isShown()) this.hide();
		else this.show();
	}
	show() {
		if (this._isTransitioning || this._isShown()) return;
		let activeChildren = [];
		if (this._config.parent) activeChildren = this._getFirstLevelChildren(SELECTOR_ACTIVES).filter((element) => element !== this._element).map((element) => Collapse.getOrCreateInstance(element, { toggle: false }));
		if (activeChildren.length && activeChildren[0]._isTransitioning) return;
		const startEvent = EventHandler.trigger(this._element, EVENT_SHOW$6);
		if (startEvent === null || startEvent === void 0 ? void 0 : startEvent.defaultPrevented) return;
		for (const activeInstance of activeChildren) activeInstance.hide();
		const dimension = this._getDimension();
		this._element.classList.remove(CLASS_NAME_COLLAPSE);
		this._element.classList.add(CLASS_NAME_COLLAPSING);
		this._element.style[dimension] = "0";
		this._addAriaAndCollapsedClass(this._triggerArray, true);
		this._isTransitioning = true;
		const complete = () => {
			this._isTransitioning = false;
			this._element.classList.remove(CLASS_NAME_COLLAPSING);
			this._element.classList.add(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$7);
			this._element.style[dimension] = "";
			EventHandler.trigger(this._element, EVENT_SHOWN$6);
		};
		const scrollSize = `scroll${dimension[0].toUpperCase() + dimension.slice(1)}`;
		this._queueCallback(complete, this._element, true);
		this._element.style[dimension] = `${this._element[scrollSize]}px`;
	}
	hide() {
		if (this._isTransitioning || !this._isShown()) return;
		const startEvent = EventHandler.trigger(this._element, EVENT_HIDE$6);
		if (startEvent === null || startEvent === void 0 ? void 0 : startEvent.defaultPrevented) return;
		const dimension = this._getDimension();
		this._element.style[dimension] = `${this._element.getBoundingClientRect()[dimension]}px`;
		reflow(this._element);
		this._element.classList.add(CLASS_NAME_COLLAPSING);
		this._element.classList.remove(CLASS_NAME_COLLAPSE, CLASS_NAME_SHOW$7);
		for (const trigger of this._triggerArray) {
			const element = SelectorEngine.getElementFromSelector(trigger);
			if (element && !this._isShown(element)) this._addAriaAndCollapsedClass([trigger], false);
		}
		this._isTransitioning = true;
		const complete = () => {
			this._isTransitioning = false;
			this._element.classList.remove(CLASS_NAME_COLLAPSING);
			this._element.classList.add(CLASS_NAME_COLLAPSE);
			EventHandler.trigger(this._element, EVENT_HIDDEN$6);
		};
		this._element.style[dimension] = "";
		this._queueCallback(complete, this._element, true);
	}
	_isShown(element = this._element) {
		return element.classList.contains(CLASS_NAME_SHOW$7);
	}
	_configAfterMerge(config) {
		config.toggle = Boolean(config.toggle);
		config.parent = getElement(config.parent);
		return config;
	}
	_getDimension() {
		return this._element.classList.contains(CLASS_NAME_HORIZONTAL) ? WIDTH : HEIGHT;
	}
	_initializeChildren() {
		if (!this._config.parent) return;
		const children = this._getFirstLevelChildren(SELECTOR_DATA_TOGGLE$7);
		for (const element of children) {
			const selected = SelectorEngine.getElementFromSelector(element);
			if (selected) this._addAriaAndCollapsedClass([element], this._isShown(selected));
		}
	}
	_getFirstLevelChildren(selector) {
		const children = SelectorEngine.find(CLASS_NAME_DEEPER_CHILDREN, this._config.parent);
		return SelectorEngine.find(selector, this._config.parent).filter((element) => !children.includes(element));
	}
	_addAriaAndCollapsedClass(triggerArray, isOpen) {
		if (!triggerArray.length) return;
		for (const element of triggerArray) {
			element.classList.toggle(CLASS_NAME_COLLAPSED, !isOpen);
			element.setAttribute("aria-expanded", String(isOpen));
		}
	}
	static jQueryInterface(config) {
		const _config = {};
		if (typeof config === "string" && /show|hide/.test(config)) _config.toggle = false;
		return this.each(function() {
			const data = Collapse.getOrCreateInstance(this, _config);
			if (typeof config === "string") {
				if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
				data[config]();
			}
		});
	}
};
EventHandler.on(document, EVENT_CLICK_DATA_API$4, SELECTOR_DATA_TOGGLE$7, function(event) {
	const { delegateTarget } = event;
	if (event.target.tagName === "A" || delegateTarget && delegateTarget.tagName === "A") event.preventDefault();
	for (const element of SelectorEngine.getMultipleElementsFromSelector(this)) Collapse.getOrCreateInstance(element, { toggle: false }).toggle();
});
defineJQueryPlugin(Collapse);
//#endregion
//#region js/src/bootstrap/dropdown.ts
/**
* --------------------------------------------------------------------------
* Bootstrap dropdown.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$15 = "dropdown";
var EVENT_KEY$9 = `.bs.dropdown`;
var DATA_API_KEY$3 = ".data-api";
var ESCAPE_KEY$2 = "Escape";
var TAB_KEY$1 = "Tab";
var ARROW_UP_KEY$1 = "ArrowUp";
var ARROW_DOWN_KEY$1 = "ArrowDown";
var RIGHT_MOUSE_BUTTON = 2;
var EVENT_HIDE$5 = `hide${EVENT_KEY$9}`;
var EVENT_HIDDEN$5 = `hidden${EVENT_KEY$9}`;
var EVENT_SHOW$5 = `show${EVENT_KEY$9}`;
var EVENT_SHOWN$5 = `shown${EVENT_KEY$9}`;
var EVENT_CLICK_DATA_API$3 = `click${EVENT_KEY$9}${DATA_API_KEY$3}`;
var EVENT_KEYDOWN_DATA_API = `keydown${EVENT_KEY$9}${DATA_API_KEY$3}`;
var EVENT_KEYUP_DATA_API = `keyup${EVENT_KEY$9}${DATA_API_KEY$3}`;
var CLASS_NAME_SHOW$6 = "show";
var CLASS_NAME_DROPUP = "dropup";
var CLASS_NAME_DROPEND = "dropend";
var CLASS_NAME_DROPSTART = "dropstart";
var CLASS_NAME_DROPUP_CENTER = "dropup-center";
var CLASS_NAME_DROPDOWN_CENTER = "dropdown-center";
var SELECTOR_DATA_TOGGLE$6 = "[data-bs-toggle=\"dropdown\"]:not(.disabled):not(:disabled), [data-tblr-toggle=\"dropdown\"]:not(.disabled):not(:disabled)";
var SELECTOR_DATA_TOGGLE_SHOWN = SELECTOR_DATA_TOGGLE$6.split(", ").map((selector) => `${selector}.${CLASS_NAME_SHOW$6}`).join(", ");
var SELECTOR_MENU = ".dropdown-menu";
var SELECTOR_NAVBAR = ".navbar";
var SELECTOR_NAVBAR_NAV = ".navbar-nav";
var SELECTOR_VISIBLE_ITEMS = ".dropdown-menu .dropdown-item:not(.disabled):not(:disabled)";
var PLACEMENT_TOP = isRTL() ? "top-end" : "top-start";
var PLACEMENT_TOPEND = isRTL() ? "top-start" : "top-end";
var PLACEMENT_BOTTOM = isRTL() ? "bottom-end" : "bottom-start";
var PLACEMENT_BOTTOMEND = isRTL() ? "bottom-start" : "bottom-end";
var PLACEMENT_RIGHT = isRTL() ? "left-start" : "right-start";
var PLACEMENT_LEFT = isRTL() ? "right-start" : "left-start";
var PLACEMENT_TOPCENTER = "top";
var PLACEMENT_BOTTOMCENTER = "bottom";
var Default$14 = {
	autoClose: true,
	boundary: "clippingParents",
	display: "dynamic",
	offset: [0, 2],
	popperConfig: null,
	reference: "toggle"
};
var DefaultType$14 = {
	autoClose: "(boolean|string)",
	boundary: "(string|element)",
	display: "string",
	offset: "(array|string|function)",
	popperConfig: "(null|object|function)",
	reference: "(string|element|object)"
};
var Dropdown = class Dropdown extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._popper = null;
		this._parent = this._element.parentNode;
		this._menu = SelectorEngine.next(this._element, SELECTOR_MENU)[0] || SelectorEngine.prev(this._element, SELECTOR_MENU)[0] || SelectorEngine.findOne(SELECTOR_MENU, this._parent);
		this._inNavbar = this._detectNavbar();
	}
	static get Default() {
		return Default$14;
	}
	static get DefaultType() {
		return DefaultType$14;
	}
	static get NAME() {
		return NAME$15;
	}
	toggle() {
		this._isShown() ? this.hide() : this.show();
	}
	show() {
		if (isDisabled(this._element) || this._isShown()) return;
		const relatedTarget = { relatedTarget: this._element };
		const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$5, relatedTarget);
		if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
		this._createPopper();
		if ("ontouchstart" in document.documentElement && !this._parent.closest(SELECTOR_NAVBAR_NAV)) for (const element of Array.from(document.body.children)) EventHandler.on(element, "mouseover", noop);
		this._element.focus();
		this._element.setAttribute("aria-expanded", "true");
		this._menu.classList.add(CLASS_NAME_SHOW$6);
		this._element.classList.add(CLASS_NAME_SHOW$6);
		EventHandler.trigger(this._element, EVENT_SHOWN$5, relatedTarget);
	}
	hide() {
		if (isDisabled(this._element) || !this._isShown()) return;
		const relatedTarget = { relatedTarget: this._element };
		this._completeHide(relatedTarget);
	}
	dispose() {
		if (this._popper) this._popper.destroy();
		super.dispose();
	}
	update() {
		this._inNavbar = this._detectNavbar();
		if (this._popper) this._popper.update();
	}
	_completeHide(relatedTarget) {
		const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$5, relatedTarget);
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		if ("ontouchstart" in document.documentElement) for (const element of Array.from(document.body.children)) EventHandler.off(element, "mouseover", noop);
		if (this._popper) this._popper.destroy();
		this._menu.classList.remove(CLASS_NAME_SHOW$6);
		this._element.classList.remove(CLASS_NAME_SHOW$6);
		this._element.setAttribute("aria-expanded", "false");
		Manipulator.removeDataAttribute(this._menu, "popper");
		EventHandler.trigger(this._element, EVENT_HIDDEN$5, relatedTarget);
	}
	_getConfig(config) {
		const merged = super._getConfig(config);
		if (typeof merged.reference === "object" && !isElement$1(merged.reference) && typeof merged.reference.getBoundingClientRect !== "function") throw new TypeError(`${NAME$15.toUpperCase()}: Option "reference" provided type "object" without a required "getBoundingClientRect" method.`);
		return merged;
	}
	_createPopper() {
		if (typeof lib_exports === "undefined") throw new TypeError("Bootstrap's dropdowns require Popper (https://popper.js.org/docs/v2/)");
		let referenceElement = this._element;
		if (this._config.reference === "parent") referenceElement = this._parent;
		else if (isElement$1(this._config.reference)) referenceElement = getElement(this._config.reference);
		else if (typeof this._config.reference === "object") referenceElement = this._config.reference;
		const popperConfig = this._getPopperConfig();
		this._popper = createPopper(referenceElement, this._menu, popperConfig);
	}
	_isShown() {
		return this._menu.classList.contains(CLASS_NAME_SHOW$6);
	}
	_getPlacement() {
		const parentDropdown = this._parent;
		if (parentDropdown.classList.contains(CLASS_NAME_DROPEND)) return PLACEMENT_RIGHT;
		if (parentDropdown.classList.contains(CLASS_NAME_DROPSTART)) return PLACEMENT_LEFT;
		if (parentDropdown.classList.contains(CLASS_NAME_DROPUP_CENTER)) return PLACEMENT_TOPCENTER;
		if (parentDropdown.classList.contains(CLASS_NAME_DROPDOWN_CENTER)) return PLACEMENT_BOTTOMCENTER;
		const isEnd = getComputedStyle(this._menu).getPropertyValue("--bs-position").trim() === "end";
		if (parentDropdown.classList.contains(CLASS_NAME_DROPUP)) return isEnd ? PLACEMENT_TOPEND : PLACEMENT_TOP;
		return isEnd ? PLACEMENT_BOTTOMEND : PLACEMENT_BOTTOM;
	}
	_detectNavbar() {
		return this._element.closest(SELECTOR_NAVBAR) !== null;
	}
	_getOffset() {
		const { offset } = this._config;
		if (typeof offset === "string") return offset.split(",").map((value) => Number.parseInt(value, 10));
		if (typeof offset === "function") return (popperData) => offset(popperData, this._element);
		return offset;
	}
	_getPopperConfig() {
		const defaultBsPopperConfig = {
			placement: this._getPlacement(),
			modifiers: [{
				name: "preventOverflow",
				options: { boundary: this._config.boundary }
			}, {
				name: "offset",
				options: { offset: this._getOffset() }
			}]
		};
		if (this._inNavbar || this._config.display === "static") {
			Manipulator.setDataAttribute(this._menu, "popper", "static");
			defaultBsPopperConfig.modifiers = [{
				name: "applyStyles",
				enabled: false
			}];
		}
		const popperConfig = execute(this._config.popperConfig, [void 0, defaultBsPopperConfig]);
		return _objectSpread2(_objectSpread2({}, defaultBsPopperConfig), typeof popperConfig === "object" && popperConfig !== null ? popperConfig : {});
	}
	_selectMenuItem({ key, target }) {
		const items = SelectorEngine.find(SELECTOR_VISIBLE_ITEMS, this._menu).filter((element) => isVisible(element));
		if (!items.length) return;
		const activeElement = target;
		getNextActiveElement(items, activeElement, key === ARROW_DOWN_KEY$1, !items.includes(activeElement)).focus();
	}
	static clearMenus(event) {
		if (event.button === RIGHT_MOUSE_BUTTON || event.type === "keyup" && event.key !== TAB_KEY$1) return;
		const openToggles = SelectorEngine.find(SELECTOR_DATA_TOGGLE_SHOWN);
		for (const toggle of openToggles) {
			const context = Dropdown.getInstance(toggle);
			if (!context || context._config.autoClose === false) continue;
			const composedPath = event.composedPath();
			const isMenuTarget = composedPath.includes(context._menu);
			if (composedPath.includes(context._element) || context._config.autoClose === "inside" && !isMenuTarget || context._config.autoClose === "outside" && isMenuTarget) continue;
			if (context._menu.contains(event.target) && (event.type === "keyup" && event.key === TAB_KEY$1 || /input|select|option|textarea|form/i.test(event.target.tagName))) continue;
			const relatedTarget = { relatedTarget: context._element };
			if (event.type === "click") relatedTarget.clickEvent = event;
			context._completeHide(relatedTarget);
		}
	}
	static dataApiKeydownHandler(event) {
		const isInput = /input|textarea/i.test(event.target.tagName);
		const isEscapeEvent = event.key === ESCAPE_KEY$2;
		const isUpOrDownEvent = [ARROW_UP_KEY$1, ARROW_DOWN_KEY$1].includes(event.key);
		if (!isUpOrDownEvent && !isEscapeEvent) return;
		if (isInput && !isEscapeEvent) return;
		event.preventDefault();
		const getToggleButton = this.matches(SELECTOR_DATA_TOGGLE$6) ? this : SelectorEngine.prev(this, SELECTOR_DATA_TOGGLE$6)[0] || SelectorEngine.next(this, SELECTOR_DATA_TOGGLE$6)[0] || SelectorEngine.findOne(SELECTOR_DATA_TOGGLE$6, event.delegateTarget.parentNode);
		const instance = Dropdown.getOrCreateInstance(getToggleButton);
		if (isUpOrDownEvent) {
			event.stopPropagation();
			instance.show();
			instance._selectMenuItem(event);
			return;
		}
		if (instance._isShown()) {
			event.stopPropagation();
			instance.hide();
			getToggleButton.focus();
		}
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Dropdown.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
			data[config]();
		});
	}
};
EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_DATA_TOGGLE$6, Dropdown.dataApiKeydownHandler);
EventHandler.on(document, EVENT_KEYDOWN_DATA_API, SELECTOR_MENU, Dropdown.dataApiKeydownHandler);
EventHandler.on(document, EVENT_CLICK_DATA_API$3, Dropdown.clearMenus);
EventHandler.on(document, EVENT_KEYUP_DATA_API, Dropdown.clearMenus);
EventHandler.on(document, EVENT_CLICK_DATA_API$3, SELECTOR_DATA_TOGGLE$6, function(event) {
	event.preventDefault();
	Dropdown.getOrCreateInstance(this).toggle();
});
defineJQueryPlugin(Dropdown);
//#endregion
//#region js/src/bootstrap/util/backdrop.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/backdrop.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$14 = "backdrop";
var CLASS_NAME_FADE$4 = "fade";
var CLASS_NAME_SHOW$5 = "show";
var EVENT_MOUSEDOWN = `mousedown.bs.${NAME$14}`;
var Default$13 = {
	className: "modal-backdrop",
	clickCallback: null,
	isAnimated: false,
	isVisible: true,
	rootElement: "body"
};
var DefaultType$13 = {
	className: "string",
	clickCallback: "(function|null)",
	isAnimated: "boolean",
	isVisible: "boolean",
	rootElement: "(element|string)"
};
var Backdrop = class extends Config {
	constructor(config) {
		super();
		this._config = this._getConfig(config);
		this._isAppended = false;
		this._element = null;
	}
	static get Default() {
		return Default$13;
	}
	static get DefaultType() {
		return DefaultType$13;
	}
	static get NAME() {
		return NAME$14;
	}
	show(callback) {
		if (!this._config.isVisible) {
			execute(callback);
			return;
		}
		this._append();
		const element = this._getElement();
		if (this._config.isAnimated) reflow(element);
		element.classList.add(CLASS_NAME_SHOW$5);
		this._emulateAnimation(() => {
			execute(callback);
		});
	}
	hide(callback) {
		if (!this._config.isVisible) {
			execute(callback);
			return;
		}
		this._getElement().classList.remove(CLASS_NAME_SHOW$5);
		this._emulateAnimation(() => {
			this.dispose();
			execute(callback);
		});
	}
	dispose() {
		if (!this._isAppended) return;
		EventHandler.off(this._element, EVENT_MOUSEDOWN);
		this._element.remove();
		this._isAppended = false;
	}
	_getElement() {
		if (!this._element) {
			const backdrop = document.createElement("div");
			backdrop.className = this._config.className;
			if (this._config.isAnimated) backdrop.classList.add(CLASS_NAME_FADE$4);
			this._element = backdrop;
		}
		return this._element;
	}
	_configAfterMerge(config) {
		config.rootElement = getElement(config.rootElement);
		return config;
	}
	_append() {
		if (this._isAppended) return;
		const element = this._getElement();
		this._config.rootElement.append(element);
		EventHandler.on(element, EVENT_MOUSEDOWN, () => {
			execute(this._config.clickCallback);
		});
		this._isAppended = true;
	}
	_emulateAnimation(callback) {
		executeAfterTransition(callback, this._getElement(), this._config.isAnimated);
	}
};
//#endregion
//#region js/src/bootstrap/util/focustrap.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/focustrap.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$13 = "focustrap";
var EVENT_KEY$8 = `.bs.focustrap`;
var EVENT_FOCUSIN$2 = `focusin${EVENT_KEY$8}`;
var EVENT_KEYDOWN_TAB = `keydown.tab${EVENT_KEY$8}`;
var TAB_KEY = "Tab";
var TAB_NAV_FORWARD = "forward";
var TAB_NAV_BACKWARD = "backward";
var Default$12 = {
	autofocus: true,
	trapElement: null
};
var DefaultType$12 = {
	autofocus: "boolean",
	trapElement: "element"
};
var FocusTrap = class extends Config {
	constructor(config) {
		super();
		this._config = this._getConfig(config);
		this._isActive = false;
		this._lastTabNavDirection = null;
	}
	static get Default() {
		return Default$12;
	}
	static get DefaultType() {
		return DefaultType$12;
	}
	static get NAME() {
		return NAME$13;
	}
	activate() {
		if (this._isActive) return;
		if (this._config.autofocus) this._config.trapElement.focus();
		EventHandler.off(document, EVENT_KEY$8);
		EventHandler.on(document, EVENT_FOCUSIN$2, (event) => this._handleFocusin(event));
		EventHandler.on(document, EVENT_KEYDOWN_TAB, (event) => this._handleKeydown(event));
		this._isActive = true;
	}
	deactivate() {
		if (!this._isActive) return;
		this._isActive = false;
		EventHandler.off(document, EVENT_KEY$8);
	}
	_handleFocusin(event) {
		const { trapElement } = this._config;
		if (!trapElement || event.target === document || event.target === trapElement || trapElement.contains(event.target)) return;
		const elements = SelectorEngine.focusableChildren(trapElement);
		if (elements.length === 0) trapElement.focus();
		else if (this._lastTabNavDirection === TAB_NAV_BACKWARD) elements[elements.length - 1].focus();
		else elements[0].focus();
	}
	_handleKeydown(event) {
		if (event.key !== TAB_KEY) return;
		this._lastTabNavDirection = event.shiftKey ? TAB_NAV_BACKWARD : TAB_NAV_FORWARD;
	}
};
//#endregion
//#region js/src/bootstrap/util/scrollbar.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/scrollbar.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var SELECTOR_FIXED_CONTENT = ".fixed-top, .fixed-bottom, .is-fixed, .sticky-top";
var SELECTOR_STICKY_CONTENT = ".sticky-top";
var PROPERTY_PADDING = "padding-right";
var PROPERTY_MARGIN = "margin-right";
var ScrollBarHelper = class {
	constructor() {
		this._element = document.body;
	}
	getWidth() {
		const documentWidth = document.documentElement.clientWidth;
		return Math.abs(window.innerWidth - documentWidth);
	}
	hide() {
		const width = this.getWidth();
		this._disableOverFlow();
		this._setElementAttributes(this._element, PROPERTY_PADDING, (calculatedValue) => calculatedValue + width);
		this._setElementAttributes(SELECTOR_FIXED_CONTENT, PROPERTY_PADDING, (calculatedValue) => calculatedValue + width);
		this._setElementAttributes(SELECTOR_STICKY_CONTENT, PROPERTY_MARGIN, (calculatedValue) => calculatedValue - width);
	}
	reset() {
		this._resetElementAttributes(this._element, "overflow");
		this._resetElementAttributes(this._element, PROPERTY_PADDING);
		this._resetElementAttributes(SELECTOR_FIXED_CONTENT, PROPERTY_PADDING);
		this._resetElementAttributes(SELECTOR_STICKY_CONTENT, PROPERTY_MARGIN);
	}
	isOverflowing() {
		return this.getWidth() > 0;
	}
	_disableOverFlow() {
		this._saveInitialAttribute(this._element, "overflow");
		this._element.style.overflow = "hidden";
	}
	_setElementAttributes(selector, styleProperty, callback) {
		const scrollbarWidth = this.getWidth();
		const manipulationCallBack = (element) => {
			if (element !== this._element && window.innerWidth > element.clientWidth + scrollbarWidth) return;
			this._saveInitialAttribute(element, styleProperty);
			const calculatedValue = window.getComputedStyle(element).getPropertyValue(styleProperty);
			element.style.setProperty(styleProperty, `${callback(Number.parseFloat(calculatedValue))}px`);
		};
		this._applyManipulationCallback(selector, manipulationCallBack);
	}
	_saveInitialAttribute(element, styleProperty) {
		const actualValue = element.style.getPropertyValue(styleProperty);
		if (actualValue) Manipulator.setDataAttribute(element, styleProperty, actualValue);
	}
	_resetElementAttributes(selector, styleProperty) {
		const manipulationCallBack = (element) => {
			const value = Manipulator.getDataAttribute(element, styleProperty);
			if (value === null) {
				element.style.removeProperty(styleProperty);
				return;
			}
			Manipulator.removeDataAttribute(element, styleProperty);
			element.style.setProperty(styleProperty, String(value));
		};
		this._applyManipulationCallback(selector, manipulationCallBack);
	}
	_applyManipulationCallback(selector, callBack) {
		if (isElement$1(selector)) {
			callBack(selector);
			return;
		}
		for (const sel of SelectorEngine.find(selector, this._element)) callBack(sel);
	}
};
//#endregion
//#region js/src/bootstrap/modal.ts
/**
* --------------------------------------------------------------------------
* Bootstrap modal.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$12 = "modal";
var EVENT_KEY$7 = `.bs.modal`;
var DATA_API_KEY$2 = ".data-api";
var ESCAPE_KEY$1 = "Escape";
var EVENT_HIDE$4 = `hide${EVENT_KEY$7}`;
var EVENT_HIDE_PREVENTED$1 = `hidePrevented${EVENT_KEY$7}`;
var EVENT_HIDDEN$4 = `hidden${EVENT_KEY$7}`;
var EVENT_SHOW$4 = `show${EVENT_KEY$7}`;
var EVENT_SHOWN$4 = `shown${EVENT_KEY$7}`;
var EVENT_RESIZE$1 = `resize${EVENT_KEY$7}`;
var EVENT_CLICK_DISMISS = `click.dismiss${EVENT_KEY$7}`;
var EVENT_MOUSEDOWN_DISMISS = `mousedown.dismiss${EVENT_KEY$7}`;
var EVENT_KEYDOWN_DISMISS$1 = `keydown.dismiss${EVENT_KEY$7}`;
var EVENT_CLICK_DATA_API$2 = `click${EVENT_KEY$7}${DATA_API_KEY$2}`;
var CLASS_NAME_OPEN = "modal-open";
var CLASS_NAME_FADE$3 = "fade";
var CLASS_NAME_SHOW$4 = "show";
var CLASS_NAME_STATIC = "modal-static";
var OPEN_SELECTOR$1 = ".modal.show";
var SELECTOR_DIALOG = ".modal-dialog";
var SELECTOR_MODAL_BODY = ".modal-body";
var SELECTOR_DATA_TOGGLE$5 = "[data-bs-toggle=\"modal\"], [data-tblr-toggle=\"modal\"]";
var Default$11 = {
	backdrop: true,
	focus: true,
	keyboard: true
};
var DefaultType$11 = {
	backdrop: "(boolean|string)",
	focus: "boolean",
	keyboard: "boolean"
};
/**
* Class definition
*/
var Modal = class Modal extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._dialog = SelectorEngine.findOne(SELECTOR_DIALOG, this._element);
		this._backdrop = this._initializeBackDrop();
		this._focustrap = this._initializeFocusTrap();
		this._isShown = false;
		this._isTransitioning = false;
		this._scrollBar = new ScrollBarHelper();
		this._addEventListeners();
	}
	static get Default() {
		return Default$11;
	}
	static get DefaultType() {
		return DefaultType$11;
	}
	static get NAME() {
		return NAME$12;
	}
	toggle(relatedTarget) {
		return this._isShown ? this.hide() : this.show(relatedTarget);
	}
	show(relatedTarget) {
		if (this._isShown || this._isTransitioning) return;
		const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$4, { relatedTarget });
		if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
		this._isShown = true;
		this._isTransitioning = true;
		this._scrollBar.hide();
		document.body.classList.add(CLASS_NAME_OPEN);
		this._adjustDialog();
		this._backdrop.show(() => this._showElement(relatedTarget));
	}
	hide() {
		if (!this._isShown || this._isTransitioning) return;
		const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$4);
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		this._isShown = false;
		this._isTransitioning = true;
		this._focustrap.deactivate();
		this._element.classList.remove(CLASS_NAME_SHOW$4);
		this._queueCallback(() => this._hideModal(), this._element, this._isAnimated());
	}
	dispose() {
		EventHandler.off(window, EVENT_KEY$7);
		EventHandler.off(this._dialog, EVENT_KEY$7);
		this._backdrop.dispose();
		this._focustrap.deactivate();
		super.dispose();
	}
	handleUpdate() {
		this._adjustDialog();
	}
	_initializeBackDrop() {
		return new Backdrop({
			isVisible: Boolean(this._config.backdrop),
			isAnimated: this._isAnimated()
		});
	}
	_initializeFocusTrap() {
		return new FocusTrap({ trapElement: this._element });
	}
	_showElement(relatedTarget) {
		if (!document.body.contains(this._element)) document.body.append(this._element);
		this._element.style.display = "block";
		this._element.removeAttribute("aria-hidden");
		this._element.setAttribute("aria-modal", "true");
		this._element.setAttribute("role", "dialog");
		this._element.scrollTop = 0;
		const modalBody = this._dialog ? SelectorEngine.findOne(SELECTOR_MODAL_BODY, this._dialog) : null;
		if (modalBody) modalBody.scrollTop = 0;
		reflow(this._element);
		this._element.classList.add(CLASS_NAME_SHOW$4);
		const transitionComplete = () => {
			if (this._config.focus) this._focustrap.activate();
			this._isTransitioning = false;
			EventHandler.trigger(this._element, EVENT_SHOWN$4, { relatedTarget });
		};
		this._queueCallback(transitionComplete, this._dialog, this._isAnimated());
	}
	_addEventListeners() {
		EventHandler.on(this._element, EVENT_KEYDOWN_DISMISS$1, (event) => {
			if (event.key !== ESCAPE_KEY$1) return;
			if (this._config.keyboard) {
				this.hide();
				return;
			}
			this._triggerBackdropTransition();
		});
		EventHandler.on(window, EVENT_RESIZE$1, () => {
			if (this._isShown && !this._isTransitioning) this._adjustDialog();
		});
		EventHandler.on(this._element, EVENT_MOUSEDOWN_DISMISS, (event) => {
			EventHandler.one(this._element, EVENT_CLICK_DISMISS, (event2) => {
				if (this._element !== event.target || this._element !== event2.target) return;
				if (this._config.backdrop === "static") {
					this._triggerBackdropTransition();
					return;
				}
				if (this._config.backdrop) this.hide();
			});
		});
	}
	_hideModal() {
		this._element.style.display = "none";
		this._element.setAttribute("aria-hidden", "true");
		this._element.removeAttribute("aria-modal");
		this._element.removeAttribute("role");
		this._isTransitioning = false;
		this._backdrop.hide(() => {
			document.body.classList.remove(CLASS_NAME_OPEN);
			this._resetAdjustments();
			this._scrollBar.reset();
			EventHandler.trigger(this._element, EVENT_HIDDEN$4);
		});
	}
	_isAnimated() {
		return this._element.classList.contains(CLASS_NAME_FADE$3);
	}
	_triggerBackdropTransition() {
		const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE_PREVENTED$1);
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		const isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;
		const initialOverflowY = this._element.style.overflowY;
		if (initialOverflowY === "hidden" || this._element.classList.contains(CLASS_NAME_STATIC)) return;
		if (!isModalOverflowing) this._element.style.overflowY = "hidden";
		this._element.classList.add(CLASS_NAME_STATIC);
		this._queueCallback(() => {
			this._element.classList.remove(CLASS_NAME_STATIC);
			this._queueCallback(() => {
				this._element.style.overflowY = initialOverflowY;
			}, this._dialog);
		}, this._dialog);
		this._element.focus();
	}
	_adjustDialog() {
		const isModalOverflowing = this._element.scrollHeight > document.documentElement.clientHeight;
		const scrollbarWidth = this._scrollBar.getWidth();
		const isBodyOverflowing = scrollbarWidth > 0;
		if (isBodyOverflowing && !isModalOverflowing) {
			const property = isRTL() ? "paddingLeft" : "paddingRight";
			this._element.style[property] = `${scrollbarWidth}px`;
		}
		if (!isBodyOverflowing && isModalOverflowing) {
			const property = isRTL() ? "paddingRight" : "paddingLeft";
			this._element.style[property] = `${scrollbarWidth}px`;
		}
	}
	_resetAdjustments() {
		this._element.style.paddingLeft = "";
		this._element.style.paddingRight = "";
	}
	static jQueryInterface(config, relatedTarget) {
		return this.each(function() {
			const data = Modal.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
			data[config](relatedTarget);
		});
	}
};
/**
* Data API implementation
*/
EventHandler.on(document, EVENT_CLICK_DATA_API$2, SELECTOR_DATA_TOGGLE$5, function(event) {
	const target = SelectorEngine.getElementFromSelector(this);
	if (["A", "AREA"].includes(this.tagName)) event.preventDefault();
	if (!target) return;
	EventHandler.one(target, EVENT_SHOW$4, (showEvent) => {
		if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
		EventHandler.one(target, EVENT_HIDDEN$4, () => {
			if (isVisible(this)) this.focus();
		});
	});
	const alreadyOpen = SelectorEngine.findOne(OPEN_SELECTOR$1);
	if (alreadyOpen) Modal.getInstance(alreadyOpen).hide();
	Modal.getOrCreateInstance(target).toggle(this);
});
enableDismissTrigger(Modal);
defineJQueryPlugin(Modal);
//#endregion
//#region js/src/bootstrap/offcanvas.ts
/**
* --------------------------------------------------------------------------
* Bootstrap offcanvas.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$11 = "offcanvas";
var EVENT_KEY$6 = `.bs.offcanvas`;
var DATA_API_KEY$1 = ".data-api";
var EVENT_LOAD_DATA_API$2 = `load${EVENT_KEY$6}${DATA_API_KEY$1}`;
var ESCAPE_KEY = "Escape";
var CLASS_NAME_SHOW$3 = "show";
var CLASS_NAME_SHOWING$1 = "showing";
var CLASS_NAME_HIDING = "hiding";
var CLASS_NAME_BACKDROP = "offcanvas-backdrop";
var OPEN_SELECTOR = ".offcanvas.show";
var EVENT_SHOW$3 = `show${EVENT_KEY$6}`;
var EVENT_SHOWN$3 = `shown${EVENT_KEY$6}`;
var EVENT_HIDE$3 = `hide${EVENT_KEY$6}`;
var EVENT_HIDE_PREVENTED = `hidePrevented${EVENT_KEY$6}`;
var EVENT_HIDDEN$3 = `hidden${EVENT_KEY$6}`;
var EVENT_RESIZE = `resize${EVENT_KEY$6}`;
var EVENT_CLICK_DATA_API$1 = `click${EVENT_KEY$6}${DATA_API_KEY$1}`;
var EVENT_KEYDOWN_DISMISS = `keydown.dismiss${EVENT_KEY$6}`;
var SELECTOR_DATA_TOGGLE$4 = "[data-bs-toggle=\"offcanvas\"], [data-tblr-toggle=\"offcanvas\"]";
var Default$10 = {
	backdrop: true,
	keyboard: true,
	scroll: false
};
var DefaultType$10 = {
	backdrop: "(boolean|string)",
	keyboard: "boolean",
	scroll: "boolean"
};
/**
* Class definition
*/
var Offcanvas = class Offcanvas extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._isShown = false;
		this._backdrop = this._initializeBackDrop();
		this._focustrap = this._initializeFocusTrap();
		this._addEventListeners();
	}
	static get Default() {
		return Default$10;
	}
	static get DefaultType() {
		return DefaultType$10;
	}
	static get NAME() {
		return NAME$11;
	}
	toggle(relatedTarget) {
		return this._isShown ? this.hide() : this.show(relatedTarget);
	}
	show(relatedTarget) {
		if (this._isShown) return;
		const showEvent = EventHandler.trigger(this._element, EVENT_SHOW$3, { relatedTarget });
		if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
		this._isShown = true;
		this._backdrop.show();
		if (!this._config.scroll) new ScrollBarHelper().hide();
		this._element.setAttribute("aria-modal", "true");
		this._element.setAttribute("role", "dialog");
		this._element.classList.add(CLASS_NAME_SHOWING$1);
		const completeCallBack = () => {
			if (!this._config.scroll || this._config.backdrop) this._focustrap.activate();
			this._element.classList.add(CLASS_NAME_SHOW$3);
			this._element.classList.remove(CLASS_NAME_SHOWING$1);
			EventHandler.trigger(this._element, EVENT_SHOWN$3, { relatedTarget });
		};
		this._queueCallback(completeCallBack, this._element, true);
	}
	hide() {
		if (!this._isShown) return;
		const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE$3);
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		this._focustrap.deactivate();
		this._element.blur();
		this._isShown = false;
		this._element.classList.add(CLASS_NAME_HIDING);
		this._backdrop.hide();
		const completeCallback = () => {
			this._element.classList.remove(CLASS_NAME_SHOW$3, CLASS_NAME_HIDING);
			this._element.removeAttribute("aria-modal");
			this._element.removeAttribute("role");
			if (!this._config.scroll) new ScrollBarHelper().reset();
			EventHandler.trigger(this._element, EVENT_HIDDEN$3);
		};
		this._queueCallback(completeCallback, this._element, true);
	}
	dispose() {
		this._backdrop.dispose();
		this._focustrap.deactivate();
		super.dispose();
	}
	_initializeBackDrop() {
		const clickCallback = () => {
			if (this._config.backdrop === "static") {
				EventHandler.trigger(this._element, EVENT_HIDE_PREVENTED);
				return;
			}
			this.hide();
		};
		const isBackdropVisible = Boolean(this._config.backdrop);
		return new Backdrop({
			className: CLASS_NAME_BACKDROP,
			isVisible: isBackdropVisible,
			isAnimated: true,
			rootElement: this._element.parentNode,
			clickCallback: isBackdropVisible ? clickCallback : null
		});
	}
	_initializeFocusTrap() {
		return new FocusTrap({ trapElement: this._element });
	}
	_addEventListeners() {
		EventHandler.on(this._element, EVENT_KEYDOWN_DISMISS, (event) => {
			if (event.key !== ESCAPE_KEY) return;
			if (this._config.keyboard) {
				this.hide();
				return;
			}
			EventHandler.trigger(this._element, EVENT_HIDE_PREVENTED);
		});
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Offcanvas.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (data[config] === void 0 || config.startsWith("_") || config === "constructor") throw new TypeError(`No method named "${config}"`);
			data[config](this);
		});
	}
};
/**
* Data API implementation
*/
EventHandler.on(document, EVENT_CLICK_DATA_API$1, SELECTOR_DATA_TOGGLE$4, function(event) {
	const target = SelectorEngine.getElementFromSelector(this);
	if (["A", "AREA"].includes(this.tagName)) event.preventDefault();
	if (!target) return;
	if (isDisabled(this)) return;
	EventHandler.one(target, EVENT_HIDDEN$3, () => {
		if (isVisible(this)) this.focus();
	});
	const alreadyOpen = SelectorEngine.findOne(OPEN_SELECTOR);
	if (alreadyOpen && alreadyOpen !== target) Offcanvas.getInstance(alreadyOpen).hide();
	Offcanvas.getOrCreateInstance(target).toggle(this);
});
EventHandler.on(window, EVENT_LOAD_DATA_API$2, () => {
	for (const selector of SelectorEngine.find(OPEN_SELECTOR)) Offcanvas.getOrCreateInstance(selector).show();
});
EventHandler.on(window, EVENT_RESIZE, () => {
	for (const element of SelectorEngine.find("[aria-modal][class*=show][class*=offcanvas-]")) if (getComputedStyle(element).position !== "fixed") Offcanvas.getOrCreateInstance(element).hide();
});
enableDismissTrigger(Offcanvas);
defineJQueryPlugin(Offcanvas);
var DefaultAllowlist = {
	"*": [
		"class",
		"dir",
		"id",
		"lang",
		"role",
		/^aria-[\w-]*$/i
	],
	"a": [
		"target",
		"href",
		"title",
		"rel"
	],
	"area": [],
	"b": [],
	"br": [],
	"col": [],
	"code": [],
	"dd": [],
	"div": [],
	"dl": [],
	"dt": [],
	"em": [],
	"hr": [],
	"h1": [],
	"h2": [],
	"h3": [],
	"h4": [],
	"h5": [],
	"h6": [],
	"i": [],
	"img": [
		"src",
		"srcset",
		"alt",
		"title",
		"width",
		"height"
	],
	"li": [],
	"ol": [],
	"p": [],
	"pre": [],
	"s": [],
	"small": [],
	"span": [],
	"sub": [],
	"sup": [],
	"strong": [],
	"u": [],
	"ul": []
};
var uriAttributes = /* @__PURE__ */ new Set([
	"background",
	"cite",
	"href",
	"itemtype",
	"longdesc",
	"poster",
	"src",
	"xlink:href"
]);
var SAFE_URL_PATTERN = /^(?!javascript:)(?:[a-z0-9+.-]+:|[^&:/?#]*(?:[/?#]|$))/i;
var allowedAttribute = (attribute, allowedAttributeList) => {
	const attributeName = attribute.nodeName.toLowerCase();
	if (allowedAttributeList.includes(attributeName)) {
		if (uriAttributes.has(attributeName)) return Boolean(SAFE_URL_PATTERN.test(attribute.nodeValue));
		return true;
	}
	return allowedAttributeList.filter((attributeRegex) => attributeRegex instanceof RegExp).some((regex) => regex.test(attributeName));
};
function sanitizeHtml(unsafeHtml, allowList, sanitizeFunction) {
	if (!unsafeHtml.length) return unsafeHtml;
	if (sanitizeFunction && typeof sanitizeFunction === "function") return sanitizeFunction(unsafeHtml);
	const createdDocument = new window.DOMParser().parseFromString(unsafeHtml, "text/html");
	const elements = Array.from(createdDocument.body.querySelectorAll("*"));
	for (const element of elements) {
		const elementName = element.nodeName.toLowerCase();
		if (!Object.keys(allowList).includes(elementName)) {
			element.remove();
			continue;
		}
		const attributeList = Array.from(element.attributes);
		const allowedAttributes = [...allowList["*"] || [], ...allowList[elementName] || []];
		for (const attribute of attributeList) if (!allowedAttribute(attribute, allowedAttributes)) element.removeAttribute(attribute.nodeName);
	}
	return createdDocument.body.innerHTML;
}
//#endregion
//#region js/src/bootstrap/util/template-factory.ts
/**
* --------------------------------------------------------------------------
* Bootstrap util/template-factory.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$10 = "TemplateFactory";
var Default$9 = {
	allowList: DefaultAllowlist,
	content: {},
	extraClass: "",
	html: false,
	sanitize: true,
	sanitizeFn: null,
	template: "<div></div>"
};
var DefaultType$9 = {
	allowList: "object",
	content: "object",
	extraClass: "(string|function)",
	html: "boolean",
	sanitize: "boolean",
	sanitizeFn: "(null|function)",
	template: "string"
};
var DefaultContentType = {
	entry: "(string|element|function|null)",
	selector: "(string|element)"
};
var TemplateFactory = class extends Config {
	constructor(config) {
		super();
		this._config = this._getConfig(config);
	}
	static get Default() {
		return Default$9;
	}
	static get DefaultType() {
		return DefaultType$9;
	}
	static get NAME() {
		return NAME$10;
	}
	getContent() {
		return Object.values(this._config.content).map((config) => this._resolvePossibleFunction(config)).filter(Boolean);
	}
	hasContent() {
		return this.getContent().length > 0;
	}
	changeContent(content) {
		this._checkContent(content);
		this._config.content = _objectSpread2(_objectSpread2({}, this._config.content), content);
		return this;
	}
	toHtml() {
		const templateWrapper = document.createElement("div");
		templateWrapper.innerHTML = this._maybeSanitize(this._config.template);
		for (const [selector, text] of Object.entries(this._config.content)) this._setContent(templateWrapper, text, selector);
		const template = templateWrapper.children[0];
		const extraClass = this._resolvePossibleFunction(this._config.extraClass);
		if (extraClass) template.classList.add(...extraClass.split(" "));
		return template;
	}
	_typeCheckConfig(config) {
		super._typeCheckConfig(config);
		this._checkContent(config.content);
	}
	_checkContent(arg) {
		for (const [selector, content] of Object.entries(arg)) super._typeCheckConfig({
			selector,
			entry: content
		}, DefaultContentType);
	}
	_setContent(template, content, selector) {
		const templateElement = SelectorEngine.findOne(selector, template);
		if (!templateElement) return;
		content = this._resolvePossibleFunction(content);
		if (!content) {
			templateElement.remove();
			return;
		}
		if (isElement$1(content)) {
			this._putElementInTemplate(getElement(content), templateElement);
			return;
		}
		if (this._config.html) {
			templateElement.innerHTML = this._maybeSanitize(content);
			return;
		}
		templateElement.textContent = content;
	}
	_maybeSanitize(arg) {
		return this._config.sanitize ? sanitizeHtml(arg, this._config.allowList, this._config.sanitizeFn) : arg;
	}
	_resolvePossibleFunction(arg) {
		return execute(arg, [void 0, this]);
	}
	_putElementInTemplate(element, templateElement) {
		if (this._config.html) {
			templateElement.innerHTML = "";
			templateElement.append(element);
			return;
		}
		templateElement.textContent = element.textContent;
	}
};
//#endregion
//#region js/src/bootstrap/tooltip.ts
/**
* --------------------------------------------------------------------------
* Bootstrap tooltip.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$9 = "tooltip";
var DISALLOWED_ATTRIBUTES = /* @__PURE__ */ new Set([
	"sanitize",
	"allowList",
	"sanitizeFn"
]);
var CLASS_NAME_FADE$2 = "fade";
var CLASS_NAME_MODAL = "modal";
var CLASS_NAME_SHOW$2 = "show";
var SELECTOR_TOOLTIP_INNER = ".tooltip-inner";
var SELECTOR_MODAL = `.${CLASS_NAME_MODAL}`;
var EVENT_MODAL_HIDE = "hide.bs.modal";
var TRIGGER_HOVER = "hover";
var TRIGGER_FOCUS = "focus";
var TRIGGER_CLICK = "click";
var TRIGGER_MANUAL = "manual";
var EVENT_HIDE$2 = "hide";
var EVENT_HIDDEN$2 = "hidden";
var EVENT_SHOW$2 = "show";
var EVENT_SHOWN$2 = "shown";
var EVENT_INSERTED = "inserted";
var EVENT_CLICK$2 = "click";
var EVENT_FOCUSIN$1 = "focusin";
var EVENT_FOCUSOUT$1 = "focusout";
var EVENT_MOUSEENTER = "mouseenter";
var EVENT_MOUSELEAVE = "mouseleave";
var AttachmentMap = {
	AUTO: "auto",
	TOP: "top",
	RIGHT: isRTL() ? "left" : "right",
	BOTTOM: "bottom",
	LEFT: isRTL() ? "right" : "left"
};
var Default$8 = {
	allowList: DefaultAllowlist,
	animation: true,
	boundary: "clippingParents",
	container: false,
	customClass: "",
	delay: 0,
	fallbackPlacements: [
		"top",
		"right",
		"bottom",
		"left"
	],
	html: false,
	offset: [0, 6],
	placement: "top",
	popperConfig: null,
	sanitize: true,
	sanitizeFn: null,
	selector: false,
	template: "<div class=\"tooltip\" role=\"tooltip\"><div class=\"tooltip-arrow\"></div><div class=\"tooltip-inner\"></div></div>",
	title: "",
	trigger: "hover focus"
};
var DefaultType$8 = {
	allowList: "object",
	animation: "boolean",
	boundary: "(string|element)",
	container: "(string|element|boolean)",
	customClass: "(string|function)",
	delay: "(number|object)",
	fallbackPlacements: "array",
	html: "boolean",
	offset: "(array|string|function)",
	placement: "(string|function)",
	popperConfig: "(null|object|function)",
	sanitize: "boolean",
	sanitizeFn: "(null|function)",
	selector: "(string|boolean)",
	template: "string",
	title: "(string|element|function)",
	trigger: "string"
};
/**
* Class definition
*/
var Tooltip = class Tooltip extends BaseComponent {
	constructor(element, config) {
		if (typeof lib_exports === "undefined") throw new TypeError("Bootstrap's tooltips require Popper (https://popper.js.org/docs/v2/)");
		super(element, config);
		this._isEnabled = true;
		this._timeout = 0;
		this._isHovered = null;
		this._activeTrigger = {};
		this._popper = null;
		this._templateFactory = null;
		this._newContent = null;
		this.tip = null;
		this._hideModalHandler = () => {
			if (this._element) this.hide();
		};
		this._setListeners();
		if (!this._config.selector) this._fixTitle();
	}
	static get Default() {
		return Default$8;
	}
	static get DefaultType() {
		return DefaultType$8;
	}
	static get NAME() {
		return NAME$9;
	}
	enable() {
		this._isEnabled = true;
	}
	disable() {
		this._isEnabled = false;
	}
	toggleEnabled() {
		this._isEnabled = !this._isEnabled;
	}
	toggle() {
		if (!this._isEnabled) return;
		if (this._isShown()) {
			this._leave();
			return;
		}
		this._enter();
	}
	dispose() {
		clearTimeout(this._timeout);
		EventHandler.off(this._element.closest(SELECTOR_MODAL), EVENT_MODAL_HIDE, this._hideModalHandler);
		if (this._element.getAttribute("data-bs-original-title") || this._element.getAttribute("data-tblr-original-title")) this._element.setAttribute("title", this._element.getAttribute("data-bs-original-title") || this._element.getAttribute("data-tblr-original-title") || "");
		this._disposePopper();
		super.dispose();
	}
	show() {
		if (this._element.style.display === "none") throw new Error("Please use show on visible elements");
		if (!(this._isWithContent() && this._isEnabled)) return;
		const showEvent = EventHandler.trigger(this._element, this.constructor.eventName(EVENT_SHOW$2));
		const isInTheDom = (findShadowRoot(this._element) || this._element.ownerDocument.documentElement).contains(this._element);
		if ((showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) || !isInTheDom) return;
		this._disposePopper();
		const tip = this._getTipElement();
		this._element.setAttribute("aria-describedby", tip.getAttribute("id"));
		const { container } = this._config;
		if (!this._element.ownerDocument.documentElement.contains(this.tip)) {
			container.append(tip);
			EventHandler.trigger(this._element, this.constructor.eventName(EVENT_INSERTED));
		}
		this._popper = this._createPopper(tip);
		tip.classList.add(CLASS_NAME_SHOW$2);
		if ("ontouchstart" in document.documentElement) for (const element of Array.from(document.body.children)) EventHandler.on(element, "mouseover", noop);
		const complete = () => {
			EventHandler.trigger(this._element, this.constructor.eventName(EVENT_SHOWN$2));
			if (this._isHovered === false) this._leave();
			this._isHovered = false;
		};
		this._queueCallback(complete, this.tip, this._isAnimated());
	}
	hide() {
		if (!this._isShown()) return;
		const hideEvent = EventHandler.trigger(this._element, this.constructor.eventName(EVENT_HIDE$2));
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		this._getTipElement().classList.remove(CLASS_NAME_SHOW$2);
		if ("ontouchstart" in document.documentElement) for (const element of Array.from(document.body.children)) EventHandler.off(element, "mouseover", noop);
		this._activeTrigger[TRIGGER_CLICK] = false;
		this._activeTrigger[TRIGGER_FOCUS] = false;
		this._activeTrigger[TRIGGER_HOVER] = false;
		this._isHovered = null;
		const complete = () => {
			if (this._isWithActiveTrigger()) return;
			if (!this._isHovered) this._disposePopper();
			this._element.removeAttribute("aria-describedby");
			EventHandler.trigger(this._element, this.constructor.eventName(EVENT_HIDDEN$2));
		};
		this._queueCallback(complete, this.tip, this._isAnimated());
	}
	update() {
		if (this._popper) this._popper.update();
	}
	_isWithContent() {
		return Boolean(this._getTitle());
	}
	_getTipElement() {
		if (!this.tip) this.tip = this._createTipElement(this._newContent || this._getContentForTemplate());
		return this.tip;
	}
	_createTipElement(content) {
		const tip = this._getTemplateFactory(content).toHtml();
		if (!tip) return null;
		tip.classList.remove(CLASS_NAME_FADE$2, CLASS_NAME_SHOW$2);
		tip.classList.add(`bs-${this.constructor.NAME}-auto`);
		const tipId = getUID(this.constructor.NAME).toString();
		tip.setAttribute("id", tipId);
		if (this._isAnimated()) tip.classList.add(CLASS_NAME_FADE$2);
		return tip;
	}
	setContent(content) {
		this._newContent = content;
		if (this._isShown()) {
			this._disposePopper();
			this.show();
		}
	}
	_getTemplateFactory(content) {
		if (this._templateFactory) this._templateFactory.changeContent(content);
		else this._templateFactory = new TemplateFactory(_objectSpread2(_objectSpread2({}, this._config), {}, {
			content,
			extraClass: this._resolvePossibleFunction(this._config.customClass)
		}));
		return this._templateFactory;
	}
	_getContentForTemplate() {
		return { [SELECTOR_TOOLTIP_INNER]: this._getTitle() };
	}
	_getTitle() {
		return this._resolvePossibleFunction(this._config.title) || this._element.getAttribute("data-bs-original-title") || this._element.getAttribute("data-tblr-original-title") || "";
	}
	_initializeOnDelegatedTarget(event) {
		return this.constructor.getOrCreateInstance(event.delegateTarget, this._getDelegateConfig());
	}
	_isAnimated() {
		return this._config.animation || this.tip !== null && this.tip.classList.contains(CLASS_NAME_FADE$2);
	}
	_isShown() {
		return this.tip !== null && this.tip.classList.contains(CLASS_NAME_SHOW$2);
	}
	_createPopper(tip) {
		const attachment = AttachmentMap[execute(this._config.placement, [
			this,
			tip,
			this._element
		]).toUpperCase()];
		return createPopper(this._element, tip, this._getPopperConfig(attachment));
	}
	_getOffset() {
		const { offset } = this._config;
		if (typeof offset === "string") return offset.split(",").map((value) => Number.parseInt(value, 10));
		if (typeof offset === "function") return (popperData) => offset(popperData, this._element);
		return offset;
	}
	_resolvePossibleFunction(arg) {
		return execute(arg, [this._element, this._element]);
	}
	_getPopperConfig(attachment) {
		const defaultBsPopperConfig = {
			placement: attachment,
			modifiers: [
				{
					name: "flip",
					options: { fallbackPlacements: this._config.fallbackPlacements }
				},
				{
					name: "offset",
					options: { offset: this._getOffset() }
				},
				{
					name: "preventOverflow",
					options: { boundary: this._config.boundary }
				},
				{
					name: "arrow",
					options: { element: `.${this.constructor.NAME}-arrow` }
				},
				{
					name: "preSetPlacement",
					enabled: true,
					phase: "beforeMain",
					fn: (data) => {
						this._getTipElement().setAttribute("data-popper-placement", data.state.placement);
					}
				}
			]
		};
		const popperConfig = execute(this._config.popperConfig, [void 0, defaultBsPopperConfig]);
		return _objectSpread2(_objectSpread2({}, defaultBsPopperConfig), typeof popperConfig === "object" && popperConfig !== null ? popperConfig : {});
	}
	_setListeners() {
		const triggers = this._config.trigger.split(" ");
		for (const trigger of triggers) if (trigger === "click") EventHandler.on(this._element, this.constructor.eventName(EVENT_CLICK$2), this._config.selector, (event) => {
			const context = this._initializeOnDelegatedTarget(event);
			context._activeTrigger[TRIGGER_CLICK] = !(context._isShown() && context._activeTrigger[TRIGGER_CLICK]);
			context.toggle();
		});
		else if (trigger !== TRIGGER_MANUAL) {
			const eventIn = trigger === TRIGGER_HOVER ? this.constructor.eventName(EVENT_MOUSEENTER) : this.constructor.eventName(EVENT_FOCUSIN$1);
			const eventOut = trigger === TRIGGER_HOVER ? this.constructor.eventName(EVENT_MOUSELEAVE) : this.constructor.eventName(EVENT_FOCUSOUT$1);
			EventHandler.on(this._element, eventIn, this._config.selector, (event) => {
				const context = this._initializeOnDelegatedTarget(event);
				context._activeTrigger[event.type === "focusin" ? TRIGGER_FOCUS : TRIGGER_HOVER] = true;
				context._enter();
			});
			EventHandler.on(this._element, eventOut, this._config.selector, (event) => {
				const context = this._initializeOnDelegatedTarget(event);
				context._activeTrigger[event.type === "focusout" ? TRIGGER_FOCUS : TRIGGER_HOVER] = context._element.contains(event.relatedTarget);
				context._leave();
			});
		}
		EventHandler.on(this._element.closest(SELECTOR_MODAL), EVENT_MODAL_HIDE, this._hideModalHandler);
	}
	_fixTitle() {
		const title = this._element.getAttribute("title");
		if (!title) return;
		if (!this._element.getAttribute("aria-label") && !this._element.textContent.trim()) this._element.setAttribute("aria-label", title);
		this._element.setAttribute("data-bs-original-title", title);
		this._element.removeAttribute("title");
	}
	_enter() {
		if (this._isShown() || this._isHovered) {
			this._isHovered = true;
			return;
		}
		this._isHovered = true;
		this._setTimeout(() => {
			if (this._isHovered) this.show();
		}, this._getDelay().show);
	}
	_leave() {
		if (this._isWithActiveTrigger()) return;
		this._isHovered = false;
		this._setTimeout(() => {
			if (!this._isHovered) this.hide();
		}, this._getDelay().hide);
	}
	_getDelay() {
		const { delay } = this._config;
		return typeof delay === "number" ? {
			show: delay,
			hide: delay
		} : delay;
	}
	_setTimeout(handler, timeout) {
		clearTimeout(this._timeout);
		this._timeout = setTimeout(handler, timeout);
	}
	_isWithActiveTrigger() {
		return Object.values(this._activeTrigger).includes(true);
	}
	_getConfig(config) {
		const dataAttributes = Manipulator.getDataAttributes(this._element);
		for (const dataAttribute of Object.keys(dataAttributes)) if (DISALLOWED_ATTRIBUTES.has(dataAttribute)) delete dataAttributes[dataAttribute];
		let merged = _objectSpread2(_objectSpread2({}, dataAttributes), typeof config === "object" && config ? config : {});
		merged = this._mergeConfigObj(merged);
		merged = this._configAfterMerge(merged);
		this._typeCheckConfig(merged);
		return merged;
	}
	_configAfterMerge(config) {
		config.container = config.container === false ? document.body : getElement(config.container);
		if (typeof config.delay === "number") config.delay = {
			show: config.delay,
			hide: config.delay
		};
		const raw = config;
		if (typeof raw.title === "number") raw.title = raw.title.toString();
		if (typeof raw.content === "number") raw.content = raw.content.toString();
		return config;
	}
	_getDelegateConfig() {
		const config = {};
		const defaults = this.constructor.Default;
		for (const [key, value] of Object.entries(this._config)) if (defaults[key] !== value) config[key] = value;
		config.selector = false;
		config.trigger = "manual";
		return config;
	}
	_disposePopper() {
		if (this._popper) {
			this._popper.destroy();
			this._popper = null;
		}
		if (this.tip) {
			this.tip.remove();
			this.tip = null;
		}
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Tooltip.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
			data[config]();
		});
	}
};
defineJQueryPlugin(Tooltip);
//#endregion
//#region js/src/bootstrap/popover.ts
/**
* --------------------------------------------------------------------------
* Bootstrap popover.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$8 = "popover";
var SELECTOR_TITLE = ".popover-header";
var SELECTOR_CONTENT = ".popover-body";
var Default$7 = _objectSpread2(_objectSpread2({}, Tooltip.Default), {}, {
	content: "",
	offset: [0, 8],
	placement: "right",
	template: "<div class=\"popover\" role=\"tooltip\"><div class=\"popover-arrow\"></div><h3 class=\"popover-header\"></h3><div class=\"popover-body\"></div></div>",
	trigger: "click"
});
var DefaultType$7 = _objectSpread2(_objectSpread2({}, Tooltip.DefaultType), {}, { content: "(null|string|element|function)" });
/**
* Class definition
*/
var Popover = class Popover extends Tooltip {
	constructor(element, config) {
		super(element, config);
	}
	static get Default() {
		return Default$7;
	}
	static get DefaultType() {
		return DefaultType$7;
	}
	static get NAME() {
		return NAME$8;
	}
	_isWithContent() {
		return Boolean(this._getTitle() || this._getContent());
	}
	_getContentForTemplate() {
		return {
			[SELECTOR_TITLE]: this._getTitle(),
			[SELECTOR_CONTENT]: this._getContent()
		};
	}
	_getContent() {
		return this._resolvePossibleFunction(this._config.content);
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Popover.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
			data[config]();
		});
	}
};
defineJQueryPlugin(Popover);
//#endregion
//#region js/src/bootstrap/scrollspy.ts
/**
* --------------------------------------------------------------------------
* Bootstrap scrollspy.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$7 = "scrollspy";
var EVENT_KEY$5 = `.bs.scrollspy`;
var DATA_API_KEY = ".data-api";
var EVENT_ACTIVATE = `activate${EVENT_KEY$5}`;
var EVENT_CLICK$1 = `click${EVENT_KEY$5}`;
var EVENT_LOAD_DATA_API$1 = `load${EVENT_KEY$5}${DATA_API_KEY}`;
var CLASS_NAME_DROPDOWN_ITEM = "dropdown-item";
var CLASS_NAME_ACTIVE$3 = "active";
var SELECTOR_DATA_SPY = "[data-bs-spy=\"scroll\"], [data-tblr-spy=\"scroll\"]";
var SELECTOR_TARGET_LINKS = "[href]";
var SELECTOR_NAV_LIST_GROUP = ".nav, .list-group";
var SELECTOR_NAV_LINKS = ".nav-link";
var SELECTOR_LINK_ITEMS = `${SELECTOR_NAV_LINKS}, .nav-item > ${SELECTOR_NAV_LINKS}, .list-group-item`;
var SELECTOR_DROPDOWN = ".dropdown";
var SELECTOR_DROPDOWN_TOGGLE$1 = ".dropdown-toggle";
var Default$6 = {
	offset: null,
	rootMargin: "0px 0px -25%",
	smoothScroll: false,
	target: null,
	threshold: [
		.1,
		.5,
		1
	]
};
var DefaultType$6 = {
	offset: "(number|null)",
	rootMargin: "string",
	smoothScroll: "boolean",
	target: "element",
	threshold: "array"
};
var ScrollSpy = class ScrollSpy extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._targetLinks = /* @__PURE__ */ new Map();
		this._observableSections = /* @__PURE__ */ new Map();
		this._rootElement = getComputedStyle(this._element).overflowY === "visible" ? null : this._element;
		this._activeTarget = null;
		this._observer = null;
		this._previousScrollData = {
			visibleEntryTop: 0,
			parentScrollTop: 0
		};
		this.refresh();
	}
	static get Default() {
		return Default$6;
	}
	static get DefaultType() {
		return DefaultType$6;
	}
	static get NAME() {
		return NAME$7;
	}
	refresh() {
		this._initializeTargetsAndObservables();
		this._maybeEnableSmoothScroll();
		if (this._observer) this._observer.disconnect();
		else this._observer = this._getNewObserver();
		for (const section of this._observableSections.values()) this._observer.observe(section);
	}
	dispose() {
		this._observer.disconnect();
		super.dispose();
	}
	_configAfterMerge(config) {
		config.target = getElement(config.target) || document.body;
		config.rootMargin = config.offset ? `${config.offset}px 0px -30%` : config.rootMargin;
		const threshold = config.threshold;
		if (typeof threshold === "string") config.threshold = threshold.split(",").map((value) => Number.parseFloat(value));
		return config;
	}
	_maybeEnableSmoothScroll() {
		if (!this._config.smoothScroll) return;
		EventHandler.off(this._config.target, EVENT_CLICK$1);
		EventHandler.on(this._config.target, EVENT_CLICK$1, SELECTOR_TARGET_LINKS, (event) => {
			const observableSection = this._observableSections.get(event.target.hash);
			if (observableSection) {
				event.preventDefault();
				const root = this._rootElement || window;
				const height = observableSection.offsetTop - this._element.offsetTop;
				if ("scrollTo" in root) {
					root.scrollTo({
						top: height,
						behavior: "smooth"
					});
					return;
				}
				root.scrollTop = height;
			}
		});
	}
	_getNewObserver() {
		const options = {
			root: this._rootElement,
			threshold: this._config.threshold,
			rootMargin: this._config.rootMargin
		};
		return new IntersectionObserver((entries) => this._observerCallback(entries), options);
	}
	_observerCallback(entries) {
		const targetElement = (entry) => this._targetLinks.get(`#${entry.target.id}`);
		const activate = (entry) => {
			this._previousScrollData.visibleEntryTop = entry.target.offsetTop;
			this._process(targetElement(entry));
		};
		const parentScrollTop = (this._rootElement || document.documentElement).scrollTop;
		const userScrollsDown = parentScrollTop >= this._previousScrollData.parentScrollTop;
		this._previousScrollData.parentScrollTop = parentScrollTop;
		for (const entry of entries) {
			if (!entry.isIntersecting) {
				this._activeTarget = null;
				this._clearActiveClass(targetElement(entry));
				continue;
			}
			const entryIsLowerThanPrevious = entry.target.offsetTop >= this._previousScrollData.visibleEntryTop;
			if (userScrollsDown && entryIsLowerThanPrevious) {
				activate(entry);
				if (!parentScrollTop) return;
				continue;
			}
			if (!userScrollsDown && !entryIsLowerThanPrevious) activate(entry);
		}
	}
	_initializeTargetsAndObservables() {
		this._targetLinks = /* @__PURE__ */ new Map();
		this._observableSections = /* @__PURE__ */ new Map();
		const targetLinks = SelectorEngine.find(SELECTOR_TARGET_LINKS, this._config.target);
		for (const anchor of targetLinks) {
			if (!anchor.hash || isDisabled(anchor)) continue;
			const observableSection = SelectorEngine.findOne(parseSelector(decodeURI(anchor.hash)), this._element);
			if (isVisible(observableSection)) {
				this._targetLinks.set(decodeURI(anchor.hash), anchor);
				this._observableSections.set(anchor.hash, observableSection);
			}
		}
	}
	_process(target) {
		if (this._activeTarget === target) return;
		this._clearActiveClass(this._config.target);
		this._activeTarget = target;
		target.classList.add(CLASS_NAME_ACTIVE$3);
		this._activateParents(target);
		EventHandler.trigger(this._element, EVENT_ACTIVATE, { relatedTarget: target });
	}
	_activateParents(target) {
		if (target.classList.contains(CLASS_NAME_DROPDOWN_ITEM)) {
			SelectorEngine.findOne(SELECTOR_DROPDOWN_TOGGLE$1, target.closest(SELECTOR_DROPDOWN)).classList.add(CLASS_NAME_ACTIVE$3);
			return;
		}
		for (const listGroup of SelectorEngine.parents(target, SELECTOR_NAV_LIST_GROUP)) for (const item of SelectorEngine.prev(listGroup, SELECTOR_LINK_ITEMS)) item.classList.add(CLASS_NAME_ACTIVE$3);
	}
	_clearActiveClass(parent) {
		parent.classList.remove(CLASS_NAME_ACTIVE$3);
		const activeNodes = SelectorEngine.find(`${SELECTOR_TARGET_LINKS}.${CLASS_NAME_ACTIVE$3}`, parent);
		for (const node of activeNodes) node.classList.remove(CLASS_NAME_ACTIVE$3);
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = ScrollSpy.getOrCreateInstance(this, config);
			if (typeof config !== "string") return;
			if (data[config] === void 0 || config.startsWith("_") || config === "constructor") throw new TypeError(`No method named "${config}"`);
			data[config]();
		});
	}
};
EventHandler.on(window, EVENT_LOAD_DATA_API$1, () => {
	for (const spy of SelectorEngine.find(SELECTOR_DATA_SPY)) ScrollSpy.getOrCreateInstance(spy);
});
defineJQueryPlugin(ScrollSpy);
//#endregion
//#region js/src/bootstrap/tab.ts
/**
* --------------------------------------------------------------------------
* Bootstrap tab.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$6 = "tab";
var EVENT_KEY$4 = `.bs.tab`;
var EVENT_HIDE$1 = `hide${EVENT_KEY$4}`;
var EVENT_HIDDEN$1 = `hidden${EVENT_KEY$4}`;
var EVENT_SHOW$1 = `show${EVENT_KEY$4}`;
var EVENT_SHOWN$1 = `shown${EVENT_KEY$4}`;
var EVENT_CLICK_DATA_API = `click${EVENT_KEY$4}`;
var EVENT_KEYDOWN = `keydown${EVENT_KEY$4}`;
var EVENT_LOAD_DATA_API = `load${EVENT_KEY$4}`;
var ARROW_LEFT_KEY = "ArrowLeft";
var ARROW_RIGHT_KEY = "ArrowRight";
var ARROW_UP_KEY = "ArrowUp";
var ARROW_DOWN_KEY = "ArrowDown";
var HOME_KEY = "Home";
var END_KEY = "End";
var CLASS_NAME_ACTIVE$2 = "active";
var CLASS_NAME_FADE$1 = "fade";
var CLASS_NAME_SHOW$1 = "show";
var CLASS_DROPDOWN = "dropdown";
var SELECTOR_DROPDOWN_TOGGLE = ".dropdown-toggle";
var SELECTOR_DROPDOWN_MENU = ".dropdown-menu";
var NOT_SELECTOR_DROPDOWN_TOGGLE = `:not(${SELECTOR_DROPDOWN_TOGGLE})`;
var SELECTOR_TAB_PANEL = ".list-group, .nav, [role=\"tablist\"]";
var SELECTOR_OUTER = ".nav-item, .list-group-item";
var SELECTOR_INNER = `.nav-link${NOT_SELECTOR_DROPDOWN_TOGGLE}, .list-group-item${NOT_SELECTOR_DROPDOWN_TOGGLE}, [role="tab"]${NOT_SELECTOR_DROPDOWN_TOGGLE}`;
var SELECTOR_DATA_TOGGLE$3 = "[data-bs-toggle=\"tab\"], [data-bs-toggle=\"pill\"], [data-bs-toggle=\"list\"], [data-tblr-toggle=\"tab\"], [data-tblr-toggle=\"pill\"], [data-tblr-toggle=\"list\"]";
var SELECTOR_INNER_ELEM = `${SELECTOR_INNER}, ${SELECTOR_DATA_TOGGLE$3}`;
var SELECTOR_DATA_TOGGLE_ACTIVE = `.${CLASS_NAME_ACTIVE$2}[data-bs-toggle="tab"], .${CLASS_NAME_ACTIVE$2}[data-bs-toggle="pill"], .${CLASS_NAME_ACTIVE$2}[data-bs-toggle="list"], .${CLASS_NAME_ACTIVE$2}[data-tblr-toggle="tab"], .${CLASS_NAME_ACTIVE$2}[data-tblr-toggle="pill"], .${CLASS_NAME_ACTIVE$2}[data-tblr-toggle="list"]`;
var Tab = class Tab extends BaseComponent {
	constructor(element) {
		super(element);
		this._parent = this._element.closest(SELECTOR_TAB_PANEL);
		if (!this._parent) return;
		this._setInitialAttributes(this._parent, this._getChildren());
		EventHandler.on(this._element, EVENT_KEYDOWN, (event) => this._keydown(event));
	}
	static get NAME() {
		return NAME$6;
	}
	show() {
		const innerElem = this._element;
		if (this._elemIsActive(innerElem)) return;
		const active = this._getActiveElem();
		const hideEvent = active ? EventHandler.trigger(active, EVENT_HIDE$1, { relatedTarget: innerElem }) : null;
		const showEvent = EventHandler.trigger(innerElem, EVENT_SHOW$1, { relatedTarget: active });
		if ((showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) || (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented)) return;
		this._deactivate(active, innerElem);
		this._activate(innerElem, active);
	}
	_activate(element, relatedElem) {
		if (!element) return;
		element.classList.add(CLASS_NAME_ACTIVE$2);
		this._activate(SelectorEngine.getElementFromSelector(element));
		const complete = () => {
			if (element.getAttribute("role") !== "tab") {
				element.classList.add(CLASS_NAME_SHOW$1);
				return;
			}
			element.removeAttribute("tabindex");
			element.setAttribute("aria-selected", "true");
			this._toggleDropDown(element, true);
			EventHandler.trigger(element, EVENT_SHOWN$1, { relatedTarget: relatedElem });
		};
		this._queueCallback(complete, element, element.classList.contains(CLASS_NAME_FADE$1));
	}
	_deactivate(element, relatedElem) {
		if (!element) return;
		element.classList.remove(CLASS_NAME_ACTIVE$2);
		element.blur();
		this._deactivate(SelectorEngine.getElementFromSelector(element));
		const complete = () => {
			if (element.getAttribute("role") !== "tab") {
				element.classList.remove(CLASS_NAME_SHOW$1);
				return;
			}
			element.setAttribute("aria-selected", "false");
			element.setAttribute("tabindex", "-1");
			this._toggleDropDown(element, false);
			EventHandler.trigger(element, EVENT_HIDDEN$1, { relatedTarget: relatedElem });
		};
		this._queueCallback(complete, element, element.classList.contains(CLASS_NAME_FADE$1));
	}
	_keydown(event) {
		if (![
			ARROW_LEFT_KEY,
			ARROW_RIGHT_KEY,
			ARROW_UP_KEY,
			ARROW_DOWN_KEY,
			HOME_KEY,
			END_KEY
		].includes(event.key)) return;
		event.stopPropagation();
		event.preventDefault();
		const children = this._getChildren().filter((element) => !isDisabled(element));
		let nextActiveElement;
		if ([HOME_KEY, END_KEY].includes(event.key)) nextActiveElement = children[event.key === HOME_KEY ? 0 : children.length - 1];
		else {
			const isNext = [ARROW_RIGHT_KEY, ARROW_DOWN_KEY].includes(event.key);
			nextActiveElement = getNextActiveElement(children, event.target, isNext, true);
		}
		if (nextActiveElement) {
			nextActiveElement.focus({ preventScroll: true });
			Tab.getOrCreateInstance(nextActiveElement).show();
		}
	}
	_getChildren() {
		return SelectorEngine.find(SELECTOR_INNER_ELEM, this._parent);
	}
	_getActiveElem() {
		return this._getChildren().find((child) => this._elemIsActive(child)) || null;
	}
	_setInitialAttributes(parent, children) {
		this._setAttributeIfNotExists(parent, "role", "tablist");
		for (const child of children) this._setInitialAttributesOnChild(child);
	}
	_setInitialAttributesOnChild(child) {
		child = this._getInnerElement(child);
		const isActive = this._elemIsActive(child);
		const outerElem = this._getOuterElement(child);
		child.setAttribute("aria-selected", String(isActive));
		if (outerElem !== child) this._setAttributeIfNotExists(outerElem, "role", "presentation");
		if (!isActive) child.setAttribute("tabindex", "-1");
		this._setAttributeIfNotExists(child, "role", "tab");
		this._setInitialAttributesOnTargetPanel(child);
	}
	_setInitialAttributesOnTargetPanel(child) {
		const target = SelectorEngine.getElementFromSelector(child);
		if (!target) return;
		this._setAttributeIfNotExists(target, "role", "tabpanel");
		if (child.id) this._setAttributeIfNotExists(target, "aria-labelledby", `${child.id}`);
	}
	_toggleDropDown(element, open) {
		const outerElem = this._getOuterElement(element);
		if (!outerElem.classList.contains(CLASS_DROPDOWN)) return;
		const toggle = (selector, className) => {
			const el = SelectorEngine.findOne(selector, outerElem);
			if (el) el.classList.toggle(className, open);
		};
		toggle(SELECTOR_DROPDOWN_TOGGLE, CLASS_NAME_ACTIVE$2);
		toggle(SELECTOR_DROPDOWN_MENU, CLASS_NAME_SHOW$1);
		outerElem.setAttribute("aria-expanded", String(open));
	}
	_setAttributeIfNotExists(element, attribute, value) {
		if (!element.hasAttribute(attribute)) element.setAttribute(attribute, value);
	}
	_elemIsActive(elem) {
		return elem.classList.contains(CLASS_NAME_ACTIVE$2);
	}
	_getInnerElement(elem) {
		return elem.matches(SELECTOR_INNER_ELEM) ? elem : SelectorEngine.findOne(SELECTOR_INNER_ELEM, elem);
	}
	_getOuterElement(elem) {
		return elem.closest(SELECTOR_OUTER) || elem;
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Tab.getOrCreateInstance(this);
			if (typeof config !== "string") return;
			if (data[config] === void 0 || config.startsWith("_") || config === "constructor") throw new TypeError(`No method named "${config}"`);
			data[config]();
		});
	}
};
EventHandler.on(document, EVENT_CLICK_DATA_API, SELECTOR_DATA_TOGGLE$3, function(event) {
	if (["A", "AREA"].includes(this.tagName)) event.preventDefault();
	if (isDisabled(this)) return;
	Tab.getOrCreateInstance(this).show();
});
EventHandler.on(window, EVENT_LOAD_DATA_API, () => {
	for (const element of SelectorEngine.find(SELECTOR_DATA_TOGGLE_ACTIVE)) Tab.getOrCreateInstance(element);
});
defineJQueryPlugin(Tab);
//#endregion
//#region js/src/bootstrap/toast.ts
/**
* --------------------------------------------------------------------------
* Bootstrap toast.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
var NAME$5 = "toast";
var EVENT_KEY$3 = `.bs.toast`;
var EVENT_MOUSEOVER = `mouseover${EVENT_KEY$3}`;
var EVENT_MOUSEOUT = `mouseout${EVENT_KEY$3}`;
var EVENT_FOCUSIN = `focusin${EVENT_KEY$3}`;
var EVENT_FOCUSOUT = `focusout${EVENT_KEY$3}`;
var EVENT_HIDE = `hide${EVENT_KEY$3}`;
var EVENT_HIDDEN = `hidden${EVENT_KEY$3}`;
var EVENT_SHOW = `show${EVENT_KEY$3}`;
var EVENT_SHOWN = `shown${EVENT_KEY$3}`;
var CLASS_NAME_FADE = "fade";
var CLASS_NAME_HIDE = "hide";
var CLASS_NAME_SHOW = "show";
var CLASS_NAME_SHOWING = "showing";
var DefaultType$5 = {
	animation: "boolean",
	autohide: "boolean",
	delay: "number"
};
var Default$5 = {
	animation: true,
	autohide: true,
	delay: 5e3
};
var Toast = class Toast extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._timeout = null;
		this._hasMouseInteraction = false;
		this._hasKeyboardInteraction = false;
		this._setListeners();
	}
	static get Default() {
		return Default$5;
	}
	static get DefaultType() {
		return DefaultType$5;
	}
	static get NAME() {
		return NAME$5;
	}
	show() {
		const showEvent = EventHandler.trigger(this._element, EVENT_SHOW);
		if (showEvent === null || showEvent === void 0 ? void 0 : showEvent.defaultPrevented) return;
		this._clearTimeout();
		if (this._config.animation) this._element.classList.add(CLASS_NAME_FADE);
		const complete = () => {
			this._element.classList.remove(CLASS_NAME_SHOWING);
			EventHandler.trigger(this._element, EVENT_SHOWN);
			this._maybeScheduleHide();
		};
		this._element.classList.remove(CLASS_NAME_HIDE);
		reflow(this._element);
		this._element.classList.add(CLASS_NAME_SHOW, CLASS_NAME_SHOWING);
		this._queueCallback(complete, this._element, this._config.animation);
	}
	hide() {
		if (!this.isShown()) return;
		const hideEvent = EventHandler.trigger(this._element, EVENT_HIDE);
		if (hideEvent === null || hideEvent === void 0 ? void 0 : hideEvent.defaultPrevented) return;
		const complete = () => {
			this._element.classList.add(CLASS_NAME_HIDE);
			this._element.classList.remove(CLASS_NAME_SHOWING, CLASS_NAME_SHOW);
			EventHandler.trigger(this._element, EVENT_HIDDEN);
		};
		this._element.classList.add(CLASS_NAME_SHOWING);
		this._queueCallback(complete, this._element, this._config.animation);
	}
	dispose() {
		this._clearTimeout();
		if (this.isShown()) this._element.classList.remove(CLASS_NAME_SHOW);
		super.dispose();
	}
	isShown() {
		return this._element.classList.contains(CLASS_NAME_SHOW);
	}
	_maybeScheduleHide() {
		if (!this._config.autohide) return;
		if (this._hasMouseInteraction || this._hasKeyboardInteraction) return;
		this._timeout = setTimeout(() => {
			this.hide();
		}, this._config.delay);
	}
	_onInteraction(event, isInteracting) {
		switch (event.type) {
			case "mouseover":
			case "mouseout":
				this._hasMouseInteraction = isInteracting;
				break;
			case "focusin":
			case "focusout": this._hasKeyboardInteraction = isInteracting;
		}
		if (isInteracting) {
			this._clearTimeout();
			return;
		}
		const nextElement = event.relatedTarget;
		if (this._element === nextElement || this._element.contains(nextElement)) return;
		this._maybeScheduleHide();
	}
	_setListeners() {
		EventHandler.on(this._element, EVENT_MOUSEOVER, (event) => this._onInteraction(event, true));
		EventHandler.on(this._element, EVENT_MOUSEOUT, (event) => this._onInteraction(event, false));
		EventHandler.on(this._element, EVENT_FOCUSIN, (event) => this._onInteraction(event, true));
		EventHandler.on(this._element, EVENT_FOCUSOUT, (event) => this._onInteraction(event, false));
	}
	_clearTimeout() {
		clearTimeout(this._timeout);
		this._timeout = null;
	}
	static jQueryInterface(config) {
		return this.each(function() {
			const data = Toast.getOrCreateInstance(this, config);
			if (typeof config === "string") {
				if (typeof data[config] === "undefined") throw new TypeError(`No method named "${config}"`);
				data[config](this);
			}
		});
	}
};
enableDismissTrigger(Toast);
defineJQueryPlugin(Toast);
//#endregion
//#region js/src/bootstrap.ts
var bootstrap = {
	Alert,
	Button,
	Carousel,
	Collapse,
	Dropdown,
	Modal,
	Offcanvas,
	Popover,
	ScrollSpy,
	Tab,
	Toast,
	Tooltip
};
//#endregion
//#region js/src/dropdown.ts
onDOMContentLoaded(() => {
	for (const dropdownTriggerEl of document.querySelectorAll("[data-bs-toggle=\"dropdown\"], [data-tblr-toggle=\"dropdown\"]")) try {
		const options = { boundary: Manipulator.getDataAttribute(dropdownTriggerEl, "boundary") === "viewport" ? document.documentElement : "clippingParents" };
		Dropdown.getOrCreateInstance(dropdownTriggerEl, options);
	} catch (error) {
		console.error(error);
	}
});
//#endregion
//#region js/src/sidebar.ts
var syncSidebarToggles = (folded) => {
	for (const toggle of document.querySelectorAll("[data-bs-toggle=\"sidebar-folded\"], [data-tblr-toggle=\"sidebar-folded\"]")) toggle.setAttribute("aria-pressed", String(folded));
};
var sidebarIsFolded = () => {
	var _document$documentEle;
	return ((_document$documentEle = document.documentElement.getAttribute("data-bs-sidebar")) !== null && _document$documentEle !== void 0 ? _document$documentEle : "").startsWith("folded") || !!document.querySelector(".navbar-vertical.navbar-folded, .navbar-vertical.navbar-folded-hover");
};
var hideSidebarDropdowns = (except) => {
	for (const toggle of document.querySelectorAll(".navbar-vertical [data-bs-toggle=\"dropdown\"][aria-expanded=\"true\"]")) if (toggle !== except) {
		var _Dropdown$getInstance;
		(_Dropdown$getInstance = Dropdown.getInstance(toggle)) === null || _Dropdown$getInstance === void 0 || _Dropdown$getInstance.hide();
	}
};
document.addEventListener("click", (event) => {
	if (!sidebarIsFolded()) return;
	const target = event.target;
	if (!(target instanceof Element) || target.closest(".navbar-vertical .dropdown-menu")) return;
	hideSidebarDropdowns(target.closest(".navbar-vertical [data-bs-toggle=\"dropdown\"]"));
});
document.addEventListener("mouseleave", (event) => {
	const target = event.target;
	if (!(target instanceof Element) || !target.matches(".navbar-vertical")) return;
	if (document.documentElement.getAttribute("data-bs-sidebar") === "folded-hover" || target.classList.contains("navbar-folded-hover")) hideSidebarDropdowns();
}, true);
onDOMContentLoaded(() => {
	var _document$documentEle2;
	const folded = ((_document$documentEle2 = document.documentElement.getAttribute("data-bs-sidebar")) !== null && _document$documentEle2 !== void 0 ? _document$documentEle2 : "").startsWith("folded");
	syncSidebarToggles(folded);
	if (folded) for (const menu of document.querySelectorAll(".navbar-vertical .dropdown-menu.show")) {
		var _menu$parentElement;
		menu.classList.remove("show");
		(_menu$parentElement = menu.parentElement) === null || _menu$parentElement === void 0 || (_menu$parentElement = _menu$parentElement.querySelector("[data-bs-toggle=\"dropdown\"]")) === null || _menu$parentElement === void 0 || _menu$parentElement.setAttribute("aria-expanded", "false");
	}
});
document.addEventListener("click", (event) => {
	var _html$getAttribute;
	if (!(event.target instanceof Element ? event.target.closest("[data-bs-toggle=\"sidebar-folded\"], [data-tblr-toggle=\"sidebar-folded\"]") : null)) return;
	const html = document.documentElement;
	const willFold = !((_html$getAttribute = html.getAttribute("data-bs-sidebar")) !== null && _html$getAttribute !== void 0 ? _html$getAttribute : "").startsWith("folded");
	if (willFold) html.setAttribute("data-bs-sidebar", "folded-hover");
	else html.removeAttribute("data-bs-sidebar");
	try {
		localStorage.setItem("tabler-sidebar", willFold ? "folded-hover" : "default");
	} catch (_unused) {}
	syncSidebarToggles(willFold);
	hideSidebarDropdowns();
	document.dispatchEvent(new CustomEvent("tabler:sidebar-folded", { detail: { folded: willFold } }));
});
//#endregion
//#region js/src/tooltip.ts
onDOMContentLoaded(() => {
	for (const tooltipTriggerEl of document.querySelectorAll("[data-bs-toggle=\"tooltip\"], [data-tblr-toggle=\"tooltip\"]")) try {
		var _Manipulator$getDataA, _Manipulator$getDataA2;
		const options = {
			delay: (_Manipulator$getDataA = Manipulator.getDataAttribute(tooltipTriggerEl, "delay")) !== null && _Manipulator$getDataA !== void 0 ? _Manipulator$getDataA : {
				show: 50,
				hide: 50
			},
			placement: (_Manipulator$getDataA2 = Manipulator.getDataAttribute(tooltipTriggerEl, "placement")) !== null && _Manipulator$getDataA2 !== void 0 ? _Manipulator$getDataA2 : "auto"
		};
		Tooltip.getOrCreateInstance(tooltipTriggerEl, options);
	} catch (error) {
		console.error(error);
	}
});
//#endregion
//#region js/src/popover.ts
onDOMContentLoaded(() => {
	for (const popoverTriggerEl of document.querySelectorAll("[data-bs-toggle=\"popover\"], [data-tblr-toggle=\"popover\"]")) try {
		var _Manipulator$getDataA, _Manipulator$getDataA2;
		const options = {
			delay: (_Manipulator$getDataA = Manipulator.getDataAttribute(popoverTriggerEl, "delay")) !== null && _Manipulator$getDataA !== void 0 ? _Manipulator$getDataA : {
				show: 50,
				hide: 50
			},
			placement: (_Manipulator$getDataA2 = Manipulator.getDataAttribute(popoverTriggerEl, "placement")) !== null && _Manipulator$getDataA2 !== void 0 ? _Manipulator$getDataA2 : "auto"
		};
		Popover.getOrCreateInstance(popoverTriggerEl, options);
	} catch (error) {
		console.error(error);
	}
});
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/checkPrivateRedeclaration.js
function _checkPrivateRedeclaration(e, t) {
	if (t.has(e)) throw new TypeError("Cannot initialize the same private elements twice on an object");
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/classPrivateMethodInitSpec.js
function _classPrivateMethodInitSpec(e, a) {
	_checkPrivateRedeclaration(e, a), a.add(e);
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/classPrivateFieldInitSpec.js
function _classPrivateFieldInitSpec(e, t, a) {
	_checkPrivateRedeclaration(e, t), t.set(e, a);
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/assertClassBrand.js
function _assertClassBrand(e, t, n) {
	if ("function" == typeof e ? e === t : e.has(t)) return arguments.length < 3 ? t : n;
	throw new TypeError("Private element is not present on this object");
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/classPrivateFieldSet2.js
function _classPrivateFieldSet2(s, a, r) {
	return s.set(_assertClassBrand(s, a), r), r;
}
//#endregion
//#region \0@oxc-project+runtime@0.147.0/helpers/esm/classPrivateFieldGet2.js
function _classPrivateFieldGet2(s, a) {
	return s.get(_assertClassBrand(s, a));
}
//#endregion
//#region js/src/switch-icon.ts
/**
* --------------------------------------------------------------------------
* Tabler switch-icon.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$4 = "switch-icon";
var EVENT_KEY$2 = `.${`bs.${NAME$4}`}`;
var EVENT_CLICK = `click${EVENT_KEY$2}`;
var EVENT_TOGGLE = `toggle${EVENT_KEY$2}`;
var EVENT_CHANGE$1 = `change${EVENT_KEY$2}`;
var CLASS_NAME_ACTIVE$1 = "active";
var CLASS_NAME_DISABLED = "disabled";
var CLASS_NAME_LOADING = "switch-icon-loading";
var SELECTOR_SWITCH_ICON = ".switch-icon";
var SELECTOR_DATA_TOGGLE$2 = `[data-bs-toggle="${NAME$4}"], [data-tblr-toggle="${NAME$4}"]`;
var Default$4 = {};
var DefaultType$4 = {};
var _iconElement = /* @__PURE__ */ new WeakMap();
var _SwitchIcon_brand = /* @__PURE__ */ new WeakSet();
/**
* Class definition
*
* A toggle button that swaps between two icons. The state is the `active`
* class, mirrored in `aria-pressed`. The element is the `.switch-icon` itself,
* or a button (e.g. `.btn-action`) wrapping one: the classes then go on the
* inner `.switch-icon` and the ARIA state stays on the button.
*/
var SwitchIcon = class extends BaseComponent {
	constructor(element, config) {
		var _SelectorEngine$findO;
		super(element, config);
		_classPrivateMethodInitSpec(this, _SwitchIcon_brand);
		_classPrivateFieldInitSpec(this, _iconElement, void 0);
		if (!this._element) return;
		_classPrivateFieldSet2(_iconElement, this, this._element.matches(SELECTOR_SWITCH_ICON) ? this._element : (_SelectorEngine$findO = SelectorEngine.findOne(SELECTOR_SWITCH_ICON, this._element)) !== null && _SelectorEngine$findO !== void 0 ? _SelectorEngine$findO : this._element);
		if (!this._element.hasAttribute("aria-pressed")) this._element.setAttribute("aria-pressed", String(this.isActive));
		EventHandler.on(this._element, EVENT_CLICK, (event) => {
			event.stopPropagation();
			if (this._element.classList.contains(CLASS_NAME_DISABLED) || this._element.getAttribute("aria-disabled") === "true") return;
			this.toggle();
		});
	}
	static get Default() {
		return Default$4;
	}
	static get DefaultType() {
		return DefaultType$4;
	}
	static get NAME() {
		return NAME$4;
	}
	get isActive() {
		return _classPrivateFieldGet2(_iconElement, this).classList.contains(CLASS_NAME_ACTIVE$1);
	}
	get isLoading() {
		return _classPrivateFieldGet2(_iconElement, this).classList.contains(CLASS_NAME_LOADING);
	}
	toggle(force) {
		if (this.isLoading) return;
		const active = force !== null && force !== void 0 ? force : !this.isActive;
		let pending;
		const toggleEvent = EventHandler.trigger(this._element, EVENT_TOGGLE, {
			active,
			wait: (promise) => {
				pending = promise;
			}
		});
		if (toggleEvent === null || toggleEvent === void 0 ? void 0 : toggleEvent.defaultPrevented) return;
		if (!pending) {
			_assertClassBrand(_SwitchIcon_brand, this, _setActive).call(this, active);
			return;
		}
		_assertClassBrand(_SwitchIcon_brand, this, _setLoading).call(this, true);
		pending.then(() => _assertClassBrand(_SwitchIcon_brand, this, _setActive).call(this, active), () => void 0).finally(() => _assertClassBrand(_SwitchIcon_brand, this, _setLoading).call(this, false));
	}
	dispose() {
		_assertClassBrand(_SwitchIcon_brand, this, _setLoading).call(this, false);
		super.dispose();
	}
};
function _setActive(active) {
	if (!this._element) return;
	_classPrivateFieldGet2(_iconElement, this).classList.toggle(CLASS_NAME_ACTIVE$1, active);
	this._element.setAttribute("aria-pressed", String(active));
	EventHandler.trigger(this._element, EVENT_CHANGE$1, { active });
}
function _setLoading(loading) {
	if (!this._element) return;
	_classPrivateFieldGet2(_iconElement, this).classList.toggle(CLASS_NAME_LOADING, loading);
	if (loading) this._element.setAttribute("aria-busy", "true");
	else this._element.removeAttribute("aria-busy");
}
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_TOGGLE$2, SwitchIcon);
//#endregion
//#region js/src/tab.ts
var EnableActivationTabsFromLocationHash = () => {
	const locationHash = window.location.hash;
	if (!locationHash) return;
	for (const tab of document.querySelectorAll("[data-bs-toggle=\"tab\"], [data-tblr-toggle=\"tab\"]")) {
		if (tab.hash !== locationHash) continue;
		try {
			Tab.getOrCreateInstance(tab).show();
		} catch (error) {
			console.error(error);
		}
	}
};
onDOMContentLoaded(EnableActivationTabsFromLocationHash);
//#endregion
//#region js/src/toast.ts
onDOMContentLoaded(() => {
	for (const toastTriggerEl of document.querySelectorAll("[data-bs-toggle=\"toast\"], [data-tblr-toggle=\"toast\"]")) try {
		const toastEl = SelectorEngine.getElementFromSelector(toastTriggerEl);
		if (!toastEl) continue;
		const toast = Toast.getOrCreateInstance(toastEl);
		toastTriggerEl.addEventListener("click", () => {
			toast.show();
		});
	} catch (error) {
		console.error(error);
	}
});
//#endregion
//#region js/src/sortable.ts
/**
* --------------------------------------------------------------------------
* Tabler sortable.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$3 = "sortable";
var DATA_ATTRIBUTE = `data-${NAME$3}`;
var SELECTOR_DATA_SORTABLE = `[${DATA_ATTRIBUTE}]`;
var Default$3 = {};
var DefaultType$3 = {};
/**
* Class definition
*
* Wraps SortableJS (https://sortablejs.github.io/Sortable/), loaded separately
* as `window.Sortable`. Without the plugin the component is inert. Options come
* from the `data-sortable` attribute as JSON, or from the config object.
* Turn `forceFallback` on so the dragged copy can be styled with `.sortable-drag`.
*/
var Sortable = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._sortable = null;
		if (!this._element || !window.Sortable) return;
		this._sortable = new window.Sortable(this._element, this._config);
	}
	static get Default() {
		return Default$3;
	}
	static get DefaultType() {
		return DefaultType$3;
	}
	static get NAME() {
		return NAME$3;
	}
	/** The SortableJS instance, for options the component does not expose. */
	get sortable() {
		return this._sortable;
	}
	toArray() {
		var _this$_sortable$toArr, _this$_sortable;
		return (_this$_sortable$toArr = (_this$_sortable = this._sortable) === null || _this$_sortable === void 0 ? void 0 : _this$_sortable.toArray()) !== null && _this$_sortable$toArr !== void 0 ? _this$_sortable$toArr : [];
	}
	sort(order, useAnimation) {
		var _this$_sortable2;
		(_this$_sortable2 = this._sortable) === null || _this$_sortable2 === void 0 || _this$_sortable2.sort(order, useAnimation);
	}
	dispose() {
		var _this$_sortable3;
		(_this$_sortable3 = this._sortable) === null || _this$_sortable3 === void 0 || _this$_sortable3.destroy();
		super.dispose();
	}
	_mergeConfigObj(config, element) {
		let dataOptions = {};
		const raw = element === null || element === void 0 ? void 0 : element.getAttribute(DATA_ATTRIBUTE);
		if (raw) try {
			const parsed = JSON.parse(raw);
			if (parsed && typeof parsed === "object" && !Array.isArray(parsed)) dataOptions = parsed;
		} catch (_unused) {}
		return super._mergeConfigObj(_objectSpread2(_objectSpread2({}, dataOptions), config), element);
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_SORTABLE, Sortable);
//#endregion
//#region js/src/otp-input.ts
/**
* --------------------------------------------------------------------------
* Bootstrap otp-input.ts
* Licensed under MIT (https://github.com/twbs/bootstrap/blob/main/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$2 = "otpInput";
var EVENT_KEY$1 = `.${`bs.${NAME$2}`}`;
var EVENT_INPUT = `input${EVENT_KEY$1}`;
var EVENT_COMPLETE = `complete${EVENT_KEY$1}`;
var CLASS_NAME_INPUT = "otp-input";
var CLASS_NAME_RENDERED = "otp-rendered";
var CLASS_NAME_SLOTS = "otp-slots";
var CLASS_NAME_SLOT = "otp-slot";
var CLASS_NAME_SLOT_FILLED = "otp-slot-filled";
var CLASS_NAME_SLOT_ACTIVE = "otp-slot-active";
var CLASS_NAME_SEPARATOR = "otp-separator";
var SELECTOR_DATA_TOGGLE$1 = "[data-bs-toggle=\"otp\"], [data-tblr-toggle=\"otp\"]";
var SELECTOR_INPUT = "input";
var SYNC_EVENTS = [
	"blur",
	"keyup",
	"select"
];
var POINTER_END_EVENTS = ["click", "pointercancel"];
var MASK_CHARACTER = "•";
var TYPES = {
	numeric: {
		inputmode: "numeric",
		pattern: "[0-9]*",
		filter: /[^0-9]/g
	},
	alphanumeric: {
		inputmode: "text",
		pattern: "[A-Za-z0-9]*",
		filter: /[^A-Za-z0-9]/g
	},
	alpha: {
		inputmode: "text",
		pattern: "[A-Za-z]*",
		filter: /[^A-Za-z]/g
	}
};
var MAX_LENGTH = 32;
var Default$2 = {
	groups: null,
	length: null,
	mask: false,
	separator: "·",
	type: "numeric"
};
var DefaultType$2 = {
	groups: "(array|null)",
	length: "(number|null)",
	mask: "boolean",
	separator: "string",
	type: "string"
};
/**
* Class definition
*
* A single real `<input>` inside `.otp` is turned into a transparent overlay
* once its value is rendered into one `.otp-slot` per character, so screen
* readers, password managers and SMS autofill still see one ordinary field.
*/
var OtpInput = class extends BaseComponent {
	constructor(element, config) {
		var _TYPES$this$_config$t;
		super(element, config);
		this._length = 0;
		this._wasComplete = false;
		this._slots = [];
		this._slotsContainer = null;
		this._pointerActive = false;
		this._pointerIndex = 0;
		this._composedValue = null;
		this._onInput = (event) => {
			if (event.isComposing) return;
			const composed = this._composedValue;
			this._composedValue = null;
			if (composed !== null && composed === this._input.value) return;
			this._handleInput();
		};
		this._onCompositionEnd = () => {
			this._handleInput();
			this._composedValue = this._input.value;
		};
		this._onBeforeInput = (event) => this._handleBeforeInput(event);
		this._onFocus = () => this._handleFocus();
		this._onPointerDown = (event) => this._handlePointerDown(event);
		this._onPointerEnd = () => {
			this._pointerActive = false;
		};
		this._onSync = () => this._render();
		this._onSelectionChange = () => {
			if (document.activeElement === this._input) this._render();
		};
		if (!this._element) return;
		const input = SelectorEngine.findOne(SELECTOR_INPUT, this._element);
		if (!input) return;
		this._input = input;
		this._type = (_TYPES$this$_config$t = TYPES[this._config.type]) !== null && _TYPES$this$_config$t !== void 0 ? _TYPES$this$_config$t : TYPES.numeric;
		this._length = this._resolveLength();
		this._setupInput();
		this._renderSlots();
		this._addEventListeners();
		this._render();
		this._wasComplete = this._input.value.length === this._length;
	}
	static get Default() {
		return Default$2;
	}
	static get DefaultType() {
		return DefaultType$2;
	}
	static get NAME() {
		return NAME$2;
	}
	getValue() {
		return this._input.value;
	}
	setValue(value) {
		this._input.value = this._sanitize(String(value));
		this._render();
		this._checkComplete();
	}
	clear() {
		this._input.value = "";
		this._render();
		this._input.focus();
	}
	focus() {
		this._pointerActive = false;
		this._input.focus();
		this._selectSlot(this._firstEmptyIndex());
		this._render();
	}
	dispose() {
		var _this$_slotsContainer;
		if (!this._input) {
			super.dispose();
			return;
		}
		this._input.removeEventListener("input", this._onInput);
		this._input.removeEventListener("beforeinput", this._onBeforeInput);
		this._input.removeEventListener("focus", this._onFocus);
		this._input.removeEventListener("pointerdown", this._onPointerDown);
		this._input.removeEventListener("compositionend", this._onCompositionEnd);
		for (const type of POINTER_END_EVENTS) this._input.removeEventListener(type, this._onPointerEnd);
		for (const type of SYNC_EVENTS) this._input.removeEventListener(type, this._onSync);
		document.removeEventListener("selectionchange", this._onSelectionChange);
		(_this$_slotsContainer = this._slotsContainer) === null || _this$_slotsContainer === void 0 || _this$_slotsContainer.remove();
		this._element.classList.remove(CLASS_NAME_RENDERED);
		super.dispose();
	}
	_resolveLength() {
		var _this$_input$getAttri;
		const candidates = [this._config.length, Number.parseInt((_this$_input$getAttri = this._input.getAttribute("maxlength")) !== null && _this$_input$getAttri !== void 0 ? _this$_input$getAttri : "", 10)];
		const { groups } = this._config;
		if (Array.isArray(groups) && groups.length > 0) candidates.push(groups.reduce((sum, group) => sum + Number(group), 0));
		for (const candidate of candidates) if (typeof candidate === "number" && Number.isInteger(candidate) && candidate > 0) return Math.min(candidate, MAX_LENGTH);
		return 6;
	}
	_setupInput() {
		const input = this._input;
		if (input.type !== "text" && input.type !== "tel") input.type = "text";
		input.classList.add(CLASS_NAME_INPUT);
		input.setAttribute("maxlength", String(this._length));
		input.setAttribute("inputmode", this._type.inputmode);
		input.setAttribute("pattern", this._type.pattern);
		if (!input.getAttribute("autocomplete")) input.setAttribute("autocomplete", "one-time-code");
		if (input.value) input.value = this._sanitize(input.value);
	}
	_renderSlots() {
		const container = document.createElement("div");
		container.className = CLASS_NAME_SLOTS;
		container.setAttribute("aria-hidden", "true");
		const { groups, separator } = this._config;
		let groupIndex = 0;
		let inGroup = 0;
		for (let i = 0; i < this._length; i++) {
			const slot = document.createElement("div");
			slot.className = CLASS_NAME_SLOT;
			container.append(slot);
			this._slots.push(slot);
			if (groups && groups.length > 0) {
				inGroup++;
				if (inGroup === groups[groupIndex] && i < this._length - 1) {
					const separatorEl = document.createElement("div");
					separatorEl.className = CLASS_NAME_SEPARATOR;
					separatorEl.textContent = separator;
					container.append(separatorEl);
					groupIndex = Math.min(groupIndex + 1, groups.length - 1);
					inGroup = 0;
				}
			}
		}
		this._slotsContainer = container;
		this._element.append(container);
		this._element.classList.add(CLASS_NAME_RENDERED);
	}
	_addEventListeners() {
		this._input.addEventListener("input", this._onInput);
		this._input.addEventListener("beforeinput", this._onBeforeInput);
		this._input.addEventListener("focus", this._onFocus);
		this._input.addEventListener("pointerdown", this._onPointerDown);
		this._input.addEventListener("compositionend", this._onCompositionEnd);
		for (const type of POINTER_END_EVENTS) this._input.addEventListener(type, this._onPointerEnd);
		document.addEventListener("selectionchange", this._onSelectionChange);
		for (const type of SYNC_EVENTS) this._input.addEventListener(type, this._onSync);
	}
	_handleFocus() {
		if (this._pointerActive) {
			this._pointerActive = false;
			this._selectSlot(this._pointerIndex);
			this._render();
			return;
		}
		this._selectSlot(this._firstEmptyIndex());
		this._render();
	}
	_handleInput() {
		const sanitized = this._sanitize(this._input.value);
		if (sanitized !== this._input.value) this._input.value = sanitized;
		if (document.activeElement === this._input) this._selectSlot(this._firstEmptyIndex());
		this._afterValueChange();
	}
	_handleBeforeInput(event) {
		const { inputType, data } = event;
		if (inputType === "insertText" && data && data.length === 1) {
			var _this$_input$selectio, _this$_input$selectio2;
			event.preventDefault();
			const char = this._sanitize(data);
			if (!char) return;
			const index = Math.min((_this$_input$selectio = this._input.selectionStart) !== null && _this$_input$selectio !== void 0 ? _this$_input$selectio : 0, this._length - 1);
			const end = Math.max((_this$_input$selectio2 = this._input.selectionEnd) !== null && _this$_input$selectio2 !== void 0 ? _this$_input$selectio2 : index, index + 1);
			const chars = [...this._input.value];
			chars.splice(index, end - index, char);
			this._input.value = chars.join("").slice(0, this._length);
			this._selectSlot(index + 1);
			this._afterValueChange();
			return;
		}
		if (inputType === "deleteContentBackward") {
			var _this$_input$selectio3, _this$_input$selectio4;
			event.preventDefault();
			const before = this._input.value;
			const start = (_this$_input$selectio3 = this._input.selectionStart) !== null && _this$_input$selectio3 !== void 0 ? _this$_input$selectio3 : 0;
			const end = (_this$_input$selectio4 = this._input.selectionEnd) !== null && _this$_input$selectio4 !== void 0 ? _this$_input$selectio4 : start;
			const chars = [...this._input.value];
			if (end > start) {
				chars.splice(start, end - start);
				this._input.value = chars.join("");
				this._selectSlot(start);
			} else if (start > 0) {
				chars.splice(start - 1, 1);
				this._input.value = chars.join("");
				this._selectSlot(start - 1);
			}
			this._afterValueChange(before);
			return;
		}
		if (inputType === "deleteContentForward") {
			var _this$_input$selectio5, _this$_input$selectio6;
			event.preventDefault();
			const before = this._input.value;
			const start = (_this$_input$selectio5 = this._input.selectionStart) !== null && _this$_input$selectio5 !== void 0 ? _this$_input$selectio5 : 0;
			const end = (_this$_input$selectio6 = this._input.selectionEnd) !== null && _this$_input$selectio6 !== void 0 ? _this$_input$selectio6 : start;
			const chars = [...this._input.value];
			if (end > start) {
				chars.splice(start, end - start);
				this._input.value = chars.join("");
			} else if (start < chars.length) {
				chars.splice(start, 1);
				this._input.value = chars.join("");
			}
			this._selectSlot(start);
			this._afterValueChange(before);
		}
	}
	_handlePointerDown(event) {
		const index = this._slotIndexFromPoint(event.clientX);
		if (index === null) return;
		const target = Math.min(index, this._firstEmptyIndex());
		if (document.activeElement === this._input) {
			event.preventDefault();
			this._selectSlot(target);
			this._render();
			return;
		}
		this._pointerActive = true;
		this._pointerIndex = target;
	}
	_slotIndexFromPoint(x) {
		const rtl = getComputedStyle(this._element).direction === "rtl";
		for (const [index, slot] of this._slots.entries()) {
			const rect = slot.getBoundingClientRect();
			if ((rtl ? x >= rect.left : x <= rect.right) || index === this._slots.length - 1) return index;
		}
		return null;
	}
	_afterValueChange(before) {
		this._render();
		if (before !== void 0 && before === this._input.value) return;
		EventHandler.trigger(this._element, EVENT_INPUT, { value: this._input.value });
		this._checkComplete();
	}
	_firstEmptyIndex() {
		return Math.min(this._input.value.length, this._length - 1);
	}
	_selectSlot(index) {
		const clamped = Math.max(0, Math.min(index, this._length - 1));
		const end = clamped < this._input.value.length ? clamped + 1 : clamped;
		this._input.setSelectionRange(clamped, end);
	}
	_sanitize(value) {
		return value.replace(this._type.filter, "").slice(0, this._length);
	}
	_render() {
		var _this$_input$selectio7;
		const { value } = this._input;
		const isFocused = document.activeElement === this._input;
		const caret = Math.min((_this$_input$selectio7 = this._input.selectionStart) !== null && _this$_input$selectio7 !== void 0 ? _this$_input$selectio7 : value.length, this._length - 1);
		for (const [index, slot] of this._slots.entries()) {
			var _value$index;
			const char = (_value$index = value[index]) !== null && _value$index !== void 0 ? _value$index : "";
			slot.textContent = char && this._config.mask ? MASK_CHARACTER : char;
			slot.classList.toggle(CLASS_NAME_SLOT_FILLED, Boolean(char));
			slot.classList.toggle(CLASS_NAME_SLOT_ACTIVE, isFocused && index === caret);
		}
	}
	_checkComplete() {
		const { value } = this._input;
		const complete = value.length === this._length;
		if (complete && !this._wasComplete) EventHandler.trigger(this._element, EVENT_COMPLETE, { value });
		this._wasComplete = complete;
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_TOGGLE$1, OtpInput);
//#endregion
//#region js/src/sparkline.ts
/**
* --------------------------------------------------------------------------
* Tabler sparkline.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME$1 = "sparkline";
var EVENT_KEY = `.${`bs.${NAME$1}`}`;
var EVENT_RENDERED = `rendered${EVENT_KEY}`;
var EVENT_UPDATED = `updated${EVENT_KEY}`;
var CLASS_NAME_SVG = "sparkline-svg";
var CLASS_NAME_LABEL = "sparkline-label";
var SELECTOR_DATA_TOGGLE = `[data-bs-toggle="${NAME$1}"], [data-tblr-toggle="${NAME$1}"]`;
var CSS_VAR_PREFIX = "--tblr-sparkline";
var SVG_NS = "http://www.w3.org/2000/svg";
var Default$1 = {
	type: "line",
	values: [],
	width: 80,
	height: 24,
	min: null,
	max: null,
	fill: "none",
	spot: "none",
	pad: 2,
	barGap: 2,
	barRadius: 2,
	label: null,
	threshold: null,
	animation: 300
};
var DefaultType$1 = {
	type: "string",
	values: "array",
	width: "number",
	height: "number",
	min: "(number|null)",
	max: "(number|null)",
	fill: "string",
	spot: "string",
	pad: "number",
	barGap: "number",
	barRadius: "number",
	label: "(string|number|boolean|null)",
	threshold: "(number|null)",
	animation: "number"
};
/**
* Helpers
*/
var svgEl = (tag) => document.createElementNS(SVG_NS, tag);
var setAttr = (el, attrs) => {
	for (const [key, value] of Object.entries(attrs)) if (value !== null && value !== void 0) el.setAttribute(key, String(value));
};
var cssVar = (name) => `var(${CSS_VAR_PREFIX}-${name})`;
var clampSpan = (min, max) => {
	const span = max - min;
	return span === 0 ? 1 : span;
};
var parseNumberList = (input) => {
	const raw = String(input !== null && input !== void 0 ? input : "").trim();
	if (!raw) return [];
	if (raw.startsWith("[")) try {
		const arr = JSON.parse(raw);
		return Array.isArray(arr) ? arr.map(Number).filter(Number.isFinite) : [];
	} catch (_unused) {
		return [];
	}
	return raw.split(",").map((value) => Number(value.trim())).filter(Number.isFinite);
};
var toValues = (input) => {
	if (Array.isArray(input)) return input.map(Number).filter(Number.isFinite);
	if (typeof input === "number") return Number.isFinite(input) ? [input] : [];
	return parseNumberList(input);
};
var rangeOf = (values, minForced, maxForced, ...include) => {
	const min = minForced !== null && minForced !== void 0 ? minForced : Math.min(...values, ...include);
	const max = maxForced !== null && maxForced !== void 0 ? maxForced : Math.max(...values, ...include);
	return {
		min,
		max,
		span: clampSpan(min, max)
	};
};
var NUMBER_RE = /-?\d*\.?\d+(?:e[-+]?\d+)?/g;
var lerpAttr = (from, to, t) => {
	const a = from.match(NUMBER_RE);
	const b = to.match(NUMBER_RE);
	if (!a || !b || a.length !== b.length || from.replace(NUMBER_RE, "#") !== to.replace(NUMBER_RE, "#")) return to;
	let i = 0;
	return to.replace(NUMBER_RE, () => {
		const value = Number(a[i]) + (Number(b[i]) - Number(a[i])) * t;
		i++;
		return String(Math.round(value * 1e3) / 1e3);
	});
};
var easeOut = (t) => 1 - Math.pow(1 - t, 3);
var findIndexByMode = (values, mode) => {
	if (values.length === 0 || mode === "none") return -1;
	if (mode === "last") return values.length - 1;
	let idx = 0;
	for (let i = 1; i < values.length; i++) if (mode === "min" ? values[i] < values[idx] : values[i] > values[idx]) idx = i;
	return idx;
};
var getCssNumber = (element, name, fallback) => {
	const raw = getComputedStyle(element).getPropertyValue(`${CSS_VAR_PREFIX}-${name}`).trim();
	const value = Number.parseFloat(raw);
	return Number.isFinite(value) ? value : fallback;
};
/**
* Class definition
*
* Tiny inline SVG chart (line, bar or circle) rendered from data attributes:
*
*   <span class="sparkline" data-bs-toggle="sparkline" data-bs-type="line" data-bs-values="3,4,2,6,5,8,7"></span>
*
* Colours and stroke widths come from the `--tblr-sparkline-*` custom
* properties, so a text colour utility on the element themes the chart.
*/
var Sparkline = class extends BaseComponent {
	constructor(element, config) {
		super(element, config);
		this._userConfig = {};
		this._frame = 0;
		if (!this._element) return;
		this._userConfig = _objectSpread2({}, config);
		this.render();
	}
	static get Default() {
		return Default$1;
	}
	static get DefaultType() {
		return DefaultType$1;
	}
	static get NAME() {
		return NAME$1;
	}
	update(values) {
		const serialized = Array.isArray(values) ? values.join(",") : String(values);
		const attribute = this._element.hasAttribute("data-tblr-values") ? "data-tblr-values" : "data-bs-values";
		this._element.setAttribute(attribute, serialized);
		delete this._userConfig.values;
		this.render();
		EventHandler.trigger(this._element, EVENT_UPDATED);
	}
	render() {
		var _this$_element$queryS;
		this._config = this._getConfig(this._userConfig);
		const previous = this._element.querySelector("svg");
		(_this$_element$queryS = this._element.querySelector(`.${CLASS_NAME_LABEL}`)) === null || _this$_element$queryS === void 0 || _this$_element$queryS.remove();
		if (this._config.values.length === 0) {
			cancelAnimationFrame(this._frame);
			previous === null || previous === void 0 || previous.remove();
			EventHandler.trigger(this._element, EVENT_RENDERED);
			return;
		}
		let svg;
		switch (this._config.type) {
			case "bar":
				svg = this._renderBars();
				break;
			case "tristate":
				svg = this._renderTristate();
				break;
			case "circle":
				svg = this._renderCircle();
				break;
			default: svg = this._renderLine();
		}
		if (previous && this._canAnimate(previous, svg)) this._animate(previous, svg);
		else {
			cancelAnimationFrame(this._frame);
			previous === null || previous === void 0 || previous.remove();
			this._element.append(svg);
		}
		const text = this._labelText();
		if (text !== "") {
			const label = document.createElement("span");
			label.className = CLASS_NAME_LABEL;
			label.textContent = text;
			this._element.append(label);
		}
		EventHandler.trigger(this._element, EVENT_RENDERED);
	}
	dispose() {
		var _this$_element$queryS2, _this$_element$queryS3;
		cancelAnimationFrame(this._frame);
		(_this$_element$queryS2 = this._element.querySelector("svg")) === null || _this$_element$queryS2 === void 0 || _this$_element$queryS2.remove();
		(_this$_element$queryS3 = this._element.querySelector(`.${CLASS_NAME_LABEL}`)) === null || _this$_element$queryS3 === void 0 || _this$_element$queryS3.remove();
		super.dispose();
	}
	_configAfterMerge(config) {
		config.values = toValues(config.values);
		return config;
	}
	_labelText() {
		const { label, values, type } = this._config;
		if (label === null || label === false || label === "") return "";
		if (label !== "auto" && label !== true) return String(label);
		if (type === "circle") return `${Math.round(this._circleRatio() * 100)}%`;
		return String(values[values.length - 1]);
	}
	_circleRatio() {
		var _cfg$values$, _cfg$max, _cfg$min;
		const cfg = this._config;
		const value = (_cfg$values$ = cfg.values[0]) !== null && _cfg$values$ !== void 0 ? _cfg$values$ : 0;
		const max = (_cfg$max = cfg.max) !== null && _cfg$max !== void 0 ? _cfg$max : cfg.values.length > 1 ? cfg.values[1] : 100;
		const min = (_cfg$min = cfg.min) !== null && _cfg$min !== void 0 ? _cfg$min : 0;
		return Math.max(0, Math.min(1, (value - min) / clampSpan(min, max)));
	}
	_canAnimate(from, to) {
		if (this._config.animation <= 0 || window.matchMedia("(prefers-reduced-motion: reduce)").matches) return false;
		if (from.getAttribute("viewBox") !== to.getAttribute("viewBox") || from.children.length !== to.children.length) return false;
		return Array.from(from.children).every((child, i) => child.tagName === to.children[i].tagName);
	}
	_animate(from, to) {
		cancelAnimationFrame(this._frame);
		const pairs = Array.from(to.children).map((target, i) => {
			const source = from.children[i];
			const tweens = [];
			for (const { name } of Array.from(source.attributes)) if (!target.hasAttribute(name)) source.removeAttribute(name);
			for (const { name, value } of Array.from(target.attributes)) {
				const start = source.getAttribute(name);
				if (start === null || start === value) source.setAttribute(name, value);
				else tweens.push([
					name,
					start,
					value
				]);
			}
			return {
				source,
				tweens
			};
		});
		const duration = this._config.animation;
		const started = performance.now();
		const step = (now) => {
			const t = easeOut(Math.min(1, (now - started) / duration));
			for (const { source, tweens } of pairs) for (const [name, start, end] of tweens) source.setAttribute(name, t >= 1 ? end : lerpAttr(start, end, t));
			if (t < 1) this._frame = requestAnimationFrame(step);
		};
		this._frame = requestAnimationFrame(step);
	}
	_hairline(svg, y, color, dashed = false) {
		const line = svgEl("line");
		setAttr(line, {
			"x1": 0,
			"x2": this._config.width,
			"y1": y,
			"y2": y,
			"stroke": cssVar(color),
			"stroke-width": 1,
			"stroke-dasharray": dashed ? "3 2" : null,
			"vector-effect": "non-scaling-stroke"
		});
		svg.append(line);
	}
	_createSvg() {
		const { width, height } = this._config;
		const svg = svgEl("svg");
		setAttr(svg, {
			"viewBox": `0 0 ${width} ${height}`,
			"preserveAspectRatio": "none",
			"width": width,
			"height": height,
			"class": CLASS_NAME_SVG,
			"aria-hidden": "true"
		});
		return svg;
	}
	_renderLine() {
		const cfg = this._config;
		const svg = this._createSvg();
		const { min, span } = rangeOf(cfg.values, cfg.min, cfg.max, ...cfg.threshold === null ? [] : [cfg.threshold]);
		const yOf = (value) => cfg.pad + (1 - (value - min) / span) * (cfg.height - cfg.pad * 2);
		const ys = cfg.values.length === 1 ? [yOf(cfg.values[0]), yOf(cfg.values[0])] : cfg.values.map(yOf);
		const step = cfg.width / (ys.length - 1);
		if (cfg.threshold !== null) this._hairline(svg, yOf(cfg.threshold), "threshold", true);
		if (cfg.fill === "auto") {
			const area = svgEl("path");
			setAttr(area, {
				d: this._areaPath(ys, step, cfg.height),
				fill: cssVar("fill"),
				stroke: "none"
			});
			svg.append(area);
		}
		const line = svgEl("polyline");
		setAttr(line, {
			"points": ys.map((y, i) => `${i * step},${y}`).join(" "),
			"fill": "none",
			"stroke": cssVar("stroke"),
			"stroke-width": cssVar("stroke-width"),
			"stroke-linecap": "round",
			"stroke-linejoin": "round"
		});
		svg.append(line);
		const spotIndex = findIndexByMode(cfg.values, cfg.spot);
		if (spotIndex >= 0) {
			const spot = svgEl("circle");
			setAttr(spot, {
				cx: cfg.values.length === 1 ? cfg.width / 2 : spotIndex * step,
				cy: ys[spotIndex],
				r: getCssNumber(this._element, "spot-size", 2),
				fill: cssVar("spot")
			});
			svg.append(spot);
		}
		return svg;
	}
	_areaPath(ys, step, height) {
		return `${ys.map((y, i) => `${i === 0 ? "M" : "L"} ${i * step} ${y}`).join(" ")} L ${(ys.length - 1) * step} ${height} L 0 ${height} Z`;
	}
	_renderBars() {
		const cfg = this._config;
		const svg = this._createSvg();
		const { min, span } = rangeOf(cfg.values, cfg.min, cfg.max, 0, ...cfg.threshold === null ? [] : [cfg.threshold]);
		const yOf = (value) => cfg.height - (value - min) / span * cfg.height;
		const count = cfg.values.length;
		const barWidth = (cfg.width - cfg.barGap * (count - 1)) / count;
		const zeroY = yOf(0);
		if (min < 0) this._hairline(svg, zeroY, "zero");
		if (cfg.threshold !== null) this._hairline(svg, yOf(cfg.threshold), "threshold", true);
		const clip = (y) => Math.max(0, Math.min(cfg.height, y));
		cfg.values.forEach((value, i) => {
			const top = clip(yOf(Math.max(value, 0)));
			const bottom = clip(yOf(Math.min(value, 0)));
			const below = cfg.threshold === null ? value < 0 : value < cfg.threshold;
			const bar = svgEl("rect");
			setAttr(bar, {
				x: i * (barWidth + cfg.barGap),
				y: top,
				width: barWidth,
				height: bottom - top,
				rx: cfg.barRadius,
				fill: cssVar(below ? "negative" : "stroke")
			});
			svg.append(bar);
		});
		return svg;
	}
	_renderTristate() {
		const cfg = this._config;
		const svg = this._createSvg();
		const count = cfg.values.length;
		const barWidth = (cfg.width - cfg.barGap * (count - 1)) / count;
		const zeroY = cfg.height / 2;
		const tick = Math.min(2, zeroY);
		this._hairline(svg, zeroY, "zero");
		cfg.values.forEach((value, i) => {
			const bar = svgEl("rect");
			const attrs = value > 0 ? {
				y: 0,
				height: zeroY,
				fill: "stroke"
			} : value < 0 ? {
				y: zeroY,
				height: zeroY,
				fill: "negative"
			} : {
				y: zeroY - tick / 2,
				height: tick,
				fill: "track"
			};
			setAttr(bar, {
				x: i * (barWidth + cfg.barGap),
				y: attrs.y,
				width: barWidth,
				height: attrs.height,
				rx: cfg.barRadius,
				fill: cssVar(attrs.fill)
			});
			svg.append(bar);
		});
		return svg;
	}
	_renderCircle() {
		const cfg = this._config;
		const svg = this._createSvg();
		const strokeWidth = getCssNumber(this._element, "stroke-width", 2);
		const radius = Math.max(0, Math.min(cfg.width, cfg.height) / 2 - strokeWidth / 2);
		const cx = cfg.width / 2;
		const cy = cfg.height / 2;
		const ratio = this._circleRatio();
		const circumference = 2 * Math.PI * radius;
		const track = svgEl("circle");
		setAttr(track, {
			"cx": cx,
			"cy": cy,
			"r": radius,
			"fill": "none",
			"stroke": cssVar("track"),
			"stroke-width": strokeWidth
		});
		svg.append(track);
		const ring = svgEl("circle");
		setAttr(ring, {
			"cx": cx,
			"cy": cy,
			"r": radius,
			"fill": "none",
			"stroke": cssVar("stroke"),
			"stroke-width": strokeWidth,
			"stroke-linecap": "round",
			"stroke-dasharray": circumference,
			"stroke-dashoffset": circumference * (1 - ratio),
			"transform": `rotate(-90 ${cx} ${cy})`
		});
		svg.append(ring);
		return svg;
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_TOGGLE, Sparkline);
//#endregion
//#region js/src/strength.ts
/**
* --------------------------------------------------------------------------
* Tabler strength.ts
* Licensed under MIT (https://github.com/tabler/tabler/blob/dev/LICENSE)
* --------------------------------------------------------------------------
*/
/**
* Constants
*/
var NAME = "strength";
var EVENT_CHANGE = `change${`.${`bs.${NAME}`}`}`;
var CLASS_NAME_ACTIVE = "active";
var SELECTOR_DATA_STRENGTH = `[data-bs-${NAME}], [data-tblr-${NAME}]`;
var SELECTOR_SEGMENT = `.${NAME}-segment`;
var SELECTOR_TEXT = `.${NAME}-text`;
var SELECTOR_PASSWORD = "input[type=\"password\"]";
var LEVELS = [
	"weak",
	"fair",
	"good",
	"strong"
];
var Default = {
	input: null,
	minLength: 8,
	messages: {
		weak: "Weak",
		fair: "Fair",
		good: "Good",
		strong: "Strong"
	},
	weights: {
		minLength: 1,
		extraLength: 1,
		longPassword: 1,
		lowercase: 1,
		uppercase: 1,
		numbers: 1,
		special: 1,
		multipleSpecial: 1
	},
	thresholds: [
		2,
		4,
		6
	],
	scorer: null
};
var DefaultType = {
	input: "(string|null)",
	minLength: "number",
	messages: "object",
	weights: "object",
	thresholds: "array",
	scorer: "(function|null)"
};
/**
* Class definition
*
* Rates the password typed in a field and fills a segmented meter. The score
* is a hint for the user, never a validation: check the password on the
* server as well.
*/
var Strength = class extends BaseComponent {
	constructor(element, config) {
		var _this$_text$textConte, _this$_text;
		super(element, config);
		this._input = null;
		this._segments = [];
		this._text = null;
		this._emptyText = "";
		this._level = null;
		this._onInput = () => this.evaluate();
		if (!this._element) return;
		this._input = this._getInput();
		this._segments = SelectorEngine.find(SELECTOR_SEGMENT, this._element);
		this._text = this._getText();
		this._emptyText = (_this$_text$textConte = (_this$_text = this._text) === null || _this$_text === void 0 || (_this$_text = _this$_text.textContent) === null || _this$_text === void 0 ? void 0 : _this$_text.trim()) !== null && _this$_text$textConte !== void 0 ? _this$_text$textConte : "";
		this._setUpAria();
		if (!this._input) return;
		this._input.addEventListener("input", this._onInput);
		this.evaluate();
	}
	static get Default() {
		return Default;
	}
	static get DefaultType() {
		return DefaultType;
	}
	static get NAME() {
		return NAME;
	}
	get level() {
		return this._level;
	}
	evaluate() {
		if (!this._input) return;
		const password = this._input.value;
		const score = this._score(password);
		const level = this._level_(score);
		if (level === this._level) return;
		this._level = level;
		this._render(level);
		EventHandler.trigger(this._element, EVENT_CHANGE, {
			strength: level,
			score
		});
	}
	dispose() {
		var _this$_input;
		(_this$_input = this._input) === null || _this$_input === void 0 || _this$_input.removeEventListener("input", this._onInput);
		super.dispose();
	}
	_configAfterMerge(config) {
		return _objectSpread2(_objectSpread2({}, config), {}, {
			messages: _objectSpread2(_objectSpread2({}, Default.messages), config.messages),
			weights: _objectSpread2(_objectSpread2({}, Default.weights), config.weights)
		});
	}
	_getInput() {
		var _ref, _precedingFields;
		const { input } = this._config;
		if (input) return getElement(input);
		const parent = this._element.parentElement;
		const fields = parent ? SelectorEngine.find(SELECTOR_PASSWORD, parent) : [];
		const precedingFields = fields.filter((field) => Boolean(field.compareDocumentPosition(this._element) & Node.DOCUMENT_POSITION_FOLLOWING));
		return (_ref = (_precedingFields = precedingFields[precedingFields.length - 1]) !== null && _precedingFields !== void 0 ? _precedingFields : fields[0]) !== null && _ref !== void 0 ? _ref : null;
	}
	_getText() {
		const parent = this._element.parentElement;
		return parent ? SelectorEngine.findOne(SELECTOR_TEXT, parent) : null;
	}
	_setUpAria() {
		var _this$_text2;
		const element = this._element;
		element.setAttribute("role", "progressbar");
		element.setAttribute("aria-valuemin", "0");
		element.setAttribute("aria-valuemax", String(LEVELS.length));
		element.setAttribute("aria-valuenow", "0");
		if (!element.hasAttribute("aria-label") && !element.hasAttribute("aria-labelledby")) element.setAttribute("aria-label", "Password strength");
		for (const segment of this._segments) segment.setAttribute("aria-hidden", "true");
		(_this$_text2 = this._text) === null || _this$_text2 === void 0 || _this$_text2.setAttribute("aria-live", "polite");
	}
	_score(password) {
		var _password$match;
		if (!password) return 0;
		const { scorer, weights, minLength } = this._config;
		if (typeof scorer === "function") return scorer(password);
		return [
			[password.length >= minLength, weights.minLength],
			[password.length >= minLength + 4, weights.extraLength],
			[password.length >= 16, weights.longPassword],
			[/[a-z]/.test(password), weights.lowercase],
			[/[A-Z]/.test(password), weights.uppercase],
			[/\d/.test(password), weights.numbers],
			[/[^\dA-Za-z]/.test(password), weights.special],
			[((_password$match = password.match(/[^\dA-Za-z]/g)) !== null && _password$match !== void 0 ? _password$match : []).length > 1, weights.multipleSpecial]
		].reduce((score, [passed, weight]) => score + (passed ? weight !== null && weight !== void 0 ? weight : 0 : 0), 0);
	}
	_level_(score) {
		var _LEVELS;
		if (score <= 0) return null;
		const index = this._config.thresholds.findIndex((threshold) => score <= threshold);
		return (_LEVELS = LEVELS[index === -1 ? LEVELS.length - 1 : index]) !== null && _LEVELS !== void 0 ? _LEVELS : null;
	}
	_render(level) {
		const element = this._element;
		const index = level ? LEVELS.indexOf(level) : -1;
		element.dataset.bsStrength = level !== null && level !== void 0 ? level : "";
		element.setAttribute("aria-valuenow", String(index + 1));
		if (level) {
			var _this$_config$message;
			element.setAttribute("aria-valuetext", (_this$_config$message = this._config.messages[level]) !== null && _this$_config$message !== void 0 ? _this$_config$message : level);
		} else element.removeAttribute("aria-valuetext");
		const filled = level ? Math.ceil((index + 1) / LEVELS.length * this._segments.length) : 0;
		for (const [position, segment] of this._segments.entries()) segment.classList.toggle(CLASS_NAME_ACTIVE, position < filled);
		if (this._text) {
			var _this$_config$message2;
			this._text.textContent = level ? (_this$_config$message2 = this._config.messages[level]) !== null && _this$_config$message2 !== void 0 ? _this$_config$message2 : "" : this._emptyText;
			this._text.dataset.bsStrength = level !== null && level !== void 0 ? level : "";
		}
	}
};
/**
* Data API implementation
*/
initAll(SELECTOR_DATA_STRENGTH, Strength);
//#endregion
//#region js/src/deprecated.ts
var deprecated_exports = /* @__PURE__ */ __exportAll({
	getColor: () => getColor,
	hexToRgba: () => hexToRgba,
	prefix: () => prefix
});
/**
* Helpers kept only so that projects written for an older 1.x release keep
* working. They are exported as the `tabler` namespace: `tabler.tabler.getColor()`
* from the bundle, `import { tabler } from '@tabler/core'` from the module.
*
* Removed in 2.0, together with this file. Read the custom property yourself,
* and mix the opacity in with `color-mix()`.
*/
var prefix = "tblr-";
var hexToRgba = (hex, opacity) => {
	const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
	return result ? `rgba(${parseInt(result[1], 16)}, ${parseInt(result[2], 16)}, ${parseInt(result[3], 16)}, ${opacity})` : null;
};
var getColor = (color, opacity = 1) => {
	var _hexToRgba;
	const c = getComputedStyle(document.body).getPropertyValue(`--${prefix}${color}`).trim();
	if (opacity === 1) return c;
	return (_hexToRgba = hexToRgba(c, opacity)) !== null && _hexToRgba !== void 0 ? _hexToRgba : c ? `color-mix(in srgb, ${c} ${opacity * 100}%, transparent)` : null;
};
//#endregion
export { Alert, Autosize, Button, Carousel, Clipboard, Collapse, Confetti, CountUp, Datepicker, Dropdown, InputMask, Modal, Offcanvas, OtpInput, Popover, lib_exports as Popper, ScrollSpy, Sortable, Sparkline, Strength, SwitchIcon, Tab, Toast, Tooltip, bootstrap, deprecated_exports as tabler };

