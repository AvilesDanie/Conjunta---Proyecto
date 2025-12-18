const { JSDOM } = require('jsdom');
const fs = require('fs');
const jsp = fs.readFileSync('src\\main\\webapp\\WEB-INF\\views\\electroquito\\electroquitoFacturar.jsp', 'utf8');
const match = jsp.match(/<script>([\s\S]*)<\/script>/);
if (!match) {
  throw new Error('Script block not found');
}
const pageScript = match[1];
const html = '<!DOCTYPE html>' +
  '<html>' +
  '<body>' +
  '<div id="productModal">' +
  '<button type="button" class="eq-product-card" data-product-id="1" data-product-name="Prod A" data-product-code="REF-001" data-product-price="100" data-product-image=""></button>' +
  '<button type="button" class="eq-product-card" data-product-id="2" data-product-name="Prod B" data-product-code="REF-002" data-product-price="50" data-product-image=""></button>' +
  '</div>' +
  '<button id="selectProductButton"></button>' +
  '<button id="closeProductModal"></button>' +
  '<div id="selectedProductEmpty"></div>' +
  '<div id="selectedProductDetails" hidden>' +
  '<div id="selectedProductThumb">' +
  '<span id="selectedProductThumbIcon"></span>' +
  '<img id="selectedProductThumbImg" />' +
  '</div>' +
  '<p id="selectedProductName"></p>' +
  '<p id="selectedProductCode"></p>' +
  '<p id="selectedProductPrice"></p>' +
  '</div>' +
  '<input id="productoIdInput" value="" />' +
  '<input id="cantidadInput" value="1" />' +
  '<button id="addProductButton" disabled></button>' +
  '<div id="cartItemsList"></div>' +
  '<p id="cartEmptyMessage"></p>' +
  '<input id="itemsStateInput" value="[]" />' +
  '<form id="facturaForm" data-has-success="false"></form>' +
  '</body>' +
  '</html>';
const dom = new JSDOM(html, { runScripts: 'dangerously', resources: 'usable', pretendToBeVisual: true });
dom.window.alert = (...args) => console.log('alert', ...args);
Object.defineProperty(dom.window.document, 'readyState', { value: 'complete', configurable: true });
dom.window.eval(pageScript);
dom.window.document.dispatchEvent(new dom.window.Event('DOMContentLoaded'));
const cards = dom.window.document.querySelectorAll('.eq-product-card');
if (cards.length) {
  cards[0].dispatchEvent(new dom.window.Event('click', { bubbles: true }));
  console.log('productoIdInput value:', dom.window.document.getElementById('productoIdInput').value);
  console.log('add button disabled?', dom.window.document.getElementById('addProductButton').disabled);
}
